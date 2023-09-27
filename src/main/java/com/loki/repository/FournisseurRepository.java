package com.loki.repository;

import com.loki.domain.Fournisseur;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Fournisseur entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FournisseurRepository extends JpaRepository<Fournisseur, Long> {}
