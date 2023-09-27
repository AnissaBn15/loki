package com.loki.repository;

import com.loki.domain.LineOfCommand;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the LineOfCommand entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LineOfCommandRepository extends JpaRepository<LineOfCommand, Long> {}
