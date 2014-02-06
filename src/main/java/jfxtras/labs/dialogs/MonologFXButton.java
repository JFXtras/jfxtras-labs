/**
 * MonologFXButton.java
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author Mark Heckler (mark.heckler@gmail.com, @MkHeck)
 */
public class MonologFXButton {

    /**
     * Type of button, with several built-in options and three custom ones. 
     */
    public enum Type { OK, CANCEL, ABORT, RETRY, IGNORE, YES, NO, CUSTOM1, CUSTOM2, CUSTOM3 };
    private List<String> defLabels = Arrays.asList( "_OK", "_Cancel", "_Abort", "_Retry", "_Ignore", "_Yes", "_No", "Custom_1", "Custom_2", "Custom_3" );
    
    private HashMap<Type, String> defaultLabels = new HashMap<>();
    private Type type = MonologFXButton.Type.OK;    // Defaults to OK(-type) button
    private String label = "";
    private Node icon;
    private boolean defaultButton = false;
    private boolean cancelButton = false;
    
    /**
     * Default constructor for a MonologFX button. Plain button, 
     * no label or icon and no default or cancel designation(s).
     */
    public MonologFXButton() {
        // Refactor.
        int i = 0;
        for (Type t: Type.values()) {
            defaultLabels.put(t, defLabels.get(i));
            i++;
        }
    }
    
    /**
     * Returns the type of this button.
     * 
     * @return type MonologFXButton.Type designation.
     * 
     * @see Type
     */
    public Type getType() {
        return type;
    }

    /**
     * Sets the type of this button.
     * 
     * @param type MonologFXButton.Type designation.
     * 
     * @see Type
     */
    public void setType(Type type) {
        this.type = type;
    }

    /**
     * Returns the appropriate button label according to the following rules:
     * 
     * If the developer specifies a label, it returns that text.
     * 
     * If not, the button checks for a resource with i18n (internationalization)
     * text to use for this type of button. If it finds the file and the key
     * corresponding to this button type, it returns the i18n value.
     * 
     * If none of the above conditions are met, it returns default text.
     * 
     * @return label String consisting of the button's text.
     */
    public String getLabel() {
        if ( !label.isEmpty() ) {
            return label;
        } else {
            String labelToReturn = defaultLabels.get(getType());
            
            try {
                ResourceBundle res = ResourceBundle.getBundle("jfxtras/labs/dialogs/MonologFXButton", Locale.getDefault());
                if ( res != null ) {
                    labelToReturn = res.getString(labelToReturn.replaceAll("_", "").toUpperCase());
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }   

            return labelToReturn;
        }         
    }

    /**
     * Sets the label text for the button.
     * 
     * To assign a shortcut key, simply place an underscore character ("_")
     * in front of the desired shortcut character.
     * 
     * @param label String consisting of the desired button text.
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Returns the graphic file (if one is assigned) for this button.
     * 
     * @return icon Node consisting of the button's graphic element.
     */
    public Node getIcon() {
        return icon;
    }

    /**
     * Sets the graphic for use on the button, either alone or with text.
     * Graphic format must be .png, .jpg (others?) supported by ImageView.
     * 
     * @param iconFile String containing the location and name of a graphic file 
     *      (.png, .jpg) for use as an icon on the button face.
     *
     * @see ImageView
     */
    public void setIcon(String iconFile) {
        try {
            this.icon = new ImageView(new Image(getClass().getResourceAsStream(iconFile)));
        } catch (Exception e) {
            System.err.println("Exception trying to load button icon:" + e.getMessage());
        }
    }

    /**
     * Indicates if this button is designated as the "default" button.
     * 
     * @return defaultButton Boolean.
     */
    public boolean isDefaultButton() {
        return defaultButton;
    }

    /**
     * Designates this button as the "default" button - or not.
     * 
     * @param defaultButton Boolean.
     */
    public void setDefaultButton(boolean defaultButton) {
        this.defaultButton = defaultButton;
    }

    /**
     * Indicates if this button is designated as the "cancel" button.
     * 
     * @return cancelButton Boolean.
     */
    public boolean isCancelButton() {
        return cancelButton;
    }

    /**
     * Designates this button as the "cancel" button - or not.
     * 
     * @param cancelButton Boolean.
     */
    public void setCancelButton(boolean cancelButton) {
        this.cancelButton = cancelButton;
    }
}
