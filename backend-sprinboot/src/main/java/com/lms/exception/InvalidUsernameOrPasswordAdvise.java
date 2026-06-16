package com.lms.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class InvalidUsernameOrPasswordAdvise {
	
	@ExceptionHandler(InvalidUsernameOrPasswordException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public Map<String, String> errorHandling(InvalidUsernameOrPasswordException exception){
		Map<String, String > map=new HashMap<>();
		map.put("errorMessage", exception.getMessage());
		return map;
		
	}
}
