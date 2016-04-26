package jfxtras.labs.icalendarfx.components;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;

import javafx.beans.property.ObjectProperty;
import jfxtras.labs.icalendarfx.properties.component.time.DateTimeEnd;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities.DateTimeType;

public interface VComponentDateTimeEnd<T> extends VComponentPersonal<T>
{
    /**
     * DTEND, Date-Time End.
     * RFC 5545 iCalendar 3.8.2.2 page 95
     * Specifies the date and time that a calendar component ends.
     * Can't be used if DURATION is used.  Must be one or the other.
     * Must be same Temporal type as dateTimeStart (DTSTART)
     */
    ObjectProperty<DateTimeEnd<? extends Temporal>> dateTimeEndProperty();
    default DateTimeEnd<? extends Temporal> getDateTimeEnd() { return dateTimeEndProperty().get(); }
    default void setDateTimeEnd(String dtEnd) { setDateTimeEnd(DateTimeUtilities.temporalFromString(dtEnd)); }
    default void setDateTimeEnd(DateTimeEnd<? extends Temporal> dtEnd) { dateTimeEndProperty().set(dtEnd); }
    default void setDateTimeEnd(Temporal temporal)
    {
        if (temporal instanceof LocalDate)
        {
            setDateTimeEnd(new DateTimeEnd<LocalDate>((LocalDate) temporal));            
        } else if (temporal instanceof LocalDateTime)
        {
            setDateTimeEnd(new DateTimeEnd<LocalDateTime>((LocalDateTime) temporal));            
        } else if (temporal instanceof ZonedDateTime)
        {
            setDateTimeEnd(new DateTimeEnd<ZonedDateTime>((ZonedDateTime) temporal));            
        } else
        {
            throw new DateTimeException("Only LocalDate, LocalDateTime and ZonedDateTime supported. "
                    + temporal.getClass().getSimpleName() + " is not supported");
        }
    }
    default T withDateTimeEnd(Temporal temporal) { setDateTimeEnd(temporal); return (T) this; }
    default T withDateTimeEnd(String dtEnd) { setDateTimeEnd(dtEnd); return (T) this; }
    default T withDateTimeEnd(DateTimeEnd<? extends Temporal> dtEnd) { setDateTimeEnd(dtEnd); return (T) this; }
    
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
    
    @Override
    default boolean isValid()
    {
        final boolean isDateTimeEndMatch;
        if (getDateTimeEnd() != null)
        {
            DateTimeType startType = DateTimeUtilities.DateTimeType.of(getDateTimeStart().getValue());
            DateTimeType endType = DateTimeUtilities.DateTimeType.of(getDateTimeEnd().getValue());
            isDateTimeEndMatch = startType == endType;
        } else
        {
            isDateTimeEndMatch = true;
        }
        return isDateTimeEndMatch;
    }
}
