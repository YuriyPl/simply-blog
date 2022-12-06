package com.github.ypl.simplyblog.web.user;

import com.github.ypl.simplyblog.MatcherFactory;
import com.github.ypl.simplyblog.model.Role;
import com.github.ypl.simplyblog.model.User;
import com.github.ypl.simplyblog.util.JsonUtil;

public class UserTestData {
    public static final MatcherFactory.Matcher<User> USER_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(User.class, "password", "entries", "comments");

    public static int ADMIN_ID = 1;
    public static int USER_ID = 2;
    public static final int NOT_FOUND = 100;
    public static final String ADMIN_MAIL = "admin@yandex.ru";
    public static final String USER_MAIL = "user@yandex.ru";

    public static User admin = new User(ADMIN_ID, "admin", ADMIN_MAIL, "admin123", "admin_description", Role.USER, Role.ADMIN);
    public static User user = new User(USER_ID, "user", USER_MAIL, "user123", "user_description", Role.USER);

    public static User getNew() {
        return new User(null, "newUser", "newemail@gmail.com", "12345uuihk", "blabla", Role.USER);
    }

    public static User getUpdated() {
        return new User(USER_ID, "updatedName", "updated@mail.ru", "updatedPassword", "updatedDescription", Role.USER);
    }

    public static String jsonWithPassword(User user, String password) {
        return JsonUtil.writeAdditionProps(user, "password", password);
    }
}
