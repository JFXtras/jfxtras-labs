package jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx;

import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.stream.Stream;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;

public class ByYearDay extends ByRuleAbstract<ObservableList<Integer>, ByYearDay>
{
    public ByYearDay()
    {
//        super(ByYearDay.class);
        super();
        throw new RuntimeException("not implemented");
    }
    
    public ByYearDay(String value)
    {
        this();
    }
    
    public ByYearDay(ByYearDay source)
    {
        super(source);
    }

    @Override
    public Stream<Temporal> streamRecurrences(Stream<Temporal> inStream, ObjectProperty<ChronoUnit> chronoUnit,
            Temporal startDateTime) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String toContent()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void parseContent(String content)
    {
        // TODO Auto-generated method stub
        
    }

}
