package org.jatakasource.geo.esri;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.geotools.data.FileDataStore;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.FeatureIterator;
import org.geotools.referencing.CRS;
import org.jatakasource.geo.data.IGeometryData;
import org.jatakasource.geo.data.IGeometryContainer;
import org.jatakasource.geo.data.IGeometryContainerReader;
import org.opengis.feature.Feature;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;


public class ShpFileReaderImpl implements IShpFileReader {
	private static final Logger logger = Logger.getLogger(ShpFileReaderImpl.class);
	
	private static final String EPSG_4326 = "EPSG:4326";
	private final String crsEPSGstr;
	private final CoordinateReferenceSystem systemCRS;
	
	public ShpFileReaderImpl() throws NoSuchAuthorityCodeException, FactoryException {
		this(EPSG_4326);
	}
	
	public ShpFileReaderImpl(String crsEPSGstr) throws NoSuchAuthorityCodeException, FactoryException {
		this.crsEPSGstr = crsEPSGstr;
		this.systemCRS = CRS.decode(this.crsEPSGstr);
	}
	
	@Override
	public <T extends IGeometryContainer<? extends IGeometryData>> List<T> getListOfGeometriesFrom(File file, Charset charset, IGeometryContainerReader<T> reader) 
			throws MalformedURLException, IOException, FactoryException, 
			MismatchedDimensionException, TransformException, Exception {

		return getListOfGeometriesFrom(file.toURI().toURL(), charset, reader);
	}
	
	
	@Override
	public <T extends IGeometryContainer<? extends IGeometryData>> List<T> getListOfGeometriesFrom(URL url, Charset charset, IGeometryContainerReader<T> reader) 
			throws MalformedURLException, IOException, FactoryException, 
			MismatchedDimensionException, TransformException, Exception {
		List<T> gList = new LinkedList<T>();
		ShapefileDataStore store = new ShapefileDataStore(url);
		
		// TODO - Find out why getShapeFileCRS(store) return null.
		// This work around prevent using store CRS. 
		store.forceSchemaCRS(getSystemCRS());
		
		FeatureIterator<? extends Feature> featureIterator;
		featureIterator = store.getFeatureSource().getFeatures().features();
		
		MathTransform mathTransform = getMathTransformFor(getShapeFileCRS(store), getSystemCRS());

		while (featureIterator.hasNext()) {
			reader.read(featureIterator.next(), mathTransform, gList);
		}
		
		return gList;
	}
	
	/**
	 * @param sourceCRS
	 * @param targetCRS
	 * @return MathTransform for the provided CRSs if one exists or null
	 */
	private MathTransform getMathTransformFor(CoordinateReferenceSystem sourceCRS, CoordinateReferenceSystem targetCRS) 
			throws FactoryException , MismatchedDimensionException , TransformException, Exception{
		MathTransform transform = null;
		try {
			transform = CRS.findMathTransform(sourceCRS, targetCRS, true);
		} catch (Exception e) {
			// catches FactoryException , MismatchedDimensionException , TransformException
			logger.error("unable to find Math Transform. between  sourceCRS:" + sourceCRS + " and targetCRS:" + targetCRS + ". errorMessage: " + e.getMessage());
			logger.debug("unable to find Math Transform. between  sourceCRS:" + sourceCRS + " and targetCRS:" + targetCRS + ". errorMessage: " + e.getMessage(), e);
			throw e;
		}
		return transform;
	}
	
	private CoordinateReferenceSystem getShapeFileCRS(FileDataStore store) {
		CoordinateReferenceSystem declaredShapeFileCrs = null;
		try {
			declaredShapeFileCrs = store.getSchema().getCoordinateReferenceSystem();
		} catch (Exception e) {
			logger.error("could not aquire shapeFileCRS from store " + store + ". error message " + e.getMessage());
			logger.debug("could not aquire shapeFileCRS from store " + store + ". error message " + e.getMessage(),e);
		} 

		return declaredShapeFileCrs;
	}
	
	private CoordinateReferenceSystem getSystemCRS() {
		return systemCRS;
	}
}
