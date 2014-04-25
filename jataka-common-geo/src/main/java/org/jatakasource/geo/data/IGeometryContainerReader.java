package org.jatakasource.geo.data;

import java.util.List;

import org.opengis.feature.Feature;
import org.opengis.referencing.operation.MathTransform;

/**
 * strategy interface for reading geometries and attributes from data store (shape file) features 
 * 
 * @param <T> the type of Geometry Object and Wrapper that this reader produces 
 */
public interface IGeometryContainerReader<T extends IGeometryContainer<? extends IGeometryData>> {
	
	/**
	 * takes a feature line from a shape file data store and convert it to the geometry objects used by the system.  
	 * @param feature - the line of the data source from which the data is extracted 
	 * @param mathTransform - to convert the input CRS to the CRS used by the system  
	 * @param destnation - the list to add the resulting object in to. 
	 */
	void read(Feature feature, MathTransform mathTransform, List<T> destnation);
}
