/**
 * Copyright (c) 2011-2021, JFXtras
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *    Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *    Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *    Neither the name of the organization nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL JFXTRAS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jfxtras.labs.scene.control.radialmenu;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.Animation.Status;
import javafx.animation.FadeTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import javafx.util.Duration;

public class RadialContainerMenuItem extends RadialMenuItem {

    private boolean selected = false;

    private final Group childAnimGroup = new Group();

    private FadeTransition fadeIn = null;
    private FadeTransition fadeOut = null;

    protected List<RadialMenuItem> items = new ArrayList<RadialMenuItem>();
    protected Polyline arrow = new Polyline(-5.0, -5.0, 5.0, 0.0, -5.0, 5.0, -5.0, -5.0);

    public RadialContainerMenuItem(final double menuSize, final Node graphic) {
	super(menuSize, graphic);
	this.initialize();
    }

    public RadialContainerMenuItem(final double menuSize, final String text,
	    final Node graphic) {
	super(menuSize, text, graphic);
	this.initialize();
    }

    private void initialize() {
		arrow.setFill(Color.GRAY);
		arrow.setStroke(null);
	this.childAnimGroup.setVisible(false);
	this.visibleProperty().addListener(new ChangeListener<Boolean>() {

	    @Override
	    public void changed(final ObservableValue<? extends Boolean> arg0,
		    final Boolean arg1, final Boolean arg2) {
		if (!arg0.getValue()) {
		    RadialContainerMenuItem.this.childAnimGroup
			    .setVisible(false);
		    RadialContainerMenuItem.this.setSelected(false);
		}
	    }
	});
	this.getChildren().add(this.childAnimGroup);
	this.fadeIn = new FadeTransition(Duration.millis(400), this.childAnimGroup);
	fadeIn.setFromValue(0.0);
	fadeIn.setToValue(1.0);
	this.fadeOut = new FadeTransition(Duration.millis(400), this.childAnimGroup);
	fadeOut.setFromValue(0.0);
	fadeOut.setToValue(1.0);
	fadeOut.setOnFinished(new EventHandler<ActionEvent>() {

		    @Override
		    public void handle(final ActionEvent arg0) {
			RadialContainerMenuItem.this.childAnimGroup
				.setVisible(false);
		    }
		});
	this.getChildren().add(this.arrow);
    }

    public void addMenuItem(final RadialMenuItem item) {
	item.backgroundColorProperty().bind(this.backgroundColor);
	item.backgroundMouseOnColorProperty().bind(this.backgroundMouseOnColor);
	item.innerRadiusProperty().bind(this.radius);
	item.radiusProperty().bind(
		this.radius.multiply(2).subtract(this.innerRadius));
	item.offsetProperty().bind(this.offset.multiply(2.0));
	item.strokeColorProperty().bind(this.strokeColor);
	item.clockwiseProperty().bind(this.clockwise);
	item.backgroundVisibleProperty().bind(this.backgroundVisible);
	item.strokeVisibleProperty().bind(this.strokeVisible);
	this.items.add(item);
	this.childAnimGroup.getChildren().add(item);
	double offset = 0;
	for (final RadialMenuItem it : this.items) {
	    it.startAngleProperty().bind(this.startAngleProperty().add(offset));
	    offset += it.getMenuSize();
	}
    }

    public void removeMenuItem(final RadialMenuItem item) {
	this.items.remove(item);
	this.childAnimGroup.getChildren().remove(item);
	item.backgroundColorProperty().unbind();
	item.backgroundMouseOnColorProperty().unbind();
	item.innerRadiusProperty().unbind();
	item.radiusProperty().unbind();
	item.offsetProperty().unbind();
	item.strokeColorProperty().unbind();
	item.clockwiseProperty().unbind();
	item.backgroundVisibleProperty().unbind();
	item.strokeVisibleProperty().unbind();

    }

    public void removeMenuItem(final int itemIndex) {
	final RadialMenuItem item = this.items.get(itemIndex);
	this.removeMenuItem(item);
    }

    @Override
    protected void redraw() {
	super.redraw();
	if (this.selected) {
	    this.path
		    .setFill(this.backgroundVisible.get() ? (this.selected
			    && this.backgroundMouseOnColor.get() != null ? this.backgroundMouseOnColor
			    .get() : this.backgroundColor.get())
			    : null);
	}
	if (this.arrow != null) {
	    this.arrow.setFill(this.backgroundVisible.get() ? (this.mouseOn
		    && this.strokeColor.get() != null ? this.strokeColor.get()
		    : this.strokeColor.get()) : null);
	    this.arrow.setStroke(this.strokeVisible.get() ? this.strokeColor
		    .get() : null);
	    if (!this.clockwise.get()) {
		this.arrow
			.setRotate(-(this.startAngle.get() + this.menuSize / 2.0));
		this.arrow.setTranslateX((this.radius.get() - this.arrow
			.getBoundsInLocal().getWidth() / 2.0)
			* Math.cos(Math.toRadians(this.startAngle.get()
				+ this.menuSize / 2.0)) + this.translateX);
		this.arrow.setTranslateY(-(this.radius.get() - this.arrow
			.getBoundsInLocal().getHeight() / 2.0)
			* Math.sin(Math.toRadians(this.startAngle.get()
				+ this.menuSize / 2.0)) + this.translateY);
	    } else {
		this.arrow.setRotate(this.startAngle.get() + this.menuSize
			/ 2.0);
		this.arrow.setTranslateX((this.radius.get() - this.arrow
			.getBoundsInLocal().getWidth() / 2.0)
			* Math.cos(Math.toRadians(this.startAngle.get()
				+ this.menuSize / 2.0)) + this.translateX);
		this.arrow.setTranslateY((this.radius.get() - this.arrow
			.getBoundsInLocal().getHeight() / 2.0)
			* Math.sin(Math.toRadians(this.startAngle.get()
				+ this.menuSize / 2.0)) + this.translateY);

	    }

	}
    }

    @Override
    void setSelected(final boolean selected) {
	this.selected = selected;
	if (this.selected) {
	    double startOpacity = 0;
	    if (this.fadeOut.getStatus() == Status.RUNNING) {
		this.fadeOut.stop();
		startOpacity = this.childAnimGroup.getOpacity();
	    }
	    // draw Children
	    this.childAnimGroup.setOpacity(startOpacity);
	    this.childAnimGroup.setVisible(true);
	    this.fadeIn.fromValueProperty().set(startOpacity);
	    this.fadeIn.playFromStart();
	} else {
	    // draw Children
	    double startOpacity = 1.0;
	    if (this.fadeIn.getStatus() == Status.RUNNING) {
		this.fadeIn.stop();
		startOpacity = this.childAnimGroup.getOpacity();
	    }
	    this.fadeOut.fromValueProperty().set(startOpacity);
	    this.fadeOut.playFromStart();
	}
	this.redraw();
    }

    @Override
    boolean isSelected() {
	return this.selected;
    }

}
