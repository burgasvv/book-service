package org.burgas.bookservice.dto.book;

import lombok.*;
import org.burgas.bookservice.dto.Response;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class BookShortResponse implements Response {

    private UUID id;
    private String name;
    private String about;
    private Double price;
}
