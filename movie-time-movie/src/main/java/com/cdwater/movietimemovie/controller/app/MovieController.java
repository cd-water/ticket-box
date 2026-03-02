package com.cdwater.movietimemovie.controller.app;

import com.cdwater.movietimecommon.Result;
import com.cdwater.movietimemovie.model.vo.MovieDetailVO;
import com.cdwater.movietimemovie.model.vo.MovieListVO;
import com.cdwater.movietimemovie.service.MovieService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("appMovieController")
@RequestMapping("/app/movie")
public class MovieController {

    @Resource
    private MovieService movieService;

    @GetMapping("/list")
    public Result list(@RequestParam("status") Integer status) {
        List<MovieListVO> list = movieService.list(status);
        return Result.success(list);
    }

    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable("id") Long movieId) {
        MovieDetailVO movieDetailVO = movieService.detail(movieId);
        return Result.success(movieDetailVO);
    }
}
