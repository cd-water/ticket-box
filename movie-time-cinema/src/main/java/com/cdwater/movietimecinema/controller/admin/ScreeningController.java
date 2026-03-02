package com.cdwater.movietimecinema.controller.admin;

import com.cdwater.movietimecinema.model.dto.ScreeningAddDTO;
import com.cdwater.movietimecinema.model.dto.ScreeningPageDTO;
import com.cdwater.movietimecinema.model.dto.ScreeningUpdateDTO;
import com.cdwater.movietimecinema.model.vo.ScreeningPageVO;
import com.cdwater.movietimecinema.model.vo.ScreeningQueryVO;
import com.cdwater.movietimecinema.service.ScreeningService;
import com.cdwater.movietimecommon.Result;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController("adminScreeningController")
@RequestMapping("/admin/screening")
public class ScreeningController {

    @Resource
    private ScreeningService screeningService;

    @PostMapping("/add")
    public Result add(@Valid @RequestBody ScreeningAddDTO screeningAddDTO) {
        screeningService.add(screeningAddDTO);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Long id) {
        screeningService.delete(id);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@Valid @RequestBody ScreeningUpdateDTO screeningUpdateDTO) {
        screeningService.update(screeningUpdateDTO);
        return Result.success();
    }

    @GetMapping("/query/{id}")
    public Result query(@PathVariable Long id) {
        ScreeningQueryVO screeningQueryVO = screeningService.query(id);
        return Result.success(screeningQueryVO);
    }

    @GetMapping("/page")
    public Result page(ScreeningPageDTO screeningPageDTO) {
        PageInfo<ScreeningPageVO> pageInfo = screeningService.page(screeningPageDTO);
        return Result.success(pageInfo);
    }
}
