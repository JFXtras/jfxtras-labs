package jfxtras.labs.icalendarfx.properties.component.recurrence;

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
import jfxtras.labs.icalendarfx.parameters.ValueType;
import jfxtras.labs.icalendarfx.properties.PropertyBaseDateTime;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities.DateTimeType;

/**
 * Abstract class for Exceptions and Recurrences
 * 
 * @author David Bal
 *
 * @param <U> - subclass
 * @param <T> - property value type
 * @see Exceptions
 * @see Recurrences
 */
public abstract class PropertyBaseRecurrence<T extends Temporal, U> extends PropertyBaseDateTime<ObservableSet<T>, U>
{
    private ZoneId zone;
    private DateTimeType myType;
    private final SetChangeListener<T> recurrenceListener = (SetChangeListener<T>) (SetChangeListener.Change<? extends T> change) ->
    {
        if (change.wasAdded())
        {
            Temporal newTemporal = change.getElementAdded();
            DateTimeType newType = DateTimeType.of(newTemporal);
            if (newType != myType)
            {
                change.getSet().remove(newTemporal);
                throw new DateTimeException("Can't add new element of type " + newType + ". New elements must match type of existing elements (" + myType + ")");
            }
            if (newTemporal instanceof ZonedDateTime)
            {
                ZoneId myZone = ((ZonedDateTime) newTemporal).getZone();
                if (! myZone.equals(zone))
                {
                    change.getSet().remove(newTemporal);
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
    
    /*
     * CONSTRUCTORS
     */
    public PropertyBaseRecurrence(CharSequence contentLine)
    {
        this();
        parseContent(contentLine);
    }

    public PropertyBaseRecurrence(ObservableSet<T> value)
    {
        this();
        setValue(value);
        if (! isValid())
        {
            throw new IllegalArgumentException("Error in parsing " + value);
        }
    }
    
    public PropertyBaseRecurrence()
    {
        super();
        setConverter(CONVERTER);
    }

    // Listen to additions to collection to ensure time zone is consistent
    private void setupListener()
    {
        if (! getValue().isEmpty())
        {
            T sampleValue = getValue().iterator().next();
            myType = DateTimeType.of(sampleValue);
            getValue().addListener(recurrenceListener);
            if (sampleValue instanceof ZonedDateTime)
            {
                zone = ((ZonedDateTime) sampleValue).getZone();
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
        System.out.println("setup listener");
        super.setValue(value);
        setupListener();
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
