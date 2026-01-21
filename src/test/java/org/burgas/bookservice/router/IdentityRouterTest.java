package org.burgas.bookservice.router;

import org.burgas.bookservice.dto.identity.IdentityRequest;
import org.burgas.bookservice.entity.identity.Authority;
import org.burgas.bookservice.entity.identity.Identity;
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
@Order(value = 4)
public class IdentityRouterTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(value = 2)
    void getIdentities() throws Exception {
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get("/api/v1/identities")
                                .accept(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.httpBasic("burgasvv@gmail.com", "burgasvv"))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    @Order(value = 3)
    void getIdentityById() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        MvcResult identitiesResult = this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get("/api/v1/identities")
                                .accept(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.httpBasic("burgasvv@gmail.com", "burgasvv"))
                )
                .andReturn();
        List<Identity> identities = objectMapper.readValue(identitiesResult.getResponse().getContentAsString(), new TypeReference<>() {});
        Identity identity = identities.stream()
                .filter(filter -> filter.getEmail().equals("user@gmail.com"))
                .distinct().toList()
                .getFirst();
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get("/api/v1/identities/by-id")
                                .accept(MediaType.APPLICATION_JSON)
                                .param("identityId", identity.getId().toString())
                                .with(SecurityMockMvcRequestPostProcessors.httpBasic("user@gmail.com", "user"))
                )
                .andReturn();
    }

    @Test
    @Order(value = 1)
    void createIdentity() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        IdentityRequest identityRequest = IdentityRequest.builder()
                .authority(Authority.USER)
                .username("user")
                .password("user")
                .email("user@gmail.com")
                .enabled(true)
                .firstname("User")
                .lastname("User")
                .patronymic("User")
                .build();
        String identityString = objectMapper.writeValueAsString(identityRequest);
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.post("/api/v1/identities/create")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(identityString)
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    @Test
    @Order(value = 4)
    void deleteIdentity() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        MvcResult identitiesResult = this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get("/api/v1/identities")
                                .accept(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.httpBasic("burgasvv@gmail.com", "burgasvv"))
                )
                .andReturn();
        List<Identity> identities = objectMapper.readValue(identitiesResult.getResponse().getContentAsString(), new TypeReference<>() {});
        Identity identity = identities.stream()
                .filter(filter -> filter.getEmail().equals("user@gmail.com"))
                .distinct().toList()
                .getFirst();
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.delete("/api/v1/identities/delete")
                                .accept(MediaType.APPLICATION_JSON)
                                .param("identityId", identity.getId().toString())
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .with(SecurityMockMvcRequestPostProcessors.httpBasic("user@gmail.com", "user"))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }
}
