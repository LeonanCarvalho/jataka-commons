package org.jatakasource.web.controller;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jatakasource.common.svc.ReturnMessageEnum;
import org.jatakasource.common.utils.ListUtils;
import org.jatakasource.web.messages.ReturnMessage;
import org.jatakasource.web.viewer.WebViewer;
import org.jatakasource.web.viewer.XmlViewer;
import org.springframework.web.servlet.ModelAndView;

public abstract class WebControllerBean {
	protected static final String PUBLIC_SERVICE = "/public";
	protected static final String PRIVATE_SERVICE = "/private";
	protected static final String PROTECTED_SERVICE = "/protected";

	/**
	 * This method create ModelAndView instance.
	 * <p>
	 * in order to retrieve date from model use ModelAndView.getModel().get(DATA_TO_SERIALIZED);
	 * </p>
	 * 
	 * @param viewer
	 * @param model
	 * @return ModelAndView
	 */
	public ModelAndView getViewer(Class<?> viewer, Object model) {
		return getViewer(StringUtils.uncapitalize(viewer.getSimpleName()), model);
	}

	private ModelAndView getViewer(String viewer, Object model) {
		return new ModelAndView(viewer, WebViewer.DATA_TO_SERIALIZED_KEY, model);
	}

	protected ModelAndView getXMLViewer(Object model) {
		return new ModelAndView(StringUtils.uncapitalize(XmlViewer.class.getSimpleName()), WebViewer.DATA_TO_SERIALIZED_KEY, model);
	}

	/**
	 * <p>
	 * Convert string comma separator string to list of Strings.
	 * </p>
	 * 
	 * Supported formats:</br> [X, Y, Z]</br> X, Y, Z
	 */
	protected List<String> toList(String arrayString) {
		return ListUtils.toList(arrayString);
	}

	/**
	 * <p>
	 * Convert string comma separator string to list of enums.
	 * </p>
	 * 
	 * Supported formats:</br> [X, Y, Z]</br> X, Y, Z
	 */
	protected <T> List<T> toList(String arrayString, Class<T> enumType) {
		return ListUtils.toList(arrayString, enumType);
	}

	protected List<Long> toLongs(String arrayString) {
		return ListUtils.toLongs(arrayString);
	}

	protected ReturnMessage getError(String title, String message) {
		return new ReturnMessage(ReturnMessageEnum.ERROR, title, message);
	}

	protected ReturnMessage getError(String title, String message, String relatedId) {
		return new ReturnMessage(ReturnMessageEnum.ERROR, title, message, relatedId);
	}

	protected ReturnMessage getInfo(String title, String message) {
		return new ReturnMessage(ReturnMessageEnum.INFO, title, message);
	}

	protected ReturnMessage getInfo(String title, String message, String relatedId) {
		return new ReturnMessage(ReturnMessageEnum.INFO, title, message, relatedId);
	}

	protected ReturnMessage getWarning(String title, String message) {
		return new ReturnMessage(ReturnMessageEnum.WARNING, title, message);
	}

	protected ReturnMessage getWarning(String title, String message, String relatedId) {
		return new ReturnMessage(ReturnMessageEnum.WARNING, title, message, relatedId);
	}
}
