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

import java.awt.Point;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import jfxtras.labs.map.render.MapOverlayable;

/**
 * 
 * @author smithjel
 */
public class MapWeatherRadar implements MapOverlayable {

	private Image radarImg;

	private ImageView radarView;

	private String radarImgUrl;

	private int radarImgHeight;

	private int radarImgWidth;

	private ImageView scaleView;

	private int refreshInterval;

	private Coordinate upperLeftCoord;

	private Coordinate lowerRightCoord;

	// Line 1: x-dimension of a pixel in map units
	// Line 2: rotation parameter
	// Line 3: rotation parameter
	// Line 4: NEGATIVE of y-dimension of a pixel in map units
	// Line 5: x-coordinate of center of upper left pixel
	// Line 6: y-coordinate of center of upper left pixel
	public MapWeatherRadar(String url, int img_width, int img_height,
			int refresh_interval, Coordinate upperLeftCoord,
			Coordinate lowerRightCoord) {
		super();
		radarImgUrl = url;
		radarImgWidth = img_width;
		radarImgHeight = img_height;
		refreshInterval = refresh_interval;
		this.upperLeftCoord = upperLeftCoord;
		this.lowerRightCoord = lowerRightCoord;

		scaleView = new ImageView(new Image(
				"jfxtras/labs/map/weather_radar_scale.png"));
		DropShadow ds2 = new DropShadow();
		ds2.setOffsetX(10.0);
		ds2.setOffsetY(8.0);
		scaleView.setEffect(ds2);

		radarImg = new Image(radarImgUrl, radarImgWidth, radarImgHeight, true,
				true);
		radarView = new ImageView(radarImg);
		DropShadow dropShadow = new DropShadow();
		dropShadow.setOffsetX(10.0);
		dropShadow.setOffsetY(8.0);
		radarView.setEffect(dropShadow);

		Timeline radarUpdateTimer = new Timeline(new KeyFrame(
				Duration.seconds(this.refreshInterval),
				new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						radarImg = new Image(radarImgUrl, radarImgWidth,
								radarImgHeight, true, true);
						radarView.setImage(radarImg);
					}
				}));
		radarUpdateTimer.setCycleCount(Timeline.INDEFINITE);
		radarUpdateTimer.play();
	}

	@Override
	public Image getImage() {
		return radarImg;
	}

	@Override
	public void setImage(Image newimg) {
		radarImg = newimg;
	}

	@Override
	public void render(MapControlable viewer) {

		ObservableList<Node> children = viewer.getTilesGroup().getChildren();
		addRadarView(viewer, children);
		addScaleView(children);

	}

	private void addRadarView(MapControlable viewer,
			ObservableList<Node> children) {

		Point ptUL = viewer.getMapPoint(upperLeftCoord);
		Point ptLR = viewer.getMapPoint(lowerRightCoord);

		double imgX = viewer.getMapPoint(upperLeftCoord).x;
		double imgY = viewer.getMapPoint(upperLeftCoord).y;
		double imgW = ptLR.x - ptUL.x;
		double imgH = ptLR.y - ptUL.y;

		radarView.setX(imgX);
		radarView.setY(imgY);
		radarView.setFitWidth(imgW);
		radarView.setFitHeight(imgH);

		children.add(radarView);
	}

	private void addScaleView(ObservableList<Node> children) {

		double scaleX = 8; // viewer.widthProperty().get() - 40;
		double scaleY = 250; // viewer.heightProperty().get() - 350;

		scaleView.setX(scaleX);
		scaleView.setY(scaleY);

		children.add(scaleView);
	}
}
