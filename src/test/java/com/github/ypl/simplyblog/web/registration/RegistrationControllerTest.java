package com.github.ypl.simplyblog.web.registration;

import com.github.ypl.simplyblog.util.JsonUtil;
import com.github.ypl.simplyblog.web.AbstractControllerTest;
import com.github.ypl.simplyblog.web.registration.payload.RegistrationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.github.ypl.simplyblog.web.registration.RegistrationController.REST_URL;
import static com.github.ypl.simplyblog.web.registration.RegistrationTestData.CONFIRMED_TOKEN;
import static com.github.ypl.simplyblog.web.registration.RegistrationTestData.NOT_CONFIRMED_TOKEN;
import static com.github.ypl.simplyblog.web.registration.RegistrationTestData.USER_REQUEST;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RegistrationControllerTest extends AbstractControllerTest {

    @Test
    void registerInvalid() throws Exception {
        RegistrationRequest invalid = new RegistrationRequest(null, null, null);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void registerDuplicate() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(USER_REQUEST)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void confirm() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/confirm").param("token", NOT_CONFIRMED_TOKEN)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void confirmDuplicate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/confirm").param("token", CONFIRMED_TOKEN)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void confirmTokenNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/confirm").param("token", "-")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }
}
