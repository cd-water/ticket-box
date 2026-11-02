package com.cdwater.ticketbox.admin;

import com.cdwater.ticketbox.admin.AdminCommandService;
import com.cdwater.ticketbox.admin.AdminLoginCommand;
import com.cdwater.ticketbox.admin.LoginView;
import com.cdwater.ticketbox.common.result.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminCommandService adminCommandService;

    @PostMapping("/login")
    public Result<LoginView> login(@Valid @RequestBody AdminLoginCommand command) {
        return Result.success(adminCommandService.login(command));
    }

    @PostMapping("/logout")
    public Result<Void> logout() {
        adminCommandService.logout();
        return Result.success();
    }
}
