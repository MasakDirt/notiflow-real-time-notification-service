package com.proj.user.controller;

import com.proj.user.dto.UserResponse;
import com.proj.user.mapper.UserMapper;
import com.proj.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public List<UserResponse> getUsers() {
        return userService.getAll(Pageable.ofSize(4))
                .stream().map(userMapper::getUserResponseFromUser).toList();
    }
}
