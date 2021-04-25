/**
 * Copyright (c) 2011-2021, JFXtras
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *    Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *    Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *    Neither the name of the organization nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL JFXTRAS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jfxtras.labs.scene.control;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import jfxtras.labs.internal.scene.control.skin.BigDecimalFieldSkin;

/**
 * Input field for BigDecimal values. This control has the following features:
 * <ul>
 * <li>BigDecimal {@link #number} is parsed and formatted according to the provided
 * NumberFormat</li>
 * <li>getter/setter for BigDecimal and (formatted) String representation of the number value</li>
 * <li>up/down arrow keys and buttons increment/decrement the
 * {@link #number} by {@link #stepwidth}</li>
 * <li>If {@link #minValueProperty()} and/or
 * {@link #maxValue} are set, values outside these boundaries are not accepted
 * for {@link #number}</li>
 * </ul>
 * CSS structure is:
 *  <ul>
 *      <li>.big-decimal-field
 *        <ul>
 *          <li>.text-field</li>
 *          <li>.arrow-button.arrow-button-up
 *            <ul>
 *              <li>.spinner-arrow.spinner-arrow-up</li>
 *            </ul></li>
 *          <li>.arrow-button.arrow-button-down
 *            <ul>
 *              <li>.spinner-arrow.spinner-arrow-down</li>
 *            </ul></li>
 *        </ul></li>
 *  </ul>
 *
 * @author Thomas Bolz
 */
public class BigDecimalField extends Control {

    /**
     * Default constructor. Returns a {@link BigDecimalField} with no number,
     * minValue and maxValue set, but stepwidth 1 and default
     * {@link NumberFormat}.
     */
    public BigDecimalField() {
        super();
        setStyle(null);
        getStyleClass().add("big-decimal-field");
        number = new SimpleObjectProperty<BigDecimal>(this, "number");
        stepwidth = new SimpleObjectProperty<BigDecimal>(this, "stepwidth", BigDecimal.ONE);
        maxValue = new SimpleObjectProperty<BigDecimal>(this, "maxValue");
        minValue = new SimpleObjectProperty<BigDecimal>(this, "minValue");
        format = new SimpleObjectProperty<NumberFormat>(this, "format", NumberFormat.getNumberInstance());
        promptText = new SimpleStringProperty(this, "promptText", "");

        initFocusSimulation();
    }

    /**
     * Initializes a construct, that mimics the focusedProperty() of e.g. a TextField. The focus is forwarded from the
     * inner TextField of the underlying Skin-Implementation.
     */
    private void initFocusSimulation() {
        setFocusTraversable(false);
        skinProperty().addListener((observable) -> {
            Skin<?> skin = getSkin();
            if (skin instanceof BigDecimalFieldSkin) {
                BigDecimalFieldSkin bigDecimalFieldSkin = (BigDecimalFieldSkin) skin;
                bigDecimalFieldSkin.focusForward.addListener((observable2) -> {
                    super.setFocused(bigDecimalFieldSkin.focusForward.get());
                });
            }
        });
    }

    /**
     * Returns a {@link BigDecimalField} with stepwidth 1 and {@link #number} set to initialValue.
     *
     * @param initialValue The initial BigDecimal value of this control.
     */
    public BigDecimalField(BigDecimal initialValue) {
        this();
        setNumber(initialValue);
    }

    /**
     * @param initialValue The initial BigDecimal value of this control.
     * @param stepwidth The stepwidth for increment/decrement operations.
     * @param format The NumberFormat that is used to format the number in the control.
     */
    public BigDecimalField(BigDecimal initialValue, BigDecimal stepwidth,
                           NumberFormat format) {
        this();
        this.number.set(initialValue);
        this.stepwidth.set(stepwidth);
        this.format.set(format);
    }

    /**
     * @return The formatted String representation of {@link #number}
     */
    public String getText() {
        if (number.getValue() != null)
            return getFormat().format(number.getValue());
        else
            return null;
    }

    /**
     * @param formattedNumber representation of {@link #number}
     */
    public void setText(String formattedNumber) {
        try {
            Number parsedNumber = getFormat().parse(formattedNumber);
            setNumber(new BigDecimal(parsedNumber.toString()));
        } catch (ParseException ex) {
            Logger.getLogger(BigDecimalField.class.getName()).log(Level.INFO,
                    null, ex);
        }
    }

