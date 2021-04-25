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
package jfxtras.labs.scene.control.edittable.triple;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import jfxtras.labs.internal.scene.control.skin.edittable.triple.TripleEditTableSkin;
import jfxtras.labs.scene.control.edittable.EditTable;

// TODO - make control instead of HBox?
public abstract class TripleEditTable<T,A,B,C> extends EditTable<T>
{
	// initialize tableList with extractor so change listeners fire when properties change.
		
	static private String language = "en";
	static private Locale myLocale = new Locale(language);
	static private ResourceBundle defaultResources  = ResourceBundle.getBundle("jfxtras.labs.scene.control.triple.Bundle", myLocale);
	protected final ResourceBundle resources;
	
	protected List<Triple<A,B,C>> tripleList;
	
	@Override
	public void setBeanList(List<T> beanList)
	{
		super.setBeanList(beanList);
		tripleList = beanList.stream()
			.map(e -> converter.fromBeanElement(e))
			.collect(Collectors.toList());
		TripleEditTableSkin<A,B,C> tripleEditTableSkin = (TripleEditTableSkin<A,B,C>) getSkin();
		System.out.println("tripleEditTableSkin:" + tripleEditTableSkin);
		if (tripleEditTableSkin != null)
		{
			ObservableList<Triple<A,B,C>> tableList = tripleEditTableSkin.getTableList();
			tableList.clear();
			System.out.println("clear tableList:"+tableList.size() + " " + tripleList);
			tableList.addAll(tripleList);
			if (tableList.isEmpty())
			{
				tableList.add(new Triple<A,B,C>());
			}
		}
	}
	
	private final Predicate<String> validateValue;
	private final String[] alertTexts;
	private final String[] nameOptions;
	private  TripleConverter<T,A,B,C> converter;
	
	protected ListChangeListener<Triple<A,B,C>> synchBeanItemTripleChangeLister = (ListChangeListener.Change<? extends Triple<A,B,C>> change) ->
    {
        while (change.next())
        {
            if (change.wasUpdated())
            {
            	int to = change.getTo();
            	int from = change.getFrom();
            	for (int i=from; i<to; i++)
            	{
            		Triple<A,B,C> t = change.getList().get(i);
            		// Only save bean item if value isn't null
            		if (t.getValue() != null)
            		{
	            		T e = converter.toBeanElement(t);
	            		System.out.println("i:" + i + " " + getBeanList().size());
	            		if (i <= getBeanList().size()-1)
	            		{
	            			System.out.println("changed existing element");
	            			getBeanList().set(i, e);
	            		} else
	            		{
	            			getBeanList().add(e);
	            			System.out.println("new element added");
	            		}
            		}
            	}
            }
        }
    };
	
	// CONSTRUCTOR
	public TripleEditTable(
			Predicate<String> validateValue,
			TripleConverter<T,A,B,C> converter,
			String[] alertTexts,
			String[] nameOptions,
			ResourceBundle resources
			)
	{
		super();
		this.validateValue = validateValue;
		this.converter = converter;
		this.alertTexts = alertTexts;
		this.nameOptions = nameOptions;
		this.resources = (resources == null) ? defaultResources : resources;
	}
}