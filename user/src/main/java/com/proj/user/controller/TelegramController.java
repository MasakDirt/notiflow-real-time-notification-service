package com.proj.user.controller;

import org.springframework.security.core.Authentication;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/api/v1/telegram")
public class TelegramController {

    @GetMapping("/bot-url")
    public ModelAndView telegramBotView(Authentication authentication, ModelMap modelMap) {
        modelMap.addAttribute("isAuthUser", authentication != null);
        return new ModelAndView("telegram-bot-url", modelMap);
    }
}
