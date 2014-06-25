package org.jatakasource.web.viewer;

import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Viewer
public class XmlViewer extends WebViewer {
	private static final Logger log = LoggerFactory.getLogger(XmlViewer.class);

	private final Serializer serializer = new Persister();

	protected void beforeRenderXMLResponse(Map<String, Object> model, PrintWriter out) throws Exception {
		// Override in case you want to write XML before RenderXMLResponse.
	}

	protected void afterRenderXMLResponse(Map<String, Object> model, PrintWriter out) throws Exception {
		// Override in case you want to write XML after RenderXMLResponse.
	}

	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.trace("Start rendering XML Header [" + request.getRequestURI() + "]");
		setHeader(request, response);

		PrintWriter out = response.getWriter();
		out.println("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
		beforeRenderXMLResponse(model, out);

		renderXMLResponse(model, out);

		afterRenderXMLResponse(model, out);
		log.trace("End Rendering XML Response [" + request.getRequestURI() + "]");
	}

	protected void setHeader(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0
		response.setDateHeader("Expires", 0); // prevents caching at the proxy
	}

	protected void renderXMLResponse(Map<String, Object> model, PrintWriter out) throws Exception {
		Object modelObj = model.get(DATA_TO_SERIALIZED_KEY);
		if (modelObj != null)
			serializer.write(model.get(DATA_TO_SERIALIZED_KEY), out);
	}
}
