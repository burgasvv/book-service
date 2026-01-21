package org.burgas.bookservice.router;

import org.burgas.bookservice.entity.author.Author;
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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestClassOrder(value = ClassOrderer.OrderAnnotation.class)
@Order(value = 1)
public class AuthorRouterTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(value = 3)
    void getAllAuthors() throws Exception {
        this.mockMvc.
                perform(
                        MockMvcRequestBuilders.get("/api/v1/authors")
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
    void getAuthorById() throws Exception {
        MvcResult authorsRequest = this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get("/api/v1/authors")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andReturn();
        ObjectMapper objectMapper = new ObjectMapper();
        List<Author> authors = objectMapper.readValue(authorsRequest.getResponse().getContentAsString(), new TypeReference<>(){});
        Author author = authors.stream()
                .filter(filter -> filter.getFirstname().equals("Добрянков"))
                .distinct().toList()
                .getFirst();

        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get("/api/v1/authors/by-id")
                                .accept(MediaType.APPLICATION_JSON)
                                .param("authorId", author.getId().toString())
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @Order(value = 1)
    void createAuthor() throws Exception {
        Author author = Author.builder()
                .firstname("Добряков")
                .lastname("Игорь")
                .patronymic("Вячеславович")
                .about("Описание автора Добрякова Игоря Вячеславовича")
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        String authorContent = objectMapper.writeValueAsString(author);
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.post("/api/v1/authors/create")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(authorContent)
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .with(SecurityMockMvcRequestPostProcessors.httpBasic("burgasvv@gmail.com", "burgasvv"))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @Order(value = 2)
    void updateAuthor() throws Exception {
        MvcResult authorsRequest = this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get("/api/v1/authors")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andReturn();
        ObjectMapper objectMapper = new ObjectMapper();
        List<Author> authors = objectMapper.readValue(authorsRequest.getResponse().getContentAsString(), new TypeReference<>(){});
        Author author = authors.stream()
                .filter(filter -> filter.getFirstname().equals("Добряков"))
                .distinct().toList()
                .getFirst();
        Author updateAuthor = Author.builder()
                .id(author.getId())
                .firstname("Добрянков")
                .about("Описание автора Добрянкова Игоря Вячеславовича")
                .build();
        String authorString = objectMapper.writeValueAsString(updateAuthor);
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.put("/api/v1/authors/update")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(authorString)
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .with(SecurityMockMvcRequestPostProcessors.httpBasic("burgasvv@gmail.com", "burgasvv"))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @Order(value = 5)
    void deleteAuthor() throws Exception {
        MvcResult authorsRequest = this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get("/api/v1/authors")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andReturn();
        ObjectMapper objectMapper = new ObjectMapper();
        List<Author> authors = objectMapper.readValue(authorsRequest.getResponse().getContentAsString(), new TypeReference<>(){});
        Author author = authors.stream()
                .filter(filter -> filter.getFirstname().equals("Добрянков"))
                .distinct().toList()
                .getFirst();
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.delete("/api/v1/authors/delete")
                                .accept(MediaType.APPLICATION_JSON)
                                .param("authorId", author.getId().toString())
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .with(SecurityMockMvcRequestPostProcessors.httpBasic("burgasvv@gmail.com", "burgasvv"))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }
}
