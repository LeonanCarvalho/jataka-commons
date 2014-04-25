package org.jatakasource.geo.data;

import com.vividsolutions.jts.geom.Geometry;

public abstract class AbstractGeometryContainer<T extends IGeometryData> implements IGeometryContainer<T> {
	private final T geometryData;
	private final Geometry geometry;

	protected AbstractGeometryContainer(Geometry geometry, T geometryData) {
		this.geometryData = geometryData;
		this.geometry = geometry;
	}

	public final T getGeometryData() {
		return geometryData;
	}

	public final Geometry getGeometry() {
		return geometry;
	}

}