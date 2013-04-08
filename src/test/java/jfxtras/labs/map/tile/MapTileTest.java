package jfxtras.labs.map.tile;

import static org.junit.Assert.*;
import javafx.scene.image.ImageView;

import org.junit.Test;


/**
 * Test for {@link MapTile}.
 * @author Mario Schroeder
 *
 */
public class MapTileTest {
	
	@Test
	public void testUnknownLocalImage() {
		TileSource tileSource = new LocalTileSource(null);
		MapTile classUnderTest = new MapTile(tileSource, 1, 1, 1);
		ImageView imageView = classUnderTest.getImageView();
		assertNotNull(imageView);
		assertNotNull(imageView.getImage());
	}
	
	@Test
	public void testLocalImage() {
		TileSource tileSource = new LocalTileSource(getClass().getResource("test").getFile());
		MapTile classUnderTest = new MapTile(tileSource, 1, 1, 1);
		ImageView imageView = classUnderTest.getImageView();
		assertNotNull(imageView);
		assertNotNull(imageView.getImage());
	}
	
}
