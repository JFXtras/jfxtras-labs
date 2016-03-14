package jfxtras.labs.icalendar.properties.recurrence.rrule.byxxx;

import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.stream.Stream;

import javafx.beans.property.ObjectProperty;

public class BySetPos extends ByRuleAbstract
{
    private final static ByRuleParameter MY_RULE = ByRuleParameter.BY_SET_POSITION;

    public BySetPos()
    {
        super(MY_RULE);
        throw new RuntimeException("not implemented");
    }
    
    @Override
    public Stream<Temporal> stream(Stream<Temporal> inStream, ObjectProperty<ChronoUnit> chronoUnit,
            Temporal startDateTime) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void copyTo(Rule destination) {
        // TODO Auto-generated method stub
        
    }

}
