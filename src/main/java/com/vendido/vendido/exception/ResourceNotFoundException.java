package com.vendido.vendido.exception;

public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ResourceNotFoundException(final String resourceName, final String by, final Object key) {
		super(String.format("El recurso %s no fue encontrado por %s <%s>", resourceName, by, key));
	}

}
