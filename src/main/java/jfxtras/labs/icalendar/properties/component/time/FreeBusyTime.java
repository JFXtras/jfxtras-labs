package jfxtras.labs.icalendar.properties.component.time;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.util.Pair;
import jfxtras.labs.icalendar.parameters.FreeBusyType;
import jfxtras.labs.icalendar.parameters.FreeBusyType.FreeBusyTypeEnum;
import jfxtras.labs.icalendar.parameters.ParameterEnum;
import jfxtras.labs.icalendar.properties.PropertyBase;
import jfxtras.labs.icalendar.utilities.DateTimeUtilities;

/**
 * 
 * @author David Bal
 *
 */
public class FreeBusyTime extends PropertyBase<FreeBusyTime, List<Pair<ZonedDateTime, TemporalAmount>>>
{
    /**
     * FBTYPE: Incline Free/Busy Time Type
     * RFC 5545, 3.2.9, page 20
     * 
     * To specify the free or busy time type.
     * 
     * Values can be = "FBTYPE" "=" ("FREE" / "BUSY" / "BUSY-UNAVAILABLE" / "BUSY-TENTATIVE"
     */
    public FreeBusyType getFreeBusyType() { return (freeBusyType == null) ? null : freeBusyType.get(); }
    public ObjectProperty<FreeBusyType> freeBusyTypeProperty()
    {
        if (freeBusyType == null)
        {
            freeBusyType = new SimpleObjectProperty<>(this, ParameterEnum.INLINE_ENCODING.toString());
        }
        return freeBusyType;
    }
    private ObjectProperty<FreeBusyType> freeBusyType;
    public void setFreeBusyType(FreeBusyType freeBusyType)
    {
        if (freeBusyType != null)
        {
            freeBusyTypeProperty().set(freeBusyType);
        }
    }
    public void setFreeBusyType(FreeBusyTypeEnum type) { setFreeBusyType(new FreeBusyType(type)); }
    public FreeBusyTime withFreeBusyType(FreeBusyType freeBusyType) { setFreeBusyType(freeBusyType); return this; }
    public FreeBusyTime withFreeBusyType(FreeBusyTypeEnum type) { setFreeBusyType(type); return this; }
    public FreeBusyTime withFreeBusyType(String freeBusyType) { setFreeBusyType(new FreeBusyType(freeBusyType)); return this; }

    /*
     * CONSTRUCTORS
     */
    
    public FreeBusyTime(FreeBusyTime source)
    {
        super(source);
    }
    
    public FreeBusyTime(CharSequence contentLine)
    {
        super(contentLine);
    }
    
    @Override
    protected String valueToString(List<Pair<ZonedDateTime, TemporalAmount>> value)
    {
        return value.stream().map(p ->
        {
            StringBuilder builder = new StringBuilder(30);
            builder.append(DateTimeUtilities.ZONED_DATE_TIME_UTC_FORMATTER.format(p.getKey()));
            builder.append("/");
            builder.append(p.getValue().toString());
            return builder.toString();
        })
        .collect(Collectors.joining(","));
    }    
    
    @Override
    protected List<Pair<ZonedDateTime, TemporalAmount>> valueFromString(String propertyValueString)
    {
        List<Pair<ZonedDateTime, TemporalAmount>> periodList = new ArrayList<>();
        Arrays.asList(getPropertyValueString().split(",")).forEach(pair ->
        {
            String[] time = pair.split("/");
            ZonedDateTime startInclusive = ZonedDateTime.parse(time[0], DateTimeUtilities.ZONED_DATE_TIME_UTC_FORMATTER);
            final TemporalAmount duration;
            if (time[1].charAt(time[1].length()-1) == 'Z')
            {
                ZonedDateTime endExclusive = ZonedDateTime.parse(time[1], DateTimeUtilities.ZONED_DATE_TIME_UTC_FORMATTER);                
                duration = Duration.between(startInclusive, endExclusive);
            } else
            {
                duration = Duration.parse(time[1]);
            }
            periodList.add(new Pair<ZonedDateTime, TemporalAmount>(startInclusive, duration));
        });
        return periodList;
    }
}
