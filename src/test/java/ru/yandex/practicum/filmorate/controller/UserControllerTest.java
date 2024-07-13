package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

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

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private InMemoryUserStorage userService;

    @Test
    void getFindAllEmptyUsersMap() throws Exception {
        Mockito.when(userService.findAll()).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
        verify(userService, times(1)).findAll();
    }

    @Test
    void getFindAllUsersMap() throws Exception {
        User user1 = User.builder()
                .name("Name1")
                .login("loG")
                .email("example@mail.com")
                .birthday(LocalDate.parse("2002-12-03"))
                .build();
        User user2 = User.builder()
                .id(1L)
                .name("Name2")
                .login("loG")
                .email("example@mail.com")
                .birthday(LocalDate.parse("2012-12-03"))
                .build();
        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        String userJson = objectMapper.writeValueAsString(users);
        Mockito.when(userService.findAll()).thenReturn(users);
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json(userJson, true));
        verify(userService, times(1)).findAll();
    }

    @Test
    void testCreateUser() throws Exception {
        User userRequest = User.builder()
                .name("Bill")
                .login("loG")
                .email("example@mail.com")
                .birthday(LocalDate.parse("2002-12-03"))
                .build();
        User userResponse = User.builder()
                .id(1L)
                .name("Bill")
                .login("loG")
                .email("example@mail.com")
                .birthday(LocalDate.parse("2002-12-03"))
                .build();
        String userJson = objectMapper.writeValueAsString(userRequest);
        String response = objectMapper.writeValueAsString(userResponse);
        when(userService.create(userRequest)).thenReturn(userResponse);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(response, true));

        verify(userService, times(1)).create(userRequest);
    }

    @Test
    void testCreateUserIsEmptyName() throws Exception {
        User userRequest = User.builder()
                .login("loG")
                .email("example@mail.com")
                .birthday(LocalDate.parse("2002-12-03"))
                .build();
        User userResponse = User.builder()
                .id(1L)
                .name("loG")
                .login("loG")
                .email("example@mail.com")
                .birthday(LocalDate.parse("2002-12-03"))
                .build();
        String userJson = objectMapper.writeValueAsString(userRequest);
        String response = objectMapper.writeValueAsString(userResponse);
        when(userService.create(userRequest)).thenReturn(userResponse);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(response, true));

        verify(userService, times(1)).create(userRequest);
    }

    @Test
    void testFailEmailIsBadCreateUser() throws Exception {
        User userBadRequest = User.builder()
                .name("Bill")
                .login("loG")
                .email("exampleMail")
                .birthday(LocalDate.parse("2002-12-03"))
                .build();
        String userJson = objectMapper.writeValueAsString(userBadRequest);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testFailLoginIsEmptyCreateUser() throws Exception {
        User userBadRequest = User.builder()
                .name("Bill")
                .email("example@mail.com")
                .birthday(LocalDate.parse("2002-12-03"))
                .build();
        String userJson = objectMapper.writeValueAsString(userBadRequest);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testFailBirthdayIsFutureCreateUser() throws Exception {
        User userBadRequest = User.builder()
                .name("Bill")
                .email("example@mail.com")
                .birthday(LocalDate.parse("2102-12-03"))
                .build();
        String userJson = objectMapper.writeValueAsString(userBadRequest);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateUser() throws Exception {
        User userRequest = User.builder()
                .id(1L)
                .name("Bill")
                .login("loG")
                .email("example@mail.com")
                .birthday(LocalDate.parse("2002-12-03"))
                .build();

        User userResponse = User.builder()
                .id(1L)
                .name("Jonn")
                .login("loG")
                .email("example@mail.com")
                .birthday(LocalDate.parse("2002-12-03"))
                .build();
        String userJson = objectMapper.writeValueAsString(userRequest);
        String response = objectMapper.writeValueAsString(userResponse);
        when(userService.update(userRequest)).thenReturn(userResponse);

        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(response, true));

        verify(userService, times(1)).update(userRequest);
    }
}