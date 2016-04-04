package jfxtras.labs.icalendar.properties.component.time;

import java.time.ZonedDateTime;
import java.time.temporal.TemporalAmount;
import java.util.List;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.util.Pair;
import jfxtras.labs.icalendar.parameters.FreeBusyType;
import jfxtras.labs.icalendar.parameters.ParameterEnum;
import jfxtras.labs.icalendar.properties.PropertyBase;

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
    public FreeBusyTime withFreeBusyType(FreeBusyType freeBusyType) { setFreeBusyType(freeBusyType); return this; }
    public FreeBusyTime withFreeBusyType(String freeBusyType) { setFreeBusyType(new FreeBusyType(freeBusyType)); return this; }

    public FreeBusyTime(String contentLine)
    {
        super(contentLine);

//        // convert time periods can be specified as either a start
//        // and end DATE-TIME or a start DATE-TIME and DURATION.
//        Map<ZonedDateTime, TemporalAmount> periods = new HashMap<>();
//        Arrays.asList(getPropertyValueString().split(",")).forEach(pair ->
//        {
//            String[] time = pair.split("/");
//            ZonedDateTime startInclusive = ZonedDateTime.parse(time[0], DateTimeUtilities.ZONED_DATE_TIME_UTC_FORMATTER);
//            final TemporalAmount duration;
//            if (time[1].charAt(time[1].length()-1) == 'Z')
//            {
//                ZonedDateTime endExclusive = ZonedDateTime.parse(time[1], DateTimeUtilities.ZONED_DATE_TIME_UTC_FORMATTER);                
//                duration = Duration.between(startInclusive, endExclusive);
//            } else
//            {
//                duration = Duration.parse(time[1]);
//            }
//            periods.put(startInclusive, duration);
//        });
//        setValue(periods);
    }
    
//  @Override
//  public <U> U parse(String value)
//  {
//      String[] time = value.split("/");
//      ZonedDateTime startInclusive = ZonedDateTime.parse(time[0], DateTimeUtilities.ZONED_DATE_TIME_UTC_FORMATTER);
//      final Duration duration;
//      if (time[1].charAt(time[1].length()-1) == 'Z')
//      {
//          ZonedDateTime endExclusive = ZonedDateTime.parse(time[1], DateTimeUtilities.ZONED_DATE_TIME_UTC_FORMATTER);                
//          duration = Duration.between(startInclusive, endExclusive);
//      } else
//      {
//          duration = Duration.parse(time[1]);
//      }
//      return (U) new Pair<ZonedDateTime, Duration>(startInclusive, duration);
//  }
//
//  @Override
//  public <U> String makeContent(U value)
//  {
//      return value.toString();
//  }
    
    public FreeBusyTime(FreeBusyTime source)
    {
        super(source);
    }
    
    public FreeBusyTime()
    {
        super();
    }
}
