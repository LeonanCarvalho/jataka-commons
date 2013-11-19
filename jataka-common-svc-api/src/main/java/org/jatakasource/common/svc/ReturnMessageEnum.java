package org.jatakasource.common.svc;

public enum ReturnMessageEnum {
	ERROR, WARNING, INFO, CRITICAL;

	private Integer type;

	private ReturnMessageEnum() {

	}

	public Integer getType() {
		return type;
	}
}
