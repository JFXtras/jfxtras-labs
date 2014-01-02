/**
 * TileRenderer.java
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
import jfxtras.labs.map.tile.TileOrderComparator;
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
	
	private TileOrderComparator tileComparator = new TileOrderComparator();

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
		loadTiles(mapController);
		return tileImages.size();
	}
	
	@Override
	public void refresh(Moveable mapController) {
		provider.setStrategy(new RefreshLoadStrategy());
		loadTiles(mapController);
		render(mapController.getTilesGroup());
	}
	
	private void loadTiles(Moveable mapController) {
		TilesLoadable tileLoader = new TilesLoader(provider);
		tileImages = tileLoader.loadTiles(mapController);
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

    @Override
	public void setMonoChrome(boolean monoChrome) {
		this.monoChrome = monoChrome;
	}

    @Override
	public void setTileGridVisible(boolean tileGridVisible) {
		this.tileGridVisible = tileGridVisible;
	}

}