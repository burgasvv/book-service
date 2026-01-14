package org.burgas.bookservice.entity.publisher;

import jakarta.persistence.*;
import lombok.*;
import org.burgas.bookservice.entity.Model;
import org.burgas.bookservice.entity.book.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "publisher", schema = "public")
@NamedEntityGraph(
        name = "publisher-entity-graph",
        attributeNodes = {
                @NamedAttributeNode(value = "books", subgraph = "books-subgraph")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "books-subgraph",
                        attributeNodes = @NamedAttributeNode(value = "author")
                ),
                @NamedSubgraph(
                        name = "books-subgraph",
                        attributeNodes = @NamedAttributeNode(value = "document")
                )
        }
)
public final class Publisher implements Model {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, unique = true)
    private UUID id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "description", unique = true, nullable = false)
    private String description;

    @OneToMany(mappedBy = "publisher", fetch = FetchType.LAZY)
    private List<Book> books = new ArrayList<>();
}
