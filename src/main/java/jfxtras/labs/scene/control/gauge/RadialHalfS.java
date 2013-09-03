/**
 * RadialHalfS.java
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

package jfxtras.labs.scene.control.gauge;

/**
 * Created by
 * User: hansolo
 * Date: 26.01.12
 * Time: 17:30
 */
public class RadialHalfS extends Gauge {
    private static final String DEFAULT_STYLE_CLASS = "radial-half-south";
    public enum ForegroundType {
        TYPE1,
        TYPE2,
        TYPE3,
        TYPE4,
        TYPE5
    }


    // ******************** Constructors **************************************
    public RadialHalfS() {
        this(new GaugeModel(), new StyleModel());
    }

    public RadialHalfS(final GaugeModel GAUGE_MODEL) {
        this(GAUGE_MODEL, new StyleModel());
    }

    public RadialHalfS(final StyleModel STYLE_MODEL) {
        this(new GaugeModel(), STYLE_MODEL);
    }

    public RadialHalfS(final GaugeModel GAUGE_MODEL, final StyleModel STYLE_MODEL) {
        super(GAUGE_MODEL, STYLE_MODEL);
        setRadialRange(Gauge.RadialRange.RADIAL_180S);

        getStyleClass().setAll(DEFAULT_STYLE_CLASS);
    }


    // ******************** Methods *******************************************
    @Override public final void setPrefSize(final double WIDTH, final double HEIGHT) {
        final double SIZE  = WIDTH == 0 ? 200 : WIDTH;
        super.setPrefSize(SIZE, SIZE / 1.5384615385);
    }

    @Override public final void setRadialRange(final RadialRange RADIAL_RANGE) {
        super.setRadialRange(RadialRange.RADIAL_180S);
    }
}
