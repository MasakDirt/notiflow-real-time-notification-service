package com.proj.user.controller;

import com.proj.user.config.RedirectConfig;
import com.proj.user.service.NotificationService;
import com.proj.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/get-notification")
public class NotificationController {

    private final UserService userService;
    private final NotificationService notificationService;

    @PostMapping("/{sender-id}")
    public void getNotificationFromUser(@PathVariable("sender-id") long senderId, Authentication authentication,
                                        HttpServletResponse response) {
        String recipientEmail = authentication.getName();
        log.info("Preparing to receive data to user {}", recipientEmail);
        notificationService.sendDataForNotification(recipientEmail, senderId);
        log.info("Data successfully transfer!");
        RedirectConfig.redirect("/api/v1/get-notification/success", response);
    }

    @GetMapping("/success")
    public ModelAndView getSuccessPage(ModelMap modelMap, Authentication authentication) {
        var authUser = userService.readByEmail(authentication.getName());
        var authNotificationType = authUser.getNotificationType();
        String authUserName = authUser.getFullName();

        modelMap.addAttribute("notificationType", authNotificationType.getName());
        modelMap.addAttribute("userName", authUserName);
        return new ModelAndView("success-notification", modelMap);
    }
}
