package org.burgas.bookservice.service;

import lombok.RequiredArgsConstructor;
import org.burgas.bookservice.dto.identity.IdentityFullResponse;
import org.burgas.bookservice.dto.identity.IdentityRequest;
import org.burgas.bookservice.dto.identity.IdentityShortResponse;
import org.burgas.bookservice.entity.book.Book;
import org.burgas.bookservice.entity.identity.Identity;
import org.burgas.bookservice.mapper.BookMapper;
import org.burgas.bookservice.mapper.IdentityMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class IdentityService implements CrudService<IdentityRequest, IdentityShortResponse, IdentityFullResponse> {

    private final IdentityMapper identityMapper;
    private final BookMapper bookMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<IdentityShortResponse> findAll() {
        return this.identityMapper.getIdentityRepository()
                .findAll()
                .stream()
                .map(this.identityMapper::toShortResponse)
                .toList();
    }

    @Override
    public IdentityFullResponse findById(UUID id) {
        return this.identityMapper.getIdentityRepository()
                .findById(id)
                .map(this.identityMapper::toFullResponse)
                .orElseThrow(() -> new IllegalArgumentException("Identity not found"));
    }

    @Override
    @Transactional(
            isolation = Isolation.READ_COMMITTED,
            propagation = Propagation.REQUIRED,
            rollbackFor = {Exception.class, RuntimeException.class}
    )
    public void create(IdentityRequest identityRequest) {
        Identity identity = this.identityMapper.toEntity(identityRequest);
        this.identityMapper.getIdentityRepository().save(identity);
    }

    @Override
    @Transactional(
            isolation = Isolation.READ_COMMITTED,
            propagation = Propagation.REQUIRED,
            rollbackFor = {Exception.class, RuntimeException.class}
    )
    public void update(IdentityRequest identityRequest) {
        if (identityRequest.getId() == null) {
            throw new IllegalArgumentException("Identity id is null");
        }
        Identity identity = this.identityMapper.toEntity(identityRequest);
        this.identityMapper.getIdentityRepository().save(identity);
    }

    @Override
    @Transactional(
            isolation = Isolation.READ_COMMITTED,
            propagation = Propagation.REQUIRED,
            rollbackFor = {Exception.class, RuntimeException.class}
    )
    public void delete(UUID id) {
        Identity identity = this.identityMapper.getIdentityRepository()
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Identity not found"));
        this.identityMapper.getIdentityRepository().delete(identity);
    }

    @Transactional(
            isolation = Isolation.READ_COMMITTED,
            propagation = Propagation.REQUIRED,
            rollbackFor = {Exception.class, RuntimeException.class}
    )
    public void changePassword(final IdentityRequest identityRequest) {
        if (identityRequest.getId() == null) {
            throw new IllegalArgumentException("Identity id is null");
        }
        Identity identity = this.identityMapper.getIdentityRepository().findIdentityById(identityRequest.getId())
                .orElseThrow(() -> new IllegalArgumentException("Identity not found"));
        if (identityRequest.getPassword() == null) {
            throw new IllegalArgumentException("New password is null");
        }
        if (this.passwordEncoder.matches(identityRequest.getPassword(), identity.getPassword())) {
            throw new IllegalArgumentException("Password matched");
        }
        identity.setPassword(this.passwordEncoder.encode(identityRequest.getPassword()));
    }

    @Transactional(
            isolation = Isolation.READ_COMMITTED,
            propagation = Propagation.REQUIRED,
            rollbackFor = {Exception.class, RuntimeException.class}
    )
    public void changeStatus(final IdentityRequest identityRequest) {
        if (identityRequest.getId() == null) {
            throw new IllegalArgumentException("Identity id is null");
        }
        Identity identity = this.identityMapper.getIdentityRepository().findIdentityById(identityRequest.getId())
                .orElseThrow(() -> new IllegalArgumentException("Identity not found"));
        if (identityRequest.getEnabled() == null) {
            throw new IllegalArgumentException("Identity status is null");
        }
        if (identity.getEnabled() == identityRequest.getEnabled()) {
            throw new IllegalArgumentException("Matched identity statuses");
        }
        identity.setEnabled(identityRequest.getEnabled());
    }

    @Transactional(
            isolation = Isolation.READ_COMMITTED,
            propagation = Propagation.REQUIRED,
            rollbackFor = {Exception.class, RuntimeException.class}
    )
    public void addBook(final UUID identityId, final UUID bookId) {
        Identity identity = this.identityMapper.getIdentityRepository().findIdentityById(identityId)
                .orElseThrow(() -> new IllegalArgumentException("Identity not found"));
        Book book = this.bookMapper.getBookRepository().findBookById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));
        if (identity.getBooks().stream().map(Book::getId).toList().contains(book.getId())) {
            throw new IllegalArgumentException("This book already added");
        }
        identity.getBooks().add(book);
    }

    @Transactional(
            isolation = Isolation.READ_COMMITTED,
            propagation = Propagation.REQUIRED,
            rollbackFor = {Exception.class, RuntimeException.class}
    )
    public void removeBook(final UUID identityId, final UUID bookId) {
        Identity identity = this.identityMapper.getIdentityRepository().findIdentityById(identityId)
                .orElseThrow(() -> new IllegalArgumentException("Identity not found"));
        Book book = this.bookMapper.getBookRepository().findBookById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));
        identity.getBooks().removeIf(findBook -> findBook.getId().equals(book.getId()));
    }
}
