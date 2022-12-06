package com.github.ypl.simplyblog.web.entry;

import com.github.ypl.simplyblog.AuthUser;
import com.github.ypl.simplyblog.model.Comment;
import com.github.ypl.simplyblog.repository.CommentRepository;
import com.github.ypl.simplyblog.service.CommentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static com.github.ypl.simplyblog.web.entry.CommentController.REST_URL;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(REST_URL)
public class CommentController {
    protected static final String REST_URL = "/api/v1/entry/{entryId}/comments";

    private final CommentRepository commentRepository;
    private final CommentService commentService;

    @GetMapping("/{id}")
    public ResponseEntity<Comment> get(@PathVariable int entryId, @PathVariable int id) {
        return ResponseEntity.of(commentRepository.findAllByIdAndEntryId(id, entryId));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Comment> create(
            @AuthenticationPrincipal AuthUser authUser, @PathVariable int entryId, @Valid @RequestBody Comment comment) {
        log.info("create for entry {} and user {}", entryId, authUser.id());
        Comment created = commentService.save(authUser.id(), entryId, comment);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(entryId, created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @DeleteMapping("/{id}")
    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthUser authUser, @PathVariable int entryId, @PathVariable int id) {
        log.info("delete from entry {} and user {}", entryId, authUser.id());
        Comment comment = commentRepository.checkBelong(id, entryId, authUser.id());
        commentRepository.delete(comment);
    }

    @GetMapping
    public List<Comment> getAll(@PathVariable int entryId) {
        log.info("get all for entry {}", entryId);
        return commentRepository.findAllByEntryId(entryId);
    }
}
