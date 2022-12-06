package com.github.ypl.simplyblog.repository;

import com.github.ypl.simplyblog.exception.DataConflictException;
import com.github.ypl.simplyblog.model.Entry;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface EntryRepository extends BaseRepository<Entry> {

    @Query("SELECT e FROM Entry e WHERE e.id=:id AND e.user.id=:userId")
    Optional<Entry> get(int id, int userId);

    default Entry checkBelong(int id, int userId) {
        return get(id, userId).orElseThrow(
                () -> new DataConflictException("Entry id=" + id + " doesn't belong to User id=" + userId));
    }
}
