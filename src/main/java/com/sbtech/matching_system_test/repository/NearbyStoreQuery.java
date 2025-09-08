package com.sbtech.matching_system_test.repository;

import com.sbtech.matching_system_test.domain.Store;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NearbyStoreQuery {
    @Query(value = """
        SELECT s.*,
        (6371 * acos(
            cos(radians(:lat)) * cos(radians(s.latitude)) *
            cos(radians(s.longitude) - radians(:lng)) +
            sin(radians(:lat)) * sin(radians(s.latitude))
        )) AS distanceKm
        FROM stores s
        WHERE s.active = true
        HAVING distanceKm <= :radiusKm
        ORDER BY distanceKm ASC
        LIMIT :limit
        """, nativeQuery = true)
    List<Store> findNearbyStores(@Param("lat") double lat,
                                 @Param("lng") double lng,
                                 @Param("radiusKm") double radiusKm,
                                 @Param("limit") int limit);
}
