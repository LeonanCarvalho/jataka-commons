package org.jatakasource.common.dao;

public class UniqueConstraintsException extends RuntimeException {

	private static final long serialVersionUID = -3769143282901246659L;

	private String key;

	public UniqueConstraintsException(String key, String message) {
		super(message);
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
