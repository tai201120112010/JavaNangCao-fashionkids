package com.example.fashionkids.config;

import com.example.fashionkids.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // ✅ 🔥 QUAN TRỌNG NHẤT – FIX AJAX CART
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/register", "/css/**", "/js/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/shop/**", "/product/**", "/cart/**").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated()
                )

                .userDetailsService(userDetailsService)

                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler((request, response, authentication) -> {

                            if (authentication.getAuthorities().stream()
                                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {

                                response.sendRedirect("/admin/dashboard");
                            } else {
                                response.sendRedirect("/shop");
                            }
                        })
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutSuccessUrl("/login")
                        .permitAll()
                );

        return http.build();
    }

    // ✅ Mã hóa mật khẩu
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
