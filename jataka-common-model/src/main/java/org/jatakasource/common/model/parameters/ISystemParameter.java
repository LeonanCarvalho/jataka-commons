package org.jatakasource.common.model.parameters;

import org.jatakasource.common.model.IDomainObject;

/**
 * System Parameter is used to manage either a database parameter or .properties
 */
public interface ISystemParameter extends IDomainObject<String>{
	String getId();

	void setId(String id);

	String getValue();

	void setValue(String value);
}
