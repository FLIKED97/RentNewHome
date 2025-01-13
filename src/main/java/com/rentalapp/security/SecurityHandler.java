package com.rentalapp.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

@Component
@Slf4j
public class SecurityHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        String username = authentication.getName();
        String redirectUrl = "/main"; // Default redirect

        // Визначаємо куди перенаправляти в залежності від ролі
        if (roles.contains("ROLE_ADMIN")) {
            redirectUrl = "/main";
            log.info("Admin {} successfully logged in", username);
        } else if (roles.contains("ROLE_USER")) {
            redirectUrl = "/main";
            log.info("User {} successfully logged in", username);
        }

        // Зберігаємо важливу інформацію в сесії, якщо потрібно
        HttpSession session = request.getSession();
        session.setAttribute("userRole", roles.iterator().next());

        response.sendRedirect(redirectUrl);
    }
}
