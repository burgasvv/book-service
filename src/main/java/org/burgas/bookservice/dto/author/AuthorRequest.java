package org.burgas.bookservice.dto.author;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.burgas.bookservice.dto.Request;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class AuthorRequest implements Request {

    private UUID id;
    private String firstname;
    private String lastname;
    private String patronymic;
    private String about;
}
