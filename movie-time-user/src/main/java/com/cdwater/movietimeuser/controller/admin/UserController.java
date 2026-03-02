package com.cdwater.movietimeuser.controller.admin;

import com.cdwater.movietimecommon.Result;
import com.cdwater.movietimeuser.model.dto.UserPageDTO;
import com.cdwater.movietimeuser.model.vo.UserPageVO;
import com.cdwater.movietimeuser.service.UserService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("adminUserController")
@RequestMapping("/admin/user")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/page")
    public Result page(UserPageDTO userPageDTO) {
        PageInfo<UserPageVO> pageInfo = userService.page(userPageDTO);
        return Result.success(pageInfo);
    }

    @GetMapping("/count")
    public Result count() {
        Long count = userService.count();
        return Result.success(count);
    }
}
