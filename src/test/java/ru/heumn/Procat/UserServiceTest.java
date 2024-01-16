package ru.heumn.Procat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.checkerframework.checker.index.qual.PolyUpperBound;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.misusing.MockitoConfigurationException;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.heumn.Procat.configuration.AopConfiguration;
import ru.heumn.Procat.configuration.SecurityConfiguration;
import ru.heumn.Procat.controllers.UserController;
import ru.heumn.Procat.exceptions.BadRequestException;
import ru.heumn.Procat.exceptions.ConflictRequestException;
import ru.heumn.Procat.exceptions.NotFoundException;
import ru.heumn.Procat.factories.UserDtoFactory;
import ru.heumn.Procat.services.UserService;
import ru.heumn.Procat.storage.dto.UserDto;
import ru.heumn.Procat.storage.entities.UserEntity;
import ru.heumn.Procat.storage.enums.Role;
import ru.heumn.Procat.storage.repository.UserRepository;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

//@ContextConfiguration(classes={ProcatApplication.class, AopConfiguration.class, SecurityConfiguration.class, UserService.class, UserDtoFactory.class, Set.class, UserEntity.class, UserDto.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class UserServiceTest {

    @MockBean
    UserRepository userRepository;

    @Autowired
    UserService userService;

    UserEntity user1;
    UserEntity user2;
    UserEntity user3;
    UserDto userDto1;
    UserDto userDto2;
    UserDto userDto3;

    UserDto userDtoBadRequest;

    @BeforeEach
    public void start() {
        MockitoAnnotations.initMocks(this);

        user1 = UserEntity.builder()
                .id(240L)
                .active(true)
                .roles(Set.of(Role.ADMIN_ROLE))
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
                .roles(Set.of(Role.ADMIN_ROLE))
                .name("name2")
                .lastName("lastname2")
                .patronymic("patronymic2")
                .password("$2a$10$22O32p.q7tMHjLqhJHC4nOCo7w9W395kSTqiz9MjDrGYe77cIgERG")
                .build();
        user3 = UserEntity.builder()
                .id(242L)
                .active(true)
                .login("heumn3")
                .roles(null)
                .name("name3")
                .lastName("lastname3")
                .patronymic("patronymic3")
                .password("$2a$10$22O32p.q7tMHjLqhJHC4nOCo7w9W395kSTqiz9MjDrGYe77cIgERG")
                .build();
        userDto1 = UserDto.builder()
                .id(240L)
                .active(true)
                .roles(null)
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
                .roles(null)
                .name("name2")
                .lastName("lastname2")
                .patronymic("patronymic2")
                .password("$2a$10$22O32p.q7tMHjLqhJHC4nOCo7w9W395kSTqiz9MjDrGYe77cIgERG")
                .build();
        userDto3 = UserDto.builder()
                .id(242L)
                .active(true)
                .login("heumn3")
                .roles(null)
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
                .roles(null)
                .lastName("lastname1")
                .patronymic("patronymic1")
                .build();
    }

    @Test
    public void testLoadByUsername() {

        when(userRepository.findByLogin("heumn1")).thenReturn(Optional.ofNullable(user1));
        when(userRepository.findByLogin("heumn2")).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(user1);

        assertThrows(UsernameNotFoundException.class, () -> {userService.loadUserByUsername("heumn2");});
        assertEquals(user1, userService.loadUserByUsername("heumn1"));
    }

    @Test
    public void testUpdateUser() {
        when(userRepository.findById(userDto1.getId())).thenReturn(Optional.ofNullable(user1));
        when(userRepository.findById(userDto3.getId())).thenReturn(Optional.empty());

        when(userRepository.findByLogin(userDto1.getLogin())).thenReturn(Optional.ofNullable(user1));
        when(userRepository.findByLogin(userDto3.getLogin())).thenReturn(Optional.ofNullable(user3));

        assertThrows(ConflictRequestException.class, () -> {userService.updateUser(userDto1);});
        assertThrows(NotFoundException.class, () -> {userService.updateUser(userDto2);});
    }

    @Test
    public void testAddUser() throws ConflictRequestException {
        when(userRepository.findByLogin(user1.getLogin())).thenReturn(Optional.empty());
        when(userRepository.findByLogin(user2.getLogin())).thenReturn(Optional.ofNullable(user1));

        assertEquals(user1.getId(), userService.addUser(userDto1).getId());
        assertThrows(ConflictRequestException.class, () -> {userService.addUser(userDto2);});
    }

    @Test
    public void testDeleteUser() throws NotFoundException {
        when(userRepository.findById(240L)).thenReturn(Optional.ofNullable(user1));
        when(userRepository.findById(450L)).thenReturn(Optional.empty());

        assertEquals(true, userService.deleteUser(240L));
        assertThrows(NotFoundException.class, () -> {userService.deleteUser(241L);});
    }

    @Test
    public void testSetRoleForUser() throws NotFoundException, ConflictRequestException {
        when(userRepository.findById(240L)).thenReturn(Optional.of(user1));
        when(userRepository.findById(249L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {userService.setRoleForUser(249L, Role.ADMIN_ROLE);});
        assertThrows(ConflictRequestException.class, () -> {userService.setRoleForUser(240L, Role.ADMIN_ROLE);});
        //assertTrue(userService.setRoleForUser(240L, Role.SELLER_ROLE).getRoles().contains(Role.SELLER_ROLE)); THIS WORK
    }

    @Test
    public void testDeleteRoleForUser() throws NotFoundException, ConflictRequestException {
        when(userRepository.findById(240L)).thenReturn(Optional.of(user1));
        when(userRepository.findById(249L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {userService.deleteRoleForUser(249L, Role.ADMIN_ROLE);});
        assertThrows(BadRequestException.class, () -> {userService.deleteRoleForUser(240L, Role.MANAGER_ROLE);});
    }

}
