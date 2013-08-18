package jfxtras.labs.map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.Dimension;
import java.awt.Point;

import jfxtras.labs.map.render.TileRenderable;

import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link MapEdgeVisibilityChecker}
 * @author Mario Schroeder
 *
 */
public class MapEdgeVisibilityCheckerTest {
	
	private TileRenderable tileRenderer;
	
	private MapEdgeVisibilityChecker classUnderTest;

	@Before
	public void setUp(){
		tileRenderer = mock(TileRenderable.class);
		classUnderTest = new MapEdgeVisibilityChecker(tileRenderer);
	}

	@Test
	public void testIsEdgeInvisible() {
		Dimension dim = new Dimension(400,400);
		assertFalse(classUnderTest.isAnyVisible(dim));
		
		Point[] bounds = new Point[]{new Point(-10,-10), new Point(410,410)};
		when(tileRenderer.getBounds()).thenReturn(bounds);
		assertFalse(classUnderTest.isAnyVisible(dim));
		
		bounds = new Point[]{new Point(0,0), new Point(410,410)};
		when(tileRenderer.getBounds()).thenReturn(bounds);
		assertFalse(classUnderTest.isAnyVisible(dim));
	}

	@Test
	public void testIsEdgeVisible() {
		Dimension dim = new Dimension(400,400);
		
		Point[] bounds = new Point[]{new Point(1,1), new Point(410,410)};
		when(tileRenderer.getBounds()).thenReturn(bounds);
		assertTrue(classUnderTest.isAnyVisible(dim));
		
		bounds = new Point[]{new Point(1,1), new Point(399,399)};
		when(tileRenderer.getBounds()).thenReturn(bounds);
		assertTrue(classUnderTest.isAnyVisible(dim));
	}
}
