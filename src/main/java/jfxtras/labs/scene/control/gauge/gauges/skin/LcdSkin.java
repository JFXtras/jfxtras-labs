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

package jfxtras.labs.scene.control.gauge.gauges.skin;

import jfxtras.labs.scene.control.gauge.gauges.Lcd;
import jfxtras.labs.scene.control.gauge.gauges.ModelEvent;
import jfxtras.labs.scene.control.gauge.gauges.Section;
import jfxtras.labs.scene.control.gauge.gauges.ViewModelEvent;
import jfxtras.labs.scene.control.gauge.gauges.behavior.LcdBehavior;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Stop;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.util.ArrayList;


/**
 * Created by
 * User: hansolo
 * Date: 24.01.12
 * Time: 07:45
 */
public class LcdSkin extends GaugeSkinBase<Lcd, LcdBehavior> {
    private static final Rectangle PREF_SIZE = new Rectangle(132, 48);
    private AnimationTimer       lcdBlinkingTimer;
    private AnimationTimer       thresholdTimer;
    private Lcd                  control;
    private boolean              valueVisible;
    private boolean              thresholdVisible;
    private Group                sections;
    private Group                glowOn;
    private ArrayList<Color>     glowColors;
    private Group                lcd;
    private Group                lcdContent;
    private Text                 lcdValueString;
    private Text                 lcdValueBackgroundString;
    private Text                 lcdUnitString;
    private double               lcdValueOffsetLeft;
    private double               lcdValueOffsetRight;
    private Group                lcdThresholdIndicator;
    private double               lcdDigitalFontSizeFactor;
    private Text                 lcdTitle;
    private Text                 lcdNumberSystem;
    private Text                 lcdMinMeasuredValue;
    private Text                 lcdMaxMeasuredValue;
    private Text                 lcdFormerValue;
    private Group                minMeasured;
    private Group                maxMeasured;
    private DoubleProperty       currentValue;
    private double               formerValue;
    private DoubleProperty       lcdValue;
    private DoubleProperty       currentLcdValue;
    private FadeTransition       glowPulse;
    private Path                 trendUp;
    private Path                 trendSteady;
    private Path                 trendDown;
    private Transition           toValueAnimation;
    private boolean              isDirty;
    private boolean              initialized;
    private long                 lastLcdTimerCall;
    private long                 lastThresholdTimerCall;


    // ******************** Constructors **************************************
    public LcdSkin(final Lcd CONTROL) {
        super(CONTROL, new LcdBehavior(CONTROL));
        control                    = CONTROL;
        sections                   = new Group();
        glowOn                     = new Group();
        glowColors                 = new ArrayList<>(4);
        lcd                        = new Group();
        lcdContent                 = new Group();
        lcdValueString             = new Text();
        lcdValueBackgroundString   = new Text();
        lcdUnitString              = new Text();
        lcdValueOffsetLeft         = 0.0;
        lcdValueOffsetRight        = 0.0;
        lcdThresholdIndicator      = new Group();
        lcdDigitalFontSizeFactor   = 1.0;
        lcdTitle                   = new Text();
        lcdNumberSystem            = new Text();
        lcdMinMeasuredValue        = new Text();
        lcdMaxMeasuredValue        = new Text();
        lcdFormerValue             = new Text();
        minMeasured                = new Group();
        maxMeasured                = new Group();
        currentValue               = new SimpleDoubleProperty(0);
        lcdValue                   = new SimpleDoubleProperty(0);
        currentLcdValue            = new SimpleDoubleProperty(0);
        glowPulse                  = new FadeTransition(Duration.millis(800), glowOn);
        toValueAnimation           = new Transition() {
            {
                setCycleDuration(Duration.millis(control.getAnimationDuration()));
            }
            protected void interpolate(double frac) {
                currentValue.set(formerValue + (control.getValue() - formerValue) * frac);
            }
        };
        isDirty                    = false;
        initialized                = false;
        lastLcdTimerCall           = System.nanoTime() + BLINK_INTERVAL;
        valueVisible               = true;
        lcdBlinkingTimer           = new AnimationTimer() {
            @Override public void handle(final long CURRENT_NANOSECONDS) {
                long currentNanoTime = System.nanoTime();
                if (currentNanoTime > lastLcdTimerCall + BLINK_INTERVAL) {
                    valueVisible ^= true;
                    lcdValueString.setVisible(valueVisible);
                    lastLcdTimerCall = currentNanoTime;
                }
            }
        };
        thresholdVisible           = true;
        lastThresholdTimerCall     = System.nanoTime() + BLINK_INTERVAL;
        thresholdTimer             = new AnimationTimer() {
            @Override public void handle(final long CURRENT_NANOSECONDS) {
                long currentNanoTime = System.nanoTime();
                if (currentNanoTime > lastThresholdTimerCall + BLINK_INTERVAL) {
                    thresholdVisible ^= true;
                    lcdThresholdIndicator.setVisible(thresholdVisible);
                    lastThresholdTimerCall = currentNanoTime;
                }
            }
        };
        init();
    }


