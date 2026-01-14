package org.burgas.bookservice.router;

import lombok.RequiredArgsConstructor;
import org.burgas.bookservice.dto.book.BookRequest;
import org.burgas.bookservice.service.BookService;
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
public class BookRouter {

    private final BookService bookService;

    @Bean
    public RouterFunction<ServerResponse> bookRoutes() {
        return RouterFunctions.route()
                .GET(
                        "/api/v1/books",
                        _ ->
                                ServerResponse
                                        .status(HttpStatus.OK)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(this.bookService.findAll())
                )
                .GET(
                        "/api/v1/books/by-id",
                        request ->
                                ServerResponse
                                        .status(HttpStatus.OK)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(
                                                this.bookService.findById(
                                                        UUID.fromString(request.param("bookId").orElseThrow())
                                                )
                                        )
                )
                .POST(
                        "/api/v1/books/create",
                        request -> {
                            this.bookService.create(request.body(BookRequest.class));
                            return ServerResponse.ok().build();
                        }
                )
                .PUT(
                        "/api/v1/books/update",
                        request -> {
                            this.bookService.update(request.body(BookRequest.class));
                            return ServerResponse.ok().build();
                        }
                )
                .DELETE(
                        "/api/v1/books/delete",
                        request -> {
                            this.bookService.delete(UUID.fromString(request.param("bookId").orElseThrow()));
                            return ServerResponse.ok().build();
                        }
                )
                .POST(
                        "/api/v1/books/upload-document",
                        request -> {
                            this.bookService.uploadDocument(
                                    UUID.fromString(request.param("bookId").orElseThrow()),
                                    request.multipartData().asSingleValueMap().get("book")
                            );
                            return ServerResponse.ok().build();
                        }
                )
                .DELETE(
                        "/api/v1/books/delete-document",
                        request -> {
                            this.bookService.deleteDocument(UUID.fromString(request.param("bookId").orElseThrow()));
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
