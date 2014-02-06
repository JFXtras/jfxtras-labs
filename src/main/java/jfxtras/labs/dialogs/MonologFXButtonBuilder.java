/**
 * MonologFXButtonBuilder.java
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

package jfxtras.labs.dialogs;

import java.util.HashMap;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.control.ControlBuilder;
import javafx.util.Builder;

/**
 *
 * @author Mark Heckler (mark.heckler@gmail.com, @MkHeck)
 */
public class MonologFXButtonBuilder { //<B extends MonologFXButtonBuilder<B>> extends ControlBuilder<B> implements Builder<MonologFXButton> {
//    private HashMap<String, Property> properties = new HashMap<>();
//
//    protected MonologFXButtonBuilder() {
//    }
//
//    /**
//     * Creates and returns a MonologFXButton builder object upon which 
//     * to set properties and eventually, create a MonologFXButton for use with
//     * a MonologFX dialog.
//     */
//    public static MonologFXButtonBuilder create() {
//        return new MonologFXButtonBuilder();
//    }
//
//    /**
//     * Sets the type of this button.
//     * 
//     * @param TYPE MonologFXButton.Type designation.
//     * 
//     * @see MonologFXButton.Type
//     */
//    public final MonologFXButtonBuilder type(final MonologFXButton.Type TYPE) {
//        properties.put("type", new SimpleObjectProperty<>(TYPE));
//        return this;
//    }
//
//    /**
//     * Sets the label text for the button.
//     * 
//     * To assign a shortcut key, simply place an underscore character ("_")
//     * in front of the desired shortcut character.
//     * 
//     * @param LABEL String consisting of the desired button text.
//     */    
//    public final MonologFXButtonBuilder label(final String LABEL) {
//        properties.put("label", new SimpleStringProperty(LABEL));
//        return this;
//    }
//
//    /**
//     * Sets the graphic for use on the button, either alone or with text.
//     * Graphic format must be .png, .jpg (others?) supported by ImageView.
//     * 
//     * @param ICON String containing the location and name of a graphic file 
//     *      (.png, .jpg) for use as an icon on the button face.
//     *
//     * @see ImageView
//     */
//    public final MonologFXButtonBuilder icon(final String ICON) {
//        properties.put("icon", new SimpleStringProperty(ICON));
//        return this;
//    }
//
//    /**
//     * Designates this button as the "default" button - or not.
//     * 
//     * @param DEFAULTBUTTON Boolean.
//     */
//    public final MonologFXButtonBuilder defaultButton(final boolean DEFAULTBUTTON) {
//        properties.put("defaultButton", new SimpleBooleanProperty(DEFAULTBUTTON));
//        return this;
//    }
//
//    /**
//     * Designates this button as the "cancel" button - or not.
//     * 
//     * @param CANCELBUTTON Boolean.
//     */    
//    public final MonologFXButtonBuilder cancelButton(final boolean CANCELBUTTON) {
//        properties.put("cancelButton", new SimpleBooleanProperty(CANCELBUTTON));
//        return this;
//    }
//
//    /**
//     * This is where the button is created/assembled. Returns a MonologFXButton
//     * object, ready to add to a MonologFX dialog.
//     * 
//     * @return MonologFXButton
//     */
//    @Override
//    public MonologFXButton build() {
//        final MonologFXButton CONTROL = new MonologFXButton();
//
//        for (String key : properties.keySet()) {
//            switch (key) {
//                case "type":
//                    CONTROL.setType(((ObjectProperty<MonologFXButton.Type>) properties.get(key)).get());
//                    break;
//                case "label":
//                    CONTROL.setLabel(((StringProperty) properties.get(key)).get());
//                    break;
//                case "icon":
//                    CONTROL.setIcon(((StringProperty) properties.get(key)).get());
//                    break;
//                case "defaultButton":
//                    CONTROL.setDefaultButton(((BooleanProperty) properties.get(key)).get());
//                    break;
//                case "cancelButton":
//                    CONTROL.setCancelButton(((BooleanProperty) properties.get(key)).get());
//                    break;
//            }
//        }
//
//        return CONTROL;
//    }
}
