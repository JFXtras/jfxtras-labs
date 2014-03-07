/**
 * MatrixPanelBuilder.java
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

package jfxtras.labs.scene.control.gauge;

import java.util.HashMap;
import java.util.List;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;
import jfxtras.labs.scene.control.gauge.MatrixPanel.FrameDesign;


/**
 *
 * @author Jose Pereda Llamas &lt;jperedadnr&gt;
 * Created on : 23-jun-2012, 11:47:23
 * @param <B>
 */
public class MatrixPanelBuilder <B extends MatrixPanelBuilder<B>> {
    private final HashMap<String, Property> properties = new HashMap<>();

    public static final MatrixPanelBuilder create(){
        return new MatrixPanelBuilder();
    }


    // ******************** LED MATRIX ****************************************
    /**
     * Set the number of LEDs columns
     * @see #ledHeight(int)
     * @param LED_WIDTH Insert the columns of LEDs in the maxtrixPanel
     * @return 
     */
    public final MatrixPanelBuilder ledWidth(final int LED_WIDTH) {
        properties.put("ledWidth", new SimpleIntegerProperty(LED_WIDTH));
        return this;
    }

    /**
     * Set the number of LEDs rows
     * @see #ledWidth(int)
     * @param LED_HEIGHT Insert the rows of LEDs in the matrixPanel
     * @return 
     */
    public final MatrixPanelBuilder ledHeight(final int LED_HEIGHT) {
        properties.put("ledHeight", new SimpleIntegerProperty(LED_HEIGHT));
        return this;
    }

    /**
     * Set the List of Contents
     * @see ContentBuilder#create() 
     * @param CONTENTS Insert a List of Contents to display in the matrixPanel. Each content can
     * be defined with a ContentBuilder 
     * @return 
     */
    public final MatrixPanelBuilder contents(final List<Content> CONTENTS) {
        properties.put("contentsList", new SimpleObjectProperty<>(CONTENTS));
        return this;
    }

    /**
     * Set the Array of Contents
     * @see ContentBuilder#create() 
     * @param CONTENT_ARRAY Insert an Array of Contents to display in the matrixPanel. Each content can
     * be defined with a ContentBuilder 
     * @return 
     */
   
    public final MatrixPanelBuilder contents(final Content[] CONTENT_ARRAY) {
        properties.put("contentsArray", new SimpleObjectProperty<>(CONTENT_ARRAY));
        return this;
    }


    // ******************** GAUGE *********************************************
    /**
     * Set the frame design
     * @see #frameVisible(boolean)
     * @see #frameBaseColor(Color)
     * @see #frameCustomPath(String)
     * @param FRAME_DESIGN Select between BLACK_METAL, SHINY_METAL (set a {@link #frameBaseColor(Color) frameBaseColor}), CHROME, 
     * DARK_GLOSSY, GLOSSY_METAL, or CUSTOM_DESIGN (set a {@link #frameCustomPath(String) frameCustomPath}), in case its visible 
     * @return 
     */
    public final MatrixPanelBuilder frameDesign(final FrameDesign FRAME_DESIGN) {
        properties.put("frameDesign", new SimpleObjectProperty<>(FRAME_DESIGN));
        return this;
    }

    /**
     * Set the frame base color
     * @see #frameDesign(MatrixPanel.FrameDesign)
     * @param FRAME_BASE_COLOR frame base color for SHINY_METAL frame design
     * @return 
     */
    public final MatrixPanelBuilder frameBaseColor(final Color FRAME_BASE_COLOR) {
        properties.put("frameBaseColor", new SimpleObjectProperty<>(FRAME_BASE_COLOR));
        return this;
    }

    /**
     * Set the path to an image for a custom frame
     * @return 
     * @see #frameDesign(FrameDesign FrameDesign)
     * @param FRAME_CUSTOM_PATH path to the image for the frame for CUSTOM_DESIGN frame design
     * Options for a valid name of an image, with its extension: 
     * <ul><li>It should be already in the source (relative to matrixPanel package)</li>
     * <li>It should be in any of the project's jars, so /package/path/to/file must be provided</li>
     * <li>A valid URL</li>
     * <li>A full valid path should be added to the name in case it has be loaded from an external resource</li></ul>
     */
    public final MatrixPanelBuilder frameCustomPath(final String FRAME_CUSTOM_PATH) {
        properties.put("frameCustomPath", new SimpleObjectProperty<>(FRAME_CUSTOM_PATH));
        return this;
    }

    /**
     * Set if the frame is visible
     * @see #frameDesign(FrameDesign)
     * @param FRAME_VISIBLE true to show the frame (select a design) or false, to hide the frame
     * @return 
     */
    public final MatrixPanelBuilder frameVisible(final boolean FRAME_VISIBLE) {
        properties.put("frameVisible", new SimpleBooleanProperty(FRAME_VISIBLE));
        return this;
    }

    /**
     * Set the preferred width of the control
     * @param PREF_WIDTH related to scene dimensions, to keep an aspectRatio of the matrixPanel control equals
     * as the one given by the ratio ledHeight/ledWidth, only prefWidth or prefHeight will be taken into acount 
     * and the other value will be adjusted properly.
     * @return 
     */
    public final B prefWidth(final double PREF_WIDTH) {
        properties.put("prefWidth", new SimpleDoubleProperty(PREF_WIDTH));
        return (B)this;
    }

    /**
     * Set the preferred height of the control
     * @param PREF_HEIGHT related to scene dimensions, to keep an aspectRatio of the matrixPanel control equals
     * as the one given by the ratio ledHeight/ledWidth, only prefWidth or prefHeight will be taken into acount 
     * and the other value will be adjusted properly.
     * @return 
     */
    public final B prefHeight(final double PREF_HEIGHT) {
        properties.put("prefHeight", new SimpleDoubleProperty(PREF_HEIGHT));
        return (B)this;
    }

   public final MatrixPanel build(){
        final MatrixPanel CONTROL = new MatrixPanel();
        properties.keySet().stream().forEach((key)-> {
            switch (key) {
                case "ledWidth":
                    CONTROL.setLedWidth(((IntegerProperty) properties.get(key)).get());
                    break;
                case "ledHeight":
                    CONTROL.setLedHeight(((IntegerProperty) properties.get(key)).get());
                    break;
                case "contentsList":
                    CONTROL.setContents(((ObjectProperty<List<Content>>) properties.get(key)).get());
                    break;
                case "contentsArray":
                    CONTROL.setContents(((ObjectProperty<Content[]>) properties.get(key)).get());
                    break;
                case "prefWidth":
                    CONTROL.setPrefWidth(((DoubleProperty) properties.get(key)).get());
                    break;
                case "prefHeight":
                    CONTROL.setPrefHeight(((DoubleProperty) properties.get(key)).get());
                    break; 
                case "frameDesign":
                    CONTROL.setFrameDesign(((ObjectProperty<FrameDesign>) properties.get(key)).get());
                    break;
                case "frameBaseColor":
                    CONTROL.setFrameBaseColor(((ObjectProperty<Color>) properties.get(key)).get());
                    break;
                case "frameCustomPath":
                    CONTROL.setFrameCustomPath(((ObjectProperty<String>) properties.get(key)).get());
                    break;
                case "frameVisible":
                    CONTROL.setFrameVisible(((BooleanProperty) properties.get(key)).get());
                    break;
            }
        });
        return CONTROL;
    }
}
