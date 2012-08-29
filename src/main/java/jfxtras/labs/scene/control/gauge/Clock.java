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
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Control;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.Calendar;
import java.util.Date;


/**
 * Created by
 * User: hansolo
 * Date: 12.03.12
 * Time: 12:44
 */
public class Clock extends Control {
    public enum Theme {
        BRIGHT,
        DARK
    }
    public static enum ClockStyle {
        DB,
        IOS6,
        STANDARD
    }
    private static final String        DEFAULT_STYLE_CLASS = "clock";
    private StringProperty             timeZone;
    private BooleanProperty            running;
    private BooleanProperty            secondPointerVisible;
    private BooleanProperty            autoDimEnabled;
    private BooleanProperty            daylightSavingTime;
    private ObjectProperty<Theme>      theme;
    private ObjectProperty<ClockStyle> clockStyle;
    private ObjectProperty<Paint>      brightBackgroundPaint;
    private ObjectProperty<Paint>      darkBackgroundPaint;
    private ObjectProperty<Paint>      brightTickMarkPaint;
    private ObjectProperty<Paint>      darkTickMarkPaint;
    private ObjectProperty<Paint>      brightPointerPaint;
    private ObjectProperty<Paint>      darkPointerPaint;
    private ObjectProperty<Paint>      secondPointerPaint;
    private IntegerProperty            hour;
    private IntegerProperty            minute;
    private IntegerProperty            second;
    private StringProperty             title;


    // ******************** Constructors **************************************
    public Clock() {
        timeZone              = new SimpleStringProperty(Calendar.getInstance().getTimeZone().getDisplayName());
        running               = new SimpleBooleanProperty(false);
        secondPointerVisible  = new SimpleBooleanProperty(true);
        autoDimEnabled        = new SimpleBooleanProperty(false);
        daylightSavingTime    = new SimpleBooleanProperty(Calendar.getInstance().getTimeZone().inDaylightTime(new Date()));
        theme                 = new SimpleObjectProperty<Theme>(Theme.BRIGHT);
        clockStyle            = new SimpleObjectProperty<ClockStyle>(ClockStyle.DB);
        brightBackgroundPaint = new SimpleObjectProperty<Paint>(Color.WHITE);
        darkBackgroundPaint   = new SimpleObjectProperty<Paint>(Color.BLACK);
        brightPointerPaint    = new SimpleObjectProperty<Paint>(Color.BLACK);
        darkPointerPaint      = new SimpleObjectProperty<Paint>(Color.WHITE);
        brightTickMarkPaint   = new SimpleObjectProperty<Paint>(Color.BLACK);
        darkTickMarkPaint     = new SimpleObjectProperty<Paint>(Color.WHITE);
        secondPointerPaint    = new SimpleObjectProperty<Paint>(Color.rgb(237, 0, 58));
        title                 = new SimpleStringProperty("");
        hour                  = new SimpleIntegerProperty(0);
        minute                = new SimpleIntegerProperty(0);
        second                = new SimpleIntegerProperty(0);
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

    public final Theme getTheme() {
        return theme.get();
    }

    public final void setTheme(final Theme Theme) {
        theme.set(Theme);
    }

    public final ObjectProperty<Theme> themeProperty() {
        return theme;
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

    public final Paint getBrightBackgroundPaint() {
        return brightBackgroundPaint.get();
    }

    public final void setBrightBackgroundPaint(final Paint BRIGHT_BACKGROUND_PAINT) {
        brightBackgroundPaint.set(BRIGHT_BACKGROUND_PAINT);
    }

    public final ObjectProperty<Paint> brightBackgroundPaintProperty() {
        return brightBackgroundPaint;
    }

    public final Paint getDarkBackgroundPaint() {
        return darkBackgroundPaint.get();
    }

    public final void setDarkBackgroundPaint(final Paint DARK_BACKGROUND_PAINT) {
        darkBackgroundPaint.set(DARK_BACKGROUND_PAINT);
    }

    public final ObjectProperty<Paint> darkBackgroundPaintProperty() {
        return darkBackgroundPaint;
    }

    public final Paint getBrightTickMarkPaint() {
        return brightTickMarkPaint.get();
    }

    public final void setBrightTickMarkPaint(final Paint BRIGHT_TICK_MARK_PAINT) {
        brightTickMarkPaint.set(BRIGHT_TICK_MARK_PAINT);
    }

    public final ObjectProperty<Paint> brightTickMarkPaintProperty() {
        return brightTickMarkPaint;
    }

    public final Paint getDarkTickMarkPaint() {
        return darkTickMarkPaint.get();
    }

    public final void setDarkTickMarkPaint(final Paint DARK_TICK_MARK_PAINT) {
        darkTickMarkPaint.set(DARK_TICK_MARK_PAINT);
    }

    public final ObjectProperty<Paint> darkTickMarkPaintProperty() {
        return darkTickMarkPaint;
    }

    public final Paint getBrightPointerPaint() {
        return brightPointerPaint.get();
    }

    public final void setBrightPointerPaint(final Paint BRIGHT_POINTER_PAINT) {
        brightPointerPaint.set(BRIGHT_POINTER_PAINT);
    }

    public final ObjectProperty<Paint> brightPointerPaintProperty() {
        return brightPointerPaint;
    }

    public final Paint getDarkPointerPaint() {
        return darkPointerPaint.get();
    }

    public final void setDarkPointerPaint(final Paint DARK_POINTER_PAINT) {
        darkPointerPaint.set(DARK_POINTER_PAINT);
    }

    public final ObjectProperty<Paint> darkPointerPaintProperty() {
        return darkPointerPaint;
    }

    public final Paint getSecondPointerPaint() {
        return secondPointerPaint.get();
    }

    public final void setSecondPointerPaint(final Paint SECOND_POINTER_PAINT) {
        secondPointerPaint.set(SECOND_POINTER_PAINT);
    }

    public final ObjectProperty<Paint> secondPointerPaintProperty() {
        return secondPointerPaint;
    }

    public final String getTitle() {
            return title.get();
        }

    public final void setTitle(final String TITLE) {
        title.set(TITLE);
    }

    public final StringProperty titleProperty() {
        return title;
    }

    public final int getHour() {
        return hour.get();
    }

    public final void setHour(final int HOUR) {
        hour.set(clamp(0, 23, HOUR));
    }

    public final ReadOnlyIntegerProperty hourProperty() {
        return hour;
    }

    public final int getMinute() {
        return minute.get();
    }

    public final void setMinute(final int MINUTE) {
        minute.set(clamp(0, 59, MINUTE));
    }

    public final ReadOnlyIntegerProperty minuteProperty() {
        return minute;
    }

    public final int getSecond() {
        return second.get();
    }

    public final void setSecond(final int SECOND) {
        second.set(clamp(0, 59, SECOND));
    }

    public final ReadOnlyIntegerProperty secondProperty() {
        return second;
    }

    private int clamp(final int MIN, final int MAX, final int VALUE) {
        return VALUE < MIN ? MIN : (VALUE > MAX ? MAX : VALUE);
    }


    // ******************** Style related *************************************
    @Override protected String getUserAgentStylesheet() {
        return getClass().getResource("extras.css").toExternalForm();
    }
}
