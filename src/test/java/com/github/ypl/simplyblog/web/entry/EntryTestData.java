package com.github.ypl.simplyblog.web.entry;

import com.github.ypl.simplyblog.MatcherFactory;
import com.github.ypl.simplyblog.model.Entry;

import java.time.LocalDateTime;

public class EntryTestData {
    public static final MatcherFactory.Matcher<Entry> ENTRY_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Entry.class, "updated", "user", "comments");

    public static final int NOT_FOUND = 100;
    public static final int ADMIN_ENTRY_1_ID = 1;
    public static final int ADMIN_ENTRY_2_ID = 2;
    public static final int USER_ENTRY_1_ID = 3;
    public static final int USER_ENTRY_2_ID = 4;

    public static Entry adminEntry1 = new Entry(ADMIN_ENTRY_1_ID, "entry_1_title", "entry_1_text", LocalDateTime.of(2022, 9, 10, 4, 5, 6));
    public static Entry adminEntry2 = new Entry(ADMIN_ENTRY_2_ID, "entry_2_title", "entry_2_text");
    public static Entry userEntry1 = new Entry(USER_ENTRY_1_ID, "entry_3_title", "entry_3_text", LocalDateTime.of(2022, 9, 25, 9, 12, 55));
    public static Entry userEntry2 = new Entry(USER_ENTRY_2_ID, "entry_4_title", "entry_4_text");

    public static Entry getNew() {
        return new Entry(null, "newTitle", "newText");
    }

    public static Entry getUpdated() {
        return new Entry(USER_ENTRY_1_ID, "updatedTitle", userEntry1.getContent());
    }
}
