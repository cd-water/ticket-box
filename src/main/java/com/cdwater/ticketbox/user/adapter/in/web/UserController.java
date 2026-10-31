package com.cdwater.ticketbox.user.adapter.in.web;

import com.cdwater.ticketbox.shared.web.Result;
import com.cdwater.ticketbox.user.application.command.CodeLoginCommand;
import com.cdwater.ticketbox.user.application.command.PwdLoginCommand;
import com.cdwater.ticketbox.user.application.command.SendCodeCommand;
import com.cdwater.ticketbox.user.application.command.UserCommandService;
import com.cdwater.ticketbox.user.application.query.view.LoginView;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserCommandService userCommandService;

    @PostMapping("/send-code")
    public Result<Void> sendCode(@Valid @RequestBody SendCodeCommand command) {
        userCommandService.sendCode(command);
        return Result.success();
    }

    @PostMapping("/login/by-code")
    public Result<LoginView> loginByCode(@Valid @RequestBody CodeLoginCommand command) {
        return Result.success(userCommandService.loginByCode(command));
    }

    @PostMapping("/login/by-pwd")
    public Result<LoginView> loginByPwd(@Valid @RequestBody PwdLoginCommand command) {
        return Result.success(userCommandService.loginByPwd(command));
    }

    @PostMapping("/logout")
    public Result<Void> logout() {
        userCommandService.logout();
        return Result.success();
    }
}
