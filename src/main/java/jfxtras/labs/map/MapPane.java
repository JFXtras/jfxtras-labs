/**
 * MapPane.java
 *
 * Copyright (c) 2011-2013, JFXtras All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. * Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. * Neither the name of the <organization> nor the names
 * of its contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jfxtras.labs.map;

import jfxtras.labs.map.tile.ZoomBounds;
import jfxtras.labs.map.render.LicenseRenderer;
import jfxtras.labs.map.render.MapLineable;
import jfxtras.labs.map.render.MapMarkable;
import jfxtras.labs.map.render.MapPolygonable;
import jfxtras.labs.map.render.Renderable;
import jfxtras.labs.map.render.TileRenderable;
import jfxtras.labs.map.tile.TileSource;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import jfxtras.labs.map.render.TileRenderer;
import jfxtras.labs.map.tile.TileProvideable;
import jfxtras.labs.map.tile.TileRepository;
import static javafx.collections.FXCollections.*;
import static jfxtras.labs.map.CoordinatesConverter.*;

/**
 *
 * @author smithjel
 * @author Mario Schroeder
 */
public final class MapPane extends Pane implements MapTilesourceable {

    private static final int INITIAL_ZOOM = 9;
    private static final int SIZE = 600;
    private static final int START = 0;
    private static final String STYLE_LOC = "cursorLocation";
    private TileProvideable tilesProvider;
    private TileRenderable tileRenderer;
    private MapEdgeChecker mapEdgeChecker;
    private ObservableList<Renderable> mapLayers = observableArrayList();
    // X&Y position of the center of this map on the world
    // in screen pixels for the current zoom level.
    private Point center = new Point();
    // Current zoom level
    private SimpleIntegerProperty zoom;
    private SimpleIntegerProperty minZoom = new SimpleIntegerProperty(ZoomBounds.MIN.getValue());
    private SimpleIntegerProperty maxZoom = new SimpleIntegerProperty(ZoomBounds.MAX.getValue());
    private boolean ignoreRepaint;
    private Rectangle clipMask = new Rectangle();
    private Group tilesGroup;
    private Text cursorLocationText;
    private SimpleIntegerProperty mapWidth = new SimpleIntegerProperty(SIZE);
    private SimpleIntegerProperty mapHeight = new SimpleIntegerProperty(SIZE);
    private SimpleBooleanProperty mapMarkersVisible = new SimpleBooleanProperty(
            true);
    private boolean cursorLocationVisible = true;
    private SimpleBooleanProperty mapPolygonsVisible = new SimpleBooleanProperty(
            true);
    private CoordinateStringFormater formater;
    /**
     * This field is true when tiles when tiles where loaded.
     */
    private boolean tilesPrepared;
    private ZoomCoordinateCache zoomCoordinateCache = new ZoomCoordinateCache();
    private ZoomChangeListener zoomChangeListener = new ZoomChangeListener();

    public MapPane(TileSource ts) {
        this(ts, SIZE, SIZE, INITIAL_ZOOM);
    }

    public MapPane(TileSource tileSource, int width, int height, int zoom) {
        this.zoom = new SimpleIntegerProperty(zoom);
        formater = new CoordinateStringFormater();

        tilesGroup = new Group();

        tilesProvider = new TileRepository(tileSource);
        setZoomBounds();
        tileRenderer = new TileRenderer(tilesProvider);
        mapEdgeChecker = new MapEdgeChecker(tileRenderer);

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

        addRenderChangeListener();
        addSizeListeners();

        setPrefSize(width, height);

        centerMap();

        setTilesMouseHandler(new TilesMouseHandler());

        clipMask.setWidth(Double.MAX_VALUE);
        clipMask.setHeight(Double.MAX_VALUE);

        this.zoom.addListener(zoomChangeListener);
    }

    public final void setTilesMouseHandler(TilesMouseHandler handler) {
        handler.setEventPublisher(this);
    }

