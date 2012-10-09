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
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.RotateBuilder;
import javafx.scene.transform.Scale;
import javafx.util.Duration;
import jfxtras.labs.internal.scene.control.behavior.SmallRadialBehavior;
import jfxtras.labs.scene.control.gauge.Section;
import jfxtras.labs.scene.control.gauge.SmallRadial;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;


/**
 * Created with IntelliJ IDEA.
 * User: hansolo
 * Date: 09.10.12
 * Time: 09:30
 * To change this template use File | Settings | File Templates.
 */
public class SmallRadialSkin extends SkinBase<SmallRadial, SmallRadialBehavior> {
    private SmallRadial     control;
    private Group           smallRadial;
    private Path            pointer;
    private Text            valueText;
    private Canvas          background;
    private GraphicsContext ctx;
    private Point2D         center;
    private double          angleOffset;
    private NumberFormat    valueFormat;
    private Timeline        timeline;
    private DoubleProperty  gaugeValue;
    private double          size;
    private Rotate          rotate;
    private boolean         isDirty;
    private boolean         initialized;


    // ******************** Constructors **************************************
    public SmallRadialSkin(final SmallRadial CONTROL) {
        super(CONTROL, new SmallRadialBehavior(CONTROL));
        control          = CONTROL;
        initialized      = false;
        isDirty          = false;
        smallRadial      = new Group();
        pointer          = new Path();
        valueFormat      = new DecimalFormat("0.0", new DecimalFormatSymbols(Locale.US));
        valueText        = new Text(valueFormat.format(control.getValue()));
        background       = new Canvas();
        ctx              = background.getGraphicsContext2D();
        center           = new Point2D(100, 100);
        angleOffset      = -(control.getRadialRange().ANGLE_RANGE / 2);
        timeline         = new Timeline();
        gaugeValue       = new SimpleDoubleProperty(control.getValue());
        size             = 200;
        rotate           = RotateBuilder.create().pivotX(100).pivotY(100).angle(angleOffset - gaugeValue.get() * control.getAngleStep()).build();
        init();
    }

    private void init() {
        if (control.getPrefWidth() < 0 | control.getPrefHeight() < 0) {
            control.setPrefSize(121, 121);
        }

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
        registerChangeListener(control.labelColorProperty(), "FULL_REPAINT");
        registerChangeListener(control.frameColorProperty(), "FULL_REPAINT");
        registerChangeListener(control.backgroundColorProperty(), "FULL_REPAINT");
        registerChangeListener(control.tickMarkColorProperty(), "FULL_REPAINT");
        registerChangeListener(control.pointerColorProperty(), "FULL_REPAINT");
        registerChangeListener(control.centerKnobColorProperty(), "FULL_REPAINT");
        registerChangeListener(control.noOfDecimalsProperty(), "LABEL");
        registerChangeListener(control.valueProperty(), "VALUE");
        registerChangeListener(gaugeValue, "GAUGE_VALUE");

        addBindings();

        pointer.getTransforms().add(rotate);

        initialized = true;
        repaint();
    }

    private void addBindings() {
        // Don't show bar if value is smaller or equal minValue
        if (valueText.visibleProperty().isBound()) {
            valueText.visibleProperty().unbind();
        }
        valueText.visibleProperty().bind(control.valueLabelVisibleProperty());
    }


