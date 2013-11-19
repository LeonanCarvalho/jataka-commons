package org.jatakasource.web.messages;

import org.apache.commons.lang.StringUtils;
import org.jatakasource.common.svc.ReturnMessageEnum;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "RETURN_MESSAGE")
public class ReturnMessage {

	@Attribute
	private final ReturnMessageEnum type;

	@Attribute(required = false)
	private final String relatedId;

	@Element(name = "TITLE", data = true, required = true)
	private final String title;

	@Element(name = "MESSAGE", data = true, required = true)
	private final String message;

	public ReturnMessage(ReturnMessageEnum type, String title, String message, String relatedId) {
		super();
		this.type = type;
		this.title = title;
		this.message = message == null ? StringUtils.EMPTY : message;
		this.relatedId = relatedId == null ? StringUtils.EMPTY : relatedId;
	}

	public ReturnMessage(ReturnMessageEnum type, String title, String message) {
		super();
		this.type = type;
		this.title = title;
		this.message = message == null ? StringUtils.EMPTY : message;
		this.relatedId = StringUtils.EMPTY;
	}

	public String getType() {
		return type.name();
	}

	public String getMessage() {
		return message;
	}

	public String getTitle() {
		return title;
	}

	public String getRelatedId() {
		return relatedId;
	}
}
