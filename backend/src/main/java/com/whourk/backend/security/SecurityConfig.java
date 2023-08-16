package com.whourk.backend.security;

import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

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
                    .userInfoEndpoint()
                    .oidcUserService(oidcUserService())
            )
            .logout(logout->
                logout
                    .logoutSuccessUrl("/")
                    .permitAll()
                );

        return http.build();
    }
    @Bean
    public OidcUserService oidcUserService() {
        return (idToken, accessToken, attributes) -> {
            Set<OidcUserAuthority> authorities = new HashSet<>();
            authorities.add(new OidcUserAuthority(attributes));
            return new OidcUser(authorities, idToken, attributes);
        };
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(
            // Define your client registration details here
        );
    }
}
