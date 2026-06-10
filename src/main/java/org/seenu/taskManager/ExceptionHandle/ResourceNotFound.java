package org.seenu.taskManager.ExceptionHandle;

public class ResourceNotFound extends RuntimeException{
    ResourceNotFound(String message) {
        super(message);
    }
}
