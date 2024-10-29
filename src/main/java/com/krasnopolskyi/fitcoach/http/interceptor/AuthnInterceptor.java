package com.krasnopolskyi.fitcoach.http.interceptor;

import com.krasnopolskyi.fitcoach.exception.AuthnException;
import com.krasnopolskyi.fitcoach.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthnInterceptor implements HandlerInterceptor {
    private final AuthenticationService authenticationService;

    /**
     * This method validate token before start executing methods in RestControllers.
     * private and public paths specified in WebConfig.clas
     *
     * @param request  current HTTP request
     * @param response current HTTP response
     * @param handler  chosen handler to execute, for type and/or instance evaluation
     * @return true if token valid otherwise false
     * @throws Exception, AuthnException if user does not authenticate
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token = request.getHeader("Authorization");
        if (token == null) {
            throw new AuthnException("Unauthorized: Missing token"); // I separated the messages only on development stage
        } else {
            token = token.substring(7);
            if (!authenticationService.isTokenValid(token)) {
                throw new AuthnException("Unauthorized: Invalid token");
            }
        }
        return true;
    }
}
