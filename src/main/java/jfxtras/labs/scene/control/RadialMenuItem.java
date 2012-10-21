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

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

/**
 * RadialMenuItem represent an item of the {@link RadialMenu} control. It can be
 * used to display a menu item with its own properties. General properties are
 * inherited from its containing {@link RadialMenu}, but some item specific one
 * can be set on construction of the item.
 * 
 * @author MrLoNee (http://www.mrlonee.com)
 * 
 */
public class RadialMenuItem extends Group implements ChangeListener<Number> {

    protected double startAngle;

    protected double menuSize;

    protected DoubleProperty innerRadius = new SimpleDoubleProperty();

    protected DoubleProperty radius = new SimpleDoubleProperty();

    protected DoubleProperty offset = new SimpleDoubleProperty();

    protected Paint computedBackgroundFill;

    protected Paint backgroundMouseOnColor;

    protected Paint backgroundColor;

    protected boolean backgroundVisible = true;

    protected boolean strokeVisible = true;

    protected boolean clockWise;

    protected Paint strokeColor;

    protected MoveTo moveTo;

    protected ArcTo arcToInner;

    protected ArcTo arcTo;

    protected LineTo lineTo;

    protected LineTo lineTo2;

    protected double innerStartX;

    protected double innerStartY;

    protected double innerEndX;

    protected double innerEndY;

    protected boolean innerSweep;

    protected double startX;

    protected double startY;

    protected double endX;

    protected double endY;

    protected boolean sweep;

    protected double graphicX;

    protected double graphicY;

    protected double translateX;

    protected double translateY;

    protected boolean mouseOn = false;

    protected Path path;

    protected Node graphic;

    protected String text;

    // void setStartAngle(final double startAngle) {
    // this.startAngle = startAngle;
    // this.redraw();
    // }

    DoubleProperty innerRadiusProperty() {
	return this.innerRadius;
    }

    DoubleProperty radiusProperty() {
	return this.radius;
    }

    DoubleProperty offsetProperty() {
	return this.offset;
    }

    Paint getBackgroundMouseOnColor() {
	return this.backgroundMouseOnColor;
    }

    void setBackgroundMouseOnColor(final Paint color) {
	this.backgroundMouseOnColor = color;
	this.redraw();
    }

    Paint getBackgroundColor() {
	return this.backgroundColor;
    }

    void setBackgroundColor(final Paint backgroundColor) {
	this.backgroundColor = backgroundColor;
	this.redraw();
    }

    boolean isClockwise() {
	return this.clockWise;
    }

    void setClockwise(final boolean clockWise) {
	this.clockWise = clockWise;
	this.redraw();
    }

    Paint getStrokeColor() {
	return this.strokeColor;
    }

    void setStrokeColor(final Paint color) {
	this.strokeColor = color;
	this.redraw();
    }

    void setBackgroundVisible(final boolean visible) {
	this.backgroundVisible = visible;
	this.redraw();
    }

    boolean isBackgroundVisible() {
	return this.backgroundVisible;
    }

    void setStrokeVisible(final boolean visible) {
	this.strokeVisible = visible;
	this.redraw();
    }

    boolean isStrokeVisible() {
	return this.strokeVisible;
    }

    public Node getGraphic() {
	return this.graphic;
    }

    public void setStartAngle(final double angle) {
	this.startAngle = angle;
	this.redraw();
    }

    public void setGraphic(final Node graphic) {
	if (this.graphic != null) {
	    this.getChildren().remove(graphic);
	}
	this.graphic = graphic;
	if (this.graphic != null) {
	    this.getChildren().add(graphic);
	}
	this.redraw();
    }

    public void setText(final String text) {
	this.text = text;
	this.redraw();
    }

    public String getText() {
	return this.text;
    }

    public RadialMenuItem() {
	this.innerRadius.addListener(this);
	this.radius.addListener(this);
	this.offset.addListener(this);

	this.path = new Path();
	this.moveTo = new MoveTo();
	this.arcToInner = new ArcTo();
	this.arcTo = new ArcTo();
	this.lineTo = new LineTo();
	this.lineTo2 = new LineTo();

	this.path.getElements().add(this.moveTo);
	this.path.getElements().add(this.arcToInner);
	this.path.getElements().add(this.lineTo);
	this.path.getElements().add(this.arcTo);
	this.path.getElements().add(this.lineTo2);

	this.getChildren().add(this.path);

	this.setOnMouseEntered(new EventHandler<MouseEvent>() {

	    @Override
	    public void handle(final MouseEvent arg0) {
		RadialMenuItem.this.mouseOn = true;
		RadialMenuItem.this.redraw();
	    }
	});

	this.setOnMouseExited(new EventHandler<MouseEvent>() {

	    @Override
	    public void handle(final MouseEvent arg0) {
		RadialMenuItem.this.mouseOn = false;
		RadialMenuItem.this.redraw();
	    }
	});

    }

    public RadialMenuItem(final double menuSize, final Node graphic) {

	this();

	this.menuSize = menuSize;
	this.graphic = graphic;
	if (this.graphic != null)
	    this.getChildren().add(this.graphic);

	this.redraw();
    }

    public RadialMenuItem(final double menuSize, final Node graphic,
	    final EventHandler<ActionEvent> actionHandler) {

	this(menuSize, graphic);
	this.setOnMouseClicked(new EventHandler<MouseEvent>() {

	    @Override
	    public void handle(final MouseEvent paramT) {
		actionHandler.handle(new ActionEvent(paramT.getSource(), paramT
			.getTarget()));
	    }
	});
	this.redraw();
    }

