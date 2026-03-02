package com.cdwater.movietimemovie.controller.admin;

import com.cdwater.movietimecommon.Result;
import com.cdwater.movietimemovie.model.dto.MovieAddDTO;
import com.cdwater.movietimemovie.model.dto.MoviePageDTO;
import com.cdwater.movietimemovie.model.dto.MovieUpdateDTO;
import com.cdwater.movietimemovie.model.vo.MovieShowingVO;
import com.cdwater.movietimemovie.model.vo.MoviePageVO;
import com.cdwater.movietimemovie.model.vo.MovieQueryVO;
import com.cdwater.movietimemovie.service.MovieService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("adminMovieController")
@RequestMapping("/admin/movie")
public class MovieController {

    @Resource
    private MovieService movieService;

    @PostMapping("/add")
    public Result add(@Valid @RequestBody MovieAddDTO movieAddDTO) {
        movieService.add(movieAddDTO);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@Valid @RequestBody MovieUpdateDTO movieUpdateDTO) {
        movieService.update(movieUpdateDTO);
        return Result.success();
    }

    @GetMapping("/query/{id}")
    public Result query(@PathVariable Long id) {
        MovieQueryVO movieQueryVO = movieService.query(id);
        return Result.success(movieQueryVO);
    }

    @GetMapping("/page")
    public Result page(MoviePageDTO moviePageDTO) {
        PageInfo<MoviePageVO> pageInfo = movieService.page(moviePageDTO);
        return Result.success(pageInfo);
    }

    @GetMapping("/showing")
    public Result showing() {
        List<MovieShowingVO> list = movieService.showing();
        return Result.success(list);
    }

    @GetMapping("/count")
    public Result count() {
        Long count = movieService.count();
        return Result.success(count);
    }
}
