package org.jatakasource.geo.data;

import java.util.List;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.jatakasource.geo.esri.IShpFileReader;
import org.jatakasource.geo.esri.ShpFileReaderImpl;
import org.junit.Test;

public class LoadShpFileTest {
	private static final Logger logger = Logger.getLogger(LoadShpFileTest.class);

	@Test
	public void isEnabledForGrulesTest() {
		List<SimpleGeometry> geomentryList = null;

		try {
			IShpFileReader reader = new ShpFileReaderImpl();
			geomentryList = reader.getListOfGeometriesFrom(TestShpFiles.getShapeFileFor(TestShpFiles.WORLD_TEST), TestShpFiles.getIso88591Charset(), SimpleGeometry.getReader());

			Assert.assertEquals("Unable to read shape file!", geomentryList.size(), 63432);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}
}
