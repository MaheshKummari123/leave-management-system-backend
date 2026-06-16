package com.lms.exception;

public class InvalidUsernameOrPasswordException extends RuntimeException{
	public InvalidUsernameOrPasswordException() {
		super("Invalid Username or Password");
	}
}
