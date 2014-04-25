package org.jatakasource.geo.data;

import com.vividsolutions.jts.geom.Geometry;

public interface IGeometryContainer<T extends IGeometryData> {

	T getGeometryData();

	Geometry getGeometry();
}