    // ******************** Initialization ************************************
    private void init() {
        if (control.getPrefWidth() < 0 || control.getPrefHeight() < 0) {
            control.setPrefSize(PREF_SIZE.getWidth(), PREF_SIZE.getHeight());
        }
        glowColors.clear();
        final Color GLOW_COLOR = control.getGlowColor();
        glowColors.add(Color.hsb(GLOW_COLOR.getHue(), 0.46, 0.96, 0.0));
        glowColors.add(Color.hsb(GLOW_COLOR.getHue(), 0.67, 0.90, 1.0));
        glowColors.add(Color.hsb(GLOW_COLOR.getHue(), 1.0, 1.0, 1.0));
        glowColors.add(Color.hsb(GLOW_COLOR.getHue(), 0.67, 0.90, 1.0));

        glowPulse.setFromValue(0.1);
        glowPulse.setToValue(1.0);
        glowPulse.setInterpolator(Interpolator.SPLINE(0.0, 0.0, 0.4, 1.0));
        glowPulse.setInterpolator(Interpolator.EASE_OUT);
        glowPulse.setCycleCount(Timeline.INDEFINITE);
        glowPulse.setAutoReverse(true);

        if (control.isPulsatingGlow() && control.isGlowVisible()) {
            if (!glowOn.isVisible()) {
                glowOn.setVisible(true);
            }
            if (glowOn.getOpacity() < 1.0) {
                glowOn.setOpacity(1.0);
            }
            glowPulse.play();
        } else {
            glowPulse.stop();
            glowOn.setOpacity(0.0);
        }

        if (control.isGlowVisible()) {
            if (control.isGlowOn()) {
                glowOn.setOpacity(1.0);
            } else {
                glowOn.setOpacity(0.0);
            }
        } else {
            glowOn.setOpacity(0.0);
        }

        if (control.isLcdBlinking()) {
            lcdBlinkingTimer.start();
        }

        addBindings();

        addListeners();

        currentValue.set(control.getValue());

        initialized = true;
        paint();
    }

    private void addBindings() {
        if (!sections.visibleProperty().isBound()) {
            sections.visibleProperty().bind(control.sectionsVisibleProperty());
        }

        if (!minMeasured.visibleProperty().isBound()) {
            minMeasured.visibleProperty().bind(control.minMeasuredValueVisibleProperty());
        }

        if (!maxMeasured.visibleProperty().isBound()) {
            maxMeasured.visibleProperty().bind(control.maxMeasuredValueVisibleProperty());
        }

        if (!lcdValue.isBound()) {
            lcdValue.bind(control.valueProperty());
        }

        if (!lcdMinMeasuredValue.visibleProperty().isBound()) {
            lcdMinMeasuredValue.visibleProperty().bind(control.lcdMinMeasuredValueVisibleProperty());
        }

        if (!lcdMaxMeasuredValue.visibleProperty().isBound()) {
            lcdMaxMeasuredValue.visibleProperty().bind(control.lcdMaxMeasuredValueVisibleProperty());
        }

        if (!lcdFormerValue.visibleProperty().isBound()) {
            lcdFormerValue.visibleProperty().bind(control.lcdFormerValueVisibleProperty());
        }
    }

