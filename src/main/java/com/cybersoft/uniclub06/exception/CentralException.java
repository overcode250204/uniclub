package com.cybersoft.uniclub06.exception;

import com.cybersoft.uniclub06.reponse.BaseReponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CentralException {

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<?> handleException(Exception ex) {
        BaseReponse reponse = new BaseReponse();
        reponse.setStatusCode(500);
        reponse.setStatusMessage(ex.getMessage());

        return new ResponseEntity<>(reponse, HttpStatus.OK);
    }
    @ExceptionHandler({AuthenException.class})
    public ResponseEntity<?> handleException1(Exception ex) {
        BaseReponse reponse = new BaseReponse();
        reponse.setStatusCode(200);
        reponse.setStatusMessage(ex.getMessage());

        return new ResponseEntity<>(reponse, HttpStatus.OK);
    }
}
