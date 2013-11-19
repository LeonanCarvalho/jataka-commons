package org.jatakasource.common.svc.exception;

import org.jatakasource.common.svc.ReturnMessageEnum;

public class ServiceException extends Exception {
	private static final long serialVersionUID = -1815974593420338687L;

	private ReturnMessageEnum type;

	public ServiceException(ReturnMessageEnum type, String message) {
		super(message);

		this.type = type;
	}

	public ReturnMessageEnum getType() {
		return type;
	}

	public void setType(ReturnMessageEnum type) {
		this.type = type;
	}
}
