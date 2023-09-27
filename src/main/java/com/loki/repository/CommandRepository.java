package com.loki.repository;

import com.loki.domain.Command;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Command entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommandRepository extends JpaRepository<Command, Long> {}
