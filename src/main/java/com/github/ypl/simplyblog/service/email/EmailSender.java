package com.github.ypl.simplyblog.service.email;

public interface EmailSender {
    void send(String to, String email);
}
