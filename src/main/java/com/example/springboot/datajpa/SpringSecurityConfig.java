package com.example.springboot.datajpa;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringSecurityConfig {

    @Bean
    public static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() throws Exception {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User
                .withUsername("Jhon")
                .password(passwordEncoder().encode("12345")).roles("USER").build());
        manager.createUser(User
                .withUsername("admin")
                .password(passwordEncoder().encode("admin"))
                .roles("ADMIN", "USER")
                .build());
        return manager;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) {
        try {
            httpSecurity
                    .authorizeHttpRequests(request -> {
                        request.requestMatchers("/", "/css/**", "/js/**", "/images/**", "/list/**").permitAll()
                                .requestMatchers("/uploads/**").hasAnyRole("USER")
                                .requestMatchers("/ver/**").hasAnyRole("USER")
                                .requestMatchers("/invoice/**").hasAnyRole("ADMIN")
                                .requestMatchers("/form/**").hasAnyRole("ADMIN")
                                .requestMatchers("/delete/**").hasAnyRole("ADMIN")
                                .anyRequest().authenticated();
                    });
            httpSecurity.formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer.loginPage("/login").permitAll());
            httpSecurity.logout(LogoutConfigurer::permitAll);
            return httpSecurity.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
