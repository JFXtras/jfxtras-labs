package jfxtras.labs.icalendaragenda;

import org.junit.Test;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseButton;
import jfxtras.test.AssertNode;
import jfxtras.test.TestUtil;

public class AgendaSelectedOneAppointmentPopupTest extends AgendaTestAbstract
{
    @Override
    public Parent getRootNode()
    {
        return super.getRootNode();
    }
        
    @Test
    public void canProduceSelectedOneAppointmentPopup()
    {
        TestUtil.runThenWaitForPaintPulse( () -> agenda.getVCalendar().getVEvents().add(ICalendarComponents.getDaily1()));

        // Open popup
        move("#hourLine11");
        press(MouseButton.PRIMARY);
        release(MouseButton.PRIMARY);
        
        Node n = find("#selectedOneAppointmentAnchorPane");
        //AssertNode.generateSource("n", n, null, false, jfxtras.test.AssertNode.A.XYWH);
        new AssertNode(n).assertXYWH(0.0, 0.0, 336.0, 93.0, 0.01);
    }
}
