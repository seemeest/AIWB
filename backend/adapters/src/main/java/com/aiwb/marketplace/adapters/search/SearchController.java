package com.aiwb.marketplace.adapters.search;

import com.aiwb.marketplace.application.metrics.MetricsService;
import com.aiwb.marketplace.application.search.SearchResult;
import com.aiwb.marketplace.application.search.SearchService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {
    private final SearchService searchService;
    private final MetricsService metricsService;

    public SearchController(SearchService searchService, MetricsService metricsService) {
        this.searchService = searchService;
        this.metricsService = metricsService;
    }

    @GetMapping
    public ResponseEntity<SearchResponse> search(@RequestParam("query") @NotBlank String query,
                                                 @RequestParam(value = "limit", defaultValue = "20") @Positive int limit) {
        List<SearchResult> results = searchService.search(query, limit);
        metricsService.recordSearchImpressions(query, results);
        return ResponseEntity.ok(new SearchResponse(results));
    }

    @GetMapping("/suggest")
    public ResponseEntity<SuggestResponse> suggest(@RequestParam("prefix") @NotBlank String prefix,
                                                   @RequestParam(value = "limit", defaultValue = "10") @Positive int limit) {
        return ResponseEntity.ok(new SuggestResponse(searchService.suggest(prefix, limit)));
    }
}
