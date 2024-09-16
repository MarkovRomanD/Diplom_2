package com.my;

import com.my.models.User;
import org.apache.commons.lang3.RandomStringUtils;

public class GenerateUser {
    public static User create() {
        return new User(
                createEmail(),
                createPassword(),
                createName()
        );
    }

    public static String createEmail() {
        return String.format(
                "%s@%s.%s",
                RandomStringUtils.random(10, true, false).toLowerCase(),
                RandomStringUtils.random(3, true, false).toLowerCase(),
                RandomStringUtils.random(3, true, false).toLowerCase());
    }

    public static String createName() {
        return RandomStringUtils.random(10, true, false).toLowerCase();
    }

    public static String createPassword() {
        return RandomStringUtils.random(10, true, false).toLowerCase();
    }
}
