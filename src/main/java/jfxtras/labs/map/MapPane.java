//==============================================================================
//   Copyright (C) 2012 Jeffrey L Smith
//
//  This library is free software; you can redistribute it and/or
//  modify it under the terms of the GNU Lesser General Public
//  License as published by the Free Software Foundation; either
//  version 2.1 of the License, or (at your option) any later version.
//    
//  This library is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
//  Lesser General Public License for more details.
//    
//  You should have received a copy of the GNU Lesser General Public
//  License along with this library; if not, write to the Free Software
//  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//    
//  For more information, please email jsmith.carlsbad@gmail.com
//    
//==============================================================================
package jfxtras.labs.map;

import jfxtras.labs.map.tile.ZoomBounds;
import jfxtras.labs.map.render.LicenseRenderer;
import jfxtras.labs.map.render.MapLineable;
import jfxtras.labs.map.render.MapMarkable;
import jfxtras.labs.map.render.MapOverlayable;
import jfxtras.labs.map.render.MapPolygonable;
import jfxtras.labs.map.render.Renderable;
import jfxtras.labs.map.tile.TileSource;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import jfxtras.labs.map.render.TileRenderer;
import jfxtras.labs.map.tile.TileCacheable;
import jfxtras.labs.map.tile.TileRepository;

import static jfxtras.labs.map.CoordinatesConverter.*;

/**
 * 
 * @author smithjel
 * @author Mario Schroeder
 */
public final class MapPane extends Pane implements MapTileable {

	private static final int INITIAL_ZOOM = 9;

	private static final int SIZE = 400;

	private static final int START = 0;

	private static final String STYLE_LOC = "cursorLocation";

	private TileRenderer tileRenderer;

	private MapEdgeVisibilityChecker mapEdgeVisibilityChecker;

	private List<MapMarkable> mapMarkerList = new ArrayList<>();

	private List<MapPolygonable> mapPolygonList = new ArrayList<>();

	private List<MapLineable> mapLineList = new ArrayList<>();

	private List<MapOverlayable> mapOverlayList = new ArrayList<>();

	// X&Y position of the center of this map on the world
	// in screen pixels for the current zoom level.
	private Point center;

	// Current zoom level
	private SimpleIntegerProperty zoom;

	// previous zoom level
	private int previousZoom;

	private boolean ignoreRepaint;

	private Rectangle clipMask = new Rectangle();

	private Group tilesGroup;

	private Text cursorLocationText;

	private SimpleIntegerProperty mapX = new SimpleIntegerProperty(START);

	private SimpleIntegerProperty mapY = new SimpleIntegerProperty(START);

	private SimpleIntegerProperty mapWidth = new SimpleIntegerProperty(SIZE);

	private SimpleIntegerProperty mapHeight = new SimpleIntegerProperty(SIZE);

	private SimpleIntegerProperty trailTime = new SimpleIntegerProperty(30);

	private SimpleBooleanProperty mapTrailsVisible = new SimpleBooleanProperty(
			true);

	private SimpleBooleanProperty mapRoutesVisible = new SimpleBooleanProperty(
			true);

	private SimpleBooleanProperty showZoomControls = new SimpleBooleanProperty(
			true);

	private SimpleBooleanProperty mapMarkersVisible = new SimpleBooleanProperty(
			true);

	private boolean cursorLocationVisible = true;

	private SimpleBooleanProperty mapVehiclesVisible = new SimpleBooleanProperty(
			true);

	private SimpleBooleanProperty mapPolygonsVisible = new SimpleBooleanProperty(
			true);

	private CoordinateStringFormater formater;

	public MapPane(TileSource ts) {
		this(ts, 800, 600, INITIAL_ZOOM);
	}

	public MapPane(TileSource tileSource, int width, int height, int zoom) {
		this.zoom = new SimpleIntegerProperty(zoom);
		formater = new CoordinateStringFormater();

		tilesGroup = new Group();

		TileCacheable tileCache = new TileRepository(tileSource);
		tileRenderer = new TileRenderer(tileCache);
		mapEdgeVisibilityChecker = new MapEdgeVisibilityChecker(tileRenderer);

		int tileSize = tileSource.getTileSize();
		setMinSize(tileSize, tileSize);

		DropShadow ds = new DropShadow();
		ds.setOffsetY(3.0f);
		ds.setColor(Color.BLACK);
		cursorLocationText = new Text("");
		cursorLocationText.setId(STYLE_LOC);
		cursorLocationText.setEffect(ds);
		cursorLocationText.setFontSmoothingType(FontSmoothingType.LCD);

		clipMask.setFill(Color.WHITE);
		tilesGroup.setClip(clipMask);
		getChildren().add(tilesGroup);
		getChildren().add(cursorLocationText);

		addSizeListeners();

		setPrefSize(width, height);

		centerMap();

		setTilesMouseHandler(new TilesMouseHandler());
	}

