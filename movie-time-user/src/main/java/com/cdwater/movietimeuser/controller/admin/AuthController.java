package com.cdwater.movietimeuser.controller.admin;

import com.cdwater.movietimecommon.Result;
import com.cdwater.movietimeuser.model.dto.AdminCpwdDTO;
import com.cdwater.movietimeuser.model.dto.AdminLoginDTO;
import com.cdwater.movietimeuser.model.vo.AdminLoginRes;
import com.cdwater.movietimeuser.service.AuthService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController("adminAuthController")
@RequestMapping("/admin/auth")
public class AuthController {

    @Resource
    private AuthService authService;

    @PostMapping("/login")
    public Result login(@Valid @RequestBody AdminLoginDTO adminLoginDTO) {
        AdminLoginRes adminLoginRes = authService.adminLogin(adminLoginDTO);
        return Result.success(adminLoginRes);
    }

    @PostMapping("/logout")
    public Result logout(@RequestHeader("Authorization") String header) {
        authService.adminLogout(header);
        return Result.success();
    }

    @PostMapping("/cpwd")
    public Result changePwd(@Valid @RequestBody AdminCpwdDTO adminCpwdDTO,
                            @RequestHeader("Authorization") String header) {
        authService.adminChangePwd(adminCpwdDTO,header);
        return Result.success();
    }
}
