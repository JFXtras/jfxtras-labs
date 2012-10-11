/*
 * Copyright (c) 2012, JFXtras
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *      * Redistributions of source code must retain the above copyright
 *        notice, this list of conditions and the following disclaimer.
 *      * Redistributions in binary form must reproduce the above copyright
 *        notice, this list of conditions and the following disclaimer in the
 *        documentation and/or other materials provided with the distribution.
 *      * Neither the name of the <organization> nor the
 *        names of its contributors may be used to endorse or promote products
 *        derived from this software without specific prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 *  DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jfxtras.labs.scene.control.gauge;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;

import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: hansolo
 * Date: 09.10.12
 * Time: 09:30
 * To change this template use File | Settings | File Templates.
 */
public class SmallRadial extends Gauge {
    private static final String   DEFAULT_STYLE_CLASS = "small-radial";
    private ObjectProperty<Color> valueLabelColor;
    private BooleanProperty       valueLabelVisible;
    private IntegerProperty       noOfDecimals;
    private DoubleProperty        timeToValueInMs;
    private ObjectProperty<Color> frameColor;
    private ObjectProperty<Color> backgroundColor;
    private ObjectProperty<Color> tickMarkColor;
    private ObjectProperty<Color> pointerColor;
    private ObjectProperty<Color> centerKnobColor;
    private ObjectProperty<Color> thresholdLedColor;
    private BooleanProperty       pointerShadowVisible;


    // ******************** Constructors **************************************
    public SmallRadial() {
        this(new GaugeModel(), new StyleModel());
    }

    public SmallRadial(final GaugeModel GAUGE_MODEL) {
        this(GAUGE_MODEL, new StyleModel());
    }

    public SmallRadial(final GaugeModel GAUGE_MODEL, final StyleModel STYLE_MODEL) {
        super(GAUGE_MODEL, STYLE_MODEL);
        setRadialRange(RadialRange.RADIAL_280);
        valueLabelColor       = new SimpleObjectProperty<Color>(Color.BLACK);
        valueLabelVisible     = new SimpleBooleanProperty(true);
        noOfDecimals          = new SimpleIntegerProperty(2);
        frameColor            = new SimpleObjectProperty<Color>(Color.rgb(110, 110, 110));
        backgroundColor       = new SimpleObjectProperty<Color>(Color.rgb(220, 220, 220));
        tickMarkColor         = new SimpleObjectProperty<Color>(Color.BLACK);
        pointerColor          = new SimpleObjectProperty<Color>(Color.RED);
        centerKnobColor       = new SimpleObjectProperty<Color>(Color.rgb(110, 110, 110));
        thresholdLedColor     = new SimpleObjectProperty<Color>(Color.RED);
        pointerShadowVisible  = new SimpleBooleanProperty(false);
        timeToValueInMs       = new SimpleDoubleProperty(1500);
        getStyleClass().add(DEFAULT_STYLE_CLASS);
    }


    // ******************** Methods *******************************************
    @Override public final RadialRange getRadialRange() {
        return RadialRange.RADIAL_280;
    }

    public final Color getValueLabelColor() {
        return valueLabelColor.get();
    }

    public final void setValueLabelColor(final Color LABEL_COLOR) {
        valueLabelColor.set(LABEL_COLOR);
    }

    public final ObjectProperty<Color> labelColorProperty() {
        return valueLabelColor;
    }

    public final boolean isValueLabelVisible() {
        return valueLabelVisible.get();
    }

    public final void setValueLabelVisible(final boolean VALUE_LABEL_VISIBLE) {
        valueLabelVisible.set(VALUE_LABEL_VISIBLE);
    }

    public final BooleanProperty valueLabelVisibleProperty() {
        return valueLabelVisible;
    }

    public final int getNoOfDecimals() {
        return noOfDecimals.get();
    }

    public final void setNoOfDecimals(final int NO_OF_DECIMALS) {
        int number = NO_OF_DECIMALS < 0 ? 0 : (NO_OF_DECIMALS > 5 ? 5 : NO_OF_DECIMALS);
        noOfDecimals.set(number);
    }

