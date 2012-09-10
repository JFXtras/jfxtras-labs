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

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;

import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: hansolo
 * Date: 10.09.12
 * Time: 15:16
 * To change this template use File | Settings | File Templates.
 */
public class SimpleGauge extends Gauge {
    private static final String   DEFAULT_STYLE_CLASS = "simple-gauge";
    private ObjectProperty<Color>      barBackgroundColor;
    private ObjectProperty<Color>      barColor;
    private DoubleProperty             barWidth;
    private DoubleProperty             labelFontSize;
    private DoubleProperty             unitFontSize;
    private ObjectProperty<Color>      labelColor;
    private ObjectProperty<Color>      unitColor;
    private IntegerProperty            noOfDecimals;


    // ******************** Constructors **************************************
    public SimpleGauge() {
        this(new GaugeModel());
    }

    public SimpleGauge(final GaugeModel MODEL) {
        barColor              = new SimpleObjectProperty<Color>(Color.rgb(178, 177, 212));
        barBackgroundColor    = new SimpleObjectProperty<Color>(Color.rgb(234, 234, 234));
        barWidth              = new SimpleDoubleProperty(20);
        labelFontSize         = new SimpleDoubleProperty(36);
        unitFontSize          = new SimpleDoubleProperty(36);
        labelColor            = new SimpleObjectProperty<Color>(Color.BLACK);
        unitColor             = new SimpleObjectProperty<Color>(Color.BLACK);
        noOfDecimals          = new SimpleIntegerProperty(2);
        init();
    }


    // ******************** Initialization ************************************
    @Override public final void init() {
        getStyleClass().setAll(DEFAULT_STYLE_CLASS);
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
        barWidth.set(BAR_WIDTH);
    }

    public final DoubleProperty barWidthProperty() {
        return barWidth;
    }

    public final double getLabelFontSize() {
        return labelFontSize.get();
    }

    public final void setLabelFontSize(final double LABEL_FONT_SIZE) {
        double size = LABEL_FONT_SIZE < 8 ? 8 : (LABEL_FONT_SIZE > 52 ? 52 : LABEL_FONT_SIZE);
        labelFontSize.set(size);
    }

    public final ReadOnlyDoubleProperty labelFontSizeProperty() {
        return labelFontSize;
    }

    public final double getUnitFontSize() {
        return unitFontSize.get();
    }

    public final void setUnitFontSize(final double UNIT_FONT_SIZE) {
        unitFontSize.set(UNIT_FONT_SIZE);
    }

    public final DoubleProperty unitFontSizeProperty() {
        return unitFontSize;
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

    public final Color getUnitColor() {
        return unitColor.get();
    }

    public final void setUnitColor(final Color UNIT_COLOR) {
        unitColor.set(UNIT_COLOR);
    }

    public final ObjectProperty<Color> unitColorProperty() {
        return unitColor;
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

    @Override public void setPrefSize(final double WIDTH, final double HEIGHT) {
        final double SIZE = WIDTH < HEIGHT ? WIDTH : HEIGHT;
        super.setPrefSize(SIZE, SIZE);
    }
}