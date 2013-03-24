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

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import jfxtras.labs.internal.scene.control.behavior.LcdBehavior;
import jfxtras.labs.scene.control.gauge.Gauge;
import jfxtras.labs.scene.control.gauge.Lcd;
import jfxtras.labs.scene.control.gauge.Section;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


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
    private Path                 trendRising;
    private Path                 trendSteady;
    private Path                 trendFalling;
    private Path                 trendDown;
    private List<Shape>          bargraph;
    private Transition           toValueAnimation;
    private boolean              isDirty;
    private boolean              initialized;
    private long                 lastLcdTimerCall;
    private long                 lastThresholdTimerCall;
    private long                 lastClockTimerCall;
    private AnimationTimer       clockTimer;
    private StringProperty       lcdClockValue;


    // ******************** Constructors **************************************
    public LcdSkin(final Lcd CONTROL) {
        super(CONTROL, new LcdBehavior(CONTROL));
        control                    = CONTROL;
        sections                   = new Group();
        glowOn                     = new Group();
        glowColors                 = new ArrayList<Color>(4);
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
        bargraph                   = new ArrayList<Shape>(20);
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
        lastLcdTimerCall           = System.nanoTime() + getBlinkInterval();
        valueVisible               = true;
        lcdBlinkingTimer           = new AnimationTimer() {
            @Override public void handle(final long NOW) {
                if (NOW > lastLcdTimerCall + getBlinkInterval()) {
                    valueVisible ^= true;
                    lcdValueString.setVisible(valueVisible);
                    lastLcdTimerCall = NOW;
                }
            }
        };
        thresholdVisible           = false;
        lastThresholdTimerCall     = System.nanoTime() + getBlinkInterval();
        thresholdTimer             = new AnimationTimer() {
            @Override public void handle(final long NOW) {
                if (NOW > lastThresholdTimerCall + getBlinkInterval() && control.isLcdThresholdVisible()) {
                    thresholdVisible ^= true;
                    lcdThresholdIndicator.setVisible(thresholdVisible);
                    lastThresholdTimerCall = NOW;
                }
            }
        };
        lastClockTimerCall         = System.nanoTime();
        clockTimer                 = new AnimationTimer() {
            @Override public void handle(final long NOW) {
                if (NOW > lastClockTimerCall + 500000000l) {
                    updateLcdClock();
                }
            }
        };
        lcdClockValue              = new SimpleStringProperty("00:00:00");
        init();
    }


    // ******************** Initialization ************************************
    private void init() {
        if (control.getPrefWidth() < 0 || control.getPrefHeight() < 0) {
            control.setPrefSize(PREF_SIZE.getWidth(), PREF_SIZE.getHeight());
        }

        if (control.getMinWidth() < 0 | control.getMinHeight() < 0) {
            control.setMinSize(50, 50);
        }

        if (control.getMaxWidth() < 0 | control.getMaxHeight() < 0) {
            control.setMaxSize(1024, 1024);
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

        if (control.isClockMode()) {
            clockTimer.start();
        }

        addBindings();
        addListeners();
        registerChangeListener(control.backgroundVisibleProperty(), "BACKGROUND_VISIBILITY");
        registerChangeListener(control.clockModeProperty(), "CLOCK_MODE");
        registerChangeListener(lcdClockValue, "CLOCK_VALUE");
        registerChangeListener(control.titleProperty(), "LCD");
        registerChangeListener(control.unitProperty(), "LCD");
        registerChangeListener(control.lcdUnitProperty(), "LCD");

        currentLcdValue.set(control.getLcdValue());

        initialized = true;
        repaint();
    }

    private void addBindings() {
        if (sections.visibleProperty().isBound()) {
            sections.visibleProperty().unbind();
        }
        sections.visibleProperty().bind(control.sectionsVisibleProperty());

        if (minMeasured.visibleProperty().isBound()) {
            minMeasured.visibleProperty().unbind();
        }
        minMeasured.visibleProperty().bind(control.minMeasuredValueVisibleProperty());

        if (maxMeasured.visibleProperty().isBound()) {
            maxMeasured.visibleProperty().unbind();
        }
        maxMeasured.visibleProperty().bind(control.maxMeasuredValueVisibleProperty());

        if (lcdValue.isBound()) {
            lcdValue.unbind();
        }
        lcdValue.bind(control.lcdValueProperty());

        if (lcdMinMeasuredValue.visibleProperty().isBound()) {
            lcdMinMeasuredValue.visibleProperty().unbind();
        }
        lcdMinMeasuredValue.visibleProperty().bind(control.lcdMinMeasuredValueVisibleProperty());

        if (lcdMaxMeasuredValue.visibleProperty().isBound()) {
            lcdMinMeasuredValue.visibleProperty().unbind();
        }
        lcdMaxMeasuredValue.visibleProperty().bind(control.lcdMaxMeasuredValueVisibleProperty());

        if (lcdFormerValue.visibleProperty().isBound()) {
            lcdFormerValue.visibleProperty().unbind();
        }
        lcdFormerValue.visibleProperty().bind(control.lcdFormerValueVisibleProperty());

        if (lcdNumberSystem.visibleProperty().isBound()) {
            lcdNumberSystem.visibleProperty().unbind();
        }
        lcdNumberSystem.visibleProperty().bind(control.lcdNumberSystemVisibleProperty());

        if (lcdTitle.visibleProperty().isBound()) {
            lcdTitle.visibleProperty().unbind();
        }
        lcdTitle.visibleProperty().bind(control.titleVisibleProperty());
    }

    private void addListeners() {
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

        control.bargraphVisibleProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean oldValue, Boolean newValue) {
                repaint();
            }
        });

        control.thresholdExceededProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean oldValue, Boolean newValue) {
                if (control.isLcdThresholdVisible()) {
                    if (newValue) {
                        thresholdTimer.start();
                    } else {
                        thresholdTimer.stop();
                        lcdThresholdIndicator.setVisible(false);
                    }
                }
            }
        });

        control.lcdValueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
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

                checkMarkers(control, oldValue.doubleValue(), newValue.doubleValue());
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

                checkMarkers(control, oldValue.doubleValue(), newValue.doubleValue());
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
                if (control.isLcdThresholdVisible()) {
                    lcdThresholdIndicator.setVisible(control.isThresholdExceeded());
                }

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


    // ******************** Methods *******************************************
    @Override protected void handleControlPropertyChanged(final String PROPERTY) {
        super.handleControlPropertyChanged(PROPERTY);

        if ("LCD".equals(PROPERTY)) {
            drawLcd();
            drawLcdContent();
        } else if ("GLOW_COLOR".equals(PROPERTY)) {
            glowColors.clear();
            final Color GLOW_COLOR = control.getGlowColor();
            glowColors.add(Color.hsb(GLOW_COLOR.getHue(), 0.46, 0.96, 0.0));
            glowColors.add(Color.hsb(GLOW_COLOR.getHue(), 0.67, 0.90, 1.0));
            glowColors.add(Color.hsb(GLOW_COLOR.getHue(), 1.0, 1.0, 1.0));
            glowColors.add(Color.hsb(GLOW_COLOR.getHue(), 0.67, 0.90, 1.0));
            drawGlowOn();
        } else if ("GLOW_VISIBILITY".equals(PROPERTY)) {
            if (!control.isGlowVisible()) {
                glowOn.setOpacity(0.0);
            }
        } else if ("GLOW_ON".equals(PROPERTY)) {
            if (control.isGlowOn()) {
                glowOn.setOpacity(1.0);
            } else {
                glowOn.setOpacity(0.0);
            }
        } else if ("PULSATING_GLOW".equals(PROPERTY)) {
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
        } else if ("TREND".equals(PROPERTY)) {
            drawLcdContent();
        } else if ("BACKGROUND_VISIBILITY".equals(PROPERTY)) {
            repaint();
        } else if ("GAUGE_MODEL".equals(PROPERTY)) {
            addBindings();
            repaint();
        } else if ("STYLE_MODEL".equals(PROPERTY)) {
            addBindings();
            repaint();
        } else if ("PREF_WIDTH".equals(PROPERTY)) {
            repaint();
        } else if ("PREF_HEIGHT".equals(PROPERTY)) {
            repaint();
        } else if ("CLOCK_MODE".equals(PROPERTY)) {
            if (control.isClockMode()) {
                clockTimer.start();
            } else {
                clockTimer.stop();
            }
        } else if ("CLOCK_VALUE".equals(PROPERTY)) {
            drawLcdContent();
        }
    }

    public void repaint() {
        isDirty = true;
        getSkinnable().requestLayout();
    }

