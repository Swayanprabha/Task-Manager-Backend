package org.seenu.taskManager.ExceptionHandle;

public class InvalidUserException extends RuntimeException{
    public InvalidUserException(String message) {
        super(message);
    }
}