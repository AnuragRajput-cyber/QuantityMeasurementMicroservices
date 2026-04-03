package com.app.auth_service.security;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.app.auth_service.config.AppProperties;
import com.app.auth_service.entity.User;
import com.app.auth_service.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuthSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final AppProperties properties;

    public OAuthSuccessHandler(UserRepository userRepository, JwtUtil jwtUtil, AppProperties properties) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.properties = properties;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {
        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();

        String email = oauthUser.getAttribute("email");
        String name = oauthUser.getAttribute("name");
        String picture = oauthUser.getAttribute("picture");

        User user = userRepository.findByEmail(email).orElseGet(User::new);
        user.setEmail(email);
        user.setName(name != null ? name : email);
        user.setPicture(picture);
        user.setProvider(user.getProvider() == null ? "GOOGLE" : user.getProvider());

        User savedUser = userRepository.save(user);
        String token = jwtUtil.generateToken(savedUser);

        String redirectUrl = properties.getFrontend().getSuccessUrl()
                + "?token="
                + URLEncoder.encode(token, StandardCharsets.UTF_8);

        response.sendRedirect(redirectUrl);
    }
}
