package org.jatakasource.geo.data;

import java.net.URL;
import java.nio.charset.Charset;

import org.geotools.data.shapefile.files.ShpFileType;
import org.junit.Ignore;

// Ignore this class from test
@Ignore
public enum TestShpFiles {
	WORLD_TEST("simplified_land_polygons");

	private static final String SHAPE_FILES_DIR = "/org/jatakasource/geo/data/";
	private static final Charset UTF8_CHARSET = Charset.forName("UTF-8");
	private static final Charset ISO_8859_1_CHARSET = Charset.forName("ISO-8859-1");
	
	private final String fileName;

	private TestShpFiles(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return this.fileName;
	}

	/**
	 * @param shapeFile
	 * @return {@value #SHAPE_FILES_DIR} +
	 *         {@code shapeFile.getFileName() + ".shp" }
	 */
	public static URL getShapeFileFor(TestShpFiles shapeFile) {
		URL url =  TestShpFiles.class.getResource(SHAPE_FILES_DIR + shapeFile.getFileName() + ShpFileType.SHP.extensionWithPeriod);
		return url;
	}

	/**
	 * @return the same UTF-8 immutable {@link Charset} object every time
	 */
	public static Charset getUTF8CharSet() {
		return UTF8_CHARSET;
	}

	public static Charset getIso88591Charset() {
		return ISO_8859_1_CHARSET;
	}

}
