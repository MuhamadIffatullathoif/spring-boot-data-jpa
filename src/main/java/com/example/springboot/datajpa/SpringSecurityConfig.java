package com.example.springboot.datajpa;

import com.example.springboot.datajpa.auth.handler.LoginSuccessHandler;
import com.example.springboot.datajpa.services.JpaUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
@Configuration
public class SpringSecurityConfig {

    @Autowired
    private LoginSuccessHandler successHandler;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    DataSource dataSource;
    @Autowired
    private JpaUserDetailsService userDetailsService;

//    @Bean
//    public UserDetailsService userDetailsService() throws Exception {
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//        manager.createUser(User
//                .withUsername("Jhon")
//                .password(passwordEncoder.encode("12345")).roles("USER").build());
//        manager.createUser(User
//                .withUsername("admin")
//                .password(passwordEncoder.encode("admin"))
//                .roles("ADMIN", "USER")
//                .build());
//        return manager;
//    }

//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
//        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
//                .jdbcAuthentication()
//                .dataSource(dataSource)
//                .passwordEncoder(passwordEncoder)
//                .usersByUsernameQuery("select username, password, enabled from users where username=?")
//                .authoritiesByUsernameQuery("select u.username, a.authority from authorities a inner join users u on(a.user_id=u.id) where u.username=?")
//                .and().build();
//    }

    @Autowired
    public void userDetailsService(AuthenticationManagerBuilder build) throws Exception {
        build.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) {
        try {
            httpSecurity
                    .authorizeHttpRequests(request -> {
                        request.requestMatchers("/", "/css/**", "/js/**", "/images/**", "/list/**").permitAll()
                                // .requestMatchers("/uploads/**").hasAnyRole("USER")
                                // .requestMatchers("/ver/**").hasAnyRole("USER")
                                // .requestMatchers("/invoice/**").hasAnyRole("ADMIN")
                                .requestMatchers("/form/**").hasAnyRole("ADMIN")
                                .requestMatchers("/delete/**").hasAnyRole("ADMIN")
                                .anyRequest().authenticated();
                    });
            httpSecurity.formLogin(login -> login
                    .successHandler(successHandler)
                    .loginPage("/login")
                    .permitAll());
            httpSecurity.logout(LogoutConfigurer::permitAll);
            httpSecurity.exceptionHandling(exception -> exception
                    .accessDeniedPage("/error_403"));
            return httpSecurity.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
