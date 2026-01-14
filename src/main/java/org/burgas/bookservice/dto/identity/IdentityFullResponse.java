package org.burgas.bookservice.dto.identity;

import lombok.*;
import org.burgas.bookservice.dto.Response;
import org.burgas.bookservice.dto.book.BookFullResponse;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class IdentityFullResponse implements Response {

    private UUID id;
    private String username;
    private String email;
    private String firstname;
    private String lastname;
    private String patronymic;
    private List<BookFullResponse> books;
}
