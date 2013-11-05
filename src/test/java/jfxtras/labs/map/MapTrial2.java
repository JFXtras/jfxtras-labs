/**
 * MapTrial2.java
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

package jfxtras.labs.map;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import jfxtras.labs.map.render.DefaultMapLine;
import jfxtras.labs.map.render.ImageMapMarker;
import jfxtras.labs.map.render.MapLineable;

/**
 * 
 * @author Mario Schroeder
 */
public class MapTrial2 extends Application {

	public static void main(String[] args) {
		Application.launch(MapTrial2.class, args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("MapTrial2.fxml"), null,
				new MapBuilderFactory());

		Scene scene = new Scene(root);
		
		final MapPane map = (MapPane) root.lookup("#map");
		
		addLayers(map);

		map.setTileGridVisible(true);
		
		scene.getStylesheets().add(getClass().getResource("map_scene.css").toExternalForm());

		stage.setTitle("Map Trial 2");

		stage.setScene(scene);
		stage.show();
	}

	private void addLayers(MapPane map) {
		
		map.setIgnoreRepaint(true); //disable painting when adding layers

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

		List<Coordinate> coordinates = new ArrayList<>();
		coordinates.add(upperLeftCoord);
		coordinates.add(lowerRightCoord);


//		MapAirspace airspace = new MapAirspace(coordinates);
//		map.addMapLayer(airspace);

		Image image = new Image(getClass().getResourceAsStream("plus.png"));
		map.addMapLayer(new ImageMapMarker(image, ul_Lat, ul_Lon, 25));
		map.addMapLayer(new ImageMapMarker(image, lr_Lat, lr_Lon, 1.0));

		MapLineable mapLine = new DefaultMapLine(coordinates);
		map.addMapLayer(mapLine);

		map.setDisplayPositionByLatLon(32.81729, -117.215905);
		
		String radarImgUrl = "http://radar.weather.gov/ridge/RadarImg/N0R/NKX_N0R_0.gif";

		MapWeatherRadar yumaRadar = new MapWeatherRadar(radarImgUrl, 600, 550,
				120, upperLeftCoord, lowerRightCoord);
		map.addMapLayer(yumaRadar);
		
		map.setIgnoreRepaint(false); //enabling painting
	}

}
