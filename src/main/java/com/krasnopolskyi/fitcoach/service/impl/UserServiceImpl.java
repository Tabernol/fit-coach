package com.krasnopolskyi.fitcoach.service.impl;

import com.krasnopolskyi.fitcoach.dto.request.ChangePasswordDto;
import com.krasnopolskyi.fitcoach.dto.request.ToggleStatusDto;
import com.krasnopolskyi.fitcoach.dto.request.UserCredentials;
import com.krasnopolskyi.fitcoach.dto.response.UserDto;
import com.krasnopolskyi.fitcoach.entity.User;
import com.krasnopolskyi.fitcoach.exception.AuthnException;
import com.krasnopolskyi.fitcoach.exception.EntityException;
import com.krasnopolskyi.fitcoach.repository.UserRepository;
import com.krasnopolskyi.fitcoach.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    @Override
    public User create(UserDto userDto) {
        String username = generateUsername(userDto.firstName(), userDto.lastName());
        User user = new User();
        user.setFirstName(userDto.firstName());
        user.setLastName(userDto.lastName());
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(userDto.password()));
        user.setIsActive(true);
        return user;
    }

    private User findByUsername(String username) throws EntityException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityException("Could not found user: " + username));
    }


    @Transactional
    @Override
    public boolean checkCredentials(UserCredentials credentials) throws EntityException {
        User user = findByUsername(credentials.username());
        return passwordEncoder.matches(credentials.password(), user.getPassword());
//        return user.getPassword().equals(credentials.password());
    }

    @Transactional
    @Override
    public User changePassword(ChangePasswordDto changePasswordDto) throws EntityException, AuthnException {
        User user = findByUsername(changePasswordDto.username());
        if(!passwordEncoder.matches(changePasswordDto.oldPassword(), user.getPassword())){
            throw new AuthnException("Bad Credentials");
        }
        validatePassword(changePasswordDto.newPassword());
        user.setPassword(passwordEncoder.encode(changePasswordDto.newPassword()));
        return userRepository.save(user);
    }

    @Override
    public User changeActivityStatus(ToggleStatusDto statusDto) throws EntityException {
        User user = findByUsername(statusDto.username());
        user.setIsActive(statusDto.isActive()); //status changes here
        return userRepository.save(user);
    }

    /**
     * Method generate unique username based on provided first name and last name.
     * If current username already exists in database method adds digit to end of username and check again.
     * example 'john.doe1' if 'john.doe' is already exist
     * @param firstName - first name of user
     * @param lastName - last name of user
     * @return unique username for current database
     */
    private String generateUsername(String firstName, String lastName) {
        int count = 1;
        String template = firstName.toLowerCase() + "." + lastName.toLowerCase();
        String username = template;
        while (isUsernameExist(username)){
            username = template + count;
            count++;
        }
        return username;
    }

    /**
     * checks if username exist in database
     * @param username target username
     * @return true is username exist, otherwise false
     */
    private boolean isUsernameExist(String username){
        return userRepository.findByUsername(username).isPresent();
    }

    private void validatePassword(String password){
        // todo check contains character and so on
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).map(user ->
                        new org.springframework.security.core.userdetails.User(
                                user.getUsername(),
                                user.getPassword(),
                                Collections.unmodifiableSet(user.getRoles())))
                .orElseThrow(()
                        -> new UsernameNotFoundException("Failed to retrieve user: " + username));
    }
}
