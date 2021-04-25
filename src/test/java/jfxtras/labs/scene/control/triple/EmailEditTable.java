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
package jfxtras.labs.scene.control.triple;

import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import javafx.scene.control.Skin;
import jfxtras.labs.internal.scene.control.skin.edittable.triple.TripleEditTableSkinStringStringBoolean;
import jfxtras.labs.scene.control.edittable.triple.TripleConverter;
import jfxtras.labs.scene.control.edittable.triple.TripleEditTable;

public class EmailEditTable extends TripleEditTable<Email,String,String,Boolean>
{
	private final static Pattern EMAIL_REGEX = Pattern.compile("\\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}\\b", Pattern.CASE_INSENSITIVE);
	private static Predicate<String> validateValue = (Predicate<String>) (v) -> EMAIL_REGEX.matcher(v).matches();
//	private static String valueName = "email";
	private static TripleConverter<Email,String,String,Boolean> converter = new EmailConverter();

	private static String[] alertTexts = new String[] {
			"Invalid Email Number",
			"Can't save email address",
			"Enter valid email address",
			"OK"
	};
	private static String[] nameOptions = new String[] {
			"Personal",
			"Work",
			"Mom",
			"Dad",
			"Other"
	};
	private static ResourceBundle customBundle = null;
	
	// CONSTRUCTOR
	public EmailEditTable()
	{
		super(
			validateValue,
//			valueName,
			converter,
			alertTexts,
			nameOptions,
			customBundle
			);
		setId("emailEditTable");
	}
	
	@Override
	public Skin<?> createDefaultSkin()
	{
		return new TripleEditTableSkinStringStringBoolean<Email>(
				validateValue,
				tripleList,
				converter,
				synchBeanItemTripleChangeLister,
				alertTexts,
				nameOptions,
				resources,
				this
				);
	}
}
