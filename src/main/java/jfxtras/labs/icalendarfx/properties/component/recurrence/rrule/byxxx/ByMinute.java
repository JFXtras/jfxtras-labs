package jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx;

import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.stream.Stream;

import javafx.beans.property.ObjectProperty;

public class ByMinute extends ByRuleAbstract
{
    public ByMinute()
    {
        super(ByMinute.class);
        throw new RuntimeException("not implemented");
    }
    
    public ByMinute(String value)
    {
        this();
    }
    
    public ByMinute(ByRule source)
    {
        super(source);
    }
    
    @Override
    public Stream<Temporal> stream(Stream<Temporal> inStream, ObjectProperty<ChronoUnit> chronoUnit,
            Temporal startDateTime) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void copyTo(ByRule destination) {
        // TODO Auto-generated method stub
        
    }


}
