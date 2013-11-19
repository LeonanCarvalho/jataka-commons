package org.jatakasource.common.model;

import java.io.Serializable;

public abstract class BasePojo<ID extends Serializable> implements IDomainObject<ID> {

	private static final long serialVersionUID = 587720558535300081L;

	/**
	 * Method not required in pojo level.
	 */
	@Override
	public String[][] uniqueConstraints() {
		return null;
	}
}
