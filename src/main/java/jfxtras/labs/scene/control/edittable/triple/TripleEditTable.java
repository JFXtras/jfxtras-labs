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