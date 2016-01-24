package jfxtras.labs.repeatagenda.scene.control.repeatagenda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.RRule;

public final class ICalendarUtilities
{
    private ICalendarUtilities() {}

    /**
     * This alert inquires how to apply changes (one, all or this-and-future)
     * Can provide a custom choiceList, or omit the list and use the default choices.
     * 
     * @param resources
     * @param choiceList
     * @return
     */
//    public static ChangeDialogOption repeatChangeDialog(ChangeDialogOption...choiceList)
    public static ChangeDialogOption repeatChangeDialog(Map<ChangeDialogOption, String> choices)
    {
        ResourceBundle resources = Settings.resources;
        List<ChangeDialogOption> choiceList = (choices == null) ? 
                new ArrayList<>(Arrays.asList(ChangeDialogOption.threeOptions())) // default choices
              : new ArrayList<>(choices.keySet());
               
        ChoiceDialog<ChangeDialogOption> dialog = new ChoiceDialog<>(null, choiceList);
        
        // set id for testing
        dialog.getDialogPane().setId("edit_dialog");
        List<Node> buttons = getAllNodes(dialog.getDialogPane(), Button.class);
        ((Button) buttons.get(0)).setId("edit_dialog_button_ok");
        ((Button) buttons.get(1)).setId("edit_dialog_button_cancel");
        ComboBox<ChangeDialogOption> comboBox = (ComboBox<ChangeDialogOption>) getAllNodes(dialog.getDialogPane(), ComboBox.class).get(0);
        comboBox.setId("edit_dialog_combobox");

        dialog.setTitle(resources.getString("dialog.repeat.change.title"));
        dialog.setContentText(resources.getString("dialog.repeat.change.content"));
        dialog.setHeaderText(resources.getString("dialog.repeat.change.header"));
//        comboBox.setConverter(new StringConverter<ChangeDialogOption>()
//        {
//            @Override public String toString(ChangeDialogOption change)
//            {
//                return change.getText() + System.lineSeparator() + choices.get(change);
////                return choices.get(change);
//            }
//            @Override public ChangeDialogOption fromString(String string) {
//                throw new RuntimeException("not required for non editable ComboBox");
//            }
//        });
        comboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> 
        {
            dialog.setHeaderText(newSelection + ":" + System.lineSeparator() + choices.get(newSelection));
//            dialog.setHeaderText(newSelection.getText());
//            dialog.setHeaderText(choices.get(newSelection));
        });
        dialog.setHeaderText(dialog.getSelectedItem() + ":" + System.lineSeparator() + choices.get(dialog.getSelectedItem())); // initial text
//        dialog.setHeaderText(dialog.getSelectedItem().getText()); // initial text
//        dialog.setHeaderText(choices.get(dialog.getSelectedItem())); // initial text

        Optional<ChangeDialogOption> result = dialog.showAndWait();
        
        return (result.isPresent()) ? result.get() : ChangeDialogOption.CANCEL;
    }
    
    /**
     * Alert to confirm delete appointments
     * 
     * @param resources
     * @param appointmentQuantity
     * @return
     */
    public static Boolean confirmDelete(String appointmentQuantity)
    {
        ResourceBundle resources = Settings.resources;
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(resources.getString("alert.repeat.delete.title"));
        alert.setContentText(resources.getString("alert.repeat.delete.content"));
        alert.setHeaderText(appointmentQuantity + " " + resources.getString("alert.repeat.delete.header"));

        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }
    
