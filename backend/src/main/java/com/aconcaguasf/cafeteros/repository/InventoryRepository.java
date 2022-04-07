package com.aconcaguasf.cafeteros.repository;

import com.aconcaguasf.cafeteros.domain.Inventory;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Inventory entity.
 */
@Repository
public interface InventoryRepository extends JpaRepository<Inventory, UUID> {
    default Optional<Inventory> findOneWithEagerRelationships(UUID id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Inventory> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Inventory> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct inventory from Inventory inventory left join fetch inventory.ente",
        countQuery = "select count(distinct inventory) from Inventory inventory"
    )
    Page<Inventory> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct inventory from Inventory inventory left join fetch inventory.ente")
    List<Inventory> findAllWithToOneRelationships();

    @Query("select inventory from Inventory inventory left join fetch inventory.ente where inventory.id =:id")
    Optional<Inventory> findOneWithToOneRelationships(@Param("id") UUID id);
}
