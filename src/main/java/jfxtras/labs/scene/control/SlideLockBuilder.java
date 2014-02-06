/**
 * SlideLockBuilder.java
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

package jfxtras.labs.scene.control;

import javafx.beans.property.*;
import javafx.scene.control.ControlBuilder;
import javafx.scene.paint.Paint;
import javafx.util.Builder;

import java.util.HashMap;

/**
 * Represents the Slide lock control's builder (pattern) for convenient object creation.
 *
 * @see jfxtras.labs.scene.control.SlideLock
 *
 * @author cdea
 * @author hansolo
 */
public class SlideLockBuilder { //<B extends SlideLockBuilder<B>> extends ControlBuilder<B> implements Builder<SlideLock> {
//    private HashMap<String, Property> properties = new HashMap<String, Property>();
//
//
//    // ******************** Constructors **************************************
//    protected SlideLockBuilder() {
//    }
//
//
//    // ******************** Methods *******************************************
//    public final static SlideLockBuilder create() {
//        return new SlideLockBuilder();
//    }
//
//    public final SlideLockBuilder backgroundVisible(final boolean BACKGROUND_VISIBLE) {
//        properties.put("BACKGROUND_VISIBLE", new SimpleBooleanProperty(BACKGROUND_VISIBLE));
//        return this;
//    }
//
//    public final SlideLockBuilder locked(final boolean LOCKED) {
//        properties.put("LOCKED", new SimpleBooleanProperty(LOCKED));
//        return this;
//    }
//
//    public final SlideLockBuilder text(final String TEXT) {
//        properties.put("TEXT", new SimpleStringProperty(TEXT));
//        return this;
//    }
//
//    public final SlideLockBuilder textOpacity(final double TEXT_OPACITY) {
//        properties.put("TEXT_OPACITY", new SimpleDoubleProperty(TEXT_OPACITY));
//        return this;
//    }
//
//    public final SlideLockBuilder buttonArrowBackgroundColor(final Paint COLOR) {
//        properties.put("BUTTON_ARROW_BG_COLOR", new SimpleObjectProperty(COLOR));
//        return this;
//    }
//
//    public final SlideLockBuilder buttonColor(final Paint COLOR) {
//        properties.put("BUTTON_COLOR", new SimpleObjectProperty(COLOR));
//        return this;
//    }
//
//    public final SlideLockBuilder buttonGlareVisible(final boolean VISIBLE) {
//        properties.put("BUTTON_GLARE", new SimpleBooleanProperty(VISIBLE));
//        return this;
//    }
//
//    @Override public final B prefWidth(final double PREF_WIDTH) {
//        properties.put("PREF_WIDTH", new SimpleDoubleProperty(PREF_WIDTH));
//        return (B) this;
//    }
//
//    @Override public final B prefHeight(final double PREF_HEIGHT) {
//        properties.put("PREF_HEIGHT", new SimpleDoubleProperty(PREF_HEIGHT));
//        return (B) this;
//    }
//
//    @Override public final B layoutX(final double LAYOUT_X) {
//        properties.put("LAYOUT_X", new SimpleDoubleProperty(LAYOUT_X));
//        return (B) this;
//    }
//
//    @Override public final B layoutY(final double LAYOUT_Y) {
//        properties.put("LAYOUT_Y", new SimpleDoubleProperty(LAYOUT_Y));
//        return (B) this;
//    }
//
//    @Override public final SlideLock build() {
//        final SlideLock CONTROL = new SlideLock();
//        for (String key : properties.keySet()) {
//            if ("LOCKED".equals(key)) {
//                CONTROL.setLocked(((BooleanProperty) properties.get(key)).get());
//            } else if ("BACKGROUND_VISIBLE".equals(key)) {
//                CONTROL.setBackgroundVisible(((BooleanProperty) properties.get(key)).get());
//            } else if ("TEXT".equals(key)) {
//                CONTROL.setText(((StringProperty) properties.get(key)).get());
//            } else if ("TEXT_OPACITY".equals(key)) {
//                CONTROL.setTextOpacity(((DoubleProperty) properties.get(key)).get());
//            } else if ("PREF_WIDTH".equals(key)) {
//                CONTROL.setPrefWidth(((DoubleProperty) properties.get(key)).get());
//            } else if ("PREF_HEIGHT".equals(key)) {
//                CONTROL.setPrefHeight(((DoubleProperty) properties.get(key)).get());
//            } else if ("LAYOUT_X".equals(key)) {
//                CONTROL.setLayoutX(((DoubleProperty) properties.get(key)).get());
//            } else if ("LAYOUT_Y".equals(key)) {
//                CONTROL.setLayoutY(((DoubleProperty) properties.get(key)).get());
//            } else if ("BUTTON_ARROW_BG_COLOR".equals(key)) {
//                CONTROL.setButtonArrowBackgroundColor(((ObjectProperty<Paint>) properties.get(key)).get());
//            } else if ("BUTTON_COLOR".equals(key)) {
//                CONTROL.setButtonColor(((ObjectProperty<Paint>) properties.get(key)).get());
//            } else if ("BUTTON_GLARE".equals(key)) {
//                CONTROL.setButtonGlareVisible(((BooleanProperty) properties.get(key)).get());
//            }
//        }
//        return CONTROL;
//    }
}