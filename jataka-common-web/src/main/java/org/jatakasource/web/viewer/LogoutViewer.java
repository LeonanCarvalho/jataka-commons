package org.jatakasource.web.viewer;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Viewer
public class LogoutViewer extends HtmlViewer {
    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        final String dispatchTo = "/j_spring_security_logout";
        request.getRequestDispatcher(dispatchTo).forward(request, response);
        super.renderMergedOutputModel(model, request, response);
    }
}