    /**
     * increments the number by {@link #stepwidth}
     */
    public void increment() {
        if (getNumber() != null && getStepwidth() != null) {
            BigDecimal newValue = getNumber().add(getStepwidth());
            if (checkBounds(newValue) == false) {
                return;
            }
            setNumber(newValue);
        }
    }

    /**
     * decrements the number by {@link #stepwidth}
     */
    public void decrement() {
        if (getNumber() != null && getStepwidth() != null) {
            BigDecimal newValue = getNumber().subtract(getStepwidth());
            if (checkBounds(newValue) == false) {
                return;
            }
            setNumber(newValue);
        }
    }

    /**
     * Contains the number that is controlled in this {@link jfxtras.labs.scene.control.BigDecimalField}.
     */
    final private ObjectProperty<BigDecimal> number;

    /**
     * @return The number of this control as {@link java.math.BigDecimal}.
     */
    public BigDecimal getNumber() {
        return number.getValue();
    }

    /**
     *
     * @param value
     * @throws java.lang.IllegalArgumentException if minValue and/or maxValue are set and value is out of these bounds.
     */
    public void setNumber(BigDecimal value) {
        if (checkBounds(value) == false) {
            String message = MessageFormat.format("number {0} is out of bounds({1}, {2})", value, minValue.get(), maxValue.get());
            throw new IllegalArgumentException(message);
        }
        number.set(value);
    }

    /**
     * Checks if value is between minValue and maxValue (both including) if set at all.
     *
     * @param value
     * @return
     */
    private boolean checkBounds(BigDecimal value) {
        if (value != null && getMaxValue() != null && value.compareTo(getMaxValue()) > 0) {
            return false;
        }
        if (value != null && getMinValue() != null && value.compareTo(getMinValue()) < 0) {
            return false;
        }
        return true;
    }

    /**
     * @return The property containing the BigDecimal number
     */
    public ObjectProperty<BigDecimal> numberProperty() {
        return number;
    }

    /**
     * Stepwidth for inc/dec operation
     */
    final private ObjectProperty<BigDecimal> stepwidth;
    public BigDecimal getStepwidth() { return stepwidth.getValue(); }
    public void setStepwidth(BigDecimal value) { stepwidth.set(value); }
    public ObjectProperty<BigDecimal> stepwidthProperty() { return stepwidth; }

    /**
     * Property that contains the {@link java.text.NumberFormat} that is used to format and parse the {@link #number}.
     */
    final private ObjectProperty<NumberFormat> format;
    public NumberFormat getFormat() {return format.getValue(); }
    public final void setFormat(NumberFormat value) { format.set(value); }
    public ObjectProperty<NumberFormat> formatProperty() { return format; }

    /**
     * Contains the text that is displayed in the control if no {@link #number}
     * is set.
     */
    final private StringProperty promptText;
    public String getPromptText() { return promptText.getValue(); }
    public final void setPromptText(String value) { promptText.setValue(value); }
    public StringProperty promptTextProperty() { return promptText; }

    /**
     * If set the control does not allow to enter a {@link #number} that is greater than maxValue.
     */
    final private ObjectProperty<BigDecimal> maxValue;
    public BigDecimal getMaxValue() { return maxValue.getValue(); }
    public void setMaxValue(BigDecimal value) { maxValue.set(value); }
    public ObjectProperty<BigDecimal> maxValueProperty() { return maxValue; }

    /**
     * If set the control does not allow to enter a {@link #number} that is smaller than minValue.
     */
    final private ObjectProperty<BigDecimal> minValue;
    public BigDecimal getMinValue() { return minValue.getValue(); }
    public void setMinValue(BigDecimal value) { minValue.set(value); }
    public ObjectProperty<BigDecimal> minValueProperty() { return minValue; }


    @Override
    public String getUserAgentStylesheet() {
        return getClass().getResource(
                "/jfxtras/labs/internal/scene/control/"
                        + getClass().getSimpleName() + ".css").toExternalForm();
    }
}
