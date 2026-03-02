package com.cdwater.movietimecinema.controller.app;

import com.cdwater.movietimecinema.model.vo.ScreeningListVO;
import com.cdwater.movietimecinema.model.vo.SeatInfoVO;
import com.cdwater.movietimecinema.service.ScreeningService;
import com.cdwater.movietimecommon.Result;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("appScreeningController")
@RequestMapping("/app/screening")
public class ScreeningController {

    @Resource
    private ScreeningService screeningService;

    @GetMapping("/cinema/{cinemaId}/movie/{movieId}")
    public Result latest(@PathVariable("cinemaId") Long cinemaId,
                         @PathVariable("movieId") Long movieId) {
        List<ScreeningListVO> list = screeningService.latest(cinemaId, movieId);
        return Result.success(list);
    }

    @GetMapping("/{id}/seat-info")
    public Result seatInfo(@PathVariable("id") Long screeningId) {
        SeatInfoVO list = screeningService.seatInfo(screeningId);
        return Result.success(list);
    }
}
