package com.github.ypl.simplyblog.web.registration.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
public class RegistrationRequest {

    @NotBlank
    @Size(max = 32)
    private final String name;

    @Email
    @NotBlank
    @Size(max = 64)
    private final String email;

    @NotBlank
    @Size(min = 5, max = 128)
    private final String password;
}
