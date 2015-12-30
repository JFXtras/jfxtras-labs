package jfxtras.labs.repeatagenda;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import javafx.scene.Node;
import javafx.scene.Parent;

public class AgendaRenderVComponentsTest extends ICalendarTestAbstract
{
    public Parent getRootNode()
    {
        Parent p = super.getRootNode();
        agenda.vComponents().add(getDaily2());
        agenda.vComponents().add(getWeekly2());
        agenda.vComponents().add(getWholeDayDaily3());
        agenda.vComponents().add(getIndividual1());
        agenda.vComponents().add(getIndividual2());
        return p;
    }
    
    @Test
    public void canRenderVComponents()
    {
        List<LocalDateTime> startDates = agenda.appointments()
                .stream()
                .map(a -> a.getStartLocalDateTime())
                .sorted()
                .collect(Collectors.toList());
        List<LocalDateTime> expectedStartDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 9, 0, 0)
              , LocalDateTime.of(2015, 11, 9, 10, 0)
              , LocalDateTime.of(2015, 11, 9, 18, 0)
              , LocalDateTime.of(2015, 11, 11, 0, 0)
              , LocalDateTime.of(2015, 11, 11, 10, 0)
              , LocalDateTime.of(2015, 11, 12, 0, 0)
              , LocalDateTime.of(2015, 11, 12, 10, 0)
              , LocalDateTime.of(2015, 11, 13, 10, 0)
              , LocalDateTime.of(2015, 11, 15, 0, 0)
                ));
        assertEquals(expectedStartDates, startDates);

        List<LocalDateTime> endDates = agenda.appointments()
                .stream()
                .map(a -> a.getEndLocalDateTime())
                .sorted()
                .collect(Collectors.toList());
        List<LocalDateTime> expectedEndDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 9, 11, 30)
              , LocalDateTime.of(2015, 11, 9, 19, 0)
              , LocalDateTime.of(2015, 11, 11, 0, 0)
              , LocalDateTime.of(2015, 11, 11, 10, 45)
              , LocalDateTime.of(2015, 11, 12, 0, 0)
              , LocalDateTime.of(2015, 11, 12, 11, 30)
              , LocalDateTime.of(2015, 11, 13, 10, 45)
              , LocalDateTime.of(2015, 11, 14, 0, 0)
              , LocalDateTime.of(2015, 11, 17, 0, 0)
                ));
        assertEquals(expectedEndDates, endDates);
//        TestUtil.sleep(3000);
        
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
