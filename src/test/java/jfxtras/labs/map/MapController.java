package jfxtras.labs.map;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import jfxtras.labs.map.tile.TileSource;
import jfxtras.labs.map.tile.TileSourceFactory;
import jfxtras.labs.map.tile.bing.BingTileSourceFactory;
import jfxtras.labs.map.tile.bing.BingType;
import jfxtras.labs.map.tile.osm.OsmTileSourceFactory;
import jfxtras.labs.map.tile.osm.OsmType;

/**
 * 
 * @author Mario Schroeder
 *
 */
public class MapController {
	
	@FXML
	private ComboBox mapType;
	
	@FXML
	private MapPane map;
	
	@FXML
	public void mapTypeChanged(ActionEvent event){
		Object value = mapType.getValue();
		TileSource tileSource = createTileSource(value);
		map.setTileSource(tileSource);
		
		updateStyleSheet(value);
	}
	
	@FXML
	public void centerMap(ActionEvent event){

		map.centerMap();
	}
	
	@FXML
	public void refreshMap(ActionEvent event){
		throw new UnsupportedOperationException("Not yet implemented");
	}

	private TileSource createTileSource(Object value){
		
        TileSource tileSource;
        if (value instanceof BingType) {
            String key = ApiKeys.Bing.toString();
            TileSourceFactory<BingType> fac = new BingTileSourceFactory(key);
            BingType type = (BingType)value;
            tileSource = fac.create(type);

        } else {
        	TileSourceFactory<OsmType> factory = new OsmTileSourceFactory();
            OsmType type = (OsmType)value;
            tileSource = factory.create(type);
        }
        
        return tileSource;
	}
	
	private void updateStyleSheet(Object value) {
		String css = "map_pane.css";
		if(value.equals(BingType.Aerial)){
			css = "img_map_pane.css";
		}
		map.getStylesheets().clear();
		map.getStylesheets().add(getClass().getResource(css)
	            .toExternalForm());
	}
}
