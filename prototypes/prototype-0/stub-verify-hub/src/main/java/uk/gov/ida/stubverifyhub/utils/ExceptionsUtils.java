package uk.gov.ida.stubverifyhub.utils;

import java.util.concurrent.Callable;

public class ExceptionsUtils {

    public static <T> T uncheck(Callable<T> fn) {
        try {
            return fn.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}