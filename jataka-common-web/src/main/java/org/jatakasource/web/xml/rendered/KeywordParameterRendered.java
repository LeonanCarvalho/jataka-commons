package org.jatakasource.web.xml.rendered;

import org.simpleframework.xml.Element;

public class KeywordParameterRendered extends GridParametersRendered {
	private String keyword;

	@Element(name = "KEYWORD", data = true, required = false)
	public String getKeyword() {
		return keyword;
	}

	@Element(name = "KEYWORD", data = true, required = false)
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

}
