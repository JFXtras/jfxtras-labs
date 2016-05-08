package jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx;

import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Predicate;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public abstract class ByRuleIntegerListAbstract<U> extends ByRuleAbstract<ObservableList<Integer>, U>
{
    @Override
    public void setValue(ObservableList<Integer> values)
    {
        super.setValue(values);
        getValue().addListener(validValueListener);
    }
    public void setValue(Integer... values)
    {
        setValue(FXCollections.observableArrayList(values));
    }
    public void setValue(String values)
    {
        parseContent(values);
    }
    public U withValue(Integer... values)
    {
        setValue(values);
        return (U) this;
    }
    public U withValue(String values)
    {
        setValue(values);
        return (U) this;
    }
    
    /*
     * CONSTRUCTORS
     */
    public ByRuleIntegerListAbstract()
    {
        super();
        setValue(FXCollections.observableArrayList());
    }
    
    public ByRuleIntegerListAbstract(Integer... yearDays)
    {
        this();
        setValue(yearDays);
    }
    
    public ByRuleIntegerListAbstract(ByRuleIntegerListAbstract<U> source)
    {
        super(source);
    }
    
    /**
     * Listener to validate additions to value list
     */
    private ListChangeListener<Integer> validValueListener = (ListChangeListener.Change<? extends Integer> change) ->
    {
        while (change.next())
        {
            if (change.wasAdded())
            {
                Iterator<? extends Integer> i = change.getAddedSubList().iterator();
                while (i.hasNext())
                {
                    int value = i.next();
                    if (isValidValue().test(value))
                    {
                        throw new IllegalArgumentException("Invalid " + elementType().toString() + " value (" + value + ").  Out of range.");
                    }
                }
            }
        }
    };
    abstract Predicate<Integer> isValidValue();
    
    @Override
    public void parseContent(String content)
    {
        Integer[] monthDayArray = Arrays.asList(content.split(","))
                .stream()
                .map(s -> Integer.parseInt(s))
                .toArray(size -> new Integer[size]);
        setValue(FXCollections.observableArrayList(monthDayArray));
    }
}
