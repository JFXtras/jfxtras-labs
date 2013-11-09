/**
 * CoordinatesConverter.java
 *
 * Copyright (c) 2011-2013, JFXtras
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the <organization> nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

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
	
	public static Coordinate toCoordinate(Point mapPoint, Moveable controller) {
		Dimension dim = new Dimension(controller.getMapWidth(), controller.getMapHeight());
		Point center = controller.getCenter();
		int zoom = getZoom(controller);
		return toCoordinate(mapPoint, center, dim, zoom);
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
	
	public static Point toMapPoint(Coordinate coordinate, Moveable controller){
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
