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

package jfxtras.labs.internal.scene.control.skin;

import com.sun.javafx.scene.control.skin.SkinBase;
import jfxtras.labs.internal.scene.control.behavior.LedBargraphBehavior;
import jfxtras.labs.scene.control.gauge.LedBargraph;
import jfxtras.labs.scene.control.gauge.Led;
import javafx.animation.AnimationTimer;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by
 * User: hansolo
 * Date: 16.02.12
 * Time: 11:30
 */
public class LedBargraphSkin extends SkinBase<LedBargraph, LedBargraphBehavior> {
    public static final long PEAK_TIMEOUT = 1500000000l;
    private LedBargraph control;
    private boolean          isDirty;
    private boolean          initialized;
    private Group            bargraph;
    private List <Led>       ledList;
    private long             lastTimerCall;
    private DoubleProperty   stepSize;
    private int              peakLedIndex;
    private AnimationTimer   timer;


    // ******************** Constructors **************************************
    public LedBargraphSkin(final LedBargraph CONTROL) {
        super(CONTROL, new LedBargraphBehavior(CONTROL));
        control         = CONTROL;
        initialized     = false;
        isDirty         = false;
        bargraph        = new Group();
        ledList         = new ArrayList<Led>(control.getNoOfLeds());
        for(int i = 0 ; i < control.getNoOfLeds() ; i++) {
            ledList.add(new Led());
        }
        lastTimerCall   = 0l;
        stepSize        = new SimpleDoubleProperty(1.0 / control.getNoOfLeds());
        peakLedIndex    = 0;
        timer           = new AnimationTimer() {
            @Override
            public void handle(long l) {
                long currentNanoTime = System.nanoTime();
                if (currentNanoTime > lastTimerCall + PEAK_TIMEOUT) {
                    ledList.get(peakLedIndex).setOn(false);
                    peakLedIndex = 0;
                    timer.stop();
                }
            }
        };

        init();
    }

    private void init() {
        if (control.getPrefWidth() < 0 | control.getPrefHeight() < 0) {
            control.setPrefSize(16, 16);
        }

        control.prefWidthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
                isDirty = true;
            }
        });

        control.prefHeightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
                isDirty = true;
            }
        });

        // Register listeners
        registerChangeListener(control.ledTypeProperty(), "LED_TYPE");
        registerChangeListener(control.frameVisibleProperty(), "FRAME_VISIBLE");
        registerChangeListener(control.ledSizeProperty(), "LED_SIZE");
        registerChangeListener(control.orientationProperty(), "ORIENTATION");
        registerChangeListener(control.noOfLedsProperty(), "LED_NUMBER");
        registerChangeListener(control.ledColorsProperty(), "LED_COLOR");

        control.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
                int currentLedPeakIndex = 0;
                for (int i = 0 ; i < control.getNoOfLeds() ; i++) {
                    if (Double.compare(i * stepSize.get(), newValue.doubleValue()) <= 0) {
                        ledList.get(i).setOn(true);
                        currentLedPeakIndex = i;
                    } else {
                        ledList.get(i).setOn(false);
                    }
                    ledList.get(peakLedIndex).setOn(true);
                }
                // PeakValue
                if (control.isPeakValueVisible()) {
                    if (currentLedPeakIndex > peakLedIndex) {
                        peakLedIndex = currentLedPeakIndex;
                        timer.stop();
                        lastTimerCall = System.nanoTime();
                        timer.start();
                    }
                }
            }
        });

        setLedColors();
        setLedTypes();

        initialized = true;
        paint();
    }


    // ******************** Methods *******************************************
    public final void paint() {
        if (!initialized) {
            init();
        }
        getChildren().clear();
        drawLed();
        getChildren().add(bargraph);
    }

    @Override protected void handleControlPropertyChanged(final String PROPERTY) {
        System.out.println(PROPERTY);
        super.handleControlPropertyChanged(PROPERTY);
        if (PROPERTY == "FRAME_VISIBLE") {
            for (Led led : ledList) {
                led.setFrameVisible(control.isFrameVisible());
            }
            paint();
        } else if (PROPERTY == "LED_SIZE") {
            for (Led led : ledList) {
                led.setPrefSize(control.getLedSize(), control.getLedSize());
            }
            paint();
        } else if (PROPERTY == "ORIENTATION") {
            paint();
        } else if (PROPERTY == "LED_NUMBER") {
            stepSize.set(1.0 / control.getNoOfLeds());
        } else if (PROPERTY == "LED_COLOR") {
            setLedColors();
            paint();
        } else if (PROPERTY == "LED_TYPE") {
            setLedTypes();
            paint();
        }
    }

    @Override public void layoutChildren() {
        if (isDirty) {
            paint();
            isDirty = false;
        }
        super.layoutChildren();
    }

    @Override public final LedBargraph getSkinnable() {
        return control;
    }

    @Override public final void dispose() {
        control = null;
    }

    @Override protected double computePrefWidth(final double PREF_WIDTH) {
        double prefWidth = 16;
        if (PREF_WIDTH != -1) {
            prefWidth = Math.max(0, PREF_WIDTH - getInsets().getLeft() - getInsets().getRight());
        }
        return super.computePrefWidth(prefWidth);
    }

    @Override protected double computePrefHeight(final double PREF_HEIGHT) {
        double prefHeight = 16;
        if (PREF_HEIGHT != -1) {
            prefHeight = Math.max(0, PREF_HEIGHT - getInsets().getTop() - getInsets().getBottom());
        }
        return super.computePrefWidth(prefHeight);
    }


    // ******************** Drawing related ***********************************
    private final void setLedColors() {
        for (int i = 0 ; i < control.getNoOfLeds() ; i++) {
            ledList.get(i).setColor(control.getLedColor(i));
        }
    }

    private final void setLedTypes() {
        for (int i = 0 ; i < control.getNoOfLeds() ; i++) {
            ledList.get(i).setType(control.getLedType());
        }
    }

    private final void drawLed() {
        final double SIZE = control.getPrefWidth() < control.getPrefHeight() ? control.getPrefWidth() : control.getPrefHeight();
        final double WIDTH = SIZE;
        final double HEIGHT = SIZE;

        bargraph.getChildren().clear();

        final Shape IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        bargraph.getChildren().add(IBOUNDS);

        if (control.getOrientation() == Orientation.VERTICAL) {
            VBox pane = new VBox();
            pane.setSpacing(0);
            pane.setPadding(new Insets(0, 0, 0, 0));
            for (int i = 0 ; i < control.getNoOfLeds() ; i++) {
                pane.getChildren().add(i, ledList.get(15 - i));
            }
            bargraph.getChildren().add(pane);
        } else {
            HBox pane = new HBox();
            pane.setSpacing(0);
            pane.setPadding(new Insets(0, 0, 0, 0));
            for (int i = 0 ; i < control.getNoOfLeds() ; i++) {
                pane.getChildren().add(i, ledList.get(i));
            }
            bargraph.getChildren().add(pane);
        }
    }
}
