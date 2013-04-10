package jfxtras.labs.map.tile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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
	public void testOsmSource() {

		TileSourceFactory factory = new OsmTileSourceFactory();
		for (OsmType type : OsmType.values()) {
			verify(factory.create(type.name()));
		}
	}

	@Test
	public void testBingSource() {

		TileSourceFactory factory = new BingTileSourceFactory();
		verify(factory.create(null));
	}

	@Test
	public void testLocalSource() {
		TileSourceFactory factory = new LocalTileSourceFactory();
		verify(factory.create(null));
		verify(factory.create(getClass().getResource("test").getFile()));
	}

	@Test
	public void testWithIllegalArgs() {
		TileSourceFactory factory = new LocalTileSourceFactory();
		TileRepository classUnderTest = new TileRepository(factory.create(null));
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
