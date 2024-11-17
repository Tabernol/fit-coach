package com.krasnopolskyi.fitcoach.config.security.filter;

import com.krasnopolskyi.fitcoach.config.security.SecurityConfig;
import com.krasnopolskyi.fitcoach.config.security.filter.CheckUsernameFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

import java.io.IOException;

import static org.mockito.Mockito.*;
class CheckUsernameFilterTest {
    @InjectMocks
    private CheckUsernameFilter checkUsernameFilter;

    @Mock
    private FilterChain filterChain;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testDoFilterInternal_ExcludedPath() throws ServletException, IOException {
        // Mock an excluded path
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/v1/trainees/create");
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Mock SecurityConfig.isExcludedPath() to return true
        mockStatic(SecurityConfig.class);
        when(SecurityConfig.isExcludedPath(request.getRequestURI())).thenReturn(true);

        // Call the filter
        checkUsernameFilter.doFilterInternal(request, response, filterChain);

        // Verify the filter chain was called without blocking
        verify(filterChain).doFilter(request, response);
    }
}
