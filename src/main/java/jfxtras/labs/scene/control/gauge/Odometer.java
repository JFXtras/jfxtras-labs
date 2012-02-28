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

import javafx.animation.AnimationTimer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import javafx.scene.paint.Color;


/**
 * Created by
 * User: hansolo
 * Date: 25.02.12
 * Time: 15:39
 */
public class Odometer extends Control {
    private static final String        DEFAULT_STYLE_CLASS = "odometer";
    private static final long          ONE_SECOND          = 1000_000_000l;
    private static double              MIN_FLIP_TIME       = 1000_000_000.0 / 60.0; // 60 fps
    private ObjectProperty<Color>      color;
    private ObjectProperty<Color>      decimalColor;
    private ObjectProperty<Color>      numberColor;
    private ObjectProperty<Color>      numberDecimalColor;
    private LongProperty               flipTime;
    private BooleanProperty            countdownMode;
    private IntegerProperty            value;
    private IntegerProperty            noOfDigits;
    private IntegerProperty            noOfDecimals;
    private boolean                    keepAspect;


    // ******************** Constructors **************************************
    public Odometer() {
        this(6);
    }

    public Odometer(final int NO_OF_DIGITS) {
        color              = new SimpleObjectProperty<>(Color.rgb(240, 240, 240));
        decimalColor       = new SimpleObjectProperty<>(Color.rgb(220, 0, 0));
        numberColor        = new SimpleObjectProperty<>(Color.BLACK);
        numberDecimalColor = new SimpleObjectProperty<>(Color.WHITE);
        noOfDigits         = new SimpleIntegerProperty(NO_OF_DIGITS < 0 ? 1 : NO_OF_DIGITS);
        noOfDecimals       = new SimpleIntegerProperty(0);
        value              = new SimpleIntegerProperty(0);
        flipTime           = new SimpleLongProperty(ONE_SECOND);
        countdownMode      = new SimpleBooleanProperty(false);
        keepAspect         = true;
        init();
    }

    private void init() {
        // the -fx-skin attribute in the CSS sets which Skin class is used
        getStyleClass().add(DEFAULT_STYLE_CLASS);
    }


    // ******************** Methods *******************************************
    public final Color getColor() {
        return color.get();
    }

    public final void setColor(final Color COLOR) {
        color.set(COLOR);
    }

    public final ObjectProperty<Color> colorProperty() {
        return color;
    }

    public final Color getDecimalColor() {
        return decimalColor.get();
    }

    public final void setDecimalColor(final Color DECIMAL_COLOR) {
        decimalColor.set(DECIMAL_COLOR);
    }

    public final ObjectProperty<Color> decimalColorProperty() {
        return decimalColor;
    }

    public final Color getNumberColor() {
        return numberColor.get();
    }

    public final void setNumberColor(final Color COLOR) {
        numberColor.set(COLOR);
    }

    public final ObjectProperty<Color> numberColorProperty() {
        return numberColor;
    }

    public final Color getNumberDecimalColor() {
        return numberDecimalColor.get();
    }

    public final void setNumberDecimalColor(final Color NUMBER_DECIMAL_COLOR) {
        numberDecimalColor.set(NUMBER_DECIMAL_COLOR);
    }

    public final ObjectProperty<Color> numberDecimalColorProperty() {
        return numberDecimalColor;
    }

    public final long getFlipTime() {
        return flipTime.get();
    }

    public final void setFlipTime(final long FLIP_TIME) {
        flipTime.set(FLIP_TIME < 16666666l ? 16666666l : (FLIP_TIME > 3000000000l ? 3000000000l : FLIP_TIME));
    }

    public final LongProperty flipTimeProperty() {
        return flipTime;
    }

    public final boolean isCountdownMode() {
        return countdownMode.get();
    }

    public final void setCountdownMode(final boolean COUNTDOWN_MODE) {
        countdownMode.set(COUNTDOWN_MODE);
    }

    public final BooleanProperty countdownModeProperty() {
        return countdownMode;
    }

    public final int getValue() {
        return value.get();
    }

    public final void setValue(final int VALUE) {
        value.set(VALUE < 0 ? 0 : VALUE);
    }

    public final IntegerProperty valueProperty() {
        return value;
    }

    public final int getNoOfDigits() {
        return noOfDigits.get();
    }

    public final int getNoOfDecimals() {
        return noOfDecimals.get();
    }

    public final void increment() {
        value.set(value.get() + 1);
    }

    public final void decrement() {
        value.set(value.get() - 1);
    }

    @Override public void setPrefSize(final double WIDTH, final double HEIGHT) {
        double prefHeight = WIDTH < (HEIGHT * 0.5925925925925926) ? (WIDTH * 1.6875) : HEIGHT;
        double prefWidth = prefHeight * 0.5925925925925926 * (noOfDigits.get() + noOfDecimals.get());

        if (keepAspect) {
            super.setPrefSize(prefWidth, prefHeight);
        } else {
            super.setPrefSize(WIDTH, HEIGHT);
        }
    }


    // ******************** Style related *************************************
    @Override protected String getUserAgentStylesheet() {
        return getClass().getResource("extras.css").toExternalForm();
    }
}
