package org.burgas.bookservice.dto.author;

import lombok.*;
import org.burgas.bookservice.dto.Response;
import org.burgas.bookservice.dto.book.BookWithoutAuthorResponse;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class AuthorFullResponse implements Response {

    private UUID id;
    private String firstname;
    private String lastname;
    private String patronymic;
    private String about;
    private List<BookWithoutAuthorResponse> books;
}
