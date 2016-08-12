package jfxtras.labs.icalendarfx.components;

import java.util.Arrays;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.properties.PropertyType;
import jfxtras.labs.icalendarfx.properties.component.misc.IANAProperty;
import jfxtras.labs.icalendarfx.properties.component.misc.NonStandardProperty;

/**
 * <p>ICalendar Component with the following properties:
 * <ul>
 * <li> {@link NonStandardProperty}
 * <li> {@link IANAProperty}
 * </ul>
 * </p>
 * 
 * @author David Bal
 *
 * @param <T>  concrete subclass
 */
public interface VComponentCommon<T> extends VComponent
{
    /**
     * 3.8.8.2.  Non-Standard Properties
     * Any property name with a "X-" prefix
     * 
     * Example:
     * X-ABC-MMSUBJ;VALUE=URI;FMTTYPE=audio/basic:http://www.example.
     *  org/mysubj.au
     */
    ObservableList<NonStandardProperty> getNonStandardProperties();
    void setNonStandardProperties(ObservableList<NonStandardProperty> properties);
    default T withNonStandardProperty(String...nonStandardProps)
    {
        Arrays.stream(nonStandardProps).forEach(c -> PropertyType.NON_STANDARD.parse(this, c));
        return (T) this;
    }
    default T withNonStandardProperty(ObservableList<NonStandardProperty> nonStandardProps) { setNonStandardProperties(nonStandardProps); return (T) this; }
    default T withNonStandardProperty(NonStandardProperty...nonStandardProps)
    {
        if (getNonStandardProperties() == null)
        {
            setNonStandardProperties(FXCollections.observableArrayList(nonStandardProps));
        } else
        {
            getNonStandardProperties().addAll(nonStandardProps);
        }
        return (T) this;
    }

    /**
     * 3.8.8.1.  IANA Properties
     * An IANA-registered property name
     * 
     * Examples:
     * NON-SMOKING;VALUE=BOOLEAN:TRUE
     * DRESSCODE:CASUAL
     */
    ObservableList<IANAProperty> getIANAProperties();
    void setIANAProperties(ObservableList<IANAProperty> properties);
    default T withIANAProperty(String...ianaProps)
    {
        Arrays.stream(ianaProps).forEach(c -> PropertyType.IANA_PROPERTY.parse(this, c));
        return (T) this;
    }
    default T withIANAProperty(ObservableList<IANAProperty> ianaProps) { setIANAProperties(ianaProps); return (T) this; }
    default T withIANAProperty(IANAProperty...ianaProps)
    {
        if (getIANAProperties() == null)
        {
            setIANAProperties(FXCollections.observableArrayList(ianaProps));
        } else
        {
            getIANAProperties().addAll(ianaProps);
        }
        return (T) this;
    }
}
