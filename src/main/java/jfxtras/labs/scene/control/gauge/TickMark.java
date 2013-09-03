/**
 * TickMark.java
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

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;


/**
 * Created by
 * User: hansolo
 * Date: 31.07.12
 * Time: 06:03
 */
public class TickMark {
    // ******************** Enum definitions **********************************
    public static enum Type {
        MINOR(0.0015, 0.3, 0.0133333333, 1),
        MEDIUM(0.0025, 0.5, 0.02, 1),
        MAJOR(0.005, 1.0, 0.03, 1);

        public double strokeWidthFactor;
        public double minStrokeWidth;
        public double strokeLengthFactor;
        public double minStrokeLength;


        private Type(final double STROKE_WIDTH_FACTOR, final double MIN_STROKE_WIDTH, final double STROKE_LENGTH_FACTOR, final double MIN_STROKE_LENGTH) {
            strokeWidthFactor  = STROKE_WIDTH_FACTOR;
            minStrokeWidth     = MIN_STROKE_WIDTH;
            strokeLengthFactor = STROKE_LENGTH_FACTOR;
            minStrokeLength    = MIN_STROKE_LENGTH;
        }
    }
    public static enum Indicator {
        LINE,
        CIRCLE,
        TRIANGLE,
        SQUARE
    }
    public static enum TickLabelOrientation {
        NORMAL,
        HORIZONTAL,
        TANGENT
    }

    // ******************** Variable definitions ******************************
    private ObjectProperty<Type>                 type;
    private ObjectProperty<Indicator>            indicator;
    private ObjectProperty<Color>                indicatorColor;
    private BooleanProperty                      indicatorVisible;
    private StringProperty                       label;
    private ObjectProperty<Color>                labelColor;
    private BooleanProperty                      labelVisible;
    private ObjectProperty<Font>                 labelFont;
    private DoubleProperty                       labelFontSizeFactor;
    private ObjectProperty<TickLabelOrientation> tickLabelOrientation;


    // ******************** Constructors **************************************
    public TickMark() {
        this(Type.MINOR, Indicator.LINE, Color.BLACK, "", Color.BLACK, TickLabelOrientation.NORMAL);
    }

    public TickMark(final Type TYPE, final Indicator INDICATOR, final String LABEL) {
        this(TYPE, INDICATOR, Color.BLACK, LABEL, Color.BLACK, TickLabelOrientation.NORMAL);
    }

    public TickMark(final Type TYPE, final Indicator INDICATOR, final Color INDICATOR_COLOR, final String LABEL, final Color LABEL_COLOR, final TickLabelOrientation TICK_LABEL_ORIENTATION) {
        type                 = new SimpleObjectProperty<Type>(TYPE);
        indicator            = new SimpleObjectProperty<Indicator>(INDICATOR);
        indicatorColor       = new SimpleObjectProperty<Color>(INDICATOR_COLOR);
        indicatorVisible     = new SimpleBooleanProperty(true);
        label                = new SimpleStringProperty(LABEL);
        labelColor           = new SimpleObjectProperty<Color>(LABEL_COLOR);
        labelVisible         = new SimpleBooleanProperty(TYPE != Type.MINOR ? true : false);
        labelFont            = new SimpleObjectProperty<Font>(Font.font("Verdana", FontWeight.NORMAL, 8));
        labelFontSizeFactor  = new SimpleDoubleProperty(0.035);
        tickLabelOrientation = new SimpleObjectProperty<TickLabelOrientation>(TICK_LABEL_ORIENTATION);
    }


    // ******************** Methods *******************************************
    public final Type getType() {
        return type.get();
    }

    public final void setType(final Type TYPE) {
        type.set(TYPE);
    }

    public final ObjectProperty<Type> typeProperty() {
        return type;
    }

    public final Indicator getIndicator() {
        return indicator.get();
    }

    public final void setIndicator(final Indicator INDICATOR) {
        indicator.set(INDICATOR);
    }

    public final ObjectProperty<Indicator> indicatorProperty() {
        return indicator;
    }

    public final Color getIndicatorColor() {
        return indicatorColor.get();
    }

    public final void setIndicatorColor(final Color INDICATOR_COLOR) {
        indicatorColor.set(INDICATOR_COLOR);
    }

    public final ObjectProperty<Color> indicatorColorProperty() {
        return indicatorColor;
    }

    public final boolean isIndicatorVisible() {
        return indicatorVisible.get();
    }

    public final void setIndicatorVisible(final boolean INDICATOR_VISIBLE) {
        indicatorVisible.set(INDICATOR_VISIBLE);
    }

    public final BooleanProperty indicatorVisibleProperty() {
        return indicatorVisible;
    }

    public final String getLabel() {
        return label.get();
    }

    public final void setLabel(final String LABEL) {
        label.set(LABEL);
    }

    public final StringProperty labelProperty() {
        return label;
    }

    public final Color getLabelColor() {
        return labelColor.get();
    }

    public final void setLabelColor(final Color LABEL_COLOR) {
        labelColor.set(LABEL_COLOR);
    }

    public final ObjectProperty<Color> labelColorProperty() {
        return labelColor;
    }

    public final boolean isLabelVisible() {
        return labelVisible.get();
    }

    public final void setLabelVisible(final boolean LABEL_VISIBLE) {
        labelVisible.set(LABEL_VISIBLE);
    }

    public final BooleanProperty labelVisibleProperty() {
        return labelVisible;
    }

    public final Font getLabelFont() {
        return labelFont.get();
    }

    public final void setLabelFont(final Font LABEL_FONT) {
        labelFont.set(LABEL_FONT);
    }

    public final ObjectProperty<Font> labelFontProperty() {
        return labelFont;
    }

    public final double getLabelFontSizeFactor() {
        return labelFontSizeFactor.get();
    }

    public final void setLabelFontSizeFactor(final double LABEL_FONT_SIZE_FACTOR) {
        labelFontSizeFactor.set(LABEL_FONT_SIZE_FACTOR < 0 ? 0.035 : (LABEL_FONT_SIZE_FACTOR > 1 ? 0.035 : LABEL_FONT_SIZE_FACTOR));
    }

    public final DoubleProperty labelFontSizeFactorProperty() {
        return labelFontSizeFactor;
    }

    public final TickLabelOrientation getTickLabelOrientation() {
        return tickLabelOrientation.get();
    }

    public final void setTickLabelOrientation(final TickLabelOrientation TICK_LABEL_ORIENTATION) {
        tickLabelOrientation.set(TICK_LABEL_ORIENTATION);
    }

    public final ObjectProperty<TickLabelOrientation> tickLabelOrienationObjectProperty() {
        return tickLabelOrientation;
    }
}
