/**
 * Copyright (c) 2012, JFXtras
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
package jfxtras.labs.dialogs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.ControlBuilder;
import javafx.util.Builder;

/**
 *
 * @author Mark Heckler (mark.heckler@gmail.com, @HecklerMark)
 */
public class MonologFXBuilder<B extends MonologFXBuilder<B>> extends ControlBuilder<B> implements Builder<MonologFX> {
    private HashMap<String, Property> properties = new HashMap<>();
    private List<MonologFXButton> buttons = new ArrayList<>();
    private List<String> stylesheets = new ArrayList<>();
 
    protected MonologFXBuilder() {
    }

    /**
     * Creates and returns a MonologFX dialog box builder object upon which 
     * to set properties and eventually, create a MonologFX dialog box.
     */
    public static MonologFXBuilder create() {
        return new MonologFXBuilder();
    }

    /**
     * Public method used to add a button to a MonologFX dialog.
     * 
     * @param BUTTON A MonologFXButton object.
     * 
     * @see MonologFXButton
     */
    public final MonologFXBuilder button(final MonologFXButton BUTTON) {
        //properties.put("button", new SimpleObjectProperty<>(BUTTON));
        buttons.add(BUTTON);
        return this;
    }

    /**
     * Sets the type of MonologFX dialog box to build/display.
     * 
     * @param TYPE One of the supported types of dialogs.
     * @see MonologFX.Type
     */
    public final MonologFXBuilder type(final MonologFX.Type TYPE) {
        properties.put("type", new SimpleObjectProperty<>(TYPE));
        return this;
    }

    /**
     * Sets the button alignment for the MonologFX dialog box. Default is CENTER.
     * 
     * @param ALIGNBUTTONS Valid values are LEFT, RIGHT, and CENTER.
     * 
     * @see ButtonAlignment
     */
    public final MonologFXBuilder buttonAlignment(final MonologFX.ButtonAlignment ALIGNBUTTONS) {
        properties.put("alignbuttons", new SimpleObjectProperty<>(ALIGNBUTTONS));
        return this;
    }

    /**
     * Sets the text displayed within the MonologFX dialog box. Word wrap 
     * ensures that all text is displayed.
     * 
     * @param MESSAGE String variable containing the text to display.
     */
    public final MonologFXBuilder message(final String MESSAGE) {
        properties.put("message", new SimpleStringProperty(MESSAGE));
        return this;
    }

    /**
     * Sets the modality of the MonologFX dialog box to build/display.
     * 
     * @param MODAL Boolean. A true value = APPLICATION_MODAL, false = NONE.
     */
    public final MonologFXBuilder modal(final boolean MODAL) {
        properties.put("modal", new SimpleBooleanProperty(MODAL));
        return this;
    }

    /**
     * Sets the text to be displayed in the title bar of the MonologFX dialog.
     * 
     * @param TITLE_TEXT String containing the text to place in the title bar.
     */
    public final MonologFXBuilder titleText(final String TITLE_TEXT) {
        properties.put("titleText", new SimpleStringProperty(TITLE_TEXT));
        return this;
    }

    /**
     * Allows developer to add stylesheet(s) for MonologFX dialog, supplementing 
     * or overriding existing styling.
     * 
     * @param STYLESHEET String variable containing the path/name of the 
     * stylesheet to apply to the dialog's scene and contained controls.
     */
    public final MonologFXBuilder stylesheet(final String STYLESHEET) {
        //properties.put("stylesheet", new SimpleStringProperty(STYLESHEET));
        stylesheets.add(STYLESHEET);
        return this;
    }

    /**
     * This is where the magic happens...or at least where it all comes 
     * together.  :-) Returns a MonologFX dialog, ready to display with
     * showDialog().
     * 
     * @return MonologFX A dialog.
     */
    @Override
    public MonologFX build() {
        final MonologFX CONTROL = new MonologFX();

        for (String key : properties.keySet()) {
            switch (key) {
                case "type":
                    CONTROL.setType(((ObjectProperty<MonologFX.Type>) properties.get(key)).get());
                    break;
                case "alignbuttons":
                    CONTROL.setButtonAlignment(((ObjectProperty<MonologFX.ButtonAlignment>) properties.get(key)).get());
                    break;
                case "message":
                    CONTROL.setMessage(((StringProperty) properties.get(key)).get());
                    break;
                case "modal":
                    CONTROL.setModal(((BooleanProperty) properties.get(key)).get());
                    break;
                case "titleText":
                    CONTROL.setTitleText(((StringProperty) properties.get(key)).get());
                    break;
            }
        }

        for ( MonologFXButton mb : buttons ) {
            CONTROL.addButton(mb);
        }
        
        for ( String ss : stylesheets ) {
            CONTROL.addStylesheet(ss);
        }
        
        return CONTROL;
    }
}
