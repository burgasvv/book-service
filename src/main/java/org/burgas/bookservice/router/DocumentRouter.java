package org.burgas.bookservice.router;

import lombok.RequiredArgsConstructor;
import org.burgas.bookservice.entity.document.Document;
import org.burgas.bookservice.service.DocumentService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

import java.io.ByteArrayInputStream;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class DocumentRouter {

    private final DocumentService documentService;

    @Bean
    public RouterFunction<ServerResponse> documentRoutes() {
        return RouterFunctions.route()
                .GET(
                        "/api/v1/documents/by-id",
                        request -> {
                            Document document = this.documentService.findById(
                                    UUID.fromString(request.param("documentId").orElseThrow())
                            );
                            return ServerResponse
                                    .status(HttpStatus.OK)
                                    .contentType(MediaType.parseMediaType(document.getContentType()))
                                    .body(
                                            new InputStreamResource(
                                                    new ByteArrayInputStream(document.getData())
                                            )
                                    );
                        }
                )
                .onError(
                        Exception.class, (throwable, _) ->
                                ServerResponse.status(HttpStatus.BAD_REQUEST).body(throwable.getLocalizedMessage())
                )
                .build();
    }
}
