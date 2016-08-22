package jfxtras.labs.icalendarfx.components;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;

import javafx.beans.property.ObjectProperty;
import jfxtras.labs.icalendarfx.properties.component.time.DateTimeEnd;
import jfxtras.labs.icalendarfx.properties.component.time.DateTimeStart;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities.DateTimeType;

/**
 * Interface for {@link DateTimeEnd} property
 * 
 * @author David Bal
 *
 * @param <T> concrete subclass
 */
public interface VComponentDateTimeEnd<T> extends VComponent
{
    /**
     *<p>This property specifies the date and time that a calendar
     * component ends.</p>
     */
    ObjectProperty<DateTimeEnd> dateTimeEndProperty();
    DateTimeEnd getDateTimeEnd();
    default void setDateTimeEnd(String dtEnd)
    {
        if (getDateTimeEnd() == null)
        {
            setDateTimeEnd(DateTimeEnd.parse(dtEnd));
        } else
        {
            DateTimeEnd temp = DateTimeEnd.parse(dtEnd);
            if (temp.getValue().getClass().equals(getDateTimeEnd().getValue().getClass()))
            {
                getDateTimeEnd().setValue(temp.getValue());
            } else
            {
                setDateTimeEnd(temp);
            }
        }
    }
    default void setDateTimeEnd(DateTimeEnd dtEnd) { dateTimeEndProperty().set(dtEnd); }
    default void setDateTimeEnd(Temporal temporal)
    {
        if ((getDateTimeEnd() == null) || ! getDateTimeEnd().getValue().getClass().equals(temporal.getClass()))
        {
            if (temporal instanceof LocalDate)
            {
                setDateTimeEnd(new DateTimeEnd(temporal));            
            } else if (temporal instanceof LocalDateTime)
            {
                setDateTimeEnd(new DateTimeEnd(temporal));            
            } else if (temporal instanceof ZonedDateTime)
            {
                setDateTimeEnd(new DateTimeEnd(temporal));            
            } else
            {
                throw new DateTimeException("Only LocalDate, LocalDateTime and ZonedDateTime supported. "
                        + temporal.getClass().getSimpleName() + " is not supported");
            }
        } else
        {
            getDateTimeEnd().setValue(temporal);
        }
    }
    default T withDateTimeEnd(Temporal dtEnd)
    {
//        if (getDateTimeEnd() == null)
//        {
            setDateTimeEnd(dtEnd);
            return (T) this;
//        } else
//        {
//            throw new IllegalArgumentException("Property can only occur once in the calendar component");
//        }
    }
    default T withDateTimeEnd(String dtEnd)
    {
//        if (getDateTimeEnd() == null)
//        {
            setDateTimeEnd(dtEnd);
            return (T) this;
//        } else
//        {
//            throw new IllegalArgumentException("Property can only occur once in the calendar component");
//        }
    }
    default T withDateTimeEnd(DateTimeEnd dtEnd)
    {
//        if (getDateTimeEnd() == null)
//        {
            setDateTimeEnd(dtEnd);
            return (T) this;
//        } else
//        {
//            throw new IllegalArgumentException("Property can only occur once in the calendar component");
//        }
    }
    
    // From VComponentPrimary
    DateTimeStart getDateTimeStart();
    
    /** Ensures DateTimeEnd has same date-time type as DateTimeStart.  Should be called by listener
     *  after dateTimeEndProperty() is initialized */
    default void checkDateTimeEndConsistency()
    {
        if ((getDateTimeEnd() != null) && (getDateTimeStart() != null))
        {
            DateTimeType dateTimeEndType = DateTimeUtilities.DateTimeType.of(getDateTimeEnd().getValue());
            DateTimeType dateTimeStartType = DateTimeUtilities.DateTimeType.of(getDateTimeStart().getValue());
            if (dateTimeEndType != dateTimeStartType)
            {
                throw new DateTimeException("DateTimeEnd DateTimeType (" + dateTimeEndType +
                        ") must be same as the DateTimeType of DateTimeStart (" + dateTimeStartType + ")");
            }
        }
    }
    
//    @Override
//    default List<String> errors()
//    {
//        List<String> errors = new ArrayList<>();
//        if (getDateTimeEnd() != null)
//        {
//            if (getDateTimeStart() != null)
//            {
//                DateTimeType startType = DateTimeUtilities.DateTimeType.of(getDateTimeStart().getValue());
//                DateTimeType endType = DateTimeUtilities.DateTimeType.of(getDateTimeEnd().getValue());
//                boolean isDateTimeEndMatch = startType == endType;
//                if (! isDateTimeEndMatch)
//                {
//                    errors.add("The value type of DTEND MUST be the same as the DTSTART property (" + endType + ", " + startType);
//                }
//            }
//        }
//        return errors();
//    }
}
