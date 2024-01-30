package com.proj.telegramkafka.exception;

import com.proj.telegramkafka.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.RedirectException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class TelegramExceptionHandler {


    @ExceptionHandler(ResponseStatusException.class)
    public ModelAndView handleResponseStatusException(HttpServletRequest request, ResponseStatusException ex) {
        return getErrorResponse(request, ex.getStatus(), ex.getReason());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ModelAndView handleMethodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException ex) {
        String message = ex.getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        return getErrorResponse(request, HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ModelAndView handleConstraintViolationException(HttpServletRequest request) {
        return getErrorResponse(request, HttpStatus.BAD_REQUEST, "Rewrite data about you, something was written bad");
    }

    @ExceptionHandler({IllegalArgumentException.class, EnumConstantNotPresentException.class})
    public ModelAndView handleBadRequestExceptions(HttpServletRequest request, RuntimeException ex) {
        return getErrorResponse(request, HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView handleForbiddenExceptions(HttpServletRequest request, AccessDeniedException ex) {
        return getErrorResponse(request, HttpStatus.FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ModelAndView handleNotFoundExceptions(HttpServletRequest request, EntityNotFoundException ex) {
        return getErrorResponse(request, HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler({NotiflowBotException.class, RedirectException.class})
    public ModelAndView handleAuthExceptions(HttpServletRequest request, RuntimeException ex) {
        return getErrorResponse(request, HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(HttpServletRequest request, Exception ex) {
        return getErrorResponse(request, HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    private ModelAndView getErrorResponse(HttpServletRequest request, HttpStatus httpStatus, String message) {
        log.error("Exception raised = {} :: URL = {}", message, request.getRequestURL());

        ModelMap map = new ModelMap();
        map.addAttribute("errorResponse",
                ErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .status(httpStatus)
                        .message(message)
                        .path(request.getRequestURL().toString())
                        .build()
        );
        map.addAttribute("formatter", DateTimeFormatter.ofPattern("h:mm a"));
        return new ModelAndView("error", map);
    }
}
