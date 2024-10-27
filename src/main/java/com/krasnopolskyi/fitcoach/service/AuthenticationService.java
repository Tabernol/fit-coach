package com.krasnopolskyi.fitcoach.service;

import com.krasnopolskyi.fitcoach.dto.request.UserCredentials;
import com.krasnopolskyi.fitcoach.exception.AuthnException;
import com.krasnopolskyi.fitcoach.exception.EntityException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private static final Map<String, String> userTokenMap = new HashMap<>();

    private final UserService userService;


    public String logIn(UserCredentials userCredentials) throws EntityException, AuthnException {
        if(userService.checkCredentials(userCredentials)){
            String token = UUID.randomUUID().toString();
            userTokenMap.put(token, userCredentials.username()); // maybe need to do in another order k/v
            return token;

        } else {
            throw new AuthnException("Invalid credentials");
        }
    }

    public boolean isTokenValid(String token) {
        return userTokenMap.containsKey(token);
    }
}
