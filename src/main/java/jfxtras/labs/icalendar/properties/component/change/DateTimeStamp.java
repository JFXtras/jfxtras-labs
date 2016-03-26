package jfxtras.labs.icalendar.properties.component.change;

import java.time.ZonedDateTime;

import jfxtras.labs.icalendar.properties.PropertyTimeBase;

public class DateTimeStamp extends PropertyTimeBase<DateTimeStamp, ZonedDateTime>
{
//    @Override public ZonedDateTime getValue() { return value.get(); }
//    public ObjectProperty<ZonedDateTime> valueProperty() { return value; }
//    final private ObjectProperty<ZonedDateTime> value = new SimpleObjectProperty<>(this, propertyType().toString());
//    public void setValue(ZonedDateTime temporal) { value.set(temporal); }
//    public DateTimeStamp withValue(ZonedDateTime temporal) { setValue(temporal); return this; }    
    
    public DateTimeStamp(ZonedDateTime temporal)
    {
        setValue(temporal);
    }

    public DateTimeStamp(String propertyString)
    {
        super(propertyString);
    }
    
    public DateTimeStamp(DateTimeStamp source)
    {
        super(source);
    }
    
    public DateTimeStamp()
    {
        super();
    }   

}
