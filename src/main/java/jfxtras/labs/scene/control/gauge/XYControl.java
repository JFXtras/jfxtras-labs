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
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Point2D;
import javafx.scene.control.Control;
import javafx.scene.paint.Color;


/**
 * Created by
 * User: hansolo
 * Date: 10.05.12
 * Time: 12:55
 */
public class XYControl extends Control {
    public enum Sensitivity {
        COARSE(0.1, Color.RED),
        MEDIUM(0.025, Color.rgb(255, 191, 0)),
        FINE(0.005, Color.LIME);

        public final double STEP_SIZE;
        public final Color COLOR;

        private Sensitivity(final double STEP_SIZE, final Color COLOR) {
            this.STEP_SIZE = STEP_SIZE;
            this.COLOR = COLOR;
        }
    }

    private static final String         DEFAULT_STYLE_CLASS = "xy-control";
    private DoubleProperty              xValue;
    private DoubleProperty              yValue;
    private StringProperty              xAxisLabel;
    private StringProperty              yAxisLabel;
    private BooleanProperty             xAxisLabelVisible;
    private BooleanProperty             yAxisLabelVisible;
    private ObjectProperty<Sensitivity> sensitivity;
    private ObjectProperty<Point2D>     position;


    // ******************** Constructors **************************************
    public XYControl() {
        xValue            = new SimpleDoubleProperty(0);
        yValue            = new SimpleDoubleProperty(0);
        xAxisLabel        = new SimpleStringProperty("x");
        yAxisLabel        = new SimpleStringProperty("y");
        xAxisLabelVisible = new SimpleBooleanProperty(false);
        yAxisLabelVisible = new SimpleBooleanProperty(false);
        sensitivity       = new SimpleObjectProperty<Sensitivity>(Sensitivity.COARSE);
        position          = new SimpleObjectProperty<Point2D>(new Point2D(xValue.get(),  yValue.get()));

        getStyleClass().add(DEFAULT_STYLE_CLASS);
    }


    // ******************** Methods *******************************************
    @Override public void setPrefSize(final double WIDTH, final double HEIGHT) {
        double SIZE = WIDTH < HEIGHT ? WIDTH : HEIGHT;
        super.setPrefSize(SIZE, SIZE);
    }

    public final double getXValue() {
        return xValue.get();
    }

    public final void setXValue(final double X_VALUE) {
        validateValue(X_VALUE, xValue);
    }

    public final DoubleProperty xValueProperty() {
        return xValue;
    }

    public final double getYValue() {
        return yValue.get();
    }

    public final void setYValue(final double Y_VALUE) {
        validateValue(Y_VALUE, yValue);
    }

    public final DoubleProperty yValueProperty() {
        return yValue;
    }

    public final String getXAxisLabel() {
        return xAxisLabel.get();
    }

    public final void setXAxisLabel(final String X_AXIS_LABEL) {
        xAxisLabel.set(X_AXIS_LABEL);
    }

    public final StringProperty xAxisLabelProperty() {
        return xAxisLabel;
    }

    public final String getYAxisLabel() {
        return yAxisLabel.get();
    }

    public final void setYAxisLabel(final String Y_AXIS_LABEL) {
        yAxisLabel.set(Y_AXIS_LABEL);
    }

    public final StringProperty yAxisLabelProperty() {
        return yAxisLabel;
    }

    public final boolean isXAxisLabelVisible() {
        return xAxisLabelVisible.get();
    }

    public final void setXAxisLabelVisible(final boolean X_AXIS_LABEL_VISIBLE) {
        xAxisLabelVisible.set(X_AXIS_LABEL_VISIBLE);
    }

    public final BooleanProperty xAxisLabelVisibleProperty() {
        return xAxisLabelVisible;
    }

    public final boolean isYAxisLabelVisible() {
        return yAxisLabelVisible.get();
    }

    public final void setYAxisLabelVisible(final boolean Y_AXIS_LABEL_VISIBLE) {
        yAxisLabelVisible.set(Y_AXIS_LABEL_VISIBLE);
    }

    public final BooleanProperty yAxisLabelVisibleProperty() {
        return yAxisLabelVisible;
    }

    public final Sensitivity getSensitivity() {
        return sensitivity.get();
    }

    public final void setSensitivity(final Sensitivity SENSITIVITY) {
        sensitivity.set(SENSITIVITY);
    }

    public final ObjectProperty<Sensitivity> sensitivityProperty() {
        return sensitivity;
    }

    public final Point2D getPosition() {
        return position.get();
    }

    public final void setPosition(final double X_VALUE, final double Y_VALUE) {
        setXValue(X_VALUE);
        setYValue(Y_VALUE);
    }

    public final void setPosition(final Point2D POSITION) {
        setXValue(POSITION.getX());
        setYValue(POSITION.getY());
        position.set(POSITION);
    }

    public final ObjectProperty<Point2D> positionProperty() {
        return position;
    }

    public final void incrementX() {
        setXValue(getXValue() + sensitivity.get().STEP_SIZE);
    }

    public final void decrementX() {
        setXValue(getXValue() - sensitivity.get().STEP_SIZE);
    }

    public final void incrementY() {
        setYValue(getYValue() + sensitivity.get().STEP_SIZE);
    }

    public final void decrementY() {
        setYValue(getYValue() - sensitivity.get().STEP_SIZE);
    }

    public final void reset() {
        setXValue(0);
        setYValue(0);
    }

    private void validateValue(final double VALUE, final DoubleProperty TARGET) {
        double value;
        if (VALUE < -1) {
            value = -1;
        } else if (VALUE > 1) {
            value = 1;
        } else {
            value = VALUE;
        }
        TARGET.set(value);
    }


    // ******************** Style related *************************************
    @Override protected String getUserAgentStylesheet() {
        return getClass().getResource("extras.css").toExternalForm();
    }
}

