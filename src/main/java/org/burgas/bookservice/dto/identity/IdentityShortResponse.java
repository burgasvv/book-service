package org.burgas.bookservice.dto.identity;

import lombok.*;
import org.burgas.bookservice.dto.Response;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class IdentityShortResponse implements Response {

    private UUID id;
    private String username;
    private String email;
    private String firstname;
    private String lastname;
    private String patronymic;
}
