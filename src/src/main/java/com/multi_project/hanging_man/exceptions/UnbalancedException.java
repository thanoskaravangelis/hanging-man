package com.multi_project.hanging_man.exceptions;

public class UnbalancedException extends Exception{
    public UnbalancedException() {
        super("Error found, dictionary should have at least 20% of its words with 9 or more letters. ");
    }
}
