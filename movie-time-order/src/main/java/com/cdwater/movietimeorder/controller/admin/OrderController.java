package com.cdwater.movietimeorder.controller.admin;

import com.cdwater.movietimecommon.Result;
import com.cdwater.movietimeorder.model.dto.OrderPageDTO;
import com.cdwater.movietimeorder.model.vo.OrderPageVO;
import com.cdwater.movietimeorder.service.OrderService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("adminOrderController")
@RequestMapping("/admin/order")
public class OrderController {

    @Resource
    private OrderService orderService;

    @GetMapping("/page")
    public Result page(OrderPageDTO orderPageDTO) {
        PageInfo<OrderPageVO> pageInfo = orderService.page(orderPageDTO);
        return Result.success(pageInfo);
    }

    @GetMapping("/count")
    public Result count() {
        Long count = orderService.count();
        return Result.success(count);
    }
}
