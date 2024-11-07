package com.krasnopolskyi.fitcoach.config;

import com.krasnopolskyi.fitcoach.http.interceptor.AuthnInterceptor;
import com.krasnopolskyi.fitcoach.http.interceptor.ControllerLogInterceptor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WebConfigTest {
    @MockBean
    private ControllerLogInterceptor controllerLogInterceptor;
    @MockBean
    private AuthnInterceptor authnInterceptor;

    private WebConfig webConfig;

    @Test
    void webConfigTest() {
        webConfig = new WebConfig(controllerLogInterceptor, authnInterceptor);
        assertNotNull(webConfig);
    }

}
