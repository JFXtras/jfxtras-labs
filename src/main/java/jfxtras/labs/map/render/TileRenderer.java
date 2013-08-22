package jfxtras.labs.map.render;

import java.awt.Point;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

import static java.util.Collections.min;
import static java.util.Collections.max;
import static java.util.Collections.sort;
import static java.util.Collections.emptyList;
import javafx.scene.Group;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import jfxtras.labs.map.Moveable;
import jfxtras.labs.map.tile.DefaultTilesLoader;
import jfxtras.labs.map.tile.Tile;
import jfxtras.labs.map.tile.TilesProvideable;
import jfxtras.labs.map.tile.TileImageComparator;
import jfxtras.labs.map.tile.TileImage;
import jfxtras.labs.map.tile.TilesLoadable;
import jfxtras.labs.map.tile.TileSource;

/**
 * Rendered for map tiles.
 * 
 * @author Mario Schroeder
 */
public class TileRenderer implements TileRenderable {

	/**
	 * stroke width
	 */
	private static final double WIDTH = 0.4;
	
	private TileImageComparator tileComparator = new TileImageComparator();

	private boolean monoChrome;

	private boolean tileGridVisible;

	private List<TileImage> tileImages;
	
	private final TilesProvideable tileCache;


	public TileRenderer(TilesProvideable tileCache) {
		this.tileCache = tileCache;
		this.tileImages = emptyList();
	}

	@Override
	public int prepareTiles(Moveable mapController) {

		tileImages = loadTiles(mapController);
		return tileImages.size();
	}

	@Override
	public Point[] getBounds() {
		Point[] bounds = new Point[0];
		if (!tileImages.isEmpty()) {
			TileImage min = min(tileImages, tileComparator);
			TileImage max = max(tileImages, tileComparator);

			int tilesize = getTileSize();
			bounds = new Point[2];
			bounds[0] = new Point(min.getPosX(), min.getPosY());
			bounds[1] = new Point(max.getPosX() + tilesize, max.getPosY()
					+ tilesize);
		}
		return bounds;
	}

	/**
	 * Load the tiles in a spiral, starting from center of the map.
	 */
	private List<TileImage> loadTiles(Moveable mapController) {

		TilesLoadable tileLoader = new DefaultTilesLoader(tileCache);
		return tileLoader.loadTiles(mapController);
	}

	private int getTileSize() {
		TileSource tileSource = tileCache.getTileSource();
		return tileSource.getTileSize();
	}


	@Override
	public void doRender(Group tilesGroup) {

		int tilesize = getTileSize();

		tilesGroup.getChildren().clear();

		for (TileImage tileImage : tileImages) {

			ImageView imageView = tileImage.getImageView();
			int posx = tileImage.getPosX();
			int posy = tileImage.getPosY();

			imageView.translateXProperty().set(posx);
			imageView.translateYProperty().set(posy);
			tilesGroup.getChildren().add(imageView);

			if (monoChrome) {
				setMonochromeEffect(imageView);
			}

			if (tileGridVisible) {
				tilesGroup.getChildren().add(createGrid(posx, posy, tilesize));
			}
		}
	}

	private void setMonochromeEffect(ImageView imageView) {

		ColorAdjust monochrome = new ColorAdjust();
		monochrome.setSaturation(-1);
		monochrome.setContrast(-0.3);
		monochrome.setBrightness(-0.3);
		imageView.setEffect(monochrome);
	}

	protected Path createGrid(int posx, int posy, int tilesize) {

		Path path = new Path();
		path.getElements().add(new MoveTo(posx, posy));
		path.getElements().add(new LineTo(posx + tilesize, posy));
		path.getElements().add(new LineTo(posx + tilesize, posy + tilesize));
		path.getElements().add(new LineTo(posx, posy + tilesize));
		path.getElements().add(new LineTo(posx, posy));
		path.setStrokeWidth(WIDTH);
		path.setStroke(Color.BLACK);

		return path;
	}

	public void setTileSource(TileSource tileSource) {
		tileCache.setTileSource(tileSource);
	}

	public TileSource getTileSource() {
		return tileCache.getTileSource();
	}

	public void setMonoChrome(boolean monoChrome) {
		this.monoChrome = monoChrome;
	}

	public void setTileGridVisible(boolean tileGridVisible) {
		this.tileGridVisible = tileGridVisible;
	}

}