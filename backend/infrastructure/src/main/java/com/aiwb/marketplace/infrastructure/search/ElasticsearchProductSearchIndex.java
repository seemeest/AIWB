package com.aiwb.marketplace.infrastructure.search;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.mapping.Property;
import co.elastic.clients.elasticsearch._types.mapping.TypeMapping;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.aiwb.marketplace.application.ports.ProductSearchIndex;
import com.aiwb.marketplace.application.search.SearchResult;
import com.aiwb.marketplace.domain.product.Product;
import com.aiwb.marketplace.infrastructure.config.SearchProperties;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ElasticsearchProductSearchIndex implements ProductSearchIndex {
    private final ElasticsearchClient client;
    private final SearchProperties properties;

    public ElasticsearchProductSearchIndex(ElasticsearchClient client, SearchProperties properties) {
        this.client = client;
        this.properties = properties;
        ensureIndex();
    }

    @Override
    public void index(Product product) {
        Map<String, Object> doc = Map.of(
                "id", product.getId().toString(),
                "sellerId", product.getSellerId().toString(),
                "title", product.getTitle(),
                "description", product.getDescription(),
                "price", product.getPrice(),
                "status", product.getStatus().name()
        );
        try {
            client.index(i -> i.index(properties.getIndex())
                    .id(product.getId().toString())
                    .document(doc));
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to index product", ex);
        }
    }

    @Override
    public List<SearchResult> search(String query, int limit) {
        SearchRequest request = SearchRequest.of(s -> s
                .index(properties.getIndex())
                .size(limit)
                .query(q -> q.multiMatch(m -> m
                        .query(query)
                        .fields("title^2", "description")
                        .fuzziness("AUTO")
                )));
        try {
            SearchResponse<Map> response = client.search(request, Map.class);
            List<SearchResult> results = new ArrayList<>();
            response.hits().hits().forEach(hit -> {
                Map source = hit.source();
                UUID id = UUID.fromString(String.valueOf(source.get("id")));
                UUID sellerId = UUID.fromString(String.valueOf(source.get("sellerId")));
                String title = String.valueOf(source.get("title"));
                String description = source.get("description") == null ? null : String.valueOf(source.get("description"));
                BigDecimal price = new BigDecimal(String.valueOf(source.get("price")));
                String status = String.valueOf(source.get("status"));
                results.add(new SearchResult(id, sellerId, title, description, price, status));
            });
            return results;
        } catch (IOException ex) {
            throw new IllegalStateException("Search failed", ex);
        }
    }

    @Override
    public List<String> suggest(String prefix, int limit) {
        SearchRequest request = SearchRequest.of(s -> s
                .index(properties.getIndex())
                .size(limit)
                .query(q -> q.matchPhrasePrefix(m -> m.field("title").query(prefix)))
        );
        try {
            SearchResponse<Map> response = client.search(request, Map.class);
            return response.hits().hits().stream()
                    .map(hit -> hit.source().get("title"))
                    .filter(item -> item != null)
                    .map(Object::toString)
                    .distinct()
                    .limit(limit)
                    .collect(Collectors.toList());
        } catch (IOException ex) {
            throw new IllegalStateException("Suggest failed", ex);
        }
    }

    private void ensureIndex() {
        try {
            boolean exists = client.indices().exists(e -> e.index(properties.getIndex())).value();
            if (exists) {
                return;
            }
            TypeMapping mapping = TypeMapping.of(m -> m.properties(Map.of(
                    "title", Property.of(p -> p.text(t -> t)),
                    "description", Property.of(p -> p.text(t -> t)),
                    "price", Property.of(p -> p.double_(d -> d)),
                    "status", Property.of(p -> p.keyword(k -> k)),
                    "sellerId", Property.of(p -> p.keyword(k -> k))
            )));
            client.indices().create(c -> c
                    .index(properties.getIndex())
                    .mappings(mapping)
            );
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to create search index", ex);
        }
    }
}
