package com.krasnopolskyi.fitcoach.service;

import com.krasnopolskyi.fitcoach.entity.User;
import com.krasnopolskyi.fitcoach.exception.UserNotFoundException;

public interface UserService {
    User get(String username) throws UserNotFoundException;
    User create(String firstName, String lastName);
    boolean delete(String username);


}
