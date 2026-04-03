package com.app.auth_service.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.app.auth_service.security.JwtAuthFilter;
import com.app.auth_service.security.OAuthSuccessHandler;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(AppProperties.class)
public class SecurityConfig {

    private final OAuthSuccessHandler successHandler;
    private final JwtAuthFilter jwtAuthFilter;
    private final ObjectProvider<ClientRegistrationRepository> clientRegistrationRepository;

    public SecurityConfig(
            OAuthSuccessHandler successHandler,
            JwtAuthFilter jwtAuthFilter,
            ObjectProvider<ClientRegistrationRepository> clientRegistrationRepository
    ) {
        this.successHandler = successHandler;
        this.jwtAuthFilter = jwtAuthFilter;
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/auth/register",
                                "/auth/login",
                                "/oauth2/**",
                                "/login/**",
                                "/error",
                                "/swagger-ui",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/h2-console/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex.authenticationEntryPoint((request, response, exception) ->
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED)
                ))
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        if (clientRegistrationRepository.getIfAvailable() != null) {
            http.oauth2Login(oauth -> oauth.successHandler(successHandler));
        }

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
