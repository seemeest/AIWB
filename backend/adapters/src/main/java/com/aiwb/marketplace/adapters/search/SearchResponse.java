package com.aiwb.marketplace.adapters.search;

import com.aiwb.marketplace.application.search.SearchResult;

import java.util.List;

public record SearchResponse(List<SearchResult> items) {
}
