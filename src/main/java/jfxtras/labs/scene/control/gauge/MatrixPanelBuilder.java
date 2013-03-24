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

package jfxtras.labs.scene.control.gauge;

import java.util.HashMap;
import java.util.List;
import javafx.beans.property.*;
import javafx.scene.control.ControlBuilder;
import javafx.scene.paint.Color;
import javafx.util.Builder;


/**
 *
 * @author Jose Pereda Llamas &lt;jperedadnr&gt;
 * Created on : 23-jun-2012, 11:47:23
 */
public class MatrixPanelBuilder { // <B extends MatrixPanelBuilder<B>> extends ControlBuilder<B> implements Builder<MatrixPanel> {
//    private HashMap<String, Property> properties = new HashMap<String, Property>();
//
//    public static final MatrixPanelBuilder create(){
//        return new MatrixPanelBuilder();
//    }
//
//
//    // ******************** LED MATRIX ****************************************
//    /**
//     * Set the number of LEDs columns
//     * @see #ledHeight(int)
//     * @param LED_WIDTH Insert the columns of LEDs in the maxtrixPanel
//     */
//    public final MatrixPanelBuilder ledWidth(final int LED_WIDTH) {
//        properties.put("ledWidth", new SimpleIntegerProperty(LED_WIDTH));
//        return this;
//    }
//
//    /**
//     * Set the number of LEDs rows
//     * @see #ledWidth(int)
//     * @param LED_HEIGHT Insert the rows of LEDs in the matrixPanel
//     */
//    public final MatrixPanelBuilder ledHeight(final int LED_HEIGHT) {
//        properties.put("ledHeight", new SimpleIntegerProperty(LED_HEIGHT));
//        return this;
//    }
//
//    /**
//     * Set the List of Contents
//     * @see ContentBuilder#create() 
//     * @param CONTENTS Insert a List of Contents to display in the matrixPanel. Each content can
//     * be defined with a ContentBuilder 
//     */
//    public final MatrixPanelBuilder contents(final List<Content> CONTENTS) {
//        properties.put("contentsList", new SimpleObjectProperty<List<Content>>(CONTENTS));
//        return this;
//    }
//
//    /**
//     * Set the Array of Contents
//     * @see ContentBuilder#create() 
//     * @param CONTENT_ARRAY Insert an Array of Contents to display in the matrixPanel. Each content can
//     * be defined with a ContentBuilder 
//     */
//    public final MatrixPanelBuilder contents(final Content[] CONTENT_ARRAY) {
//        properties.put("contentsArray", new SimpleObjectProperty<Content[]>(CONTENT_ARRAY));
//        return this;
//    }
//
//
//    // ******************** GAUGE *********************************************
//    /**
//     * Set the frame design
//     * @see Gauge#frameDesignProperty()  
//     * @see #frameVisible(boolean)
//     * @see #frameBaseColor(Color)
//     * @param FRAME_DESIGN Select between BLACK_METAL, SHINY_METAL (set a {@link #frameBaseColor(Color) frameBaseColor}), CHROME, 
//     * DARK_GLOSSY or GLOSSY_METAL, in case its visible 
//     */
//    public final MatrixPanelBuilder frameDesign(final Gauge.FrameDesign FRAME_DESIGN) {
//        properties.put("frameDesign", new SimpleObjectProperty<Gauge.FrameDesign>(FRAME_DESIGN));
//        return this;
//    }
//
//    /**
//     * Set the frame base color
//     * @see #frameDesign(Gauge.FrameDesign Gauge.FrameDesign.SHINY_METAL)
//     * @param FRAME_BASE_COLOR frame base color for SHINY_METAL frame design
//     */
//    public final MatrixPanelBuilder frameBaseColor(final Color FRAME_BASE_COLOR) {
//        properties.put("frameBaseColor", new SimpleObjectProperty<Color>(FRAME_BASE_COLOR));
//        return this;
//    }
//
//    /**
//     * Set if the frame is visible
//     * @see #frameDesign(Gauge.FrameDesign)
//     * @param FRAME_VISIBLE true to show the frame (select a design) or false, to hide the frame
//     */
//    public final MatrixPanelBuilder frameVisible(final boolean FRAME_VISIBLE) {
//        properties.put("frameVisible", new SimpleBooleanProperty(FRAME_VISIBLE));
//        return this;
//    }
//
//    /**
//     * Set the preferred width of the control
//     * @param PREF_WIDTH related to scene dimensions, to keep an aspectRatio of the matrixPanel control equals
//     * as the one given by the ratio ledHeight/ledWidth, only prefWidth or prefHeight will be taken into acount 
//     * and the other value will be adjusted properly.
//     */
//    @Override public final B prefWidth(final double PREF_WIDTH) {
//        properties.put("prefWidth", new SimpleDoubleProperty(PREF_WIDTH));
//        return (B)this;
//    }
//
//    /**
//     * Set the preferred height of the control
//     * @param PREF_HEIGHT related to scene dimensions, to keep an aspectRatio of the matrixPanel control equals
//     * as the one given by the ratio ledHeight/ledWidth, only prefWidth or prefHeight will be taken into acount 
//     * and the other value will be adjusted properly.
//     */
//    @Override public final B prefHeight(final double PREF_HEIGHT) {
//        properties.put("prefHeight", new SimpleDoubleProperty(PREF_HEIGHT));
//        return (B)this;
//    }
//
//    @Override
//    public final MatrixPanel build(){
//        final MatrixPanel CONTROL = new MatrixPanel();
//        for (String key : properties.keySet()) {
//            if ("ledWidth".equals(key)) {
//                CONTROL.setLedWidth(((IntegerProperty) properties.get(key)).get());
//            } else if ("ledHeight".equals(key)) {
//                CONTROL.setLedHeight(((IntegerProperty) properties.get(key)).get());
//            } else if ("contentsList".equals(key)) {
//                CONTROL.setContents(((ObjectProperty<List<Content>>) properties.get(key)).get());
//            } else if ("contentsArray".equals(key)) {
//                CONTROL.setContents(((ObjectProperty<Content[]>) properties.get(key)).get());
//            } else if ("prefWidth".equals(key)) {
//                CONTROL.setPrefWidth(((DoubleProperty) properties.get(key)).get());
//            } else if ("prefHeight".equals(key)) {
//                CONTROL.setPrefHeight(((DoubleProperty) properties.get(key)).get());
//            } else if ("frameDesign".equals(key)) {
//                CONTROL.setFrameDesign(((ObjectProperty<Gauge.FrameDesign>) properties.get(key)).get());
//            } else if ("frameBaseColor".equals(key)) {
//                CONTROL.setFrameBaseColor(((ObjectProperty<Color>) properties.get(key)).get());
//            } else if ("frameVisible".equals(key)) {
//                CONTROL.setFrameVisible(((BooleanProperty) properties.get(key)).get());
//            } 
//        }
//        return CONTROL;
//    }
}
