package org.burgas.bookservice.repository;

import org.burgas.bookservice.entity.author.Author;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuthorRepository extends JpaRepository<Author, UUID> {

    @Override
    @EntityGraph(value = "author-entity-graph", type = EntityGraph.EntityGraphType.FETCH)
    @NonNull Optional<Author> findById(UUID uuid);
}
