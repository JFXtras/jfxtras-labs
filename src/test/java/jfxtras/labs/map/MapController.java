package jfxtras.labs.map;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import jfxtras.labs.map.tile.BingTileSourceFactory;
import jfxtras.labs.map.tile.BingType;
import jfxtras.labs.map.tile.OsmTileSourceFactory;
import jfxtras.labs.map.tile.OsmType;
import jfxtras.labs.map.tile.TileSource;
import jfxtras.labs.map.tile.TileSourceFactory;

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
		TileSource tileSource = createTileSource(mapType.getValue());
		map.setTileSource(tileSource);
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
}
