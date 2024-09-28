package com.krasnopolskyi.fitcoach.service.impl;

import com.krasnopolskyi.fitcoach.dto.exception.UserNotFoundException;
import com.krasnopolskyi.fitcoach.entity.User;
import com.krasnopolskyi.fitcoach.repository.UserRepository;
import com.krasnopolskyi.fitcoach.service.UserService;
import com.krasnopolskyi.fitcoach.utils.password_generator.PasswordGenerator;
import com.krasnopolskyi.fitcoach.utils.username_generator.UsernameGenerator;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User get(String username) throws UserNotFoundException {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Not found user with username " + username));
    }

    @Override
    public User create(String firstName, String lastName) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(UsernameGenerator.generateUsername(firstName, lastName));
        user.setPassword(PasswordGenerator.generatePassword());
        user.setActive(true);
        return repository.save(user);
    }

    @Override
    public boolean delete(String username) {
        return repository.findByUsername(username)
                .map(entity -> {
                    repository.deleteById(entity.getId());
                    repository.flush();
                    return true;
                }).orElse(false);
    }
}
