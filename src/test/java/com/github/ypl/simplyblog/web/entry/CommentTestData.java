package com.github.ypl.simplyblog.web.entry;

import com.github.ypl.simplyblog.MatcherFactory;
import com.github.ypl.simplyblog.model.Comment;

import java.time.LocalDateTime;

public class CommentTestData {
    public static final MatcherFactory.Matcher<Comment> COMMENT_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Comment.class, "user", "entry");

    public static final int NOT_FOUND = 100;
    public static final int ADMIN_ENTRY_1_COMMENT_ID = 1;
    public static final int USER_ENTRY_1_COMMENT_ID = 2;

    public static final Comment adminComment = new Comment(ADMIN_ENTRY_1_COMMENT_ID, "admin_text_1", LocalDateTime.of(2022, 9, 10, 4, 15, 22));
    public static final Comment userComment1 = new Comment(USER_ENTRY_1_COMMENT_ID, "user_text_1", LocalDateTime.of(2022, 9, 15, 10, 5, 0));

    public static Comment getNew() {
        return new Comment(null, "newText", LocalDateTime.now());
    }
}
