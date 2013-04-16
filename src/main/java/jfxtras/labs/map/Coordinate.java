package jfxtras.labs.map;

//License: GPL. Copyright 2009 by Stefan Zeller
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * This class encapsulates a Point2D.Double and provide access
 * via <tt>latitude</tt> and <tt>longitude</tt>.
 *
 * @author Jan Peter Stotz
 *
 */
public class Coordinate{
	
	private transient Point2D.Double point;

    /**
     * Constructor for the coordinate
     * @param lat
     * @param lon 
     */
    public Coordinate(double lat, double lon) {
        point = new Point2D.Double(lon, lat);
    }

    public double getLatitude() {
        return point.y;
    }

    public void setLatitude(double lat) {
        point.y = lat;
    }

    public double getLongitude() {
        return point.x;
    }

    public void setLongitude(double lon) {
        point.x = lon;
    }

    @Override
    public String toString() {
        return point.toString();
    }
}
