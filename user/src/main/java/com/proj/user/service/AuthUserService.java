package com.proj.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthUserService {

    private final UserService userService;

    public boolean isUserSame(long id, String email) {
        return userService.readByEmail(email).getId() == id;
    }

    public boolean isAdmin(String email) {
        return userService.readByEmail(email)
                .getRole()
                .getName()
                .equals("ADMIN");
    }
}