	public final void setTilesMouseHandler(TilesMouseHandler handler) {
		handler.setEventPublisher(this);
	}

	private void addSizeListeners() {
		widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				setMapWidth(newValue.doubleValue());
			}
		});
		heightProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				setMapHeight(newValue.doubleValue());
			}
		});

		mapX.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				int val = newValue.intValue();
				setLayoutX(val);
				clipMask.setLayoutX(val);
			}
		});

		mapY.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				int val = newValue.intValue();
				setLayoutY(val);
				clipMask.setLayoutY(val);
			}
		});

		mapWidth.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				int val = newValue.intValue();
				setMinWidth(val);
				setMaxWidth(val);
				setPrefWidth(val);
				clipMask.setWidth(val);
				adjustCursorLocationText();
				renderControl();
			}
		});

		mapHeight.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				int val = newValue.intValue();
				setMinHeight(val);
				setMaxHeight(val);
				setPrefHeight(val);
				clipMask.setHeight(val);
				adjustCursorLocationText();
				renderControl();
			}
		});
	}

	@Override
	public void setCursorLocationText(double x, double y) {
		if (cursorLocationVisible) {
			Coordinate coord = getCoordinate(new Point((int) x, (int) y));
			cursorLocationText.setText(formater.format(coord));
		}
	}

	@Override
	public void adjustCursorLocationText() {
		double strwidth = cursorLocationText.getBoundsInParent().getWidth();
		double x = (double) ((getMapWidth() / 2) - (strwidth / 2));
		int y = getMapHeight() - 28;

		cursorLocationText.setLayoutX(x);
		cursorLocationText.setLayoutY(y);
	}

	public void setMapBounds(int x, int y, int width, int height) {
		this.mapX.set(x);
		this.mapY.set(y);
		this.mapWidth.set(width);
		this.mapHeight.set(height);
	}

	public void setDisplayPositionByLatLon(double lat, double lon) {
		setDisplayPositionByLatLon(lat, lon, zoom.get());
	}

	public void setDisplayPositionByLatLon(double lat, double lon, int zoom) {
		setDisplayPositionByLatLon(createMapCenterPoint(), lat, lon, zoom);
	}

	private void setDisplayPosition(int x, int y, int zoom) {
		setDisplayPosition(createMapCenterPoint(), x, y, zoom);
	}

	private Point createMapCenterPoint() {
		return new Point((int) (getMapWidth() / 2), (int) (getMapHeight() / 2));
	}

	public void setDisplayPositionByLatLon(Point mapPoint, double lat,
			double lon, int zoom) {
		int x = Mercator.lonToX(lon, zoom);
		int y = Mercator.latToY(lat, zoom);
		setDisplayPosition(mapPoint, x, y, zoom);
	}

	private void setDisplayPosition(Point mapPoint, int x, int y, int zoom) {

		if (zoom >= tileRenderer.getTileSource().getMinZoom()
				&& zoom <= tileRenderer.getTileSource().getMaxZoom()) {

			// Get the plain tile number
			moveCenter(mapPoint, x, y);
			this.zoom.set(zoom);
			renderControl();
		}
	}

	private void moveCenter(Point mapPoint, int x, int y) {
		Point p = new Point();
		p.x = x - mapPoint.x + (int) (getMapWidth() / 2);
		p.y = y - mapPoint.y + (int) (getMapHeight() / 2);
		center = p;
	}

	public void setDisplayToFitMapMarkers() {
		if (mapMarkerList != null && !mapMarkerList.isEmpty()) {
			int x_min = Integer.MAX_VALUE;
			int y_min = Integer.MAX_VALUE;
			int x_max = Integer.MIN_VALUE;
			int y_max = Integer.MIN_VALUE;
			int mapZoomMax = getTileSource().getMaxZoom();
			for (MapMarkable marker : mapMarkerList) {
				int x = Mercator.lonToX(marker.getLon(), mapZoomMax);
				int y = Mercator.latToY(marker.getLat(), mapZoomMax);
				x_max = Math.max(x_max, x);
				y_max = Math.max(y_max, y);
				x_min = Math.min(x_min, x);
				y_min = Math.min(y_min, y);
			}
			int height = (int) Math.max(START, getMapHeight());
			int width = (int) Math.max(START, getMapWidth());
			int newZoom = mapZoomMax;
			int x = x_max - x_min;
			int y = y_max - y_min;
			while (x > width || y > height) {
				// System.out.println("zoom: " + zoom + " -> " + x + " " + y);
				newZoom--;
				x >>= 1;
				y >>= 1;
			}
			x = x_min + (x_max - x_min) / 2;
			y = y_min + (y_max - y_min) / 2;
			int z = 1 << (mapZoomMax - newZoom);
			x /= z;
			y /= z;
			setDisplayPosition(x, y, newZoom);
		}
	}

	public void setDisplayToFitMapRectangle() {
		if (mapPolygonList != null && !mapPolygonList.isEmpty()) {
			int x_min = Integer.MAX_VALUE;
			int y_min = Integer.MAX_VALUE;
			int x_max = Integer.MIN_VALUE;
			int y_max = Integer.MIN_VALUE;
			int mapZoomMax = getTileSource().getMaxZoom();

			int height = (int) Math.max(START, getMapHeight());
			int width = (int) Math.max(START, getMapWidth());
			int newZoom = mapZoomMax;
			int x = x_max - x_min;
			int y = y_max - y_min;
			while (x > width || y > height) {
				newZoom--;
				x >>= 1;
				y >>= 1;
			}
			x = x_min + (x_max - x_min) / 2;
			y = y_min + (y_max - y_min) / 2;
			int z = 1 << (mapZoomMax - newZoom);
			x /= z;
			y /= z;
			setDisplayPosition(x, y, newZoom);
		}
	}

	@Override
	public void moveMap(int x, int y) {

		center.x += x;
		center.y += y;
		renderControl();

		if (isEdgeVisible()) {
			centerMap();
		}
	}

	/**
	 * centers the map when necessary
	 */
	public final void centerMap() {

		setDisplayPositionByLatLon(START, START);
	}

	@Override
	public SimpleIntegerProperty zoomProperty() {
		return zoom;
	}

	@Override
	public void zoomIn() {
		setZoom(zoom.get() + 1);
	}

	@Override
	public void zoomIn(Point mapPoint) {

		updateZoom(zoom.get() + 1, mapPoint);
	}

	@Override
	public void zoomOut() {
		setZoom(zoom.get() - 1);
	}

	@Override
	public void zoomOut(Point mapPoint) {

		updateZoom(zoom.get() - 1, mapPoint);
	}

	@Override
	public void setZoom(int nextZoom) {

		Point mapPoint = createMapCenterPoint();
		updateZoom(nextZoom, mapPoint);
	}

	private void updateZoom(int nextZoom, Point mapPoint) {
		if (nextZoom <= getMaxZoom() && nextZoom >= getMinZoom()) {

			Coordinate zoomPos = getCoordinate(mapPoint);

			setDisplayPositionByLatLon(mapPoint, zoomPos.getLatitude(),
					zoomPos.getLongitude(), nextZoom);

			if (nextZoom < previousZoom && isEdgeVisible()) {
				centerMap();
			}
			
			if(nextZoom != zoom.get()){
				zoom.set(nextZoom);
				previousZoom = nextZoom;
			}
		}
	}

	private boolean isEdgeVisible() {
		Dimension dim = new Dimension(getMapWidth(), getMapHeight());
		return mapEdgeVisibilityChecker.isAllVisible(dim);
	}

	private Coordinate getCoordinate(Point p) {
		return toCoordinate(p, this);
	}

	public void setMapMarkerVisible(boolean mapMarkersVisible) {
		this.mapMarkersVisible.set(mapMarkersVisible);
		renderControl();
	}

	public void setMapMarkerList(List<MapMarkable> mapMarkerList) {
		this.mapMarkerList = mapMarkerList;
		renderControl();
	}

	public List<MapMarkable> getMapMarkerList() {
		return mapMarkerList;
	}

	public void removeMapOverlay(MapOverlayable overlay) {
		mapOverlayList.remove(overlay);
		renderControl();
	}

	public void addMapOverlay(MapOverlayable overlay) {
		mapOverlayList.add(overlay);
		renderControl();
	}

	public void setMapPolygonList(List<MapPolygonable> mapPolygonList) {
		this.mapPolygonList = mapPolygonList;
		renderControl();
	}

	public List<MapPolygonable> getMapRectangleList() {
		return mapPolygonList;
	}

	public void addMapLine(MapLineable line) {
		mapLineList.add(line);
		renderControl();
	}

	public void removeMapMarker(MapLineable line) {
		mapLineList.remove(line);
		renderControl();
	}

	public void addMapMarker(MapMarkable marker) {
		mapMarkerList.add(marker);
		renderControl();
	}

	public void removeMapMarker(MapMarkable marker) {
		mapMarkerList.remove(marker);
		renderControl();
	}

	public void addMapPolygon(MapPolygonable polygon) {
		mapPolygonList.add(polygon);
		renderControl();
	}

	public void removeMapPolygon(MapPolygonable polygon) {
		mapPolygonList.remove(polygon);
		renderControl();
	}

	public void setTileSource(TileSource tileSource) {
		int minZoom = tileSource.getMinZoom();
		int maxZoom = tileSource.getMaxZoom();
		if (maxZoom > ZoomBounds.MAX.getValue()) {
			throw new IllegalArgumentException("Maximum zoom level too high");
		}
		if (minZoom < ZoomBounds.Min.getValue()) {
			throw new IllegalArgumentException("Minumim zoom level too low");
		}
		tileRenderer.setTileSource(tileSource);
		// zoomSlider.setMin(tileSource.getMinZoom());
		// zoomSlider.setMax(tileSource.getMaxZoom());

		if (zoom.get() > tileSource.getMaxZoom()) {
			setZoom(tileSource.getMaxZoom());
		}

		renderControl();
	}

	protected void renderControl() {

		if (!ignoreRepaint) {
			boolean changed = renderMap();

			if (changed) {

				renderOverlays();
				renderPolygons();
				renderMarkers();

				renderAttribution();
			}
		}
	}

	protected boolean renderMap() {

		boolean updated = false;
		int tilesCount = tileRenderer.prepareTiles(this);

		if (tilesCount > 0) {
			tileRenderer.doRender(tilesGroup);
			updated = true;
		}

		return updated;
	}

	protected void renderOverlays() {
		for (MapOverlayable overlay : mapOverlayList) {
			overlay.render(this);
		}
	}

	protected void renderPolygons() {
		if (mapPolygonsVisible.get() && mapPolygonList != null) {
			for (MapPolygonable polygon : mapPolygonList) {
				polygon.render(this);
			}
		}
	}

	protected void renderMarkers() {
		if (mapMarkersVisible.get() && mapMarkerList != null) {
			for (MapMarkable marker : mapMarkerList) {
				marker.render(this);
			}
			for (MapLineable line : mapLineList) {
				line.render(this);
			}
		}
	}

	protected void renderAttribution() {

		if (getTileSource().isAttributionRequired()) {
			Renderable renderer = new LicenseRenderer();
			renderer.render(this);
		}
	}

	public void setMapX(int val) {
		this.mapX.set(val);
	}

	@Override
	public int getMapX() {
		return this.mapX.get();
	}

	public void setMapY(int val) {
		this.mapY.set(val);
	}

	@Override
	public int getMapY() {
		return this.mapY.get();
	}

	public void setMapWidth(double val) {
		mapWidth.set((int) val);
	}

	@Override
	public int getMapWidth() {
		return mapWidth.get();
	}

	public void setMapHeight(double val) {
		mapHeight.set((int) val);
	}

	@Override
	public int getMapHeight() {
		return mapHeight.get();
	}

	public void setTrailTime(int val) {
		this.trailTime.set(val);
	}

	public void setMapVehiclesVisible(boolean val) {
		this.mapVehiclesVisible.set(val);
		renderControl();
	}

	public boolean isMapVehiclesVisible() {
		return this.mapVehiclesVisible.get();
	}

	public void setMapPolygonsVisible(boolean val) {
		this.mapPolygonsVisible.set(val);
		renderControl();
	}

	public boolean isMapPolygonsVisible() {
		return this.mapPolygonsVisible.get();
	}

	public void setMapRoutesVisible(boolean val) {
		this.mapRoutesVisible.set(val);
		renderControl();
	}

	public boolean isMapRoutesVisible() {
		return this.mapRoutesVisible.get();
	}

	public void setShowZoomControls(boolean val) {
		this.showZoomControls.set(val);
		renderControl();
	}

	public boolean isShowZoomControls() {
		return this.showZoomControls.get();
	}

	public void setMapTrailsVisible(boolean val) {
		this.mapTrailsVisible.set(val);
		renderControl();
	}

	public boolean isMapTrailsVisible() {
		return this.mapTrailsVisible.get();
	}

	public void setMonochromeMode(boolean val) {
		tileRenderer.setMonoChrome(val);
		renderControl();
	}

	public void setTileGridVisible(boolean val) {
		tileRenderer.setTileGridVisible(val);
		renderControl();
	}

	public void setCursorLocationVisible(boolean val) {
		this.cursorLocationVisible = val;
	}

	@Override
	public TileSource getTileSource() {
		return tileRenderer.getTileSource();
	}

	@Override
	public Group getTilesGroup() {
		return tilesGroup;
	}

	@Override
	public Point getCenter() {
		return center;
	}

	@Override
	public boolean isMapMoveable() {
		Dimension dim = new Dimension(getMapWidth(), getMapHeight());
		return !mapEdgeVisibilityChecker.isAllVisible(dim);
	}

	@Override
	public int getMinZoom() {
		return getTileSource().getMinZoom();
	}

	@Override
	public int getMaxZoom() {
		return getTileSource().getMaxZoom();
	}

	public void setIgnoreRepaint(boolean ignoreRepaint) {
		this.ignoreRepaint = ignoreRepaint;
	}
}