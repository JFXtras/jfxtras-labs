package jfxtras.labs.map;

import java.awt.Dimension;
import java.awt.Point;

/**
 * This class provides method to convert between real coordinates and points on
 * the map window.
 * 
 * @author Mario Schroeder
 * 
 */
public class CoordinatesConverter {
	
	private CoordinatesConverter(){
	}

	private static final int START = 0;
	
	private static int getZoom(Zoomable zoomable){
		return zoomable.zoomProperty().get();
	}
	
	public static Coordinate toCoordinate(Point mapPoint, MapControlable controller) {
		Dimension dim = new Dimension(controller.getMapWidth(), controller.getMapHeight());
		return toCoordinate(mapPoint, controller.getCenter(), dim, getZoom(controller));
	}

	/**
	 * Converts the relative pixel coordinate (regarding the top left corner of
	 * the displayed map) into a latitude / longitude coordinate
	 * 
	 * @param mapPoint
	 * @param center
	 * @param dim
	 * @param zoom
	 * @return
	 */
	public static Coordinate toCoordinate(Point mapPoint, Point center,
			Dimension dim, int zoom) {
		int x = (int) (center.x + mapPoint.x - dim.getWidth() / 2);
		int y = (int) (center.y + mapPoint.y - dim.getHeight() / 2);
		double lon = Mercator.xToLon(x, zoom);
		double lat = Mercator.yToLat(y, zoom);
		return new Coordinate(lat, lon);
	}
	
	public static Point toMapPoint(Coordinate coordinate, MapControlable controller){
		Dimension dim = new Dimension(controller.getMapWidth(), controller.getMapHeight());
		return toMapPoint(coordinate, controller.getCenter(), dim, getZoom(controller));
	}

	/**
	 * Calculates the position on the map of a given coordinate.
	 * 
	 * @param coordinate
	 * @param center
	 * @param dim
	 * @param zoom
	 * @return
	 */
	public static Point toMapPoint(Coordinate coordinate, Point center, Dimension dim,
			int zoom) {
		int x = Mercator.lonToX(coordinate.getLongitude(), zoom);
		int y = Mercator.latToY(coordinate.getLatitude(), zoom);
		x -= center.x - dim.getWidth() / 2;
		y -= center.y - dim.getHeight() / 2;

		return new Point(x, y);
	}

	public static Point toMapPointWithCheckOutside(Coordinate coordinate, Point center,
			Dimension dim, int zoom) {
		Point p = toMapPoint(coordinate, center, dim, zoom);
		if (p.x < START || p.y < START || p.x > dim.getWidth()
				|| p.y > dim.getHeight()) {
			return null;
		}
		return p;
	}

}
