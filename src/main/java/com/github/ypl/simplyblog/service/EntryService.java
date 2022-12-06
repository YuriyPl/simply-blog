package com.github.ypl.simplyblog.service;

import com.github.ypl.simplyblog.model.Entry;
import com.github.ypl.simplyblog.repository.EntryRepository;
import com.github.ypl.simplyblog.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class EntryService {
    private final EntryRepository entryRepository;
    private final UserRepository userRepository;

    public Entry save(int userId, Entry entry) {
        entry.setUser(userRepository.getReferenceById(userId));
        return entryRepository.save(entry);
    }
}
