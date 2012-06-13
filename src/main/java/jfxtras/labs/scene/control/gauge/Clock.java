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
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Control;
import javafx.scene.paint.Color;

import java.util.Calendar;
import java.util.Date;


/**
 * Created by
 * User: hansolo
 * Date: 12.03.12
 * Time: 12:44
 */
public class Clock extends Control {
    public enum                               BackgroundStyle {
        BRIGHT,
        DARK,
        CUSTOM
    }
    public static enum                        ClockStyle {
        DB,
        IOS6
    }
    private static final String               DEFAULT_STYLE_CLASS = "clock";
    private StringProperty                    timeZone;
    private BooleanProperty                   running;
    private BooleanProperty                   secondPointerVisible;
    private BooleanProperty                   autoDimEnabled;
    private BooleanProperty                   daylightSavingTime;
    private ObjectProperty<BackgroundStyle>   backgroundStyle;
    private ObjectProperty<ClockStyle>        clockStyle;
    private ObjectProperty<Color>             backgroundColor;
    private ObjectProperty<Color> tickMarkColor;
    private ObjectProperty<Color>             pointerColor;
    private ObjectProperty<Color>             secondPointerColor;


    // ******************** Constructors **************************************
    public Clock() {
        timeZone             = new SimpleStringProperty(Calendar.getInstance().getTimeZone().getDisplayName());
        running              = new SimpleBooleanProperty(false);
        secondPointerVisible = new SimpleBooleanProperty(true);
        autoDimEnabled       = new SimpleBooleanProperty(true);
        daylightSavingTime   = new SimpleBooleanProperty(Calendar.getInstance().getTimeZone().inDaylightTime(new Date()));
        backgroundStyle      = new SimpleObjectProperty<BackgroundStyle>(BackgroundStyle.BRIGHT);
        clockStyle           = new SimpleObjectProperty<ClockStyle>(ClockStyle.DB);
        backgroundColor      = new SimpleObjectProperty<Color>(Color.WHITE);
        tickMarkColor        = new SimpleObjectProperty<Color>(Color.BLACK);
        pointerColor         = new SimpleObjectProperty<Color>(Color.BLACK);
        secondPointerColor   = new SimpleObjectProperty<Color>(Color.rgb(237, 0, 58));
        init();
    }

    private void init() {
        // the -fx-skin attribute in the CSS sets which Skin class is used
        getStyleClass().add(DEFAULT_STYLE_CLASS);
    }


    // ******************** Methods *******************************************
    @Override public void setPrefSize(final double WIDTH, final double HEIGHT) {
        final double SIZE = WIDTH <= HEIGHT ? WIDTH : HEIGHT;
        super.setPrefSize(SIZE, SIZE);
    }

    public final String getTimeZone() {
        return timeZone.get();
    }

    public final void setTimeZone(final String TIME_ZONE) {
        timeZone.set(TIME_ZONE);
    }

    public final StringProperty timeZoneProperty() {
        return timeZone;
    }

    public final boolean isRunning() {
        return running.get();
    }

    public final void setRunning(final boolean RUNNING) {
        running.set(RUNNING);
    }

    public final BooleanProperty runningProperty() {
        return running;
    }

    public final boolean isSecondPointerVisible() {
        return secondPointerVisible.get();
    }

    public final void setSecondPointerVisible(final boolean SECOND_POINTER_VISIBLE) {
        secondPointerVisible.set(SECOND_POINTER_VISIBLE);
    }

    public final BooleanProperty secondPointerVisibleProperty() {
        return secondPointerVisible;
    }

    public final boolean isAutoDimEnabled() {
        return autoDimEnabled.get();
    }

    public final void setAutoDimEnabled(final boolean AUTO_DIM_ENABLED) {
        autoDimEnabled.set(AUTO_DIM_ENABLED);
    }

    public final BooleanProperty autoDimEnabledProperty() {
        return autoDimEnabled;
    }

    public final boolean isDaylightSavingTime() {
        return daylightSavingTime.get();
    }

    public final void setDaylightSavingTime(final boolean DAYLIGHT_SAVING_TIME) {
        daylightSavingTime.set(DAYLIGHT_SAVING_TIME);
    }

    public final BooleanProperty daylightSavingTimeProperty() {
        return daylightSavingTime;
    }

    public final BackgroundStyle getBackgroundStyle() {
        return backgroundStyle.get();
    }

    public final void setBackgroundStyle(final BackgroundStyle BACKGROUND_STYLE) {
        backgroundStyle.set(BACKGROUND_STYLE);
    }

    public final ObjectProperty<BackgroundStyle> backgroundStyleProperty() {
        return backgroundStyle;
    }

    public final ClockStyle getClockStyle() {
        return clockStyle.get();
    }

    public final void setClockStyle(final ClockStyle CLOCK_STYLE) {
        clockStyle.set(CLOCK_STYLE);
    }

    public final ObjectProperty<ClockStyle> clockStyleProperty() {
        return clockStyle;
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

    public final Color getSecondPointerColor() {
        return secondPointerColor.get();
    }

    public final void setSecondPointerColor(final Color SECOND_POINTER_COLOR) {
        secondPointerColor.set(SECOND_POINTER_COLOR);
    }

    public final ObjectProperty<Color> secondPointerColorProperty() {
        return secondPointerColor;
    }


    // ******************** Style related *************************************
    @Override protected String getUserAgentStylesheet() {
        return getClass().getResource("extras.css").toExternalForm();
    }
}
