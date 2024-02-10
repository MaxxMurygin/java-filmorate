package com.github.maxxmurygin.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.maxxmurygin.filmorate.model.Film;
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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ContextConfiguration(classes = {FilmController.class})
@WebMvcTest
class FilmControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private final Film film = Film.builder()
            .name("Film")
            .description("Адъ, трэшъ и содомiя")
            .releaseDate(LocalDate.of(2022, 1, 1))
            .duration(90)
            .build();

    @Test
    void findAll() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String resultString = result.getResponse().getContentAsString();
        assertNotNull(resultString);
    }

    @Test
    void createOk() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/films")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();

        String resultString = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        assertNotNull(resultString);
    }

    @Test
    void createEmpty() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders
                    .post("/films")
                    .content("")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    void createWithWrongName() throws Exception {
        film.setName("");
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/films")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    void createWith201SymbolsDescription() throws Exception {
        film.setDescription("200 symbols description 200 symbols description 200 symbols description " +
                "200 symbols description 200 symbols description 200 symbols description 200 symbols description " +
                "200 symbols description 200 symb1");
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/films")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    void createWith200SymbolsDescription() throws Exception {
        film.setDescription("200 symbols description 200 symbols description 200 symbols description " +
                "200 symbols description 200 symbols description 200 symbols description 200 symbols description " +
                "200 symbols description 200 symb");
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/films")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void createWithTooEarlyAndBorderReleaseDate() throws Exception {
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/films")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();

        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/films")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void createWithWrongDuration() throws Exception {
        film.setDuration(0);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/films")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    void updateOk() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/films")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        Film updated = objectMapper.readValue(result.getResponse().getContentAsString(StandardCharsets.UTF_8),
                Film.class);
        updated.setName("New Name");
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/films")
                        .content(objectMapper.writeValueAsString(updated))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void updateUnknown() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/films")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }
}