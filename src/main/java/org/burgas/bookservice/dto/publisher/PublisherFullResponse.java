package org.burgas.bookservice.dto.publisher;

import lombok.*;
import org.burgas.bookservice.dto.Response;
import org.burgas.bookservice.dto.book.BookWithoutPublisherResponse;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class PublisherFullResponse implements Response {

    private UUID id;
    private String name;
    private String description;
    private List<BookWithoutPublisherResponse> books;
}
