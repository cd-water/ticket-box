package com.cdwater.movietimeuser.controller.app;

import com.cdwater.movietimecommon.Result;
import com.cdwater.movietimecommon.annotations.SkipAppInterceptor;
import com.cdwater.movietimeuser.model.dto.AppCpwdDTO;
import com.cdwater.movietimeuser.model.dto.AppFpwdDTO;
import com.cdwater.movietimeuser.model.dto.AppLoginDTO;
import com.cdwater.movietimeuser.model.dto.SendCodeDTO;
import com.cdwater.movietimeuser.model.vo.AppLoginRes;
import com.cdwater.movietimeuser.service.AuthService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController("appAuthController")
@RequestMapping("/app/auth")
public class AuthController {

    @Resource
    private AuthService authService;

    @SkipAppInterceptor
    @PostMapping("/send-code")
    public Result sendCode(@RequestBody @Valid SendCodeDTO sendCodeDTO) {
        authService.appSendCode(sendCodeDTO);
        return Result.success();
    }

    @SkipAppInterceptor
    @PostMapping("/phone-login")
    public Result phoneLogin(@RequestBody @Valid AppLoginDTO appLoginDTO) {
        AppLoginRes appLoginRes = authService.appPhoneLogin(appLoginDTO);
        return Result.success(appLoginRes);
    }

    @SkipAppInterceptor
    @PostMapping("/pwd-login")
    public Result pwdLogin(@RequestBody @Valid AppLoginDTO appLoginDTO) {
        AppLoginRes appLoginRes = authService.appPwdLogin(appLoginDTO);
        return Result.success(appLoginRes);
    }

    @PutMapping("/cpwd")
    public Result changePwd(@RequestBody @Valid AppCpwdDTO appCpwdDTO) {
        authService.appChangePwd(appCpwdDTO);
        return Result.success();
    }

    @SkipAppInterceptor
    @PostMapping("/fpwd")
    public Result forgetPwd(@RequestBody @Valid AppFpwdDTO appFpwdDTO) {
        authService.appForgetPwd(appFpwdDTO);
        return Result.success();
    }

    @PostMapping("/logout")
    public Result logout(@RequestHeader("Authorization") String authHeader) {
        authService.appLogout(authHeader);
        return Result.success();
    }

    @SkipAppInterceptor
    @PostMapping("/refresh-token")
    public Result refreshToken(@RequestHeader("Authorization") String authHeader,
                               @RequestHeader("X-Refresh-Token") String refreshToken) {
        AppLoginRes appLoginRes = authService.appRefreshToken(authHeader, refreshToken);
        return Result.success(appLoginRes);
    }
}
