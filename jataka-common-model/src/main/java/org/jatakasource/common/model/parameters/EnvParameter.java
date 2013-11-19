package org.jatakasource.common.model.parameters;

import org.jatakasource.common.model.BasePojo;

/**
 * Property Class is used to manage properties from a .properties file.
 */
public class EnvParameter extends BasePojo<String> implements ISystemParameter {
	private static final long serialVersionUID = 2625906231476916596L;

	private String id;
	private String value;

	public EnvParameter(String id, String value) {
		super();
		this.id = id;
		this.value = value;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public void setValue(String value) {
		this.value = value;
	}
}