// removed compilation problem
//    @Override public void layoutChildren() {
//        if (!isDirty) {
//            return;
//        }
//        if (!initialized) {
//            init();
//        }
//        if (control.getScene() != null) {
//            drawGlowOn();
//            drawLcd();
//            drawLcdContent();
//
//            getChildren().setAll(minMeasured,
//                maxMeasured,
//                lcd,
//                glowOn,
//                lcdContent);
//        }
//        isDirty = false;
//
//        super.layoutChildren();
//    }

    public Lcd getControl() {
        return control;
    }

    @Override public void dispose() {
        control = null;
    }

    @Override protected double computePrefWidth(final double HEIGHT) {
        double prefWidth = PREF_SIZE.getWidth();
        if (HEIGHT != -1) {
            prefWidth = Math.max(0, HEIGHT - getSkinnable().getInsets().getLeft() - getSkinnable().getInsets().getRight());
        }
        return super.computePrefWidth(prefWidth);
    }

    @Override protected double computePrefHeight(final double WIDTH) {
        double prefHeight = PREF_SIZE.getHeight();
        if (WIDTH != -1) {
            prefHeight = Math.max(0, WIDTH - getSkinnable().getInsets().getTop() - getSkinnable().getInsets().getBottom());
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
        /*
        * THANX A LOT TO VOLCANO TYPE FOR SPONSORING JFXTRAS WITH THE 'BUS' FONT THAT
        * IS NOW AVAILABLE IN THE LCD CONTROL.
        * HTTP://WWW.VOLCANO-TYPE.DE
        * */
        final Font LCD_VALUE_FONT;
        switch(control.getLcdValueFont()) {
            case BUS:
                LCD_VALUE_FONT = Font.loadFont(getClass().getResourceAsStream("/jfxtras/labs/scene/control/gauge/bus.otf"), (0.4583333333 * SIZE));
                lcdDigitalFontSizeFactor = 1.0;
                break;
            case LCD:
                LCD_VALUE_FONT = Font.loadFont(getClass().getResourceAsStream("/jfxtras/labs/scene/control/gauge/digital.ttf"), (0.5833333333 * SIZE));
                lcdDigitalFontSizeFactor = 1.9098073909;
                break;
            case PIXEL:
                LCD_VALUE_FONT = Font.loadFont(getClass().getResourceAsStream("/jfxtras/labs/scene/control/gauge/pixel.ttf"), (0.5208333333 * SIZE));
                lcdDigitalFontSizeFactor = 1.0;
                break;
            case PHONE_LCD:
                LCD_VALUE_FONT = Font.loadFont(getClass().getResourceAsStream("/jfxtras/labs/scene/control/gauge/phonelcd.ttf"), (0.4583333333 * SIZE));
                lcdDigitalFontSizeFactor = 1.0;
                break;
            case STANDARD:
            default:
                LCD_VALUE_FONT = Font.font("Verdana", FontWeight.NORMAL, (0.5 * SIZE));
                lcdDigitalFontSizeFactor = 1.0;
                break;
        }
        lcdValueString.setFont(LCD_VALUE_FONT);
        lcdValueString.setFontSmoothingType(FontSmoothingType.LCD);
        lcdValueString.getStyleClass().clear();
        lcdValueString.getStyleClass().add("lcd");
        lcdValueString.getStyleClass().add(control.getLcdDesign().CSS);
        lcdValueString.getStyleClass().add("lcd-text");

        // Setup the lcd unit
        final Font LCD_UNIT_FONT = Font.font(control.getLcdUnitFont(), FontWeight.NORMAL, (0.26 * LCD_MAIN.getLayoutBounds().getHeight()));
        lcdUnitString.setFont(LCD_UNIT_FONT);
        lcdUnitString.setTextOrigin(VPos.BASELINE);
        lcdUnitString.setTextAlignment(TextAlignment.RIGHT);

        lcdUnitString.setText(control.isLcdValueCoupled() ? control.getUnit() : control.getLcdUnit());
        if (lcdUnitString.visibleProperty().isBound()) {
            lcdUnitString.visibleProperty().unbind();
        }
        lcdUnitString.visibleProperty().bind(control.lcdUnitVisibleProperty());

        lcdValueOffsetLeft = SIZE * 0.04;// SIZE * 0.0151515152; // distance between value and left border

        if (control.isLcdUnitVisible()) {
            lcdUnitString.setX((WIDTH - lcdUnitString.getLayoutBounds().getWidth()) - SIZE * 0.04);
            lcdUnitString.setY(SIZE - (lcdValueString.getLayoutBounds().getHeight() * lcdDigitalFontSizeFactor) / 2.0);
            lcdUnitString.setFontSmoothingType(FontSmoothingType.LCD);
            lcdUnitString.getStyleClass().clear();
            lcdUnitString.getStyleClass().add("lcd");
            lcdUnitString.getStyleClass().add(control.getLcdDesign().CSS);
            lcdUnitString.getStyleClass().add("lcd-text");
            lcdValueOffsetRight = (lcdUnitString.getLayoutBounds().getWidth() + SIZE * 0.0833333333); // distance between value and unit
            lcdValueString.setX(LCD_MAIN.getX() + LCD_MAIN.getWidth() - lcdValueString.getLayoutBounds().getWidth() - lcdValueOffsetRight);
        } else {
            //lcdValueOffsetRight = SIZE * 0.04;// SIZE * 0.0151515152; // distance between value and right border
            lcdValueOffsetRight = SIZE * 0.0833333333;
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
        lcdValueBackgroundString.getStyleClass().clear();
        lcdValueBackgroundString.getStyleClass().add("lcd");
        lcdValueBackgroundString.getStyleClass().add(control.getLcdDesign().CSS);
        lcdValueBackgroundString.getStyleClass().add("lcd-text-background");
        lcdValueBackgroundString.setVisible(Gauge.LcdFont.LCD == control.getLcdValueFont());

        // Setup the font for the lcd title, number system, min measured, max measure and former value
        final Font LCD_TITLE_FONT = Font.font(control.getLcdTitleFont(), FontWeight.BOLD, (0.1666666667 * SIZE));
        final Font LCD_SMALL_FONT = Font.font("Verdana", FontWeight.NORMAL, (0.1666666667 * SIZE));
        // Title
        lcdTitle.setFont(LCD_TITLE_FONT);
        lcdTitle.setTextOrigin(VPos.BASELINE);
        lcdTitle.setTextAlignment(TextAlignment.CENTER);
        lcdTitle.setText(control.getTitle());
        lcdTitle.setX(LCD_MAIN.getLayoutX() + (LCD_MAIN.getLayoutBounds().getWidth() - lcdTitle.getLayoutBounds().getWidth()) / 2.0);
        lcdTitle.setY(LCD_MAIN.getLayoutY() + lcdTitle.getLayoutBounds().getHeight() + 0.04 * SIZE);
        lcdTitle.setFontSmoothingType(FontSmoothingType.LCD);
        lcdTitle.getStyleClass().clear();
        lcdTitle.getStyleClass().add("lcd");
        lcdTitle.getStyleClass().add(control.getLcdDesign().CSS);
        lcdTitle.getStyleClass().add("lcd-text");

        // NumberSystem
        lcdNumberSystem.setFont(LCD_SMALL_FONT);
        lcdNumberSystem.setTextOrigin(VPos.BASELINE);
        lcdNumberSystem.setTextAlignment(TextAlignment.RIGHT);
        lcdNumberSystem.setText(control.getLcdNumberSystem().toString());
        lcdNumberSystem.setX(LCD_MAIN.getLayoutX() + (LCD_MAIN.getLayoutBounds().getWidth() - lcdTitle.getLayoutBounds().getWidth()) / 2.0);
        lcdNumberSystem.setY(LCD_MAIN.getLayoutY() + LCD_MAIN.getHeight() - 0.0416666667 * SIZE);
        lcdNumberSystem.setFontSmoothingType(FontSmoothingType.LCD);
        lcdNumberSystem.getStyleClass().clear();
        lcdNumberSystem.getStyleClass().add("lcd");
        lcdNumberSystem.getStyleClass().add(control.getLcdDesign().CSS);
        lcdNumberSystem.getStyleClass().add("lcd-text");

        // Min measured value
        lcdMinMeasuredValue.setFont(LCD_SMALL_FONT);
        lcdMinMeasuredValue.setTextOrigin(VPos.BASELINE);
        lcdMinMeasuredValue.setTextAlignment(TextAlignment.RIGHT);
        lcdMinMeasuredValue.setX(LCD_MAIN.getLayoutX() + 0.0416666667 * SIZE);
        lcdMinMeasuredValue.setY(LCD_MAIN.getLayoutY() + lcdMinMeasuredValue.getLayoutBounds().getHeight() + 0.04 * SIZE);
        lcdMinMeasuredValue.setFontSmoothingType(FontSmoothingType.LCD);
        lcdMinMeasuredValue.getStyleClass().clear();
        lcdMinMeasuredValue.getStyleClass().add("lcd");
        lcdMinMeasuredValue.getStyleClass().add(control.getLcdDesign().CSS);
        lcdMinMeasuredValue.getStyleClass().add("lcd-text");

        // Max measured value
        lcdMaxMeasuredValue.setFont(LCD_SMALL_FONT);
        lcdMaxMeasuredValue.setTextOrigin(VPos.BASELINE);
        lcdMaxMeasuredValue.setTextAlignment(TextAlignment.RIGHT);
        lcdMaxMeasuredValue.setY(LCD_MAIN.getLayoutY() + lcdMinMeasuredValue.getLayoutBounds().getHeight() + 0.04 * SIZE);
        lcdMaxMeasuredValue.setFontSmoothingType(FontSmoothingType.LCD);
        lcdMaxMeasuredValue.getStyleClass().clear();
        lcdMaxMeasuredValue.getStyleClass().add("lcd");
        lcdMaxMeasuredValue.getStyleClass().add(control.getLcdDesign().CSS);
        lcdMaxMeasuredValue.getStyleClass().add("lcd-text");

        // Former value
        lcdFormerValue.setFont(LCD_SMALL_FONT);
        lcdFormerValue.setTextOrigin(VPos.BASELINE);
        lcdFormerValue.setTextAlignment(TextAlignment.CENTER);
        lcdFormerValue.setY(LCD_MAIN.getLayoutY() + LCD_MAIN.getHeight() - 0.0416666667 * SIZE);
        lcdFormerValue.setFontSmoothingType(FontSmoothingType.LCD);
        lcdFormerValue.getStyleClass().clear();
        lcdFormerValue.getStyleClass().add("lcd");
        lcdFormerValue.getStyleClass().add(control.getLcdDesign().CSS);
        lcdFormerValue.getStyleClass().add("lcd-text");
    }

    private boolean isNoOfDigitsValid() {
        final Rectangle LCD_MAIN     = new Rectangle(1.0, 1.0, control.getPrefWidth() - 2.0, control.getPrefHeight() - 2.0);
        final double AVAILABLE_WIDTH = LCD_MAIN.getWidth() - lcdValueOffsetLeft - lcdValueOffsetRight;
        final double NEEDED_WIDTH    = lcdValueString.getLayoutBounds().getWidth();

        return Double.compare(AVAILABLE_WIDTH, NEEDED_WIDTH) >= 0;
    }


    // ******************** Drawing *******************************************
    private void drawGlowOn() {
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
        if (GLOW_EFFECT.colorProperty().isBound()) {
            GLOW_EFFECT.colorProperty().unbind();
        }
        GLOW_EFFECT.colorProperty().bind(control.glowColorProperty());
        LCD_FRAME.effectProperty().set(GLOW_EFFECT);

        glowOn.getChildren().addAll(LCD_FRAME);
        glowOn.setCache(true);
    }

    private void drawLcd() {
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

        LCD_MAIN.getStyleClass().add(control.getLcdDesign().CSS);
        LCD_MAIN.getStyleClass().add("lcd-main");

        final InnerShadow INNER_GLOW = new InnerShadow();
        INNER_GLOW.setWidth(0.25 * SIZE);
        INNER_GLOW.setHeight(0.25 * SIZE);
        INNER_GLOW.setOffsetY(-0.05 * SIZE);
        INNER_GLOW.setOffsetX(0.0);
        INNER_GLOW.setColor(Color.color(1, 1, 1, 0.2));

        final InnerShadow INNER_SHADOW = new InnerShadow();
        INNER_SHADOW.setInput(INNER_GLOW);
        INNER_SHADOW.setWidth(0.15 * SIZE);
        INNER_SHADOW.setHeight(0.075 * SIZE);
        INNER_SHADOW.setOffsetY(0.025 * SIZE);
        INNER_SHADOW.setColor(Color.color(0, 0, 0, 0.65));

        LCD_MAIN.setEffect(INNER_SHADOW);

        // Switch lcd background off if needed
        if (!control.isLcdBackgroundVisible()) {
            LCD_FRAME.setVisible(false);
            LCD_MAIN.setVisible(false);
        }

        // Prepare the trend markers
        trendUp = new Path();
        trendUp.setFillRule(FillRule.EVEN_ODD);
        trendUp.getElements().add(new MoveTo(0.18181818181818182 * WIDTH, 0.9375 * HEIGHT));
        trendUp.getElements().add(new LineTo(0.21212121212121213 * WIDTH, 0.8125 * HEIGHT));
        trendUp.getElements().add(new LineTo(0.24242424242424243 * WIDTH, 0.9375 * HEIGHT));
        trendUp.getElements().add(new LineTo(0.18181818181818182 * WIDTH, 0.9375 * HEIGHT));
        trendUp.getElements().add(new ClosePath());
        trendUp.getStyleClass().clear();
        trendUp.getStyleClass().add("lcd");
        trendUp.getStyleClass().add(control.getLcdDesign().CSS);
        trendUp.getStyleClass().add("lcd-text");
        trendUp.setVisible(false);

        trendRising = new Path();
        trendRising.setFillRule(FillRule.EVEN_ODD);
        trendRising.getElements().add(new MoveTo(0.18181818181818182 * WIDTH, 0.8541666666666666 * HEIGHT));
        trendRising.getElements().add(new LineTo(0.24242424242424243 * WIDTH, 0.8125 * HEIGHT));
        trendRising.getElements().add(new LineTo(0.20454545454545456 * WIDTH, 0.9375 * HEIGHT));
        trendRising.getElements().add(new LineTo(0.18181818181818182 * WIDTH, 0.8541666666666666 * HEIGHT));
        trendRising.getElements().add(new ClosePath());
        trendRising.getStyleClass().clear();
        trendRising.getStyleClass().add("lcd");
        trendRising.getStyleClass().add(control.getLcdDesign().CSS);
        trendRising.getStyleClass().add("lcd-text");
        trendRising.setVisible(false);

        trendSteady = new Path();
        trendSteady.setFillRule(FillRule.EVEN_ODD);
        trendSteady.getElements().add(new MoveTo(0.18181818181818182 * WIDTH, 0.8125 * HEIGHT));
        trendSteady.getElements().add(new LineTo(0.24242424242424243 * WIDTH, 0.875 * HEIGHT));
        trendSteady.getElements().add(new LineTo(0.18181818181818182 * WIDTH, 0.9375 * HEIGHT));
        trendSteady.getElements().add(new LineTo(0.18181818181818182 * WIDTH, 0.8125 * HEIGHT));
        trendSteady.getElements().add(new ClosePath());
        trendSteady.getStyleClass().clear();
        trendSteady.getStyleClass().add("lcd");
        trendSteady.getStyleClass().add(control.getLcdDesign().CSS);
        trendSteady.getStyleClass().add("lcd-text");
        trendSteady.setVisible(false);

        trendFalling = new Path();
        trendFalling.setFillRule(FillRule.EVEN_ODD);
        trendFalling.getElements().add(new MoveTo(0.18181818181818182 * WIDTH, 0.8958333333333334 * HEIGHT));
        trendFalling.getElements().add(new LineTo(0.24242424242424243 * WIDTH, 0.9375 * HEIGHT));
        trendFalling.getElements().add(new LineTo(0.20454545454545456 * WIDTH, 0.8125 * HEIGHT));
        trendFalling.getElements().add(new LineTo(0.18181818181818182 * WIDTH, 0.8958333333333334 * HEIGHT));
        trendFalling.getElements().add(new ClosePath());
        trendFalling.getStyleClass().clear();
        trendFalling.getStyleClass().add("lcd");
        trendFalling.getStyleClass().add(control.getLcdDesign().CSS);
        trendFalling.getStyleClass().add("lcd-text");
        trendFalling.setVisible(false);

        trendDown = new Path();
        trendDown.setFillRule(FillRule.EVEN_ODD);
        trendDown.getElements().add(new MoveTo(0.18181818181818182 * WIDTH, 0.8125 * HEIGHT));
        trendDown.getElements().add(new LineTo(0.21212121212121213 * WIDTH, 0.9375 * HEIGHT));
        trendDown.getElements().add(new LineTo(0.24242424242424243 * WIDTH, 0.8125 * HEIGHT));
        trendDown.getElements().add(new LineTo(0.18181818181818182 * WIDTH, 0.8125 * HEIGHT));
        trendDown.getElements().add(new ClosePath());
        trendDown.getStyleClass().clear();
        trendDown.getStyleClass().add("lcd");
        trendDown.getStyleClass().add(control.getLcdDesign().CSS);
        trendDown.getStyleClass().add("lcd-text");
        trendDown.setVisible(false);

        // Prepare all font related parameters of the lcd
        prepareLcd();
        lcd.getChildren().addAll(LCD_FRAME, LCD_MAIN);

        // Prepare bargraph
        if (control.isBargraphVisible() && !control.isClockMode()) {
            final Path BAR_GRAPH_OFF = new Path();
            BAR_GRAPH_OFF.setFillRule(FillRule.EVEN_ODD);
            BAR_GRAPH_OFF.getElements().add(new MoveTo(0.9166666666666666 * WIDTH, 0.74 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new LineTo(0.9166666666666666 * WIDTH, 0.78 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new LineTo(0.946969696969697 * WIDTH, 0.78 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new LineTo(0.946969696969697 * WIDTH, 0.74 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new ClosePath());
            BAR_GRAPH_OFF.getElements().add(new MoveTo(0.8712121212121212 * WIDTH, 0.74 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new LineTo(0.8712121212121212 * WIDTH, 0.78 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new LineTo(0.9015151515151515 * WIDTH, 0.78 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new LineTo(0.9015151515151515 * WIDTH, 0.74 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new ClosePath());
            BAR_GRAPH_OFF.getElements().add(new MoveTo(0.8257575757575758 * WIDTH, 0.74 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new LineTo(0.8257575757575758 * WIDTH, 0.78 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new LineTo(0.8560606060606061 * WIDTH, 0.78 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new LineTo(0.8560606060606061 * WIDTH, 0.74 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new ClosePath());
            BAR_GRAPH_OFF.getElements().add(new MoveTo(0.7803030303030303 * WIDTH, 0.74 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new LineTo(0.7803030303030303 * WIDTH, 0.78 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new LineTo(0.8106060606060606 * WIDTH, 0.78 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new LineTo(0.8106060606060606 * WIDTH, 0.74 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new ClosePath());
            BAR_GRAPH_OFF.getElements().add(new MoveTo(0.7348484848484849 * WIDTH, 0.74 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new LineTo(0.7348484848484849 * WIDTH, 0.78 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new LineTo(0.7651515151515151 * WIDTH, 0.78 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new LineTo(0.7651515151515151 * WIDTH, 0.74 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new ClosePath());
            BAR_GRAPH_OFF.getElements().add(new MoveTo(0.6893939393939394 * WIDTH, 0.74 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new LineTo(0.6893939393939394 * WIDTH, 0.78 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new LineTo(0.7196969696969697 * WIDTH, 0.78 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new LineTo(0.7196969696969697 * WIDTH, 0.74 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new ClosePath());
            BAR_GRAPH_OFF.getElements().add(new MoveTo(0.6439393939393939 * WIDTH, 0.74 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new LineTo(0.6439393939393939 * WIDTH, 0.78 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new LineTo(0.6742424242424242 * WIDTH, 0.78 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new LineTo(0.6742424242424242 * WIDTH, 0.74 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new ClosePath());
            BAR_GRAPH_OFF.getElements().add(new MoveTo(0.5984848484848485 * WIDTH, 0.74 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new LineTo(0.5984848484848485 * WIDTH, 0.78 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new LineTo(0.6287878787878788 * WIDTH, 0.78 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new LineTo(0.6287878787878788 * WIDTH, 0.74 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new ClosePath());
            BAR_GRAPH_OFF.getElements().add(new MoveTo(0.553030303030303 * WIDTH, 0.74 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new LineTo(0.553030303030303 * WIDTH, 0.78 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new LineTo(0.5833333333333334 * WIDTH, 0.78 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new LineTo(0.5833333333333334 * WIDTH, 0.74 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new ClosePath());
            BAR_GRAPH_OFF.getElements().add(new MoveTo(0.5075757575757576 * WIDTH, 0.74 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new LineTo(0.5075757575757576 * WIDTH, 0.78 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new LineTo(0.5378787878787878 * WIDTH, 0.78 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new LineTo(0.5378787878787878 * WIDTH, 0.74 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new ClosePath());
            BAR_GRAPH_OFF.getElements().add(new MoveTo(0.4621212121212121 * WIDTH, 0.74 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new LineTo(0.4621212121212121 * WIDTH, 0.78 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new LineTo(0.49242424242424243 * WIDTH, 0.78 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new LineTo(0.49242424242424243 * WIDTH, 0.74 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new ClosePath());
            BAR_GRAPH_OFF.getElements().add(new MoveTo(0.4166666666666667 * WIDTH, 0.74 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new LineTo(0.4166666666666667 * WIDTH, 0.78 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new LineTo(0.44696969696969696 * WIDTH, 0.78 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new LineTo(0.44696969696969696 * WIDTH, 0.74 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new ClosePath());
            BAR_GRAPH_OFF.getElements().add(new MoveTo(0.3712121212121212 * WIDTH, 0.74 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new LineTo(0.3712121212121212 * WIDTH, 0.78 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new LineTo(0.4015151515151515 * WIDTH, 0.78 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new LineTo(0.4015151515151515 * WIDTH, 0.74 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new ClosePath());
            BAR_GRAPH_OFF.getElements().add(new MoveTo(0.32575757575757575 * WIDTH, 0.74 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new LineTo(0.32575757575757575 * WIDTH, 0.78 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new LineTo(0.3560606060606061 * WIDTH, 0.78 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new LineTo(0.3560606060606061 * WIDTH, 0.74 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new ClosePath());
            BAR_GRAPH_OFF.getElements().add(new MoveTo(0.2803030303030303 * WIDTH, 0.74 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new LineTo(0.2803030303030303 * WIDTH, 0.78 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new LineTo(0.3106060606060606 * WIDTH, 0.78 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new LineTo(0.3106060606060606 * WIDTH, 0.74 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new ClosePath());
            BAR_GRAPH_OFF.getElements().add(new MoveTo(0.23484848484848486 * WIDTH, 0.74 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new LineTo(0.23484848484848486 * WIDTH, 0.78 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new LineTo(0.26515151515151514 * WIDTH, 0.78 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new LineTo(0.26515151515151514 * WIDTH, 0.74 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new ClosePath());
            BAR_GRAPH_OFF.getElements().add(new MoveTo(0.1893939393939394 * WIDTH, 0.74 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new LineTo(0.1893939393939394 * WIDTH, 0.78 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new LineTo(0.2196969696969697 * WIDTH, 0.78 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new LineTo(0.2196969696969697 * WIDTH, 0.74 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new ClosePath());
            BAR_GRAPH_OFF.getElements().add(new MoveTo(0.14393939393939395 * WIDTH, 0.74 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new LineTo(0.14393939393939395 * WIDTH, 0.78 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new LineTo(0.17424242424242425 * WIDTH, 0.78 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new LineTo(0.17424242424242425 * WIDTH, 0.74 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new ClosePath());
            BAR_GRAPH_OFF.getElements().add(new MoveTo(0.09848484848484848 * WIDTH, 0.74 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new LineTo(0.09848484848484848 * WIDTH, 0.78 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new LineTo(0.12878787878787878 * WIDTH, 0.78 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new LineTo(0.12878787878787878 * WIDTH, 0.74 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new ClosePath());
            BAR_GRAPH_OFF.getElements().add(new MoveTo(0.05303030303030303 * WIDTH, 0.74 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new LineTo(0.05303030303030303 * WIDTH, 0.78 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new LineTo(0.08333333333333333 * WIDTH, 0.78 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new LineTo(0.08333333333333333 * WIDTH, 0.74 * HEIGHT));
            BAR_GRAPH_OFF.getElements().add(new ClosePath());
            BAR_GRAPH_OFF.getStyleClass().add(control.getLcdDesign().CSS);
            BAR_GRAPH_OFF.getStyleClass().add("lcd-text-background");

            final Rectangle SEG1 = new Rectangle(0.05303030303030303 * WIDTH, 0.74 * HEIGHT,
                                                 0.030303030303030304 * WIDTH, 0.04 * HEIGHT);
            SEG1.getStyleClass().add(control.getLcdDesign().CSS);
            SEG1.getStyleClass().add("lcd-text");
            SEG1.setVisible(false);

            final Rectangle SEG2 = new Rectangle(0.09848484848484848 * WIDTH, 0.74 * HEIGHT,
                                                 0.030303030303030304 * WIDTH, 0.04 * HEIGHT);
            SEG2.getStyleClass().add(control.getLcdDesign().CSS);
            SEG2.getStyleClass().add("lcd-text");
            SEG2.setVisible(false);

            final Rectangle SEG3 = new Rectangle(0.14393939393939395 * WIDTH, 0.74 * HEIGHT,
                                                 0.030303030303030304 * WIDTH, 0.04 * HEIGHT);
            SEG3.getStyleClass().add(control.getLcdDesign().CSS);
            SEG3.getStyleClass().add("lcd-text");
            SEG3.setVisible(false);

            final Rectangle SEG4 = new Rectangle(0.1893939393939394 * WIDTH, 0.74 * HEIGHT,
                                                 0.030303030303030304 * WIDTH, 0.04 * HEIGHT);
            SEG4.getStyleClass().add(control.getLcdDesign().CSS);
            SEG4.getStyleClass().add("lcd-text");
            SEG4.setVisible(false);

            final Rectangle SEG5 = new Rectangle(0.23484848484848486 * WIDTH, 0.74 * HEIGHT,
                                                 0.030303030303030304 * WIDTH, 0.04 * HEIGHT);
            SEG5.getStyleClass().add(control.getLcdDesign().CSS);
            SEG5.getStyleClass().add("lcd-text");
            SEG5.setVisible(false);

            final Rectangle SEG6 = new Rectangle(0.2803030303030303 * WIDTH, 0.74 * HEIGHT,
                                                 0.030303030303030304 * WIDTH, 0.04 * HEIGHT);
            SEG6.getStyleClass().add(control.getLcdDesign().CSS);
            SEG6.getStyleClass().add("lcd-text");
            SEG6.setVisible(false);

            final Rectangle SEG7 = new Rectangle(0.32575757575757575 * WIDTH, 0.74 * HEIGHT,
                                                 0.030303030303030304 * WIDTH, 0.04 * HEIGHT);
            SEG7.getStyleClass().add(control.getLcdDesign().CSS);
            SEG7.getStyleClass().add("lcd-text");
            SEG7.setVisible(false);

            final Rectangle SEG8 = new Rectangle(0.3712121212121212 * WIDTH, 0.74 * HEIGHT,
                                                 0.030303030303030304 * WIDTH, 0.04 * HEIGHT);
            SEG8.getStyleClass().add(control.getLcdDesign().CSS);
            SEG8.getStyleClass().add("lcd-text");
            SEG8.setVisible(false);

            final Rectangle SEG9 = new Rectangle(0.4166666666666667 * WIDTH, 0.74 * HEIGHT,
                                                 0.030303030303030304 * WIDTH, 0.04 * HEIGHT);
            SEG9.getStyleClass().add(control.getLcdDesign().CSS);
            SEG9.getStyleClass().add("lcd-text");
            SEG9.setVisible(false);

            final Rectangle SEG10 = new Rectangle(0.4621212121212121 * WIDTH, 0.74 * HEIGHT,
                                                  0.030303030303030304 * WIDTH, 0.04 * HEIGHT);
            SEG10.getStyleClass().add(control.getLcdDesign().CSS);
            SEG10.getStyleClass().add("lcd-text");
            SEG10.setVisible(false);

            final Rectangle SEG11 = new Rectangle(0.5075757575757576 * WIDTH, 0.74 * HEIGHT,
                                                  0.030303030303030304 * WIDTH, 0.04 * HEIGHT);
            SEG11.getStyleClass().add(control.getLcdDesign().CSS);
            SEG11.getStyleClass().add("lcd-text");
            SEG11.setVisible(false);

            final Rectangle SEG12 = new Rectangle(0.553030303030303 * WIDTH, 0.74 * HEIGHT,
                                                  0.030303030303030304 * WIDTH, 0.04 * HEIGHT);
            SEG12.getStyleClass().add(control.getLcdDesign().CSS);
            SEG12.getStyleClass().add("lcd-text");
            SEG12.setVisible(false);

            final Rectangle SEG13 = new Rectangle(0.5984848484848485 * WIDTH, 0.74 * HEIGHT,
                                                  0.030303030303030304 * WIDTH, 0.04 * HEIGHT);
            SEG13.getStyleClass().add(control.getLcdDesign().CSS);
            SEG13.getStyleClass().add("lcd-text");
            SEG13.setVisible(false);

            final Rectangle SEG14 = new Rectangle(0.6439393939393939 * WIDTH, 0.74 * HEIGHT,
                                                  0.030303030303030304 * WIDTH, 0.04 * HEIGHT);
            SEG14.getStyleClass().add(control.getLcdDesign().CSS);
            SEG14.getStyleClass().add("lcd-text");
            SEG14.setVisible(false);

            final Rectangle SEG15 = new Rectangle(0.6893939393939394 * WIDTH, 0.74 * HEIGHT,
                                                  0.030303030303030304 * WIDTH, 0.04 * HEIGHT);
            SEG15.getStyleClass().add(control.getLcdDesign().CSS);
            SEG15.getStyleClass().add("lcd-text");
            SEG15.setVisible(false);

            final Rectangle SEG16 = new Rectangle(0.7348484848484849 * WIDTH, 0.74 * HEIGHT,
                                                  0.030303030303030304 * WIDTH, 0.04 * HEIGHT);
            SEG16.getStyleClass().add(control.getLcdDesign().CSS);
            SEG16.getStyleClass().add("lcd-text");
            SEG16.setVisible(false);

            final Rectangle SEG17 = new Rectangle(0.7803030303030303 * WIDTH, 0.74 * HEIGHT,
                                                  0.030303030303030304 * WIDTH, 0.04 * HEIGHT);
            SEG17.getStyleClass().add(control.getLcdDesign().CSS);
            SEG17.getStyleClass().add("lcd-text");
            SEG17.setVisible(false);

            final Rectangle SEG18 = new Rectangle(0.8257575757575758 * WIDTH, 0.74 * HEIGHT,
                                                  0.030303030303030304 * WIDTH, 0.04 * HEIGHT);
            SEG18.getStyleClass().add(control.getLcdDesign().CSS);
            SEG18.getStyleClass().add("lcd-text");
            SEG18.setVisible(false);

            final Rectangle SEG19 = new Rectangle(0.8712121212121212 * WIDTH, 0.74 * HEIGHT,
                                                  0.030303030303030304 * WIDTH, 0.04 * HEIGHT);
            SEG19.getStyleClass().add(control.getLcdDesign().CSS);
            SEG19.getStyleClass().add("lcd-text");
            SEG19.setVisible(false);

            final Rectangle SEG20 = new Rectangle(0.9166666666666666 * WIDTH, 0.74 * HEIGHT,
                                                  0.030303030303030304 * WIDTH, 0.04 * HEIGHT);
            SEG20.getStyleClass().add(control.getLcdDesign().CSS);
            SEG20.getStyleClass().add("lcd-text");
            SEG20.setVisible(false);

            bargraph.clear();
            bargraph.add(SEG1);
            bargraph.add(SEG2);
            bargraph.add(SEG3);
            bargraph.add(SEG4);
            bargraph.add(SEG5);
            bargraph.add(SEG6);
            bargraph.add(SEG7);
            bargraph.add(SEG8);
            bargraph.add(SEG9);
            bargraph.add(SEG10);
            bargraph.add(SEG11);
            bargraph.add(SEG12);
            bargraph.add(SEG13);
            bargraph.add(SEG14);
            bargraph.add(SEG15);
            bargraph.add(SEG16);
            bargraph.add(SEG17);
            bargraph.add(SEG18);
            bargraph.add(SEG19);
            bargraph.add(SEG20);

            lcd.getChildren().add(BAR_GRAPH_OFF);
            lcd.getChildren().addAll(bargraph);


        }

        // Add lcd title string if visible
        lcd.getChildren().add(lcdTitle);

        // Add lcd number system string if visible
        lcd.getChildren().add(lcdNumberSystem);

        // Add lcd unit string if visible
        lcd.getChildren().add(lcdUnitString);

        // Add semitransparent lcd background text if digital font is used
        lcd.getChildren().add(lcdValueBackgroundString);

        // Add treshold indicator if visible
        lcdThresholdIndicator = createLcdThresholdIndicator(HEIGHT * 0.2045454545, HEIGHT * 0.2045454545);
        lcdThresholdIndicator.setTranslateX(0.04 * SIZE);
        lcdThresholdIndicator.setTranslateY(HEIGHT - lcdThresholdIndicator.getLayoutBounds().getHeight() - 0.0416666667 * SIZE);
        lcdThresholdIndicator.setVisible(control.isLcdThresholdVisible());

        lcd.getChildren().add(lcdThresholdIndicator);

        lcd.setCache(true);
    }

    private void drawLcdContent() {
        final double SIZE   = control.getPrefWidth() < control.getPrefHeight() ? control.getPrefWidth() : control.getPrefHeight();
        final double WIDTH  = control.getPrefWidth();
        final double HEIGHT = control.getPrefHeight();

        lcdContent.getChildren().clear();

        final Rectangle IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        IBOUNDS.setStroke(null);
        lcdContent.getChildren().add(IBOUNDS);

        final Rectangle LCD_MAIN = new Rectangle(1.0, 1.0, WIDTH - 2.0, HEIGHT - 2.0);

        // Update the lcd value
        if (control.isClockMode()) {
            lcdValueString.setText(lcdClockValue.get());
        } else {
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
            lcdNumberSystem.setY(LCD_MAIN.getLayoutY() + LCD_MAIN.getHeight() - 0.0416666667 * SIZE);

            if (!isNoOfDigitsValid()) {
                lcdValueString.setText("-E-");
            }
        }

        if (control.isLcdUnitVisible() && !control.isClockMode()) {
            lcdValueString.setX((LCD_MAIN.getX() + (LCD_MAIN.getWidth() - lcdValueString.getLayoutBounds().getWidth()) - lcdValueOffsetRight));
        } else {
            lcdValueString.setX((WIDTH - lcdValueString.getLayoutBounds().getWidth()) - lcdValueOffsetRight);
        }
        lcdValueString.setY(SIZE - (lcdValueString.getLayoutBounds().getHeight() * lcdDigitalFontSizeFactor) / 2.0);

        if (control.isBargraphVisible() && !bargraph.isEmpty()) {
            int activeBargraphSegments = (int) ((currentLcdValue.get() - (long) currentLcdValue.get()) * 20);
            for (int i = 0 ; i < 20 ; i++) {
                if (i <= activeBargraphSegments) {
                    bargraph.get(i).setVisible(true);
                } else {
                    bargraph.get(i).setVisible(false);
                }
            }
        }

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

        // Update the trend markers
        if (control.isTrendVisible()) {
            switch (control.getTrend()) {
                case UP:
                    trendUp.setVisible(true);
                    trendRising.setVisible(false);
                    trendSteady.setVisible(false);
                    trendFalling.setVisible(false);
                    trendDown.setVisible(false);
                    break;
                case RISING:
                    trendUp.setVisible(false);
                    trendRising.setVisible(true);
                    trendSteady.setVisible(false);
                    trendFalling.setVisible(false);
                    trendDown.setVisible(false);
                    break;
                case STEADY:
                    trendUp.setVisible(false);
                    trendRising.setVisible(false);
                    trendSteady.setVisible(true);
                    trendFalling.setVisible(false);
                    trendDown.setVisible(false);
                    break;
                case FALLING:
                    trendUp.setVisible(false);
                    trendRising.setVisible(false);
                    trendSteady.setVisible(false);
                    trendFalling.setVisible(true);
                    trendDown.setVisible(false);
                    break;
                case DOWN:
                    trendUp.setVisible(false);
                    trendRising.setVisible(false);
                    trendSteady.setVisible(false);
                    trendFalling.setVisible(false);
                    trendDown.setVisible(true);
                    break;
                default:
                    trendUp.setVisible(false);
                    trendRising.setVisible(false);
                    trendSteady.setVisible(false);
                    trendFalling.setVisible(false);
                    trendDown.setVisible(false);
                    break;
            }
        }

        if (control.isClockMode()) {
            lcdContent.getChildren().addAll(lcdValueString);
        } else {
            lcdContent.getChildren().addAll(lcdValueString,
                                            lcdMinMeasuredValue,
                                            lcdMaxMeasuredValue,
                                            lcdFormerValue,
                                            trendUp,
                                            trendRising,
                                            trendSteady,
                                            trendFalling,
                                            trendDown);
        }
    }

    private void updateLcdClock() {
        int hours   = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int minutes = Calendar.getInstance().get(Calendar.MINUTE);
        int seconds = Calendar.getInstance().get(Calendar.SECOND);
        String hourString   = hours < 10 ? "0" + Integer.toString(hours) : Integer.toString(hours);
        String minuteString = minutes < 10 ? "0" + Integer.toString(minutes) : Integer.toString(minutes);
        String secondString = seconds < 10 ? "0" + Integer.toString(seconds) : Integer.toString(seconds);

        if (control.isClockSecondsVisible()) {
            lcdClockValue.set(hourString + ":" + minuteString + ":" + secondString);
        } else {
            lcdClockValue.set(hourString + ":" + minuteString);
        }
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
        LCD_THRESHOLD.getStyleClass().add(control.getLcdDesign().CSS);
        LCD_THRESHOLD.getStyleClass().add("lcd-text");
        LCD_THRESHOLD.setStroke(null);

        INDICATOR.getChildren().addAll(LCD_THRESHOLD);

        return INDICATOR;
    }
}
