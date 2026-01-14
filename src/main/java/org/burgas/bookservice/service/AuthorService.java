package org.burgas.bookservice.service;

import lombok.RequiredArgsConstructor;
import org.burgas.bookservice.dto.author.AuthorFullResponse;
import org.burgas.bookservice.dto.author.AuthorRequest;
import org.burgas.bookservice.dto.author.AuthorShortResponse;
import org.burgas.bookservice.entity.author.Author;
import org.burgas.bookservice.mapper.AuthorMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
public class AuthorService implements CrudService<AuthorRequest, AuthorShortResponse, AuthorFullResponse> {

    private final AuthorMapper authorMapper;

    @Override
    public List<AuthorShortResponse> findAll() {
        return this.authorMapper.getAuthorRepository().findAll()
                .stream()
                .map(this.authorMapper::toShortResponse)
                .toList();
    }

    @Override
    public AuthorFullResponse findById(UUID id) {
        return this.authorMapper.getAuthorRepository().findById(id)
                .map(this.authorMapper::toFullResponse)
                .orElseThrow(() -> new IllegalArgumentException("Author not found"));
    }

    @Override
    @Transactional(
            isolation = Isolation.READ_COMMITTED,
            propagation = Propagation.REQUIRED,
            rollbackFor = {Exception.class, RuntimeException.class}
    )
    public void create(AuthorRequest authorRequest) {
        Author author = this.authorMapper.toEntity(authorRequest);
        this.authorMapper.getAuthorRepository().save(author);
    }

    @Override
    @Transactional(
            isolation = Isolation.READ_COMMITTED,
            propagation = Propagation.REQUIRED,
            rollbackFor = {Exception.class, RuntimeException.class}
    )
    public void update(AuthorRequest authorRequest) {
        Author author = this.authorMapper.toEntity(authorRequest);
        this.authorMapper.getAuthorRepository().save(author);
    }

    @Override
    @Transactional(
            isolation = Isolation.READ_COMMITTED,
            propagation = Propagation.REQUIRED,
            rollbackFor = {Exception.class, RuntimeException.class}
    )
    public void delete(UUID id) {
        Author author = this.authorMapper.getAuthorRepository().findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Author not found"));
        this.authorMapper.getAuthorRepository().delete(author);
    }
}
