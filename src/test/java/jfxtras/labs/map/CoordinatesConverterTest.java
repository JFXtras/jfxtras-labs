package jfxtras.labs.map;

import static org.junit.Assert.*;

import java.awt.Dimension;
import java.awt.Point;

import jfxtras.labs.map.tile.TileSourceFactory;
import jfxtras.labs.map.tile.local.LocalTileSourceFactory;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for the map pane.
 * @author Mario Schroeder
 *
 */
public class CoordinatesConverterTest {
	
	private static final double COOR_DIFF = 0.003;
	
	private static final double COOR_VAL = 1.0;
	
	private static final int ZOOM = 6;
	
	private Point center;
	
	private Dimension dim;
	
	@Before
	public void setUp(){
		center = new Point(0,0);
		dim = new Dimension(400, 400);
	}
	
	@Test
	public void testToCoordinate(){

		Coordinate coord = CoordinatesConverter.toCoordinate(center, center, dim, ZOOM);
		assertNotNull(coord);
		assertEquals(85.416, coord.getLatitude(), COOR_DIFF);
		assertEquals(-184.395, coord.getLongitude(), COOR_DIFF);
		
	}
	
	@Test
	public void testToMapPoint(){
		Coordinate coord = new Coordinate(0, 0);
		Point p = CoordinatesConverter.toMapPoint(coord, center, dim, ZOOM);
		assertNotNull(coord);
		assertEquals(8392, p.x, COOR_DIFF);
		assertEquals(8392, p.y, COOR_DIFF);

	}

}
