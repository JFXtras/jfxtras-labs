/**
 * MapPaneTest.java
 *
 * Copyright (c) 2011-2014, JFXtras
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

import java.awt.Point;

import jfxtras.labs.map.ZoomPoint.Direction;
import jfxtras.labs.map.tile.TileSource;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * 
 * @author Mario
 *
 */
public class MapPaneTest {

	private MapPane classUnderTest;
	
	private TileSource tileSource;
	
	@Before
	public void setUp() {
		tileSource = mock(TileSource.class);
		when(tileSource.getMaxZoom()).thenReturn(20);
		when(tileSource.getTileSize()).thenReturn(256);
		
		classUnderTest = new MapPane(tileSource);
	}
	
	@Test
	public void testZoomChange(){
		int expected = 12;
		classUnderTest.zoomProperty().set(expected);
		assertEquals(expected, classUnderTest.zoomProperty().get());
	}
	
	@Test
	public void testZoomPointChange(){
		int initial = classUnderTest.zoomProperty().get();
		ZoomPoint zp = new ZoomPoint(10,10);
		zp.setDirection(Direction.IN);
		
		classUnderTest.zoomPointProperty().set(zp);
		assertEquals(initial + 1, classUnderTest.zoomProperty().get());
	}
	
	@Test
	public void testMoveMap(){
		Point initial = classUnderTest.getCenter();
		classUnderTest.moveMap(5, 5);
		assertFalse(classUnderTest.getCenter().equals(initial));
	}
}
