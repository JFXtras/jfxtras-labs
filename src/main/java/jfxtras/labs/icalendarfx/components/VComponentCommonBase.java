package jfxtras.labs.icalendarfx.components;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.properties.PropertyType;
import jfxtras.labs.icalendarfx.properties.component.misc.IANAProperty;
import jfxtras.labs.icalendarfx.properties.component.misc.NonStandardProperty;

/**
 * <p>{@link VComponent} with the following properties
 * <ul>
 * <li>{@link NonStandardProperty X-PROP}
 * <li>{@link IANAProperty IANA-PROP}
 * </ul>
 * </p>
 * 
 * @author David Bal
 *
 * @param <T> concrete subclass
 */
public abstract class VComponentCommonBase<T> extends VComponentBase
{
    /**
     * Provides a framework for defining non-standard properties.
     */
    public ObjectProperty<ObservableList<NonStandardProperty>> nonStandardProperty()
    {
        if (nonStandardProps == null)
        {
            nonStandardProps = new SimpleObjectProperty<>(this, PropertyType.NON_STANDARD.toString());
        }
        return nonStandardProps;
    }
    private ObjectProperty<ObservableList<NonStandardProperty>> nonStandardProps;
    public ObservableList<NonStandardProperty> getNonStandard()
    {
        return (nonStandardProps == null) ? null : nonStandardProps.get();
    }
    public void setNonStandard(ObservableList<NonStandardProperty> nonStandardProps)
    {
        if (nonStandardProps != null)
        {
            orderer().registerSortOrderProperty(nonStandardProps);
        } else
        {
            orderer().unregisterSortOrderProperty(nonStandardProperty().get());
        }
        nonStandardProperty().set(nonStandardProps);
    }
    /**
     * Sets the value of the {@link #nonStandardProperty()} by parsing a vararg of
     * iCalendar content text representing individual {@link NonStandardProperty} objects.
     * 
     * @return - this class for chaining
     */
    public T withNonStandard(String...nonStandardProps)
    {
        List<NonStandardProperty> a = Arrays.stream(nonStandardProps)
                .map(c -> NonStandardProperty.parse(c))
                .collect(Collectors.toList());
        setNonStandard(FXCollections.observableArrayList(a));
        return (T) this;
    }
    /**
     * Sets the value of the {@link #nonStandardProperty()}
     * 
     * @return - this class for chaining
     */
    public T withNonStandard(ObservableList<NonStandardProperty> nonStandardProps)
    {
        setNonStandard(nonStandardProps);
        return (T) this;
    }
    /**
     * Sets the value of the {@link #nonStandardProperty()} from a vararg of {@link NonStandardProperty} objects.
     * 
     * @return - this class for chaining
     */    
    public T withNonStandard(NonStandardProperty...nonStandardProps)
    {
        setNonStandard(FXCollections.observableArrayList(nonStandardProps));
        return (T) this;
    }
    
    /**
     *<p>Allows other properties registered
     * with IANA to be specified in any calendar components.</p>
     */
    public ObjectProperty<ObservableList<IANAProperty>> ianaProperty()
    {
        if (ianaProps == null)
        {
            ianaProps = new SimpleObjectProperty<>(this, PropertyType.IANA_PROPERTY.toString());
        }
        return ianaProps;
    }
    public ObservableList<IANAProperty> getIana()
    {
        return (ianaProps == null) ? null : ianaProps.get();
    }
    private ObjectProperty<ObservableList<IANAProperty>> ianaProps;
    public void setIana(ObservableList<IANAProperty> ianaProps)
    {
        if (ianaProps != null)
        {
            orderer().registerSortOrderProperty(ianaProps);
        } else
        {
            orderer().unregisterSortOrderProperty(ianaProperty().get());
        }
        ianaProperty().set(ianaProps);
    }
    /**
     * Sets the value of the {@link #IANAProperty()} by parsing a vararg of
     * iCalendar content text representing individual {@link IANAProperty} objects.
     * 
     * @return - this class for chaining
     */
    public T withIana(String...ianaProps)
    {
        List<IANAProperty> a = Arrays.stream(ianaProps)
                .map(c -> IANAProperty.parse(c))
                .collect(Collectors.toList());
        setIana(FXCollections.observableArrayList(a));
        return (T) this;
    }
    /**
     * Sets the value of the {@link #IANAProperty()}
     * 
     * @return - this class for chaining
     */
    public T withIana(ObservableList<IANAProperty> ianaProps)
    {
        setIana(ianaProps);
        return (T) this;
    }
    /**
     * Sets the value of the {@link #IANAProperty()} from a vararg of {@link IANAProperty} objects.
     * 
     * @return - this class for chaining
     */    
    public T withIana(IANAProperty...ianaProps)
    {
        setIana(FXCollections.observableArrayList(ianaProps));
        return (T) this;
    }

    /*
     * CONSTRUCTORS
     */
    VComponentCommonBase()
    {
        super();
    }
    
    VComponentCommonBase(VComponentCommonBase<T> source)
    {
        super(source);
    }
}
