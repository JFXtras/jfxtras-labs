package jfxtras.labs.icalendarfx.properties.component.recurrence.rrule;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

abstract public class RRuleElementBase<T, U> implements RRuleElement<T>
{
    @Override
    public T getValue() { return value.get(); }
    @Override
    public ObjectProperty<T> valueProperty() { return value; }
    private ObjectProperty<T> value;
    @Override
    public void setValue(T value) { this.value.set(value); }
    public U withValue(T value) { setValue(value); return (U) this; }
    
    /**
     * PARAMETER TYPE
     * 
     *  The enumerated type of the parameter.
     */
    @Override
    public RRuleElementType elementType() { return elementType; }
    final private RRuleElementType elementType;
    
    /*
     * CONSTRUCTORS
     */
    protected RRuleElementBase()
    {
        elementType = RRuleElementType.enumFromClass(getClass());
        value = new SimpleObjectProperty<>(this, elementType.toString());
    }
    
    @Override
    public String toString()
    {
        return super.toString() + ", " + toContent();
    }
}
