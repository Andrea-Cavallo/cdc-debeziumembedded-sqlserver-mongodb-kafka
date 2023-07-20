package com.demo.exception;

public class OutboxNotFoundException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OutboxNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
}
