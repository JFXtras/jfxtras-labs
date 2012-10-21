/**
 * Copyright (c) 2011, JFXtras
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the <organization> nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package jfxtras.labs.scene.control;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;

/**
 * This class represent a radial menu that can display its item (see
 * {@link RadialMenuItem} in a radial layout. General properties are available
 * on the radialMenu while item specific ones can be set on item construction.
 * 
 * @author MrLoNee (http://www.mrlonee.com)
 * 
 */
public class RadialMenu extends Group {

    protected List<RadialMenuItem> items = new ArrayList<RadialMenuItem>();

    protected DoubleProperty innerRadius;
    protected DoubleProperty radius;
    protected DoubleProperty offset;
    protected DoubleProperty initialAngle;
    protected Paint backgroundFill;
    protected Paint backgroundMouseOnFill;
    protected Paint strokeFill;
    protected BooleanProperty clockwise;
    protected BooleanProperty backgroundVisible;;
    protected BooleanProperty strokeVisible;
    protected Node graphic;

    public Paint getBackgroundFill() {
	return this.backgroundFill;
    }

    public void setBackgroundFill(final Paint backgroundFill) {
	this.backgroundFill = backgroundFill;
    }

    public Paint getBackgroundMouseOnFill() {
	return this.backgroundMouseOnFill;
    }

    public void setBackgroundMouseOnFill(final Paint backgroundMouseOnFill) {
	this.backgroundMouseOnFill = backgroundMouseOnFill;
    }

    public Paint getStrokeFill() {
	return this.strokeFill;
    }

    public void setStrokeFill(final Paint strokeFill) {
	this.strokeFill = strokeFill;
    }

    public Node getGraphic() {
	return this.graphic;
    }

    public void setGraphic(final Node graphic) {
	this.graphic = graphic;
    }

    public double getInitialAngle() {
	return this.initialAngle.get();
    }

    public DoubleProperty initialAngleProperty() {
	return this.initialAngle;
    }

    public double getInnerRadius() {
	return this.innerRadius.get();
    }

    public DoubleProperty innerRadiusProperty() {
	return this.innerRadius;
    }

    public double getRadius() {
	return this.radius.get();
    }

    public DoubleProperty radiusProperty() {
	return this.radius;
    }

    public double getOffset() {
	return this.offset.get();
    }

    public DoubleProperty offsetProperty() {
	return this.offset;
    }

    public boolean isClockwise() {
	return this.clockwise.get();
    }

    public BooleanProperty clockwiseProperty() {
	return this.clockwise;
    }

    public boolean isBackgroundVisible() {
	return this.backgroundVisible.get();
    }

    public BooleanProperty backgroundVisibleProperty() {
	return this.backgroundVisible;
    }

    public BooleanProperty strokeVisibleProperty() {
	return this.strokeVisible;
    }

    public boolean isStrokeVisible() {
	return this.strokeVisible.get();
    }

    public RadialMenu() {

    }

    public RadialMenu(final double initialAngle, final double innerRadius,
	    final double radius, final double offset, final Paint bgFill,
	    final Paint bgMouseOnFill, final Paint strokeFill,
	    final boolean clockwise) {

	this.initialAngle = new SimpleDoubleProperty(initialAngle);
	this.initialAngle.addListener(new ChangeListener<Number>() {

	    @Override
	    public void changed(
		    final ObservableValue<? extends Number> paramObservableValue,
		    final Number paramT1, final Number paramT2) {
		RadialMenu.this.setInitialAngle(paramObservableValue.getValue()
			.doubleValue());
	    }
	});
	this.innerRadius = new SimpleDoubleProperty(innerRadius);
	this.radius = new SimpleDoubleProperty(radius);
	this.offset = new SimpleDoubleProperty(offset);
	this.clockwise = new SimpleBooleanProperty(clockwise);
	this.backgroundFill = bgFill;
	this.backgroundMouseOnFill = bgMouseOnFill;
	this.strokeFill = strokeFill;

	this.strokeVisible = new SimpleBooleanProperty(true);
	this.backgroundVisible = new SimpleBooleanProperty(true);
    }

    public void setOnMenuItemMouseClicked(
	    final EventHandler<? super MouseEvent> paramEventHandler) {
	for (final RadialMenuItem item : this.items) {
	    item.setOnMouseClicked(paramEventHandler);
	}
    }

    public void setInitialAngle(final double angle) {
	this.initialAngle.set(angle);

	double angleOffset = this.initialAngle.get();
	for (final RadialMenuItem item : this.items) {
	    item.setStartAngle(angleOffset);
	    angleOffset = angleOffset + item.getMenuSize();
	}
    }

    public void setInnerRadius(final double radius) {
	this.innerRadius.set(radius);

    }

    public void setRadius(final double radius) {
	this.radius.set(radius);

    }

    public void setOffset(final double offset) {
	this.offset.set(offset);
    }

    public void setBackgroundVisible(final boolean visible) {
	this.backgroundVisible.set(visible);

    }

    public void setStrokeVisible(final boolean visible) {
	this.strokeVisible.set(visible);

    }

    public void setBackgroundColor(final Paint color) {
	this.backgroundFill = color;

	for (final RadialMenuItem item : this.items) {
	    item.setBackgroundColor(color);
	}
    }

    public void setBackgroundMouseOnColor(final Paint color) {
	this.backgroundMouseOnFill = color;

	for (final RadialMenuItem item : this.items) {
	    item.setBackgroundMouseOnColor(color);
	}
    }

    public void setStrokeColor(final Paint color) {
	this.strokeFill = color;

	for (final RadialMenuItem item : this.items) {
	    item.setStrokeColor(color);
	}
    }

    public void setClockwise(final boolean clockwise) {
	this.clockwise.set(clockwise);

	for (final RadialMenuItem item : this.items) {
	    item.setClockwise(clockwise);
	}
    }

    public void addMenuItem(final RadialMenuItem item) {
	item.setBackgroundColor(this.backgroundFill);
	item.setBackgroundMouseOnColor(this.backgroundMouseOnFill);
	item.innerRadiusProperty().bind(this.innerRadius);
	item.radiusProperty().bind(this.radius);
	item.offsetProperty().bind(this.offset);
	item.setStrokeColor(this.strokeFill);
	item.setClockwise(this.clockwise.get());
	this.items.add(item);
	this.getChildren().add(item);
	double angleOffset = this.initialAngle.get();
	for (final RadialMenuItem it : this.items) {
	    it.setStartAngle(angleOffset);
	    angleOffset = angleOffset + item.getMenuSize();
	}
    }

    public List<RadialMenuItem> getMenuItems() {
	return this.items;
    }

    public void removeMenuItem(final RadialMenuItem item) {
	this.items.remove(item);
	this.getChildren().remove(item);

	item.innerRadiusProperty().unbind();
	item.radiusProperty().unbind();
	item.offsetProperty().unbind();
    }

    public void removeMenuItem(final int itemIndex) {
	final RadialMenuItem item = this.items.remove(itemIndex);
	this.getChildren().remove(item);
    }

}
