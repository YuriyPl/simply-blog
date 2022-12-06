package com.github.ypl.simplyblog.web.entry;

import com.github.ypl.simplyblog.AuthUser;
import com.github.ypl.simplyblog.model.Entry;
import com.github.ypl.simplyblog.repository.EntryRepository;
import com.github.ypl.simplyblog.service.EntryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static com.github.ypl.simplyblog.util.ValidationUtil.assureIdConsistent;
import static com.github.ypl.simplyblog.web.entry.EntryController.REST_URL;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(REST_URL)
public class EntryController {
    protected static final String REST_URL = "/api/v1/entry";

    private EntryRepository entryRepository;
    private EntryService entryService;

    @GetMapping("/{id}")
    public ResponseEntity<Entry> get(@PathVariable int id) {
        log.info("get entry {}", id);
        return ResponseEntity.of(entryRepository.findById(id));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Entry> create(@AuthenticationPrincipal AuthUser authUser, @Valid @RequestBody Entry entry) {
        log.info("create entry for user {}", authUser.id());
        Entry created = entryService.save(authUser.id(), entry);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void update(@AuthenticationPrincipal AuthUser authUser, @Valid @RequestBody Entry entry, @PathVariable int id) {
        log.info("update {} for user {}", id, authUser.id());
        assureIdConsistent(entry, id);
        entryRepository.checkBelong(id, authUser.id());
        entryService.save(authUser.id(), entry);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void delete(@AuthenticationPrincipal AuthUser authUser, @PathVariable int id) {
        log.info("delete {} for user {}", id, authUser.id());
        Entry entry = entryRepository.checkBelong(id, authUser.id());
        entryRepository.delete(entry);
    }

    @GetMapping
    public List<Entry> getAll() {
        log.info("get all");
        return entryRepository.findAll(Sort.by(Sort.Direction.DESC, "updated", "title"));
    }
}
