/*
 * Copyright (c) 2012, JFXtras
 *   All rights reserved.
 *
 *   Redistribution and use in source and binary forms, with or without
 *   modification, are permitted provided that the following conditions are met:
 *       * Redistributions of source code must retain the above copyright
 *         notice, this list of conditions and the following disclaimer.
 *       * Redistributions in binary form must reproduce the above copyright
 *         notice, this list of conditions and the following disclaimer in the
 *         documentation and/or other materials provided with the distribution.
 *       * Neither the name of the <organization> nor the
 *         names of its contributors may be used to endorse or promote products
 *         derived from this software without specific prior written permission.
 *
 *   THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *   ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *   WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *   DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 *   DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *   (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *   LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *   ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *   (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *   SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jfxtras.labs.scene.control.gauge;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;

import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: hansolo
 * Date: 18.09.12
 * Time: 10:21
 * To change this template use File | Settings | File Templates.
 */
public abstract class SimpleGauge extends Gauge {
    private ObjectProperty<Color> barFrameColor;
    private ObjectProperty<Color> barBackgroundColor;
    private ObjectProperty<Color> barColor;
    private DoubleProperty        barWidth;
    private DoubleProperty        valueLabelFontSize;
    private DoubleProperty        unitLabelFontSize;
    private ObjectProperty<Color> valueLabelColor;
    private ObjectProperty<Color> unitLabelColor;
    private BooleanProperty       valueLabelVisible;
    private BooleanProperty       unitLabelVisible;
    private IntegerProperty       noOfDecimals;
    private BooleanProperty       minLabelVisible;
    private BooleanProperty       maxLabelVisible;
    private DoubleProperty        minMaxLabelFontSize;
    private ObjectProperty<Color> minLabelColor;
    private ObjectProperty<Color> maxLabelColor;
    private BooleanProperty       roundedBar;
    private DoubleProperty        timeToValueInMs;
    private StringProperty        unit;
    private BooleanProperty       canvasMode;


    // ******************** Constructors **************************************
    protected SimpleGauge() {
        this(new GaugeModel(), new StyleModel());
    }

    protected SimpleGauge(final GaugeModel GAUGE_MODEL) {
        this(GAUGE_MODEL, new StyleModel());
    }

    protected SimpleGauge(final GaugeModel GAUGE_MODEL, final StyleModel STYLE_MODEL) {
        super(GAUGE_MODEL, STYLE_MODEL);
        barColor            = new SimpleObjectProperty<Color>(Color.rgb(178, 177, 212));
        barFrameColor       = new SimpleObjectProperty<Color>(Color.rgb(100, 100, 100));
        barBackgroundColor  = new SimpleObjectProperty<Color>(Color.rgb(234, 234, 234));
        barWidth            = new SimpleDoubleProperty(20);
        valueLabelFontSize  = new SimpleDoubleProperty(36);
        unitLabelFontSize   = new SimpleDoubleProperty(20);
        valueLabelColor     = new SimpleObjectProperty<Color>(Color.BLACK);
        unitLabelColor      = new SimpleObjectProperty<Color>(Color.BLACK);
        valueLabelVisible   = new SimpleBooleanProperty(true);
        unitLabelVisible    = new SimpleBooleanProperty(true);
        noOfDecimals        = new SimpleIntegerProperty(2);
        minLabelVisible     = new SimpleBooleanProperty(false);
        maxLabelVisible     = new SimpleBooleanProperty(false);
        minMaxLabelFontSize = new SimpleDoubleProperty(10);
        minLabelColor       = new SimpleObjectProperty<Color>(Color.BLACK);
        maxLabelColor       = new SimpleObjectProperty<Color>(Color.BLACK);
        roundedBar          = new SimpleBooleanProperty(true);
        timeToValueInMs     = new SimpleDoubleProperty(1500);
        unit                = new SimpleStringProperty(GAUGE_MODEL.getUnit());
        canvasMode          = new SimpleBooleanProperty(false);
    }


    // ******************** Methods *******************************************
    public final Color getBarColor() {
        return barColor.get();
    }

    public final void setValueColor(final Color BAR_COLOR) {
        barColor.set(BAR_COLOR);
    }

    public final ObjectProperty<Color> barColorProperty() {
        return barColor;
    }

    public final Color getBarFrameColor() {
        return barFrameColor.get();
    }

    public final void setBarFrameColor(final Color BAR_FRAME_COLOR) {
        barFrameColor.set(BAR_FRAME_COLOR);
    }

    public final ObjectProperty<Color> barFrameColorProperty() {
        return barFrameColor;
    }

    public final Color getBarBackgroundColor() {
        return barBackgroundColor.get();
    }

    public final void setBarBackgroundColor(final Color BAR_BACKGROUND_COLOR) {
        barBackgroundColor.set(BAR_BACKGROUND_COLOR);
    }

    public final ObjectProperty<Color> barBackgroundColorProperty() {
        return barBackgroundColor;
    }

    public final double getBarWidth() {
        return barWidth.get();
    }

