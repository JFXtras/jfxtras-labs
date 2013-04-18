package jfxtras.labs.map.tile;

import javafx.scene.image.Image;

class BingTileSource extends DefaultTileSource {

    
    public BingTileSource(String name, String base_url) {
        super(name, base_url);
        
       
    }
    
    @Override
    public Image getAttributionImage() {
        return new Image(getClass().getResourceAsStream("bing_maps.png"));
    }

}