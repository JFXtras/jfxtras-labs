/**
 * Gauge.java
 *
 * Copyright (c) 2011-2014, JFXtras
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the organization nor the
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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import javafx.geometry.Point2D;
import javafx.scene.control.Control;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


/**
 * Created by
 * User: hansolo
 * Date: 02.01.12
 * Time: 17:14
 */
public abstract class Gauge extends Control {
    // ******************** Enum definitions **********************************
    public static enum BackgroundDesign {
        DARK_GRAY("background-design-darkgray"),
        SATIN_GRAY("background-design-satingray"),
        LIGHT_GRAY("background-design-lightgray"),
        WHITE("background-design-white"),
        BLACK("background-design-black"),
        BEIGE("background-design-beige"),
        BROWN("background-design-brown"),
        RED("background-design-red"),
        GREEN("background-design-green"),
        BLUE("background-design-blue"),
        ANTHRACITE("background-design-anthracite"),
        MUD("background-design-mud"),
        CARBON("background-design-carbon"),
        STAINLESS("background-design-stainless"),
        //STAINLESS_GRINDED("background-design-stainlessgrinded"),
        BRUSHED_METAL("background-design-brushedmetal"),
        PUNCHED_SHEET("background-design-punchedsheet"),
        //LINEN("background-design-linen"),
        NOISY_PLASTIC("backgroundd-design-noisyplastic"),
        SIMPLE_GRADIENT("background-design-simplegradient"),
        TRANSPARENT("background-design-transparent"),
        CUSTOM("background-design-custom");

        public final String CSS_BACKGROUND;
        public final String CSS_TEXT;

        BackgroundDesign(final String CSS_BACKGROUND) {
            this.CSS_BACKGROUND = CSS_BACKGROUND;
            this.CSS_TEXT = CSS_BACKGROUND + "-text";
        }
    }
    public static enum FrameDesign {
        BLACK_METAL("frame-design-blackmetal"),
        SHINY_METAL("frame-design-shinymetal"),
        CHROME("frame-design-chrome"),
        METAL("frame-design-metal"),
        GLOSSY_METAL("frame-design-glossymetal"),
        DARK_GLOSSY("frame-design-darkglossy"),
        BRASS("frame-design-brass"),
        STEEL("frame-design-steel"),
        GOLD("frame-design-gold"),
        ANTHRACITE("frame-design-anthracite"),
        TILTED_GRAY("frame-design-tiltedgray"),
        TILTED_BLACK("frame-design-tiltedblack"),
        CUSTOM("frame-design-custom");

        public final String CSS;

        FrameDesign(final String CSS) {
            this.CSS = CSS;
        }
    }
    public static enum KnobColor {
            BLACK,
            BRASS,
            SILVER
        }
    public static enum KnobDesign {
        STANDARD,
        PLAIN,
        METAL,
        BIG
    }
    public static enum LcdFont {
        STANDARD,
        LCD,
        BUS,
        PIXEL,
        PHONE_LCD
    }
    public static enum NumberFormat {
        AUTO("0"),
        STANDARD("0"),
        FRACTIONAL("0.0#"),
        SCIENTIFIC("0.##E0"),
        PERCENTAGE("##0.0%");

        private final DecimalFormat DF;

        private NumberFormat(final String FORMAT_STRING) {
            DF = new DecimalFormat(FORMAT_STRING,new DecimalFormatSymbols(Locale.US));
        }

        public String format(final Number NUMBER) {
            return DF.format(NUMBER);
        }
    }
    public static enum NumberSystem {
        DECIMAL("dec"),
        HEXADECIMAL("hex"),
        OCTAL("oct");

        private String text;

        private NumberSystem(final String TEXT) {
            text = TEXT;
        }

