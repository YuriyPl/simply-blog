package com.github.ypl.simplyblog.web.entry;

import com.github.ypl.simplyblog.model.Entry;
import com.github.ypl.simplyblog.repository.EntryRepository;
import com.github.ypl.simplyblog.util.JsonUtil;
import com.github.ypl.simplyblog.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.github.ypl.simplyblog.web.entry.EntryController.REST_URL;
import static com.github.ypl.simplyblog.web.entry.EntryTestData.*;
import static com.github.ypl.simplyblog.web.user.UserTestData.ADMIN_MAIL;
import static com.github.ypl.simplyblog.web.user.UserTestData.USER_MAIL;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EntryControllerTest extends AbstractControllerTest {

    @Autowired
    private EntryRepository repository;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/" + USER_ENTRY_1_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(ENTRY_MATCHER.contentJson(userEntry1));
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/" + NOT_FOUND))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void create() throws Exception {
        Entry newEntry = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newEntry)))
                .andDo(print())
                .andExpect(status().isCreated());

        Entry created = ENTRY_MATCHER.readFromJson(action);
        int newId = created.id();
        newEntry.setId(newId);
        ENTRY_MATCHER.assertMatch(created, newEntry);
        ENTRY_MATCHER.assertMatch(repository.getExisted(newId), newEntry);
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void createInvalid() throws Exception {
        Entry invalidEntry = new Entry(null, null, null);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalidEntry)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void createUnAuth() throws Exception {
        Entry newEntry = getNew();
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newEntry)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void update() throws Exception {
        Entry updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + "/" + USER_ENTRY_1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());

        ENTRY_MATCHER.assertMatch(repository.getExisted(USER_ENTRY_1_ID), getUpdated());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateInvalid() throws Exception {
        Entry invalid = new Entry(null, null, null, null);
        perform(MockMvcRequestBuilders.put(REST_URL + "/" + USER_ENTRY_1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void updateUnAuth() throws Exception {
        Entry updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + "/" + updated.id())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateNotOwn() throws Exception {
        Entry updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + "/" + USER_ENTRY_1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + "/" + USER_ENTRY_2_ID))
                .andExpect(status().isNoContent());
        ENTRY_MATCHER.assertMatch(repository.findAll(), adminEntry1, adminEntry2, userEntry1);
    }

    @Test
    void deleteUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + "/" + USER_ENTRY_2_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteNotOwn() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + "/" + USER_ENTRY_2_ID))
                .andExpect(status().isConflict());
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(ENTRY_MATCHER.contentJson(userEntry2, adminEntry2, userEntry1, adminEntry1));
    }
}