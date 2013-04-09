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
	public void testRemoteSource() {

		final TileSource[] tileSources = new TileSource[] {
			new MapnikOsmTileSource(), new CycleMapOsmTileSource(), 
            new TilePyramidSource(), new BingAerialTileSource()};
        
		for (TileSource tileSource : tileSources) {
			verify(tileSource);
		}
	}
    
	@Test
	public void testLocalSource() {
		verify(new TilePyramidSource(null));
		verify(new TilePyramidSource(getClass().getResource("test").getFile()));
	}
	
	@Test
	public void testWithIllegalArgs(){
		TileRepository classUnderTest = new TileRepository(new TilePyramidSource(null));
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
