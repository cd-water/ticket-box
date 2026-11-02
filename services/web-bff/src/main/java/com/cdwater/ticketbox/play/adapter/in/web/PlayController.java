package com.cdwater.ticketbox.play.adapter.in.web;

import com.cdwater.ticketbox.common.result.PageResult;
import com.cdwater.ticketbox.common.result.Result;
import com.cdwater.ticketbox.content.api.PlayQueryApi;
import com.cdwater.ticketbox.content.api.dto.PlayDetailDTO;
import com.cdwater.ticketbox.content.api.dto.PlayListItemDTO;
import com.cdwater.ticketbox.content.api.dto.PlayListQueryDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/plays")
@RequiredArgsConstructor
public class PlayController {

    @DubboReference(check = false, timeout = 3000)
    private PlayQueryApi playQueryApi;

    @GetMapping
    public Result<PageResult<PlayListItemDTO>> list(@Valid PlayListQueryDTO query) {
        return Result.success(playQueryApi.list(query));
    }

    @GetMapping("/{id}")
    public Result<PlayDetailDTO> detail(@PathVariable Long id) {
        return Result.success(playQueryApi.detail(id));
    }
}
