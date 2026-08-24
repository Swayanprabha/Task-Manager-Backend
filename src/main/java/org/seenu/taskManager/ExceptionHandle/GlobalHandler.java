package org.seenu.taskManager.ExceptionHandle;
import org.seenu.taskManager.dto.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalHandler {
    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorMessage> handleException(Exception e){
        return new ResponseEntity<>(new ErrorMessage("something went wrong ",HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
@ExceptionHandler({UserAlreadyExistsException.class,InvalidUserException.class})
    public ResponseEntity<ErrorMessage> handleBadRequest(RuntimeException e){
    return new ResponseEntity<>(new ErrorMessage(e.getMessage(),HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
}
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleNotFound(ResourceNotFoundException e) {
        return new ResponseEntity<>(new ErrorMessage(e.getMessage(), HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
    }

}
