package org.burgas.bookservice.entity.book;

import jakarta.persistence.*;
import lombok.*;
import org.burgas.bookservice.entity.Model;
import org.burgas.bookservice.entity.author.Author;
import org.burgas.bookservice.entity.document.Document;
import org.burgas.bookservice.entity.publisher.Publisher;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "book", schema = "public")
@NamedEntityGraph(
        name = "book-entity-graph",
        attributeNodes = {
                @NamedAttributeNode(value = "author"),
                @NamedAttributeNode(value = "publisher"),
                @NamedAttributeNode(value = "document")
        }
)
public final class Book implements Model {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, unique = true)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private Author author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id", referencedColumnName = "id")
    private Publisher publisher;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", referencedColumnName = "id")
    private Document document;

    @Column(name = "about", unique = true, nullable = false)
    private String about;

    @Column(name = "price", nullable = false)
    private Double price;
}
