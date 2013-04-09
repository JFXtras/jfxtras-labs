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

import jfxtras.labs.map.tile.Tile;
import jfxtras.labs.map.tile.TileRepository;
import jfxtras.labs.map.render.MapLineable;
import jfxtras.labs.map.render.MapMarkable;
import jfxtras.labs.map.render.MapOverlayable;
import jfxtras.labs.map.render.MapPolygonable;
import jfxtras.labs.map.tile.TileSource;
import jfxtras.labs.map.tile.TilePyramidSource;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;

/**
 *
 * @author smithjel
 * @author Mario Schroeder
 */
public final class MapPane extends Pane implements MapControlable {
    private static final double ZOOM_DIFF = .01;

    private TileSource tileSource;

    private TileRepository tileRepository;

    private static final Point[] move = {new Point(1, 0), new Point(0, 1), new Point(-1, 0), new Point(0, -1)};

    private List<MapMarkable> mapMarkerList;

    private List<MapPolygonable> mapPolygonList;

    private List<MapLineable> mapLineList;

    private List<MapOverlayable> mapOverlayList;

    // X&Y position of the center of this map on the world 
    // in screen pixels for the current zoom level.
    private Point center;

    // Current zoom level
    private int zoom;

    private Slider zoomSlider;

    private Button zoomInButton;

    private Button zoomOutButton;

    private VBox zoomControlsVbox;

    // Attribution
    private Image attrImage;

    private Text attrTermsUrl;

    private boolean ignoreRepaint;

    private Rectangle clipMask = new Rectangle();

    private Group tilesGroup;

    private Text cursorLocationText;

    private Point lastDragPoint = new Point();

    private boolean moving = false;

    private boolean movementEnabled = true;

    private SimpleIntegerProperty mapX;

    private SimpleIntegerProperty mapWidth;

    private SimpleIntegerProperty mapHeight;

    private SimpleIntegerProperty trailTime = new SimpleIntegerProperty(30);

    private SimpleBooleanProperty mapTrailsVisible = new SimpleBooleanProperty(true);

    private SimpleBooleanProperty mapRoutesVisible = new SimpleBooleanProperty(true);

    private SimpleBooleanProperty showZoomControls = new SimpleBooleanProperty(true);

    private SimpleBooleanProperty mapMarkersVisible;

    private SimpleBooleanProperty tileGridVisible;

    private SimpleBooleanProperty cursorLocationVisible;

    private SimpleBooleanProperty monochromeMode = new SimpleBooleanProperty();

    private SimpleIntegerProperty mapY;

    private SimpleBooleanProperty mapVehiclesVisible = new SimpleBooleanProperty(true);

    private SimpleBooleanProperty mapPolygonsVisible = new SimpleBooleanProperty(true);

    public MapPane() {
        this(new TilePyramidSource());
    }

    public MapPane(TileSource ts) {
        this(ts, 0, 0, 400, 400, 11);
    }

    public MapPane(TileSource ts, int x, int y, int width, int height, int initial_zoom) {

        cursorLocationText = new Text("");


        TilesMouseHandler handler = new TilesMouseHandler(this);
        tilesGroup = new Group();
        handler.setEventPublisher(tilesGroup);

        buildMapBounds(x, y, width, height);

        mapMarkersVisible = new SimpleBooleanProperty(true);
        tileGridVisible = new SimpleBooleanProperty(false);
        cursorLocationVisible = new SimpleBooleanProperty(true);

        this.tileSource = ts;
        tileRepository = new TileRepository(tileSource);

        mapMarkerList = new ArrayList<>();
        mapPolygonList = new ArrayList<>();
        mapLineList = new ArrayList<>();
        mapOverlayList = new ArrayList<>();

        buildZoomControls();

        zoom = initial_zoom;

        setMinSize(tileSource.getTileSize(), tileSource.getTileSize());
        setPrefSize(width, height);
        setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        setDisplayPositionByLatLon(0, 0, 3);

        DropShadow ds = new DropShadow();
        ds.setOffsetY(3.0f);
        ds.setColor(Color.BLACK);
        cursorLocationText.setEffect(ds);
        cursorLocationText.setFontSmoothingType(FontSmoothingType.LCD);

        clipMask.setFill(Color.WHITE);

        tilesGroup.setClip(clipMask);
        getChildren().add(tilesGroup);
        getChildren().add(zoomControlsVbox);
        getChildren().add(cursorLocationText);
    }

