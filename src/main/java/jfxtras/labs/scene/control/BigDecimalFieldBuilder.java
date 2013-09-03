/**
 * BigDecimalFieldBuilder.java
 *
 * Copyright (c) 2011-2013, JFXtras
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

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.HashMap;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.util.Builder;

/**
 * Builder for {@link BigDecimalField}.
 * 
 * @author Thomas Bolz
 * 
 */
public class BigDecimalFieldBuilder<B extends BigDecimalFieldBuilder<B>>
		implements Builder<BigDecimalField> {
	private static final String			NUMBER			= "number";
	private static final String			NUMBER_FORMAT	= "numberFormat";
	private static final String			STEPWIDTH		= "stepwidth";
	private static final String			PROMPT_TEXT		= "promptText";
	private static final String			MIN_VALUE		= "minValue";
	private static final String			MAX_VALUE		= "maxValue";
	private HashMap<String, Property>	properties		= new HashMap<String, Property>();

	// ******************** Constructors **************************************
	protected BigDecimalFieldBuilder() {
		super();
	};

	// ******************** Methods *******************************************
	public static final BigDecimalFieldBuilder create() {
		return new BigDecimalFieldBuilder();
	}

	public final BigDecimalFieldBuilder number(BigDecimal number) {
		properties.put(NUMBER, new SimpleObjectProperty<BigDecimal>(number));
		return this;
	}

	public final BigDecimalFieldBuilder format(NumberFormat numberFormat) {
		properties.put(NUMBER_FORMAT, new SimpleObjectProperty<NumberFormat>(numberFormat));
		return this;
	}

	public final BigDecimalFieldBuilder stepwidth(BigDecimal stepwidth) {
		properties.put(STEPWIDTH, new SimpleObjectProperty<BigDecimal>(stepwidth));
		return this;
	}

	public final BigDecimalFieldBuilder promptText(String promptText) {
		properties.put(PROMPT_TEXT, new SimpleStringProperty(promptText));
		return this;
	}

	public final BigDecimalFieldBuilder minValue(BigDecimal minValue) {
		properties.put(MIN_VALUE, new SimpleObjectProperty<BigDecimal>(minValue));
		return this;
	}

	public final BigDecimalFieldBuilder maxValue(BigDecimal maxValue) {
		properties.put(MAX_VALUE, new SimpleObjectProperty<BigDecimal>(maxValue));
		return this;
	}

	@Override
	public BigDecimalField build() {
		final BigDecimalField CONTROL = new BigDecimalField();
		for (String key : properties.keySet()) {
			if (MAX_VALUE.equals(key)) {
				CONTROL.setMaxValue(((SimpleObjectProperty<BigDecimal>) properties.get(key)).get());
			} else if (MIN_VALUE.equals(key)) {
				CONTROL.setMinValue(((SimpleObjectProperty<BigDecimal>) properties.get(key)).get());
			} else if (NUMBER.equals(key)) {
				CONTROL.setNumber(((SimpleObjectProperty<BigDecimal>) properties.get(key)).get());
			} else if (NUMBER_FORMAT.equals(key)) {
				CONTROL.setFormat(((SimpleObjectProperty<NumberFormat>) properties.get(key)).get());
			} else if (PROMPT_TEXT.equals(key)) {
				CONTROL.setPromptText(((SimpleStringProperty) properties.get(key)).get());
			} else if (STEPWIDTH.equals(key)) {
				CONTROL.setStepwidth(((SimpleObjectProperty<BigDecimal>) properties.get(key)).get());
			}
		}
		return CONTROL;
	}

}
