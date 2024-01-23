package com.proj.user.exception;

import com.proj.user.dto.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import javax.security.auth.login.LoginException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ModelAndView handleResponseStatusException(HttpServletRequest request, ResponseStatusException ex) {
        return getErrorResponse(request, ex.getStatusCode(), ex.getReason());
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
    public ModelAndView handleConstraintViolationException(HttpServletRequest request, ConstraintViolationException ex) {
        return getErrorResponse(request, HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler({IllegalArgumentException.class, LoginException.class,
            RoleNameException.class, EnumConstantNotPresentException.class})
    public ModelAndView handleBadRequestExceptions(HttpServletRequest request, RuntimeException ex) {
        return getErrorResponse(request, HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ModelAndView handleBadCredentialsException(HttpServletRequest request, BadCredentialsException ex) {
        return getErrorResponse(request, HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView handleForbiddenExceptions(HttpServletRequest request, AccessDeniedException ex) {
        return getErrorResponse(request, HttpStatus.FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ModelAndView handleNotFoundExceptions(HttpServletRequest request, EntityNotFoundException ex) {
        return getErrorResponse(request, HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler({AuthorizationException.class, GoogleLoginException.class,
            LogoutException.class, RedirectException.class})
    public ModelAndView handleAuthExceptions(HttpServletRequest request, RuntimeException ex) {
        return getErrorResponse(request, HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(HttpServletRequest request, Exception ex) {
        return getErrorResponse(request, HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    private ModelAndView getErrorResponse(HttpServletRequest request, HttpStatusCode httpStatus, String message) {
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
