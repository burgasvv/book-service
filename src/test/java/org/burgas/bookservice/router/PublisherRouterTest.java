package org.burgas.bookservice.router;

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
@Order(value = 2)
public class PublisherRouterTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(value = 3)
    void getAllPublishers() throws Exception {
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get("/api/v1/publishers")
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
    void getPublisherById() throws Exception {
        MvcResult mvcResult = this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get("/api/v1/publishers")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andReturn();
        ObjectMapper objectMapper = new ObjectMapper();
        List<Publisher> publishers = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });
        Publisher findPublisher = publishers.stream()
                .filter(filter -> filter.getName().equals("Буклинг Софт"))
                .distinct().toList()
                .getFirst();
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get("/api/v1/publishers/by-id")
                                .accept(MediaType.APPLICATION_JSON)
                                .param("publisherId", findPublisher.getId().toString())
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @Order(value = 1)
    void createPublisher() throws Exception {
        Publisher publisher = Publisher.builder()
                .name("Буклинг")
                .description("Описание издателя Буклинг")
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
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
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    @Test
    @Order(value = 2)
    void updatePublisher() throws Exception {
        MvcResult mvcResult = this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get("/api/v1/publishers")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andReturn();
        ObjectMapper objectMapper = new ObjectMapper();
        List<Publisher> publishers = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });
        Publisher findPublisher = publishers.stream()
                .filter(filter -> filter.getName().equals("Буклинг"))
                .distinct().toList()
                .getFirst();
        Publisher publisher = Publisher.builder()
                .id(findPublisher.getId())
                .name("Буклинг Софт")
                .description("Описание издателя Буклинг Софт")
                .build();
        String publisherString = objectMapper.writeValueAsString(publisher);
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.put("/api/v1/publishers/update")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(publisherString)
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .with(SecurityMockMvcRequestPostProcessors.httpBasic("burgasvv@gmail.com", "burgasvv"))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    @Test
    @Order(value = 5)
    void deletePublisher() throws Exception {
        MvcResult mvcResult = this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get("/api/v1/publishers")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andReturn();
        ObjectMapper objectMapper = new ObjectMapper();
        List<Publisher> publishers = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });
        Publisher findPublisher = publishers.stream()
                .filter(filter -> filter.getName().equals("Буклинг Софт"))
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
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }
}
