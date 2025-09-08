package com.sbtech.matching_system_test.repository;

import com.sbtech.matching_system_test.domain.Store;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NearbyStoreRepository {
    @PersistenceContext
    private EntityManager em;

    public List<Store> findNearbyStores(double lat, double lng, double radiusKm, int limit) {
        String sql = """
            SELECT s.*,
            (6371 * acos(
                cos(radians(?1)) * cos(radians(s.latitude)) *
                cos(radians(s.longitude) - radians(?2)) +
                sin(radians(?1)) * sin(radians(s.latitude))
            )) AS distanceKm
            FROM stores s
            WHERE s.active = true
            HAVING distanceKm <= ?3
            ORDER BY distanceKm ASC
            LIMIT ?4
            """;
        return em.createNativeQuery(sql, Store.class)
                .setParameter(1, lat)
                .setParameter(2, lng)
                .setParameter(3, radiusKm)
                .setParameter(4, limit)
                .getResultList();
    }
}
