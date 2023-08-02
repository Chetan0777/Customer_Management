package com.customer.exception;

import java.time.LocalDate;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;


@ControllerAdvice
public class globalExceptionHandler {

	
	@ExceptionHandler(CustomerException.class)
	public ResponseEntity<myErrorDetails> userExceptionHandler(CustomerException e, WebRequest wr){
		
		myErrorDetails err=new myErrorDetails();
		err.setLocalDate(LocalDate.now());
		err.setName(e.getMessage());
		err.setDesc(wr.getDescription(false));
		
		return new ResponseEntity<myErrorDetails>(err, HttpStatus.BAD_REQUEST);
		
	}
	
	@ExceptionHandler(LoginException.class)
	public ResponseEntity<myErrorDetails> userExceptionHandler(LoginException e, WebRequest wr){
		
		myErrorDetails err=new myErrorDetails();
		err.setLocalDate(LocalDate.now());
		err.setName(e.getMessage());
		err.setDesc(wr.getDescription(false));
		
		return new ResponseEntity<myErrorDetails>(err, HttpStatus.BAD_REQUEST);
		
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<myErrorDetails> masterExceptionHandler(Exception e, WebRequest wr){
		
		myErrorDetails err=new myErrorDetails();
		err.setLocalDate(LocalDate.now());
		err.setName(e.getMessage());
		err.setDesc(wr.getDescription(false));
		
		return new ResponseEntity<myErrorDetails>(err, HttpStatus.BAD_REQUEST);
		
	}

}
