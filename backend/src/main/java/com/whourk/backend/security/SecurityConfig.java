package com.whourk.backend.security;

import com.whourk.backend.security.auth.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests(authorizeRequests->
                authorizeRequests
                    .antMatchers("/", "/login**", "/error**")
                    .permitAll()
                    .anyRequest().authenticated()
            )
            .oauth2Login(oauth2Login->
                oauth2Login
                    .loginPage("/login")
                    .defaultSuccessUrl("/dashboard")
                    .userInfoEndpoint(userInfoEndpoint-> userInfoEndpoint.userService(
                        customOAuth2UserService))
            )
            .logout(logout->
                logout
                    .logoutSuccessUrl("/")
                    .permitAll()
                );

        return http.build();
    }

}
