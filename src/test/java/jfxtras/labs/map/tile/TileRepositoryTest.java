package jfxtras.labs.map.tile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import javafx.scene.image.ImageView;

import org.junit.Test;

/**
 * Test for {@link TileRepository}.
 * 
 * @author Mario Schroeder
 * 
 */
public class TileRepositoryTest {

	@Test
	public void testRemoteSource() {

		TileSource[] tileSources = new TileSource[] {
				new OsmTileSource.Mapnik(), new OsmTileSource.CycleMap(),
				new BingAerialTileSource() };
		for (TileSource tileSource : tileSources) {
			test(tileSource);
		}
	}

	@Test
	public void testLocalSource() {
		test(new TilePyramidSource(null));
		test(new TilePyramidSource(getClass().getResource("test").getFile()));
	}

	private void test(TileSource tileSource) {
		TileRepository classUnderTest = new TileRepository(tileSource);
		Tile tile = classUnderTest.getTile(1, 1, 1);
		assertNotNull(tile);
		assertEquals(tileSource, classUnderTest.getTileSource());
		ImageView imageView = tile.getImageView();
		assertNotNull(imageView);
		assertNotNull(imageView.getImage());
	}
	
}
