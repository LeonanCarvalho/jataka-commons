package org.jatakasource.web.exception;

public class XmlDesirializeException extends RuntimeException {
	private static final long serialVersionUID = 789883207348108225L;

	public XmlDesirializeException(Throwable cause) {
		super(cause);
	}

	public XmlDesirializeException(String message, Throwable cause) {
		super(message, cause);
	}
}