    public final void setBarWidth(final double BAR_WIDTH) {
        double width = BAR_WIDTH < 1 ? 1 : (BAR_WIDTH > 100 ? 100 : BAR_WIDTH);
        barWidth.set(width);
    }

    public final DoubleProperty barWidthProperty() {
        return barWidth;
    }

    public final double getValueLabelFontSize() {
        return valueLabelFontSize.get();
    }

    public final void setValueLabelFontSize(final double VALUE_LABEL_FONT_SIZE) {
        double size = VALUE_LABEL_FONT_SIZE < 8 ? 8 : (VALUE_LABEL_FONT_SIZE > 52 ? 52 : VALUE_LABEL_FONT_SIZE);
        valueLabelFontSize.set(size);
    }

    public final ReadOnlyDoubleProperty valueLabelFontSizeProperty() {
        return valueLabelFontSize;
    }

    public final double getUnitLabelFontSize() {
        return unitLabelFontSize.get();
    }

    public final void setUnitLabelFontSize(final double UNIT_LABEL_FONT_SIZE) {
        double size = UNIT_LABEL_FONT_SIZE < 8 ? 8 : (UNIT_LABEL_FONT_SIZE > 52 ? 52 : UNIT_LABEL_FONT_SIZE);
        unitLabelFontSize.set(size);
    }

    public final DoubleProperty unitLabelFontSizeProperty() {
        return unitLabelFontSize;
    }

    public final Color getValueLabelColor() {
        return valueLabelColor.get();
    }

    public final void setValueLabelColor(final Color VALUE_LABEL_COLOR) {
        valueLabelColor.set(VALUE_LABEL_COLOR);
    }

    public final ObjectProperty<Color> valueLabelColorProperty() {
        return valueLabelColor;
    }

    public final Color getUnitLabelColor() {
        return unitLabelColor.get();
    }

    public final void setUnitLabelColor(final Color UNIT_LABEL_COLOR) {
        unitLabelColor.set(UNIT_LABEL_COLOR);
    }

    public final ObjectProperty<Color> unitLabelColorProperty() {
        return unitLabelColor;
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

    public final boolean isUnitLabelVisible() {
        return unitLabelVisible.get();
    }

    public final void setUnitLabelVisible(final boolean UNIT_LABEL_VISIBLE) {
        unitLabelVisible.set(UNIT_LABEL_VISIBLE);
    }

    public final BooleanProperty unitLabelVisibleProperty() {
        return unitLabelVisible;
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

    public final boolean isMinLabelVisible() {
        return minLabelVisible.get();
    }

    public final void setMinLabelVisible(final boolean MIN_LABEL_VISIBLE) {
        minLabelVisible.set(MIN_LABEL_VISIBLE);
    }

    public final BooleanProperty minLabelVisibleProperty() {
        return minLabelVisible;
    }

    public final boolean isMaxLabelVisible() {
        return maxLabelVisible.get();
    }

    public final void setMaxLabelVisible(final boolean MAX_LABEL_VISIBLE) {
        maxLabelVisible.set(MAX_LABEL_VISIBLE);
    }

    public final BooleanProperty maxLabelVisibleProperty() {
        return maxLabelVisible;
    }

    public final double getMinMaxLabelFontSize() {
        return minMaxLabelFontSize.get();
    }

    public final void setMinMaxLabelFontSize(final double MIN_MAX_LABEL_FONT_SIZE) {
        double size = MIN_MAX_LABEL_FONT_SIZE < 8 ? 8 : (MIN_MAX_LABEL_FONT_SIZE > 52 ? 52 : MIN_MAX_LABEL_FONT_SIZE);
        minMaxLabelFontSize.set(size);
    }

    public final DoubleProperty minMaxLabelFontSizeProperty() {
        return minMaxLabelFontSize;
    }

    public final Color getMinLabelColor() {
        return minLabelColor.get();
    }

    public final void setMinLabelColor(final Color MIN_LABEL_COLOR) {
        minLabelColor.set(MIN_LABEL_COLOR);
    }

    public final ObjectProperty<Color> minLabelColorProperty() {
        return minLabelColor;
    }

    public final Color getMaxLabelColor() {
        return maxLabelColor.get();
    }

    public final void setMaxLabelColor(final Color MAX_LABEL_COLOR) {
        maxLabelColor.set(MAX_LABEL_COLOR);
    }

    public final ObjectProperty<Color> maxLabelColorProperty() {
        return maxLabelColor;
    }

    public final boolean isRoundedBar() {
        return roundedBar.get();
    }

    public final void setRoundedBar(final boolean ROUNDED_BAR) {
        roundedBar.set(ROUNDED_BAR);
    }

    public final BooleanProperty roundedBarProperty() {
        return roundedBar;
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

    public final boolean isCanvasMode() {
        return canvasMode.get();
    }

    public final void setCanvasMode(final boolean CANVAS_MODE) {
        canvasMode.set(CANVAS_MODE);
    }

    public final BooleanProperty canvasModeProperty() {
        return canvasMode;
    }
}
