package org.jatakasource.geo.esri;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

import org.jatakasource.geo.data.IGeometryData;
import org.jatakasource.geo.data.IGeometryContainer;
import org.jatakasource.geo.data.IGeometryContainerReader;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;

public interface IShpFileReader {
	public <T extends IGeometryContainer<? extends IGeometryData>> List<T> getListOfGeometriesFrom(File file, Charset charset, IGeometryContainerReader<T> reader) 
			throws MalformedURLException, IOException, FactoryException, 
			MismatchedDimensionException, TransformException, Exception;
	
	public <T extends IGeometryContainer<? extends IGeometryData>> List<T> getListOfGeometriesFrom(URL url, Charset charset, IGeometryContainerReader<T> reader) 
			throws MalformedURLException, IOException, FactoryException, 
			MismatchedDimensionException, TransformException, Exception;
}
