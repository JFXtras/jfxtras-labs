package jfxtras.labs.map;

import static org.junit.Assert.*;

import java.awt.Point;

import jfxtras.labs.map.tile.LocalTileSourceFactory;
import jfxtras.labs.map.tile.TileSourceFactory;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for the map pane.
 * @author Mario Schröder
 *
 */
public class MapPaneTest {
	
	private static final double COOR_DIFF = 0.003;
	
	private static final double COOR_VAL = 1.0;
	
	private MapPane classUnderTest;

	@Before
	public void setUp() throws Exception {
		TileSourceFactory<String> factory = new LocalTileSourceFactory();
		classUnderTest = new MapPane(factory.create());
		classUnderTest.setDisplayPositionByLatLon(COOR_VAL, COOR_VAL);
	}

	@Test
	public void testCenterCoordinate() {
		Coordinate coord = classUnderTest.getCenterCoordinate();
		assertNotNull(coord);
		assertEquals(COOR_VAL, coord.getLatitude(), COOR_DIFF);
		assertEquals(COOR_VAL, coord.getLongitude(), COOR_DIFF);
	}
	
	@Test
	public void testGetCoordinate(){
		classUnderTest.setDisplayPositionByLatLon(32.81729, -117.215905);
		int x = classUnderTest.getMapX();
		int y = classUnderTest.getMapY();
		assertEquals(0, x);
		assertEquals(0, y);
		
		Point p = new Point(x,y);
		Coordinate coord = classUnderTest.getCoordinate(p);
		assertNotNull(coord);
		assertEquals(33.28, coord.getLatitude(), COOR_DIFF);
		assertEquals(-117.767, coord.getLongitude(), COOR_DIFF);
		
		int width = classUnderTest.getMapWidth();
		int height = classUnderTest.getMapHeight();
		assertEquals(400, width);
		assertEquals(400, height);
		
		p = new Point(width, height);
		coord = classUnderTest.getCoordinate(p);
		assertNotNull(coord);
		assertEquals(32.3568, coord.getLatitude(), COOR_DIFF);
		assertEquals(-116.6693, coord.getLongitude(), COOR_DIFF);
	}
	
	@Test
	public void testGetMapPoint(){
		Coordinate coord = new Coordinate(1.549, 0.453);
		Point p = classUnderTest.getMapPoint(coord);
		assertNotNull(coord);
		assertEquals(0.0, p.x, COOR_DIFF);
		assertEquals(0.0, p.y, COOR_DIFF);
		
		coord = new Coordinate(COOR_VAL, COOR_VAL);
		p = classUnderTest.getMapPoint(coord);
		assertNotNull(coord);
		assertEquals(200.0, p.x, COOR_DIFF);
		assertEquals(200.0, p.y, COOR_DIFF);
	}

}