    private void buildMapBounds(int x, int y, int width, int height) {
        mapX = new SimpleIntegerProperty(x);
        mapX.addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                int val = (int) newValue;
                setLayoutX(val);
                clipMask.setLayoutX(val);
            }
        });

        mapY = new SimpleIntegerProperty(y);
        mapY.addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                int val = (int) newValue;
                setLayoutY(val);
                clipMask.setLayoutY(val);
            }
        });

        mapWidth = new SimpleIntegerProperty(width);
        mapWidth.addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                int val = (int) newValue;
                setMinWidth(val);
                setMaxWidth(val);
                setPrefWidth(val);
                clipMask.setWidth(val);
                adjustCursorLocationText();
                renderControl();
            }
        });

        mapHeight = new SimpleIntegerProperty(height);
        mapHeight.addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                int val = (int) newValue;
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
    public void adjustCursorLocationText() {
        double strwidth = cursorLocationText.getBoundsInParent().getWidth();
        double x = (double) ((getMapWidth() / 2) - (strwidth / 2));
        int y = getMapHeight() - 24;

        cursorLocationText.setLayoutX(x);
        cursorLocationText.setLayoutY(y);
    }

    protected void buildZoomControls() {

        ignoreRepaint = true;

        ZoomSliderFactory zoomSliderFactory = new ZoomSliderFactory(this);
        zoomSlider = zoomSliderFactory.create();

        ZoomButtonFactory zoomButtonFactory = new ZoomInButtonFactory(this);
        zoomInButton = zoomButtonFactory.create();

        zoomButtonFactory = new ZoomOutButtonFactory(this);
        zoomOutButton = zoomButtonFactory.create();

        zoomControlsVbox = new VBox();
        zoomControlsVbox.getChildren().add(zoomInButton);
        zoomControlsVbox.getChildren().add(zoomSlider);
        zoomControlsVbox.getChildren().add(zoomOutButton);
        zoomControlsVbox.setLayoutX(10);
        zoomControlsVbox.setLayoutY(10);

        ignoreRepaint = false;
    }

    public void setMapBounds(int x, int y, int width, int height) {
        this.mapX.set(x);
        this.mapY.set(y);
        this.mapWidth.set(width);
        this.mapHeight.set(height);
    }

    public void setDisplayPositionByLatLon(double lat, double lon, int zoom) {
        setDisplayPositionByLatLon(new Point((int) (getMapWidth() / 2), (int) (getMapHeight() / 2)), lat, lon, zoom);
    }

    public void setDisplayPositionByLatLon(Point mapPoint, double lat, double lon, int zoom) {
        int x = Mercator.lonToX(lon, zoom);
        int y = Mercator.latToY(lat, zoom);
        setDisplayPosition(mapPoint, x, y, zoom);
    }

    public void setDisplayPosition(int x, int y, int zoom) {
        setDisplayPosition(new Point((int) (getMapWidth() / 2), (int) (getMapHeight() / 2)), x, y, zoom);
    }

    public void setDisplayPosition(Point mapPoint, int x, int y, int zoom) {

        if (zoom > getTileSource().getMaxZoom() || zoom < ZoomBounds.Min.getValue()) {
            return;
        }

        // Get the plain tile number
        Point p = new Point();
        p.x = x - mapPoint.x + (int) (getMapWidth() / 2);
        p.y = y - mapPoint.y + (int) (getMapHeight() / 2);
        center = p;
        ignoreRepaint = true;
        try {
            int oldZoom = this.zoom;
            this.zoom = zoom;
            if (oldZoom != zoom) {
                zoomChanged(oldZoom);
            }
            if (Math.abs(zoomSlider.getValue() - zoom) > ZOOM_DIFF) {
                zoomSlider.setValue(zoom);
            }
        } finally {
            ignoreRepaint = false;
            renderControl();
        }
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
            int height = (int) Math.max(0, getMapHeight());
            int width = (int) Math.max(0, getMapWidth());
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

            int height = (int) Math.max(0, getMapHeight());
            int width = (int) Math.max(0, getMapWidth());
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

    /**
     * Calculates the latitude/longitude coordinate of the center of the currently displayed map area.
     *
     */
    public Coordinate getCenterCoordinate() {
        double lon = Mercator.xToLon(center.x, zoom);
        double lat = Mercator.yToLat(center.y, zoom);
        return new Coordinate(lat, lon);
    }

    // Converts the relative pixel coordinate (regarding the top left corner of
    // the displayed map) into a latitude / longitude coordinate
    public Coordinate getCoordinate(Point mapPoint) {
        return getCoordinate(mapPoint.x, mapPoint.y);
    }

    @Override
    public Coordinate getCoordinate(int mapPointX, int mapPointY) {
        int x = (int) (center.x + mapPointX - getMapWidth() / 2);
        int y = (int) (center.y + mapPointY - getMapHeight() / 2);
        double lon = Mercator.xToLon(x, zoom);
        double lat = Mercator.yToLat(y, zoom);
        return new Coordinate(lat, lon);
    }

    // Calculates the position on the map of a given coordinate
    @Override
    public Point getMapPoint(double lat, double lon, boolean checkOutside) {
        int x = Mercator.lonToX(lon, zoom);
        int y = Mercator.latToY(lat, zoom);
        x -= center.x - getMapWidth() / 2;
        y -= center.y - getMapHeight() / 2;
        if (checkOutside) {
            if (x < 0 || y < 0 || x > getMapWidth() || y > getMapHeight()) {
                return null;
            }
        }
        return new Point(x, y);
    }

    // Calculates the position on the map of a given coordinate
    @Override
    public Point getMapPoint(Coordinate coord) {
        return getMapPoint(coord.getLat(), coord.getLon(), false);
    }

    @Override
    public void moveMap(int x, int y) {
        center.x += x;
        center.y += y;
        renderControl();
    }

    @Override
    public int getZoom() {
        return zoom;
    }

    @Override
    public void zoomIn() {
        setZoom(zoom + 1);
    }

    @Override
    public void zoomIn(Point mapPoint) {
        setZoom(zoom + 1, mapPoint);
    }

    @Override
    public void zoomOut() {
        setZoom(zoom - 1);
    }

    @Override
    public void zoomOut(Point mapPoint) {
        setZoom(zoom - 1, mapPoint);
    }

    public void setZoom(int zoom, Point mapPoint) {
        if (zoom > getTileSource().getMaxZoom() || zoom < getTileSource().getMinZoom() || zoom == this.zoom) {
            return;
        }
        Coordinate zoomPos = getCoordinate(mapPoint);
        setDisplayPositionByLatLon(mapPoint, zoomPos.getLat(), zoomPos.getLon(), zoom);
    }

    @Override
    public void setZoom(int zoom) {
        setZoom(zoom, new Point((int) (getMapWidth() / 2), (int) (getMapHeight() / 2)));
    }

    protected void zoomChanged(int oldZoom) {
        zoomSlider.setTooltip(new Tooltip("Zoom level " + zoom));
        zoomInButton.setTooltip(new Tooltip("Zoom to level " + (zoom + 1)));
        zoomOutButton.setTooltip(new Tooltip("Zoom to level " + (zoom - 1)));
        zoomOutButton.setDisable(!(zoom > getTileSource().getMinZoom()));
        zoomInButton.setDisable(!(zoom < getTileSource().getMaxZoom()));
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

    public void setZoomContolsVisible(boolean visible) {
        this.zoomSlider.setVisible(visible);
        this.zoomInButton.setVisible(visible);
        this.zoomOutButton.setVisible(visible);
        this.zoomControlsVbox.setVisible(visible);
    }

    public boolean getZoomContolsVisible() {
        return zoomSlider.isVisible();
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
        this.tileSource = tileSource;
        tileRepository.setTileSource(tileSource);
        zoomSlider.setMin(tileSource.getMinZoom());
        zoomSlider.setMax(tileSource.getMaxZoom());

        if (zoom > tileSource.getMaxZoom()) {
            setZoom(tileSource.getMaxZoom());
        }
        boolean requireAttr = tileSource.requiresAttribution();
        if (requireAttr) {
            attrImage = tileSource.getAttributionImage();
            attrTermsUrl = new Text(tileSource.getTermsOfUseURL());
        } else {
            attrImage = null;
            attrTermsUrl = null;
        }
        renderControl();
    }

    protected void renderControl() {

        int iMove;
        int tilesize = tileSource.getTileSize();

        int off_x = (center.x % tilesize);
        int off_y = (center.y % tilesize);

        int diff_left = off_x;
        int diff_right = tilesize - off_x;
        int diff_top = off_y;
        int diff_bottom = tilesize - off_y;

        boolean start_left = diff_left < diff_right;
        boolean start_top = diff_top < diff_bottom;

        tilesGroup.getChildren().clear();
        clipMask.setTranslateX(-(this.mapX.get()));
        clipMask.setTranslateY(-(this.mapY.get()));


        if (this.showZoomControls.get() == true) {
            setZoomContolsVisible(true);
        } else {
            setZoomContolsVisible(false);
        }

        if (start_top) {
            if (start_left) {
                iMove = 2;
            } else {
                iMove = 3;
            }
        } else {
            if (start_left) {
                iMove = 1;
            } else {
                iMove = 0;
            }
        }

        renderTiles(tilesize, off_x, off_y, iMove);

        renderOverlays();
        renderPolygons();
        renderMarkers();

        renderAttribution();
    }

    private void renderTiles(int tilesize, int off_x, int off_y, int iMove) {

        int tilex = (center.x / tilesize);
        int tiley = (center.y / tilesize);

        int w2 = (int) (getMapWidth() / 2);
        int h2 = (int) (getMapHeight() / 2);
        int posx = w2 - off_x;
        int posy = h2 - off_y;

        // calculate the visibility borders
        int x_min = -tilesize;
        int y_min = -tilesize;
        int x_max = (int) getMapWidth();
        int y_max = (int) getMapHeight();

        // paint the tiles in a spiral, starting from center of the map
        boolean painted = true;
        int x = 0;
        while (painted) {
            painted = false;
            for (int i = 0; i < 4; i++) {
                if (i % 2 == 0) {
                    x++;
                }
                for (int j = 0; j < x; j++) {
                    if (x_min <= posx && posx <= x_max && y_min <= posy && posy <= y_max) {
                        // tile is visible
                        Tile tile = tileRepository.getTile(tilex, tiley, zoom);
                        if (tile != null) {
                            painted = true;

                            tile.getImageView().translateXProperty().set(posx);
                            tile.getImageView().translateYProperty().set(posy);

                            if (this.monochromeMode.get() == true) {
                                ColorAdjust monochrome = new ColorAdjust();
                                monochrome.setSaturation(-1);
                                monochrome.setContrast(-0.3);
                                monochrome.setBrightness(-0.3);
                                tile.getImageView().setEffect(monochrome);
                            }

                            tilesGroup.getChildren().add(tile.getImageView());

                            if (tileGridVisible.get() == true) {
                                Path path = new Path();
                                path.getElements().add(new MoveTo(posx, posy));
                                path.getElements().add(new LineTo(posx + tilesize, posy));
                                path.getElements().add(new LineTo(posx + tilesize, posy + tilesize));
                                path.getElements().add(new LineTo(posx, posy + tilesize));
                                path.getElements().add(new LineTo(posx, posy));
                                path.setStrokeWidth(1);
                                path.setStroke(Color.BLACK);

                                tilesGroup.getChildren().add(path);
                            }
                        }
                    }
                    Point p = move[iMove];
                    posx += p.x * tilesize;
                    posy += p.y * tilesize;
                    tilex += p.x;
                    tiley += p.y;
                }
                iMove = (iMove + 1) % move.length;
            }
        }
    }

    private void renderOverlays() {
        for (MapOverlayable overlay : mapOverlayList) {
            overlay.render(this);
        }
    }

    private void renderPolygons() {
        if (mapPolygonsVisible.get() && mapPolygonList != null) {
            for (MapPolygonable polygon : mapPolygonList) {
                polygon.render(this);
            }
        }
    }

    private void renderMarkers() {
        if (mapMarkersVisible.get() && mapMarkerList != null) {
            for (MapMarkable marker : mapMarkerList) {
                marker.render(this);
            }
            for (MapLineable line : mapLineList) {
                line.render(this);
            }
        }
    }

    private void renderAttribution() {

        if (tileSource.requiresAttribution()) {

            // Draw attribution text
            if (attrTermsUrl != null) {
                DropShadow ds = new DropShadow();
                ds.setOffsetY(3.0f);
                ds.setColor(Color.BLACK); // Color.color(0.4f, 0.4f, 0.4f));
                attrTermsUrl.setEffect(ds);
                attrTermsUrl.setFontSmoothingType(FontSmoothingType.LCD);
                attrTermsUrl.setFill(Color.BLUE);

                tilesGroup.getChildren().add(attrTermsUrl);

                double strwidth = attrTermsUrl.getBoundsInParent().getWidth();
                attrTermsUrl.setLayoutX((double) (mapWidth.get() - strwidth));
                attrTermsUrl.setLayoutY(mapHeight.doubleValue() - 8);
            }

            // Draw attribution logo
            if (attrImage != null) {
                ImageView attImgView = new ImageView(attrImage);
                DropShadow ds = new DropShadow();
                ds.setOffsetY(3.0f);
                ds.setColor(Color.BLACK); // Color.color(0.4f, 0.4f, 0.4f));
                attImgView.setEffect(ds);
                attImgView.setLayoutX(mapWidth.get() - attrImage.getWidth());
                attImgView.setLayoutY((mapHeight.doubleValue() - 8) - (1.4 * attrImage.getHeight()));
                tilesGroup.getChildren().add(attImgView);
            }
        }
    }

    @Override
    public boolean isMovementEnabled() {
        return movementEnabled;
    }

    // Enables or disables moap movement via the mouse.
    public void setMovementEnabled(boolean movementEnabled) {
        this.movementEnabled = movementEnabled;
    }

    @Override
    public Point getLastDragPoint() {
        return lastDragPoint;
    }

    @Override
    public void setLastDragPoint(Point point) {
        this.lastDragPoint = point;
    }

    @Override
    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    @Override
    public boolean isMoving() {
        return moving;
    }

    @Override
    public Text getCursorLocationText() {
        return cursorLocationText;
    }

    @Override
    public boolean isCursorLocationVisible() {
        return cursorLocationVisible.get();
    }

    public void setMapX(int val) {
        this.mapX.set(val);
    }

    public int getMapX() {
        return this.mapX.get();
    }

    public void setMapY(int val) {
        this.mapY.set(val);
    }

    public int getMapY() {
        return this.mapY.get();
    }

    public void setMapWidth(double val) {
        mapWidth.set((int) val);
    }

    private int getMapWidth() {
        return mapWidth.get();
    }

    public void setMapHeight(double val) {
        mapHeight.set((int) val);
    }

    private int getMapHeight() {
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
        this.monochromeMode.set(val);
        renderControl();
    }

    public boolean isMonochromeMode() {
        return this.monochromeMode.get();
    }

    public void setMapMarkersVisible(boolean val) {
        this.tileGridVisible.set(val);
        renderControl();
    }

    public boolean isMapMarkersVisible() {
        return this.tileGridVisible.get();
    }

    public void setMapGridVisible(boolean val) {
        this.tileGridVisible.set(val);
        renderControl();
    }

    public boolean isMapGridVisible() {
        return this.tileGridVisible.get();
    }

    public void setCursorLocationVisible(boolean val) {
        this.cursorLocationVisible.set(val);
    }

    @Override
    public TileSource getTileSource() {
        return tileRepository.getTileSource();
    }

    @Override
    public boolean isIgnoreRepaint() {
        return ignoreRepaint;
    }

    @Override
    public Group getTilesGroup() {
        return tilesGroup;
    }
}
