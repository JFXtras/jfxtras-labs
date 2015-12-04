package jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.byxxx;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.stream.Stream;

import javafx.beans.property.ObjectProperty;

public class BySetPos extends ByRuleAbstract
{
    private final static ByRules MY_RULE = ByRules.BYSETPOS;

    public BySetPos()
    {
        super(MY_RULE);
        throw new RuntimeException("not implemented");
    }
    
    @Override
    public Stream<LocalDateTime> stream(Stream<LocalDateTime> inStream, ObjectProperty<ChronoUnit> chronoUnit,
            LocalDateTime startDateTime) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void copyTo(Rule destination) {
        // TODO Auto-generated method stub
        
    }

}
