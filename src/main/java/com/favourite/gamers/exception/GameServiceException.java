package com.favourite.gamers.exception;

public class GameServiceException extends IllegalArgumentException {

	private static final long serialVersionUID = -470180507998010368L;

	public GameServiceException() {
		super();
	}

	public GameServiceException(final String message) {
		super(message);
	}
}
