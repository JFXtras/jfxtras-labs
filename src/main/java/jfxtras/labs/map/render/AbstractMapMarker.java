package jfxtras.labs.map.render;

import java.awt.Point;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

import jfxtras.labs.map.MapControlable;

/**
 *
 * @author Mario Schröder
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
    public void render(MapControlable mapController) {
        
        Point postion = mapController.getMapPoint(lat, lon, true);
        if(postion != null){
            doRender(mapController.getTilesGroup(), postion);
        }
    }
    
    abstract void doRender(Group group, Point position);
    
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
