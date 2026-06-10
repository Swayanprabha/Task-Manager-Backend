package org.seenu.taskManager.ExceptionHandle;
import org.seenu.taskManager.dto.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleException(Exception e){
        Integer statuscode= HttpStatus.INTERNAL_SERVER_ERROR.value();
        ErrorMessage errorMessage = new ErrorMessage(e.getMessage(),statuscode);
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
