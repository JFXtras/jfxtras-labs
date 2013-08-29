package jfxtras.labs.map;

import javafx.util.Builder;
import jfxtras.labs.map.tile.TileSourceFactory;
import jfxtras.labs.map.tile.osm.OsmTileSourceFactory;
import jfxtras.labs.map.tile.osm.OsmType;

/**
 * 
 * @author Mario Schroeder
 *
 */
public class MapBuilder implements Builder<MapPane>{

	@Override
	public MapPane build() {
		
		TileSourceFactory<OsmType> factory = new OsmTileSourceFactory();
        OsmType osmType = OsmType.Mapnik;

        MapPane map = new MapPane(factory.create(osmType));
        ZoomControlFactory zoomControlFactory = new ZoomControlFactory();
        map.getChildren().add(zoomControlFactory.create(map));
        
		return map;
	}
	
}