//    /**
//     * Removes an element from a collection.
//     * Similar to removeIf, but quits when one remove occurs
//     * 
//     * @param collection
//     * @param element
//     * @return
//     */
//    public static <T> boolean removeFirst(Collection<T> collection, T element) {
//        Iterator<T> i = collection.iterator();
//        while (i.hasNext()) {
//            T a = i.next();
//            if (a == element) {
//                i.remove();
//                return true;
//            }
//        }
//        return false;
//    }
    
    /**
     * Options available when editing or deleting a repeatable appointment.
     * Sometimes all options are not available.  For example, a one-part repeating
     * event doesn't have the SEGMENT option.
     */
    public enum ChangeDialogOption
    {
        ONE("Individual event:")                 // individual instance
//      , SEGMENT("Segment of events:")             // one part of a multi-part series
      , ALL("Whole series:")                 // entire series
      , THIS_AND_FUTURE("This and future events") // same as THIS_AND_FUTURE_ALL, but has a shorter text.  It is used when THIS_AND_FUTURE_SEGMENT does not appear
//      , THIS_AND_FUTURE_SEGMENT("This event and future events in this segment:")     // all instances from this time forward
//      , THIS_AND_FUTURE_ALL("This event and future events in whole series:")     // all instances from this time forward
      , CANCEL("Cancel and do nothing");             // do nothing
// TODO - REPLACE WITH BUNDLE TEXTS
        private String text;
        
        ChangeDialogOption(String s) { text =  s; }
        public String getText() { return text; }
        
        @Override
        public String toString() {
            return Settings.REPEAT_CHANGE_CHOICES.get(this);
        }
        
        /** for one-part repeatable events */
        private static ChangeDialogOption[] threeOptions()
        {
            return new ChangeDialogOption[] {
                    ONE
                  , ALL
                  , THIS_AND_FUTURE
            };
        }
        
//        public String toStringSingular()
//        {
//            switch (this)
//            {
//            case ONE:
//                return Settings.REPEAT_CHANGE_CHOICES.get(ONE);
//            case SEGMENT:
//                return Settings.REPEAT_CHANGE_CHOICES.get(SEGMENT);
//            case ALL:
//                return Settings.REPEAT_CHANGE_CHOICES.get(ALL);
//            case THIS_AND_FUTURE_SEGMENT:
//                return Settings.REPEAT_CHANGE_CHOICES.get(THIS_AND_FUTURE_SEGMENT);
//            case THIS_AND_FUTURE_ALL:
//                return Settings.REPEAT_CHANGE_CHOICES.get(THIS_AND_FUTURE_ALL);
//            default:
//                return null;                
//            }
//        }
//        
//        /** For multi-part repeatable events */
//        private static ChangeDialogOption[] fourOptions()
//        {
//            return new ChangeDialogOption[] {
//                    ONE
//                  , SEGMENT
//                  , ALL
//                  , THIS_AND_FUTURE
//            };
//        }
//        
//        public static ChangeDialogOption[] selectChoices(int choices)
//        {
//            if (choices == 3) return threeOptions();
//            if (choices == 4) return fourOptions();
//            return null;
//        }

        public static void ONE(String one2) {
            // TODO Auto-generated method stub
            
        }
    }
    
    @Deprecated
    public enum WindowCloseType
    {
        X, CANCEL, CLOSE_WITH_CHANGE, CLOSE_WITHOUT_CHANGE
    }

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
    
    public static List<Node> getAllNodes(Parent root, Class<? extends Node> matchClass)
    {
        List<Node> nodes = new ArrayList<>();
        addAllDescendents(root, nodes, matchClass);
        return nodes;
    }

    private static void addAllDescendents(Parent parent, List<Node> nodes, Class<? extends Node> matchClass)
    {
        if (parent instanceof ButtonBar)
        {
            for (Node node : ((ButtonBar) parent).getButtons())
            {
//                System.out.println(node.getClass().getSimpleName() + " " + matchClass);
                if (node.getClass().equals(matchClass)) nodes.add(node);
                if (node instanceof Parent) addAllDescendents((Parent)node, nodes, matchClass);
            }
        } else
        {
            for (Node node : parent.getChildrenUnmodifiable())
            {
//                System.out.println(node.getClass().getSimpleName() + " " + matchClass);
                if (node.getClass().equals(matchClass)) nodes.add(node);
                if (node instanceof Parent) addAllDescendents((Parent)node, nodes, matchClass);
            }
        }
    }

}
