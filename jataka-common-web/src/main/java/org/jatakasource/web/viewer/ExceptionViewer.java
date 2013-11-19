package org.jatakasource.web.viewer;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.jatakasource.common.svc.exception.ServiceException;
import org.jatakasource.common.svc.exception.ServiceRunTimeException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

public abstract class ExceptionViewer extends XmlViewer {
	private final static Logger logger = Logger.getLogger(ExceptionViewer.class);

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Exception ex = (Exception) model.get(SimpleMappingExceptionResolver.DEFAULT_EXCEPTION_ATTRIBUTE);

		if (ex != null) {
			handleException(model, ex, response);
		}

		super.renderMergedOutputModel(model, request, response);
	}

	protected void handleException(Map<String, Object> model, Exception exception, HttpServletResponse response) {
		if (ServiceRunTimeException.class.isAssignableFrom(exception.getClass())) {
			handleException((ServiceRunTimeException)exception, model);
			
			response.setStatus(HttpStatus.SC_OK);
		} else if (ServiceException.class.isAssignableFrom(exception.getClass())) {
			handleException((ServiceException)exception, model);
			
			response.setStatus(HttpStatus.SC_OK);
		}else{
			logMessage(exception, response);
		}
	}

	private void logMessage(Exception ex, HttpServletResponse response) {
		// Just info, don't print stack trace
		if (BadCredentialsException.class.isAssignableFrom(ex.getClass())) {
			logger.error("Unhandled Exception -> " + ex.getMessage());
			response.setStatus(HttpStatus.SC_FORBIDDEN);
		} else if (AccessDeniedException.class.isAssignableFrom(ex.getClass())) {
			logger.error("Unhandled Exception -> " + ex.getMessage());
			// Status code (403) indicating the server understood the request but refused to fulfill it
			response.setStatus(HttpStatus.SC_FORBIDDEN);
		} else {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("Unhandled Exception -> " + ex.getMessage(), ex);
		}
	}
	
	protected abstract void handleException(ServiceRunTimeException exception, Map<String, Object> model);
	protected abstract void handleException(ServiceException exception, Map<String, Object> model);
}
