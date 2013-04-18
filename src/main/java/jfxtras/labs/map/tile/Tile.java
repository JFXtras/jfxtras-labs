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
package jfxtras.labs.map.tile;

import java.io.File;

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

    private String tileLocation;
    
    private SimpleBooleanProperty imageLoadedProperty;

    public Tile(String tileLocation, Image image) {
        this(tileLocation);
        this.imageView.setImage(image);
    }

    public Tile(String tileLocation) {
        imageView = new ImageView();
        this.tileLocation = tileLocation;
        imageLoadedProperty = new SimpleBooleanProperty();
    }
    
    
    public void loadImage(){
        imageLoadedProperty.set(false);
    	if (tileLocation.startsWith(HTTP)) {
            loadFromHttp();
        } else {
            loadFromFile();
        }
    }

    private void loadFromHttp() {
        imageView.setImage(getDelayImage());
        imageView.setFitWidth(DIM);
        imageView.setFitHeight(DIM);

        final Image img = new Image(tileLocation, true);

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
        File file = new File(tileLocation);
        if (file.exists()) {
            Image image = new Image(file.toURI().toString());
            setLoadedImage(image);
        } else {
            imageView.setImage(getErrorImage());
        }
    }

    private Image getDelayImage(){
    	if(delayImage == null){
    		delayImage = new Image(getClass().getResourceAsStream("wait_tile.png"));
    	}
    	return delayImage;
    }
    
    private Image getErrorImage() {
		if(errorImage == null){
			errorImage = new Image(getClass().getResourceAsStream("error.png"));
		}
		return errorImage;
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

    
    public SimpleBooleanProperty imageLoadedProperty() {
        return imageLoadedProperty;
    }

    private void setLoadedImage(Image image) {
        imageView.setImage(image);
        imageLoadedProperty.set(true);
    }
}
