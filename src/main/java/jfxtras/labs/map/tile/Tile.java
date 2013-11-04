/**
 * Tile.java
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

package jfxtras.labs.map.tile;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.animation.AnimationTimer;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * This class is a wrapper for the image for a map tile.<br/>
 * It is also responsible for loading the image from the source.
 * 
 * @author jsmith
 *
 */
public class Tile {

    private static volatile Image delayImage;

    private static volatile Image errorImage;

    private static final String HTTP = "http:";

    private static final double COMPLETE = 1.0;

    private static final int DIM = 256;

    private ImageView imageView;

    private String location;
    
    private SimpleBooleanProperty imageLoadedProperty;

    public Tile(String tileLocation, Image image) {
        this(tileLocation);
        this.imageView.setImage(image);
    }

    public Tile(String tileLocation) {
        imageView = new ImageView();
        this.location = tileLocation;
        imageLoadedProperty = new SimpleBooleanProperty();
    }
    
    
    public void loadImage(){
        imageLoadedProperty.set(false);
    	if (location.startsWith(HTTP)) {
            loadFromHttp();
        } else {
            loadFromFile();
        }
    }

    private void loadFromHttp() {
        imageView.setImage(getDelayImage());
        imageView.setFitWidth(DIM);
        imageView.setFitHeight(DIM);

        final Image img = new Image(location, true);

        //You can add a specific action when each frame is started.
        final AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                rotateImageView();
            }
        };

        timer.start();

        img.progressProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue ov, Number old_val, Number new_val) {
                if (new_val.doubleValue() == COMPLETE) {
                    timer.stop();
                    resetImageView();
                    setLoadedImage(img);
                }
            }
        });
    }

    private void loadFromFile() {
        File file = new File(location);
        if (file.exists()) {
            Image image = new Image(file.toURI().toString());
            setLoadedImage(image);
        }
    }

    private Image getDelayImage(){
    	if(delayImage == null){
    		delayImage = new Image(getClass().getResourceAsStream("wait_tile.png"));
    	}
    	return delayImage;
    }

	private void rotateImageView() {
        double angle = imageView.getRotate();
        angle += 1;
        imageView.setRotate(angle);
    }

    private void resetImageView() {
        imageView.setRotate(0);
    }

    public ImageView getImageView() {
        return imageView;
    }

    public String getLocation() {
		return location;
	}

	public SimpleBooleanProperty imageLoadedProperty() {
        return imageLoadedProperty;
    }

    private void setLoadedImage(Image image) {
        imageView.setImage(image);
        imageLoadedProperty.set(true);
    }
}
