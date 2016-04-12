package jfxtras.labs.icalendar.properties.component.recurrence;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.Arrays;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.util.StringConverter;
import jfxtras.labs.icalendar.parameters.ValueType;
import jfxtras.labs.icalendar.properties.PropertyBaseDateTime;
import jfxtras.labs.icalendar.utilities.DateTimeUtilities;

public abstract class PropertyBaseRecurrence<T extends Temporal, U> extends PropertyBaseDateTime<ObservableSet<T>, U>
{
    private ZoneId zone;
    private final SetChangeListener<T> recurrenceListener = (SetChangeListener<T>) (SetChangeListener.Change<? extends T> change) ->
    {
        if (change.wasAdded())
        {
            Temporal newTemporal = change.getElementAdded();
            if (newTemporal instanceof ZonedDateTime)
            {
                ZoneId myZone = ((ZonedDateTime) newTemporal).getZone();
                if (! myZone.equals(zone))
                {
                    throw new DateTimeException("Can't add new element with ZoneId of " + myZone + ". New elements must match ZoneId of existing elements (" + zone + ")");
                }
            }
        }
    };
    
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
        
    public PropertyBaseRecurrence(CharSequence contentLine)
    {
        super();
        setConverter(CONVERTER);
        parseContent(contentLine);
        setupListener();
    }

    public PropertyBaseRecurrence(ObservableSet<T> value)
    {
        super();
        setConverter(CONVERTER);
        setValue(value);
        setupListener();
        if (! isValid())
        {
            throw new IllegalArgumentException("Error in parsing " + value);
        }
    }
    
    // Listen to additions to collection to ensure time zone is consistent
    private void setupListener()
    {
        if (! getValue().isEmpty())
        {
            T sampleValue = getValue().iterator().next();
            if (sampleValue instanceof ZonedDateTime)
            {
                zone = ((ZonedDateTime) sampleValue).getZone();
                getValue().addListener(recurrenceListener);
            }
        }

    }

    @Override
    public void setValue(ObservableSet<T> value)
    {
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
        super.setValue(value);
    }
    
    @Override
    public boolean isValid()
    {
        if (! getValue().isEmpty())
        {
            T sampleValue = getValue().iterator().next();
            // ensure all ZoneId values are the same
            if (sampleValue instanceof ZonedDateTime)
            {
                zone = ((ZonedDateTime) sampleValue).getZone();
                boolean valuesMatchZone = getValue()
                        .stream()
                        .map(t -> ((ZonedDateTime) t).getZone())
                        .allMatch(z -> z.equals(zone));
                return valuesMatchZone && super.isValid();
            }
        }
        return super.isValid();
    }
}
