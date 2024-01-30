package com.proj.telegramkafka.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

import static com.proj.telegramkafka.TelegramTestAdvice.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ErrorResponseTests {
    private ErrorResponse errorResponse;

    @BeforeEach
    public void setErrorResponse() {
        errorResponse = ErrorResponse.builder()
                .message("Some message")
                .path("/api/v1/**")
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Test
    public void testValidErrorResponse() {
        assertEquals(0, getViolation(errorResponse).size());
    }

    @Test
    public void testInvalidStatus() {
        errorResponse.setStatus(null);
        assertionsForCheckingNull(errorResponse);
    }

    @Test
    public void testInvalidMessage() {
        errorResponse.setMessage(null);
        assertionsForCheckingNull(errorResponse);
    }

    @Test
    public void testInvalidPath() {
        errorResponse.setPath(null);
        assertionsForCheckingNull(errorResponse);
    }

    private void assertionsForCheckingNull(ErrorResponse errorResponse) {
        assertEquals(1, getViolation(errorResponse).size());
    }
}
