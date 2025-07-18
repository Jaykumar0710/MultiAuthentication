package com.SecureVault.MultiAuthentication.config;

import com.SecureVault.MultiAuthentication.CustomAuthSuccessHandler;
import com.SecureVault.MultiAuthentication.OAuth2LoginSuccessHandler;
import com.SecureVault.MultiAuthentication.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final CustomAuthSuccessHandler customAuthSuccessHandler;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;// Injected

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userDetailsService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/home/**","/register", "/login", "/otp-login", "/otp-verify", "/otp-send", "/css/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/manager/**").hasRole("MANAGER")
                        .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(customAuthSuccessHandler) // or use defaultSuccessUrl() but NOT both
                        .permitAll()
                )

                .oauth2Login(oauth -> oauth
                        .loginPage("/login")
                        .successHandler(oAuth2LoginSuccessHandler)
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true) // invalidate session
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .authenticationProvider(daoAuthProvider())
                .headers(headers -> headers
                        .cacheControl(cache -> cache.disable()) // prevent browser caching
                )
                .csrf(csrf -> csrf.disable()); // optional for testing; enable in prod

        return http.build();
    }
}
