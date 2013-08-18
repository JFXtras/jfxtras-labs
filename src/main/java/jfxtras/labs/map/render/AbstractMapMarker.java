package jfxtras.labs.map.render;

import java.awt.Point;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

import jfxtras.labs.map.Coordinate;
import jfxtras.labs.map.Moveable;
import jfxtras.labs.map.MapTilesourceable;

import static jfxtras.labs.map.CoordinatesConverter.*;

/**
 *
 * @author Mario Schroeder
 */
abstract class AbstractMapMarker implements MapMarkable {

    private static final int SHADOW_OFFSET = 10;

    private double lat;

    private double lon;

    AbstractMapMarker() {
    }

    AbstractMapMarker(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    public double getLat() {
        return lat;
    }

    @Override
    public double getLon() {
        return lon;
    }

    @Override
    public void render(MapTilesourceable mapController) {
        
        Point postion = getPoint(mapController);
        if(postion != null){
            Group tilesGroup = mapController.getTilesGroup();
            ObservableList<Node> children = tilesGroup.getChildren();
			List<? extends Node> nodes = createChildren(postion);
			for(Node node : nodes){
				if(!children.contains(node)){
					children.add(node);
				}
			}
        }
    }
    
    private Point getPoint(Moveable mapController){
    	Coordinate coordinate = new Coordinate(lat, lon);
    	return toMapPoint(coordinate, mapController);
    }
    
    abstract List<? extends Node> createChildren(Point position);
    
    public void setLat(double val) {
        this.lat = val;
    }

    public void setLon(double val) {
        this.lon = val;
    }

    protected DropShadow createShadow(Color color) {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(SHADOW_OFFSET);
        dropShadow.setOffsetY(SHADOW_OFFSET);
        dropShadow.setColor(color);
        return dropShadow;
    }

    @Override
    public String toString() {
        return getClass().getName() + " " + getLat() + " " + getLon();
    }
}
