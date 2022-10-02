package com.multi_project.hanging_man.exceptions;
public class GameOverException extends Exception {
    private String message = "Game is over.";

    public GameOverException() {
        super("Game is over.");
    }

    public String getMessage() {
        return this.message;
    }
}
