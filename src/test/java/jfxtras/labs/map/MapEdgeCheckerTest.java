/**
 * MapEdgeCheckerTest.java
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
