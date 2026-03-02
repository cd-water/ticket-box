package com.cdwater.movietimeorder.service.impl;

import com.cdwater.movietimecinema.mapper.ScreeningMapper;
import com.cdwater.movietimecinema.model.entity.Screening;
import com.cdwater.movietimecinema.model.vo.SeatVO;
import com.cdwater.movietimecommon.constants.RedisConstant;
import com.cdwater.movietimecommon.constants.TextConstant;
import com.cdwater.movietimecommon.enums.RetEnum;
import com.cdwater.movietimecommon.exceptions.BusinessException;
import com.cdwater.movietimecommon.utils.SnowflakeIdGenerator;
import com.cdwater.movietimecommon.utils.UserContext;
import com.cdwater.movietimeorder.mapper.OrderMapper;
import com.cdwater.movietimeorder.model.dto.OrderPageDTO;
import com.cdwater.movietimeorder.model.dto.OrderPlaceDTO;
import com.cdwater.movietimeorder.model.entity.Order;
import com.cdwater.movietimeorder.model.vo.OrderListVO;
import com.cdwater.movietimeorder.model.vo.OrderPageVO;
import com.cdwater.movietimeorder.model.vo.OrderPlaceVO;
import com.cdwater.movietimeorder.mq.OrderProducer;
import com.cdwater.movietimeorder.service.OrderService;
import com.cdwater.movietimeorder.service.PayService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private ScreeningMapper screeningMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private static final DefaultRedisScript<Long> SEAT_SCRIPT;

    static {
        SEAT_SCRIPT = new DefaultRedisScript<>();
        SEAT_SCRIPT.setLocation(new ClassPathResource("lua/seat.lua"));
        SEAT_SCRIPT.setResultType(Long.class);
    }

    @Resource
    private SnowflakeIdGenerator snowflakeIdGenerator;

    @Resource
    private OrderProducer orderProducer;

    @Resource
    private PayService payService;

    @Override
    public PageInfo<OrderPageVO> page(OrderPageDTO orderPageDTO) {
        PageHelper.startPage(orderPageDTO.getPageNum(), orderPageDTO.getPageSize());
        List<OrderPageVO> list = orderMapper.selectPage(orderPageDTO);
        return PageInfo.of(list);
    }

    @Override
    public Long count() {
        return orderMapper.count();
    }

    @Override
    public List<OrderListVO> list() {
        //用户id
        Long userId = UserContext.getId();
        //mysql查询订单列表
        List<OrderListVO> list = orderMapper.selectByUserId(userId);
        //解析座位信息：1_2,1_3 -> 1排2座 1排3座
        list.forEach(item -> {
            String seatText = Arrays.stream(item.getSeatInfo().split(","))
                    .map(seat -> {
                        String[] rowAndCol = seat.split("_");
                        return rowAndCol[0] + "排" + rowAndCol[1] + "座";
                    }).collect(Collectors.joining(" "));
            item.setSeatInfo(seatText);
        });
        return list;
    }

    @Override
    public OrderPlaceVO place(OrderPlaceDTO orderPlaceDTO) {
        Long screeningId = orderPlaceDTO.getScreeningId();
        Screening screening = screeningMapper.selectById(screeningId);
        //获取bitmap偏移量
        List<SeatVO> placeSeats = orderPlaceDTO.getPlaceSeats();
        List<Long> offsets = new ArrayList<>();
        for (SeatVO seat : placeSeats) {
            int row = seat.getRow();
            int col = seat.getCol();
            long offset = (long) screening.getColCount() * (row - 1) + (col - 1);
            offsets.add(offset);
        }
        //lua脚本锁定座位
        String key = RedisConstant.SEAT_SCREENING + ":" + screeningId;
        Long result = redisTemplate.execute(
                SEAT_SCRIPT,
                List.of(key),
                offsets.toArray());
        if (result == 1L) {
            //座位已锁定
            throw new BusinessException(RetEnum.SEAT_LOCKED);
        }
        //创建订单
        Order order = Order.builder()
                .orderNo(snowflakeIdGenerator.nextId())
                .userId(UserContext.getId())
                .screeningId(screeningId)
                .seatInfo(placeSeats.stream().map(seat -> seat.getRow() + "_" + seat.getCol()).collect(Collectors.joining(",")))
                .ticketCount(placeSeats.size())
                .amount(screening.getPrice().multiply(BigDecimal.valueOf(placeSeats.size())))
                .status(TextConstant.PENDING_PAYMENT)
                .build();
        orderMapper.insert(order);
        //10分钟后延迟队列释放锁定座位并取消订单
        orderProducer.releaseSeatAndCancelOrder(order.getOrderNo());
        //返回下单信息
        OrderPlaceVO placeOrderVO = new OrderPlaceVO();
        placeOrderVO.setOrderNo(order.getOrderNo());
        placeOrderVO.setAmount(order.getAmount());
        placeOrderVO.setExpireTime(LocalDateTime.now().plusMinutes(TextConstant.ORDER_TTL));
        return placeOrderVO;
    }

    @Override
    public void pay(Long orderNo) {
        //mysql查询订单是否存在
        Order order = orderMapper.selectByOrderNo(orderNo);
        //订单不存在
        if (order == null) {
            throw new BusinessException(RetEnum.NOT_FOUND);
        }
        //本人操作判断
        if (!Objects.equals(order.getUserId(), UserContext.getId())) {
            throw new BusinessException(RetEnum.FORBIDDEN);
        }
        //调用支付服务
        if (payService.pay()) {
            //更新订单状态
            if (orderMapper.updateStatus(orderNo, TextConstant.PAID) == 0) {
                //初始状态非待支付，修改失败
                throw new BusinessException(RetEnum.FORBIDDEN);
            }
        } else {
            throw new BusinessException(RetEnum.PAY_FAIL);
        }
    }

    @Override
    public void cancel(Long orderNo) {
        //mysql查询订单是否存在
        Order order = orderMapper.selectByOrderNo(orderNo);
        //订单不存在
        if (order == null) {
            throw new BusinessException(RetEnum.NOT_FOUND);
        }
        //本人操作判断
        if (!Objects.equals(order.getUserId(), UserContext.getId())) {
            throw new BusinessException(RetEnum.FORBIDDEN);
        }
        //更新订单状态
        if (orderMapper.updateStatus(orderNo, TextConstant.CANCELLED) == 0) {
            //初始状态非待支付，修改失败
            throw new BusinessException(RetEnum.FORBIDDEN);
        }
        //释放锁定座位
        Long screeningId = order.getScreeningId();
        Screening screening = screeningMapper.selectById(screeningId);
        String key = RedisConstant.SEAT_SCREENING + ":" + screeningId;
        //解析座位信息
        String seatInfo = order.getSeatInfo();
        String[] seats = seatInfo.split(",");
        for (String seat : seats) {
            String[] rowAndCol = seat.split("_");
            int row = Integer.parseInt(rowAndCol[0]);
            int col = Integer.parseInt(rowAndCol[1]);
            long offset = (long) screening.getColCount() * (row - 1) + (col - 1);
            redisTemplate.opsForValue().setBit(key, offset, false);
        }
    }

    @Override
    public boolean cancelMQ(Long orderNo) {
        //mysql查询订单是否存在
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null || orderMapper.updateStatus(orderNo, TextConstant.CANCELLED) == 0) {
            //订单不存在 | 初始状态非待支付，修改失败
            return false;
        }
        //释放锁定座位
        Long screeningId = order.getScreeningId();
        Screening screening = screeningMapper.selectById(screeningId);
        String key = RedisConstant.SEAT_SCREENING + ":" + screeningId;
        //解析座位信息
        String seatInfo = order.getSeatInfo();
        String[] seats = seatInfo.split(",");
        for (String seat : seats) {
            String[] rowAndCol = seat.split("_");
            int row = Integer.parseInt(rowAndCol[0]);
            int col = Integer.parseInt(rowAndCol[1]);
            long offset = (long) screening.getColCount() * (row - 1) + (col - 1);
            redisTemplate.opsForValue().setBit(key, offset, false);
        }
        return true;
    }
}
