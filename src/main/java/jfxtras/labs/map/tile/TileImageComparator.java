package jfxtras.labs.map.tile;

import java.util.Comparator;

/**
 * Compares tiles based on tileX & tileY fields.
 * @author Mario Schroeder
 *
 */
public class TileImageComparator implements Comparator<TileImage> {

	@Override
	public int compare(TileImage left, TileImage right) {
		return (left.getTileX() < right.getTileY()) ? -1
				: (left.getTileX() > right.getTileY()) ? +1
						: (left.getTileY() < right.getTileY()) ? -1
								: (left.getTileY() > right.getTileY()) ? +1 : 0;
	}

}
