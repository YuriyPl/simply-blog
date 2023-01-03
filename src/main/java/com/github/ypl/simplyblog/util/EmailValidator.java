package com.github.ypl.simplyblog.util;

import com.github.ypl.simplyblog.exception.IllegalRequestDataException;
import lombok.experimental.UtilityClass;

import java.util.regex.Pattern;

@UtilityClass
public class EmailValidator {
    public static void validate(String email) {
        if (!Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$").matcher(email).matches()) {
            throw new IllegalRequestDataException(email + " email not valid");
        }
    }
}
