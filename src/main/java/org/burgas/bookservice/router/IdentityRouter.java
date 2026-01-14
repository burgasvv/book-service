package org.burgas.bookservice.router;

import lombok.RequiredArgsConstructor;
import org.burgas.bookservice.dto.identity.IdentityRequest;
import org.burgas.bookservice.filter.IdentityFilter;
import org.burgas.bookservice.service.IdentityService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class IdentityRouter {

    private final IdentityService identityService;
    private final IdentityFilter identityFilter;

    @Bean
    public RouterFunction<ServerResponse> identityRoutes() {
        return RouterFunctions.route()
                .filter(this.identityFilter)
                .GET(
                        "/api/v1/identities",
                        _ ->
                                ServerResponse
                                        .status(HttpStatus.OK)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(this.identityService.findAll())
                )
                .GET(
                        "/api/v1/identities/by-id",
                        request ->
                                ServerResponse
                                        .status(HttpStatus.OK)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(
                                                this.identityService.findById(
                                                        UUID.fromString(request.param("identityId").orElseThrow())
                                                )
                                        )
                )
                .POST(
                        "/api/v1/identities/create",
                        request -> {
                            this.identityService.create(request.body(IdentityRequest.class));
                            return ServerResponse.ok().build();
                        }
                )
                .PUT(
                        "/api/v1/identities/update",
                        request -> {
                            IdentityRequest identityRequest = (IdentityRequest) request.attribute("identityRequest").orElseThrow();
                            this.identityService.update(identityRequest);
                            return ServerResponse.ok().build();
                        }
                )
                .DELETE(
                        "/api/v1/identities/delete",
                        request -> {
                            this.identityService.delete(UUID.fromString(request.param("identityId").orElseThrow()));
                            return ServerResponse.ok().build();
                        }
                )
                .PUT(
                        "/api/v1/identities/change-password",
                        request -> {
                            IdentityRequest identityRequest = (IdentityRequest) request.attribute("identityRequest").orElseThrow();
                            this.identityService.changePassword(identityRequest);
                            return ServerResponse.ok().build();
                        }
                )
                .PUT(
                        "/api/v1/identities/change-status",
                        request -> {
                            this.identityService.changeStatus(request.body(IdentityRequest.class));
                            return ServerResponse.ok().build();
                        }
                )
                .POST(
                        "/api/v1/identities/add-book",
                        request -> {
                            this.identityService.addBook(
                                    UUID.fromString(request.param("identityId").orElseThrow()),
                                    UUID.fromString(request.param("bookId").orElseThrow())
                            );
                            return ServerResponse.ok().build();
                        }
                )
                .DELETE(
                        "/api/v1/identities/remove-book",
                        request -> {
                            this.identityService.removeBook(
                                    UUID.fromString(request.param("identityId").orElseThrow()),
                                    UUID.fromString(request.param("bookId").orElseThrow())
                            );
                            return ServerResponse.ok().build();
                        }
                )
                .onError(
                        Exception.class, (throwable, _) ->
                                ServerResponse.status(HttpStatus.BAD_REQUEST).body(throwable.getLocalizedMessage())
                )
                .build();
    }
}
