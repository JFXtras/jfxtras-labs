//==============================================================================
//
//  This library is free software; you can redistribute it and/or
//  modify it under the terms of the GNU Lesser General Public
//  License as published by the Free Software Foundation; either
//  version 2.1 of the License, or (at your option) any later version.
//    
//  This library is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
//  Lesser General Public License for more details.
//    
//  You should have received a copy of the GNU Lesser General Public
//  License along with this library; if not, write to the Free Software
//  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//    
//  For more information, please email jsmith.carlsbad@gmail.com
//    
//==============================================================================
package jfxtras.labs.map.tile;

import javafx.scene.image.Image;

/**
 * This class keeps the timestamp and image together.
 * 
 * @author Mario Schroeder
 * 
 */
class TileInfo {

	private long timeStamp;

	private Image image;

	TileInfo(long timeStamp, Image image) {
		this.timeStamp = timeStamp;
		this.image = image;
	}

    /**
     * Returns the time stampe when the image was loaded.
     * @return time in milli seconds
     */
	long getTimeStamp() {
		return timeStamp;
	}

    /**
     * Returns the image for the tile.
     * @return the loaded image
     */
	Image getImage() {
		return image;
	}

}
