package com.proj.user.service;

import com.proj.user.model.Provider;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthUserService {

    private final UserService userService;

    public boolean isUserAdminOrSame(long id, String email) {
        return isAdmin(email) || userService.readByEmail(email).getId() == id;
    }

    public boolean isUserAuthWithGoogle(long id) {
        return userService.readById(id).getProvider().equals(Provider.GOOGLE);
    }

    public boolean isAdmin(String email) {
        return userService.readByEmail(email)
                .getRole()
                .getName()
                .equals("ADMIN");
    }
}
