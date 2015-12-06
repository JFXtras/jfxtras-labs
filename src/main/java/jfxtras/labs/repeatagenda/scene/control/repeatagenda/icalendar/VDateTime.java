package jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Class that represents both DATE (3.3.4) and DATE-TIME (3.3.5)
 * from RFC 5545 iCalendar
 * 
 * @author David Bal
 *
 */
public class VDateTime implements Temporal
{
//    public ObjectProperty<LocalDateTime> dateTimeProperty = new SimpleObjectProperty<>();
    public LocalDateTime getLocalDateTime()
    {
        return (getLocalDate() == null) ? null
               : (getLocalTime() == null) ? getLocalDate().atStartOfDay()
               : LocalDateTime.of(getLocalDate(), getLocalTime());
    }
    public void setLocalDateTime(LocalDateTime dateTime)
    {
        setLocalDate(dateTime.toLocalDate());
        setLocalDate(dateTime.toLocalTime());
    }

    /** DATE part of DATE-TIME property */
    public ObjectProperty<LocalDate> dateProperty() { return date; }
    public LocalDate getLocalDate() { return date.get(); }
    private ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();
    public void setLocalDate(LocalDate date) { this.date.set(date); }
    
    /** TIME part of DATE-TIME property */
    public ObjectProperty<LocalTime> timeProperty()
    {
        if (time == null) time = new SimpleObjectProperty<LocalTime>(_time);
        return time;
    }
    private ObjectProperty<LocalTime> time;
    private LocalTime _time;
    public LocalTime getLocalTime()
    {
        return (time == null) ? _time : time.getValue();
    }
    public void setLocalDate(LocalTime time)
    {
        if (time == null)
        {
            _time = time;
        } else
        {
            this.time.set(time);
        }
    }

    public boolean isWholeDay() { return time == null; }
    
    @Override
    public boolean isSupported(TemporalField field) {
        return getLocalDateTime().isSupported(field);
    }

    @Override
    public long getLong(TemporalField field) {
        return getLocalDateTime().getLong(field);
    }

    @Override
    public boolean isSupported(TemporalUnit unit) {
        return getLocalDateTime().isSupported(unit);
    }

    @Override
    public Temporal with(TemporalField field, long newValue) {
        return getLocalDateTime().with(field, newValue);
    }

    @Override
    public Temporal plus(long amountToAdd, TemporalUnit unit) {
        return getLocalDateTime().plus(amountToAdd, unit);
    }

    @Override
    public long until(Temporal endExclusive, TemporalUnit unit) {
        return getLocalDateTime().until(endExclusive, unit);
    }

}
