package com.proj.user.service;

import com.proj.user.model.CustomOAuth2User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService
        implements Customizer<OAuth2LoginConfigurer<HttpSecurity>.UserInfoEndpointConfig> {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        CustomOAuth2User user = new CustomOAuth2User(
                super.loadUser(userRequest)
        );
        log.info("Get {Google} user by username - {}", Optional.ofNullable(user.getAttribute("email")));
        return user;
    }

    @Override
    public void customize(OAuth2LoginConfigurer<HttpSecurity>.UserInfoEndpointConfig userInfoEndpointConfig) {}
}
