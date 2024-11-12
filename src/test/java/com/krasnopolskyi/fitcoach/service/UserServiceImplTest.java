package com.krasnopolskyi.fitcoach.service;

import com.krasnopolskyi.fitcoach.dto.request.ChangePasswordDto;
import com.krasnopolskyi.fitcoach.dto.request.ToggleStatusDto;
import com.krasnopolskyi.fitcoach.dto.request.UserCredentials;
import com.krasnopolskyi.fitcoach.dto.response.UserDto;
import com.krasnopolskyi.fitcoach.entity.User;
import com.krasnopolskyi.fitcoach.exception.AuthnException;
import com.krasnopolskyi.fitcoach.exception.EntityException;
import com.krasnopolskyi.fitcoach.exception.GymException;
import com.krasnopolskyi.fitcoach.repository.UserRepository;
import com.krasnopolskyi.fitcoach.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Mockito;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private User mockUser;
    private UserDto mockUserDto;
    private UserCredentials mockCredentials;
    private ChangePasswordDto mockChangePasswordDto;
    private ToggleStatusDto mockToggleStatusDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userServiceImpl = new UserServiceImpl(userRepository, passwordEncoder); // inject mock repository
        // Setup mock User
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setFirstName("John");
        mockUser.setLastName("Doe");
        mockUser.setUsername("john.doe");
        mockUser.setPassword("password123");
        mockUser.setIsActive(true);

        // Setup mock UserDto
        mockUserDto = new UserDto("John", "Doe", "root");

        // Setup mock credentials
        mockCredentials = new UserCredentials("john.doe", "password123");

        // Setup mock ChangePasswordDto
        mockChangePasswordDto = new ChangePasswordDto("john.doe", "password123", "newPassword123");

        // Setup mock ToggleStatusDto
        mockToggleStatusDto = new ToggleStatusDto("john.doe", false);
    }

    @Test
    void testCreateUser() {
        when(userRepository.findByUsername("john.doe")).thenReturn(Optional.empty());

        User result = userServiceImpl.create(mockUserDto);

        assertNotNull(result);
        assertEquals(mockUserDto.firstName(), result.getFirstName());
        assertEquals(mockUserDto.lastName(), result.getLastName());
        assertTrue(result.getIsActive());
        assertNotNull(result.getUsername());
        assertNotNull(result.getPassword());
    }

    @Test
    void testCreateUserIfUsernameExist() {
        when(userRepository.findByUsername("john.doe")).thenReturn(Optional.ofNullable(mockUser));
        when(userRepository.findByUsername("john.doe1")).thenReturn(Optional.empty());

        User result = userServiceImpl.create(mockUserDto);

        assertNotNull(result);
        assertEquals(mockUserDto.firstName(), result.getFirstName());
        assertEquals(mockUserDto.lastName(), result.getLastName());
        assertTrue(result.getIsActive());
        assertNotNull(result.getUsername());
        assertNotNull(result.getPassword());
    }


    @Test
    void testChangePasswordSuccess() throws GymException {
        when(userRepository.findByUsername(mockChangePasswordDto.username()))
                .thenReturn(Optional.of(mockUser));
        when(userRepository.save(Mockito.any(User.class))).thenReturn(mockUser);

        User updatedUser = userServiceImpl.changePassword(mockChangePasswordDto);

        assertNotNull(updatedUser);
        assertEquals(mockChangePasswordDto.newPassword(), updatedUser.getPassword());
    }

    @Test
    void testChangePasswordFailWrongOldPassword() {
        when(userRepository.findByUsername(mockChangePasswordDto.username()))
                .thenReturn(Optional.of(mockUser));

        ChangePasswordDto wrongOldPasswordDto = new ChangePasswordDto("john.doe", "wrongOldPassword", "newPassword123");

        assertThrows(AuthnException.class, () -> userServiceImpl.changePassword(wrongOldPasswordDto));
    }

    @Test
    void testChangeActivityStatus() throws EntityException {
        when(userRepository.findByUsername(mockToggleStatusDto.username()))
                .thenReturn(Optional.of(mockUser));
        when(userRepository.save(Mockito.any(User.class))).thenReturn(mockUser);

        User updatedUser = userServiceImpl.changeActivityStatus(mockToggleStatusDto);

        assertNotNull(updatedUser);
        assertFalse(updatedUser.getIsActive());
    }

}
