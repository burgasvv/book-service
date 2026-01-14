package org.burgas.bookservice.repository;

import jakarta.persistence.LockModeType;
import org.burgas.bookservice.entity.book.Book;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<Book, UUID> {

    @Override
    @EntityGraph(value = "book-entity-graph", type = EntityGraph.EntityGraphType.FETCH)
    @NonNull Optional<Book> findById(UUID uuid);

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    Optional<Book> findBookById(UUID id);
}
