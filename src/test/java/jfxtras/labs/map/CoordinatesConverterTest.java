/**
 * CoordinatesConverterTest.java
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
	
	private static final int ZOOM = 6;
	
	private Point center;
	
	private Dimension dim;
	
	@Before
	public void setUp(){
		center = new Point(200,200);
		dim = new Dimension(400, 400);
	}
	
	@Test
	public void testToCoordinate(){

		Coordinate coord = CoordinatesConverter.toCoordinate(new Point(8192,8192), center, dim, ZOOM);
		assertNotNull(coord);
		assertEquals(-0.0, coord.getLatitude(), COOR_DIFF);
		assertEquals(0.0, coord.getLongitude(), COOR_DIFF);
	}
	
	@Test
	public void testToMapPoint(){
		Coordinate coord = new Coordinate(0, 0);
		Point p = CoordinatesConverter.toMapPoint(coord, center, dim, ZOOM);
		assertNotNull(p);
		assertEquals(8192, p.x, COOR_DIFF);
		assertEquals(8192, p.y, COOR_DIFF);

	}

}
