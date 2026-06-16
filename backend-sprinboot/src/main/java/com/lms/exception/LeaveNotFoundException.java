package com.lms.exception;

public class LeaveNotFoundException extends RuntimeException {
	
	public LeaveNotFoundException(int id) {
		super("Leave could not exists with id "+id);
	}
}
