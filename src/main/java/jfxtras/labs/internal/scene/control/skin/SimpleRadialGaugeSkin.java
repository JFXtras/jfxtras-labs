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
package jfxtras.labs.internal.scene.control.skin;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.CacheHint;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import jfxtras.labs.internal.scene.control.behavior.SimpleRadialGaugeBehavior;
import jfxtras.labs.scene.control.gauge.Section;
import jfxtras.labs.scene.control.gauge.SimpleRadialGauge;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;


/**
 * Created with IntelliJ IDEA.
 * User: hansolo
 * Date: 10.09.12
 * Time: 15:16
 * To change this template use File | Settings | File Templates.
 */
public class SimpleRadialGaugeSkin extends GaugeSkinBase<SimpleRadialGauge, SimpleRadialGaugeBehavior> {
    private static final double INSETS = 4;
    private SimpleRadialGauge control;
    private boolean           isDirty;
    private boolean           initialized;
    private Canvas            canvas;
    private GraphicsContext   ctx;
    private Pane              gauge;
    private Point2D           center;
    private Arc               bar;
    private Text              valueText;
    private NumberFormat      valueFormat;
    private Text              unitText;
    private Timeline          timeline;
    private DoubleProperty    gaugeValue;
    private double            size;
    private Text              minLabel;
    private Text              maxLabel;
    private Stop[]            barGradientStops;
    private double            xy;
    private double            wh;
    private double            startAngle;
    private double            length;
    private Canvas            alertIndicator;


