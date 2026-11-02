package com.cdwater.ticketbox.show.adapter.in.web;

import com.cdwater.ticketbox.common.result.PageResult;
import com.cdwater.ticketbox.common.result.Result;
import com.cdwater.ticketbox.show.application.query.ShowListQuery;
import com.cdwater.ticketbox.show.application.query.ShowQueryService;
import com.cdwater.ticketbox.show.application.query.view.ShowDetailView;
import com.cdwater.ticketbox.show.application.query.view.ShowListItemView;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/shows")
@RequiredArgsConstructor
public class ShowController {

    private final ShowQueryService showQueryService;

    @GetMapping
    public Result<PageResult<ShowListItemView>> list(@Valid ShowListQuery query) {
        return Result.success(showQueryService.list(query));
    }

    @GetMapping("/{id}")
    public Result<ShowDetailView> detail(@PathVariable Long id) {
        return Result.success(showQueryService.detail(id));
    }
}
