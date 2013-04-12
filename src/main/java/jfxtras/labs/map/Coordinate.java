package jfxtras.labs.map;

//License: GPL. Copyright 2009 by Stefan Zeller
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * This class encapsulates a Point2D.Double and provide access
 * via <tt>lat</tt> and <tt>lon</tt>.
 *
 * @author Jan Peter Stotz
 *
 */
public class Coordinate implements Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -3681333609178265369L;
	
	private transient Point2D.Double data;

    /**
     * Constructor for the coordinate
     * @param lat
     * @param lon 
     */
    public Coordinate(double lat, double lon) {
        data = new Point2D.Double(lon, lat);
    }

    public double getLat() {
        return data.y;
    }

    public void setLat(double lat) {
        data.y = lat;
    }

    public double getLon() {
        return data.x;
    }

    public void setLon(double lon) {
        data.x = lon;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(data.x);
        out.writeObject(data.y);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        data = new Point2D.Double();
        data.x = (Double) in.readObject();
        data.y = (Double) in.readObject();
    }

    @Override
    public String toString() {
        return "Coordinate[" + data.y + ", " + data.x + "]";
    }
}
