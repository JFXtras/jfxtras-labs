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
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.CacheHint;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import jfxtras.labs.internal.scene.control.behavior.SimpleLinearGaugeBehavior;
import jfxtras.labs.scene.control.gauge.Section;
import jfxtras.labs.scene.control.gauge.SimpleLinearGauge;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;


/**
 * Created with IntelliJ IDEA.
 * User: hansolo
 * Date: 18.09.12
 * Time: 10:32
 * To change this template use File | Settings | File Templates.
 */
public class SimpleLinearGaugeSkin extends GaugeSkinBase<SimpleLinearGauge, SimpleLinearGaugeBehavior> {
    private SimpleLinearGauge control;
    private boolean           isDirty;
    private boolean           initialized;
    private Pane              gauge;
    private Point2D           center;
    private Line              bar;
    private Text              valueText;
    private NumberFormat      valueFormat;
    private Text              unitText;
    private Timeline          timeline;
    private DoubleProperty    gaugeValue;
    private double            width;
    private double            height;
    private Text              minLabel;
    private Text              maxLabel;
    private Orientation       orientation;
    private double            stepsize;


    // ******************** Constructors **************************************
    public SimpleLinearGaugeSkin(final SimpleLinearGauge CONTROL) {
        super(CONTROL, new SimpleLinearGaugeBehavior(CONTROL));
        control        = CONTROL;
        initialized    = false;
        isDirty        = false;
        gauge          = new Pane();
        center         = new Point2D(100, 50);
        bar            = new Line();
        valueText      = new Text();
        valueFormat    = new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.US));
        unitText       = new Text();
        timeline       = new Timeline();
        gaugeValue     = new SimpleDoubleProperty(control.getValue());
        width          = 200;
        height         = 60;
        minLabel       = new Text(valueFormat.format(control.getMinValue()));
        maxLabel       = new Text(valueFormat.format(control.getMaxValue()));
        orientation    = Orientation.HORIZONTAL;
        stepsize       = 1;
        init();
    }


    // ******************** Initialization ************************************
    private void init() {
        if (control.getPrefWidth() < 0 | control.getPrefHeight() < 0) {
            control.setPrefSize(200, 100);
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
        registerChangeListener(control.roundedBarProperty(), "BAR");
        registerChangeListener(control.labelFontSizeProperty(), "LABEL");
        registerChangeListener(control.noOfDecimalsProperty(), "LABEL");
        registerChangeListener(control.minLabelColorProperty(), "MIN_MAX_LABEL");
        registerChangeListener(control.maxLabelColorProperty(), "MIN_MAX_LABEL");
        registerChangeListener(control.minMaxLabelFontSizeProperty(), "MIN_MAX_LABEL");
        registerChangeListener(control.valueProperty(), "VALUE");
        registerChangeListener(control.minLabelVisibleProperty(), "MIN_LABEL_VISIBLE");
        registerChangeListener(gaugeValue, "GAUGE_VALUE");

        addBindings();

        updateNumberFormat();

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
    }


    // ******************** Methods *******************************************
    @Override protected void handleControlPropertyChanged(final String PROPERTY) {
        super.handleControlPropertyChanged(PROPERTY);
        if ("PREF_WIDTH".equals(PROPERTY)) {
            width  = control.getPrefWidth();
            height = control.getPrefHeight();
            orientation = width < height ? Orientation.VERTICAL : Orientation.HORIZONTAL;
            repaint();
        } else if ("PREF_HEIGHT".equals(PROPERTY)) {
            width  = control.getPrefWidth();
            height = control.getPrefHeight();
            orientation = width < height ? Orientation.VERTICAL : Orientation.HORIZONTAL;
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
                timeline = new Timeline();
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
        } else if ("MIN_MAX_LABEL".equals(PROPERTY)) {
            minLabel.setFill(control.getMinLabelColor());
            minLabel.setFont(Font.font("Verdana", control.getMinMaxLabelFontSize()));
            maxLabel.setFill(control.getMaxLabelColor());
            maxLabel.setFont(Font.font("Verdana", control.getMinMaxLabelFontSize()));
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
            switch (orientation) {
                case VERTICAL:
                    valueText.setLayoutX((width - valueText.getLayoutBounds().getWidth()) / 2);
                    bar.setEndY(height - control.getBarWidth() - (gaugeValue.get() * stepsize));
                    break;
                case HORIZONTAL:
                default:
                    valueText.setLayoutX((width - valueText.getLayoutBounds().getWidth()) / 2);
                    bar.setEndX(control.getBarWidth() + (gaugeValue.get() * stepsize));
                    break;
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
        getChildren().clear();
        drawGauge();
        getChildren().addAll(gauge);
        isDirty = false;

        super.layoutChildren();
    }

    @Override public final SimpleLinearGauge getSkinnable() {
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
        double prefHeight = 100;
        if (PREF_HEIGHT != -1) {
            prefHeight = Math.max(0, PREF_HEIGHT - getInsets().getTop() - getInsets().getBottom());
        }
        return super.computePrefHeight(prefHeight);
    }

    @Override protected double computeMinWidth(final double MIN_WIDTH) {
        return super.computeMinWidth(Math.max(50, MIN_WIDTH - getInsets().getLeft() - getInsets().getRight()));
    }

    @Override protected double computeMinHeight(final double MIN_HEIGHT) {
        return super.computeMinHeight(Math.max(50, MIN_HEIGHT - getInsets().getTop() - getInsets().getBottom()));
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
        switch (orientation) {
            case VERTICAL:
                bar.setStroke(new LinearGradient(bar.getStartX() - control.getBarWidth() / 2, 0,
                                                 bar.getStartX() + control.getBarWidth() / 2, 0,
                                                 false, CycleMethod.NO_CYCLE,
                                                 new Stop(0.0, BAR_COLOR),
                                                 new Stop(0.5, BAR_COLOR),
                                                 new Stop(1.0, BAR_COLOR.darker())));
                break;
            case HORIZONTAL:
            default:
                bar.setStroke(new LinearGradient(0, bar.getStartY() - control.getBarWidth() / 2,
                                                 0, bar.getStartY() + control.getBarWidth() / 2,
                                                 false, CycleMethod.NO_CYCLE,
                                                 new Stop(0.0, BAR_COLOR),
                                                 new Stop(0.5, BAR_COLOR),
                                                 new Stop(1.0, BAR_COLOR.darker())));
                break;
        }
    }


    // ******************** Drawing related ***********************************
    public final void drawGauge() {
        width       = control.getPrefWidth();
        height      = control.getPrefHeight();
        orientation = width < height ? Orientation.VERTICAL : Orientation.HORIZONTAL;
        double size = width < height ? height : width;
        center      = new Point2D(width / 2, height / 2);

        switch (orientation) {
            case VERTICAL:
                stepsize = Math.abs((height - (2 * control.getBarWidth()) - valueText.getLayoutBounds().getHeight() - 5 - control.getBarWidth() / 2) / control.getRange());
                break;
            case HORIZONTAL:
            default:
                stepsize = Math.abs((width - (2 * control.getBarWidth())) / control.getRange());
                break;
        }

        gauge.getChildren().clear();

        final Shape IBOUNDS = new Rectangle(0, 0, width, width);
        IBOUNDS.setOpacity(0.0);

        final InnerShadow INNER_SHADOW = new InnerShadow();
        INNER_SHADOW.setRadius(0.01 * size);
        INNER_SHADOW.setBlurType(BlurType.GAUSSIAN);
        INNER_SHADOW.setColor(Color.rgb(0, 0, 0, 0.65));

        final InnerShadow INNER_GLOW = new InnerShadow();
        INNER_GLOW.setRadius(0.005 * size);
        INNER_GLOW.setBlurType(BlurType.GAUSSIAN);
        INNER_GLOW.setOffsetY(-1);
        INNER_GLOW.setColor(Color.color(1, 1, 1, 0.65));

        final Line BAR_BACKGROUND = new Line();
        switch(orientation) {
            case VERTICAL:
                BAR_BACKGROUND.setStartX(center.getX());
                BAR_BACKGROUND.setStartY(height - control.getBarWidth());
                BAR_BACKGROUND.setEndX(center.getX());
                BAR_BACKGROUND.setEndY(control.getBarWidth() + valueText.getLayoutBounds().getHeight() + 5 + control.getBarWidth() / 2);
                break;
            case HORIZONTAL:
            default:
                BAR_BACKGROUND.setStartX(control.getBarWidth());
                BAR_BACKGROUND.setStartY(center.getY());
                BAR_BACKGROUND.setEndX(width - control.getBarWidth());
                BAR_BACKGROUND.setEndY(center.getY());
                break;
        }
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

        switch (orientation) {
            case VERTICAL:
                bar.setStartX(BAR_BACKGROUND.getStartX());
                bar.setStartY(BAR_BACKGROUND.getStartY());
                bar.setEndX(BAR_BACKGROUND.getEndX());
                bar.setEndY(BAR_BACKGROUND.getStartY() - (gaugeValue.get() * stepsize));
                break;
            case HORIZONTAL:
            default:
                bar.setStartX(BAR_BACKGROUND.getStartX());
                bar.setStartY(BAR_BACKGROUND.getStartY());
                bar.setEndX(control.getBarWidth() + (gaugeValue.get() * stepsize));
                bar.setEndY(BAR_BACKGROUND.getEndY());
                break;
        }
        bar.setFill(null);
        bar.setSmooth(true);
        updateBarColor(control.getBarColor());
        if (control.isRoundedBar()) {
            bar.setStrokeLineCap(StrokeLineCap.ROUND);
        } else {
            bar.setStrokeLineCap(StrokeLineCap.BUTT);
        }
        bar.setStrokeWidth(control.getBarWidth());
        bar.setEffect(INNER_SHADOW);

        valueText.setFont(Font.font("Verdana", FontWeight.BOLD, control.getLabelFontSize()));
        valueText.setFill(control.getLabelColor());
        valueText.setTextAlignment(TextAlignment.CENTER);
        valueText.setTextOrigin(VPos.BOTTOM);
        valueText.setText(valueFormat.format(gaugeValue.doubleValue()));
        valueText.setEffect(INNER_GLOW);

        unitText.setFont(Font.font("Verdana", FontWeight.BOLD, control.getUnitFontSize()));
        unitText.setFill(control.getUnitColor());
        unitText.setTextAlignment(TextAlignment.CENTER);
        unitText.setTextOrigin(VPos.BOTTOM);
        unitText.setText(control.getUnit());
        unitText.setEffect(INNER_GLOW);

        minLabel.setFont(Font.font("Verdana", FontWeight.NORMAL, control.getMinMaxLabelFontSize()));
        minLabel.setFill(control.getMinLabelColor());
        minLabel.setTextAlignment(TextAlignment.CENTER);
        minLabel.setTextOrigin(VPos.BOTTOM);
        minLabel.setText(valueFormat.format(control.getMinValue()));

        maxLabel.setFont(Font.font("Verdana", FontWeight.NORMAL, control.getMinMaxLabelFontSize()));
        maxLabel.setFill(control.getMaxLabelColor());
        maxLabel.setTextAlignment(TextAlignment.CENTER);
        maxLabel.setTextOrigin(VPos.BOTTOM);
        maxLabel.setText(valueFormat.format(control.getMaxValue()));

        switch (orientation) {
            case VERTICAL:
                valueText.setLayoutX((width - valueText.getLayoutBounds().getWidth()) / 2);
                valueText.setLayoutY(valueText.getLayoutBounds().getHeight());
                unitText.setLayoutX(center.getX() - control.getBarWidth() / 2 - unitText.getLayoutBounds().getHeight() - 5);
                unitText.setLayoutY(BAR_BACKGROUND.getEndY() + (BAR_BACKGROUND.getStartY() - BAR_BACKGROUND.getEndY()) / 2 + unitText.getLayoutBounds().getHeight() / 2);
                minLabel.setLayoutX(center.getX() - control.getBarWidth() / 2 - minLabel.getLayoutBounds().getWidth() - 2);
                minLabel.setLayoutY(BAR_BACKGROUND.getStartY());
                maxLabel.setLayoutX(center.getX() - control.getBarWidth() / 2 - maxLabel.getLayoutBounds().getWidth() - 2);
                maxLabel.setLayoutY(BAR_BACKGROUND.getEndY() + maxLabel.getLayoutBounds().getHeight());
                break;
            case HORIZONTAL:
            default:
                valueText.setLayoutX((width - valueText.getLayoutBounds().getWidth()) / 2);
                valueText.setLayoutY(center.getY() - control.getBarWidth() / 2 - 2);
                unitText.setLayoutX((width - unitText.getLayoutBounds().getWidth()) / 2);
                unitText.setLayoutY(center.getY() + control.getBarWidth() / 2 + unitText.getLayoutBounds().getHeight() + 2);
                minLabel.setLayoutX(BAR_BACKGROUND.getStartX());
                minLabel.setLayoutY(center.getY() + control.getBarWidth() / 2 + minLabel.getLayoutBounds().getHeight() + 2);
                maxLabel.setLayoutX(BAR_BACKGROUND.getEndX() - maxLabel.getLayoutBounds().getWidth());
                maxLabel.setLayoutY(center.getY() + control.getBarWidth() / 2 + maxLabel.getLayoutBounds().getHeight() + 2);
                break;
        }

        gauge.getChildren().addAll(IBOUNDS,
                                   BAR_BACKGROUND,
                                   bar,
                                   valueText,
                                   unitText,
                                   minLabel,
                                   maxLabel);
        gauge.setCache(true);
        gauge.setCacheHint(CacheHint.QUALITY);
    }
}