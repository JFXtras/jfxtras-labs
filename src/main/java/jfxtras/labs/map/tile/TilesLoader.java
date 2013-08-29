package jfxtras.labs.map.tile;

import static java.util.Collections.sort;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import jfxtras.labs.map.Moveable;

/**
 * This is the default implementation of the tile loader.
 * @author Mario Schroeder
 *
 */
public class TilesLoader implements TilesLoadable {
	
	private static final int START = 0;
	
	private TileImageComparator tileComparator = new TileImageComparator();
	
	private static final Point[] directions = { new Point(1, 0),
		new Point(0, 1), new Point(-1, 0), new Point(0, -1) };

	private final TileProvideable provider;
	
	public TilesLoader(TileProvideable provider) {
		this.provider = provider;
	}

	/**
	 * Load the tiles in a spiral, starting from center of the map.
	 */
	@Override
	public List<TileImage> loadTiles(Moveable mapController) {
		List<TileImage> tiles = new ArrayList<>();

		int tilesize = getTileSize();
		int iMove = 2;
		Point center = mapController.getCenter();

		int zoom = mapController.zoomProperty().get();

		int x_max = mapController.getMapWidth();
		int y_max = mapController.getMapHeight();

		int x_min = -tilesize, y_min = -tilesize;

		int posx = getInitialPositionX(tilesize, center, x_max);
		int posy = getInitialPositionY(tilesize, center, y_max);

		int tilex = (center.x / tilesize);
		int tiley = (center.y / tilesize);

		boolean added = true;
		int x = START;
		while (added) {
			added = false;
			for (int i = START; i < 4; i++) {
				if (i % 2 == START) {
					x++;
				}
				for (int j = START; j < x; j++) {
					if (x_min <= posx && posx <= x_max && y_min <= posy
							&& posy <= y_max) {
						// tile is visible
						Tile tile = getTile(zoom, tilex, tiley);
						if (tile != null) {
							added = true;
							TileImage tileImage = new TileImage(
									tile.getImageView(), posx, posy);
							tileImage.setTileX(tilex);
							tileImage.setTileY(tiley);
							tiles.add(tileImage);
						}
					}
					Point next = directions[iMove];
					posx += next.x * tilesize;
					posy += next.y * tilesize;
					tilex += next.x;
					tiley += next.y;
				}
				iMove = (iMove + 1) % directions.length;
			}
		}

		sort(tiles, tileComparator);
		return tiles;
	}
	
	private Tile getTile(int zoom, int tilex, int tiley) {
		return provider.getTile(tilex, tiley, zoom);
	}
	
	private int getTileSize() {
		return provider.getTileSource().getTileSize();
	}

	private int getInitialPositionY(int tilesize, Point center, int y_max) {
		int diff_top = (center.y % tilesize);
		return (y_max / 2) - diff_top;
	}

	private int getInitialPositionX(int tilesize, Point center, int x_max) {
		int diff_left = (center.x % tilesize);
		return (x_max / 2) - diff_left;
	}

}
