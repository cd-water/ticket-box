package com.cdwater.ticketbox.auth.api.controller;

import com.cdwater.ticketbox.common.result.Result;
import com.cdwater.ticketbox.auth.api.dto.CodeLoginDTO;
import com.cdwater.ticketbox.auth.api.dto.PwdLoginDTO;
import com.cdwater.ticketbox.auth.api.dto.SendCodeDTO;
import com.cdwater.ticketbox.auth.application.service.UserService;
import com.cdwater.ticketbox.auth.api.vo.LoginVO;
import com.cdwater.ticketbox.auth.api.vo.UserVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/send-code")
    public Result<String> sendCode(@Valid @RequestBody SendCodeDTO dto) {
        return Result.success(userService.sendCode(dto.getPhone()));
    }

    @PostMapping("/login/by-code")
    public Result<LoginVO> loginByCode(@Valid @RequestBody CodeLoginDTO dto) {
        return Result.success(userService.loginByCode(dto));
    }

    @PostMapping("/login/by-pwd")
    public Result<LoginVO> loginByPwd(@Valid @RequestBody PwdLoginDTO dto) {
        return Result.success(userService.loginByPwd(dto));
    }

    @PostMapping("/logout")
    public Result<Void> logout() {
        userService.logout();
        return Result.success();
    }

    @GetMapping("/info")
    public Result<UserVO> info() {
        return Result.success(userService.info());
    }
}
