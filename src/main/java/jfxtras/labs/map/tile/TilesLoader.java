/**
 * TilesLoader.java
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
 *     * Neither the name of the <organization> nor the
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
	
	private TileOrderComparator tileComparator = new TileOrderComparator();
	
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
