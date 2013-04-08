package jfxtras.labs.map.tile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import javafx.scene.image.ImageView;

import org.junit.Test;

/**
 * Test for {@link MapTile}.
 * 
 * @author Mario Schroeder
 * 
 */
public class MapTileTest {

	@Test
	public void testHttp() {

		TileSource[] tileSources = new TileSource[] {
				new OsmTileSource.Mapnik(), new OsmTileSource.CycleMap(),
				new TilePyramidSource(), new BingAerialTileSource() };
		for (TileSource tileSource : tileSources) {
			test(tileSource);
		}
	}

	@Test
	public void testUnknownLocalImage() {
		test(new LocalTileSource(null));
	}

	@Test
	public void testLocalImage() {
		test(new LocalTileSource(getClass().getResource("test").getFile()));
	}

	private void test(TileSource tileSource) {
		MapTile classUnderTest = new MapTile(tileSource, 1, 1, 1);
		assertEquals(tileSource, classUnderTest.getSource());
		ImageView imageView = classUnderTest.getImageView();
		assertNotNull(imageView);
		assertNotNull(imageView.getImage());
	}

	private class LocalTileSource extends AbstractTileSource {

		LocalTileSource(String base_url) {
			super("Test", base_url);

		}
	}
}
