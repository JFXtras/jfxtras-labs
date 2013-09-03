/**
 * MapWeatherRadar.java
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
import javafx.util.Duration;

import jfxtras.labs.map.render.MapOverlayable;

import static jfxtras.labs.map.CoordinatesConverter.*;

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
	public void render(MapTilesourceable viewer) {

		ObservableList<Node> children = viewer.getTilesGroup().getChildren();
		addRadarView(viewer, children);
		addScaleView(children);

	}

	private void addRadarView(Moveable viewer,
			ObservableList<Node> children) {

		Point ptUL = toMapPoint(upperLeftCoord, viewer);
		Point ptLR = toMapPoint(lowerRightCoord, viewer);

		double imgX = toMapPoint(upperLeftCoord, viewer).x;
		double imgY = toMapPoint(upperLeftCoord, viewer).y;
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
