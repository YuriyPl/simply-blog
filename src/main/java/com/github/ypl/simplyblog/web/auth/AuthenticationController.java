package com.github.ypl.simplyblog.web.auth;

import com.github.ypl.simplyblog.config.jwt.JwtUtils;
import com.github.ypl.simplyblog.exception.TokenRefreshException;
import com.github.ypl.simplyblog.model.RefreshToken;
import com.github.ypl.simplyblog.service.RefreshTokenService;
import com.github.ypl.simplyblog.web.auth.payload.request.AuthenticationRequest;
import com.github.ypl.simplyblog.web.auth.payload.request.TokenRefreshRequest;
import com.github.ypl.simplyblog.web.auth.payload.response.JwtResponse;
import com.github.ypl.simplyblog.web.auth.payload.response.TokenRefreshResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.ypl.simplyblog.web.auth.AuthenticationController.REST_URL;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(value = REST_URL)
public class AuthenticationController {

    protected static final String REST_URL = "/api/v1/auth";

    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtils jwtUtils;

    @Transactional
    @PostMapping(path = "/authenticate", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody AuthenticationRequest authRequest) {
        log.info("authenticate user {}", authRequest.getEmail());
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwt = jwtUtils.generateToken(userDetails);
        List<String> roles = userDetails
                .getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken(), userDetails.getId(),
                userDetails.getUsername(), roles));
    }

    @PostMapping(path = "/refresh_token", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TokenRefreshResponse> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        log.info("refresh token {}", request.getRefreshToken());
        String requestRefreshToken = request.getRefreshToken();
        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateToken(new UserDetailsImpl(user));
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "refresh token not found"));
    }

    @DeleteMapping(path = "/delete_tokens", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTokens(@Valid @RequestBody AuthenticationRequest authRequest) {
        log.info("delete all refresh tokens for user {}", authRequest.getEmail());
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        refreshTokenService.deleteAllByUserId(userDetails.getId());
    }
}
