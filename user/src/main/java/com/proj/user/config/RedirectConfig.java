package com.proj.user.config;

import com.proj.user.exception.RedirectException;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RedirectConfig {

    public static void redirect(String path, HttpServletResponse response) {
        try {
            response.sendRedirect(path);
        } catch (Exception exception) {
            log.error("Error while redirect user to {}", path);
            throw new RedirectException("Sorry, that`s our mistake, error was appeared while we redirect you, " +
                    " we are already working on it!⚒️");
        }
    }
}
