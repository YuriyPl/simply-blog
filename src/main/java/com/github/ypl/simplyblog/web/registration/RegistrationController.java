package com.github.ypl.simplyblog.web.registration;

import com.github.ypl.simplyblog.model.User;
import com.github.ypl.simplyblog.service.RegistrationService;
import com.github.ypl.simplyblog.web.registration.payload.RegistrationRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.github.ypl.simplyblog.web.registration.RegistrationController.REST_URL;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(value = REST_URL)
public class RegistrationController {
    protected static final String REST_URL = "/api/v1/registration";

    private final RegistrationService registrationService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public User register(@RequestBody @Valid RegistrationRequest request) {
        log.info("register {}", request.getEmail());
        return registrationService.register(request);
    }

    @GetMapping("/confirm")
    public void confirm(@RequestParam("token") String token) {
        log.info("confirm token {}", token);
        registrationService.confirmToken(token);
    }
}
