package jfxtras.labs.map;

import javafx.util.Builder;
import jfxtras.labs.map.tile.OsmTileSourceFactory;
import jfxtras.labs.map.tile.OsmType;
import jfxtras.labs.map.tile.TileSourceFactory;

/**
 * 
 * @author Mario Schroeder
 *
 */
public class MapPaneBuilder implements Builder<MapPane>{

	@Override
	public MapPane build() {
		
		TileSourceFactory<OsmType> factory = new OsmTileSourceFactory();
        OsmType osmType = OsmType.Mapnik;

        MapPane mapPane = new MapPane(factory.create(osmType));
        ZoomControlFactory zoomControlFactory = new ZoomControlFactory();
        mapPane.getChildren().add(zoomControlFactory.create(mapPane));
        
		return mapPane;
	}
	
}