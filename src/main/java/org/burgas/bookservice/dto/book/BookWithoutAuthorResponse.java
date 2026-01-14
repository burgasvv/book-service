package org.burgas.bookservice.dto.book;

import lombok.*;
import org.burgas.bookservice.dto.Response;
import org.burgas.bookservice.dto.publisher.PublisherShortResponse;
import org.burgas.bookservice.entity.document.Document;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class BookWithoutAuthorResponse implements Response {

    private UUID id;
    private String name;
    private PublisherShortResponse publisher;
    private Document document;
    private String about;
    private Double price;
}
