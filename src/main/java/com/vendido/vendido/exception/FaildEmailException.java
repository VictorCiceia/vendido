package com.vendido.vendido.exception;

public class FaildEmailException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public FaildEmailException(final String faild) {
		super(faild);
	}

}
