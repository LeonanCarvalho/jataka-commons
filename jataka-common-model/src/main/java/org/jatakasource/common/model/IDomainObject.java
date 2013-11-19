package org.jatakasource.common.model;

import java.io.Serializable;

/**
 * Base class to model objects
 * 
 * @author yaniv
 * @param <ID>
 */
public interface IDomainObject<ID extends Serializable> extends Serializable {
	public static final long serialVersionUID = -3603182179665343481L;

	public static final String FIELD_ID = "id";
	public static final String FIELD_DESC = "description";
	public static final String FIELD_NAME = "name";

	public static final int FIELD_TEXT_LENGTH = 50;
	public static final int FIELD_NAME_LENGTH = 100;
	public static final int FIELD_EMAIL_LENGTH = 50;
	public static final int FIELD_PHONE_LENGTH = 20;
	public static final int FIELD_URL_LENGTH = 200;
	public static final int FIELD_DESC_LENGTH = 200;
	public static final int FIELD_LOCALE_LENGTH = 5;

	ID getId();

	void setId(ID id);

	String[][] uniqueConstraints();
}