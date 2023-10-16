package com.loki.repository;

import com.loki.domain.Panier;
import com.loki.domain.enumeration.PanierStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Panier entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PanierRepository extends JpaRepository<Panier, Long> {
        Panier findFirstByClient_IdAndStatusOrderByCreatedDesc(Long userId, PanierStatus status);
    }
