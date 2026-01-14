package org.burgas.bookservice.dto.book;

import lombok.*;
import org.burgas.bookservice.dto.Response;
import org.burgas.bookservice.dto.author.AuthorShortResponse;
import org.burgas.bookservice.entity.document.Document;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class BookWithoutPublisherResponse implements Response {

    private UUID id;
    private String name;
    private AuthorShortResponse author;
    private Document document;
    private String about;
    private Double price;
}
