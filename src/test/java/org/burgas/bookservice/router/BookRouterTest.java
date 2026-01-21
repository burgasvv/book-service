package org.burgas.bookservice.router;

import org.burgas.bookservice.dto.book.BookRequest;
import org.burgas.bookservice.entity.author.Author;
import org.burgas.bookservice.entity.book.Book;
import org.burgas.bookservice.entity.publisher.Publisher;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
@TestClassOrder(value = ClassOrderer.OrderAnnotation.class)
@Order(value = 3)
public class BookRouterTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(value = 3)
    void getBooks() throws Exception {
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get("/api/v1/books")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @Order(value = 4)
    void getBookById() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        MvcResult booksResult = this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get("/api/v1/books")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andReturn();
        List<Book> books = objectMapper.readValue(booksResult.getResponse().getContentAsString(), new TypeReference<>() {
        });
        Book findBook = books.stream()
                .filter(filter -> filter.getName().equals("Test Java Developer"))
                .distinct().toList()
                .getFirst();
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get("/api/v1/books/by-id")
                                .accept(MediaType.APPLICATION_JSON)
                                .param("bookId", findBook.getId().toString())
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @Order(value = 1)
    void createBook() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Author author = Author.builder()
                .firstname("Таркаев")
                .lastname("Валерий")
                .patronymic("Игоревич")
                .about("Описание автора Таркаева Валерия Игоревича")
                .build();
        String authorString = objectMapper.writeValueAsString(author);
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.post("/api/v1/authors/create")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(authorString)
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .with(SecurityMockMvcRequestPostProcessors.httpBasic("burgasvv@gmail.com", "burgasvv"))
                );
        MvcResult authorsResult = this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get("/api/v1/authors")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andReturn();
        List<Author> authors = objectMapper.readValue(authorsResult.getResponse().getContentAsString(), new TypeReference<>() {});
        Author findAuthor = authors.stream().filter(
                        filter -> filter.getFirstname().equals("Таркаев") &&
                                  filter.getLastname().equals("Валерий") &&
                                  filter.getPatronymic().equals("Игоревич")
                )
                .distinct().toList()
                .getFirst();

        Publisher publisher = Publisher.builder()
                .name("Книголюб")
                .description("Описание издателя Книголюб")
                .build();
        String publisherString = objectMapper.writeValueAsString(publisher);
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.post("/api/v1/publishers/create")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(publisherString)
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .with(SecurityMockMvcRequestPostProcessors.httpBasic("burgasvv@gmail.com", "burgasvv"))
                )
                .andReturn();
        MvcResult publishersResult = this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get("/api/v1/publishers")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andReturn();
        List<Publisher> publishers = objectMapper.readValue(publishersResult.getResponse().getContentAsString(), new TypeReference<>() {
        });
        Publisher findPublisher = publishers.stream()
                .filter(filter -> filter.getName().equals("Книголюб"))
                .distinct().toList()
                .getFirst();

        BookRequest bookRequest = BookRequest.builder()
                .name("Test Java")
                .about("Описание книги Test java")
                .price(2500.2)
                .authorId(findAuthor.getId())
                .publisherId(findPublisher.getId())
                .build();
        String bookString = objectMapper.writeValueAsString(bookRequest);
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.post("/api/v1/books/create")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(bookString)
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .with(SecurityMockMvcRequestPostProcessors.httpBasic("burgasvv@gmail.com", "burgasvv"))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    @Test
    @Order(value = 2)
    void updateBook() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        MvcResult booksResult = this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get("/api/v1/books")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andReturn();
        List<Book> books = objectMapper.readValue(booksResult.getResponse().getContentAsString(), new TypeReference<>() {
        });
        Book findBook = books.stream()
                .filter(filter -> filter.getName().equals("Test Java"))
                .distinct().toList()
                .getFirst();
        Book book = Book.builder()
                .id(findBook.getId())
                .name("Test Java Developer")
                .build();
        String bookString = objectMapper.writeValueAsString(book);
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.put("/api/v1/books/update")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(bookString)
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .with(SecurityMockMvcRequestPostProcessors.httpBasic("burgasvv@gmail.com", "burgasvv"))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    @Test
    @Order(value = 5)
    void deleteBook() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        MvcResult booksResult = this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get("/api/v1/books")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andReturn();
        List<Book> books = objectMapper.readValue(booksResult.getResponse().getContentAsString(), new TypeReference<>() {});
        Book findBook = books.stream()
                .filter(filter -> filter.getName().equals("Test Java Developer"))
                .distinct().toList()
                .getFirst();
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.delete("/api/v1/books/delete")
                                .accept(MediaType.APPLICATION_JSON)
                                .param("bookId", findBook.getId().toString())
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .with(SecurityMockMvcRequestPostProcessors.httpBasic("burgasvv@gmail.com", "burgasvv"))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        MvcResult authorsResult = this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get("/api/v1/authors")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andReturn();
        List<Author> authors = objectMapper.readValue(authorsResult.getResponse().getContentAsString(), new TypeReference<>() {});
        Author findAuthor = authors.stream().filter(
                        filter -> filter.getFirstname().equals("Таркаев") &&
                                  filter.getLastname().equals("Валерий") &&
                                  filter.getPatronymic().equals("Игоревич")
                )
                .distinct().toList()
                .getFirst();

        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.delete("/api/v1/authors/delete")
                                .accept(MediaType.APPLICATION_JSON)
                                .param("authorId", findAuthor.getId().toString())
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .with(SecurityMockMvcRequestPostProcessors.httpBasic("burgasvv@gmail.com", "burgasvv"))
                )
                .andReturn();

        MvcResult publishersResult = this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get("/api/v1/publishers")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andReturn();
        List<Publisher> publishers = objectMapper.readValue(publishersResult.getResponse().getContentAsString(), new TypeReference<>() {
        });
        Publisher findPublisher = publishers.stream()
                .filter(filter -> filter.getName().equals("Книголюб"))
                .distinct().toList()
                .getFirst();

        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.delete("/api/v1/publishers/delete")
                                .accept(MediaType.APPLICATION_JSON)
                                .param("publisherId", findPublisher.getId().toString())
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .with(SecurityMockMvcRequestPostProcessors.httpBasic("burgasvv@gmail.com", "burgasvv"))
                )
                .andReturn();
    }
}
