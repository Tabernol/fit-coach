package com.krasnopolskyi.fitcoach.http.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@Slf4j(topic = "REST")
public class ControllerLogInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String requestId = UUID.randomUUID().toString();
        request.setAttribute("requestId", requestId);
        log.info("Incoming Request: RequestId={}, Endpoint={}, Method={}, Params={}",
                requestId, request.getRequestURI(), request.getMethod(), formatParams(request.getParameterMap()));
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        String requestId = (String) request.getAttribute("requestId");
        int status = response.getStatus();

        // Check for an exception message stored by the exception handler
        String errorMessage = (String) request.getAttribute("errorMessage");
        if (errorMessage != null) {
            log.info("Request Completed: RequestId={}, Status={}, Response=Error, Message={}",
                    requestId, status, errorMessage);
        } else {
            log.info("Request Completed: RequestId={}, Status={}, Response=Success",
                    requestId, status);
        }
    }

    // Helper method to format request parameters
    private String formatParams(Map<String, String[]> paramMap) {
        if (paramMap == null || paramMap.isEmpty()) {
            return "No parameters";
        }
        return paramMap.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + Arrays.toString(entry.getValue()))
                .collect(Collectors.joining(", "));
    }
}
