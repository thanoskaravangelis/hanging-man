package com.multi_project.hanging_man.exceptions;

public class InvalidCountException extends Exception {
    public InvalidCountException(String duplicatesString) {
        super("Error found, dictionary should not contain duplicate words. \nFound duplicate words: " + duplicatesString );
    }
}
