package jfxtras.labs.repeatagenda;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseButton;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarAgenda;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.test.AssertNode;
import jfxtras.test.TestUtil;

public class ICalendarAgendaRenderTest extends ICalendarTestAbstract
{
    public Parent getRootNode()
    {
        return super.getRootNode();
    }
    
    @Test
    public void renderRegularAppointment()
    {
        TestUtil.runThenWaitForPaintPulse( () -> {
            agenda.appointments().add( new ICalendarAgenda.AppointmentImplLocal2()
                .withStartLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2014-01-01T10:00"))
                .withEndLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2014-01-01T12:00"))
                .withAppointmentGroup(DEFAULT_APPOINTMENT_GROUPS.get(0))
            );
        });
                
        Node n = (Node)find("#AppointmentRegularBodyPane2014-01-01/0");

//      AssertNode.generateSource("n", n, null, false, jfxtras.test.AssertNode.A.XYWH);
        String os = System.getProperty("os.name");
        if (os.equals("Linux"))
        {
            new AssertNode(n).assertXYWH(0.5, 402.5, 124.0, 81.0, 0.01);
        } else
        {
            new AssertNode(n).assertXYWH(0.5, 419.5, 125.0, 84.0, 0.01);
        }
    }
    
    @Test
    public void createRepeatableAppointmentByDragging()
    {
        Assert.assertEquals(0, agenda.appointments().size());
        move("#hourLine10");
        press(MouseButton.PRIMARY);
        move("#hourLine12");
        release(MouseButton.PRIMARY);
        
        Assert.assertEquals(1, agenda.vComponents().size());

        move("#hourLine11");
        press(MouseButton.SECONDARY);
        release(MouseButton.SECONDARY);

//        Stage stage2 = GuiTest.findStageByTitle("editPopup");
//        Node popup = find(".editPopup");
//        ArrayList<Node> n = getAllNodes(getRootNode());
//        System.out.println(n.size() + " " + n);
//        System.exit(0);

        Assert.assertEquals("2014-01-01T10:00", agenda.appointments().get(0).getStartLocalDateTime().toString() );
        Assert.assertEquals("2014-01-01T12:00", agenda.appointments().get(0).getEndLocalDateTime().toString() );
        
        find("#AppointmentRegularBodyPane2014-01-01/0"); // validate that the pane has the expected id
        
        // type value
        click("#repeatableTab");
//        CheckBox box = (CheckBox) find(".CalendarTextField .text-field");
        click("#repeatableCheckBox");
        click("#mondayCheckBox");
        click("#fridayCheckBox");
        
        click("#closeRepeatButton"); // change focus
        
        Assert.assertEquals(1, agenda.appointments().size());
        Appointment a = agenda.appointments().get(0);
        assertEquals ("edited summary", a.getSummary());
        TestUtil.sleep(3000);
    }
    
    public static ArrayList<Node> getAllNodes(Parent root) {
        ArrayList<Node> nodes = new ArrayList<Node>();
        addAllDescendents(root, nodes);
        return nodes;
    }

    private static void addAllDescendents(Parent parent, ArrayList<Node> nodes) {
        for (Node node : parent.getChildrenUnmodifiable()) {
            nodes.add(node);
            if (node instanceof Parent)
                addAllDescendents((Parent)node, nodes);
        }
    }

}
