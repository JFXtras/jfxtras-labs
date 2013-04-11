//==============================================================================
//   map is a Java library for parsing raw weather data
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
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import jfxtras.labs.map.tile.OsmTileSourceFactory;
import jfxtras.labs.map.tile.OsmType;
import jfxtras.labs.map.tile.TileSourceFactory;
import jfxtras.labs.map.render.MapLineable;
import jfxtras.labs.map.render.DefaultMapLine;
import jfxtras.labs.map.render.ImageMapMarker;
import jfxtras.labs.map.render.MapMarkable;

/**
 * 
 * @author smithjel
 */
public class MapPaneTrial1 extends Application {

	// static { // use system proxy settings when standalone application
	// System.setProperty("java.net.useSystemProxies", "true");
	// }


	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Map Demo");
		StackPane root = new StackPane();

		TileSourceFactory factory = new OsmTileSourceFactory();
		final MapPane map = new MapPane(factory.create(OsmType.Landscape.name()));

		map.setMapBounds(0, 0, 800, 600);

		String BASE_URL = "http://radar.weather.gov/ridge";
		final String radarImgUrl = BASE_URL + "/RadarImg/N0R/NKX_N0R_0.gif";

		int RadarImgWidth = 600;
		int RadarImgHeight = 550;
		double RadarImgXpixelStep = 0.00856977982954547;
		double RadarImgYpixelStep = -0.00856977982954547;
		double ul_Lat = 35.2714032814719;
		double ul_Lon = -119.60764942516;
		double lr_Lat = 35.2714032814719 + (RadarImgHeight * RadarImgYpixelStep);
		double lr_Lon = -119.60764942516 + (RadarImgWidth * RadarImgXpixelStep);

		Coordinate upperLeftCoord = new Coordinate(ul_Lat, ul_Lon);
		Coordinate lowerRightCoord = new Coordinate(lr_Lat, lr_Lon);

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

		map.setDisplayPositionByLatLon(32.81729, -117.215905, 9);

		Scene scene = createScene(root, map);

		root.getChildren().add(map);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private Scene createScene(StackPane root, final MapPane map) {
		Scene scene = new Scene(root, 800, 600);
		scene.widthProperty().addListener(new ChangeListener() {
			@Override
			public void changed(ObservableValue observable, Object oldValue,
					Object newValue) {
				Double width = (Double) newValue;
				map.setMapWidth(width);
			}
		});
		scene.heightProperty().addListener(new ChangeListener() {
			@Override
			public void changed(ObservableValue observable, Object oldValue,
					Object newValue) {
				Double height = (Double) newValue;
				map.setMapHeight(height);
			}
		});
		scene.getStylesheets().add(
				MapPaneTrial1.class.getResource("map_pane.css")
						.toExternalForm());
		return scene;
	}
}
