package org.burgas.bookservice.mapper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.burgas.bookservice.dto.identity.IdentityFullResponse;
import org.burgas.bookservice.dto.identity.IdentityRequest;
import org.burgas.bookservice.dto.identity.IdentityShortResponse;
import org.burgas.bookservice.entity.identity.Authority;
import org.burgas.bookservice.entity.identity.Identity;
import org.burgas.bookservice.repository.IdentityRepository;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
@RequiredArgsConstructor
public final class IdentityMapper implements Mapper<IdentityRequest, Identity, IdentityShortResponse, IdentityFullResponse> {

    @Getter
    private final IdentityRepository identityRepository;
    private final PasswordEncoder passwordEncoder;

    private final ObjectFactory<BookMapper> bookMapperObjectFactory;

    private BookMapper getBookMapper() {
        return this.bookMapperObjectFactory.getObject();
    }

    @Override
    public Identity toEntity(IdentityRequest identityRequest) {
        UUID identityId = identityRequest.getId() != null ? identityRequest.getId() : UUID.randomUUID();
        return this.identityRepository.findById(identityId)
                .map(
                        identity -> {
                            Authority authority = this.getData(identityRequest.getAuthority(), identity.getAuthority());
                            String username = this.getData(identityRequest.getUsername(), identity.getUsername());
                            String email = this.getData(identityRequest.getEmail(), identity.getEmail());
                            String firstname = this.getData(identityRequest.getFirstname(), identity.getFirstname());
                            String lastname = this.getData(identityRequest.getLastname(), identity.getLastname());
                            String patronymic = this.getData(identityRequest.getPatronymic(), identity.getPatronymic());
                            Boolean enabled = this.getData(identityRequest.getEnabled(), identity.getEnabled());
                            return Identity.builder()
                                    .id(identity.getId())
                                    .authority(authority)
                                    .username(username)
                                    .password(identity.getPassword())
                                    .email(email)
                                    .firstname(firstname)
                                    .lastname(lastname)
                                    .patronymic(patronymic)
                                    .enabled(enabled)
                                    .build();
                        }
                )
                .orElseGet(
                        () -> {
                            Authority authority = this.getDataThrowable(identityRequest.getAuthority(), "Identity authority is null");
                            String username = this.getDataThrowable(identityRequest.getUsername(), "Identity username is null");
                            String password = this.getDataThrowable(identityRequest.getPassword(), "Identity password is null");
                            String newPassword = this.passwordEncoder.encode(password);
                            String email = this.getDataThrowable(identityRequest.getEmail(), "Identity email is null");
                            String firstname = this.getDataThrowable(identityRequest.getFirstname(), "Identity firstname is null");
                            String lastname = this.getDataThrowable(identityRequest.getLastname(), "Identity lastname is null");
                            String patronymic = this.getDataThrowable(identityRequest.getPatronymic(), "Identity patronymic is null");
                            Boolean enabled = this.getDataThrowable(identityRequest.getEnabled(), "Identity enabled is null");
                            return Identity.builder()
                                    .authority(authority)
                                    .username(username)
                                    .password(newPassword)
                                    .email(email)
                                    .firstname(firstname)
                                    .lastname(lastname)
                                    .patronymic(patronymic)
                                    .enabled(enabled)
                                    .build();
                        }
                );
    }

    @Override
    public IdentityShortResponse toShortResponse(Identity identity) {
        return IdentityShortResponse.builder()
                .id(identity.getId())
                .username(identity.getUsername())
                .email(identity.getEmail())
                .firstname(identity.getFirstname())
                .lastname(identity.getLastname())
                .patronymic(identity.getPatronymic())
                .build();
    }

    @Override
    public IdentityFullResponse toFullResponse(Identity identity) {
        return IdentityFullResponse.builder()
                .id(identity.getId())
                .username(identity.getUsername())
                .email(identity.getEmail())
                .firstname(identity.getFirstname())
                .lastname(identity.getLastname())
                .patronymic(identity.getPatronymic())
                .books(
                        identity.getBooks()
                                .stream()
                                .map(book -> this.getBookMapper().toFullResponse(book))
                                .toList()
                )
                .build();
    }
}
