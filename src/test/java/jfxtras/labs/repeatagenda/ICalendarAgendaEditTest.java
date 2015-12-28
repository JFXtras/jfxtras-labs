package jfxtras.labs.repeatagenda;

import org.junit.Test;

import javafx.scene.Parent;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.VEventImpl;
import jfxtras.test.TestUtil;

public class ICalendarAgendaEditTest extends ICalendarTestAbstract
{
    public Parent getRootNode()
    {
        Parent p = super.getRootNode();
        
        VEventImpl v = getDaily1();
//        v.setDateTimeRangeStart(LocalDateTime.of(2015, 11, 8, 0, 0));
//        v.setDateTimeRangeEnd(LocalDateTime.of(2015, 11, 15, 0, 0));
        agenda.vComponents().add(v);
        return p;
    }
    
    @Test
    public void canEditDatetime2()
    {
        VEventImpl v = getDaily1();
        System.out.println("start:" + agenda.getDateTimeRange().getStartLocalDateTime());
//        agenda.setDisplayedLocalDateTime(LocalDateTime.of(2015, 11, 8, 0, 0));
//        v.setDateTimeRangeStart(agenda.getDateTimeRange().getStartLocalDateTime());
//        v.setDateTimeRangeEnd(agenda.getDateTimeRange().getEndLocalDateTime());
//        agenda.vComponents().add(v);
        System.out.println(agenda.appointments().size());

//        agenda.appointments().addAll(v.makeInstances());
        TestUtil.sleep(3000);
        
    }

}
