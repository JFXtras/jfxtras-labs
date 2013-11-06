/**
 * TileRepositoryTest.java
 *
 * Copyright (c) 2011-2013, JFXtras All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. * Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. * Neither the name of the <organization> nor the names
 * of its contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jfxtras.labs.map.tile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;

import javafx.scene.image.ImageView;
import jfxtras.labs.JavaFXPlatformAbstractTest;
import jfxtras.labs.map.ApiKeys;
import jfxtras.labs.map.tile.bing.BingTileSourceFactory;
import jfxtras.labs.map.tile.bing.BingType;
import jfxtras.labs.map.tile.local.LocalTileSourceFactory;
import jfxtras.labs.map.tile.osm.OsmTileSourceFactory;
import jfxtras.labs.map.tile.osm.OsmType;
import org.junit.Ignore;

import org.junit.Test;

/**
 * Test for {@link TileRepository}.
 *
 * @author Mario Schroeder
 *
 */
public class TileRepositoryTest extends JavaFXPlatformAbstractTest {

    @Test
    public void testWithNull() {

        TileRepository classUnderTest = new TileRepository(null);
        Tile tile = classUnderTest.getTile(1, 1, 1);
        assertNull(tile);
    }

    @Test
    public void testOsmSource() {

        TileSourceFactory<OsmType> factory = new OsmTileSourceFactory();
        for (OsmType type : OsmType.values()) {
            verify(factory.create(type));
        }
    }

    @Test
    public void testBingSource() {
        TileSourceFactory<BingType> factory = new BingTileSourceFactory(ApiKeys.Bing.toString());
        for (BingType type : BingType.values()) {
            verify(factory.create(type));
        }
    }

    @Test
    @Ignore
    public void testLocalSource() {

        TileSourceFactory<String> factory = new LocalTileSourceFactory();
        TileRepository classUnderTest = new TileRepository(factory.create(null));
        Tile tile = classUnderTest.getTile(1, 1, 1);
        assertNotNull(tile);
        assertNull(tile.getImageView().getImage());

        String root = getClass().getResource(".").getFile();
        String propTiles = System.getProperty("tiles.source");
        if (propTiles != null && !propTiles.trim().isEmpty()) {
            root = propTiles;
        }
        File dir = new File(root, "tiles");
        TileSource tileSource = factory.create(dir.getPath());
        classUnderTest = new TileRepository(tileSource);
        tile = classUnderTest.getTile(65, 40, 7);
        assertNotNull(tile);
        assertNotNull(tile.getImageView().getImage());
        assertEquals(7, tileSource.getMinZoom());
        assertEquals(8, tileSource.getMaxZoom());
    }

    @Test
    public void testWithIllegalArgs() {

        TileSourceFactory<String> factory = new LocalTileSourceFactory();
        TileRepository classUnderTest = new TileRepository(factory.create());
        Tile tile = classUnderTest.getTile(-1, 0, 0);
        assertNull(tile);
        tile = classUnderTest.getTile(0, -1, 0);
        assertNull(tile);
    }

    private void verify(TileSource tileSource) {

        TileRepository classUnderTest = new TileRepository(tileSource);
        Tile tile = classUnderTest.getTile(1, 1, 1);
        assertNotNull(tile);
        assertEquals(tileSource, classUnderTest.getTileSource());
        ImageView imageView = tile.getImageView();
        assertNotNull(imageView);
        assertNotNull(imageView.getImage());
    }
}
