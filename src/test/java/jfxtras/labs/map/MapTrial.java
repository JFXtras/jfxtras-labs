package jfxtras.labs.map;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import jfxtras.labs.map.render.DefaultMapLine;
import jfxtras.labs.map.render.ImageMapMarker;
import jfxtras.labs.map.render.MapLineable;

/**
 * 
 * @author Mario Schroeder
 */
public class MapTrial extends Application {

	public static void main(String[] args) {
		Application.launch(MapTrial.class, args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("Map.fxml"), null,
				new MapBuilderFactory());

		Scene scene = new Scene(root);
		// TODO find a way that the map automatic resizes without the need to
		// add listeners
		final ToolBar toolBar = (ToolBar) root.lookup("#toolBar");
		final MapPane map = (MapPane) root.lookup("#map");

		addLayers(map);

		map.setTileGridVisible(true);

		scene.getStylesheets().add(getClass().getResource("map_scene.css").toExternalForm());
		scene.widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				map.setMinWidth(newValue.doubleValue());
			}
		});
		scene.heightProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				map.setMinHeight(newValue.doubleValue() - toolBar.getHeight());
			}
		});

		stage.setTitle("FXML Map");

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
