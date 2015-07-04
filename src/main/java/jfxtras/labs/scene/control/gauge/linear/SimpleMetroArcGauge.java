/**
 * SimpleMetroArcGauge.java
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
import jfxtras.labs.internal.scene.control.gauge.linear.skin.SimpleMetroArcGaugeSkin;
import jfxtras.scene.control.ListSpinner;

/**
 * = SimpleMetroArcGauge
 * 
 * This gauge is a simple flat possibly colorful (Microsoft Metro style) arc shaped gauge.
 * The needle ranges from about 7 o'clock (min) clockwise to 5 o'clock (max).
 * 
 * == CSS properties
 * The gauge supports the following CSS styleable properties:
 * 
 * - -fxx-animated: is the gauge animated, YES or NO.
 * - -fxx-value-format: a DecimalFormat pattern for rendering the label inside the needle
 * 
 * === Example 
 * [source,css]
 * --
 *     #myGauge {
 *        -fxx-animated: YES; 
 *        -fxx-value-format:' ##0.0W'; 
 *     }
 * --
 * 
 * == Segments
 * This gauge supports segments, which are colored parts of the arc over which the needle moves:
 * 
 * - Segment styling can be set using CSS classes like "segment0", "segment1", ... , the numeric suffix is the index of the segment in the segments list.
 * - Another option is to specify an segment ID, which can then be used to style the segment in CSS.
 * - The SimpleMetroArcGauge.css per default supports segment classes segment0 - segment9.
 * - A shortcut is available through -fxx-segment0-color, ..., which can be set in any styleclass (this is used in the colorschemes).
 * - The CSS also contains a number of colorschemes, like "colorscheme-green-to-red-10" (for 10 segments) which can be activated by assigning the colorscheme class to the gauge.
 * - If no segments are specified a single segment will automatically be drawn.
 * - If segments are specified, the user is fully responsible for convering the whole range.
 *
 * === Example
 * [source,java]
 * --
 *     final SimpleMetroArcGauge lSimpleMetroArcGauge = new SimpleMetroArcGauge();
 *     lSimpleMetroArcGauge.getStyleClass().add("colorscheme-green-to-red-10");
 *     for (int i = 0; i < 10; i++) {
 *         Segment lSegment = new PercentSegment(lSimpleMetroArcGauge, i * 10.0, (i+1) * 10.0);
 *         lSimpleMetroArcGauge.segments().add(lSegment);
 *     }
 * --
 * Note: the colorscheme CSS must be manually loaded in the scene! See below.
 * 
 * == Marker
 * This gauge supports markers, which are tiny notches on the arc to mark special values:
 * 
 * - Marker styling can be set using CSS classes like "marker0", "marker1", ... , the numeric suffix is the index of the marker in the markers list.
 * - Another option is to specify an marker ID, which can then be used to style the marker in CSS.
 * - The SimpleMetroArcGauge.css per default supports marker classes marker0 - marker9.
 * - A shortcut is available through -fxx-marker0-color, ..., which can be set in any styleclass (this is used in the colorschemes).
 * - Marker colors are also set in the colorschemes.`
 *
 * === Example
 * [source,java]
 * --
 *     final SimpleMetroArcGauge lSimpleMetroArcGauge = new SimpleMetroArcGauge();
 *     for (int i = 0; i <= 20; i++) {
 *         lSimpleMetroArcGauge.markers().add(new PercentMarker(lSimpleMetroArcGauge, i * 5.0));
 *     }
 * --
 * 
 * Markers can be custom shaped using a SVG shape:
 * [source,css]
 * --
 *     .marker1 {
 *         -fx-shape: 'M 0 0 L -3 -6 L 3 -6 Z'; 
 *     } 
 * --
 * 
 * == Indicators
 * This gauge has two indicators positions: 0 and 1, located at the bottom between the ends of the arc.
 * Indicators can be assigned to these position as follows:
 * [source,java]
 * --
 *     final SimpleMetroArcGauge lSimpleMetroArcGauge = new SimpleMetroArcGauge();
 *     lSimpleMetroArcGauge.indicators().add(new Indicator(0, "warning"));
 *     lSimpleMetroArcGauge.indicators().add(new Indicator(1, "error"));
 * --
 * 
 * This enables (but does not show!) the indicator at the corresponding locations, the example uses the two predefined indicators "error" and "warning".
 * Indicators can be made visible by assigning "visible" to the corresponding -fxx-INDICATORID-indicator-visibility variable in CSS, like so:
 * [source,css]
 * --
 *     -fxx-warning-indicator-visibility: visible; 
 *     -fxx-error-indicator-visibility: visible;
 * --
 *
 * It is possible to have indicators become visible based on the needle's value, by means of the segments.
 * Suppose the needle is over segment1, then a CSS class named "segment1-active" is added to the node.
 * Using this CSS class an indicator can be made visible, for example:
 * [source,css]
 * --
 *    .segment1-active {
 *        -fxx-warning-indicator-visibility: visible; 
 *     }
 *    .segment2-active {
 *        -fxx-error-indicator-visibility: visible; 
 *     }
 * --
 * Segments may overlap and can be transparent, there is a special "colorscheme-first-grey-rest-transparent-10" colorscheme.
 * Segments could be setup solely to show indicators, for example segment1 could run from 50% to 100% and segment2 from 75% to 100%.
 * If the needle is over segment2, both the warning and error indicator will be visible.
 *
 * === Example (using the CSS above)
 * [source,java]
 * --
 *     final SimpleMetroArcGauge lSimpleMetroArcGauge = new SimpleMetroArcGauge();
 *     lSimpleMetroArcGauge.getStyleClass().add("colorscheme-first-grey-rest-transparent-10");
 *     lSimpleMetroArcGauge.segments().add(new CompleteSegment(lSimpleMetroArcGauge));
 *     lSimpleMetroArcGauge.segments().add(new PercentSegment(lSimpleMetroArcGauge, 50.0, 100.0, "warningSegment"));
 *     lSimpleMetroArcGauge.segments().add(new PercentSegment(lSimpleMetroArcGauge, 75.0, 100.0, "errorSegment"));
 * --
 * 
 * Given that the segements have id's, you can also use active classes based on these id's: 
 * [source,css]
 * --
 *    .segment-warningSegment-active {
 *        -warning-indicator-visibility: visible; 
 *     }
 *    .segment-errorSegment-active {
 *        -error-indicator-visibility: visible; 
 *     }
 * --
 * 
 * This should not be confused with the CSS rules based on the id of the segments (in the example: #warningSegment and #errorSegment).
 * 
 * Custom indicators can be created in CSS by defining a 100x100 SVG shape in CSS, with 0,0 being in the center, and assign an id to it. Similar to the default error indicator:.
 * [source,css]
 * --
 *    .error-indicator {
 *        visibility: -fxx-error-indicator-visibility;
 *        -fx-background-color: -fxx-error-indicator-color;
 *        -fx-shape: 'M-50,0 a50,50 0 1,0 100,0 a50,50 0 1,0 -100,0'; 
 *        -fx-scale-shape: false; 
 *    }
 * --
 *  
 * == Segment colorscheme
 * The CSS defines a number of default colorschemes for the segments, some of which already were shown in the examples.
 * These can be loaded into a scene using:
 * [source,java]
 * --
 *     scene.getStylesheets().add(LinearGauge.segmentColorschemeCSSPath());
 * --
 *  
 * The numeric suffix denotes the number of segments the colorschema is for.
 * 
 * - colorscheme-blue-to-red-5
 * - colorscheme-red-to-blue-5
 * - colorscheme-green-to-darkgreen-6
 * - colorscheme-green-to-red-6 
 * - colorscheme-red-to-green-6 
 * - colorscheme-purple-to-red-6 
 * - colorscheme-blue-to-red-6 
 * - colorscheme-green-to-red-7 
 * - colorscheme-red-to-green-7 
 * - colorscheme-green-to-red-10 
 * - colorscheme-red-to-green-10 
 * - colorscheme-purple-to-cyan-10 
 * - colorscheme-first-grey-rest-transparent-10
 *  
 * == Disclaimer
 * This is a blatant but approved visual copy of Gerrit Grunwald's Enzo SimpleGauge (https://bitbucket.org/hansolo/enzo/src).
 * Gerrit describes the Enzo library in his blog like this: _all controls in that library are made for my personal demos and are not production ready._
 * This JFXtras control is supposed to be production ready. 
 */
public class SimpleMetroArcGauge extends LinearGauge<SimpleMetroArcGauge> {
	
	// ==================================================================================================================
	// Constructor
	
	public SimpleMetroArcGauge() {
		setPrefSize(200, 200);
	}
	
	// ==================================================================================================================
	// LinearGauge

	/**
	 * Return the path to the CSS file so things are setup right
	 */
	@Override public String getUserAgentStylesheet() {
		return ListSpinner.class.getResource("/jfxtras/labs/internal/scene/control/gauge/linear/" + SimpleMetroArcGauge.class.getSimpleName() + ".css").toExternalForm();
	}

	@Override public Skin<?> createDefaultSkin() {
		return new SimpleMetroArcGaugeSkin(this); 
	}
}
