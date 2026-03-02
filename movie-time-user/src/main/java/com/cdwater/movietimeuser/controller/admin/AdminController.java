package com.cdwater.movietimeuser.controller.admin;

import com.cdwater.movietimecommon.Result;
import com.cdwater.movietimeuser.aop.SuperAdmin;
import com.cdwater.movietimeuser.model.dto.AdminCreateDTO;
import com.cdwater.movietimeuser.model.dto.AdminPageDTO;
import com.cdwater.movietimeuser.model.dto.AdminEditDTO;
import com.cdwater.movietimeuser.model.vo.AdminPageVO;
import com.cdwater.movietimeuser.service.AdminService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/admin")
public class AdminController {

    @Resource
    private AdminService adminService;

    @SuperAdmin
    @PostMapping("/create")
    public Result create(@Valid @RequestBody AdminCreateDTO adminCreateDTO) {
        adminService.create(adminCreateDTO);
        return Result.success();
    }

    @SuperAdmin
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Long id) {
        adminService.delete(id);
        return Result.success();
    }

    @SuperAdmin
    @PutMapping("/edit")
    public Result edit(@Valid @RequestBody AdminEditDTO adminEditDTO) {
        adminService.edit(adminEditDTO);
        return Result.success();
    }

    @SuperAdmin
    @GetMapping("/page")
    public Result page(AdminPageDTO adminPageDTO) {
        PageInfo<AdminPageVO> pageInfo = adminService.page(adminPageDTO);
        return Result.success(pageInfo);
    }
}
