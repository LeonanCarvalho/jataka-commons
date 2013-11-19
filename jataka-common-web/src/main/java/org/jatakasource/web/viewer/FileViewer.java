package org.jatakasource.web.viewer;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Viewer
public class FileViewer extends WebViewer {
	public static final String FILE_TYPE_KEY = "FILE_TYPE_KEY";
	public static final String FILE_NAME_KEY = "FILE_NAME_KEY";

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// Write the data to the response.
		// To open csv file with excel by double click, the csv must be with
		// encoding "UTF-16LE", bom and tab as separator
		String fileName = (String) model.get(FILE_NAME_KEY);

		logger.trace("Srart rendering the file Header [" + request.getRequestURI() + "]");
		setHeader(response, fileName);

		OutputStream out = response.getOutputStream();

		File file = (File) model.get(DATA_TO_SERIALIZED_KEY);
		FileInputStream fin = new FileInputStream(file);
		byte fileContent[] = new byte[(int) file.length()];

		fin.read(fileContent);
		out.write(fileContent);
		out.flush();
	}

	protected void setHeader(HttpServletResponse response, String fileName) {
		response.setContentType("multipart/form-data; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0
		response.setHeader("Content-Disposition", "attachment; fileName=" + fileName);
		response.setDateHeader("Expires", 0); // prevents caching at the proxy
	}
}