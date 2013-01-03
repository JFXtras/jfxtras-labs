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
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;


/**
 * Created by
 * User: hansolo
 * Date: 02.01.12
 * Time: 17:14
 */
public final class Radial extends Gauge {
    private static final String DEFAULT_STYLE_CLASS = "radial";
    public enum ForegroundType {
        TYPE1,
        TYPE2,
        TYPE3,
        TYPE4,
        TYPE5
    }
    private BooleanProperty       histogramVisible;
    private ObjectProperty<Color> histogramColor;
    private DoubleProperty        histogramLineWidth;
    private BooleanProperty       histogramCreationEnabled;
    private IntegerProperty       histogramDataPeriodInMinutes;


    // ******************** Constructors **************************************
    public Radial() {
        this(new GaugeModel(), new StyleModel());
    }

    public Radial(final GaugeModel GAUGE_MODEL) {
        this(GAUGE_MODEL, new StyleModel());
    }

    public Radial(final StyleModel STYLE_MODEL) {
        this(new GaugeModel(), STYLE_MODEL);
    }

    public Radial(final GaugeModel GAUGE_MODEL, final StyleModel STYLE_MODEL) {
        super(GAUGE_MODEL, STYLE_MODEL);
        getStyleClass().setAll(DEFAULT_STYLE_CLASS);
        histogramVisible             = new SimpleBooleanProperty(false);
        histogramColor               = new SimpleObjectProperty<Color>(Color.AQUAMARINE);
        histogramLineWidth           = new SimpleDoubleProperty(1.0);
        histogramCreationEnabled     = new SimpleBooleanProperty(false);
        histogramDataPeriodInMinutes = new SimpleIntegerProperty(5);
    }


    // ******************** Methods *******************************************
    @Override public void setPrefSize(final double WIDTH, final double HEIGHT) {
        final double SIZE = WIDTH <= HEIGHT ? WIDTH : HEIGHT;
        super.setPrefSize(SIZE, SIZE);
    }

    @Override public void setMinSize(final double WIDTH, final double HEIGHT) {
        final double SIZE = WIDTH <= HEIGHT ? WIDTH : HEIGHT;
        super.setMinSize(SIZE, SIZE);
    }

    @Override public void setMaxSize(final double WIDTH, final double HEIGHT) {
        final double SIZE = WIDTH <= HEIGHT ? WIDTH : HEIGHT;
        super.setMaxSize(SIZE, SIZE);
    }

    public final boolean isHistogramVisible() {
        return histogramVisible.get();
    }

    public final void setHistogramVisible(final boolean HISTOGRAM_VISIBLE) {
        histogramVisible.set(HISTOGRAM_VISIBLE);
    }

    public final BooleanProperty histogramVisibleProperty() {
        return histogramVisible;
    }

    public final Color getHistogramColor() {
        return histogramColor.get();
    }

    public final void setHistogramColor(final Color HISTOGRAM_COLOR) {
        histogramColor.set(HISTOGRAM_COLOR);
    }

    public final ObjectProperty<Color> histogramColorProperty() {
        return histogramColor;
    }

    public final double getHistogramLineWidth() {
        return histogramLineWidth.get();
    }

    public final void setHistogramLineWidth(final double HISTOGRAM_LINE_WIDTH) {
        double lineWidth = HISTOGRAM_LINE_WIDTH < 0.5 ? 0.5 : (HISTOGRAM_LINE_WIDTH > 3.0 ? 3.0 : HISTOGRAM_LINE_WIDTH);
        histogramLineWidth.set(lineWidth);
    }

    public final DoubleProperty histogramLineWidthProperty() {
        return histogramLineWidth;
    }

    public final boolean isHistogramCreationEnabled() {
        return histogramCreationEnabled.get();
    }

    public final void setHistogramCreationEnabled(final boolean HISTOGRAM_CREATION_ENABLED) {
        histogramCreationEnabled.set(HISTOGRAM_CREATION_ENABLED);
    }

    public final BooleanProperty histogramCreationEnabledProperty() {
        return histogramCreationEnabled;
    }

    public final int getHistogramDataPeriodInMinutes() {
        return histogramDataPeriodInMinutes.get();
    }

    public final void setHistogramDataPeriodInMinutes(final int HISTOGRAM_DATA_PERIOD_IN_MINUTES) {
        histogramDataPeriodInMinutes.set(HISTOGRAM_DATA_PERIOD_IN_MINUTES);
    }

    public final IntegerProperty histogramDataPeriodInMinutesProperty() {
        return histogramDataPeriodInMinutes;
    }
}
