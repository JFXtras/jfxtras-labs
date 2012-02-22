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
import javafx.geometry.Orientation;
import javafx.scene.control.Control;
import javafx.scene.paint.Color;

import java.util.LinkedList;


/**
 * Created by
 * User: hansolo
 * Date: 16.02.12
 * Time: 11:29
 */
public class LedBargraph extends Control {
    private static final String               DEFAULT_STYLE_CLASS = "bargraph";
    private ObjectProperty<Led.Type>          ledType;
    private BooleanProperty                   frameVisible;
    private DoubleProperty                    ledSize;
    private ObjectProperty<Orientation>       orientation;
    private IntegerProperty                   noOfLeds;
    private ObjectProperty<LinkedList<Color>> ledColors;
    private BooleanProperty                   peakValueVisible;
    private DoubleProperty                    value;


    // ******************** Constructors **************************************
    public LedBargraph() {
        ledType          = new SimpleObjectProperty<Led.Type>(Led.Type.ROUND);
        frameVisible     = new SimpleBooleanProperty(true);
        ledSize          = new SimpleDoubleProperty(16);
        orientation      = new SimpleObjectProperty<Orientation>(Orientation.HORIZONTAL);
        noOfLeds         = new SimpleIntegerProperty(16);
        ledColors        = new SimpleObjectProperty<LinkedList<Color>>(new LinkedList<Color>());
        peakValueVisible = new SimpleBooleanProperty(false);
        value            = new SimpleDoubleProperty(0);
        init();
    }


    // ******************** Initialization ************************************
    private void init() {
        for (int i = 0 ; i < noOfLeds.get() ; i++) {
            if (i < 11) {
                ledColors.get().add(Color.LIME);
            } else if (i > 10 && i < 13) {
                ledColors.get().add(Color.YELLOW);
            } else {
                ledColors.get().add(Color.RED);
            }
        }

        // the -fx-skin attribute in the CSS sets which Skin class is used
        getStyleClass().add(DEFAULT_STYLE_CLASS);
    }


    // ******************** Methods *******************************************
    public final Led.Type getLedType() {
        return ledType.get();
    }

    public final void setLedType(final Led.Type LED_TYPE) {
        ledType.set(LED_TYPE);
    }

    public final ObjectProperty<Led.Type> ledTypeProperty() {
        return ledType;
    }

    public final boolean isFrameVisible() {
        return frameVisible.get();
    }

    public final void setFrameVisible(final boolean FRAME_VISIBLE) {
        frameVisible.set(FRAME_VISIBLE);
    }

    public final BooleanProperty frameVisibleProperty() {
        return frameVisible;
    }

    public final double getLedSize() {
        return ledSize.get();
    }

    public final void setLedSize(final double LED_SIZE) {
        double size = LED_SIZE < 10 ? 10 : (LED_SIZE > 50 ? 50 : LED_SIZE);
        ledSize.set(size);
    }

    public final DoubleProperty ledSizeProperty() {
        return ledSize;
    }

    public final Orientation getOrientation() {
        return orientation.get();
    }

    public final void setOrientation(final Orientation ORIENTATION) {
        orientation.set(ORIENTATION);
    }

    public final ObjectProperty<Orientation> orientationProperty() {
        return orientation;
    }

    public final int getNoOfLeds() {
        return noOfLeds.get();
    }

    public final void setNoOfLeds(final int NO_OF_LEDS) {
        int amount = NO_OF_LEDS < 5 ? 5 : NO_OF_LEDS;
        noOfLeds.set(NO_OF_LEDS);
    }

    public final IntegerProperty noOfLedsProperty() {
        return noOfLeds;
    }

    public final LinkedList<Color> getLedColors() {
        return ledColors.get();
    }

    public final void setLedColors(final LinkedList<Color> LED_COLORS) {
        ledColors.set(LED_COLORS);
    }

    public final ObjectProperty<LinkedList<Color>> ledColorsProperty() {
        return ledColors;
    }

    public final Color getLedColor(final int INDEX) {
        Color ledColor;
        if (INDEX < 0) {
            ledColor = ledColors.get().get(0);
        } else if (INDEX > noOfLeds.get() - 1) {
            ledColor = ledColors.get().get(noOfLeds.get() - 1);
        } else {
            ledColor = ledColors.get().get(INDEX);
        }
        return ledColor;
    }

    public final void setLedColor(final int INDEX, final Color COLOR) {
        if (INDEX < 0) {
            ledColors.get().set(0, COLOR);
        } else if (INDEX > noOfLeds.get() - 1) {
            ledColors.get().set(noOfLeds.get() - 1, COLOR);
        } else {
            ledColors.get().set(INDEX, COLOR);
        }
    }

    public final boolean isPeakValueVisible() {
        return peakValueVisible.get();
    }

    public final void setPeakValueVisible(final boolean PEAK_VALUE_VISIBLE) {
        peakValueVisible.set(PEAK_VALUE_VISIBLE);
    }

    public final BooleanProperty peakValueVisibleProperty() {
        return peakValueVisible;
    }

    public final double getValue() {
        return value.get();
    }

    public final void setValue(final double VALUE) {
        double val = VALUE < 0 ? 0 : (VALUE > 1 ? 1 : VALUE);
        value.set(val);
    }

    public final DoubleProperty valueProperty() {
        return value;
    }


    // ******************** Stylesheet handling *******************************
    @Override public String getUserAgentStylesheet() {
        return getClass().getResource("extras.css").toExternalForm();
    }
}
