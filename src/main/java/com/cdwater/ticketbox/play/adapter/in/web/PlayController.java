package com.cdwater.ticketbox.play.adapter.in.web;

import com.cdwater.ticketbox.play.application.query.PlayListQuery;
import com.cdwater.ticketbox.play.application.query.PlayQueryService;
import com.cdwater.ticketbox.play.application.query.view.PlayDetailView;
import com.cdwater.ticketbox.play.application.query.view.PlayListItemView;
import com.cdwater.ticketbox.shared.query.PageResult;
import com.cdwater.ticketbox.shared.web.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/plays")
@RequiredArgsConstructor
public class PlayController {

    private final PlayQueryService playQueryService;

    @GetMapping
    public Result<PageResult<PlayListItemView>> list(@Valid PlayListQuery query) {
        return Result.success(playQueryService.list(query));
    }

    @GetMapping("/{id}")
    public Result<PlayDetailView> detail(@PathVariable Long id) {
        return Result.success(playQueryService.detail(id));
    }
}
