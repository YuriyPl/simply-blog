package com.github.ypl.simplyblog.web.user;

import com.github.ypl.simplyblog.config.PassEncoder;
import com.github.ypl.simplyblog.model.Role;
import com.github.ypl.simplyblog.model.User;
import com.github.ypl.simplyblog.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.EnumSet;

@Slf4j
public class AbstractUserController {

    @Autowired
    private PassEncoder passEncoder;

    @Autowired
    protected UserRepository repository;

    public ResponseEntity<User> get(int id) {
        log.info("get {}", id);
        return ResponseEntity.of(repository.findById(id));
    }

    public void delete(int id) {
        log.info("delete {}", id);
        repository.deleteExisted(id);
    }

    protected User prepareAndSaveNewUser(User user) {
        user.setRoles(EnumSet.of(Role.USER));
        return prepareAndSave(user);
    }

    protected User prepareAndSave(User user) {
        user.setPassword(passEncoder.passwordEncoder().encode(user.getPassword()));
        user.setEmail(user.getEmail().toLowerCase());
        return repository.save(user);
    }
}
