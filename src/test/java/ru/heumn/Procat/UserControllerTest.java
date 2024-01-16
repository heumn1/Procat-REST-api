package ru.heumn.Procat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.heumn.Procat.configuration.AopConfiguration;
import ru.heumn.Procat.configuration.SecurityConfiguration;
import ru.heumn.Procat.controllers.UserController;
import ru.heumn.Procat.exceptions.BadRequestException;
import ru.heumn.Procat.exceptions.NotFoundException;
import ru.heumn.Procat.factories.UserDtoFactory;
import ru.heumn.Procat.services.UserService;
import ru.heumn.Procat.storage.dto.UserDto;
import ru.heumn.Procat.storage.entities.UserEntity;
import ru.heumn.Procat.storage.repository.UserRepository;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@WebMvcTest(UserController.class)
@ContextConfiguration(classes={ProcatApplication.class, AopConfiguration.class, SecurityConfiguration.class, UserDtoFactory.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    UserRepository userRepository;
    @MockBean
    UserService userService;

    UserEntity user1;
    UserEntity user2;
    UserEntity user3;
    UserDto userDto1;
    UserDto userDto2;
    UserDto userDto3;

    UserDto userDtoBadRequest;

    @BeforeAll()
    public void start() {
        user1 = UserEntity.builder()
                .id(240L)
                .active(true)
                .login("heumn1")
                .name("name1")
                .lastName("lastname1")
                .patronymic("patronymic1")
                .password("$2a$10$22O32p.q7tMHjLqhJHC4nOCo7w9W395kSTqiz9MjDrGYe77cIgERG")
                .build();
        user2 = UserEntity.builder()
                .id(241L)
                .active(true)
                .login("heumn2")
                .name("name2")
                .lastName("lastname2")
                .patronymic("patronymic2")
                .password("$2a$10$22O32p.q7tMHjLqhJHC4nOCo7w9W395kSTqiz9MjDrGYe77cIgERG")
                .build();
        user3 = UserEntity.builder()
                .id(242L)
                .active(true)
                .login("heumn3")
                .name("name3")
                .lastName("lastname3")
                .patronymic("patronymic3")
                .password("$2a$10$22O32p.q7tMHjLqhJHC4nOCo7w9W395kSTqiz9MjDrGYe77cIgERG")
                .build();
        userDto1 = UserDto.builder()
                .id(240L)
                .active(true)
                .login("heumn1")
                .name("name1")
                .lastName("lastname1")
                .patronymic("patronymic1")
                .password("$2a$10$22O32p.q7tMHjLqhJHC4nOCo7w9W395kSTqiz9MjDrGYe77cIgERG")
                .build();
        userDto2 = UserDto.builder()
                .id(241L)
                .active(true)
                .login("heumn2")
                .name("name2")
                .lastName("lastname2")
                .patronymic("patronymic2")
                .password("$2a$10$22O32p.q7tMHjLqhJHC4nOCo7w9W395kSTqiz9MjDrGYe77cIgERG")
                .build();
        userDto3 = UserDto.builder()
                .id(242L)
                .active(true)
                .login("heumn3")
                .name("name3")
                .lastName("lastname3")
                .patronymic("patronymic3")
                .password("$2a$10$22O32p.q7tMHjLqhJHC4nOCo7w9W395kSTqiz9MjDrGYe77cIgERG")
                .build();

        userDtoBadRequest = UserDto.builder()
                .id(244L)
                .active(true)
                .login("heumn1")
                .name("name1")
                .lastName("lastname1")
                .patronymic("patronymic1")
                .build();
    }

    @Test
    public void testGetAllUser() throws Exception {
        List<UserDto> usersDto = new ArrayList<>(Arrays.asList(userDto1, userDto2, userDto3));

        when(userService.getAllUsers()).thenReturn(usersDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/user/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[2].name", is("name3")));
    }

    @Test
    public void testGet() throws Exception {

        when(userRepository.findById(52L)).thenReturn(Optional.of(user1));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/user/get/52")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("name1")));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/user/get/561")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAdd() throws Exception {

        when(userService.addUser(any())).thenReturn(user1);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/user/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userDtoBadRequest)))
                .andExpect(status().isBadRequest());

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/user/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userDto1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("name1")));
    }

    @Test
    public void testDeleteUser() throws Exception {

        doThrow(NotFoundException.class).when(userService).deleteUser(53L);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/user/delete/52")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/user/delete/53")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateUser() throws Exception {

        doNothing().when(userService).updateUser(userDto1);

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/user/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userDto1)))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/user/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userDtoBadRequest)))
                .andExpect(status().isBadRequest());
    }

    private String asJsonString(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


    }
}