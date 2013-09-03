/**
 * MapController.java
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
		map.refereshMap();
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
