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

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TimelineBuilder;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Control;
import javafx.util.Duration;

/**
 *
 * @author cdea
 */
public class SlideLock extends Control{
    private static final String DEFAULT_STYLE_CLASS = "slide-lock";
    public static final double START_XCOORD = 33;
    public static final double END_XCOORD = 375;
    public static final double BUTTON_YCOORD = 49;
    private BooleanProperty locked = new SimpleBooleanProperty(true);
    private StringProperty text = new SimpleStringProperty();
    
    private DoubleProperty startX = new SimpleDoubleProperty(START_XCOORD);
    private DoubleProperty endX = new SimpleDoubleProperty(START_XCOORD);
    private DoubleProperty textOpacity = new SimpleDoubleProperty(1);
    private Timeline snapButtonBackAnim = null;
    
    public SlideLock() {
        getStyleClass().setAll(DEFAULT_STYLE_CLASS);
        text.set("slide to unlock");
        init();
    }

    public SlideLock(String text) {
        getStyleClass().setAll(DEFAULT_STYLE_CLASS);
        this.text.set(text);
        init();
    }
    
    private void init(){
            // snap button back
        KeyValue buttonXValue = new KeyValue(endX, 33);
        KeyFrame buttonXKeyFrame = new KeyFrame(Duration.millis(150), buttonXValue);
        
        // make text more opaque
        KeyValue textOpacityValue = new KeyValue(textOpacity, 1);
        KeyFrame textOpacityKeyFrame = new KeyFrame(Duration.millis(150), textOpacityValue);
        snapButtonBackAnim = TimelineBuilder.create()
                .keyFrames(buttonXKeyFrame, textOpacityKeyFrame)
                .build();
    }
    
    @Override
    protected String getUserAgentStylesheet() {
        return getClass().getResource("/jfxtras/labs/scene/control/slidelock.css").toExternalForm();
    }

    public BooleanProperty lockedProperty() {
        return locked;
    }
   
    public StringProperty textProperty() {
        return text;
    }
    
    public DoubleProperty startXProperty() {
        return startX;
    }
    
    public DoubleProperty endXProperty() {
        return endX;
    } 
    
    public DoubleProperty textOpacityProperty() {
        return textOpacity;
    }
    
    protected Timeline getSnapButtonBackAnim() {
        return snapButtonBackAnim;
    }

}
