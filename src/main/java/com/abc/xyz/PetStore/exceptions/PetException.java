package com.abc.xyz.PetStore.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class PetException extends RuntimeException {

    private HttpStatus httpStatus;

    public PetException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }


}

