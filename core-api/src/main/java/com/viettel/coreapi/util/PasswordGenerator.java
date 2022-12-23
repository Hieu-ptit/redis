package com.viettel.coreapi.util;

import org.apache.commons.lang3.RandomStringUtils;

import java.security.SecureRandom;

public class PasswordGenerator {
    private final static char[] possibleCharacters = ("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*").toCharArray();
    private final static SecureRandom secureRandom = new SecureRandom();

    public static String generate(int length) {
        return RandomStringUtils.random(length, 0, possibleCharacters.length - 1, false, false, possibleCharacters, secureRandom);
    }

}
