package org.jatakasource.web.viewer;

import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

@Viewer
public class ImageViewer extends WebViewer {
	private static final Logger logger = Logger.getLogger(ImageViewer.class);
	public static final String IMAGE_TYPE_KEY = "IMAGE_TYPE_KEY";

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// Type - png, jpeg ...
		String imageFormat = (String) model.get(IMAGE_TYPE_KEY);

		logger.debug("Srart rendering image Header [" + request.getRequestURI() + "]");
		setHeader(response, imageFormat);

		OutputStream out = response.getOutputStream();

		BufferedImage bufferedImage = (BufferedImage) model.get(DATA_TO_SERIALIZED_KEY);

		if (bufferedImage == null) {
			response.sendError(500, "Unable to get Image from server !!!");
		} else {
			ImageIO.write(bufferedImage, imageFormat, out);
			out.flush();
		}
	}

	protected void setHeader(HttpServletResponse response, String imageFormat) {
		response.setContentType("image/" + imageFormat + "; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0
		response.setDateHeader("Expires", 0); // prevents caching at the proxy
	}

}
