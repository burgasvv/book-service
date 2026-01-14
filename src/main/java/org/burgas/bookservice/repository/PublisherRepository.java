package org.burgas.bookservice.repository;

import org.burgas.bookservice.entity.publisher.Publisher;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, UUID> {

    @Override
    @EntityGraph(value = "publisher-entity-graph", type = EntityGraph.EntityGraphType.FETCH)
    @NonNull Optional<Publisher> findById(UUID uuid);
}