    public RadialMenuItem(final double menuSize, final String text,
	    final Node graphic, final EventHandler<ActionEvent> actionHandler) {

	this(menuSize, graphic, actionHandler);

	this.text = text;
	this.redraw();
    }

    protected void redraw() {

	this.path
		.setFill(this.backgroundVisible ? (this.mouseOn
			&& this.backgroundMouseOnColor != null ? this.backgroundMouseOnColor
			: this.backgroundColor)
			: null);
	this.path.setStroke(this.strokeVisible ? this.strokeColor : null);

	this.path.setFillRule(FillRule.EVEN_ODD);

	this.computeCoordinates();

	this.update();

    }

    protected void update() {
	final double innerRadiusValue = this.innerRadius.get();
	final double radiusValue = this.radius.get();

	this.moveTo.setX(this.innerStartX);
	this.moveTo.setY(this.innerStartY);

	this.arcToInner.setX(this.innerEndX);
	this.arcToInner.setY(this.innerEndY);
	this.arcToInner.setSweepFlag(this.innerSweep);
	this.arcToInner.setRadiusX(innerRadiusValue);
	this.arcToInner.setRadiusY(innerRadiusValue);

	this.lineTo.setX(this.startX);
	this.lineTo.setY(this.startY);

	this.arcTo.setX(this.endX);
	this.arcTo.setY(this.endY);
	this.arcTo.setSweepFlag(this.sweep);

	this.arcTo.setRadiusX(radiusValue);
	this.arcTo.setRadiusY(radiusValue);

	this.lineTo2.setX(this.innerStartX);
	this.lineTo2.setY(this.innerStartY);

	if (this.graphic != null) {
	    this.graphic.setTranslateX(this.graphicX);
	    this.graphic.setTranslateY(this.graphicY);
	}

	this.translateXProperty().set(this.translateX);
	this.translateYProperty().set(this.translateY);
    }

    protected void computeCoordinates() {
	final double innerRadiusValue = this.innerRadius.get();
	final double startAngleValue = this.startAngle;

	final double graphicAngle = startAngleValue + (this.menuSize / 2.0);
	final double radiusValue = this.radius.get();

	final double graphicRadius = innerRadiusValue
		+ (radiusValue - innerRadiusValue) / 2.0;

	final double offsetValue = this.offset.get();

	if (!this.clockWise) {
	    this.innerStartX = innerRadiusValue
		    * Math.cos(Math.toRadians(startAngleValue));
	    this.innerStartY = -innerRadiusValue
		    * Math.sin(Math.toRadians(startAngleValue));
	    this.innerEndX = innerRadiusValue
		    * Math.cos(Math.toRadians(startAngleValue + this.menuSize));
	    this.innerEndY = -innerRadiusValue
		    * Math.sin(Math.toRadians(startAngleValue + this.menuSize));

	    this.innerSweep = false;

	    this.startX = radiusValue
		    * Math.cos(Math.toRadians(startAngleValue + this.menuSize));
	    this.startY = -radiusValue
		    * Math.sin(Math.toRadians(startAngleValue + this.menuSize));
	    this.endX = radiusValue * Math.cos(Math.toRadians(startAngleValue));
	    this.endY = -radiusValue
		    * Math.sin(Math.toRadians(startAngleValue));

	    this.sweep = true;

	    this.graphicX = graphicRadius
		    * Math.cos(Math.toRadians(graphicAngle))
		    - this.graphic.getBoundsInParent().getWidth() / 2.0;
	    this.graphicY = -graphicRadius
		    * Math.sin(Math.toRadians(graphicAngle))
		    - this.graphic.getBoundsInParent().getHeight() / 2.0;

	    this.translateX = offsetValue
		    * Math.cos(Math.toRadians(startAngleValue
			    + (this.menuSize / 2.0)));
	    this.translateY = -offsetValue
		    * Math.sin(Math.toRadians(startAngleValue
			    + (this.menuSize / 2.0)));

	} else if (this.clockWise) {
	    this.innerStartX = innerRadiusValue
		    * Math.cos(Math.toRadians(startAngleValue));
	    this.innerStartY = innerRadiusValue
		    * Math.sin(Math.toRadians(startAngleValue));
	    this.innerEndX = innerRadiusValue
		    * Math.cos(Math.toRadians(startAngleValue + this.menuSize));
	    this.innerEndY = innerRadiusValue
		    * Math.sin(Math.toRadians(startAngleValue + this.menuSize));

	    this.innerSweep = true;

	    this.startX = radiusValue
		    * Math.cos(Math.toRadians(startAngleValue + this.menuSize));
	    this.startY = radiusValue
		    * Math.sin(Math.toRadians(startAngleValue + this.menuSize));
	    this.endX = radiusValue * Math.cos(Math.toRadians(startAngleValue));
	    this.endY = radiusValue * Math.sin(Math.toRadians(startAngleValue));

	    this.sweep = false;

	    this.graphicX = graphicRadius
		    * Math.cos(Math.toRadians(graphicAngle))
		    - this.graphic.getBoundsInParent().getWidth() / 2.0;
	    this.graphicY = graphicRadius
		    * Math.sin(Math.toRadians(graphicAngle))
		    - this.graphic.getBoundsInParent().getHeight() / 2.0;

	    this.translateX = offsetValue
		    * Math.cos(Math.toRadians(startAngleValue
			    + (this.menuSize / 2.0)));
	    this.translateY = offsetValue
		    * Math.sin(Math.toRadians(startAngleValue
			    + (this.menuSize / 2.0)));

	}
    }

    public double getMenuSize() {
	return this.menuSize;
    }

    @Override
    public void changed(final ObservableValue<? extends Number> arg0,
	    final Number arg1, final Number arg2) {
	this.redraw();
    }
}
