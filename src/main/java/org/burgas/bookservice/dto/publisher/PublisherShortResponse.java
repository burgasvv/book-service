package org.burgas.bookservice.dto.publisher;

import lombok.*;
import org.burgas.bookservice.dto.Response;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class PublisherShortResponse implements Response {

    private UUID id;
    private String name;
    private String description;
}
