package com.vendido.vendido.exception;

public class FaildPasswordException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public FaildPasswordException(final String faild) {
		super(faild);
	}

}
