package com.example.puzzle.exception;


public class PuzzleException extends Exception {

    private static final long serialVersionUID = -2790465907756794101L;

    public PuzzleException() {}

    public PuzzleException(String message) {
        super(message);
    }

}
