package com.krasnopolskyi.fitcoach.integration.service;

import com.krasnopolskyi.fitcoach.dto.request.ChangePasswordDto;
import com.krasnopolskyi.fitcoach.dto.request.ToggleStatusDto;
import com.krasnopolskyi.fitcoach.dto.request.UserCredentials;
import com.krasnopolskyi.fitcoach.dto.response.UserDto;
import com.krasnopolskyi.fitcoach.entity.User;
import com.krasnopolskyi.fitcoach.exception.AuthnException;
import com.krasnopolskyi.fitcoach.exception.EntityException;
import com.krasnopolskyi.fitcoach.exception.GymException;
import com.krasnopolskyi.fitcoach.integration.IntegrationTestBase;
import com.krasnopolskyi.fitcoach.repository.UserRepository;
import com.krasnopolskyi.fitcoach.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceIT extends IntegrationTestBase {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void create_ShouldReturnNewUser() {
        UserDto userDto = new UserDto("John", "Doe");

        User createdUser = userService.create(userDto);

        assertNotNull(createdUser);
        assertEquals("John", createdUser.getFirstName());
        assertEquals("Doe", createdUser.getLastName());
        assertTrue(createdUser.getIsActive());
        assertNotNull(createdUser.getUsername()); // Unique username should be generated
        assertNotNull(createdUser.getPassword()); // Password should be generated
        assertNotNull(createdUser.getPassword());
    }

    @Test
    void checkCredentials_ShouldThrowEntityException_WhenUserNotFound() {
        UserCredentials credentials = new UserCredentials("nonexistent.user", "password123");

        EntityException thrown = assertThrows(
                EntityException.class,
                () -> userService.checkCredentials(credentials)
        );

        assertEquals("Could not found user: nonexistent.user", thrown.getMessage());
    }

    @Test
    void changePassword_ShouldUpdatePassword_WhenOldPasswordMatches() throws EntityException, GymException {

        ChangePasswordDto changePasswordDto = new ChangePasswordDto("john.doe", "root", "newPassword");

        User updatedUser = userService.changePassword(changePasswordDto);

        assertNotNull(updatedUser);
        assertEquals("newPassword", updatedUser.getPassword());
    }

    @Test
    void changePassword_ShouldThrowAuthnException_WhenOldPasswordDoesNotMatch() {
        ChangePasswordDto changePasswordDto = new ChangePasswordDto("john.doe", "wrongOldPassword", "newPassword");

        AuthnException thrown = assertThrows(
                AuthnException.class,
                () -> userService.changePassword(changePasswordDto)
        );

        assertEquals("Bad Credentials", thrown.getMessage());
    }

    @Test
    void changeActivityStatus_ShouldUpdateIsActiveStatus() throws EntityException {
        ToggleStatusDto toggleStatusDto = new ToggleStatusDto("john.doe", false);

        User updatedUser = userService.changeActivityStatus(toggleStatusDto);

        assertNotNull(updatedUser);
        assertFalse(updatedUser.getIsActive()); // User's active status should be updated
    }

    @Test
    void generateUsername_ShouldReturnUniqueUsername_WhenBaseUsernameAlreadyExists() {
        // Given: a user with the base username "john.doe" exists
        User existingUser = new User();
        existingUser.setUsername("john.doe");
        existingUser.setFirstName("John");
        existingUser.setLastName("Doe");
        existingUser.setPassword("password");
        existingUser.setIsActive(true);

        // When: a new user with the same first and last name is created
        String newUsername = userService.create(new UserDto("John", "Doe")).getUsername();

        // Then: the new username should be "john.doe1"
        assertEquals("john.doe1", newUsername);
    }
}
