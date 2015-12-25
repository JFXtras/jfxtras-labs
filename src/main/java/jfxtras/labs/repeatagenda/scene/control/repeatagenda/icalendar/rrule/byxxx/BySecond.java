package jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.byxxx;

import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.stream.Stream;

import javafx.beans.property.ObjectProperty;

public class BySecond extends ByRuleAbstract
{
    private final static ByRules MY_RULE = ByRules.BYSECOND;

    public BySecond()
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
