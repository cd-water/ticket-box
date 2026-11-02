package com.cdwater.ticketbox.user;

import com.cdwater.ticketbox.common.security.CurrentUser;
import com.cdwater.ticketbox.common.result.Result;
import com.cdwater.ticketbox.user.CodeLoginCommand;
import com.cdwater.ticketbox.user.PwdLoginCommand;
import com.cdwater.ticketbox.user.SendCodeCommand;
import com.cdwater.ticketbox.user.UpdateNicknameCommand;
import com.cdwater.ticketbox.user.UpdatePasswordCommand;
import com.cdwater.ticketbox.user.UserCommandService;
import com.cdwater.ticketbox.user.UserQueryService;
import com.cdwater.ticketbox.user.LoginView;
import com.cdwater.ticketbox.user.ProfileView;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;
    private final CurrentUser currentUser;

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

    @GetMapping("/profile")
    public Result<ProfileView> getProfile() {
        return Result.success(userQueryService.getProfile(currentUser.id()));
    }

    @PostMapping("/profile/nickname")
    public Result<ProfileView> updateNickname(@Valid @RequestBody UpdateNicknameCommand command) {
        return Result.success(userCommandService.updateNickname(currentUser.id(), command));
    }

    @PostMapping("/profile/password")
    public Result<ProfileView> updatePassword(@Valid @RequestBody UpdatePasswordCommand command) {
        return Result.success(userCommandService.updatePassword(currentUser.id(), command));
    }

    @PostMapping("/profile/avatar")
    public Result<ProfileView> updateAvatar(@RequestParam("file") MultipartFile file) {
        return Result.success(userCommandService.updateAvatar(currentUser.id(), file));
    }
}
