package com.cdwater.movietimeorder.controller.app;

import com.cdwater.movietimecommon.Result;
import com.cdwater.movietimeorder.model.dto.OrderPlaceDTO;
import com.cdwater.movietimeorder.model.vo.OrderListVO;
import com.cdwater.movietimeorder.model.vo.OrderPlaceVO;
import com.cdwater.movietimeorder.service.OrderService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("appOrderController")
@RequestMapping("/app/order")
public class OrderController {

    @Resource
    private OrderService orderService;

    @GetMapping("/list")
    public Result list() {
        List<OrderListVO> list = orderService.list();
        return Result.success(list);
    }

    @PostMapping("/place")
    public Result place(@RequestBody OrderPlaceDTO orderPlaceDTO) {
        OrderPlaceVO orderPlaceVO = orderService.place(orderPlaceDTO);
        return Result.success(orderPlaceVO);
    }

    @PostMapping("/pay/{orderNo}")
    public Result pay(@PathVariable("orderNo") Long orderNo) {
        orderService.pay(orderNo);
        return Result.success();
    }

    @PostMapping("/cancel/{orderNo}")
    public Result cancel(@PathVariable("orderNo") Long orderNo) {
        orderService.cancel(orderNo);
        return Result.success();
    }
}
