package org.burgas.bookservice.dto.book;

import lombok.*;
import org.burgas.bookservice.dto.Response;
import org.burgas.bookservice.dto.author.AuthorShortResponse;
import org.burgas.bookservice.dto.publisher.PublisherShortResponse;
import org.burgas.bookservice.entity.document.Document;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class BookFullResponse implements Response {

    private UUID id;
    private String name;
    private AuthorShortResponse author;
    private PublisherShortResponse publisher;
    private Document document;
    private String about;
    private Double price;
}
