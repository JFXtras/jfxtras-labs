package jfxtras.labs.map.render;

import java.awt.Point;
import java.util.List;

import static java.util.Collections.min;
import static java.util.Collections.max;
import static java.util.Collections.emptyList;
import javafx.scene.Group;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import jfxtras.labs.map.Moveable;
import jfxtras.labs.map.tile.CacheLoadStrategy;
import jfxtras.labs.map.tile.RefreshLoadStrategy;
import jfxtras.labs.map.tile.TileProvideable;
import jfxtras.labs.map.tile.TileImageComparator;
import jfxtras.labs.map.tile.TileImage;
import jfxtras.labs.map.tile.TilesLoadable;
import jfxtras.labs.map.tile.TileSource;
import jfxtras.labs.map.tile.TilesLoader;

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
	
	private final TileProvideable provider;


	public TileRenderer(TileProvideable provider) {
		this.provider = provider;
		this.tileImages = emptyList();
	}

	@Override
	public int prepareTiles(Moveable mapController) {
		provider.setStrategy(new CacheLoadStrategy());
		TilesLoadable tileLoader = new TilesLoader(provider);
		tileImages = tileLoader.loadTiles(mapController);
		return tileImages.size();
	}
	
	@Override
	public void refresh(Moveable mapController) {
		provider.setStrategy(new RefreshLoadStrategy());
		TilesLoadable tileLoader = new TilesLoader(provider);
		tileImages = tileLoader.loadTiles(mapController);
		render(mapController.getTilesGroup());
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

	private int getTileSize() {
		TileSource tileSource = provider.getTileSource();
		return tileSource.getTileSize();
	}

	@Override
	public void render(Group tilesGroup) {

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
				tilesGroup.getChildren().add(createBorder(posx, posy, tilesize));
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

	protected Path createBorder(int posx, int posy, int tilesize) {

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

	public void setMonoChrome(boolean monoChrome) {
		this.monoChrome = monoChrome;
	}

	public void setTileGridVisible(boolean tileGridVisible) {
		this.tileGridVisible = tileGridVisible;
	}

}