package com.multi_project.hanging_man.exceptions;

public class InvalidRangeException extends Exception {
    public InvalidRangeException() {
        super("Error found, dictionary should not contain words with less than 6 letters. ");
    }
}
