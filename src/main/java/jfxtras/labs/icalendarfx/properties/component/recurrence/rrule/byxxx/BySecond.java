package jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx;

import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.stream.Stream;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;

public class BySecond extends ByRuleAbstract<ObservableList<Integer>, BySecond>
{
    public BySecond()
    {
        super(BySecond.class);
        throw new RuntimeException("not implemented");
    }
        
    public BySecond(String value)
    {
        this();
    }
    
    public BySecond(ByRule source)
    {
        super(source);
    }

    @Override
    public Stream<Temporal> streamRecurrences(Stream<Temporal> inStream, ObjectProperty<ChronoUnit> chronoUnit,
            Temporal startDateTime) {
        // TODO Auto-generated method stub
        return null;
    }
}
