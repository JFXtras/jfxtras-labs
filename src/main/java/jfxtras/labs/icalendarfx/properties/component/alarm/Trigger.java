package jfxtras.labs.icalendarfx.properties.component.alarm;

import java.time.DateTimeException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAmount;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import jfxtras.labs.icalendarfx.components.VAlarm;
import jfxtras.labs.icalendarfx.parameters.AlarmTriggerRelationship;
import jfxtras.labs.icalendarfx.parameters.ParameterEnum;
import jfxtras.labs.icalendarfx.parameters.ValueParameter;
import jfxtras.labs.icalendarfx.parameters.ValueType;
import jfxtras.labs.icalendarfx.parameters.AlarmTriggerRelationship.AlarmTriggerRelationshipType;
import jfxtras.labs.icalendarfx.properties.PropertyAlarmTrigger;
import jfxtras.labs.icalendarfx.properties.PropertyBase;

/**
 * TRIGGER
 * RFC 5545, 3.8.6.3, page 133
 * 
 * This property specifies when an alarm will trigger.
 * 
 * Value defaults to DURATION, but can also be DATE-TIME.  Only UTC-formatted
 * DATE-TIME is valid.
 * 
 * Example:  A trigger set 15 minutes prior to the start of the event or to-do.
 * TRIGGER:-PT15M
 * A trigger set five minutes after the end of an event or the due date of a to-do.
 * TRIGGER;RELATED=END:PT5M
 * 
 * @author David Bal
 * 
 * The property can be specified in following components:
 * @see VAlarm
 */
public class Trigger<T> extends PropertyBase<T, Trigger<T>> implements PropertyAlarmTrigger<T>
{
    /**
    * RELATED: Alarm Trigger Relationship
    * RFC 5545, 3.2.14, page 24
    * To specify the relationship of the alarm trigger with
    * respect to the start or end of the calendar component.
    */
   @Override
   public AlarmTriggerRelationship getAlarmTrigger() { return (relationship == null) ? null : relationship.get(); }
   @Override
   public ObjectProperty<AlarmTriggerRelationship> AlarmTriggerProperty()
   {
       if (relationship == null)
       {
           relationship = new SimpleObjectProperty<>(this, ParameterEnum.FORMAT_TYPE.toString());
       }
       return relationship;
   }
   private ObjectProperty<AlarmTriggerRelationship> relationship;
   @Override
   public void setAlarmTrigger(AlarmTriggerRelationship relationship)
   {
       if (relationship != null)
       {
           ValueType valueType = (getValueParameter() == null) ? propertyType().allowedValueTypes().get(0) : getValueParameter().getValue();
           if (valueType == ValueType.DURATION)
           {
               AlarmTriggerProperty().set(relationship);
           } else
           {
               throw new IllegalArgumentException("Alarm Trigger Relationship can only be set if value type is DURATION");
           }
       }
   }
   public void setAlarmTrigger(AlarmTriggerRelationshipType type) { setAlarmTrigger(new AlarmTriggerRelationship(type)); } 
   public Trigger<T> withAlarmTrigger(AlarmTriggerRelationship format) { setAlarmTrigger(format); return this; }
   public Trigger<T> withAlarmTrigger(AlarmTriggerRelationshipType type) { setAlarmTrigger(type); return this; }
   public Trigger<T> withAlarmTrigger(String format) { setAlarmTrigger(new AlarmTriggerRelationship(format)); return this; }

   
    public Trigger(Class<T> clazz, CharSequence contentLine)
    {
        super(clazz, contentLine);
    }
    
    public Trigger(Trigger<T> source)
    {
        super(source);
    }
    
    public Trigger(T value)
    {
        super(value);
    }
    
    @Override
    public void setValue(T value)
    {
        if (value instanceof ZonedDateTime)
        {
            ZoneId zone = ((ZonedDateTime) value).getZone();
            if (! zone.equals(ZoneId.of("Z")))
            {
                throw new DateTimeException("Unsupported ZoneId:" + zone + " only Z supported");
            }
            setValueParameter(ValueType.DATE_TIME); // override default value type            
        }
        super.setValue(value);
    }

    @Override
    public void setValueParameter(ValueParameter valueType)
    {
        if ((valueType.getValue() == ValueType.DATE_TIME) && (getAlarmTrigger() != null))
        {
            throw new IllegalArgumentException("Value type can only be set to DATE-TIME if Alarm Trigger Relationship is null");
        }
        super.setValueParameter(valueType);
    }
    
    @Override
    protected void setConverterByClass(Class<T> clazz)
    {
        if (TemporalAmount.class.isAssignableFrom(clazz))
        {
            setConverter(ValueType.DURATION.getConverter());
        } else if (clazz.equals(ZonedDateTime.class))
        {
            setConverter(ValueType.DATE_TIME.getConverter());           
        } else
        {
            throw new IllegalArgumentException("Only parameterized types of Duration, Period and ZonedDateTime are supported.");           
        }
    }
    
    @Override
    public boolean isValid()
    {
        boolean isDateTimeValue = (getValueParameter() == null) ? false : getValueParameter().getValue() != ValueType.DATE_TIME;
        if (isDateTimeValue)
        {
            // The "RELATED" property parameter is not valid if the value type of the property is set to DATE-TIME
            if (getAlarmTrigger() != null)
            {
                return false;
            }
        }
        return true && super.isValid();
    }        
}
