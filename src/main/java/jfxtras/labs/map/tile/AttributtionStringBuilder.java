/**
 * AttributtionStringBuilder.java
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

package jfxtras.labs.map.tile;

import jfxtras.labs.map.Coordinate;

/**
 * Builder for attributions.
 * @author Mario Schroeder
 */
public class AttributtionStringBuilder {

    private StringBuilder builder;

    private int zoom;

    private Coordinate topLeft, bottomRight;

    public AttributtionStringBuilder(int zoom, Coordinate topLeft, Coordinate bottomRight) {
        builder = new StringBuilder();
        this.zoom = zoom;
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }

    public void append(Attribution attr) {
        
        if(attr.getMaxZoom() == Integer.MAX_VALUE){
            appendText(attr);
            
        } else if (zoom <= attr.getMaxZoom() && zoom >= attr.getMinZoom()) {
            if (isMaxLongitudeGreater(attr) && isMinLongitudeSmaller(attr)
                && isMinLatitudeSmaller(attr) && isMaxLatitudeGreater(attr)) {

                appendText(attr);
            }
        }
    }

    private boolean isMaxLongitudeGreater(Attribution attr) {
        return topLeft.getLongitude() < attr.getMax().getLongitude();
    }

    private boolean isMinLongitudeSmaller(Attribution attr) {
        return bottomRight.getLongitude() > attr.getMin().getLongitude();
    }

    private boolean isMinLatitudeSmaller(Attribution attr) {
        return topLeft.getLatitude() > attr.getMin().getLatitude();
    }

    private boolean isMaxLatitudeGreater(Attribution attr) {
        return bottomRight.getLatitude() < attr.getMax().getLatitude();
    }
    
    @Override
    public String toString() {
        return builder.toString();
    }

    private void appendText(Attribution attr) {
        builder.append(attr.getText()).append(" ");
    }
}
