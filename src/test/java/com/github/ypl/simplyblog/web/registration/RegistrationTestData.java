package com.github.ypl.simplyblog.web.registration;

import com.github.ypl.simplyblog.web.registration.payload.RegistrationRequest;

import static com.github.ypl.simplyblog.web.user.UserTestData.user;

public class RegistrationTestData {
    public static final String CONFIRMED_TOKEN = "8407e5e2-0a3f-4e9a-986f-f07405368c26";
    public static final String NOT_CONFIRMED_TOKEN = "8e9456a4-a4d3-464e-920c-c50b9922ad4b";

    public static final RegistrationRequest USER_REQUEST = new RegistrationRequest(user.getName(), user.getEmail(), user.getPassword());

    public static RegistrationRequest getNew() {
        return new RegistrationRequest("newUser", "newemail@gmail.com", "newPassword");
    }
}