    private void addRenderChangeListener() {
        RenderChangeListener listener = new RenderChangeListener();
        mapLayers.addListener(listener);

        mapMarkersVisible.addListener(listener);
        mapPolygonsVisible.addListener(listener);
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

        mapWidth.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                clipMask.setWidth((Integer) newValue);
                adjustCursorLocationText();
                renderControl();
            }
        });

        mapHeight.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                clipMask.setHeight((Integer) newValue);
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

    public void setDisplayPositionByLatLon(double lat, double lon) {
        setDisplayPositionByLatLon(createMapCenterPoint(), lat, lon, zoom.get());
    }

    private Point createMapCenterPoint() {
        return new Point((int) (getMapWidth() / 2), (int) (getMapHeight() / 2));
    }

    private void setDisplayPositionByLatLon(Point mapPoint, double lat,
            double lon, int zoom) {
        int x = Mercator.lonToX(lon, zoom);
        int y = Mercator.latToY(lat, zoom);
        setDisplayPosition(mapPoint, x, y, zoom);
    }

    private void setDisplayPosition(int x, int y, int zoom) {
        setDisplayPosition(createMapCenterPoint(), x, y, zoom);
    }

    private void setDisplayPosition(Point mapPoint, int x, int y, int zoom) {
        if (isValidZoom(zoom)) {
            // Get the plain tile number
            moveCenter(mapPoint, x, y);
            renderControl();
        }
    }

    private void moveCenter(Point mapPoint, int x, int y) {
        center.x = x - mapPoint.x + (int) (getMapWidth() / 2);
        center.y = y - mapPoint.y + (int) (getMapHeight() / 2);
    }

    public void setDisplayToFitMapMarkers() {
        List<MapMarkable> markers = getMapMarkers();
        if (!markers.isEmpty()) {
            int x_min = Integer.MAX_VALUE;
            int y_min = Integer.MAX_VALUE;
            int x_max = Integer.MIN_VALUE;
            int y_max = Integer.MIN_VALUE;
            int mapZoomMax = getTileSource().getMaxZoom();

            for (MapMarkable marker : markers) {
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

    private List<MapMarkable> getMapMarkers() {
        List<MapMarkable> markers = new ArrayList<>();
        // a bit ugly to filter out by instance of
        for (Renderable layer : mapLayers) {
            if (layer instanceof MapMarkable) {
                markers.add((MapMarkable) layer);
            }
        }
        return markers;
    }

    public void setDisplayToFitMapRectangle() {
        if (!mapLayers.isEmpty()) {
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
        zoomCoordinateCache.clear();
        
        Point previous = new Point(center);
        center.x += x;
        center.y += y;

        //check if we are not on edge with the new center point
        if (prepareTiles() > 0 && !isOnEdge()) {
            tilesPrepared = true;
        } else {
            //we are on the edge of the map, reset the center point to the previous
            center = previous;
            prepareTiles();
        }

        renderControl();
    }

    /**
     * This method checks if the map is close before getting out of the component.
     * @return <code>true</code> if the map is on the edge.
     */
    private boolean isOnEdge() {
        Dimension dim = new Dimension(getMapWidth(), getMapHeight());
        return mapEdgeChecker.isOnEdge(dim);
    }

    /**
     * Centers the map
     */
    @Override
    public final void centerMap() {
        setDisplayPositionByLatLon(START, START);
        zoomCoordinateCache.clear();
    }

    @Override
    public SimpleIntegerProperty zoomProperty() {
        return zoom;
    }

    private void setZoom(int nextZoom) {
        zoom.set(nextZoom);
    }

    private void updateZoom(int nextZoom) {
        Point mapPoint = createMapCenterPoint();
        updateZoom(nextZoom, mapPoint, zoomCoordinateCache.getZoomCoordinate());
    }

    private void updateZoom(int nextZoom, Point mapPoint, Coordinate zoomPos) {
        if (isValidZoom(nextZoom)) {
            setDisplayPositionByLatLon(mapPoint, zoomPos.getLatitude(),
                    zoomPos.getLongitude(), nextZoom);
        }
    }

    private boolean isValidZoom(int nextZoom) {
        return nextZoom <= getMaxZoom() && nextZoom >= getMinZoom();
    }

    private Coordinate getCoordinate(Point p) {
        return toCoordinate(p, this);
    }

    private Coordinate getCoordinate(Point p, int zoom) {
        Dimension dim = new Dimension(getMapWidth(), getMapHeight());
        return toCoordinate(p, center, dim, zoom);
    }

    public void setMapMarkerVisible(boolean mapMarkersVisible) {
        this.mapMarkersVisible.set(mapMarkersVisible);
    }

    public void removeMapLayer(Renderable layer) {
        mapLayers.remove(layer);
    }

    public void addMapLayer(Renderable layer) {
        mapLayers.add(layer);
    }

    public void setTileSource(TileSource tileSource) {
        int min = tileSource.getMinZoom();
        int max = tileSource.getMaxZoom();
        if (max > ZoomBounds.MAX.getValue()) {
            throw new IllegalArgumentException("Maximum zoom level too high");
        }
        if (min < ZoomBounds.MIN.getValue()) {
            throw new IllegalArgumentException("Minumim zoom level too low");
        }
        tilesProvider.setTileSource(tileSource);
        setZoomBounds();
        //set zoom according to max or min available zoom
        if (zoom.get() > max) {
            setZoom(max);
        } else if (zoom.get() < min) {
            setZoom(min);
        }
        zoomCoordinateCache.clear();

        renderControl();
    }

    protected void renderControl() {

        if (!ignoreRepaint) {
            boolean changed = renderMap();

            if (changed) {
                renderMapLayers();
                renderAttribution();
            }
        }
    }

    /**
     * This method renders the map.
     * @return 
     */
    protected boolean renderMap() {

        boolean updated = false;

        //tiles where not loaded
        if (!tilesPrepared) {
            //only call the renderer if some tiles where loaded
            if (prepareTiles() > 0) {
                tileRenderer.render(tilesGroup);
                updated = true;
            }
        } else {
            tileRenderer.render(tilesGroup);
            updated = true;
        }

        //tiles where loaded
        tilesPrepared = false;

        return updated;
    }

    /**
     * Preload the tiles for the current view.
     * @return the number of actual loaded tiles
     */
    private int prepareTiles() {
        return tileRenderer.prepareTiles(this);
    }

    /**
     * This method renders all add overlays.
     */
    protected void renderMapLayers() {
        for (Renderable overlay : mapLayers) {
            if (isEnabled(overlay)) {
                overlay.render(this);
            }
        }
    }

    protected boolean isEnabled(Renderable renderable) {
        boolean enabled = true;
        if ((renderable instanceof MapPolygonable && !isMapPolygonsVisible())) {
            enabled = false;
        } else if (renderable instanceof MapMarkable
                || renderable instanceof MapLineable) {
            enabled = mapMarkersVisible.get();
        }
        return enabled;
    }

    protected void renderAttribution() {

        if (getTileSource().isAttributionRequired()) {
            Renderable renderer = new LicenseRenderer();
            renderer.render(this);
        }
    }

    /**
     * Reloads the hole map, incded additional layers and attribution.
     */
    public void refereshMap() {
        tileRenderer.refresh(this);
        renderMapLayers();
        renderAttribution();
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

    public void setMapPolygonsVisible(boolean val) {
        this.mapPolygonsVisible.set(val);
    }

    public boolean isMapPolygonsVisible() {
        return this.mapPolygonsVisible.get();
    }

    public void setMonochromeMode(boolean val) {
        tileRenderer.setMonoChrome(val);
        renderControl();
    }

    /**
     * Renders a a frame around the tiles which will be display as a grid for all tiles.
     * @param val if set to <code>true</code> a grid is added.
     */
    public void setTileGridVisible(boolean val) {
        tileRenderer.setTileGridVisible(val);
        renderControl();
    }

    public void setCursorLocationVisible(boolean val) {
        this.cursorLocationVisible = val;
    }

    @Override
    public TileSource getTileSource() {
        return tilesProvider.getTileSource();
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
        return !mapEdgeChecker.isAllVisible(dim);
    }

    private int getMinZoom() {
        return minZoomProperty().get();
    }

    @Override
    public SimpleIntegerProperty minZoomProperty() {
        return minZoom;
    }

    private int getMaxZoom() {
        return maxZoomProperty().get();
    }

    @Override
    public SimpleIntegerProperty maxZoomProperty() {
        return maxZoom;
    }

	/**
     * This method can be used to avoid refresh when a bunch of properties is changed.
     * @param ignoreRepaint 
     */
    public void setIgnoreRepaint(boolean ignoreRepaint) {
        this.ignoreRepaint = ignoreRepaint;
    }

    private void setZoomBounds() {
        minZoom.set(getTileSource().getMinZoom());
        maxZoom.set(getTileSource().getMaxZoom());
    }

    private class RenderChangeListener implements
            ListChangeListener<Renderable>, ChangeListener<Boolean> {

        @Override
        public void onChanged(Change<? extends Renderable> change) {
            renderControl();
        }

        @Override
        public void changed(ObservableValue<? extends Boolean> observable,
                Boolean oldVal, Boolean newVal) {
            renderControl();
        }
    }

    //store the coordinate where the zoom started
    private class ZoomCoordinateCache {

        private Coordinate zoomCoordinate;

        Coordinate getZoomCoordinate() {
            if (zoomCoordinate == null) {
                Point p = createMapCenterPoint();
                zoomCoordinate = getCoordinate(p);
            }
            return zoomCoordinate;
        }

        void setZoomCoordinate(Coordinate coordinate) {
            this.zoomCoordinate = coordinate;
        }

        void clear() {
            zoomCoordinate = null;
        }
    }

    private class ZoomChangeListener implements ChangeListener<Number> {

        @Override
        public void changed(ObservableValue<? extends Number> ov,
                Number oldVal, Number newVal) {
            if (oldVal != null) {
                int oldZoomVal = oldVal.intValue();
                Coordinate c = getCoordinate(createMapCenterPoint(), oldZoomVal);
                zoomCoordinateCache.setZoomCoordinate(c);
                int newZoomVal = newVal.intValue();
                updateZoom(newZoomVal);
            }
        }
    }
}