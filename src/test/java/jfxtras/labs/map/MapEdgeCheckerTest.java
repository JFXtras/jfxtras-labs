package jfxtras.labs.map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.Dimension;
import java.awt.Point;

import jfxtras.labs.map.render.TileRenderable;

import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link MapEdgeChecker}
 * @author Mario Schroeder
 *
 */
public class MapEdgeCheckerTest {
	
	private TileRenderable tileRenderer;
	
	private MapEdgeChecker classUnderTest;
	
	private Dimension dim;

	@Before
	public void setUp(){
		tileRenderer = mock(TileRenderable.class);
		classUnderTest = new MapEdgeChecker(tileRenderer);
		dim = new Dimension(400,400);
	}

	@Test
	public void testIsEdgeInvisible() {
		
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
		
		Point[] bounds = new Point[]{new Point(1,1), new Point(410,410)};
		when(tileRenderer.getBounds()).thenReturn(bounds);
		assertTrue(classUnderTest.isAnyVisible(dim));
		
		bounds = new Point[]{new Point(1,1), new Point(399,399)};
		when(tileRenderer.getBounds()).thenReturn(bounds);
		assertTrue(classUnderTest.isAnyVisible(dim));
	}
	
	@Test
	public void testOnEdge(){
		Point[] bounds = new Point[]{new Point(250,250), new Point(300,300)};
		when(tileRenderer.getBounds()).thenReturn(bounds);
		assertFalse(classUnderTest.isOnRightEdge(dim));
		assertFalse(classUnderTest.isOnLeftEdge(dim));
		assertFalse(classUnderTest.isOnTopEdge(dim));
		assertFalse(classUnderTest.isOnBottomEdge(dim));
		
		bounds = new Point[]{new Point(390,390), new Point(410,410)};
		when(tileRenderer.getBounds()).thenReturn(bounds);
		assertTrue(classUnderTest.isOnRightEdge(dim));
		assertFalse(classUnderTest.isOnLeftEdge(dim));
		assertFalse(classUnderTest.isOnTopEdge(dim));
		assertTrue(classUnderTest.isOnBottomEdge(dim));
		
		bounds = new Point[]{new Point(-10,-10), new Point(10,10)};
		when(tileRenderer.getBounds()).thenReturn(bounds);
		assertTrue(classUnderTest.isOnLeftEdge(dim));
		assertFalse(classUnderTest.isOnRightEdge(dim));
		assertTrue(classUnderTest.isOnTopEdge(dim));
		assertFalse(classUnderTest.isOnBottomEdge(dim));
	}
}
