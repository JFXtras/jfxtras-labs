package jfxtras.labs.icalendar.properties;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import jfxtras.labs.icalendar.parameters.CommonName;
import jfxtras.labs.icalendar.parameters.ParameterEnum;

public abstract class CalendarUserAddressBase<T,U> extends LanguageBase<T,U>
{
    /**
     * CN
     * Common Name
     * RFC 5545, 3.2.2, page 15
     * 
     * To specify the common name to be associated with the calendar user specified by the property.
     * 
     * Example:
     * ORGANIZER;CN="John Smith":mailto:jsmith@example.com
     */
    public CommonName getCommonName() { return (commonName != null) ? commonName.get() : null; }
    public ObjectProperty<CommonName> commonNameProperty()
    {
        if (commonName == null)
        {
            commonName = new SimpleObjectProperty<>(this, ParameterEnum.COMMON_NAME.toString());
        }
        return commonName;
    }
    private ObjectProperty<CommonName> commonName;
    public void setCommonName(CommonName commonName)
    {
        if (commonName != null)
        {
            commonNameProperty().set(commonName);
        }
    }
    public void setCommonName(String value) { setCommonName(new CommonName(value)); }
    public T withCommonName(CommonName commonName) { setCommonName(commonName); return (T) this; }
    public T withCommonName(String content) { ParameterEnum.COMMON_NAME.parse(this, content); return (T) this; }    

    /*
     * CONSTRUCTORS
     */    
    protected CalendarUserAddressBase(String propertyString)
    {
        super(propertyString);
//        setValue((U) getPropertyValueString());
    }

    // copy constructor
    public CalendarUserAddressBase(CalendarUserAddressBase<T,U> property)
    {
        super(property);
        if (getCommonName() != null)
        {
            setCommonName(property.getCommonName());
        }
    }

    public CalendarUserAddressBase() { super(); }
}
