package org.jatakasource.geo.data;

import java.util.LinkedList;
import java.util.List;

import org.geotools.feature.type.BasicFeatureTypes;
import org.opengis.feature.Feature;
import org.opengis.feature.Property;
import org.opengis.referencing.operation.MathTransform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.awt.PointShapeFactory.Square;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPolygon;

public final class SimpleGeometry extends AbstractGeometryContainer<SimpleGeometryData> {
	private static final Logger logger = LoggerFactory.getLogger(Square.class);

	private static final SimpleGeometryMaker SHAPE_FILE_READER = new SimpleGeometryMaker();

	private SimpleGeometry(Geometry geometry, SimpleGeometryData geometryData) {
		super(geometry, geometryData);
	}

	public static class SimpleGeometryMaker extends AbstractGeometryContainerReader<SimpleGeometry> {
		@Override
		public void read(Feature feature, MathTransform mathTransform, List<SimpleGeometry> destnation) {
			Integer id = null;
			List<Geometry> gList = new LinkedList<Geometry>();
			try {
				for (Property property : feature.getValue()) {
					String propertyName = property.getName().getLocalPart();
					if (BasicFeatureTypes.GEOMETRY_ATTRIBUTE_NAME.equals(propertyName)) {
						transformAndAddGeometries(mathTransform, gList, (MultiPolygon) property.getValue());
					} else if ("FID".equalsIgnoreCase(propertyName)) {
						id = Double.valueOf(property.getValue().toString()).intValue();
					}
				}
				if (id != null && !gList.isEmpty()) {
					for (Geometry g : gList) {
						SimpleGeometry sg = new SimpleGeometry(g, new SimpleGeometryData(id));
						destnation.add(sg);
					}
				}
			} catch (Exception e) {
				logger.error("SquareMaker.read failed Feature:" + feature + "  mathTransForm:" + mathTransform + "  destListSize:"
						+ (destnation == null ? null : destnation.size()));
				logger.debug("SquareMaker.read failed Feature:" + feature + "  mathTransForm:" + mathTransform + "  destListSize:"
						+ (destnation == null ? null : destnation.size()), e);
			}
		}
	}

	public static IGeometryContainerReader<SimpleGeometry> getReader() {
		return SHAPE_FILE_READER;
	}

}