    public final ReadOnlyIntegerProperty noOfDecimalsProperty() {
        return noOfDecimals;
    }

    public final void setSections(final Section... SECTION_ARRAY) {
        getGaugeModel().setSections(SECTION_ARRAY);
    }

    public final void setSections(final List<Section> SECTIONS) {
        getGaugeModel().setSections(SECTIONS);
    }

    public final Color getFrameColor() {
        return frameColor.get();
    }

    public final void setFrameColor(final Color FRAME_COLOR) {
        frameColor.set(FRAME_COLOR);
    }

    public final ObjectProperty<Color> frameColorProperty() {
        return frameColor;
    }

    public final Color getBackgroundColor() {
        return backgroundColor.get();
    }

    public final void setBackgroundColor(final Color BACKGROUND_COLOR) {
        backgroundColor.set(BACKGROUND_COLOR);
    }

    public final ObjectProperty<Color> backgroundColorProperty() {
        return backgroundColor;
    }

    public final Color getTickMarkColor() {
        return tickMarkColor.get();
    }

    public final void setTickMarkColor(final Color TICK_MARK_COLOR) {
        tickMarkColor.set(TICK_MARK_COLOR);
    }

    public final ObjectProperty<Color> tickMarkColorProperty() {
        return tickMarkColor;
    }

    public final Color getPointerColor() {
        return pointerColor.get();
    }

    public final void setPointerColor(final Color POINTER_COLOR) {
        pointerColor.set(POINTER_COLOR);
    }

    public final ObjectProperty<Color> pointerColorProperty() {
        return pointerColor;
    }

    public final Color getCenterKnobColor() {
        return centerKnobColor.get();
    }

    public final void setCenterKnobColor(final Color CENTER_KNOB_COLOR) {
        centerKnobColor.set(CENTER_KNOB_COLOR);
    }

    public final ObjectProperty<Color> centerKnobColorProperty() {
        return centerKnobColor;
    }

    public final Color getThresholdLedColor() {
        return thresholdLedColor.get();
    }

    public final void setThresholdLedColor(final Color THRESHOLD_LED_COLOR) {
        thresholdLedColor.set(THRESHOLD_LED_COLOR);
    }

    public final ObjectProperty<Color> thresholdLedColorProperty() {
        return thresholdLedColor;
    }

    public final boolean isPointerShadowVisible() {
        return pointerShadowVisible.get();
    }

    public final void setPointerShadowVisible(final boolean POINTER_SHADOW_VISIBLE) {
        pointerShadowVisible.set(POINTER_SHADOW_VISIBLE);
    }

    public final BooleanProperty pointerShadowVisibleProperty() {
        return pointerShadowVisible;
    }

    public final double getTimeToValueInMs() {
        return timeToValueInMs.get();
    }

    public final void setTimeToValueInMs(final double TIME_TO_VALUE_IN_MS) {
        double value = TIME_TO_VALUE_IN_MS < 10 ? 10 : (TIME_TO_VALUE_IN_MS > 5000 ? 5000 : TIME_TO_VALUE_IN_MS);
        timeToValueInMs.set(value);
    }

    public final DoubleProperty timeToValueInMsProperty() {
        return timeToValueInMs;
    }

    @Override public void setPrefSize(final double WIDTH, final double HEIGHT) {
        final double SIZE = WIDTH < (HEIGHT * 1.0) ? (WIDTH * 1.0) : HEIGHT;
        super.setPrefSize(SIZE, SIZE);
    }

    @Override public void setMinSize(final double WIDTH, final double HEIGHT) {
        double size = WIDTH < (HEIGHT * 1.0) ? (WIDTH * 1.0) : HEIGHT;
        size = size < 30 ? 30 : size;
        super.setPrefSize(size, size);
    }

    @Override public void setMaxSize(final double WIDTH, final double HEIGHT) {
        double size = WIDTH < (HEIGHT * 1.0) ? (WIDTH * 1.0) : HEIGHT;
        size = size > 500 ? 500 : size;
        super.setPrefSize(size, size);
    }
}

