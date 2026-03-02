package com.cdwater.movietimecinema.controller.admin;

import com.cdwater.movietimecinema.model.dto.CinemaAddDTO;
import com.cdwater.movietimecinema.model.dto.CinemaPageDTO;
import com.cdwater.movietimecinema.model.dto.CinemaUpdateDTO;
import com.cdwater.movietimecinema.model.vo.CinemaPageVO;
import com.cdwater.movietimecinema.model.vo.CinemaQueryVO;
import com.cdwater.movietimecinema.service.CinemaService;
import com.cdwater.movietimecommon.Result;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController("adminCinemaController")
@RequestMapping("/admin/cinema")
public class CinemaController {

    @Resource
    private CinemaService cinemaService;

    @PostMapping("/add")
    public Result add(@Valid @RequestBody CinemaAddDTO cinemaAddDTO) {
        cinemaService.add(cinemaAddDTO);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Long id) {
        cinemaService.delete(id);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@Valid @RequestBody CinemaUpdateDTO cinemaUpdateDTO) {
        cinemaService.update(cinemaUpdateDTO);
        return Result.success();
    }

    @GetMapping("/query/{id}")
    public Result query(@PathVariable Long id) {
        CinemaQueryVO cinemaQueryVO = cinemaService.query(id);
        return Result.success(cinemaQueryVO);
    }

    @GetMapping("/page")
    public Result page(CinemaPageDTO cinemaPageDTO) {
        PageInfo<CinemaPageVO> pageInfo = cinemaService.page(cinemaPageDTO);
        return Result.success(pageInfo);
    }

    @GetMapping("/count")
    public Result count() {
        Long count = cinemaService.count();
        return Result.success(count);
    }
}
