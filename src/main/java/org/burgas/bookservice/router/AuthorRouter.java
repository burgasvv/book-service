package org.burgas.bookservice.router;

import lombok.RequiredArgsConstructor;
import org.burgas.bookservice.dto.author.AuthorRequest;
import org.burgas.bookservice.service.AuthorService;
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
public class AuthorRouter {

    private final AuthorService authorService;

    @Bean
    public RouterFunction<ServerResponse> authorRoutes() {
        return RouterFunctions.route()
                .GET(
                        "/api/v1/authors",
                        _ ->
                                ServerResponse
                                        .status(HttpStatus.OK)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(this.authorService.findAll())
                )
                .GET(
                        "/api/v1/authors/by-id",
                        request ->
                                ServerResponse
                                        .status(HttpStatus.OK)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(
                                                this.authorService.findById(
                                                        UUID.fromString(request.param("authorId").orElseThrow())
                                                )
                                        )
                )
                .POST(
                        "/api/v1/authors/create",
                        request -> {
                            this.authorService.create(request.body(AuthorRequest.class));
                            return ServerResponse.ok().build();
                        }
                )
                .PUT(
                        "/api/v1/authors/update",
                        request -> {
                            this.authorService.update(request.body(AuthorRequest.class));
                            return ServerResponse.ok().build();
                        }
                )
                .DELETE(
                        "/api/v1/authors/delete",
                        request -> {
                            this.authorService.delete(UUID.fromString(request.param("authorId").orElseThrow()));
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
