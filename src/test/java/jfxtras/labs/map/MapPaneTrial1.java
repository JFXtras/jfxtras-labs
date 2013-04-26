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

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import jfxtras.labs.map.render.DefaultMapLine;
import jfxtras.labs.map.render.ImageMapMarker;
import jfxtras.labs.map.render.MapLineable;
import jfxtras.labs.map.render.MapMarkable;
import jfxtras.labs.map.tile.BingTileSourceFactory;
import jfxtras.labs.map.tile.BingType;
import jfxtras.labs.map.tile.OsmTileSourceFactory;
import jfxtras.labs.map.tile.OsmType;
import jfxtras.labs.map.tile.TileSource;
import jfxtras.labs.map.tile.TileSourceFactory;

/**
 *
 * @author smithjel
 */
public class MapPaneTrial1 extends Application {
    
    private static final String BING_KEY = "Arzdiw4nlOJzRwOz__qailc8NiR31Tt51dN2D7cm57NrnceZnCpgOkmJhNpGoppU";
    
    private static final String OSM = "OSM";
    
    private static final String BING = "Bing";
    
    private static final String DEFAULT_CSS = "map_pane.css";
    
    private static final String IMG_CSS = "img_map_pane.css";

    // static { // use system proxy settings when standalone application
    // System.setProperty("java.net.useSystemProxies", "true");
    // }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        int RadarImgWidth = 600;
        int RadarImgHeight = 500;
        double RadarImgXpixelStep = 0.00856977982954547;
        double RadarImgYpixelStep = -0.00856977982954547;
        double ul_Lat = 35.2714032814719;
        double ul_Lon = -119.60764942516;
        double lr_Lat = ul_Lat + (RadarImgHeight * RadarImgYpixelStep);
        double lr_Lon = ul_Lon + (RadarImgWidth * RadarImgXpixelStep);

        Coordinate upperLeftCoord = new Coordinate(ul_Lat, ul_Lon);
        Coordinate lowerRightCoord = new Coordinate(lr_Lat, lr_Lon);

        primaryStage.setTitle("Map Demo");

        final TileSourceFactory<OsmType> factory = new OsmTileSourceFactory();
        OsmType osmType = OsmType.Mapnik;

        final MapPane map = new MapPane(factory.create(osmType));

        final String radarImgUrl = "http://radar.weather.gov/ridge/RadarImg/N0R/NKX_N0R_0.gif";

        MapWeatherRadar yumaRadar = new MapWeatherRadar(radarImgUrl, 600, 550,
            120, upperLeftCoord, lowerRightCoord);
        map.addMapOverlay(yumaRadar);

        List<Coordinate> coordinates = new ArrayList<>();
        coordinates.add(upperLeftCoord);
        coordinates.add(lowerRightCoord);

        MapAirspace airspace = new MapAirspace(coordinates);
        map.addMapPolygon(airspace);

        Image image = new Image(getClass().getResourceAsStream("plus.png"));
        map.addMapMarker(new ImageMapMarker(image, ul_Lat, ul_Lon, 25));
        MapMarkable marker = new ImageMapMarker(image, lr_Lat, lr_Lon, 1.0);
        map.addMapMarker(marker);

        MapLineable mapLine = new DefaultMapLine(coordinates);
        map.addMapLine(mapLine);

        map.setDisplayPositionByLatLon(32.81729, -117.215905);
        
        BorderPane borderPan = new BorderPane();
        final Scene scene = createScene(borderPan);
        ComboBox<String> comboBox = new ComboBox<>();
        for (OsmType type : OsmType.values()) {
            comboBox.getItems().add(OSM + " " + type.toString());
        }
        for(BingType type : BingType.values()){
        	comboBox.getItems().add(BING + " " + type.toString());
        }
        
        comboBox.getSelectionModel().select(osmType.toString());
        comboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> arg0,
                String oldVal, String newVal) {

                String name = newVal.split(" ")[1];
                TileSource ts;
                if (newVal.startsWith(BING)) {
                    TileSourceFactory<BingType> fac = new BingTileSourceFactory(BING_KEY);
                    BingType type = BingType.valueOf(name);
                    ts = fac.create(type);
                    if(type.equals(BingType.Aerial)){
                        setStyle(scene, IMG_CSS);
                    }
                } else {
                    OsmType type = OsmType.valueOf(name);
                    ts = factory.create(type);
                }
                
                map.setTileSource(ts);

            }
        });

        final ToolBar toolBar = new ToolBar(comboBox);
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue,
                Number newValue) {
                map.setMinWidth(newValue.doubleValue());
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue,
                Number newValue) {
                map.setMinHeight(newValue.doubleValue() - toolBar.getHeight());
            }
        });

        borderPan.setTop(toolBar);
        borderPan.setCenter(map);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Scene createScene(Pane root) {
        Scene scene = new Scene(root, 800, 600);
        setStyle(scene, DEFAULT_CSS);
        return scene;
    }

    private void setStyle(Scene scene, String css) {
        scene.getStylesheets().add(getClass().getResource(css)
            .toExternalForm());
    }
}
