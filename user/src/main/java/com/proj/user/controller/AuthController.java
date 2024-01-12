package com.proj.user.controller;

import com.proj.user.config.RedirectConfig;
import com.proj.user.mapper.UserMapper;
import com.proj.user.dto.LoginRequest;
import com.proj.user.dto.RegisterRequest;
import com.proj.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public ModelAndView getLoginForm(ModelMap modelMap) {
        modelMap.addAttribute("loginRequest", new LoginRequest());
        return new ModelAndView("login", modelMap);
    }

    @PostMapping("/login")
    public void login(@Valid LoginRequest loginRequest) {
        var user = userService.readByEmail(loginRequest.getEmail());
        checkPasswords(loginRequest.getPassword(), user.getPassword());

        log.info("=== POST-LOGIN === auth - {} === time - {}.", user.getEmail(), LocalDateTime.now());
    }

    private void checkPasswords(String rawPass, String encodedPass) {
        if (!passwordEncoder.matches(rawPass, encodedPass)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong old password!");
        }
    }


    @GetMapping("/register")
    public ModelAndView getRegisterForm(ModelMap modelMap) {
        modelMap.addAttribute("registerRequest", new RegisterRequest());
        return new ModelAndView("register", modelMap);
    }

    @PostMapping("/register")
    public void register(@RequestBody @Valid RegisterRequest registerRequest, HttpServletResponse response) {
        userService.create(userMapper.getUserFromRegisterRequest(registerRequest), "USER");
        log.info("Register user with email - {} == {}", registerRequest.getEmail(), LocalDateTime.now());

        RedirectConfig.redirect("/api/v1/users", response);
    }
}
