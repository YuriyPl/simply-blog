package com.github.ypl.simplyblog.web.entry;

import com.github.ypl.simplyblog.model.Comment;
import com.github.ypl.simplyblog.repository.CommentRepository;
import com.github.ypl.simplyblog.util.JsonUtil;
import com.github.ypl.simplyblog.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static com.github.ypl.simplyblog.web.entry.CommentController.REST_URL;
import static com.github.ypl.simplyblog.web.entry.CommentTestData.COMMENT_MATCHER;
import static com.github.ypl.simplyblog.web.entry.CommentTestData.NOT_FOUND;
import static com.github.ypl.simplyblog.web.entry.CommentTestData.USER_ENTRY_1_COMMENT_ID;
import static com.github.ypl.simplyblog.web.entry.CommentTestData.adminComment;
import static com.github.ypl.simplyblog.web.entry.CommentTestData.getNew;
import static com.github.ypl.simplyblog.web.entry.CommentTestData.userComment1;
import static com.github.ypl.simplyblog.web.entry.EntryTestData.ADMIN_ENTRY_1_ID;
import static com.github.ypl.simplyblog.web.user.UserTestData.ADMIN_MAIL;
import static com.github.ypl.simplyblog.web.user.UserTestData.USER_MAIL;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CommentControllerTest extends AbstractControllerTest {

    @Autowired
    private CommentRepository repository;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/" + USER_ENTRY_1_COMMENT_ID, ADMIN_ENTRY_1_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(COMMENT_MATCHER.contentJson(userComment1));
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/" + NOT_FOUND, ADMIN_ENTRY_1_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void create() throws Exception {
        Comment newComment = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL, ADMIN_ENTRY_1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newComment)))
                .andDo(print())
                .andExpect(status().isCreated());

        Comment created = COMMENT_MATCHER.readFromJson(action);
        int newId = created.id();
        newComment.setId(newId);
        COMMENT_MATCHER.assertMatch(created, newComment);
        COMMENT_MATCHER.assertMatch(repository.getExisted(newId), newComment);
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void createInvalid() throws Exception {
        Comment invalidEntry = new Comment(null, null, null);
        perform(MockMvcRequestBuilders.post(REST_URL, ADMIN_ENTRY_1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalidEntry)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void createUnAuth() throws Exception {
        Comment newEntry = getNew();
        perform(MockMvcRequestBuilders.post(REST_URL, ADMIN_ENTRY_1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newEntry)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    @Transactional
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + "/" + USER_ENTRY_1_COMMENT_ID, ADMIN_ENTRY_1_ID))
                .andExpect(status().isNoContent());
        COMMENT_MATCHER.assertMatch(repository.findAllByEntryId(ADMIN_ENTRY_1_ID), adminComment);
    }

    @Test
    void deleteUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + "/" + USER_ENTRY_1_COMMENT_ID, ADMIN_ENTRY_1_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteNotOwn() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + "/" + USER_ENTRY_1_COMMENT_ID, ADMIN_ENTRY_1_ID))
                .andExpect(status().isConflict());
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL, ADMIN_ENTRY_1_ID))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(COMMENT_MATCHER.contentJson(adminComment, userComment1));
    }
}
