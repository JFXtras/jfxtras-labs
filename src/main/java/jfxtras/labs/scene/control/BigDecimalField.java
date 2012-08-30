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

package jfxtras.labs.scene.control;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Control;

/**
 * Input field for BigDecimal values. This control has the following features: -
 * BigDecimal {@link #number} is parsed and formatted according to the provided
 * NumberFormat - up/down arrow keys and buttons increment/decrement the
 * {@link #number} by {@link #stepwidth}
 * 
 * @author Thomas Bolz
 */
public class BigDecimalField extends Control {

	public BigDecimalField() {
		super();
		setStyle(null);
		getStyleClass().add("big-decimal-field");
		number = new SimpleObjectProperty<BigDecimal>(this, "number");
		stepwidth = new SimpleObjectProperty<BigDecimal>(this, "stepwidth", BigDecimal.ONE);
		format = new SimpleObjectProperty<NumberFormat>(this, "format",
				NumberFormat.getNumberInstance());
		promptText = new SimpleStringProperty(this, "promptText", "");
	}

	public BigDecimalField(BigDecimal number) {
		this();
		setNumber(number);
	}

	public BigDecimalField(BigDecimal initialValue, BigDecimal stepwidth,
			NumberFormat format) {
		this();
		this.number.set(initialValue);
		this.stepwidth.set(stepwidth);
		this.format.set(format);
	}

	/**
	 * @return The text representation of number
	 */
	public String getText() {
		if (number.getValue() != null)
			return getFormat().format(number.getValue());
		else
			return null;
	}

	/**
	 * @param formattedNumber
	 *            representation of number
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
	 * increments the number by stepwidth
	 */
	public void increment() {
		if (getNumber() != null && getStepwidth() != null) {
			setNumber(getNumber().add(getStepwidth()));
		}
	}

	/**
	 * decrements the number by stepwidth
	 */
	public void decrement() {
		if (getNumber() != null && getStepwidth() != null) {
			setNumber(getNumber().subtract(getStepwidth()));
		}
	}

	final private ObjectProperty<BigDecimal> number;

	/**
	 * @return The BigDecimal number
	 */
	public BigDecimal getNumber() {
		return number.getValue();
	}

	/**
	 * Set the BigDecimal number
	 */
	public void setNumber(BigDecimal value) {
		number.set(value);
	}

	/**
	 * @return The property containing the BigDecimal number
	 */
	public ObjectProperty<BigDecimal> numberProperty() {
		return number;
	}

	/**
	 * stepwidth for inc/dec operation
	 */
	final private ObjectProperty<BigDecimal> stepwidth;

	/**
	 * stepwidth for inc/dec operation
	 */
	public BigDecimal getStepwidth() {
		return stepwidth.getValue();
	}

	/**
	 * stepwidth for inc/dec operation
	 */
	public void setStepwidth(BigDecimal value) {
		stepwidth.set(value);
	}

	/**
	 * stepwidth for inc/dec operation
	 */
	public ObjectProperty<BigDecimal> stepwidthProperty() {
		return stepwidth;
	}

	final private ObjectProperty<NumberFormat> format;

	public NumberFormat getFormat() {
		return format.getValue();
	}

	public final void setFormat(NumberFormat value) {
		format.set(value);
	}

	public ObjectProperty<NumberFormat> formatProperty() {
		return format;
	}

	final private StringProperty promptText;

	public String getPromptText() {
		return promptText.getValue();
	}

	public final void setPromptText(String value) {
		promptText.setValue(value);
	}

	public StringProperty promptTextProperty() {
		return promptText;
	}

	@Override
	protected String getUserAgentStylesheet() {
		return getClass().getResource(
				"/jfxtras/labs/internal/scene/control/"
						+ getClass().getSimpleName() + ".css").toExternalForm();
	}
}
