package com.krasnopolskyi.fitcoach.http.rest;

import com.krasnopolskyi.fitcoach.dto.request.ChangePasswordDto;
import com.krasnopolskyi.fitcoach.dto.request.UserCredentials;
import com.krasnopolskyi.fitcoach.exception.AuthnException;
import com.krasnopolskyi.fitcoach.exception.EntityException;
import com.krasnopolskyi.fitcoach.exception.GymException;
import com.krasnopolskyi.fitcoach.service.AuthenticationService;
import com.krasnopolskyi.fitcoach.service.UserService;
import com.krasnopolskyi.fitcoach.validation.Create;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/authn")
public class AuthnController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserCredentials credentials) throws EntityException, AuthnException {
        return ResponseEntity.ok(authenticationService.logIn(credentials));
    }

    @PutMapping("/change-pass")
    public ResponseEntity<String> changePassword(
            @Validated(Create.class) @RequestBody ChangePasswordDto changePasswordDto)
            throws GymException {
        userService.changePassword(changePasswordDto);
        return ResponseEntity.status(HttpStatus.OK).body("Password has changed");
    }
}
