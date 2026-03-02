package com.cdwater.movietimeuser.controller.app;

import com.cdwater.movietimecommon.Result;
import com.cdwater.movietimecommon.annotations.SkipAppInterceptor;
import com.cdwater.movietimeuser.model.dto.UserEditDTO;
import com.cdwater.movietimeuser.model.vo.UserDetailVO;
import com.cdwater.movietimeuser.model.vo.UserListVO;
import com.cdwater.movietimeuser.service.UserService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("appUserController")
@RequestMapping("/app/user")
public class UserController {

    @Resource
    private UserService userService;

    @SkipAppInterceptor
    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable("id") Long userId,
                         @RequestHeader("Authorization") String authHeader) {
        UserDetailVO userDetailVO = userService.detail(userId, authHeader);
        return Result.success(userDetailVO);
    }

    @PostMapping("/follow/{id}")
    public Result follow(@PathVariable("id") Long targetId) {
        userService.follow(targetId);
        return Result.success();
    }

    @SkipAppInterceptor
    @GetMapping("/list/{id}/follower")
    public Result listFollower(@PathVariable("id") Long userId) {
        List<UserListVO> list = userService.listFollower(userId);
        return Result.success(list);
    }

    @SkipAppInterceptor
    @GetMapping("/list/{id}/fan")
    public Result listFan(@PathVariable("id") Long userId) {
        List<UserListVO> list = userService.listFan(userId);
        return Result.success(list);
    }

    @PutMapping("/edit")
    public Result edit(@RequestBody @Valid UserEditDTO userEditDTO) {
        userService.edit(userEditDTO);
        return Result.success();
    }
}
