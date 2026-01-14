package org.burgas.bookservice.mapper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.burgas.bookservice.dto.book.*;
import org.burgas.bookservice.entity.author.Author;
import org.burgas.bookservice.entity.book.Book;
import org.burgas.bookservice.entity.publisher.Publisher;
import org.burgas.bookservice.repository.BookRepository;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public final class BookMapper implements Mapper<BookRequest, Book, BookShortResponse, BookFullResponse> {

    @Getter
    private final BookRepository bookRepository;

    private final ObjectFactory<AuthorMapper> authorMapperObjectFactory;
    private final ObjectFactory<PublisherMapper> publisherMapperObjectFactory;

    private AuthorMapper getAuthorMapper() {
        return this.authorMapperObjectFactory.getObject();
    }

    private PublisherMapper getPublisherMapper() {
        return this.publisherMapperObjectFactory.getObject();
    }

    @Override
    public Book toEntity(BookRequest bookRequest) {
        UUID bookId = this.getData(bookRequest.getId(), UUID.randomUUID());
        return this.bookRepository.findById(bookId)
                .map(
                        book -> {
                            String name = this.getData(bookRequest.getName(), book.getName());

                            UUID authorId = this.getData(bookRequest.getAuthorId(), UUID.randomUUID());
                            Author authorOeNull = this.getAuthorMapper().getAuthorRepository().findById(authorId).orElse(null);
                            Author author = this.getData(authorOeNull, book.getAuthor());

                            UUID publisherId = this.getData(bookRequest.getPublisherId(), UUID.randomUUID());
                            Publisher publisherOrNull = this.getPublisherMapper().getPublisherRepository().findById(publisherId).orElse(null);
                            Publisher publisher = this.getData(publisherOrNull, book.getPublisher());

                            String about = this.getData(bookRequest.getAbout(), book.getAbout());
                            Double price = this.getData(bookRequest.getPrice(), book.getPrice());
                            return Book.builder()
                                    .id(book.getId())
                                    .name(name)
                                    .author(author)
                                    .publisher(publisher)
                                    .about(about)
                                    .price(price)
                                    .build();
                        }
                )
                .orElseGet(
                        () -> {
                            String name = this.getDataThrowable(bookRequest.getName(), "Book name is null");

                            UUID authorId = this.getDataThrowable(bookRequest.getAuthorId(), "Book author id is null");
                            Author author = this.getAuthorMapper().getAuthorRepository().findById(authorId)
                                    .orElseThrow(() -> new IllegalArgumentException("Book author not found"));

                            UUID publisherId = this.getDataThrowable(bookRequest.getPublisherId(), "Book publisher id is null");
                            Publisher publisher = this.getPublisherMapper().getPublisherRepository().findById(publisherId)
                                    .orElseThrow(() -> new IllegalArgumentException("Book publisher not found"));

                            String about = this.getDataThrowable(bookRequest.getAbout(), "Book about is null");
                            Double price = this.getDataThrowable(bookRequest.getPrice(), "Book price is null");

                            return Book.builder()
                                    .name(name)
                                    .author(author)
                                    .publisher(publisher)
                                    .about(about)
                                    .price(price)
                                    .build();
                        }
                );
    }

    @Override
    public BookShortResponse toShortResponse(Book book) {
        return BookShortResponse.builder()
                .id(book.getId())
                .name(book.getName())
                .about(book.getAbout())
                .price(book.getPrice())
                .build();
    }

    @Override
    public BookFullResponse toFullResponse(Book book) {
        return BookFullResponse.builder()
                .id(book.getId())
                .name(book.getName())
                .author(
                        Optional.ofNullable(book.getAuthor())
                                .map(author -> this.getAuthorMapper().toShortResponse(author))
                                .orElse(null)
                )
                .publisher(
                        Optional.ofNullable(book.getPublisher())
                                .map(publisher -> this.getPublisherMapper().toShortResponse(publisher))
                                .orElse(null)
                )
                .document(book.getDocument())
                .about(book.getAbout())
                .price(book.getPrice())
                .build();
    }

    public BookWithoutAuthorResponse toBookWithoutAuthorResponse(Book book) {
        return BookWithoutAuthorResponse.builder()
                .id(book.getId())
                .name(book.getName())
                .publisher(
                        Optional.ofNullable(book.getPublisher())
                                .map(publisher -> this.getPublisherMapper().toShortResponse(publisher))
                                .orElse(null)
                )
                .document(book.getDocument())
                .about(book.getAbout())
                .price(book.getPrice())
                .build();
    }

    public BookWithoutPublisherResponse toBookWithoutPublisherResponse(Book book) {
        return BookWithoutPublisherResponse.builder()
                .id(book.getId())
                .name(book.getName())
                .author(
                        Optional.ofNullable(book.getAuthor())
                                .map(author -> this.getAuthorMapper().toShortResponse(author))
                                .orElse(null)
                )
                .document(book.getDocument())
                .about(book.getAbout())
                .price(book.getPrice())
                .build();
    }
}
