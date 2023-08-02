package com.customer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class CustomerException extends Exception{
	
	private final HttpStatus status;

    public CustomerException(String message, HttpStatusCode httpStatusCode) {
        super(message);
        this.status = (HttpStatus) httpStatusCode;
    }

    public HttpStatus getStatus() {
        return status;
    }
	

}