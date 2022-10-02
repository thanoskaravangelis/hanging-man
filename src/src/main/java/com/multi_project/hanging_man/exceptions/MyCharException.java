package com.multi_project.hanging_man.exceptions;

public class MyCharException extends Exception {
    public MyCharException() {
        super("This character is already shown");
    }
}
