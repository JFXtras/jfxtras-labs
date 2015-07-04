/**
 * BasicArcGauge.java
 *
 * Copyright (c) 2011-2015, JFXtras
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

package jfxtras.labs.scene.control.gauge.linear;

import javafx.scene.control.Skin;
import jfxtras.labs.internal.scene.control.gauge.linear.skin.BasicArcGaugeSkin;
import jfxtras.labs.scene.control.gauge.linear.elements.AbsoluteLabel;
import jfxtras.scene.control.ListSpinner;

/**
 * = BasicArcGauge
 * 
 *  
 * == Disclaimer
 * This is a blatant but approved visual copy of Gerrit Grunwald's Enzo RadialSteel (https://bitbucket.org/hansolo/enzo/src).
 * Gerrit describes the Enzo library in his blog like this: _all controls in that library are made for my personal demos and are not production ready._
 * This JFXtras control is supposed to be production ready. 
 */
public class BasicArcGauge extends LinearGauge<BasicArcGauge> {
	
	// ==================================================================================================================
	// Constructor
	
	/**
	 * 
	 */
	public BasicArcGauge() {
		setPrefSize(200, 200);
		
		// create the default label
		for (double d = 0.0; d <= 100.0; d += 10.0) {
			labels().add(new AbsoluteLabel(d, Math.round(d) + "%"));
		}
	}

	// ==================================================================================================================
	// LinearGauge

	/**
	 * Return the path to the CSS file so things are setup right
	 */
	@Override public String getUserAgentStylesheet() {
		return ListSpinner.class.getResource("/jfxtras/labs/internal/scene/control/gauge/linear/" + BasicArcGauge.class.getSimpleName() + ".css").toExternalForm();
	}

	@Override public Skin<?> createDefaultSkin() {
		return new BasicArcGaugeSkin(this); 
	}
}
