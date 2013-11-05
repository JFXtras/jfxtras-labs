/**
 * MapTest.java
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
 *     * Neither the name of the organization nor the
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import javafx.scene.Node;
import javafx.scene.control.Button;
import jfxtras.labs.AbstractJemmyTest;
import jfxtras.labs.map.tile.TileSource;
import jfxtras.labs.map.tile.TileSourceFactory;
import jfxtras.labs.map.tile.local.LocalTileSourceFactory;

import org.jemmy.Point;
import org.jemmy.fx.TextDock;
import org.jemmy.fx.control.ControlDock;
import org.jemmy.fx.control.SliderDock;
import org.junit.Test;

/**
 * Map Test.
 * @author Mario Schroeder
 *
 */
public class MapTest extends AbstractJemmyTest {
		
	private ControlDock getZoomInButton() {
		return new ControlDock(getSceneDock().asParent(), Button.class, 0);
	}
	
	private ControlDock getZoomOutButton() {
		return new ControlDock(getSceneDock().asParent(), Button.class, 1);
	}
	
	private SliderDock getSlider() {
		return new SliderDock(getSceneDock().asParent());
	}

	@Test
	public void testZoom() {
		
		
		assertEquals(7, getSlider().getValue(), 0.0);
		assertFalse((Boolean)getZoomInButton().isDisable());
		assertTrue((Boolean)getZoomOutButton().isDisable());
		
        getZoomInButton().mouse().click();

        assertEquals(8, getSlider().getValue(), 0.0);
        assertTrue((Boolean)getZoomInButton().isDisable());
        assertFalse((Boolean)getZoomOutButton().isDisable());
        
        getZoomOutButton().mouse().click();
        
        assertEquals(7, getSlider().getValue(), 0.0);
		assertFalse((Boolean)getZoomInButton().isDisable());
		assertTrue((Boolean)getZoomOutButton().isDisable());
		
	}
	
	@Test
	public void testMove() throws InterruptedException {
		
		getSceneDock().mouse().move(new Point(50,50));
		TextDock text = new TextDock(getSceneDock().asParent());
		String txtBefore = text.getText();
		
		getSceneDock().drag().dnd(new Point(100,50));
		
		String txtAfter = text.getText();
		assertFalse("The coordinates should be different.", txtBefore.equals(txtAfter));
	}

	
	@Override
	protected Node createTestContent() {

		TileSourceFactory<String> factory = new LocalTileSourceFactory();

		String rootDir = getClass().getResource("tile").getFile();
		String propTiles = System.getProperty("tiles.source");
		if (propTiles != null && !propTiles.trim().isEmpty()) {
			rootDir = propTiles;
		}
		File dir = new File(rootDir, "tiles");
		TileSource tileSource = factory.create(dir.getPath());
		MapPane map = new MapPane(tileSource, 400, 400, 7);
		ZoomControlFactory zoomControlFactory = new ZoomControlFactory();
        map.getChildren().add(zoomControlFactory.create(map));

		map.setDisplayPositionByLatLon(52.4, 5.9);
		
		return map;
	}
}
