package com.proj.user.config;

import com.proj.user.exception.RedirectException;
import jakarta.servlet.http.HttpServletResponse;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest
public class RedirectConfigTests {

    private final HttpServletResponse httpServletResponse;

    @Autowired
    public RedirectConfigTests(HttpServletResponse httpServletResponse) {
        this.httpServletResponse = httpServletResponse;
    }

    @Test
    public void testInjectedComponent() {
        AssertionsForClassTypes.assertThat(httpServletResponse).isNotNull();
    }

    @Test
    public void testValidRedirect() {
        Assertions.assertDoesNotThrow(() ->
                RedirectConfig.redirect("/api/v1/auth/login", httpServletResponse));
    }

    @Test
    public void testInvalidRedirect() {
        Assertions.assertThrows(RedirectException.class,
                () -> RedirectConfig.redirect(null, httpServletResponse));
    }
}
