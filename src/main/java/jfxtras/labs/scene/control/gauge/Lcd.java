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
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;


/**
 * Created by
 * User: hansolo
 * Date: 24.01.12
 * Time: 07:39
 */
public class Lcd extends Gauge {
    private static final String DEFAULT_STYLE_CLASS = "lcd";
    private BooleanProperty     lcdMinMeasuredValueVisible;
    private BooleanProperty     lcdMaxMeasuredValueVisible;
    private BooleanProperty     lcdFormerValueVisible;
    private IntegerProperty     lcdMinMeasuredValueDecimals;
    private IntegerProperty     lcdMaxMeasuredValueDecimals;
    private BooleanProperty     bargraphVisible;
    private BooleanProperty     clockMode;
    private BooleanProperty     clockSecondsVisible;


    // ******************** Constructors **************************************
    public Lcd() {
        this(new GaugeModel(), new StyleModel());
    }

    public Lcd(final GaugeModel GAUGE_MODEL) {
        this(GAUGE_MODEL, new StyleModel());
    }

    public Lcd(final StyleModel STYLE_MODEL) {
        this(new GaugeModel(), STYLE_MODEL);
    }

    public Lcd(final GaugeModel GAUGE_MODEL, final StyleModel STYLE_MODEL) {
        super(GAUGE_MODEL, STYLE_MODEL);
        lcdMinMeasuredValueVisible  = new SimpleBooleanProperty(false);
        lcdMaxMeasuredValueVisible  = new SimpleBooleanProperty(false);
        lcdFormerValueVisible       = new SimpleBooleanProperty(false);
        lcdMinMeasuredValueDecimals = new SimpleIntegerProperty(STYLE_MODEL.getLcdDecimals());
        lcdMaxMeasuredValueDecimals = new SimpleIntegerProperty(STYLE_MODEL.getLcdDecimals());
        bargraphVisible             = new SimpleBooleanProperty(false);
        clockMode                   = new SimpleBooleanProperty(false);
        clockSecondsVisible         = new SimpleBooleanProperty(true);
        setValueAnimationEnabled(false);

        getStyleClass().setAll(DEFAULT_STYLE_CLASS);
    }


    // ******************** Methods *******************************************
    @Override public final void setPrefSize(final double WIDTH, final double HEIGHT) {
        super.setPrefSize(WIDTH, HEIGHT);
    }

    public final boolean isLcdMinMeasuredValueVisible() {
        return lcdMinMeasuredValueVisible.get();
    }

    public final void setLcdMinMeasuredValueVisible(final boolean LCD_MIN_MEASURED_VALUE_VISIBLE) {
        lcdMinMeasuredValueVisible.set(LCD_MIN_MEASURED_VALUE_VISIBLE);
    }

    public final BooleanProperty lcdMinMeasuredValueVisibleProperty() {
        return lcdMinMeasuredValueVisible;
    }

    public final boolean isLcdMaxMeasuredValueVisible() {
        return lcdMaxMeasuredValueVisible.get();
    }

    public final void setLcdMaxMeasuredValueVisible(final boolean LCD_MAX_MEASURED_VALUE_VISIBLE) {
        lcdMaxMeasuredValueVisible.set(LCD_MAX_MEASURED_VALUE_VISIBLE);
    }

    public final BooleanProperty lcdMaxMeasuredValueVisibleProperty() {
        return lcdMaxMeasuredValueVisible;
    }

    public final boolean isLcdFormerValueVisible() {
        return lcdFormerValueVisible.get();
    }

    public final void setLcdFormerValueVisible(final boolean LCD_FORMER_VALUE_VISIBLE) {
        lcdFormerValueVisible.set(LCD_FORMER_VALUE_VISIBLE);
    }

    public final BooleanProperty lcdFormerValueVisibleProperty() {
        return lcdFormerValueVisible;
    }

    public final int getLcdMinMeasuredValueDecimals() {
        return lcdMinMeasuredValueDecimals.get();
    }

    public final void setLcdMinMeasuredValueDecimals(final int LCD_MIN_MEASURED_VALUE_DECIMALS) {
        final int DECIMALS = LCD_MIN_MEASURED_VALUE_DECIMALS > 5 ? 5 : (LCD_MIN_MEASURED_VALUE_DECIMALS < 0 ? 0 : LCD_MIN_MEASURED_VALUE_DECIMALS);
        lcdMinMeasuredValueDecimals.set(DECIMALS);
    }

    public final IntegerProperty lcdMinMeasuredValueDecimalsProperty() {
        return lcdMaxMeasuredValueDecimals;
    }

    public final int getLcdMaxMeasuredValueDecimals() {
        return lcdMaxMeasuredValueDecimals.get();
    }

    public final void setLcdMaxMeasuredValueDecimals(final int LCD_MAX_MEASURED_VALUE_DECIMALS) {
        final int DECIMALS = LCD_MAX_MEASURED_VALUE_DECIMALS > 5 ? 5 : (LCD_MAX_MEASURED_VALUE_DECIMALS < 0 ? 0 : LCD_MAX_MEASURED_VALUE_DECIMALS);
        lcdMaxMeasuredValueDecimals.set(DECIMALS);
    }

    public final IntegerProperty lcdMaxMeasuredValueDecimalsProperty() {
        return lcdMaxMeasuredValueDecimals;
    }

    public final boolean isBargraphVisible() {
        return bargraphVisible.get();
    }

    public final void setBargraphVisible(final boolean BARGRAPH_VISIBLE) {
        bargraphVisible.set(BARGRAPH_VISIBLE);
    }

    public final BooleanProperty bargraphVisibleProperty() {
        return bargraphVisible;
    }

    public final boolean isClockMode() {
        return clockMode.get();
    }

    public final void setClockMode(final boolean CLOCK_MODE) {
        clockMode.set(CLOCK_MODE);
    }

    public final BooleanProperty clockModeProperty() {
        return clockMode;
    }

    public final boolean isClockSecondsVisible() {
        return clockSecondsVisible.get();
    }

    public final void setClockSecondsVisible(final boolean CLOCK_SECONDS_VISIBLE) {
        clockSecondsVisible.set(CLOCK_SECONDS_VISIBLE);
    }

    public final BooleanProperty clockSecondsVisibleProperty() {
        return clockSecondsVisible;
    }
}
