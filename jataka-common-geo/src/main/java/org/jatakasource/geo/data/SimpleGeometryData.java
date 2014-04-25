package org.jatakasource.geo.data;

public class SimpleGeometryData implements IGeometryData {

	private final int id;

	public SimpleGeometryData(int id) {
		this.id = id;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "{id:" + id + "}";
	}

}
