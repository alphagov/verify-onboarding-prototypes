package uk.gov.ida.stubverifyhub.utils;

import java.util.Base64;

public class Base64EncodeUtils {

    public static String encode(String string) {
        return new String(Base64.getEncoder().encode(string.toString().getBytes()));
    }

    public static String decode(String string) {
        return new String(Base64.getDecoder().decode(string));
    }
}
