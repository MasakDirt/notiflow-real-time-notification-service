package com.proj.user.controller;

import com.proj.user.config.RedirectConfig;
import com.proj.user.mapper.UserMapper;
import com.proj.user.dto.LoginRequest;
import com.proj.user.dto.RegisterRequest;
import com.proj.user.service.UserService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/login")
    public ModelAndView getLoginForm(ModelMap modelMap) {
        modelMap.addAttribute("loginRequest", new LoginRequest());
        return new ModelAndView("login", modelMap);
    }

    @PostMapping("/login")
    public void login(@Valid LoginRequest loginRequest, HttpServletResponse response) {
        var user = userService.readByEmail(loginRequest.getEmail());
        userService.checkInvalidPasswords(loginRequest.getPassword(), user.getPassword());

        log.info("=== POST-LOGIN === auth - {} === time - {}.", user.getEmail(), LocalDateTime.now());
        RedirectConfig.redirect("/api/v1/users", response);
    }


    @GetMapping("/register")
    public ModelAndView getRegisterForm(ModelMap modelMap) {
        modelMap.addAttribute("registerRequest", new RegisterRequest());
        return new ModelAndView("register", modelMap);
    }

    @PostMapping("/register")
    public void register(@Valid RegisterRequest registerRequest, HttpServletResponse response) {
        var user = userService.saveWithSettingFields(userMapper.getUserFromRegisterRequest(registerRequest), "USER");
        log.info("Register user with email - {} == {}", registerRequest.getEmail(), LocalDateTime.now());
        chooseRedirectAfterRegister(user.getId(), response);
    }

    private void chooseRedirectAfterRegister(long id, HttpServletResponse response) {
        if (userService.isUsersNotificationTypeTelegram(id)) {
            RedirectConfig.redirect("/api/v1/telegram/bot-url", response);
        } else {
            RedirectConfig.redirect("/api/v1/auth/login", response);
        }
    }
}
