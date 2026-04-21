package io.github.pluton33.ezgloszenie.controller;

import io.github.pluton33.ezgloszenie.data.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class) // lapie tylko bledy typu ResponseStatusException

    public ResponseEntity<ErrorResponse> handleResponseStatusException(ResponseStatusException ex) 
    {
        // tworzenie pudelka z danymi
        ErrorResponse error = new ErrorResponse(
                ex.getStatusCode().value(), 
                ex.getReason()
        );
        // zwracanie odpowiedzi z pudelkiem i statusem HTTP 
        return new ResponseEntity<>(error, ex.getStatusCode());
    }
}
