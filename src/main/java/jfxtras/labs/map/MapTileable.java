package jfxtras.labs.map;

import javafx.scene.Group;
import jfxtras.labs.map.tile.TileSource;

/**
 * This interface provides method to deal with tiles of the map.
 * @author mario
 *
 */
public interface MapTileable extends CursorLocationable{
	
	Group getTilesGroup();

    TileSource getTileSource();

}
