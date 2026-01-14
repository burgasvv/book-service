package org.burgas.bookservice.repository;

import jakarta.persistence.LockModeType;
import org.burgas.bookservice.entity.identity.Identity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IdentityRepository extends JpaRepository<Identity, UUID> {

    @Override
    @EntityGraph(value = "identity-entity-graph", type = EntityGraph.EntityGraphType.FETCH)
    @NonNull Optional<Identity> findById(UUID uuid);

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    Optional<Identity> findIdentityById(UUID id);

    Optional<Identity> findIdentityByEmail(String email);
}