        @Override public String toString() {
            return text;
        }
    }
    public static enum PointerType {
        TYPE1,
        TYPE2,
        TYPE3,
        TYPE4,
        TYPE5,
        TYPE6,
        TYPE7,
        TYPE8,
        TYPE9,
        TYPE10,
        TYPE11,
        TYPE12,
        TYPE13,
        TYPE14,
        TYPE15,
        TYPE16
    }
    public static enum RadialRange {
        RADIAL_360(360, 0, 0, new Rectangle(0.4, 0.56, 0.4, 0.12), 0, new Point2D(0.6, 0.4), new Point2D(0.3, 0.4), 1, 0.38),
        RADIAL_300(300, -150, 240, new Rectangle(0.4, 0.56, 0.4, 0.12), 150, new Point2D(0.6, 0.4), new Point2D(0.3, 0.4), 1, 0.38),
        RADIAL_280(280, -140, 280, new Rectangle(0.4, 0.56, 0.4, 0.12), 150, new Point2D(0.6, 0.4), new Point2D(0.3, 0.4), 1, 0.38),
        RADIAL_270(270, -180, 270, new Rectangle(0.4, 0.56, 0.4, 0.12), 180, new Point2D(0.6, 0.4), new Point2D(0.3, 0.4), 1, 0.38),
        RADIAL_180(180, -90, 180, new Rectangle(0.55, 0.56, 0.55, 0.12), 90, new Point2D(0.6, 0.4), new Point2D(0.3, 0.4), 1, 0.38),
        RADIAL_180N(180, -90, 180, new Rectangle(0.55, 0.56, 0.55, 0.12), 90, new Point2D(0.6, 0.35), new Point2D(0.3, 0.35), 1, 0.38),
        RADIAL_180S(180, -90, 180, new Rectangle(0.55, 0.56, 0.55, 0.12), 0, new Point2D(0.6, 0.2), new Point2D(0.3, 0.2), -1, 0.38),
        RADIAL_90(90, -90, 180, new Rectangle(0.55, 0.56, 0.55, 0.12), 91, new Point2D(0.6, 0.4), new Point2D(0.3, 0.4), 1, 0.38),
        RADIAL_90N(90, 315, 225, new Rectangle(0.55, 0.52, 0.55, 0.12), 45, new Point2D(0.6, 0.4), new Point2D(0.3, 0.4), 1, 0.5),
        RADIAL_90W(90, 225, 45, new Rectangle(0.2, 0.58, 0.45, 0.12), 135, new Point2D(0.12, 0.35), new Point2D(0.12, 0.55), 1, 0.5),
        RADIAL_90S(90, -135, 45, new Rectangle(0.55, 0.36, 0.55, 0.12), 225, new Point2D(0.6, 0.5), new Point2D(0.3, 0.5), -1, 0.5),
        RADIAL_90E(90, 135, 225, new Rectangle(0.2, 0.58, 0.45, 0.12), -315, new Point2D(0.78, 0.35), new Point2D(0.78, 0.55), -1, 0.5);

        public final double    ANGLE_RANGE;
        public final double    ROTATION_OFFSET;
        public final double    SECTIONS_OFFSET;
        public final Rectangle LCD_FACTORS;
        public final double    TICKLABEL_ORIENATION_CHANGE_ANGLE;
        public final Point2D   LED_POSITION;
        public final Point2D   USER_LED_POSITION;
        public final double    ANGLE_STEP_SIGN;
        public final double    RADIUS_FACTOR;

        private RadialRange(final double ANGLE_RANGE,
                            final double ROTATION_OFFSET,
                            final double SECTIONS_OFFSET,
                            final Rectangle LCD_FACTORS,
                            final double TICKLABEL_ORIENATION_CHANGE_ANGLE,
                            final Point2D LED_POSITION,
                            final Point2D USER_LED_POSITION,
                            final double ANGLE_STEP_SIGN,
                            final double RADIUS_FACTOR) {
            this.ANGLE_RANGE                       = ANGLE_RANGE;
            this.ROTATION_OFFSET                   = ROTATION_OFFSET;
            this.SECTIONS_OFFSET                   = SECTIONS_OFFSET;
            this.LCD_FACTORS                       = LCD_FACTORS;
            this.TICKLABEL_ORIENATION_CHANGE_ANGLE = TICKLABEL_ORIENATION_CHANGE_ANGLE;
            this.LED_POSITION                      = LED_POSITION;
            this.USER_LED_POSITION                 = USER_LED_POSITION;
            this.ANGLE_STEP_SIGN                   = ANGLE_STEP_SIGN;
            this.RADIUS_FACTOR                     = RADIUS_FACTOR;
        }
    }
    public static enum ThresholdColor {
        RED("-fx-red;", Color.rgb(213, 0, 0)),
        GREEN("-fx-green;", Color.rgb(0, 148, 0)),
        BLUE("-fx-blue;", Color.rgb(0, 120, 220)),
        ORANGE("-fx-orange;", Color.rgb(248, 142, 0)),
        YELLOW("-fx-yellow;", Color.rgb(210, 204, 0)),
        CYAN("-fx-cyan;", Color.rgb(0, 159, 215)),
        MAGENTA("-fx-magenta;", Color.rgb(223, 42, 125)),
        LILA("-fx-lila", Color.rgb(71, 0, 255)),
        WHITE("-fx-white;", Color.rgb(245, 245, 245)),
        GRAY("-fx-gray;", Color.rgb(102, 102, 102)),
        BLACK("-fx-black;", Color.rgb(15, 15, 15)),
        RAITH("-fx-raith;", Color.rgb(65, 143, 193)),
        GREEN_LCD("-fx-green-lcd;", Color.rgb(24, 220, 183)),
        JUG_GREEN("-fx-jug-green;", Color.rgb(90, 183, 0)),
        CUSTOM("-fx-custom;", Color.rgb(0, 195, 97));

        public final String CSS;
        public final Color COLOR;

        ThresholdColor(final String CSS_COLOR, final Color COLOR) {
            this.CSS   = "-fx-threshold: " + CSS_COLOR;
            this.COLOR = COLOR;
        }
    }
    public static enum TickmarkType {
        LINE,
        TRIANGLE
    }
    public static enum TicklabelOrientation {
        NORMAL,
        HORIZONTAL,
        TANGENT
    }
    public static enum Trend {
        UP,
        RISING,
        STEADY,
        FALLING,
        DOWN,
        UNKNOWN;
    }
    

    // ******************** Constructors **************************************
    protected Gauge() {
        
    }    
}
