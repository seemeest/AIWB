package com.aiwb.marketplace.infrastructure.persistence;

import com.aiwb.marketplace.application.metrics.ProductView;
import com.aiwb.marketplace.application.metrics.SearchImpression;
import com.aiwb.marketplace.application.ports.MetricsRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
@Transactional
public class MetricsRepositoryImpl implements MetricsRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void saveProductView(ProductView view) {
        ProductViewEntity entity = new ProductViewEntity();
        entity.setId(view.id());
        entity.setProductId(view.productId());
        entity.setSellerId(view.sellerId());
        entity.setViewerId(view.viewerId());
        entity.setSessionId(view.sessionId());
        entity.setViewedAt(view.viewedAt());
        entityManager.persist(entity);
    }

    @Override
    public void saveSearchImpressions(List<SearchImpression> impressions) {
        for (SearchImpression impression : impressions) {
            SearchImpressionEntity entity = new SearchImpressionEntity();
            entity.setId(impression.id());
            entity.setProductId(impression.productId());
            entity.setSellerId(impression.sellerId());
            entity.setQuery(impression.query());
            entity.setPosition(impression.position());
            entity.setShownAt(impression.shownAt());
            entityManager.persist(entity);
        }
    }

    @Override
    public long countUniqueViews(UUID sellerId, Instant from, Instant to) {
        Object result = entityManager.createNativeQuery("""
                select count(distinct coalesce(cast(viewer_id as text), session_id))
                from product_views
                where seller_id = :sellerId
                  and viewed_at between :from and :to
                """)
                .setParameter("sellerId", sellerId)
                .setParameter("from", from)
                .setParameter("to", to)
                .getSingleResult();
        return result == null ? 0L : ((Number) result).longValue();
    }

    @Override
    public double averageSearchPosition(UUID sellerId, Instant from, Instant to) {
        Object result = entityManager.createNativeQuery("""
                select avg(position)
                from search_impressions
                where seller_id = :sellerId
                  and shown_at between :from and :to
                """)
                .setParameter("sellerId", sellerId)
                .setParameter("from", from)
                .setParameter("to", to)
                .getSingleResult();
        return result == null ? 0.0 : ((Number) result).doubleValue();
    }

    @Override
    public long countOrders(UUID sellerId, Instant from, Instant to) {
        Object result = entityManager.createNativeQuery("""
                select count(distinct o.id)
                from order_items oi
                join orders o on o.id = oi.order_id
                where oi.seller_id = :sellerId
                  and o.created_at between :from and :to
                """)
                .setParameter("sellerId", sellerId)
                .setParameter("from", from)
                .setParameter("to", to)
                .getSingleResult();
        return result == null ? 0L : ((Number) result).longValue();
    }
}
