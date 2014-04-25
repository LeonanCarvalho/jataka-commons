package org.jatakasource.geo.data;

import java.util.List;

import org.geotools.geometry.jts.JTS;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPolygon;

public abstract class AbstractGeometryContainerReader<T extends IGeometryContainer<? extends IGeometryData>> implements IGeometryContainerReader<T> {

	protected static void transformAndAddGeometries(MathTransform mathTransform, List<Geometry> destination, MultiPolygon multiPolygon) throws TransformException {
		int numGeometries = multiPolygon.getNumGeometries();
		for (int index = 0; index < numGeometries; index++) {
			destination.add(JTS.transform(multiPolygon.getGeometryN(index), mathTransform));
		}
	}
}