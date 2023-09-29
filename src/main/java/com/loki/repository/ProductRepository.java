package com.loki.repository;

import com.loki.domain.Product;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the Product entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(value = "SELECT p FROM Product p JOIN FETCH p.productCategory pc WHERE pc.name like %:search%",
        countQuery = "SELECT COUNT(p) FROM Product p JOIN p.productCategory pc WHERE pc.name like %:search%")
    Page<Product> findProduitCategorie(Pageable page, @Param("search") String search);

    @Query("SELECT p FROM Product p ORDER BY p.nbrOfSells DESC")
    Page<Product> findAllByOrderByNbrOfSellsDesc(Pageable pageable);
}
