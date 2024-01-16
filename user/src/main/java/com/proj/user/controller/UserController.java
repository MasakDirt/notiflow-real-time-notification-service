package com.proj.user.controller;

import com.proj.user.config.RedirectConfig;
import com.proj.user.dto.UpdateRequest;
import com.proj.user.mapper.UserMapper;
import com.proj.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public ModelAndView getUsers(ModelMap modelMap, Authentication authentication) {
        modelMap.addAttribute("users", userService.getAll(Pageable.ofSize(4)));
        log.info("USERS-GET === {}", authentication.getName());
        return new ModelAndView("users", modelMap);
    }

    @GetMapping("/update/{id}")
    @PreAuthorize("@authUserService.isUserSame(#id, authentication.principal)")
    public ModelAndView getUpdateForm(@PathVariable long id, ModelMap modelMap, Authentication authentication) {
        modelMap.addAttribute("updateRequest", UpdateRequest.withId(id));
        log.info("UPDATE-FORM-GET === {}, time = {}", authentication.getPrincipal(), LocalDateTime.now());

        return new ModelAndView("user-update", modelMap);
    }

    @PostMapping("/update/{id}")
    @PreAuthorize("@authUserService.isUserSame(#id, authentication.principal)")
    public void update(@PathVariable long id, @Valid UpdateRequest updateRequest,
                       HttpServletResponse response, Authentication authentication) {
        userService.update(userMapper.getUserFromUpdateRequest(updateRequest));
        log.info("UPDATE-POST === {}, time = {}", authentication.getPrincipal(), LocalDateTime.now());
        RedirectConfig.redirect("/api/v1/users", response);
    }
}
