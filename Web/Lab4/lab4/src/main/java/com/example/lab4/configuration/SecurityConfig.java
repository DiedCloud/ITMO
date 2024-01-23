package com.example.lab4.configuration;

import com.example.lab4.auth.SessionFilter;
import com.example.lab4.configuration.dsl.CustomDsl;
import com.example.lab4.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;

import java.util.Objects;

import static org.springframework.security.config.Customizer.withDefaults;

@ToString
@EqualsAndHashCode
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final UserService userService;
    private final SessionFilter sessionFilter;
    private final PasswordEncoder passwordEncoder;
    private final MvcRequestMatcher.Builder mvc;
    private final CustomDsl dsl;

    @Autowired
    public SecurityConfig(final UserService userService,
                          final SessionFilter sessionFilter,
                          final PasswordEncoder passwordEncoder,
                          final MvcRequestMatcher.Builder mvc,
                          final CustomDsl dsl) {
        this.userService = userService;
        this.sessionFilter = sessionFilter;
        this.passwordEncoder = passwordEncoder;
        this.mvc = mvc;
        this.dsl = dsl;
    }

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        http.cors(AbstractHttpConfigurer::disable).csrf(AbstractHttpConfigurer::disable);
        http.exceptionHandling(eh -> eh.authenticationEntryPoint(
                (rq, rs, ex) -> rs.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                        ex.getLocalizedMessage())
        ));
        http.authorizeHttpRequests((ar) -> {
            ar.requestMatchers(mvc.pattern("/users/login")).permitAll();
            ar.requestMatchers(mvc.pattern("/users/registration")).permitAll();
            ar.anyRequest().authenticated();
        }).httpBasic(withDefaults());
        http.apply(dsl);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(final AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
