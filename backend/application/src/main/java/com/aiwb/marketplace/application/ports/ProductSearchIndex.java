package com.aiwb.marketplace.application.ports;

import com.aiwb.marketplace.application.search.SearchResult;
import com.aiwb.marketplace.domain.product.Product;

import java.util.List;

public interface ProductSearchIndex {
    void index(Product product);
    List<SearchResult> search(String query, int limit);
    List<String> suggest(String prefix, int limit);
}
