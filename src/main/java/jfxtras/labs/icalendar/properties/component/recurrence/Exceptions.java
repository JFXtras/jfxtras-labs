package jfxtras.labs.icalendar.properties.component.recurrence;

import java.time.temporal.Temporal;
import java.util.Arrays;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.util.StringConverter;
import jfxtras.labs.icalendar.properties.PropertyBase;
import jfxtras.labs.icalendar.utilities.DateTimeUtilities;

public class Exceptions<T extends Temporal> extends PropertyBase<Exceptions<T>, ObservableSet<T>>
{
    private final StringConverter<ObservableSet<T>> CONVERTER = new StringConverter<ObservableSet<T>>()
    {
        @Override
        public String toString(ObservableSet<T> object)
        {
            return object.stream()
                    .sorted()
                    .map(t -> DateTimeUtilities.temporalToString(t))
                    .collect(Collectors.joining(","));
        }

        @Override
        public ObservableSet<T> fromString(String string)
        {
            return (ObservableSet<T>) FXCollections.observableSet(Arrays.stream(string.split(","))
                    .map(s -> DateTimeUtilities.temporalFromString(s))
                    .collect(Collectors.toSet()));
        }
    };
    
    public Exceptions(CharSequence contentLine)
    {
//        super();
//        this.construct(contentLine, CONVERTER);
        
        // null as argument for string converter causes default converter from ValueType to be used
        super(contentLine, null);
//        setConverter(CONVERTER);
    }

    public Exceptions(ObservableSet<T> value)
    {
        super(value, null);
        setConverter(CONVERTER);
    }
}
