package com.krasnopolskyi.fitcoach.service;

import com.krasnopolskyi.fitcoach.dto.request.ChangePasswordDto;
import com.krasnopolskyi.fitcoach.dto.request.UserCredentials;
import com.krasnopolskyi.fitcoach.dto.request.UserDto;
import com.krasnopolskyi.fitcoach.entity.User;
import com.krasnopolskyi.fitcoach.exception.AuthnException;
import com.krasnopolskyi.fitcoach.exception.EntityException;
import com.krasnopolskyi.fitcoach.exception.GymException;
import com.krasnopolskyi.fitcoach.exception.ValidateException;
import com.krasnopolskyi.fitcoach.repository.UserRepository;
import com.krasnopolskyi.fitcoach.utils.password_generator.PasswordGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User create(UserDto userDto) {
        String username = generateUsername(userDto.firstName(), userDto.lastName());
        String password = PasswordGenerator.generatePassword();
        User user = new User();
        user.setFirstName(userDto.firstName());
        user.setLastName(userDto.lastName());
        user.setUsername(username);
        user.setPassword(password);
        user.setIsActive(true);
        return user;
    }

//    public User findById(Long id) throws EntityException {
//        return userRepository.findById(id)
//                .orElseThrow(() -> new EntityException("Could not found user with id " + id));
//    }
//
    private User findByUsername(String username) throws EntityException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityException("Could not found user: " + username));
    }


    @Transactional
    public boolean checkCredentials(UserCredentials credentials) throws EntityException {
        User user = findByUsername(credentials.username());
        return user.getPassword().equals(credentials.password());
    }

    @Transactional
    public User changePassword(ChangePasswordDto changePasswordDto) throws GymException {
        User user = findByUsername(changePasswordDto.username());
        if(!changePasswordDto.oldPassword().equals(user.getPassword())){
            throw new AuthnException("Bad Credentials");
        }
        validatePassword(changePasswordDto.newPassword());
        user.setPassword(changePasswordDto.newPassword());
        return userRepository.save(user);
    }
//
//
//    @Transactional
//    public User changeActivityStatus(String target) throws GymException {
//        User user = findByUsername(target);
//        user.setIsActive(!user.getIsActive()); //status changes here
//        return userRepository.update(user);
//    }

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

    private boolean isUsernameExist(String username){
        return userRepository.findByUsername(username).isPresent();
    }

    private void validatePassword(String password){
        // todo check contains character and so on
    }
}
