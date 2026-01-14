package org.burgas.bookservice.router;

import lombok.RequiredArgsConstructor;
import org.burgas.bookservice.dto.publisher.PublisherRequest;
import org.burgas.bookservice.service.PublisherService;
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
public class PublisherRouter {

    private final PublisherService publisherService;

    @Bean
    public RouterFunction<ServerResponse> publisherRoutes() {
        return RouterFunctions.route()
                .GET(
                        "/api/v1/publishers",
                        _ ->
                                ServerResponse
                                        .status(HttpStatus.OK)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(this.publisherService.findAll())
                )
                .GET(
                        "/api/v1/publishers/by-id",
                        request ->
                                ServerResponse
                                        .status(HttpStatus.OK)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(
                                                this.publisherService.findById(
                                                        UUID.fromString(request.param("publisherId").orElseThrow())
                                                )
                                        )
                )
                .POST(
                        "/api/v1/publishers/create",
                        request -> {
                            this.publisherService.create(request.body(PublisherRequest.class));
                            return ServerResponse.ok().build();
                        }
                )
                .PUT(
                        "/api/v1/publishers/update",
                        request -> {
                            this.publisherService.update(request.body(PublisherRequest.class));
                            return ServerResponse.ok().build();
                        }
                )
                .DELETE(
                        "/api/v1/publishers/delete",
                        request -> {
                            this.publisherService.delete(UUID.fromString(request.param("publisherId").orElseThrow()));
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
