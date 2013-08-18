package jfxtras.labs.map;

/**
 * This interface defines methods to update the location information 
 * of the cursor.
 * 
 * @author Mario Schroeder
 *
 */
public interface CursorLocationable extends Moveable{
	
	void setCursorLocationText(double x, double y);

    void adjustCursorLocationText();

}
