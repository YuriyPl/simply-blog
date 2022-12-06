package com.github.ypl.simplyblog.service;

import com.github.ypl.simplyblog.model.Comment;
import com.github.ypl.simplyblog.repository.CommentRepository;
import com.github.ypl.simplyblog.repository.EntryRepository;
import com.github.ypl.simplyblog.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EntryRepository entryRepository;

    public Comment save(int userId, int entryId, Comment comment) {
        comment.setUser(userRepository.getReferenceById(userId));
        comment.setEntry(entryRepository.getReferenceById(entryId));
        return commentRepository.save(comment);
    }
}
