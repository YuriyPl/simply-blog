package com.github.ypl.simplyblog.web.user;

import com.github.ypl.simplyblog.model.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.github.ypl.simplyblog.web.user.UserController.REST_URL;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(REST_URL)
public class UserController extends AbstractUserController {
    protected static final String REST_URL = "/api/v1/user";

    @GetMapping("/{id}")
    public ResponseEntity<User> get(@PathVariable int id) {
        return super.get(id);
    }
}
