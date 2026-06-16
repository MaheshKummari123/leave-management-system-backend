package com.lms.exception;

public class UserNotFoundException extends RuntimeException{
	
	public UserNotFoundException(int id) {
		super("Could not found user with id "+id);
	}
}
