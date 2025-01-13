package com.rentalapp.config;


import com.rentalapp.security.SecurityHandler;
import com.rentalapp.services.PersonDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final PersonDetailsService personDetailsService;
    private final SecurityHandler securityHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf((csrf) -> csrf
                        .csrfTokenRepository(new HttpSessionCsrfTokenRepository()))
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfiguration = new CorsConfiguration();
                    corsConfiguration.setAllowedOriginPatterns(List.of("*"));
                    corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    corsConfiguration.setAllowedHeaders(List.of("*"));
                    corsConfiguration.setAllowCredentials(true);
                    return corsConfiguration;
                }))
                .authorizeHttpRequests(request -> request
                        // Публічні маршрути
                        .requestMatchers(
                                "/auth/registration",
                                "/auth/**",
                                "/search/**",
                                "/properties/view/**",    // Перегляд оголошень доступний всім
                                "/home/**"
                        ).permitAll()

                        // Маршрути для Landlord
                        .requestMatchers(
                                "/landlord/**",
                                "/properties/create/**",
                                "/properties/edit/**",
                                "/properties/delete/**"
                        ).hasRole("LANDLORD")

                        // Маршрути для Tenant
                        .requestMatchers(
                                "/tenant/**",
                                "/bookings/**",
                                "/reviews/**"
                        ).hasRole("TENANT")

                        // Маршрути для Admin
                        .requestMatchers(
                                "/admin/**",
                                "/management/**",
                                "/users/**"
                        ).hasRole("ADMIN")

                        // Загальні маршрути для авторизованих користувачів
                        .requestMatchers(
                                "/profile/**",
                                "/messages/**",
                                "/notifications/**"
                        ).hasAnyRole("TENANT", "LANDLORD", "ADMIN")

                        // Всі інші запити потребують автентифікації
                        .anyRequest().authenticated()
                )
                .formLogin((login) -> login
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/process_login")
                        .usernameParameter("email")
                        .successHandler(securityHandler)
                        .failureUrl("/auth/login?error")
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/auth/login")
                        .permitAll()
                )
                .authenticationProvider(authenticationProvider());
        return http.build();
    }

    // Базові налаштування залишаються тими самими
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(personDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }


    @Bean
    public RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.fromHierarchy("""
            ROLE_ADMIN > ROLE_LANDLORD
            ROLE_ADMIN > ROLE_TENANT
            """);
    }

    @Bean
    public SecurityExpressionHandler<FilterInvocation> customWebSecurityExpressionHandler() {
        DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
        handler.setRoleHierarchy(roleHierarchy());
        return handler;
    }
}