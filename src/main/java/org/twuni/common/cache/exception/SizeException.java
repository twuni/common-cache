package org.twuni.common.cache.exception;

public class SizeException extends IllegalArgumentException {

	private static final long serialVersionUID = 1L;
	private static final String MESSAGE = "%d exceeds limit of %d.";

	public SizeException( int attemptedSize, int capacity ) {
		super( String.format( MESSAGE, attemptedSize, capacity ) );
	}

}