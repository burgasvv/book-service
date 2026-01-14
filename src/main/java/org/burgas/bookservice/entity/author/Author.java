package org.burgas.bookservice.entity.author;

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
@Table(name = "author", schema = "public")
@NamedEntityGraph(
        name = "author-entity-graph",
        attributeNodes = {
                @NamedAttributeNode(value = "books", subgraph = "books-subgraph")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "books-subgraph",
                        attributeNodes = @NamedAttributeNode(value = "publisher")
                ),
                @NamedSubgraph(
                        name = "books-subgraph",
                        attributeNodes = @NamedAttributeNode(value = "document")
                )
        }
)
public final class Author implements Model {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, unique = true)
    private UUID id;

    @Column(name = "firstname", nullable = false)
    private String firstname;

    @Column(name = "lastname", nullable = false)
    private String lastname;

    @Column(name = "patronymic", nullable = false)
    private String patronymic;

    @Column(name = "about", nullable = false, unique = true)
    private String about;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private List<Book> books = new ArrayList<>();
}
