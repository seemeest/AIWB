package com.aiwb.marketplace.application.search;

import com.aiwb.marketplace.application.ports.ProductSearchIndex;

import java.util.List;

public class SearchService {
    private final ProductSearchIndex productSearchIndex;

    public SearchService(ProductSearchIndex productSearchIndex) {
        this.productSearchIndex = productSearchIndex;
    }

    public List<SearchResult> search(String query, int limit) {
        return productSearchIndex.search(query, limit);
    }

    public List<String> suggest(String prefix, int limit) {
        return productSearchIndex.suggest(prefix, limit);
    }
}
