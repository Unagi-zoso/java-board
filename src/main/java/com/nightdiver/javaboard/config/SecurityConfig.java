package com.nightdiver.javaboard.config;

import static org.springframework.security.config.Customizer.withDefaults;

import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll() // forward 요청은 모두 허용, forward 란 서버 내부에서 서블릿이나 jsp 등을 호출하는 방식
                        .anyRequest().permitAll()
                )
                .formLogin(withDefaults())
                .logout(withDefaults())
                .build();
    }
}
