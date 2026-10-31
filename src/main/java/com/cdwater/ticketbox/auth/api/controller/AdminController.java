package com.cdwater.ticketbox.auth.api.controller;

import com.cdwater.ticketbox.common.result.Result;
import com.cdwater.ticketbox.auth.api.dto.AdminLoginDTO;
import com.cdwater.ticketbox.auth.application.service.AdminService;
import com.cdwater.ticketbox.auth.api.vo.AdminVO;
import com.cdwater.ticketbox.auth.api.vo.LoginVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody AdminLoginDTO dto) {
        return Result.success(adminService.login(dto));
    }

    @PostMapping("/logout")
    public Result<Void> logout() {
        adminService.logout();
        return Result.success();
    }

    @GetMapping("/info")
    public Result<AdminVO> info() {
        return Result.success(adminService.info());
    }
}
