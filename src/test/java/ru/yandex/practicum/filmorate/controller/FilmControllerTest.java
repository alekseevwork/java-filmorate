package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FilmController.class)
public class FilmControllerTest {

    @MockBean
    private InMemoryFilmStorage filmService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetFindAllEmptyFilmsMap() throws Exception {
        Mockito.when(filmService.findAll()).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/films"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
        verify(filmService, times(1)).findAll();
    }

    @Test
    void testGetFindAllFilmsMap() throws Exception {
        Film film = Film.builder()
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(1997, 3, 25))
                .duration(100).build();
        List<Film> filmsList = new ArrayList<>();
        filmsList.add(film);
        String filmsJson = objectMapper.writeValueAsString(filmsList);

        Mockito.when(filmService.findAll()).thenReturn(filmsList);
        mockMvc.perform(get("/films"))
                .andExpect(status().isOk())
                .andExpect(content().json(filmsJson, true));
    }

    @Test
    void testPostFilmCreate() throws Exception {
        Film filmRequest = Film.builder()
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(1997, 3, 25))
                .duration(100).build();

        Film filmResponse = Film.builder()
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(1997, 3, 25))
                .duration(100)
                .id(1L).build();

        String request = objectMapper.writeValueAsString(filmRequest);
        String response = objectMapper.writeValueAsString(filmResponse);
        when(filmService.create(filmRequest)).thenReturn(filmResponse);

        mockMvc.perform(post("/films").contentType(MediaType.APPLICATION_JSON).content(request))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(response, true));
        verify(filmService, times(1)).create(filmRequest);
    }

    @Test
    void testFailNameIsEmptyFilmCreate() throws Exception {
        Film filmRequest = Film.builder()
                .description("description")
                .releaseDate(LocalDate.of(1997, 3, 25))
                .duration(100).build();

        String request = objectMapper.writeValueAsString(filmRequest);

        mockMvc.perform(post("/films").contentType(MediaType.APPLICATION_JSON).content(request))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testFailDescriptionVeryLongFilmCreate() throws Exception {
        Film filmRequest = Film.builder()
                .name("name")
                .description("descriptiondsdsdsdfsfshsdhfjshdfjhsdkfhsjkdfhjsdhfjkhsdjkfhsdjkhfkjsdhfjksd" +
                        "asdasdasdasgdhagdagdhagsdhagshdgashdgahsdghasgdhagsdhgashdgashdgahsdghasgdhagdscz" +
                        "zxczxczxjchjzjxckajssifwjeifiowehfio oiwe foiwe ofweiofjwjeoifj weiojf wej fiwejf" +
                        "weifihi ewjfiowej fiojewiofjweiofjwegfweifjwekfjw oiwiefiowejfoijweiofjiojweoifjwoe")
                .releaseDate(LocalDate.of(1997, 3, 25))
                .duration(100).build();

        String request = objectMapper.writeValueAsString(filmRequest);

        mockMvc.perform(post("/films").contentType(MediaType.APPLICATION_JSON).content(request))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testFailReleaseDateTooEarlyFilmCreate() throws Exception {
        Film filmRequest = Film.builder()
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(1895, 12, 25))
                .duration(100).build();

        String request = objectMapper.writeValueAsString(filmRequest);

        mockMvc.perform(post("/films").contentType(MediaType.APPLICATION_JSON).content(request))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testFailDurationIsZeroFilmCreate() throws Exception {
        Film filmRequest = Film.builder()
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(1895, 12, 25))
                .duration(0).build();

        String request = objectMapper.writeValueAsString(filmRequest);

        mockMvc.perform(post("/films").contentType(MediaType.APPLICATION_JSON).content(request))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testFailDurationIsNegativeFilmCreate() throws Exception {
        Film filmRequest = Film.builder()
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(1895, 12, 25))
                .duration(-5).build();

        String request = objectMapper.writeValueAsString(filmRequest);

        mockMvc.perform(post("/films").contentType(MediaType.APPLICATION_JSON).content(request))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testPutFilmUpdate() throws Exception {
        Film filmRequest = Film.builder()
                .id(1L)
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(1997, 3, 25))
                .duration(100).build();

        Film filmResponse = Film.builder()
                .name("new Name")
                .description("description")
                .releaseDate(LocalDate.of(1997, 3, 25))
                .duration(100)
                .id(1L).build();

        String request = objectMapper.writeValueAsString(filmRequest);
        String response = objectMapper.writeValueAsString(filmResponse);
        when(filmService.update(filmRequest)).thenReturn(filmResponse);

        mockMvc.perform(put("/films").contentType(MediaType.APPLICATION_JSON).content(request))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(response, true));
        verify(filmService, times(1)).update(filmRequest);
    }
}
