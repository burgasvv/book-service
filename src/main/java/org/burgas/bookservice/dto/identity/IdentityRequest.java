package org.burgas.bookservice.dto.identity;

import lombok.*;
import org.burgas.bookservice.dto.Request;
import org.burgas.bookservice.entity.identity.Authority;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class IdentityRequest implements Request {

    private UUID id;
    private Authority authority;
    private String username;
    private String password;
    private String email;
    private String firstname;
    private String lastname;
    private String patronymic;
    private Boolean enabled;
}
