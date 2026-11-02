package com.cdwater.ticketbox.controller;

import com.cdwater.ticketbox.common.result.PageResult;
import com.cdwater.ticketbox.common.result.Result;
import com.cdwater.ticketbox.content.api.ShowQueryApi;
import com.cdwater.ticketbox.content.api.dto.ShowDetailDTO;
import com.cdwater.ticketbox.content.api.dto.ShowListItemDTO;
import com.cdwater.ticketbox.content.api.dto.ShowListQueryDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/shows")
@RequiredArgsConstructor
public class ShowController {

    @DubboReference(check = false, timeout = 3000)
    private ShowQueryApi showQueryApi;

    @GetMapping
    public Result<PageResult<ShowListItemDTO>> list(@Valid ShowListQueryDTO query) {
        return Result.success(showQueryApi.list(query));
    }

    @GetMapping("/{id}")
    public Result<ShowDetailDTO> getDetail(@PathVariable Long id) {
        return Result.success(showQueryApi.getDetail(id));
    }
}
