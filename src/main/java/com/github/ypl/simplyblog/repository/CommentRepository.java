package com.github.ypl.simplyblog.repository;

import com.github.ypl.simplyblog.exception.DataConflictException;
import com.github.ypl.simplyblog.model.Comment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface CommentRepository extends BaseRepository<Comment> {

    @Query("SELECT c FROM Comment c WHERE c.entry.id=:entryId")
    List<Comment> findAllByEntryId(int entryId);

    @Query("SELECT c FROM Comment c WHERE c.entry.id=:entryId AND c.id=:id")
    Optional<Comment> findAllByIdAndEntryId(int id, int entryId);

    @Query("SELECT c FROM Comment c WHERE c.id=:id AND c.entry.id=:entryId AND c.user.id=:userId")
    Optional<Comment> get(int id, int entryId, int userId);

    default Comment checkBelong(int id, int entryId, int userId) {
        return get(id, entryId, userId).orElseThrow(
                () -> new DataConflictException("Comment id=" + id + " doesn't belong to Entry id=" + entryId + "or User id=" + userId));
    }
}
