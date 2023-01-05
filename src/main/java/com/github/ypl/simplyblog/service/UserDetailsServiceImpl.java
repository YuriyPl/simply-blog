package com.github.ypl.simplyblog.service;

import com.github.ypl.simplyblog.exception.IllegalRequestDataException;
import com.github.ypl.simplyblog.model.ConfirmationToken;
import com.github.ypl.simplyblog.model.User;
import com.github.ypl.simplyblog.repository.ConfirmationTokenRepository;
import com.github.ypl.simplyblog.repository.UserRepository;
import com.github.ypl.simplyblog.web.auth.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    @Value("${ypl.app.confirmTokenExpirationMs}")
    private Integer confirmTokenExpirationMs;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ConfirmationTokenRepository confirmationTokenRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        log.debug("Authenticating '{}'", email);
        Optional<User> optionalUser = userRepository.findByEmailIgnoreCase(email);

        return new UserDetailsImpl(optionalUser.orElseThrow(
                () -> new UsernameNotFoundException("User '" + email + "' was not found")));
    }

    public String signUpUser(User user) {
        User dbUser = checkAndSave(user);

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                Instant.now(),
                Instant.now().plusMillis(confirmTokenExpirationMs),
                dbUser
        );
        confirmationTokenRepository.save(confirmationToken);
        return token;
    }

    public void enableUser(String email) {
        userRepository.enableUser(email);
    }

    private User checkAndSave(User user) {
        User dbUser = userRepository.findByEmailIgnoreCase(user.getEmail())
                .orElseGet(
                        () -> prepareAndSave(user)
                );

        if (dbUser.isEnabled()) {
            throw new IllegalRequestDataException(user.getEmail() + " user with this email already exists");
        }

        return dbUser;
    }

    private User prepareAndSave(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEmail(user.getEmail().toLowerCase());
        return userRepository.save(user);
    }
}
