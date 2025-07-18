package com.SecureVault.MultiAuthentication;

import com.SecureVault.MultiAuthentication.entity.User;
import com.SecureVault.MultiAuthentication.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepo;

    public OAuth2LoginSuccessHandler(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        String email = oauthUser.getAttribute("email");
        String name = oauthUser.getAttribute("name");

        // Fallback for GitHub (in case name is null)
        if (name == null) {
            name = oauthUser.getAttribute("login");
        }

        // Create user if not exists
        if (!userRepo.existsByEmail(email)) {
            User user = new User();
            user.setEmail(email);
            user.setName(name);
            user.setRole(User.Role.USER); // ✅ Use enum directly, not "ROLE_USER"
            user.setEnabled(true);
            userRepo.save(user);
        }

        response.sendRedirect("/dashboard");
    }
}
