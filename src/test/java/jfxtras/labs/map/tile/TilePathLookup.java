package jfxtras.labs.map.tile;

import java.io.File;
import java.util.logging.Logger;

/**
 * This class searches for the path to the local tiles
 * @author schroeder
 *
 */
public class TilePathLookup {
	private static final Logger LOG = Logger.getLogger(TilePathLookup.class.getName());
	
	private TilePathLookup(){
	}

	public static String getPath() {
		String root = TilePathLookup.class.getResource(".").getFile();
        String propTiles = System.getProperty("tiles.source");
        if (propTiles != null && !propTiles.trim().isEmpty()) {
            root = propTiles;
        }
        File dir = new File(root, "tiles");
        String path = dir.getPath();
        LOG.info("Path for tiles: " + path);
        return path;
	}
}
