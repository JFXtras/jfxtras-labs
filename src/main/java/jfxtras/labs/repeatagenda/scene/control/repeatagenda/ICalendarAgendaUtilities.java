package jfxtras.labs.repeatagenda.scene.control.repeatagenda;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ButtonBar;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.RRule;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.Agenda.AppointmentGroup;

public final class ICalendarAgendaUtilities
{
    private ICalendarAgendaUtilities() {}

    final public static List<AppointmentGroup> DEFAULT_APPOINTMENT_GROUPS
    = IntStream.range(0, 24)
               .mapToObj(i -> new Agenda.AppointmentGroupImpl()
                     .withStyleClass("group" + i)
                     .withDescription("group" + (i < 10 ? "0" : "") + i))
               .collect(Collectors.toList());
        
    /**
     * Options available when editing or deleting a repeatable appointment.
     * Sometimes all options are not available.  For example, a one-part repeating
     * event doesn't have the SEGMENT option.
     */
    public enum ChangeDialogOption
    {
        ONE                  // individual instance
      , ALL                  // entire series
      , THIS_AND_FUTURE      // same as THIS_AND_FUTURE_ALL, but has a shorter text.  It is used when THIS_AND_FUTURE_SEGMENT does not appear
      , CANCEL;             // do nothing
                
        @Override
        public String toString() {
            return Settings.REPEAT_CHANGE_CHOICES.get(this);
        }
    }
    
//    @Deprecated
//    public enum WindowCloseType
//    {
//        X, CANCEL, CLOSE_WITH_CHANGE, CLOSE_WITHOUT_CHANGE
//    }

    public enum RRuleType
    {
        INDIVIDUAL
      , WITH_EXISTING_REPEAT
      , WITH_NEW_REPEAT
      , HAD_REPEAT_BECOMING_INDIVIDUAL
    }
    
    public static RRuleType getRRuleType(RRule rruleNew, RRule rruleOld)
    {
        if (rruleNew == null)
        {
            if (rruleOld == null)
            { // doesn't have repeat or have old repeat either
                return RRuleType.INDIVIDUAL;
            } else {
                return RRuleType.HAD_REPEAT_BECOMING_INDIVIDUAL;
            }
        } else
        { // RRule != null
            if (rruleOld == null)
            {
                return RRuleType.WITH_NEW_REPEAT;                
            } else
            {
                return RRuleType.WITH_EXISTING_REPEAT;
            }
        }
    }
    
    
    public static List<Node> getMatchingNodes(Parent root, Class<? extends Node> matchClass)
    {
        List<Node> nodes = new ArrayList<>();
        addMatchingDescendents(root, nodes, matchClass);
        return nodes;
    }

    private static void addMatchingDescendents(Parent parent, List<Node> nodes, Class<? extends Node> matchClass)
    {
        if (parent instanceof ButtonBar)
        {
            for (Node node : ((ButtonBar) parent).getButtons())
            {
                if (node.getClass().equals(matchClass)) nodes.add(node);
                if (node instanceof Parent) addMatchingDescendents((Parent)node, nodes, matchClass);
            }
        } else
        {
            for (Node node : parent.getChildrenUnmodifiable())
            {
//                System.out.println(node.getClass().getSimpleName() + " " + matchClass);
                if (node.getClass().equals(matchClass)) nodes.add(node);
                if (node instanceof Parent) addMatchingDescendents((Parent)node, nodes, matchClass);
            }
        }
    }
    
    public static List<Node> getAllNodes(Parent root)
    {
        List<Node> nodes = new ArrayList<>();
        addAllDescendents(root, nodes);
        return nodes;
    }
    
    private static void addAllDescendents(Parent parent, List<Node> nodes)
    {
        if (parent instanceof ButtonBar)
        {
            for (Node node : ((ButtonBar) parent).getButtons())
            {
                nodes.add(node);
                if (node instanceof Parent) addAllDescendents((Parent)node, nodes);
            }
        } else
        {
            for (Node node : parent.getChildrenUnmodifiable())
            {
                nodes.add(node);
                if (node instanceof Parent) addAllDescendents((Parent)node, nodes);
            }
        }
    }


}
