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
package jfxtras.labs.map.tile;

import java.io.File;
import java.io.IOException;
import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MapTile {

    private static Image delayImage;

    private static Image errorImage;

    private static final String HTTP = "http:";

    private static final double COMPLETE = 1.0;

    private static final int DIM = 256;

    private ImageView imageView;

    protected TileSource source;

    protected int xtile;

    protected int ytile;

    protected int zoom;

    protected String tileLocation;

    public MapTile(TileSource source, int xtile, int ytile, int zoom, Image image) {
        this(source, xtile, ytile, zoom);
        this.imageView.setImage(image);
    }

    public MapTile(TileSource source, int xtile, int ytile, int zoom) {
        imageView = new ImageView();
        this.source = source;
        this.xtile = xtile;
        this.ytile = ytile;
        this.zoom = zoom;
        this.tileLocation = source.getTileUrl(zoom, xtile, ytile);

        if (this.tileLocation.startsWith(HTTP)) {
            loadFromHttp();
        } else {
            loadFromFile();
        }
    }

    private void loadFromHttp() {
        this.imageView.setImage(getDelayImage());
        this.imageView.setFitWidth(DIM);
        this.imageView.setFitHeight(DIM);

        this.loadImage(this.tileLocation);
    }

    private void loadFromFile() {
        File file = new File(this.tileLocation);
        if (file.exists()) {
            this.imageView.setImage(new Image(file.toURI().toString()));
        } else {
            this.imageView.setImage(getErrorImage());
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

    private void loadImage(String url) {

        final Image img = new Image(url, true);

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
                    setImage(img);
                }
            }
        });

    }

    public String getUrl() throws IOException {
        return source.getTileUrl(zoom, xtile, ytile);
    }

    public ImageView getImageView() {
        return imageView;
    }

    public TileSource getSource() {
        return source;
    }

    /**
     * @return tile number on the x axis of this tile
     */
    public int getXtile() {
        return xtile;
    }

    /**
     * @return tile number on the y axis of this tile
     */
    public int getYtile() {
        return ytile;
    }

    public int getZoom() {
        return zoom;
    }

    public void setImage(Image image) {
        this.imageView.setImage(image);
    }
    
}
