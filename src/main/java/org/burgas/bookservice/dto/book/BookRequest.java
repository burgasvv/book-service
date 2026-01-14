package org.burgas.bookservice.dto.book;

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
public final class BookRequest implements Request {

    private UUID id;
    private String name;
    private UUID authorId;
    private UUID publisherId;
    private String about;
    private Double price;
}
