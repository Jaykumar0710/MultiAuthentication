package com.SecureVault.MultiAuthentication;

import com.SecureVault.MultiAuthentication.service.OtpService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {

    private final OtpService otpService;

    public CustomAuthSuccessHandler(OtpService otpService) {
        this.otpService = otpService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        String email = authentication.getName();
        otpService.generateAndSendOtp(email);

        // Temporarily store the auth in session
        request.getSession().setAttribute("PRE_AUTH_USER", authentication);

        // Redirect to 2FA verify page
        response.sendRedirect("/2fa-verify");
    }
}