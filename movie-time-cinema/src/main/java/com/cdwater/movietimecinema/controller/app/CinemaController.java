package com.cdwater.movietimecinema.controller.app;

import com.cdwater.movietimecinema.model.vo.CinemaDetailVO;
import com.cdwater.movietimecinema.model.vo.CinemaListVO;
import com.cdwater.movietimecinema.service.CinemaService;
import com.cdwater.movietimecommon.Result;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController("appCinemaController")
@RequestMapping("/app/cinema")
public class CinemaController {

    @Resource
    private CinemaService cinemaService;

    @GetMapping("/list")
    public Result list(
            @RequestParam BigDecimal longitude,
            @RequestParam BigDecimal latitude) {
        List<CinemaListVO> list = cinemaService.list(longitude, latitude);
        return Result.success(list);
    }

    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable("id") Long cinemaId) {
        CinemaDetailVO cinemaDetailVO = cinemaService.detail(cinemaId);
        return Result.success(cinemaDetailVO);
    }
}
