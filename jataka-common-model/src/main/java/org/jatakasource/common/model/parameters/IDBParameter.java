package org.jatakasource.common.model.parameters;

import org.jatakasource.common.model.IDomainObject;

public interface IDBParameter extends IDomainObject<String>, ISystemParameter {

	String getDescription();

	void setDescription(String description);

	Boolean isUpdateble();

	void setUpdateble(Boolean updateble);

	String getValidatorClass();

	void setValidatorClass(String classNme);
}