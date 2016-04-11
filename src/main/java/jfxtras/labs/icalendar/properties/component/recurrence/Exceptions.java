package jfxtras.labs.icalendar.properties.component.recurrence;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.Arrays;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.util.StringConverter;
import jfxtras.labs.icalendar.parameters.ValueType;
import jfxtras.labs.icalendar.properties.PropertyBase;
import jfxtras.labs.icalendar.utilities.DateTimeUtilities;

public class Exceptions<T extends Temporal> extends PropertyBase<ObservableSet<T>, Exceptions<Temporal>>
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
        super();
        setConverter(CONVERTER);
        parseContent(contentLine);
    }

    public Exceptions(ObservableSet<T> value)
    {
        super();
        setConverter(CONVERTER);
        setValue(value);
    }

    @Override
    public void setValue(ObservableSet<T> value)
    {
        super.setValue(value);
        if (! value.isEmpty())
        {
            T sampleValue = value.iterator().next();
            if (sampleValue instanceof LocalDate)
            {
                setValueParameter(ValueType.DATE); // must set value parameter to force output of VALUE=DATE
            } else if (! (sampleValue instanceof LocalDateTime) && ! (sampleValue instanceof ZonedDateTime))
            {
                throw new RuntimeException("can't convert property value to type: " + sampleValue.getClass().getSimpleName() +
                        ". Accepted types are: " + propertyType().allowedValueTypes());                
            }
        }
    }
}
