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

import javafx.scene.control.ControlBuilder;
import javafx.util.Builder;

/**
 * Builder for {@link BigDecimalField}.
 * @author Thomas Bolz
 *
 */
public class BigDecimalFieldBuilder extends ControlBuilder<BigDecimalFieldBuilder> implements
		Builder<BigDecimalField> {

	private BigDecimalField control;
	
	private BigDecimalFieldBuilder() {
		super();
		control = new BigDecimalField();
	}

	public static final BigDecimalFieldBuilder create() {
		return new BigDecimalFieldBuilder();
	}

	public final BigDecimalFieldBuilder number(BigDecimal value) {
		control.setNumber(value);
		return this;
	}
	
	public final BigDecimalFieldBuilder format(NumberFormat value) {
		control.setFormat(value);
		return this;
	}
	
	public final BigDecimalFieldBuilder stepwidth(BigDecimal value) {
		control.setStepwidth(value);
		return this;
	}
	
	public final BigDecimalFieldBuilder promptText(String value) {
		control.setPromptText(value);
		return this;
	}
	
	public final BigDecimalFieldBuilder minValue(BigDecimal value) {
		control.setMinValue(value);
		return this;
	}
	
	public final BigDecimalFieldBuilder maxValue(BigDecimal value) {
		control.setMaxValue(value);
		return this;
	}
	
	
	@Override
	public BigDecimalField build() {
		return control;
	}


}
