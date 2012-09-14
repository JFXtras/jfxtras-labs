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
import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import jfxtras.labs.internal.scene.control.behavior.SimpleGaugeBehavior;
import jfxtras.labs.scene.control.gauge.Section;
import jfxtras.labs.scene.control.gauge.SimpleGauge;

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
public class SimpleGaugeSkin extends GaugeSkinBase<SimpleGauge, SimpleGaugeBehavior> {
    private SimpleGauge    control;
    private boolean        isDirty;
    private boolean        initialized;
    private Pane           gauge;
    private Point2D        center;
    private Arc            bar;
    private Text           valueText;
    private NumberFormat   valueFormat;
    private Text           unitText;
    private Timeline       timeline;
    private DoubleProperty gaugeValue;
    private double         size;


    // ******************** Constructors **************************************
    public SimpleGaugeSkin(final SimpleGauge CONTROL) {
        super(CONTROL, new SimpleGaugeBehavior(CONTROL));
        control        = CONTROL;
        initialized    = false;
        isDirty        = false;
        gauge          = new Pane();
        center         = new Point2D(100, 100);
        bar            = new Arc();
        valueText      = new Text();
        valueFormat    = new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.US));
        unitText       = new Text();
        timeline       = new Timeline();
        gaugeValue     = new SimpleDoubleProperty(control.getValue());
        size           = 200;

        init();
    }

    private void init() {
        if (control.getPrefWidth() < 0 | control.getPrefHeight() < 0) {
            control.setPrefSize(200, 200);
        }
        center = new Point2D(control.getPrefWidth() / 2, control.getPrefHeight() / 2);

        // Register listeners
        registerChangeListener(control.prefWidthProperty(), "PREF_WIDTH");
        registerChangeListener(control.prefHeightProperty(), "PREF_HEIGHT");
        registerChangeListener(control.minValueProperty(), "FULL_REPAINT");
        registerChangeListener(control.maxValueProperty(), "FULL_REPAINT");
        registerChangeListener(control.gaugeModelProperty(), "FULL_REPAINT");
        registerChangeListener(control.barBackgroundColorProperty(), "FULL_REPAINT");
        registerChangeListener(control.labelColorProperty(), "FULL_REPAINT");
        registerChangeListener(control.unitColorProperty(), "FULL_REPAINT");
        registerChangeListener(control.unitProperty(), "FULL_REPAINT");
        registerChangeListener(control.barColorProperty(), "BAR");
        registerChangeListener(control.barWidthProperty(), "BAR");
        registerChangeListener(control.labelFontSizeProperty(), "LABEL");
        registerChangeListener(control.noOfDecimalsProperty(), "LABEL");
        registerChangeListener(control.valueProperty(), "VALUE");
        registerChangeListener(gaugeValue, "GAUGE_VALUE");

        updateNumberFormat();

        initialized = true;
        repaint();
    }


    // ******************** Methods *******************************************
    @Override protected void handleControlPropertyChanged(final String PROPERTY) {
        super.handleControlPropertyChanged(PROPERTY);
        if ("PREF_WIDTH".equals(PROPERTY)) {
            size  = control.getPrefWidth() < control.getPrefHeight() ? control.getPrefWidth() : control.getPrefHeight();
            valueText.setLayoutX((size - valueText.getLayoutBounds().getWidth()) / 2);
            valueText.setLayoutY((size - valueText.getLayoutBounds().getHeight()) / 2 + control.getLabelFontSize());
            repaint();
        } else if ("PREF_HEIGHT".equals(PROPERTY)) {
            size  = control.getPrefWidth() < control.getPrefHeight() ? control.getPrefWidth() : control.getPrefHeight();
            valueText.setLayoutX((size - valueText.getLayoutBounds().getWidth()) / 2);
            valueText.setLayoutY((size - valueText.getLayoutBounds().getHeight()) / 2 + control.getLabelFontSize());
            repaint();
        } else if ("FULL_REPAINT".equals(PROPERTY)) {
            repaint();
        } else if ("VALUE".equals(PROPERTY)) {
            if (control.isValueAnimationEnabled()) {
                final KeyValue KEY_VALUE = new KeyValue(gaugeValue, control.getValue(), Interpolator.SPLINE(0.5, 0.4, 0.4, 1.0));
                final KeyFrame KEY_FRAME = new KeyFrame(Duration.millis(1500), KEY_VALUE);
                timeline.setOnFinished(new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent actionEvent) {
                        gaugeValue.set(control.getValue());
                    }
                });
                timeline    = new Timeline();
                timeline.getKeyFrames().add(KEY_FRAME);
                timeline.play();
            } else {
                gaugeValue.set(control.getValue());
            }
        } else if ("BAR".equals(PROPERTY)) {
            drawGauge();
        } else if ("LABEL".equals(PROPERTY)) {
            updateNumberFormat();
            repaint();
        } else if ("GAUGE_VALUE".equals(PROPERTY)) {
            if (!control.getSections().isEmpty()) {
                for (Section section : control.getSections()) {
                    if (gaugeValue.get() > section.getStart() && gaugeValue.get() < section.getStop()) {
                        updateBarColor(section.getColor());
                        break;
                    } else {
                        updateBarColor(control.getBarColor());
                    }
                }
            }
            valueText.setText(valueFormat.format(gaugeValue.get()));
            valueText.setLayoutX((size - valueText.getLayoutBounds().getWidth()) / 2);
            valueText.setLayoutY((size - valueText.getLayoutBounds().getHeight()) / 2 + control.getLabelFontSize());
            bar.setLength(-gaugeValue.get() * control.getAngleStep());
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
        getChildren().clear();
        drawGauge();
        getChildren().addAll(gauge);
        isDirty = false;

        super.layoutChildren();
    }

    @Override public final SimpleGauge getSkinnable() {
        return control;
    }

    @Override public final void dispose() {
        control = null;
    }

    @Override protected double computePrefWidth(final double PREF_WIDTH) {
        double prefWidth = 200;
        if (PREF_WIDTH != -1) {
            prefWidth = Math.max(0, PREF_WIDTH - getInsets().getLeft() - getInsets().getRight());
        }
        return super.computePrefWidth(prefWidth);
    }

    @Override protected double computePrefHeight(final double PREF_HEIGHT) {
        double prefHeight = 200;
        if (PREF_HEIGHT != -1) {
            prefHeight = Math.max(0, PREF_HEIGHT - getInsets().getTop() - getInsets().getBottom());
        }
        return super.computePrefHeight(prefHeight);
    }

    @Override protected double computeMinWidth(final double MIN_WIDTH) {
        return super.computeMinWidth(Math.max(200, MIN_WIDTH - getInsets().getLeft() - getInsets().getRight()));
    }

    @Override protected double computeMinHeight(final double MIN_HEIGHT) {
        return super.computeMinHeight(Math.max(200, MIN_HEIGHT - getInsets().getTop() - getInsets().getBottom()));
    }

    @Override protected double computeMaxWidth(final double MAX_WIDTH) {
        return super.computeMaxWidth(Math.max(200, MAX_WIDTH - getInsets().getLeft() - getInsets().getRight()));
    }

    @Override protected double computeMaxHeight(final double MAX_HEIGHT) {
        return super.computeMaxHeight(Math.max(200, MAX_HEIGHT - getInsets().getTop() - getInsets().getBottom()));
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
                                         new Stop(1.0, BAR_COLOR)));
    }


    // ******************** Drawing related ***********************************
    public final void drawGauge() {
        size  = control.getPrefWidth() < control.getPrefHeight() ? control.getPrefWidth() : control.getPrefHeight();

        final double RADIUS = size / 2 - 4;
        center              = new Point2D(size / 2, size / 2);

        gauge.getChildren().clear();

        final Shape IBOUNDS = new Rectangle(0, 0, size, size);
        IBOUNDS.setOpacity(0.0);

        final InnerShadow INNER_SHADOW = new InnerShadow();
        INNER_SHADOW.setRadius(0.01 * size);
        INNER_SHADOW.setBlurType(BlurType.ONE_PASS_BOX);
        INNER_SHADOW.setColor(Color.rgb(0, 0, 0, 0.65));

        final InnerShadow INNER_GLOW = new InnerShadow();
        INNER_GLOW.setRadius(0.005 * size);
        INNER_GLOW.setBlurType(BlurType.GAUSSIAN);
        INNER_GLOW.setOffsetY(-1);
        INNER_GLOW.setColor(Color.color(1, 1, 1, 0.65));

        final Arc BAR_BACKGROUND = new Arc();
        BAR_BACKGROUND.setCenterX(center.getX());
        BAR_BACKGROUND.setCenterY(center.getY());
        BAR_BACKGROUND.setRadiusX(RADIUS - control.getBarWidth() / 2);
        BAR_BACKGROUND.setRadiusY(RADIUS - control.getBarWidth() / 2);
        BAR_BACKGROUND.setStartAngle(-(90 - (360 - control.getRadialRange().ANGLE_RANGE) / 2));
        BAR_BACKGROUND.setLength(control.getRadialRange().ANGLE_RANGE);
        BAR_BACKGROUND.setType(ArcType.OPEN);
        BAR_BACKGROUND.setFill(null);
        BAR_BACKGROUND.setSmooth(true);
        BAR_BACKGROUND.setStroke(control.getBarBackgroundColor());
        BAR_BACKGROUND.setStrokeWidth(control.getBarWidth());
        BAR_BACKGROUND.setStrokeLineCap(StrokeLineCap.ROUND);
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
        updateBarColor(control.getBarColor());
        bar.setStrokeLineCap(StrokeLineCap.ROUND);
        bar.setStrokeWidth(control.getBarWidth());
        bar.setEffect(INNER_SHADOW);

        valueText.setFont(Font.font("Verdana", FontWeight.BOLD, control.getLabelFontSize()));
        valueText.setFill(control.getLabelColor());
        valueText.setTextAlignment(TextAlignment.CENTER);
        valueText.setTextOrigin(VPos.BOTTOM);
        valueText.setText(valueFormat.format(gaugeValue.doubleValue()));
        valueText.setLayoutX((size - valueText.getLayoutBounds().getWidth()) / 2);
        valueText.setLayoutY((size - valueText.getLayoutBounds().getHeight()) / 2 + control.getLabelFontSize());
        valueText.setEffect(INNER_GLOW);

        unitText.setFont(Font.font("Verdana", FontWeight.BOLD, control.getUnitFontSize()));
        unitText.setFill(control.getUnitColor());
        unitText.setTextAlignment(TextAlignment.CENTER);
        unitText.setTextOrigin(VPos.BOTTOM);
        unitText.setText(control.getUnit());
        unitText.setLayoutX((size - unitText.getLayoutBounds().getWidth()) / 2);
        unitText.setLayoutY((size - valueText.getLayoutBounds().getHeight()) / 2 + 2 * control.getLabelFontSize() + size * 0.01);
        unitText.setEffect(INNER_GLOW);

        gauge.getChildren().addAll(IBOUNDS,
            BAR_BACKGROUND,
            bar,
            valueText,
            unitText);
        gauge.setCache(true);
        gauge.setCacheHint(CacheHint.QUALITY);
    }
}