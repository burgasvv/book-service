package org.burgas.bookservice.service;

import jakarta.servlet.http.Part;
import lombok.RequiredArgsConstructor;
import org.burgas.bookservice.dto.book.BookFullResponse;
import org.burgas.bookservice.dto.book.BookRequest;
import org.burgas.bookservice.dto.book.BookShortResponse;
import org.burgas.bookservice.entity.book.Book;
import org.burgas.bookservice.entity.document.Document;
import org.burgas.bookservice.mapper.BookMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
public class BookService implements CrudService<BookRequest, BookShortResponse, BookFullResponse> {

    private final BookMapper bookMapper;
    private final DocumentService documentService;

    @Override
    public List<BookShortResponse> findAll() {
        return this.bookMapper.getBookRepository()
                .findAll()
                .stream()
                .map(this.bookMapper::toShortResponse)
                .toList();
    }

    @Override
    public BookFullResponse findById(UUID id) {
        return this.bookMapper.getBookRepository()
                .findById(id)
                .map(this.bookMapper::toFullResponse)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));
    }

    @Override
    @Transactional(
            isolation = Isolation.READ_COMMITTED,
            propagation = Propagation.REQUIRED,
            rollbackFor = {Exception.class, RuntimeException.class}
    )
    public void create(BookRequest bookRequest) {
        Book book = this.bookMapper.toEntity(bookRequest);
        this.bookMapper.getBookRepository().save(book);
    }

    @Override
    @Transactional(
            isolation = Isolation.READ_COMMITTED,
            propagation = Propagation.REQUIRED,
            rollbackFor = {Exception.class, RuntimeException.class}
    )
    public void update(BookRequest bookRequest) {
        Book book = this.bookMapper.toEntity(bookRequest);
        this.bookMapper.getBookRepository().save(book);
    }

    @Override
    @Transactional(
            isolation = Isolation.READ_COMMITTED,
            propagation = Propagation.REQUIRED,
            rollbackFor = {Exception.class, RuntimeException.class}
    )
    public void delete(UUID id) {
        Book book = this.bookMapper.getBookRepository().findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));
        this.bookMapper.getBookRepository().delete(book);
    }

    @Transactional(
            isolation = Isolation.READ_COMMITTED,
            propagation = Propagation.REQUIRED,
            rollbackFor = {Exception.class, RuntimeException.class}
    )
    public void uploadDocument(final UUID bookId, final Part part) {
        Book book = this.bookMapper.getBookRepository().findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));
        Document document = this.documentService.upload(part);
        book.setDocument(document);
    }

    @Transactional(
            isolation = Isolation.READ_COMMITTED,
            propagation = Propagation.REQUIRED,
            rollbackFor = {Exception.class, RuntimeException.class}
    )
    public void deleteDocument(final UUID bookId) {
        Book book = this.bookMapper.getBookRepository().findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));
        if (book.getDocument() != null) {
            Document document = book.getDocument();
            book.setDocument(null);
            this.documentService.delete(document.getId());

        } else {
            throw new IllegalArgumentException("Book document is null");
        }
    }
}
