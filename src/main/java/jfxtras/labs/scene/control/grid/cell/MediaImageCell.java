/**
 * MediaImageCell.java
 *
 * Copyright (c) 2011-2014, JFXtras
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the organization nor the
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

package jfxtras.labs.scene.control.grid.cell;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayerBuilder;
import javafx.scene.media.MediaView;
import javafx.scene.media.MediaViewBuilder;
import jfxtras.labs.scene.control.grid.GridCell;

/**
 * 
 * @author Hendrik Ebbers
 * 
 */
public class MediaImageCell extends GridCell<Media> {
	
	private MediaPlayer mediaPlayer;
	
	public MediaImageCell() {
		getStyleClass().add("media-grid-cell");
		itemProperty().addListener(new ChangeListener<Media>() {

			@Override
			public void changed(ObservableValue<? extends Media> arg0,
					Media arg1, Media arg2) {
				getChildren().clear();
				if(mediaPlayer != null) {
					mediaPlayer.stop();
				}
				mediaPlayer = MediaPlayerBuilder.create().media(arg2).build();
				MediaView mediaView = MediaViewBuilder.create().mediaPlayer(mediaPlayer).build();
				mediaView.fitHeightProperty().bind(heightProperty());
				mediaView.fitWidthProperty().bind(widthProperty());
				setGraphic(mediaView);
			}
		});
	}
	
	public void pause() {
		if(mediaPlayer != null) {
			mediaPlayer.pause();
		}
	}
	
	public void play() {
		if(mediaPlayer != null) {
			mediaPlayer.play();
		}
	}
	
	public void stop() {
		if(mediaPlayer != null) {
			mediaPlayer.stop();
		}
	}
}