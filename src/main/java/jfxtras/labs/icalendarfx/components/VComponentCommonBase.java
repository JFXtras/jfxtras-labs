package jfxtras.labs.icalendarfx.components;

import java.util.Arrays;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.properties.PropertyType;
import jfxtras.labs.icalendarfx.properties.component.misc.IANAProperty;
import jfxtras.labs.icalendarfx.properties.component.misc.NonStandardProperty;

public abstract class VComponentCommonBase<T> extends VComponentBase
{
    /*
     * 3.8.8.2.  Non-Standard Properties
     * Any property name with a "X-" prefix
     * 
     * Example:
     * X-ABC-MMSUBJ;VALUE=URI;FMTTYPE=audio/basic:http://www.example.
     *  org/mysubj.au
     */
    public ObservableList<NonStandardProperty> getNonStandardProperties() { return nonStandardProps; }
    private ObservableList<NonStandardProperty> nonStandardProps;
    public void setNonStandardProperties(ObservableList<NonStandardProperty> nonStandardProps)
    {
        if (nonStandardProps != null)
        {
            orderer().registerSortOrderProperty(nonStandardProps);
        } else
        {
            orderer().unregisterSortOrderProperty(this.nonStandardProps);
        }
        this.nonStandardProps = nonStandardProps;
    }
    public T withNonStandardProperty(String...nonStandardProps)
    {
        Arrays.stream(nonStandardProps).forEach(c -> PropertyType.NON_STANDARD.parse(this, c));
        return (T) this;
    }
    public T withNonStandardProperty(ObservableList<NonStandardProperty> nonStandardProps) { setNonStandardProperties(nonStandardProps); return (T) this; }
    public T withNonStandardProperty(NonStandardProperty...nonStandardProps)
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
    /** add comma separated nonStandardProps into separate nonStandardProps objects */
    
    /*
     * 3.8.8.1.  IANA Properties
     * An IANA-registered property name
     * 
     * Examples:
     * NON-SMOKING;VALUE=BOOLEAN:TRUE
     * DRESSCODE:CASUAL
     */
    public ObservableList<IANAProperty> getIANAProperties() { return ianaProps; }
    private ObservableList<IANAProperty> ianaProps;
    public void setIANAProperties(ObservableList<IANAProperty> ianaProps)
    {
        if (ianaProps != null)
        {
            orderer().registerSortOrderProperty(ianaProps);
        } else
        {
            orderer().unregisterSortOrderProperty(this.ianaProps);
        }
        this.ianaProps = ianaProps;
    }
    public T withIANAProperty(String...ianaProps)
    {
        Arrays.stream(ianaProps).forEach(c -> PropertyType.IANA_PROPERTY.parse(this, c));
        return (T) this;
    }
    public T withIANAProperty(ObservableList<IANAProperty> ianaProps) { setIANAProperties(ianaProps); return (T) this; }
    public T withIANAProperty(IANAProperty...ianaProps)
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

    /*
     * CONSTRUCTORS
     */
    VComponentCommonBase()
    {
        super();
    }
    
    /** Copy constructor */
    public VComponentCommonBase(VComponentCommonBase<T> source)
    {
        super(source);
    }
}
