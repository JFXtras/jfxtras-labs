package jfxtras.labs.repeatagenda.scene.control.repeatagenda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

public class ICalendarUtilities
{
//    private static Button okButton;
//    private Button cancelButton;

    /**
     * This alert inquires how to apply changes (one, all or this-and-future)
     * Can provide a custom choiceList, or omit the list and use the default choices.
     * 
     * @param resources
     * @param choiceList
     * @return
     */
    public static ChangeDialogOptions repeatChangeDialog(ChangeDialogOptions...choiceList)
    {
        ResourceBundle resources = Settings.resources;
        List<ChangeDialogOptions> choices;
        if (choiceList == null || choiceList.length == 0)
        { // use default choices
            choices = new ArrayList<>();
            choices.add(ChangeDialogOptions.ONE);
            choices.add(ChangeDialogOptions.ALL);
            choices.add(ChangeDialogOptions.THIS_AND_FUTURE);
        } else { // use inputed choices
            choices = new ArrayList<ChangeDialogOptions>(Arrays.asList(choiceList));
        }
               
        ChoiceDialog<ChangeDialogOptions> dialog = new ChoiceDialog<>(choices.get(0), choices);
        
        // set id for testing
        dialog.getDialogPane().setId("edit_dialog");
        List<Node> buttons = getAllNodes(dialog.getDialogPane(), Button.class);
        Button okButton = (Button) buttons.get(0);
        okButton.setId("edit_dialog_button_ok");
        Button cancelButton = (Button) buttons.get(1);
        cancelButton.setId("edit_dialog_button_cancel");
        Node n = getAllNodes(dialog.getDialogPane(), ComboBox.class).get(0);
        ComboBox<ChangeDialogOptions> c = (ComboBox<ChangeDialogOptions>) n;
        c.setId("edit_dialog_combobox");

        dialog.setTitle(resources.getString("dialog.repeat.change.title"));
        dialog.setContentText(resources.getString("dialog.repeat.change.content"));
        dialog.setHeaderText(resources.getString("dialog.repeat.change.header"));

        Optional<ChangeDialogOptions> result = dialog.showAndWait();
        
        return (result.isPresent()) ? result.get() : ChangeDialogOptions.CANCEL;
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
     * Options available when changing a repeatable appointment
     * ONE: Change only selected appointment
     * ALL: Change all appointments
     * FUTURE: Change this and future appointments
     * @author David Bal
     *
     */
    public enum ChangeDialogOptions
    {
        ONE, ALL, THIS_AND_FUTURE, CANCEL;

        @Override
        public String toString() {
            return Settings.REPEAT_CHANGE_CHOICES.get(this);
        }
    }
    
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
