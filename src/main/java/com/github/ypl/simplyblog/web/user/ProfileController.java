package com.github.ypl.simplyblog.web.user;

import com.github.ypl.simplyblog.model.User;
import com.github.ypl.simplyblog.web.auth.UserDetailsImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.github.ypl.simplyblog.util.ValidationUtil.assureIdConsistent;
import static com.github.ypl.simplyblog.web.user.ProfileController.REST_URL;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(REST_URL)
public class ProfileController extends AbstractUserController {
    protected static final String REST_URL = "/api/v1/profile";

    @GetMapping
    public ResponseEntity<User> get(@AuthenticationPrincipal UserDetailsImpl authUser) {
        return super.get(authUser.getId());
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void update(@Valid @RequestBody User user, @AuthenticationPrincipal UserDetailsImpl authUser) {
        log.info("update {} with id={}", user, authUser.getId());
        assureIdConsistent(user, authUser.getId());
        prepareAndSave(user);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void delete(@AuthenticationPrincipal UserDetailsImpl authUser) {
        super.delete(authUser.getId());
    }
}