    // ******************** Constructors **************************************
    public SimpleRadialGaugeSkin(final SimpleRadialGauge CONTROL) {
        super(CONTROL, new SimpleRadialGaugeBehavior(CONTROL));
        control          = CONTROL;
        initialized      = false;
        isDirty          = false;
        canvas           = new Canvas();
        ctx              = canvas.getGraphicsContext2D();
        gauge            = new Pane();
        center           = new Point2D(100, 100);
        bar              = new Arc();
        valueText        = new Text(Double.toString(control.getValue()));
        valueFormat      = new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.US));
        unitText         = new Text(control.getUnit());
        timeline         = new Timeline();
        gaugeValue       = new SimpleDoubleProperty(control.getValue());
        size             = 200;
        minLabel         = new Text(valueFormat.format(control.getMinValue()));
        maxLabel         = new Text(valueFormat.format(control.getMaxValue()));
        xy               = (control.getBarWidth() + 2) / 2 + INSETS;
        wh               = size - 2 * INSETS - control.getBarWidth() - 2;
        startAngle       = -(150 - (360 - control.getRadialRange().ANGLE_RANGE) / 2);
        length           = control.getRadialRange().ANGLE_RANGE;
        barGradientStops = new Stop[] {
            new Stop(0, Color.TRANSPARENT),
            new Stop(0.8, control.getBarColor().darker()),
            new Stop(1.0, control.getBarColor().brighter())
        };
        alertIndicator   = createAlertIndicatorCanvas(control.getPrefWidth() * 0.165, control.getPrefHeight() * 0.135, control.getThresholdColor().COLOR);
        init();
    }


    // ******************** Initialization ************************************
    private void init() {
        if (control.getPrefWidth() < 0 | control.getPrefHeight() < 0) {
            control.setPrefSize(200, 200);
        }

        if (control.getMinWidth() < 0 | control.getMinHeight() < 0) {
            control.setMinSize(50, 50);
        }

        if (control.getMaxWidth() < 0 | control.getMaxHeight() < 0) {
            control.setMaxSize(1024, 1024);
        }

        center = new Point2D(control.getPrefWidth() / 2, control.getPrefHeight() / 2);

        // Register listeners
        registerChangeListener(control.widthProperty(), "WIDTH");
        registerChangeListener(control.heightProperty(), "HEIGHT");
        registerChangeListener(control.prefWidthProperty(), "WIDTH");
        registerChangeListener(control.prefHeightProperty(), "HEIGHT");
        registerChangeListener(control.minWidthProperty(), "WIDTH");
        registerChangeListener(control.minHeightProperty(), "HEIGHT");
        registerChangeListener(control.maxWidthProperty(), "WIDTH");
        registerChangeListener(control.maxHeightProperty(), "HEIGHT");
        registerChangeListener(control.minValueProperty(), "FULL_REPAINT");
        registerChangeListener(control.maxValueProperty(), "FULL_REPAINT");
        registerChangeListener(control.gaugeModelProperty(), "FULL_REPAINT");
        registerChangeListener(control.barFrameColorProperty(), "FULL_REPAINT");
        registerChangeListener(control.barBackgroundColorProperty(), "FULL_REPAINT");
        registerChangeListener(control.valueLabelColorProperty(), "FULL_REPAINT");
        registerChangeListener(control.unitLabelColorProperty(), "FULL_REPAINT");
        registerChangeListener(control.unitProperty(), "FULL_REPAINT");
        registerChangeListener(control.barColorProperty(), "BAR");
        registerChangeListener(control.barWidthProperty(), "BAR");
        registerChangeListener(control.roundedBarProperty(), "BAR");
        registerChangeListener(control.valueLabelFontSizeProperty(), "LABEL");
        registerChangeListener(control.noOfDecimalsProperty(), "LABEL");
        registerChangeListener(control.minLabelColorProperty(), "MIN_MAX_LABEL");
        registerChangeListener(control.maxLabelColorProperty(), "MIN_MAX_LABEL");
        registerChangeListener(control.minMaxLabelFontSizeProperty(), "MIN_MAX_LABEL");
        registerChangeListener(control.valueProperty(), "VALUE");
        registerChangeListener(control.minLabelVisibleProperty(), "MIN_LABEL_VISIBLE");
        registerChangeListener(gaugeValue, "GAUGE_VALUE");

        valueText.setFill(control.getValueLabelColor());
        unitText.setFill(control.getUnitLabelColor());
        minLabel.setFill(control.getMinLabelColor());
        maxLabel.setFill(control.getMaxLabelColor());

        addBindings();

        updateNumberFormat();

        if (control.isCanvasMode()) {
            for (Section section : control.getSections()) {
                if ((gaugeValue.get() - control.getMinValue()) > section.getStart() && (gaugeValue.get() - control.getMinValue()) < section.getStop()) {
                    barGradientStops = new Stop[] {
                        new Stop(0, Color.TRANSPARENT),
                        new Stop(0.8, section.getColor().darker()),
                        new Stop(1.0, section.getColor().brighter())
                    };
                    break;
                } else {
                    barGradientStops = new Stop[] {
                        new Stop(0, Color.TRANSPARENT),
                        new Stop(0.8, control.getBarColor().darker()),
                        new Stop(1.0, control.getBarColor().brighter())
                    };
                }
            }
        }

        initialized = true;
        repaint();
    }

    private void addBindings() {
        // Don't show bar if value is smaller or equal minValue
        if (bar.visibleProperty().isBound()) {
            bar.visibleProperty().unbind();
        }
        bar.visibleProperty().bind(gaugeValue.greaterThan(control.minValueProperty()));

        if (minLabel.visibleProperty().isBound()) {
            minLabel.visibleProperty().unbind();
        }
        minLabel.visibleProperty().bind(control.minLabelVisibleProperty());

        if (maxLabel.visibleProperty().isBound()) {
            maxLabel.visibleProperty().unbind();
        }
        maxLabel.visibleProperty().bind(control.maxLabelVisibleProperty());
        
        if (valueText.visibleProperty().isBound()) {
            valueText.visibleProperty().unbind();
        }
        valueText.visibleProperty().bind(control.valueLabelVisibleProperty());

        if (unitText.visibleProperty().isBound()) {
            unitText.visibleProperty().unbind();
        }
        unitText.visibleProperty().bind(control.unitLabelVisibleProperty());

        if (valueText.fillProperty().isBound()) {
            valueText.fillProperty().unbind();
        }
        valueText.fillProperty().bind(control.valueLabelColorProperty());

        if (unitText.fillProperty().isBound()) {
            unitText.fillProperty().unbind();
        }
        unitText.fillProperty().bind(control.unitLabelColorProperty());
    }


    // ******************** Methods *******************************************
    @Override protected void handleControlPropertyChanged(final String PROPERTY) {
        super.handleControlPropertyChanged(PROPERTY);
        if ("WIDTH".equals(PROPERTY)) {
            recalcParameters();
            valueText.setLayoutX((size - valueText.getLayoutBounds().getWidth()) / 2);
            valueText.setLayoutY((size - valueText.getLayoutBounds().getHeight()) / 2 + control.getValueLabelFontSize());
            canvas.setWidth(size);
            canvas.setHeight(size);
            alertIndicator = createAlertIndicatorCanvas(control.getPrefWidth() * 0.165, control.getPrefHeight() * 0.135, control.getThresholdColor().COLOR);
            repaint();
        } else if ("HEIGHT".equals(PROPERTY)) {
            recalcParameters();
            valueText.setLayoutX((size - valueText.getLayoutBounds().getWidth()) / 2);
            valueText.setLayoutY((size - valueText.getLayoutBounds().getHeight()) / 2 + control.getValueLabelFontSize());
            canvas.setWidth(size);
            canvas.setHeight(size);
            alertIndicator = createAlertIndicatorCanvas(control.getPrefWidth() * 0.165, control.getPrefHeight() * 0.135, control.getThresholdColor().COLOR);
            repaint();
        } else if ("FULL_REPAINT".equals(PROPERTY)) {
            alertIndicator = createAlertIndicatorCanvas(control.getPrefWidth() * 0.165, control.getPrefHeight() * 0.135, control.getThresholdColor().COLOR);
            repaint();
        } else if ("VALUE".equals(PROPERTY)) {
            if (control.isValueAnimationEnabled()) {
                final KeyValue KEY_VALUE = new KeyValue(gaugeValue, control.getValue(), Interpolator.EASE_BOTH);
                final KeyFrame KEY_FRAME = new KeyFrame(Duration.millis(control.getTimeToValueInMs()), KEY_VALUE);
                timeline.setOnFinished(new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent actionEvent) {
                        gaugeValue.set(control.getValue());
                    }
                });
                timeline = new Timeline();
                timeline.getKeyFrames().add(KEY_FRAME);
                timeline.play();
            } else {
                gaugeValue.set(control.getValue());
            }
        } else if ("BAR".equals(PROPERTY)) {
            if (control.isCanvasMode()) {
                barGradientStops = new Stop[] {
                    new Stop(0, Color.TRANSPARENT),
                    new Stop(0.8, control.getBarColor().darker()),
                    new Stop(1.0, control.getBarColor().brighter())
                };
                drawCanvasGauge(ctx);
            } else {
                drawNodeGauge();
            }
        } else if ("LABEL".equals(PROPERTY)) {
            updateNumberFormat();
            repaint();
        } else if ("MIN_MAX_LABEL".equals(PROPERTY)) {
            minLabel.setFill(control.getMinLabelColor());
            minLabel.setFont(Font.font("Verdana", control.getMinMaxLabelFontSize()));
            maxLabel.setFill(control.getMaxLabelColor());
            maxLabel.setFont(Font.font("Verdana", control.getMinMaxLabelFontSize()));
            repaint();
        } else if ("GAUGE_VALUE".equals(PROPERTY)) {
            if (!control.getSections().isEmpty()) {
                for (Section section : control.getSections()) {
                    if ((gaugeValue.get() - control.getMinValue()) > section.getStart() && (gaugeValue.get() - control.getMinValue()) < section.getStop()) {
                        updateBarColor(section.getColor());
                        break;
                    } else {
                        updateBarColor(control.getBarColor());
                    }
                }
            }
            valueText.setText(valueFormat.format(gaugeValue.get()));
            valueText.setLayoutX((size - valueText.getLayoutBounds().getWidth()) / 2);
            valueText.setLayoutY((size - valueText.getLayoutBounds().getHeight()) / 2 + control.getValueLabelFontSize());
            bar.setLength(-gaugeValue.get() * control.getAngleStep());

            if (control.isThresholdBehaviorInverted() && gaugeValue.doubleValue() < control.getThreshold()) {
                control.setThresholdExceeded(true);
            } else if (!control.isThresholdBehaviorInverted() && gaugeValue.doubleValue() > control.getThreshold()) {
                control.setThresholdExceeded(true);
            } else {
                control.setThresholdExceeded(false);
            }
            alertIndicator.setVisible(control.isThresholdExceeded());

            if (control.isCanvasMode()) {
                for (Section section : control.getSections()) {
                    if ((gaugeValue.get() - control.getMinValue()) > section.getStart() && (gaugeValue.get() - control.getMinValue()) < section.getStop()) {
                        barGradientStops = new Stop[] {
                            new Stop(0, Color.TRANSPARENT),
                            new Stop(0.8, section.getColor().darker()),
                            new Stop(1.0, section.getColor().brighter())
                        };
                        break;
                    } else {
                        barGradientStops = new Stop[] {
                            new Stop(0, Color.TRANSPARENT),
                            new Stop(0.8, control.getBarColor().darker()),
                            new Stop(1.0, control.getBarColor().brighter())
                        };
                    }
                }
                drawCanvasGauge(ctx);
            }
        }
    }

    public final void repaint() {
        isDirty = true;
        requestLayout();
    }

    @Override public void layoutChildren() {
        if (!isDirty) {
            return;
        }
        if (!initialized) {
            init();
        }
        if (control.isCanvasMode()) {
            alertIndicator.setLayoutX((size - alertIndicator.getWidth()) * 0.5);
            alertIndicator.setLayoutY(size * 0.6);
            drawCanvasGauge(ctx);
            getChildren().setAll(canvas, alertIndicator);
        } else {
            drawNodeGauge();
            getChildren().setAll(gauge);
        }

        isDirty = false;

        super.layoutChildren();
    }

    @Override public final SimpleRadialGauge getSkinnable() {
        return control;
    }

    @Override public final void dispose() {
        control = null;
    }

    @Override protected double computePrefWidth(final double PREF_HEIGHT) {
        double prefWidth = 200;
        if (PREF_HEIGHT != -1) {
            prefWidth = Math.max(0, PREF_HEIGHT - getInsets().getLeft() - getInsets().getRight());
        }
        return super.computePrefWidth(prefWidth);
    }

    @Override protected double computePrefHeight(final double PREF_WIDTH) {
        double prefHeight = 200;
        if (PREF_WIDTH != -1) {
            prefHeight = Math.max(0, PREF_WIDTH - getInsets().getTop() - getInsets().getBottom());
        }
        return super.computePrefHeight(prefHeight);
    }

    @Override protected double computeMinWidth(final double MIN_HEIGHT) {
        return super.computeMinWidth(Math.max(50, MIN_HEIGHT - getInsets().getLeft() - getInsets().getRight()));
    }

    @Override protected double computeMinHeight(final double MIN_WIDTH) {
        return super.computeMinHeight(Math.max(50, MIN_WIDTH - getInsets().getTop() - getInsets().getBottom()));
    }

    @Override protected double computeMaxWidth(final double MAX_HEIGHT) {
        return super.computeMaxWidth(Math.max(200, MAX_HEIGHT - getInsets().getLeft() - getInsets().getRight()));
    }

    @Override protected double computeMaxHeight(final double MAX_WIDTH) {
        return super.computeMaxHeight(Math.max(200, MAX_WIDTH - getInsets().getTop() - getInsets().getBottom()));
    }

    private void updateNumberFormat() {
        StringBuilder noOfDecimals = new StringBuilder(5);
        if (control.getNoOfDecimals() == 0) {
            noOfDecimals.append("0");
        } else {
            noOfDecimals.append("0.");
            for (int i = 0 ; i < control.getNoOfDecimals() ; i++) {
                noOfDecimals.append("0");
            }
        }
        valueFormat = new DecimalFormat(noOfDecimals.toString(), new DecimalFormatSymbols(Locale.US));
    }

    private void updateBarColor(final Color BAR_COLOR) {
        final double RADIUS = size / 2 - 4;
        bar.setStroke(new RadialGradient(0, 0,
                                         center.getX(), center.getY(),
                                         RADIUS, false, CycleMethod.NO_CYCLE,
                                         new Stop(0.0, control.getBarColor()),
                                         new Stop((RADIUS - control.getBarWidth()) / RADIUS, Color.TRANSPARENT),
                                         new Stop((RADIUS - control.getBarWidth() + 1) / RADIUS, BAR_COLOR.darker()),
                                         new Stop((RADIUS - control.getBarWidth() / 2) / RADIUS, BAR_COLOR),
                                         new Stop(1.0, BAR_COLOR.deriveColor(0.85, 0.85, 0.85, 1))));
    }

    private void recalcParameters() {
        size             = control.getPrefWidth() < control.getPrefHeight() ? control.getPrefWidth() : control.getPrefHeight();
        xy               = (control.getBarWidth() + 2) / 2 + INSETS;
        wh               = size - 2 * INSETS - control.getBarWidth() - 2;
        length           = control.getRadialRange().ANGLE_RANGE;
        startAngle       = -(150 - (360 - length) / 2);
    }


    // ******************** Drawing related ***********************************
    private void drawNodeGauge() {
        size  = control.getPrefWidth() < control.getPrefHeight() ? control.getPrefWidth() : control.getPrefHeight();

        final double RADIUS     = size / 2 - 4;
        final double ARC_RADIUS = RADIUS - control.getBarWidth() / 2;
        center                  = new Point2D(size / 2, size / 2);

        gauge.getChildren().clear();

        final InnerShadow INNER_SHADOW = new InnerShadow();
        INNER_SHADOW.setRadius(0.01 * size);
        INNER_SHADOW.setBlurType(BlurType.GAUSSIAN);
        INNER_SHADOW.setColor(Color.rgb(0, 0, 0, 0.65));

        final InnerShadow INNER_GLOW = new InnerShadow();
        INNER_GLOW.setRadius(0.005 * size);
        INNER_GLOW.setBlurType(BlurType.GAUSSIAN);
        INNER_GLOW.setOffsetY(-1);
        INNER_GLOW.setColor(Color.color(1, 1, 1, 0.65));

        final Arc BAR_BACKGROUND = new Arc();
        BAR_BACKGROUND.setCenterX(center.getX());
        BAR_BACKGROUND.setCenterY(center.getY());
        BAR_BACKGROUND.setRadiusX(ARC_RADIUS);
        BAR_BACKGROUND.setRadiusY(ARC_RADIUS);
        BAR_BACKGROUND.setStartAngle(-(90 - (360 - control.getRadialRange().ANGLE_RANGE) / 2));
        BAR_BACKGROUND.setLength(control.getRadialRange().ANGLE_RANGE);
        BAR_BACKGROUND.setType(ArcType.OPEN);
        BAR_BACKGROUND.setFill(null);
        BAR_BACKGROUND.setSmooth(true);
        BAR_BACKGROUND.setStroke(control.getBarBackgroundColor());
        BAR_BACKGROUND.setStrokeWidth(control.getBarWidth());
        if (control.isRoundedBar()) {
            BAR_BACKGROUND.setStrokeLineCap(StrokeLineCap.ROUND);
        } else {
            BAR_BACKGROUND.setStrokeLineCap(StrokeLineCap.BUTT);
        }
        BAR_BACKGROUND.setEffect(INNER_SHADOW);

        bar.setCenterX(center.getX());
        bar.setCenterY(center.getY());
        bar.setRadiusX(BAR_BACKGROUND.getRadiusX());
        bar.setRadiusY(BAR_BACKGROUND.getRadiusY());
        bar.setStartAngle(control.getRadialRange().ANGLE_RANGE - (90 - (360 - control.getRadialRange().ANGLE_RANGE) / 2));
        bar.setLength(-gaugeValue.get() * control.getAngleStep());
        bar.setType(ArcType.OPEN);
        bar.setFill(null);
        bar.setSmooth(true);
        if (!control.getSections().isEmpty()) {
            for (Section section : control.getSections()) {
                if ((gaugeValue.get() - control.getMinValue()) > section.getStart() && (gaugeValue.get() - control.getMinValue()) < section.getStop()) {
                    updateBarColor(section.getColor());
                    break;
                } else {
                    updateBarColor(control.getBarColor());
                }
            }
        }
        if (control.isRoundedBar()) {
            bar.setStrokeLineCap(StrokeLineCap.ROUND);
        } else {
            bar.setStrokeLineCap(StrokeLineCap.BUTT);
        }
        bar.setStrokeWidth(control.getBarWidth());
        bar.setEffect(INNER_SHADOW);

        valueText.setFont(Font.font("Verdana", FontWeight.BOLD, control.getValueLabelFontSize()));
        //valueText.setFill(control.getValueLabelColor());
        valueText.setTextAlignment(TextAlignment.CENTER);
        valueText.setTextOrigin(VPos.BASELINE);
        valueText.setText(valueFormat.format(gaugeValue.get()));
        valueText.setLayoutX((size - valueText.getLayoutBounds().getWidth()) / 2);
        valueText.setLayoutY((size - valueText.getLayoutBounds().getHeight()) / 2 + control.getValueLabelFontSize());
        valueText.setEffect(INNER_GLOW);

        unitText.setFont(Font.font("Verdana", FontWeight.BOLD, control.getUnitLabelFontSize()));
        //unitText.setFill(control.getUnitLabelColor());
        unitText.setTextAlignment(TextAlignment.CENTER);
        unitText.setTextOrigin(VPos.BOTTOM);
        unitText.setText(control.getUnit());
        unitText.setLayoutX((size - unitText.getLayoutBounds().getWidth()) / 2);
        unitText.setLayoutY((size - unitText.getLayoutBounds().getHeight()));
        unitText.setEffect(INNER_GLOW);

        minLabel.setFont(Font.font("Verdana", FontWeight.NORMAL, control.getMinMaxLabelFontSize()));
        minLabel.setFill(control.getMinLabelColor());
        minLabel.setTextAlignment(TextAlignment.CENTER);
        minLabel.setTextOrigin(VPos.BOTTOM);
        minLabel.setText(valueFormat.format(control.getMinValue()));
        minLabel.setLayoutX(size * 0.025);
        minLabel.setLayoutY(size - minLabel.getLayoutBounds().getHeight() - (size * 0.025));

        maxLabel.setFont(Font.font("Verdana", FontWeight.NORMAL, control.getMinMaxLabelFontSize()));
        maxLabel.setFill(control.getMaxLabelColor());
        maxLabel.setTextAlignment(TextAlignment.CENTER);
        maxLabel.setTextOrigin(VPos.BOTTOM);
        maxLabel.setText(valueFormat.format(control.getMaxValue()));
        maxLabel.setLayoutX(size - maxLabel.getLayoutBounds().getWidth() - (size * 0.025));
        maxLabel.setLayoutY(size - maxLabel.getLayoutBounds().getHeight() - (size * 0.025));

        alertIndicator.setLayoutX((size - alertIndicator.getWidth()) * 0.5);
        alertIndicator.setLayoutY(size * 0.6);

        gauge.getChildren().addAll(BAR_BACKGROUND,
                                   bar,
                                   valueText,
                                   unitText,
                                   minLabel,
                                   maxLabel,
                                   alertIndicator);
        gauge.setCache(true);
        gauge.setCacheHint(CacheHint.QUALITY);
    }

    private void drawCanvasGauge(final GraphicsContext CTX) {
        //size  = control.getPrefWidth() < control.getPrefHeight() ? control.getPrefWidth() : control.getPrefHeight();

        //canvas.setWidth(size);
        //canvas.setHeight(size);

        //final double INSETS       = 4;
        //final double xy           = (control.getBarWidth() + 2) / 2 + INSETS;
        //final double wh           = size - 2 * INSETS - control.getBarWidth() - 2;
        //final double startAngle   = -(150 - (360 - control.getAngleRange()) / 2);
        //final double length       = control.getAngleRange();

        CTX.clearRect(0, 0, size, size);
        CTX.setStroke(control.getBarFrameColor());
        CTX.setLineWidth(control.getBarWidth() + 2);
        if (control.isRoundedBar()) {
            CTX.setLineCap(StrokeLineCap.ROUND);
        } else {
            CTX.setLineCap(StrokeLineCap.BUTT);
        }
        CTX.strokeArc(xy, xy, wh, wh, startAngle, -length, ArcType.OPEN);

        CTX.setStroke(control.getBarBackgroundColor());
        CTX.setLineWidth(control.getBarWidth());
        if (control.isRoundedBar()) {
            CTX.setLineCap(StrokeLineCap.ROUND);
        } else {
            CTX.setLineCap(StrokeLineCap.BUTT);
        }
        CTX.strokeArc(xy, xy, wh, wh, startAngle, -length, ArcType.OPEN);

        //CTX.setStroke(control.getBarColor());
        CTX.setStroke(new RadialGradient(0, 0,
                                         0.5 * size, 0.5 * size,
                                         0.5 * wh,
                                         false, CycleMethod.NO_CYCLE,
                                         barGradientStops));
        CTX.setLineWidth(control.getBarWidth());
        if (control.isRoundedBar()) {
            CTX.setLineCap(StrokeLineCap.ROUND);
        } else {
            CTX.setLineCap(StrokeLineCap.BUTT);
        }
        CTX.strokeArc(xy, xy, wh, wh, startAngle, -gaugeValue.doubleValue() * control.getAngleStep(), ArcType.OPEN);

        if (control.isValueLabelVisible()) {
            CTX.setFont(Font.font("Verdana", FontWeight.BOLD, control.getValueLabelFontSize()));
            CTX.setFill(control.getValueLabelColor());
            CTX.setTextAlign(TextAlignment.CENTER);
            CTX.setTextBaseline(VPos.CENTER);
            CTX.fillText(valueFormat.format(gaugeValue.doubleValue()), size / 2, size / 2);
        }

        if (control.isUnitLabelVisible() && !control.getUnit().isEmpty()) {
            CTX.setFont(Font.font("Verdana", FontWeight.BOLD, control.getUnitLabelFontSize()));
            CTX.setFill(control.getUnitLabelColor());
            CTX.setTextAlign(TextAlignment.CENTER);
            CTX.setTextBaseline(VPos.BOTTOM);
            CTX.fillText(control.getUnit(), size / 2, size - control.getUnitLabelFontSize());
        }
    }
}