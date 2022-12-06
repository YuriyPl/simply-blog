package com.github.ypl.simplyblog.config;

import com.github.ypl.simplyblog.AuthUser;
import com.github.ypl.simplyblog.model.Role;
import com.github.ypl.simplyblog.model.User;
import com.github.ypl.simplyblog.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    public static final PasswordEncoder PASSWORD_ENCODER = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private final UserRepository userRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PASSWORD_ENCODER;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> {
            log.debug("Authenticating '{}'", email);
            Optional<User> optionalUser = userRepository.findByEmailIgnoreCase(email);
            return new AuthUser(optionalUser.orElseThrow(
                    () -> new UsernameNotFoundException("User '" + email + "' was not found")));
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/v1/profile").anonymous()
                .antMatchers("/api/v1/profile").authenticated()
                .antMatchers("/api/v1/admin/**").hasRole(Role.ADMIN.name())
                .antMatchers(HttpMethod.GET, "/api/v1/entry/*").permitAll()
                .antMatchers("/api/v1/entry/*").authenticated()
                .antMatchers(HttpMethod.POST, "/api/v1/entry").authenticated()
                .antMatchers(HttpMethod.GET, "/api/v1/entry/*/comments/*").permitAll()
                .antMatchers("/api/v1/entry/*/comments/*").authenticated()
                .antMatchers(HttpMethod.GET, "/api/v1/entry/*/comments").permitAll()
                .antMatchers("/api/v1/entry/*/comments").authenticated()
                .and().httpBasic()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().csrf().disable();
        return http.build();
    }
}
