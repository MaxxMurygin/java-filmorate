package com.github.maxxmurygin.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.maxxmurygin.filmorate.model.User;
import com.github.maxxmurygin.filmorate.storage.user.InMemoryUserStorage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ContextConfiguration(classes = {UserController.class, InMemoryUserStorage.class})
@WebMvcTest
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private final User user = User.builder()
            .login("User1")
            .email("user1@userdomain.net")
            .name("User1")
            .birthday(LocalDate.of(2022, 1, 1))
            .build();

    @Test
    void findAll() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String resultString = result.getResponse().getContentAsString();
        assertNotNull(resultString);
    }

    @Test
    void createOk() throws Exception {

//        MvcResult result =
                mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("User1"))
                .andExpect(jsonPath("$.email").value("user1@userdomain.net"))
                .andExpect(jsonPath("$.birthday").value("2022-01-01"));
//                .andReturn();

//        String resultString = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
//        assertNotNull(resultString);
//        User returned = objectMapper.readValue(resultString, User.class);
//        user.setId(returned.getId());
//        assertEquals(user, returned, "Пользователи не эквивалентны.");
    }

    @Test
    void createEmpty() throws Exception {

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .content("")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(log())
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    void createWithWrongEmail() throws Exception {
        user.setEmail("");
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(log())
                .andExpect(status().is4xxClientError())
                .andReturn();

        user.setEmail("use r@user.com");
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();

        user.setEmail("user.com");
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    void createWithWrongLogin() throws Exception {
        user.setLogin("");
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();

        user.setLogin("use r");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .content(objectMapper.writeValueAsString(user)))
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();
        System.out.println();
    }

    @Test
    void createWithBirthdayInFutureAndPresent() throws Exception {
        user.setBirthday(LocalDate.now());
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        user.setBirthday(LocalDate.now().plusDays(1));
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    void updateOk() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        User updated = objectMapper.readValue(result.getResponse().getContentAsString(StandardCharsets.UTF_8),
                User.class);
        updated.setName("New Name");
        MvcResult resultUpdated = mockMvc.perform(MockMvcRequestBuilders
                        .put("/users")
                        .content(objectMapper.writeValueAsString(updated))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        User updatedReturned = objectMapper.readValue(resultUpdated.getResponse()
                        .getContentAsString(StandardCharsets.UTF_8),
                User.class);
        assertEquals(updated, updatedReturned, "Пользователи не эквивалентны.");
    }

    @Test
    void updateUnknown() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .put("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }
}