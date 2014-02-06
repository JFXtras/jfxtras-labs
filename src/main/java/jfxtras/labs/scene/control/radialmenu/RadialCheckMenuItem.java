/**
 * RadialCheckMenuItem.java
 *
 * Copyright (c) 2011-2014, JFXtras
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the organization nor the
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

package jfxtras.labs.scene.control.radialmenu;

import javafx.scene.Node;
import javafx.scene.paint.Paint;

public class RadialCheckMenuItem extends RadialMenuItem {

    protected boolean selected = false;

    protected Paint selectedColor;

    protected Paint selectedMouseOnColor;

    public RadialCheckMenuItem(final double menuSize, final Node graphic) {
	super(menuSize, graphic);
    }

    public RadialCheckMenuItem(final double menuSize, final Node graphic,
	    final boolean selected) {
	this(menuSize, graphic);
	this.selected = selected;
    }

    public RadialCheckMenuItem(final double menuSize, final Node graphic,
	    final boolean selected, final Paint selectedColor) {
	this(menuSize, graphic, selected);
	this.selectedColor = selectedColor;
    }

    public RadialCheckMenuItem(final double menuSize, final Node graphic,
	    final boolean selected, final Paint selectedColor,
	    final Paint selectedMouseOnColor) {
	this(menuSize, graphic, selected);
	this.selectedColor = selectedColor;
	this.selectedMouseOnColor = selectedMouseOnColor;
    }

    @Override
    protected void redraw() {
	super.redraw();

	Paint color = null;
	if (this.backgroundVisible.get()) {
	    if (this.selected && this.selectedColor != null) {
		if (this.mouseOn && this.selectedMouseOnColor != null) {
		    color = this.selectedMouseOnColor;
		} else {
		    color = this.selectedColor;
		}
	    } else {
		if (this.mouseOn && this.backgroundMouseOnColor != null) {
		    color = this.backgroundMouseOnColor.get();
		} else {
		    color = this.backgroundColor.get();
		}
	    }
	}

	this.path.setFill(color);
    }

    @Override
    void setSelected(final boolean selected) {
	this.selected = selected;
	this.redraw();
    }

    @Override
    boolean isSelected() {
	return this.selected;
    }

}
