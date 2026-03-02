package com.cdwater.movietimecinema.service.impl;

import com.cdwater.movietimecinema.mapper.ScreeningMapper;
import com.cdwater.movietimecinema.model.dto.ScreeningAddDTO;
import com.cdwater.movietimecinema.model.dto.ScreeningPageDTO;
import com.cdwater.movietimecinema.model.dto.ScreeningUpdateDTO;
import com.cdwater.movietimecinema.model.entity.Screening;
import com.cdwater.movietimecinema.model.vo.*;
import com.cdwater.movietimecinema.service.ScreeningService;
import com.cdwater.movietimecommon.constants.RedisConstant;
import com.cdwater.movietimecommon.enums.RetEnum;
import com.cdwater.movietimecommon.exceptions.BusinessException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class ScreeningServiceImpl implements ScreeningService {

    @Resource
    private ScreeningMapper screeningMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void add(ScreeningAddDTO screeningAddDTO) {
        Screening screening = Screening.builder()
                .movieId(screeningAddDTO.getMovieId())
                .cinemaId(screeningAddDTO.getCinemaId())
                .hallName(screeningAddDTO.getHallName())
                .rowCount(screeningAddDTO.getRowCount())
                .colCount(screeningAddDTO.getColCount())
                .startTime(screeningAddDTO.getStartTime())
                .endTime(screeningAddDTO.getEndTime())
                .price(screeningAddDTO.getPrice())
                .build();
        screeningMapper.insert(screening);
    }

    @Override
    public void delete(Long id) {
        screeningMapper.delete(id);
    }

    @Override
    public void update(ScreeningUpdateDTO screeningUpdateDTO) {
        Screening screening = Screening.builder()
                .id(screeningUpdateDTO.getId())
                .movieId(screeningUpdateDTO.getMovieId())
                .hallName(screeningUpdateDTO.getHallName())
                .rowCount(screeningUpdateDTO.getRowCount())
                .colCount(screeningUpdateDTO.getColCount())
                .startTime(screeningUpdateDTO.getStartTime())
                .endTime(screeningUpdateDTO.getEndTime())
                .price(screeningUpdateDTO.getPrice())
                .build();
        screeningMapper.updateById(screening);
    }

    @Override
    public ScreeningQueryVO query(Long id) {
        Screening screening = screeningMapper.selectById(id);
        if (screening == null) {
            //场次不存在
            throw new BusinessException(RetEnum.NOT_FOUND);
        }
        return ScreeningQueryVO.builder()
                .id(screening.getId())
                .movieId(screening.getMovieId())
                .hallName(screening.getHallName())
                .rowCount(screening.getRowCount())
                .colCount(screening.getColCount())
                .startTime(screening.getStartTime())
                .endTime(screening.getEndTime())
                .price(screening.getPrice())
                .build();
    }

    @Override
    public PageInfo<ScreeningPageVO> page(ScreeningPageDTO screeningPageDTO) {
        PageHelper.startPage(screeningPageDTO.getPageNum(), screeningPageDTO.getPageSize());
        List<ScreeningPageVO> list = screeningMapper.selectPage(screeningPageDTO);
        return PageInfo.of(list);
    }

    @Override
    public List<ScreeningListVO> latest(Long cinemaId, Long movieId) {
        return screeningMapper.selectLatestByCinemaIdAndMovieId(cinemaId, movieId);
    }

    @Override
    public SeatInfoVO seatInfo(Long screeningId) {
        //获取排数-座数
        Screening screening = screeningMapper.selectById(screeningId);
        SeatInfoVO seatInfoVO = SeatInfoVO.builder()
                .screeningId(screeningId)
                .rowCount(screening.getColCount())
                .colCount(screening.getRowCount())
                .build();
        //查询bitmap场次座位占用
        String key = RedisConstant.SEAT_SCREENING + ":" + screeningId;
        byte[] bytes = redisTemplate.execute((RedisCallback<byte[]>) connection -> {
            return connection.get(key.getBytes(StandardCharsets.UTF_8));
        });
        if (bytes == null) {
            return seatInfoVO;
        }
        //获取座位占用
        List<SeatVO> lockSeats = new ArrayList<>();
        for (int i = 0; i < bytes.length * 8; i++) {
            //第byteIdx个字节的第bitIdx位
            int byteIdx = i / 8;
            int bitIdx = 7 - i % 8;//大端序需要反转位索引
            //该位是否为1
            if (((bytes[byteIdx] >> bitIdx) & 1) == 1) {
                int seatNum = i + 1;//座位号从1开始
                int row = (seatNum - 1) / seatInfoVO.getRowCount() + 1;
                int col = (seatNum - 1) % seatInfoVO.getColCount() + 1;
                SeatVO seat = new SeatVO();
                seat.setRow(row);
                seat.setCol(col);
                lockSeats.add(seat);
            }
        }
        //返回占用座位
        seatInfoVO.setLockSeats(lockSeats);
        return seatInfoVO;
    }
}
