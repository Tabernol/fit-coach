package com.krasnopolskyi.fitcoach.service;

import com.krasnopolskyi.fitcoach.dto.exception.UserNotFoundException;
import com.krasnopolskyi.fitcoach.entity.User;

public interface UserService {
    User get(String username) throws UserNotFoundException;
    User create(String firstName, String lastName);
    boolean delete(String username);


}
