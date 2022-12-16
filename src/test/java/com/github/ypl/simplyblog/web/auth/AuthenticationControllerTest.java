package com.github.ypl.simplyblog.web.auth;

import com.github.ypl.simplyblog.util.JsonUtil;
import com.github.ypl.simplyblog.web.AbstractControllerTest;
import com.github.ypl.simplyblog.web.auth.payload.request.TokenRefreshRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.github.ypl.simplyblog.web.auth.AuthenticationController.REST_URL;
import static com.github.ypl.simplyblog.web.auth.AuthenticationTestData.NOT_FOUND_AUTHENTICATION_REQUEST;
import static com.github.ypl.simplyblog.web.auth.AuthenticationTestData.USER_AUTHENTICATION_REQUEST;
import static com.github.ypl.simplyblog.web.auth.AuthenticationTestData.USER_EXPIRED_TOKEN;
import static com.github.ypl.simplyblog.web.auth.AuthenticationTestData.USER_TOKEN;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthenticationControllerTest extends AbstractControllerTest {

    @Test
    public void authenticateUser() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL + "/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(USER_AUTHENTICATION_REQUEST)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void authenticateUserNotFound() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL + "/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(NOT_FOUND_AUTHENTICATION_REQUEST)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void refreshToken() throws Exception {
        TokenRefreshRequest request = new TokenRefreshRequest();
        request.setRefreshToken(USER_TOKEN);

        perform(MockMvcRequestBuilders.post(REST_URL + "/refresh_token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(request)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void refreshTokenExpired() throws Exception {
        TokenRefreshRequest request = new TokenRefreshRequest();
        request.setRefreshToken(USER_EXPIRED_TOKEN);

        perform(MockMvcRequestBuilders.post(REST_URL + "/refresh_token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(request)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void refreshTokenNotFound() throws Exception {
        TokenRefreshRequest request = new TokenRefreshRequest();
        request.setRefreshToken("-");

        perform(MockMvcRequestBuilders.post(REST_URL + "/refresh_token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(request)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void deleteTokens() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + "/delete_tokens")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(USER_AUTHENTICATION_REQUEST)))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteTokensUserNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + "/delete_tokens")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(NOT_FOUND_AUTHENTICATION_REQUEST)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
