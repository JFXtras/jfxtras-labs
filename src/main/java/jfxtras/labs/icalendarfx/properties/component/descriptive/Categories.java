package jfxtras.labs.icalendarfx.properties.component.descriptive;

import java.util.Arrays;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.StringConverter;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.components.VJournal;
import jfxtras.labs.icalendarfx.components.VTodo;
import jfxtras.labs.icalendarfx.properties.PropertyBaseLanguage;
import jfxtras.labs.icalendarfx.properties.ValueType;

/**
 * CATEGORIES
 * RFC 5545, 3.8.1.2, page 81
 * 
 * This property defines the categories for a calendar component.
 * 
 * Examples:
 * CATEGORIES:APPOINTMENT,EDUCATION
 * CATEGORIES:MEETING
 * 
 * @author David Bal
 * 
 * The property can be specified in following components:
 * @see VEvent
 * @see VTodo
 * @see VJournal
 */
public class Categories extends PropertyBaseLanguage<ObservableList<String>, Categories>
{
    private final static StringConverter<ObservableList<String>> CONVERTER = new StringConverter<ObservableList<String>>()
    {
        @Override
        public String toString(ObservableList<String> object)
        {
            return object.stream()
                    .map(v -> ValueType.TEXT.getConverter().toString(v)) // escape special characters
                    .collect(Collectors.joining(","));
        }

        @Override
        public ObservableList<String> fromString(String string)
        {
            return FXCollections.observableArrayList(Arrays.stream(string.replace("\\,", "~~").split(",")) // change comma escape sequence to avoid splitting by it
                    .map(s -> s.replace("~~", "\\,"))
                    .map(v -> (String) ValueType.TEXT.getConverter().fromString(v)) // unescape special characters
                    .collect(Collectors.toList()));
        }
    };
    
    public Categories(ObservableList<String> values)
    {
        this();
        setValue(values);
    }
    
    /** Constructor with varargs of property values 
     * Note: Do not use to parse the content line.  Use static parse method instead.*/
    public Categories(String...values)
    {
        this();
        setValue(FXCollections.observableArrayList(values));
    }
    
    public Categories(Categories source)
    {
        super(source);
    }
    
    public Categories()
    {
        super();
        setConverter(CONVERTER);
    }

    public static Categories parse(String propertyContent)
    {
        Categories property = new Categories();
        property.parseContent(propertyContent);
        return property;
    }
    
    @Override
    protected ObservableList<String> copyValue(ObservableList<String> source)
    {
        return FXCollections.observableArrayList(source);
    }
}
