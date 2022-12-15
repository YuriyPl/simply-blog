package com.github.ypl.simplyblog.service;

import com.github.ypl.simplyblog.model.User;
import com.github.ypl.simplyblog.repository.UserRepository;
import com.github.ypl.simplyblog.web.auth.UserDetailsImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        log.debug("Authenticating '{}'", email);
        Optional<User> optionalUser = userRepository.findByEmailIgnoreCase(email);

        return new UserDetailsImpl(optionalUser.orElseThrow(
                () -> new UsernameNotFoundException("User '" + email + "' was not found")));
    }
}
