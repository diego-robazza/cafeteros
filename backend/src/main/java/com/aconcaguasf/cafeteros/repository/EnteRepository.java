package com.aconcaguasf.cafeteros.repository;

import com.aconcaguasf.cafeteros.domain.Ente;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Ente entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EnteRepository extends JpaRepository<Ente, UUID> {}
