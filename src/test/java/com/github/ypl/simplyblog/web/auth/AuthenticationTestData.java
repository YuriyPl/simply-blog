package com.github.ypl.simplyblog.web.auth;

import com.github.ypl.simplyblog.web.auth.payload.request.AuthenticationRequest;

import static com.github.ypl.simplyblog.web.user.UserTestData.user;

public class AuthenticationTestData {
    public static final AuthenticationRequest NOT_FOUND_AUTHENTICATION_REQUEST = new AuthenticationRequest("notfound@gmail.com", "notfoundpassword123");
    public static final AuthenticationRequest USER_AUTHENTICATION_REQUEST = new AuthenticationRequest(user.getEmail(), user.getPassword());

    public static final String USER_EXPIRED_TOKEN = "f9dfbb1c-3cd3-4975-8790-7d2135bd7c0e";
    public static final String USER_TOKEN = "f209c28f-8b96-4837-b6e8-810730f7f959";
}
