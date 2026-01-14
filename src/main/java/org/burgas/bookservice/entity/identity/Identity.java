package org.burgas.bookservice.entity.identity;

import jakarta.persistence.*;
import lombok.*;
import org.burgas.bookservice.entity.Model;
import org.burgas.bookservice.entity.book.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "identity", schema = "public")
@NamedEntityGraph(
        name = "identity-entity-graph",
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
                        attributeNodes = @NamedAttributeNode(value = "publisher")
                ),
                @NamedSubgraph(
                        name = "books-subgraph",
                        attributeNodes = @NamedAttributeNode(value = "document")
                )
        }
)
public final class Identity implements Model {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, unique = true)
    private UUID id;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "authority", nullable = false)
    private Authority authority;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "firstname", nullable = false)
    private String firstname;

    @Column(name = "lastname", nullable = false)
    private String lastname;

    @Column(name = "patronymic", nullable = false)
    private String patronymic;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "identity_book",
            joinColumns = @JoinColumn(name = "identity_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id")
    )
    private List<Book> books = new ArrayList<>();
}

