package org.burgas.bookservice.mapper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.burgas.bookservice.dto.author.AuthorFullResponse;
import org.burgas.bookservice.dto.author.AuthorRequest;
import org.burgas.bookservice.dto.author.AuthorShortResponse;
import org.burgas.bookservice.entity.author.Author;
import org.burgas.bookservice.repository.AuthorRepository;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public final class AuthorMapper implements Mapper<AuthorRequest, Author, AuthorShortResponse, AuthorFullResponse> {

    @Getter
    private final AuthorRepository authorRepository;

    private final ObjectFactory<BookMapper> bookMapperObjectFactory;

    private BookMapper getBookMapper() {
        return this.bookMapperObjectFactory.getObject();
    }

    @Override
    public Author toEntity(AuthorRequest authorRequest) {
        UUID authorId = this.getData(authorRequest.getId(), UUID.randomUUID());
        return this.authorRepository.findById(authorId)
                .map(
                        author -> {
                            String firstname = this.getData(authorRequest.getFirstname(), author.getFirstname());
                            String lastname = this.getData(authorRequest.getLastname(), author.getLastname());
                            String patronymic = this.getData(authorRequest.getPatronymic(), author.getPatronymic());
                            String about = this.getData(authorRequest.getAbout(), author.getAbout());
                            return Author.builder()
                                    .id(author.getId())
                                    .firstname(firstname)
                                    .lastname(lastname)
                                    .patronymic(patronymic)
                                    .about(about)
                                    .build();
                        }
                )
                .orElseGet(
                        () -> {
                            String firstname = this.getDataThrowable(authorRequest.getFirstname(), "Author firstname is null");
                            String lastname = this.getDataThrowable(authorRequest.getLastname(), "Author lastname is null");
                            String patronymic = this.getDataThrowable(authorRequest.getPatronymic(), "Author patronymic is null");
                            String about = this.getDataThrowable(authorRequest.getAbout(), "Author about is null");
                            return Author.builder()
                                    .firstname(firstname)
                                    .lastname(lastname)
                                    .patronymic(patronymic)
                                    .about(about)
                                    .build();
                        }
                );
    }

    @Override
    public AuthorShortResponse toShortResponse(Author author) {
        return AuthorShortResponse.builder()
                .id(author.getId())
                .firstname(author.getFirstname())
                .lastname(author.getLastname())
                .patronymic(author.getPatronymic())
                .about(author.getAbout())
                .build();
    }

    @Override
    public AuthorFullResponse toFullResponse(Author author) {
        return AuthorFullResponse.builder()
                .id(author.getId())
                .firstname(author.getFirstname())
                .lastname(author.getLastname())
                .patronymic(author.getPatronymic())
                .about(author.getAbout())
                .books(
                        author.getBooks()
                                .stream()
                                .map(book -> this.getBookMapper().toBookWithoutAuthorResponse(book))
                                .toList()
                )
                .build();
    }
}
