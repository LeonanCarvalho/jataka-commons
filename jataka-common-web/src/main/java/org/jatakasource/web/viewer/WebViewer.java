package org.jatakasource.web.viewer;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.view.AbstractView;

public abstract class WebViewer extends AbstractView {
	public static final String DATA_TO_SERIALIZED_KEY = "DATA_TO_SERIALIZED_KEY";
	public static final String DATA_TO_SERIALIZED_TYPE = "DATA_TO_SERIALIZED_TYPE";
	public static final String ERROR_TO_RETURN_KEY = "ERROR_TO_RETURN_KEY";

	protected ServletContext getServletContext(HttpServletRequest request) {
		return request.getSession().getServletContext();
	}

	protected String getRequestContextPath(HttpServletRequest request) {
		return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	}
}
