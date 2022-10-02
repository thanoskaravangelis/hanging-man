package com.multi_project.hanging_man.exceptions;

public class UndersizeException extends Exception {
    public UndersizeException() {
        super("Error found, dictionary is too short (less than 20 words).");
    }
}