    // ******************** Methods *******************************************
    @Override protected void handleControlPropertyChanged(final String PROPERTY) {
        super.handleControlPropertyChanged(PROPERTY);
        if ("WIDTH".equals(PROPERTY)) {
            reCalcParameters();
            repaint();
        } else if ("HEIGHT".equals(PROPERTY)) {
            reCalcParameters();
            repaint();
        } else if ("FULL_REPAINT".equals(PROPERTY)) {
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
        } else if ("LABEL".equals(PROPERTY)) {
            updateNumberFormat();
            repaint();
        } else if ("GAUGE_VALUE".equals(PROPERTY)) {
            valueText.setText(valueFormat.format(gaugeValue.get()));
            valueText.setX((size - valueText.getLayoutBounds().getWidth()) / 2);
            valueText.setY(0.88 * size);
            rotate.setAngle(angleOffset + gaugeValue.get() * control.getAngleStep());
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
        drawGauge();
        getChildren().clear();
        getChildren().add(smallRadial);
        isDirty = false;
        super.layoutChildren();
    }

    @Override public final SmallRadial getSkinnable() {
        return control;
    }

    @Override public final void dispose() {
        control = null;
    }

    @Override protected double computePrefWidth(final double PREF_HEIGHT) {
        double prefHeight = 121;
        if (PREF_HEIGHT != -1) {
            prefHeight = Math.max(0, PREF_HEIGHT - getInsets().getLeft() - getInsets().getRight());
        }
        return super.computePrefWidth(prefHeight);
    }

    @Override protected double computePrefHeight(final double PREF_WIDTH) {
        double prefWidth = 121;
        if (PREF_WIDTH != -1) {
            prefWidth = Math.max(0, PREF_WIDTH - getInsets().getTop() - getInsets().getBottom());
        }
        return super.computePrefHeight(prefWidth);
    }

    @Override protected double computeMinWidth(final double MIN_HEIGHT) {
        return super.computeMinWidth(Math.max(121, MIN_HEIGHT - getInsets().getLeft() - getInsets().getRight()));
    }

    @Override protected double computeMinHeight(final double MIN_WIDTH) {
        return super.computeMinHeight(Math.max(121, MIN_WIDTH - getInsets().getTop() - getInsets().getBottom()));
    }

    @Override protected double computeMaxWidth(final double MAX_HEIGHT) {
        return super.computeMaxWidth(Math.max(121, MAX_HEIGHT - getInsets().getLeft() - getInsets().getRight()));
    }

    @Override protected double computeMaxHeight(final double MAX_WIDTH) {
        return super.computeMaxHeight(Math.max(121, MAX_WIDTH - getInsets().getTop() - getInsets().getBottom()));
    }

    private final String createCssColor(final Color COLOR) {
        final StringBuilder CSS_COLOR = new StringBuilder(19);
        CSS_COLOR.append("rgba(");
        CSS_COLOR.append((int) (COLOR.getRed() * 255));
        CSS_COLOR.append(", ");
        CSS_COLOR.append((int) (COLOR.getGreen() * 255));
        CSS_COLOR.append(", ");
        CSS_COLOR.append((int) (COLOR.getBlue() * 255));
        CSS_COLOR.append(", ");
        CSS_COLOR.append(COLOR.getOpacity());
        CSS_COLOR.append(");");
        return CSS_COLOR.toString();
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

    private void reCalcParameters() {
        size        = control.getPrefWidth() < control.getPrefHeight() ? control.getPrefWidth() : control.getPrefHeight();
        double c    = size / 2;
        angleOffset = -(control.getRadialRange().ANGLE_RANGE / 2);
        center      = new Point2D(c, c);
        rotate.setPivotX(c);
        rotate.setPivotY(c);
        valueText.setX((size - valueText.getLayoutBounds().getWidth()) / 2);
        valueText.setY(0.88 * size);
        background.setWidth(size);
        background.setHeight(size);
        drawGaugeBackground();
    }


    // ******************** Drawing related ***********************************
    private final void drawGauge() {
        valueText.setFont(Font.font("Verdana", FontWeight.NORMAL, FontPosture.REGULAR, 0.11570247933884298 * size));
        valueText.setX((size - valueText.getLayoutBounds().getWidth()) / 2);
        valueText.setY(0.88 * size);
        valueText.setTextOrigin(VPos.BOTTOM);
        valueText.getTransforms().add(new Rotate(0.0, 0.36363636363636365 * size, 0.8760330578512396 * size));
        valueText.getTransforms().add(new Scale(1.0, 1.0));
        //valueLabel.getStyleClass().add("embeddedradial-value4");
        valueText.setFill(control.getValueLabelColor());


        pointer.setFillRule(FillRule.EVEN_ODD);
        pointer.getElements().add(new MoveTo(0.50 * size, 0.12396694214876033 * size));
        pointer.getElements().add(new CubicCurveTo(0.50 * size, 0.12396694214876033 * size,
            0.48760330578512395 * size, 0.371900826446281 * size,
            0.48760330578512395 * size, 0.4380165289256198 * size));
        pointer.getElements().add(new CubicCurveTo(0.48760330578512395 * size, 0.4380165289256198 * size,
            0.48760330578512395 * size, 0.5619834710743802 * size,
            0.48760330578512395 * size, 0.5619834710743802 * size));
        pointer.getElements().add(new CubicCurveTo(0.48760330578512395 * size, 0.5619834710743802 * size,
            0.48760330578512395 * size, 0.6033057851239669 * size,
            0.5 * size, 0.6033057851239669 * size));
        pointer.getElements().add(new CubicCurveTo(0.512396694214876 * size, 0.6033057851239669 * size,
            0.512396694214876 * size, 0.5619834710743802 * size,
            0.512396694214876 * size, 0.5619834710743802 * size));
        pointer.getElements().add(new CubicCurveTo(0.512396694214876 * size, 0.5619834710743802 * size,
            0.512396694214876 * size, 0.4380165289256198 * size,
            0.512396694214876 * size, 0.4380165289256198 * size));
        pointer.getElements().add(new CubicCurveTo(0.512396694214876 * size, 0.371900826446281 * size,
            0.50 * size, 0.12396694214876033 * size,
            0.50 * size, 0.12396694214876033 * size));
        pointer.getElements().add(new ClosePath());
        //pointer.getStyleClass().add("embeddedradial-pointer");
        final Color POINTER_FILL = Color.color(control.getPointerColor().getRed(),
            control.getPointerColor().getGreen(),
            control.getPointerColor().getBlue(),
            0.8);
        pointer.setStroke(POINTER_FILL.darker());
        pointer.setFill(POINTER_FILL);

        final Circle KNOB = new Circle(0.5 * size, 0.5 * size, 0.06198347107438017 * size);
        //KNOB.getStyleClass().add("embeddedradial-knob");
        KNOB.setFill(control.getCenterKnobColor());
        KNOB.setStroke(null);

        smallRadial.getChildren().clear();
        smallRadial.getChildren().addAll(background, valueText);
        smallRadial.getChildren().addAll(pointer, KNOB);
    }

    private void drawTickmarks(final GraphicsContext CTX) {
        double sinValue;
        double cosValue;
        double offset = 180 + control.getRadialRange().ANGLE_RANGE / 2;

        for (double angle = 0, counter = control.getMinValue() ; Double.compare(counter, control.getMaxValue()) <= 0 ; angle -= control.getAngleStep(), counter++) {
            sinValue = Math.sin(Math.toRadians(angle + offset));
            cosValue = Math.cos(Math.toRadians(angle + offset));

            Point2D innerPoint = new Point2D(center.getX() + size * 0.36 * sinValue, center.getY() + size * 0.36 * cosValue);
            Point2D outerPoint = new Point2D(center.getX() + size * 0.41 * sinValue, center.getY() + size * 0.41 * cosValue);
            Point2D textPoint  = new Point2D(center.getX() + size * 0.32 * sinValue, center.getY() + size * 0.32 * cosValue);

            CTX.setStroke(control.getTickMarkColor());
            if (counter % 10 == 0) {
                CTX.strokeLine(innerPoint.getX(), innerPoint.getY(), outerPoint.getX(), outerPoint.getY());
            }

            if (Double.compare(control.getMinValue(), counter) == 0 || Double.compare(control.getMaxValue(), counter) == 0) {
                CTX.save();
                CTX.setFont(Font.font("Verdana", FontWeight.NORMAL, 0.04 * size));
                CTX.setTextAlign(TextAlignment.CENTER);
                CTX.setFill(control.getTickMarkColor());
                CTX.fillText(Integer.toString((int)counter), textPoint.getX(), textPoint.getY());
                CTX.restore();
            }
        }
    }

    private final void drawSections(final GraphicsContext CTX) {
        final double xy     = (size - 0.77 * size) / 2;
        final double wh     = size * 0.77;
        final double OFFSET = angleOffset - 90;
        for (final Section section : control.getSections()) {
            final double ANGLE_START   = section.getStart() * control.getAngleStep();
            final double ANGLE_EXTEND  = (section.getStop() - section.getStart()) * control.getAngleStep();
            CTX.save();
            CTX.setStroke(section.getColor());
            CTX.setLineWidth(size * 0.07);
            CTX.setLineCap(StrokeLineCap.BUTT);
            CTX.strokeArc(xy, xy, wh, wh, -(OFFSET + ANGLE_START), -ANGLE_EXTEND, ArcType.OPEN);
            CTX.restore();
        }
    }

    private final void drawGaugeBackground() {
        ctx.clearRect(0, 0, size, size);

        // FRAME
        ctx.save();
        ctx.scale(1.0, 1);
        ctx.beginPath();
        ctx.arc(0.5 * size, 0.5 * size, 0.5 * size, 0.5 * size, 0, 360);
        ctx.setFill(control.getFrameColor());
        ctx.fill();
        ctx.restore();

        // BACKGROUND
        ctx.save();
        ctx.scale(1.0, 1);
        ctx.beginPath();
        ctx.arc(0.5 * size, 0.5 * size, 0.43388429752066116 * size, 0.43388429752066116 * size, 0, 360);
        ctx.setFill(control.getBackgroundColor());
        ctx.fill();
        ctx.restore();

        // SECTIONS
        drawSections(ctx);

        // TICKMARKS
        drawTickmarks(ctx);

        background.setCache(true);
    }

}
