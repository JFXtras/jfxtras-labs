/*
 * (C) Copyright Dilax Intelcom GmbH.
 * 
 *  All Rights Reserved.
 */
package jfxtras.labs.map;

/**
 * This class formats the string which contains coordinates.
 * 
 * @author Mario Schröder
 */
public class CoordinateStringFormater {
    
    /**
     * Build the string which displays the current location of the cursor.
     * @param coordinate the value of coordinate
     * @return string which will be displayed on the map
     */
    protected String format(Coordinate coordinate){
        return format(coordinate.getLatitude(), coordinate.getLongitude());
    }
    
    /**
     * Build the string which displays the current location of the cursor.
     * @param lat the value of latitude
     * @param lon the value of the longitude
     * @return string which will be displayed on the map
     */
    protected String format(double lat, double lon) {
        StringBuilder builder = new StringBuilder();
        builder.append("Latitude: ").append(String.format("%2.5f", lat));
        builder.append(" Longitude: ").append(String.format("%3.6f", lon));
        return builder.toString();
    }
}
