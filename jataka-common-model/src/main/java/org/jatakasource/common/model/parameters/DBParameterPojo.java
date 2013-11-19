package org.jatakasource.common.model.parameters;

import org.jatakasource.common.model.BasePojo;

public class DBParameterPojo extends BasePojo<String> implements IDBParameter {
	private static final long serialVersionUID = -6918922609101792229L;

	private String id;
	private String value;
	private String validatorClass;
	private String description;
	private Boolean updateble;

	public DBParameterPojo() {

	}

	public DBParameterPojo(String id, String value, String description) {
		super();
		this.id = id;
		this.value = value;
		this.description = description;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public Boolean isUpdateble() {
		return updateble;
	}

	@Override
	public void setUpdateble(Boolean updateble) {
		this.updateble = updateble;
	}

	@Override
	public String getValidatorClass() {
		return validatorClass;
	}

	@Override
	public void setValidatorClass(String validatorClass) {
		this.validatorClass = validatorClass;
	}
}
