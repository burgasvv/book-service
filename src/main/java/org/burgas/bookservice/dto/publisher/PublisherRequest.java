package org.burgas.bookservice.dto.publisher;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.burgas.bookservice.dto.Request;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class PublisherRequest implements Request {

    private UUID id;
    private String name;
    private String description;
}