    private void addListeners() {
        control.setOnModelEvent(new EventHandler<ModelEvent>() {
            @Override public void handle(final ModelEvent EVENT) {
                // Trigger repaint
                isDirty = true;
            }
        });

        control.setOnViewModelEvent(new EventHandler<ViewModelEvent>() {
            @Override public void handle(final ViewModelEvent EVENT) {
                // Trigger repaint
                isDirty = true;
            }
        });

        control.prefWidthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(final ObservableValue<? extends Number> ov, final Number oldValue, final Number newValue) {
                //control.setPrefHeight(newValue.doubleValue());
                isDirty = true;
            }
        });

        control.prefHeightProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(final ObservableValue<? extends Number> ov, final Number oldValue, final Number newValue) {
                //control.setPrefWidth(newValue.doubleValue());
                isDirty = true;
            }
        });

        control.lcdBlinkingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    lcdBlinkingTimer.start();
                } else {
                    lcdBlinkingTimer.stop();
                    lcdValueString.setVisible(true);
                }
            }
        });

        control.thresholdExceededProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    thresholdTimer.start();
                } else {
                    thresholdTimer.stop();
                    lcdThresholdIndicator.setVisible(true);
                }
            }
        });

        control.valueProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(final ObservableValue<? extends Number> ov, final Number oldValue, final Number newValue) {
                formerValue = oldValue.doubleValue();
                if (toValueAnimation.getStatus() != Animation.Status.STOPPED) {
                    toValueAnimation.stop();
                }
                if (control.isValueAnimationEnabled()) {
                    toValueAnimation.setInterpolator(Interpolator.SPLINE(0.5, 0.4, 0.4, 1.0));
                    toValueAnimation.play();
                } else {
                    currentValue.set(newValue.doubleValue());
                }
            }
        });

        currentValue.addListener(new ChangeListener<Number>() {
            @Override public void changed(final ObservableValue<? extends Number> ov, final Number oldValue, final Number newValue) {
                currentLcdValue.set(control.isLcdValueCoupled() ? currentValue.get() : control.getLcdValue());
                if (Double.compare(currentValue.get(), control.getMinMeasuredValue()) < 0) {
                    control.setMinMeasuredValue(currentValue.get());
                }
                if (Double.compare(currentValue.get(), control.getMaxMeasuredValue()) > 0) {
                    control.setMaxMeasuredValue(currentValue.get());
                }
                if (control.isThresholdBehaviorInverted()) {
                    control.setThresholdExceeded(currentValue.get() < control.getThreshold());
                } else {
                    control.setThresholdExceeded(currentValue.get() > control.getThreshold());
                }
                lcdThresholdIndicator.setVisible(control.isThresholdExceeded());

                drawLcdContent();

                if (!control.getSections().isEmpty()) {
                    for (Section section : control.getSections()) {
                        if (Double.compare(currentValue.get(),section.getStart()) >= 0 && Double.compare(currentValue.get(),section.getStop()) <= 0) {

                            break;
                        } else {

                        }
                    }
                    if (control.getWidth() <= control.getHeight()) {
                        // vertical

                    } else {
                        // horizontal

                    }

                }
            }
        });
    }

    @Override protected void handleControlPropertyChanged(final String PROPERTY) {
        super.handleControlPropertyChanged(PROPERTY);

        if (PROPERTY == "LCD_DESIGN") {
            drawLcd();
            drawLcdContent();
        } else if (PROPERTY == "LCD_NUMBER_SYSTEM") {
            drawLcdContent();
        } else if (PROPERTY == "GLOW_COLOR") {
            glowColors.clear();
            final Color GLOW_COLOR = control.getGlowColor();
            glowColors.add(Color.hsb(GLOW_COLOR.getHue(), 0.46, 0.96, 0.0));
            glowColors.add(Color.hsb(GLOW_COLOR.getHue(), 0.67, 0.90, 1.0));
            glowColors.add(Color.hsb(GLOW_COLOR.getHue(), 1.0, 1.0, 1.0));
            glowColors.add(Color.hsb(GLOW_COLOR.getHue(), 0.67, 0.90, 1.0));
            drawGlowOn();
        } else if (PROPERTY == "GLOW_VISIBILITY") {
            if (!control.isGlowVisible()) {
                glowOn.setOpacity(0.0);
            }
        } else if (PROPERTY == "GLOW_ON") {
            if (control.isGlowOn()) {
                glowOn.setOpacity(1.0);
            } else {
                glowOn.setOpacity(0.0);
            }
        } else if (PROPERTY == "PULSATING_GLOW") {
            if (control.isPulsatingGlow() && control.isGlowVisible()) {
                if (!glowOn.isVisible()) {
                    glowOn.setVisible(true);
                }
                if (glowOn.getOpacity() < 1.0) {
                    glowOn.setOpacity(1.0);
                }
                glowPulse.play();
            } else {
                glowPulse.stop();
                glowOn.setOpacity(0.0);
            }
        } else if (PROPERTY == "TREND") {
            drawLcdContent();
        }
    }


    // ******************** Methods *******************************************
    public void paint() {
        if (!initialized) {
            init();
        }
        getChildren().clear();
        drawGlowOn();
        drawLcd();
        drawLcdContent();

        getChildren().addAll(minMeasured,
                             maxMeasured,
                             lcd,
                             glowOn,
                             lcdContent);
    }

    @Override public void layoutChildren() {
        if (isDirty) {
            paint();
            isDirty = false;
        }
        super.layoutChildren();
    }

    @Override public Lcd getSkinnable() {
        return control;
    }

    @Override public void dispose() {
        control = null;
    }

    @Override protected double computePrefWidth(final double HEIGHT) {
        double prefWidth = PREF_SIZE.getWidth();
        if (HEIGHT != -1) {
            prefWidth = Math.max(0, HEIGHT - getInsets().getLeft() - getInsets().getRight());
        }
        return super.computePrefWidth(prefWidth);
    }

    @Override protected double computePrefHeight(final double WIDTH) {
        double prefHeight = PREF_SIZE.getHeight();
        if (WIDTH != -1) {
            prefHeight = Math.max(0, WIDTH - getInsets().getTop() - getInsets().getBottom());
        }
        return super.computePrefWidth(prefHeight);
    }

    private String formatLcdValue(final double VALUE, final int DECIMALS) {
        final StringBuilder DEC_BUFFER = new StringBuilder(16);
        DEC_BUFFER.append("0");
        final boolean lcdScientificFormatEnabled = false;

        if (DECIMALS > 0) {
            DEC_BUFFER.append(".");
        }

        for (int i = 0; i < DECIMALS; i++) {
            DEC_BUFFER.append("0");
        }

        if (lcdScientificFormatEnabled) {
            DEC_BUFFER.append("E0");
        }

        DEC_BUFFER.trimToSize();
        final java.text.DecimalFormat DEC_FORMAT = new java.text.DecimalFormat(DEC_BUFFER.toString(), new java.text.DecimalFormatSymbols(java.util.Locale.US));

        return DEC_FORMAT.format(VALUE);
    }

    private void prepareLcd() {
        final double SIZE = control.getPrefWidth() < control.getPrefHeight() ? control.getPrefWidth() : control.getPrefHeight();
        final double WIDTH = control.getPrefWidth();
        final double HEIGHT = control.getPrefHeight();

        final Rectangle LCD_MAIN = new Rectangle(1.0, 1.0, WIDTH - 2.0, HEIGHT - 2.0);

        // Setup the lcd value
        final Font LCD_VALUE_FONT;
        if (control.isLcdDigitalFontEnabled()) {
            LCD_VALUE_FONT = Font.loadFont(getClass().getResourceAsStream("/eu/hansolo/steelseriesfx/resources/digital.ttf"), (0.5833333333 * SIZE));
            lcdDigitalFontSizeFactor = 1.9098073909;
        } else {
            LCD_VALUE_FONT = Font.font("Verdana", FontWeight.NORMAL, (0.5 * SIZE));
            lcdDigitalFontSizeFactor = 1.0;
        }
        lcdValueString.setFont(LCD_VALUE_FONT);
        lcdValueString.setFontSmoothingType(FontSmoothingType.LCD);

        // Setup the lcd unit
        final Font LCD_UNIT_FONT = Font.font(control.getLcdUnitFont(), FontWeight.NORMAL, (0.26 * LCD_MAIN.getLayoutBounds().getHeight()));
        lcdUnitString.setFont(LCD_UNIT_FONT);
        lcdUnitString.setTextOrigin(VPos.BASELINE);
        lcdUnitString.setTextAlignment(TextAlignment.RIGHT);

        lcdUnitString.setText(control.isLcdValueCoupled() ? control.getUnit() : control.getLcdUnit());
        if (!lcdUnitString.visibleProperty().isBound()) {
            lcdUnitString.visibleProperty().bind(control.lcdUnitVisibleProperty());
        }

        lcdValueOffsetLeft = SIZE * 0.04;// SIZE * 0.0151515152; // distance between value and left border

        if (control.isLcdUnitVisible()) {
            lcdUnitString.setX((WIDTH - lcdUnitString.getLayoutBounds().getWidth()) - SIZE * 0.04);
            lcdUnitString.setY(SIZE - (lcdValueString.getLayoutBounds().getHeight() * lcdDigitalFontSizeFactor) / 2.0);
            lcdUnitString.setFontSmoothingType(FontSmoothingType.LCD);
            lcdUnitString.getStyleClass().add("lcd");
            lcdUnitString.setStyle(control.getLcdDesign().CSS);
            lcdUnitString.setId("lcd-text");
            lcdValueOffsetRight = (lcdUnitString.getLayoutBounds().getWidth() + SIZE * 0.0833333333); // distance between value and unit
            lcdValueString.setX(LCD_MAIN.getX() + LCD_MAIN.getWidth() - lcdValueString.getLayoutBounds().getWidth() - lcdValueOffsetRight);
        } else {
            lcdValueOffsetRight = SIZE * 0.04;// SIZE * 0.0151515152; // distance between value and right border
            lcdValueString.setX((WIDTH - lcdValueString.getLayoutBounds().getWidth()) - lcdValueOffsetRight);
        }

        // Setup the semitransparent background text
        lcdValueBackgroundString.setFont(LCD_VALUE_FONT);
        lcdValueBackgroundString.setTextOrigin(VPos.BASELINE);
        lcdValueBackgroundString.setTextAlignment(TextAlignment.RIGHT);

        // Setup the semitransparent background text
        // Width of one segment
        Text ONE_SEGMENT = new Text("8");
        ONE_SEGMENT.setFont(LCD_VALUE_FONT);
        final double ONE_SEGMENT_WIDTH = ONE_SEGMENT.getLayoutBounds().getWidth();

        // Width of decimals
        final double WIDTH_OF_DECIMALS = control.getLcdDecimals() == 0 ? 0 : control.getLcdDecimals() * ONE_SEGMENT_WIDTH + ONE_SEGMENT_WIDTH;

        // Available width
        final double AVAILABLE_WIDTH = LCD_MAIN.getWidth() - lcdValueOffsetRight - WIDTH_OF_DECIMALS;

        // Number of segments
        final int NO_OF_SEGMENTS = (int) Math.floor(AVAILABLE_WIDTH / ONE_SEGMENT_WIDTH);

        // Add segments to background text
        StringBuilder lcdBackgroundText = new StringBuilder();
        for (int i = 0 ; i < control.getLcdDecimals() ; i++) {
            lcdBackgroundText.append("8");
        }
        if (control.getLcdDecimals() != 0) {
            lcdBackgroundText.insert(0, ".");
        }

        for (int i = 0 ; i < NO_OF_SEGMENTS ; i++) {
            lcdBackgroundText.insert(0, "8");
        }
        lcdValueBackgroundString.setText(lcdBackgroundText.toString());

        // Visualize the lcd semitransparent background text
        if (control.isLcdUnitVisible()) {
            lcdValueBackgroundString.setX(LCD_MAIN.getX() + LCD_MAIN.getWidth() - lcdValueBackgroundString.getLayoutBounds().getWidth() - lcdValueOffsetRight);
        } else {
            lcdValueBackgroundString.setX((WIDTH - lcdValueBackgroundString.getLayoutBounds().getWidth()) - lcdValueOffsetRight);
        }
        lcdValueBackgroundString.setY(SIZE - (lcdValueBackgroundString.getLayoutBounds().getHeight() * lcdDigitalFontSizeFactor) / 2.0);
        lcdValueBackgroundString.setFontSmoothingType(FontSmoothingType.LCD);
        lcdValueBackgroundString.getStyleClass().add("lcd");
        lcdValueBackgroundString.setStyle(control.getLcdDesign().CSS);
        lcdValueBackgroundString.setId("lcd-text-background");

        // Setup the font for the lcd title, number system, min measured, max measure and former value
        final Font LCD_SMALL_FONT = Font.font("Verdana", FontWeight.NORMAL, (0.1666666667 * SIZE));
        // Title
        lcdTitle.setFont(LCD_SMALL_FONT);
        lcdTitle.setTextOrigin(VPos.BASELINE);
        lcdTitle.setTextAlignment(TextAlignment.CENTER);
        lcdTitle.setText(control.getTitle());
        lcdTitle.setX(LCD_MAIN.getLayoutX() + (LCD_MAIN.getLayoutBounds().getWidth() - lcdTitle.getLayoutBounds().getWidth()) / 2.0);
        lcdTitle.setY(LCD_MAIN.getLayoutY() + lcdTitle.getLayoutBounds().getHeight() + 0.04 * SIZE);
        lcdTitle.setFontSmoothingType(FontSmoothingType.LCD);
        lcdTitle.getStyleClass().add("lcd");
        lcdTitle.setStyle(control.getLcdDesign().CSS);
        lcdTitle.setId("lcd-text");

        // NumberSystem
        lcdNumberSystem.setFont(LCD_SMALL_FONT);
        lcdNumberSystem.setTextOrigin(VPos.BASELINE);
        lcdNumberSystem.setTextAlignment(TextAlignment.RIGHT);
        lcdNumberSystem.setText(control.getLcdNumberSystem().toString());
        lcdNumberSystem.setX(LCD_MAIN.getLayoutX() + (LCD_MAIN.getLayoutBounds().getWidth() - lcdTitle.getLayoutBounds().getWidth()) / 2.0);
        lcdNumberSystem.setTranslateY(HEIGHT - lcdThresholdIndicator.getLayoutBounds().getHeight() - 0.0416666667 * SIZE);
        lcdNumberSystem.setFontSmoothingType(FontSmoothingType.LCD);
        lcdNumberSystem.getStyleClass().add("lcd");
        lcdNumberSystem.setStyle(control.getLcdDesign().CSS);
        lcdNumberSystem.setId("lcd-text");


        // Min measured value
        lcdMinMeasuredValue.setFont(LCD_SMALL_FONT);
        lcdMinMeasuredValue.setTextOrigin(VPos.BASELINE);
        lcdMinMeasuredValue.setTextAlignment(TextAlignment.RIGHT);
        lcdMinMeasuredValue.setX(LCD_MAIN.getLayoutX() + 0.0416666667 * SIZE);
        lcdMinMeasuredValue.setY(LCD_MAIN.getLayoutY() + lcdMinMeasuredValue.getLayoutBounds().getHeight() + 0.04 * SIZE);
        lcdMinMeasuredValue.setFontSmoothingType(FontSmoothingType.LCD);
        lcdMinMeasuredValue.getStyleClass().add("lcd");
        lcdMinMeasuredValue.setStyle(control.getLcdDesign().CSS);
        lcdMinMeasuredValue.setId("lcd-text");

        // Max measured value
        lcdMaxMeasuredValue.setFont(LCD_SMALL_FONT);
        lcdMaxMeasuredValue.setTextOrigin(VPos.BASELINE);
        lcdMaxMeasuredValue.setTextAlignment(TextAlignment.RIGHT);
        lcdMaxMeasuredValue.setY(LCD_MAIN.getLayoutY() + lcdMinMeasuredValue.getLayoutBounds().getHeight() + 0.04 * SIZE);
        lcdMaxMeasuredValue.setFontSmoothingType(FontSmoothingType.LCD);
        lcdMaxMeasuredValue.getStyleClass().add("lcd");
        lcdMaxMeasuredValue.setStyle(control.getLcdDesign().CSS);
        lcdMaxMeasuredValue.setId("lcd-text");

        // Former value
        lcdFormerValue.setFont(LCD_SMALL_FONT);
        lcdFormerValue.setTextOrigin(VPos.BASELINE);
        lcdFormerValue.setTextAlignment(TextAlignment.CENTER);
        lcdFormerValue.setY(LCD_MAIN.getLayoutY() + LCD_MAIN.getHeight() - 0.0416666667 * SIZE);
        lcdFormerValue.setFontSmoothingType(FontSmoothingType.LCD);
        lcdFormerValue.getStyleClass().add("lcd");
        lcdFormerValue.setStyle(control.getLcdDesign().CSS);
        lcdFormerValue.setId("lcd-text");
    }

    private boolean isNoOfDigitsValid() {
        final Rectangle LCD_MAIN = new Rectangle(1.0, 1.0, control.getPrefWidth() - 2.0, control.getPrefHeight() - 2.0);
        final double AVAILABLE_WIDTH = LCD_MAIN.getWidth() - lcdValueOffsetLeft - lcdValueOffsetRight;
        final double NEEDED_WIDTH    = lcdValueString.getLayoutBounds().getWidth();

        return Double.compare(AVAILABLE_WIDTH, NEEDED_WIDTH) >= 0;
    }


    // ******************** Drawing *******************************************
    public void drawGlowOn() {
        final double SIZE = control.getPrefWidth() < control.getPrefHeight() ? control.getPrefWidth() : control.getPrefHeight();
        final double WIDTH = control.getPrefWidth();
        final double HEIGHT = control.getPrefHeight();

        glowOn.getChildren().clear();

        final Rectangle LCD_FRAME = new Rectangle(0.0, 0.0, WIDTH, HEIGHT);
        final double LCD_FRAME_CORNER_RADIUS = LCD_FRAME.getWidth() > LCD_FRAME.getHeight() ? (LCD_FRAME.getHeight() * 0.15) : (LCD_FRAME.getWidth() * 0.15);
        LCD_FRAME.setArcWidth(LCD_FRAME_CORNER_RADIUS);
        LCD_FRAME.setArcHeight(LCD_FRAME_CORNER_RADIUS);
        LCD_FRAME.setFill(Color.color(1.0, 1.0, 1.0, 0.5));
        LCD_FRAME.setStroke(null);

        final InnerShadow GLOW_EFFECT = new InnerShadow();
        GLOW_EFFECT.setWidth(0.6 * SIZE);
        GLOW_EFFECT.setHeight(0.6 * SIZE);
        GLOW_EFFECT.setBlurType(BlurType.GAUSSIAN);
        if (!GLOW_EFFECT.colorProperty().isBound()) {
            GLOW_EFFECT.colorProperty().bind(control.glowColorProperty());
        }
        LCD_FRAME.effectProperty().set(GLOW_EFFECT);

        glowOn.getChildren().addAll(LCD_FRAME);
    }

    public void drawLcd() {
        final double SIZE = control.getPrefWidth() < control.getPrefHeight() ? control.getPrefWidth() : control.getPrefHeight();
        final double WIDTH = control.getPrefWidth();
        final double HEIGHT = control.getPrefHeight();

        lcd.getChildren().clear();
        lcd.getStyleClass().add("lcd");

        final Rectangle LCD_FRAME = new Rectangle(0.0, 0.0, WIDTH, HEIGHT);
        final double LCD_FRAME_CORNER_RADIUS = LCD_FRAME.getWidth() > LCD_FRAME.getHeight() ? (LCD_FRAME.getHeight() * 0.15) : (LCD_FRAME.getWidth() * 0.15);
        LCD_FRAME.setArcWidth(LCD_FRAME_CORNER_RADIUS);
        LCD_FRAME.setArcHeight(LCD_FRAME_CORNER_RADIUS);

        final Paint LCD_FRAME_FILL = new LinearGradient(0.5 * WIDTH, 0.0,
                                                        0.5 * WIDTH, HEIGHT,
                                                        false, CycleMethod.NO_CYCLE,
                                                        new Stop(0.0, Color.color(0.1, 0.1, 0.1, 1)),
                                                        new Stop(0.1, Color.color(0.3, 0.3, 0.3, 1)),
                                                        new Stop(0.93, Color.color(0.3, 0.3, 0.3, 1)),
                                                        new Stop(1.0, Color.color(0.86, 0.86, 0.86, 1)));
        LCD_FRAME.setFill(LCD_FRAME_FILL);
        LCD_FRAME.setStroke(null);

        final Rectangle LCD_MAIN = new Rectangle(1.0, 1.0, WIDTH - 2.0, HEIGHT - 2.0);
        final double LCD_MAIN_CORNER_RADIUS = LCD_FRAME.getArcWidth() - 1;
        LCD_MAIN.setArcWidth(LCD_MAIN_CORNER_RADIUS);
        LCD_MAIN.setArcHeight(LCD_MAIN_CORNER_RADIUS);

        LCD_MAIN.setStyle(control.getLcdDesign().CSS);
        LCD_MAIN.setId("lcd-main");
        LCD_MAIN.setStroke(null);

        final InnerShadow INNER_GLOW = new InnerShadow();
        INNER_GLOW.setWidth(0.25 * SIZE);
        INNER_GLOW.setHeight(0.25 * SIZE);
        INNER_GLOW.setOffsetY(-0.05 * SIZE);
        INNER_GLOW.setOffsetX(0.0);
        INNER_GLOW.setColor(Color.color(1, 1, 1, 0.2));

        final InnerShadow INNER_SHADOW = new InnerShadow();
        INNER_SHADOW.setInput(INNER_GLOW);
        INNER_SHADOW.setWidth(0.15 * SIZE);
        INNER_SHADOW.setHeight(0.15 * SIZE);
        INNER_SHADOW.setOffsetY(0.025 * SIZE);
        INNER_SHADOW.setColor(Color.color(0, 0, 0, 0.65));
        LCD_MAIN.setEffect(INNER_SHADOW);

        // Prepare the trend indicators
        trendUp = new Path();
        trendUp.setFillRule(FillRule.EVEN_ODD);
        trendUp.getElements().add(new MoveTo(0.18181818181818182 * WIDTH, 0.9375 * HEIGHT));
        trendUp.getElements().add(new LineTo(0.21212121212121213 * WIDTH, 0.8125 * HEIGHT));
        trendUp.getElements().add(new LineTo(0.24242424242424243 * WIDTH, 0.9375 * HEIGHT));
        trendUp.getElements().add(new LineTo(0.18181818181818182 * WIDTH, 0.9375 * HEIGHT));
        trendUp.getElements().add(new ClosePath());
        trendUp.getStyleClass().add("lcd");
        trendUp.setStyle(control.getLcdDesign().CSS);
        trendUp.setId("lcd-text");
        trendUp.setStroke(null);
        trendUp.setVisible(false);

        trendSteady = new Path();
        trendSteady.setFillRule(FillRule.EVEN_ODD);
        trendSteady.getElements().add(new MoveTo(0.18181818181818182 * WIDTH, 0.8125 * HEIGHT));
        trendSteady.getElements().add(new LineTo(0.24242424242424243 * WIDTH, 0.875 * HEIGHT));
        trendSteady.getElements().add(new LineTo(0.18181818181818182 * WIDTH, 0.9375 * HEIGHT));
        trendSteady.getElements().add(new LineTo(0.18181818181818182 * WIDTH, 0.8125 * HEIGHT));
        trendSteady.getElements().add(new ClosePath());
        trendSteady.getStyleClass().add("lcd");
        trendSteady.setStyle(control.getLcdDesign().CSS);
        trendSteady.setId("lcd-text");
        trendSteady.setStroke(null);
        trendSteady.setVisible(false);

        trendDown = new Path();
        trendDown.setFillRule(FillRule.EVEN_ODD);
        trendDown.getElements().add(new MoveTo(0.18181818181818182 * WIDTH, 0.8125 * HEIGHT));
        trendDown.getElements().add(new LineTo(0.21212121212121213 * WIDTH, 0.9375 * HEIGHT));
        trendDown.getElements().add(new LineTo(0.24242424242424243 * WIDTH, 0.8125 * HEIGHT));
        trendDown.getElements().add(new LineTo(0.18181818181818182 * WIDTH, 0.8125 * HEIGHT));
        trendDown.getElements().add(new ClosePath());
        trendDown.getStyleClass().add("lcd");
        trendDown.setStyle(control.getLcdDesign().CSS);
        trendDown.setId("lcd-text");
        trendDown.setStroke(null);
        trendDown.setVisible(false);

        // Prepare all font related parameters of the lcd
        prepareLcd();

        lcd.getChildren().addAll(LCD_FRAME, LCD_MAIN);

        // Add lcd title string if visible
        if (control.isTitleVisible()) {
            lcd.getChildren().add(lcdTitle);
        }

        // Add lcd number system string if visible
        if (control.isLcdNumberSystemVisible()) {
            lcd.getChildren().add(lcdNumberSystem);
        }

        // Add lcd unit string if visible
        if (control.isLcdUnitVisible()) {
            lcd.getChildren().add(lcdUnitString);
        }

        // Add semitransparent lcd background text if digital font is used
        if (control.isLcdDigitalFontEnabled()) {
            lcd.getChildren().add(lcdValueBackgroundString);
        }

        // Add treshold indicator if visible
        lcdThresholdIndicator = createLcdThresholdIndicator(HEIGHT * 0.2045454545, HEIGHT * 0.2045454545);
        lcdThresholdIndicator.setTranslateX(0.04 * SIZE);
        lcdThresholdIndicator.setTranslateY(HEIGHT - lcdThresholdIndicator.getLayoutBounds().getHeight() - 0.0416666667 * SIZE);

        if (control.isLcdThresholdVisible()) {
            lcd.getChildren().add(lcdThresholdIndicator);
        }
    }

    public void drawLcdContent() {
        final double SIZE = control.getPrefWidth() < control.getPrefHeight() ? control.getPrefWidth() : control.getPrefHeight();
        final double WIDTH = control.getPrefWidth();
        final double HEIGHT = control.getPrefHeight();

        lcdContent.getChildren().clear();

        final Rectangle IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        IBOUNDS.setStroke(null);

        final Rectangle LCD_MAIN = new Rectangle(1.0, 1.0, WIDTH - 2.0, HEIGHT - 2.0);

        // Update the lcd value
        switch (control.getLcdNumberSystem()) {
            case HEXADECIMAL:
                lcdValueString.setText(Integer.toHexString((int) currentLcdValue.get()).toUpperCase());
                break;

            case OCTAL:
                lcdValueString.setText(Integer.toOctalString((int) currentLcdValue.get()).toUpperCase());
                break;

            case DECIMAL:

            default:
                lcdValueString.setText(formatLcdValue(currentLcdValue.get(), control.getLcdDecimals()));
                break;
        }
        lcdNumberSystem.setText(control.getLcdNumberSystem().toString());
        lcdNumberSystem.setX(WIDTH - lcdNumberSystem.getLayoutBounds().getWidth() - 0.0416666667 * SIZE);

        if (!isNoOfDigitsValid()) {
            lcdValueString.setText("-E-");
        }

        if (control.isLcdUnitVisible()) {
            lcdValueString.setX((LCD_MAIN.getX() + (LCD_MAIN.getWidth() - lcdValueString.getLayoutBounds().getWidth()) - lcdValueOffsetRight));
        } else {
            lcdValueString.setX((WIDTH - lcdValueString.getLayoutBounds().getWidth()) - lcdValueOffsetRight);
        }
        lcdValueString.setY(SIZE - (lcdValueString.getLayoutBounds().getHeight() * lcdDigitalFontSizeFactor) / 2.0);
        lcdValueString.getStyleClass().add("lcd");
        lcdValueString.setStyle(control.getLcdDesign().CSS);
        lcdValueString.setId("lcd-text");
        lcdValueString.setStroke(null);

        // Update the title
        lcdTitle.setText(control.getTitle());

        // Update the min measured value
        lcdMinMeasuredValue.setText(formatLcdValue(control.getMinMeasuredValue(), control.getLcdMinMeasuredValueDecimals()));

        // Update the max measured value
        lcdMaxMeasuredValue.setText(formatLcdValue(control.getMaxMeasuredValue(), control.getLcdMaxMeasuredValueDecimals()));
        lcdMaxMeasuredValue.setX(WIDTH - lcdMaxMeasuredValue.getLayoutBounds().getWidth() - 0.0416666667 * SIZE);

        // Update the former lcd value
        lcdFormerValue.setText(formatLcdValue(formerValue, control.getLcdDecimals()));
        lcdFormerValue.setX((WIDTH - lcdFormerValue.getLayoutBounds().getWidth()) / 2.0);
        lcdFormerValue.setFontSmoothingType(FontSmoothingType.LCD);

        // Update the trend indicators
        if (control.isTrendVisible()) {
            switch (control.getTrend()) {
                case UP:
                    trendUp.setVisible(true);
                    trendSteady.setVisible(false);
                    trendDown.setVisible(false);
                    break;
                case STEADY:
                    trendUp.setVisible(false);
                    trendSteady.setVisible(true);
                    trendDown.setVisible(false);
                    break;
                case DOWN:
                    trendUp.setVisible(false);
                    trendSteady.setVisible(false);
                    trendDown.setVisible(true);
                    break;
                default:
                    trendUp.setVisible(false);
                    trendSteady.setVisible(false);
                    trendDown.setVisible(false);
                    break;
            }
        }

        lcdContent.getChildren().addAll(IBOUNDS, lcdValueString, lcdMinMeasuredValue, lcdMaxMeasuredValue, lcdFormerValue, trendUp, trendSteady, trendDown);
    }


    // ******************** More drawing **************************************
    private Group createLcdThresholdIndicator(final double WIDTH, final double HEIGHT) {
        final Group INDICATOR = new Group();
        INDICATOR.getChildren().clear();

        final Shape IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        IBOUNDS.setStroke(null);
        INDICATOR.getChildren().add(IBOUNDS);

        final Path LCD_THRESHOLD = new Path();
        LCD_THRESHOLD.setFillRule(FillRule.EVEN_ODD);
        LCD_THRESHOLD.getElements().add(new MoveTo(WIDTH * 0.4444444444444444, HEIGHT * 0.7777777777777778));
        LCD_THRESHOLD.getElements().add(new LineTo(WIDTH * 0.5555555555555556, HEIGHT * 0.7777777777777778));
        LCD_THRESHOLD.getElements().add(new LineTo(WIDTH * 0.5555555555555556, HEIGHT * 0.8888888888888888));
        LCD_THRESHOLD.getElements().add(new LineTo(WIDTH * 0.4444444444444444, HEIGHT * 0.8888888888888888));
        LCD_THRESHOLD.getElements().add(new LineTo(WIDTH * 0.4444444444444444, HEIGHT * 0.7777777777777778));
        LCD_THRESHOLD.getElements().add(new ClosePath());
        LCD_THRESHOLD.getElements().add(new MoveTo(WIDTH * 0.4444444444444444, HEIGHT * 0.3333333333333333));
        LCD_THRESHOLD.getElements().add(new LineTo(WIDTH * 0.5555555555555556, HEIGHT * 0.3333333333333333));
        LCD_THRESHOLD.getElements().add(new LineTo(WIDTH * 0.5555555555555556, HEIGHT * 0.7222222222222222));
        LCD_THRESHOLD.getElements().add(new LineTo(WIDTH * 0.4444444444444444, HEIGHT * 0.7222222222222222));
        LCD_THRESHOLD.getElements().add(new LineTo(WIDTH * 0.4444444444444444, HEIGHT * 0.3333333333333333));
        LCD_THRESHOLD.getElements().add(new ClosePath());
        LCD_THRESHOLD.getElements().add(new MoveTo(0.0, HEIGHT));
        LCD_THRESHOLD.getElements().add(new LineTo(WIDTH, HEIGHT));
        LCD_THRESHOLD.getElements().add(new LineTo(WIDTH * 0.5, 0.0));
        LCD_THRESHOLD.getElements().add(new LineTo(0.0, HEIGHT));
        LCD_THRESHOLD.getElements().add(new ClosePath());
        LCD_THRESHOLD.getStyleClass().add("lcd");
        LCD_THRESHOLD.setStyle(control.getLcdDesign().CSS);
        LCD_THRESHOLD.setId("lcd-text");
        LCD_THRESHOLD.setStroke(null);

        INDICATOR.getChildren().addAll(LCD_THRESHOLD);

        return INDICATOR;
    }
}
