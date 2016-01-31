package jfxtras.labs.repeatagenda.scene.control.repeatagenda;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
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
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.EXDate;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VComponent;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VEvent;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.RRule;

public final class ICalendarAgendaEditUtilities
{
    private ICalendarAgendaEditUtilities() {}

    /**
     * This alert inquires how to apply changes (one, all or this-and-future)
     * Can provide a custom choiceList, or omit the list and use the default choices.
     * 
     * @param resources
     * @param choiceList
     * @return
     */
//    public static ChangeDialogOption repeatChangeDialog(ChangeDialogOption...choiceList)
    private static ChangeDialogOption changeDialog(
              String title
            , String content
            , String header
            , Map<ChangeDialogOption, String> choices)
    {       
        List<ChangeDialogOption> choiceList = (choices == null) ? 
                new ArrayList<>(Arrays.asList(ChangeDialogOption.threeOptions())) // default choices
              : new ArrayList<>(choices.keySet());
               
        ChoiceDialog<ChangeDialogOption> dialog = new ChoiceDialog<>(null, choiceList);
        dialog.setTitle(title);
        dialog.setContentText(content);
        dialog.setHeaderText(header);
        
        // set id for testing
        dialog.getDialogPane().setId("edit_dialog");
        List<Node> buttons = getAllNodes(dialog.getDialogPane(), Button.class);
        ((Button) buttons.get(0)).setId("edit_dialog_button_ok");
        ((Button) buttons.get(1)).setId("edit_dialog_button_cancel");
        ComboBox<ChangeDialogOption> comboBox = (ComboBox<ChangeDialogOption>) getAllNodes(dialog.getDialogPane(), ComboBox.class).get(0);
        comboBox.setId("edit_dialog_combobox");

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
     * This alert inquires how to apply edit changes (one, all or this-and-future)
     * Can provide a custom choiceList, or omit the list and use the default choices.
     * 
     * @param resources
     * @param choiceList
     * @return
     */
    public static ChangeDialogOption editChangeDialog(Map<ChangeDialogOption, String> choices)
  {
        ResourceBundle resources = Settings.resources;
        return changeDialog(resources.getString("dialog.repeat.change.title")
                , resources.getString("dialog.repeat.change.content")
                , resources.getString("dialog.repeat.change.header")
                , choices);
  }
    
    /**
     * This alert inquires how to apply delete changes (one, all or this-and-future)
     * Can provide a custom choiceList, or omit the list and use the default choices.
     * 
     * @param resources
     * @param choiceList
     * @return
     */
    public static ChangeDialogOption deleteChangeDialog(Map<ChangeDialogOption, String> choices)
  {
        ResourceBundle resources = Settings.resources;
        return changeDialog(resources.getString("dialog.repeat.delete.title")
                , resources.getString("dialog.repeat.delete.content")
                , resources.getString("dialog.repeat.delete.header")
                , choices);
  }
    
    
    /**
     * Saves changes to a VEvent after modifying an instance
     * If the VEvent has a repeat rule a dialog prompting the user to change one, this-and-future or all
     * events in the series.
     * 
     * Note: Doesn't work for a VComponent DTEND is needed, which is not in VComponent.
     * It is possible to provide similar functionality for different VComponents with modifications.
     * 
     * @param vEvent
     * @param vEventOriginal
     * @param vComponents
     * @param startOriginalInstance
     * @param startInstance
     * @param endInstance
     * @param instances
     */
    public static <U> void saveChange(
            VEvent<U> vEvent
          , VEvent<U> vEventOriginal
          , Collection<VComponent<U>> vComponents
          , Temporal startOriginalInstance
          , Temporal startInstance
          , Temporal endInstance
          , Collection<U> instances)
    {
        final RRuleType rruleType = ICalendarAgendaEditUtilities.getRRuleType(vEvent.getRRule(), vEventOriginal.getRRule());
        boolean incrementSequence = true;
        System.out.println("DTEND:" + vEvent.getDateTimeStart() + " " + vEvent.getDateTimeEnd());
        System.out.println("dates: " + startOriginalInstance + " " + startInstance + " " + endInstance);
        System.out.println("range: " + vEvent.getStartRange() + " " + vEvent.getEndRange());
//        System.out.println("rrule: " + rruleType);
//        System.out.println("vEvent: " + vEvent);
        switch (rruleType)
        {
        case HAD_REPEAT_BECOMING_INDIVIDUAL:
            vEvent.setRRule(null);
            vEvent.setRDate(null);
            vEvent.setExDate(null);
            // fall through
        case WITH_NEW_REPEAT: // no dialog
        case INDIVIDUAL:
            if (! vEvent.equals(vEventOriginal)) updateAppointments(vEvent, instances);
            break;
        case WITH_EXISTING_REPEAT:
            if (! vEvent.equals(vEventOriginal)) // if changes occurred
            {
//                List<VComponent<U>> relatedVComponents = VComponent.findRelatedVComponents(vComponents, vEvent); // this version is experimental (edits related vComponents as a group), not currently used
                List<VComponent<U>> relatedVComponents = Arrays.asList(vEvent);
                Map<ChangeDialogOption, String> choices = new LinkedHashMap<>();
                String one = VComponent.temporalToStringPretty(startInstance);
                choices.put(ChangeDialogOption.ONE, one);
                if (! vEvent.isIndividual())
                {
                    {
                        String future = VComponent.rangeToString(relatedVComponents, startInstance);
//                        String future = VComponent.temporalToStringPretty(vEvent.lastStartTemporal());
                        choices.put(ChangeDialogOption.THIS_AND_FUTURE, future);
                    }
                    String all = VComponent.rangeToString(vEvent);
                    choices.put(ChangeDialogOption.ALL, all);
                }
                
                ChangeDialogOption changeResponse = ICalendarAgendaEditUtilities.editChangeDialog(choices);
                switch (changeResponse)
                {
                case ALL:
                    if (relatedVComponents.size() == 1)
                    {
                        updateAppointments(vEvent, instances);
                    } else
                    { // this version is experimental (edits related vComponents as a group), not currently used (there will always be 1 relatedVComponent now)
                        relatedVComponents.stream().forEach(v -> 
                        {
                            // Copy ExDates
                            if (v.getExDate() != null)
                            {
                                if (vEvent.getExDate() == null)
                                { // make new EXDate object for destination if necessary
                                    try {
                                        EXDate newEXDate = v.getExDate().getClass().newInstance();
                                        vEvent.setExDate(newEXDate);
                                    } catch (InstantiationException | IllegalAccessException e) {
                                        e.printStackTrace();
                                    }
                              }
                              v.getExDate().getTemporals().addAll(v.getExDate().getTemporals());
                            }
                            // update start and end dates
                            if (VComponent.isBefore(v.getDateTimeStart(), vEvent.getDateTimeStart()))
                            {
                                final Temporal startNew;
                                final Temporal endNew;
                                if (vEvent.getDateTimeStart() instanceof LocalDateTime)
                                {
                                    LocalTime startTime = LocalTime.from(vEvent.getDateTimeStart());
                                    startNew = LocalDate.from(v.getDateTimeStart()).atTime(startTime);
                                    long shift = ChronoUnit.DAYS.between(vEvent.getDateTimeStart(), startNew);
                                    endNew = vEvent.getDateTimeEnd().plus(shift, ChronoUnit.DAYS);
                                } else if (vEvent.getDateTimeStart() instanceof LocalDate)
                                {
                                    startNew = v.getDateTimeStart();
                                } else throw new DateTimeException("Illegal Temporal type.  Only LocalDate and LocalDateTime are supported)");
                                vEvent.setDateTimeStart(startNew);
                            }
                        });
                    }
                    break;
                case CANCEL:
                    vEventOriginal.copyTo(vEvent); // return to original vEvent
                    incrementSequence = false;
                    break;
                case THIS_AND_FUTURE:
                    editThisAndFuture(vEvent, vEventOriginal, vComponents, startOriginalInstance, startInstance, instances);
                    break;
                case ONE:
                    editOne(vEvent, vEventOriginal, vComponents, startOriginalInstance, startInstance, endInstance, instances);
                    break;
                default:
                    break;
                }
            }
        }
        if (! vEvent.isValid()) throw new RuntimeException(vEvent.makeErrorString());
        if (incrementSequence) vEvent.incrementSequence();
//        vComponents.stream().forEach(System.out::println);
//        System.out.println(vEvent);
    }
    
    private static <U> void updateAppointments(VComponent<U> vEvent, Collection<U> instances)
    {
        Collection<U> instancesTemp = new ArrayList<>(); // use temp array to avoid unnecessary firing of Agenda change listener attached to appointments
        instancesTemp.addAll(instances);
        instancesTemp.removeIf(a -> vEvent.instances().stream().anyMatch(a2 -> a2 == a));
        vEvent.instances().clear(); // clear VEvent's outdated collection of appointments
        instancesTemp.addAll(vEvent.makeInstances()); // make new appointments and add to main collection (added to VEvent's collection in makeAppointments)
        instances.clear();
        instances.addAll(instancesTemp);
    }
    
    /*
     * Edit one instance of a VEvent with a RRule.  The instance becomes a new VEvent without a RRule
     * as with the same UID as the parent and a recurrence-id for the replaced date or date/time.
     */
    private static <U> void editOne(
            VEvent<U> vEvent
          , VEvent<U> vEventOriginal
          , Collection<VComponent<U>> vComponents
          , Temporal startOriginalInstance
          , Temporal startInstance
          , Temporal endInstance
          , Collection<U> instances)
    {
        if (vEvent.isWholeDay())
        {
            LocalDate start = LocalDate.from(startInstance);
            vEvent.setDateTimeStart(start);
            LocalDate end = LocalDate.from(endInstance);
            vEvent.setDateTimeEnd(end);
        } else
        {
            vEvent.setDateTimeStart(startInstance);
            vEvent.setDateTimeEnd(endInstance);
        }
        vEvent.setRRule(null);
        vEvent.setDateTimeRecurrence(startOriginalInstance);
        vEvent.setDateTimeStamp(LocalDateTime.now());
        vEvent.setParent(vEventOriginal);
   
              // Add recurrence to original vEvent
        vEventOriginal.getRRule().recurrences().add(vEvent);

        // Check for validity
        if (! vEvent.isValid()) throw new RuntimeException(vEvent.makeErrorString());
        if (! vEventOriginal.isValid()) throw new RuntimeException(vEventOriginal.makeErrorString());
        
        // Remove old appointments, add back ones
        Collection<U> instancesTemp = new ArrayList<>(); // use temp array to avoid unnecessary firing of Agenda change listener attached to appointments
        instancesTemp.addAll(instances);
        instancesTemp.removeIf(a -> vEventOriginal.instances().stream().anyMatch(a2 -> a2 == a));
        vEventOriginal.instances().clear(); // clear vEventOriginal outdated collection of appointments
        instancesTemp.addAll(vEventOriginal.makeInstances()); // make new appointments and add to main collection (added to vEventNew's collection in makeAppointments)
        vEvent.instances().clear(); // clear vEvent outdated collection of appointments
        instancesTemp.addAll(vEvent.makeInstances()); // add vEventOld part of new appointments
        instances.clear();
        instances.addAll(instancesTemp);
        vComponents.add(vEventOriginal); // TODO - LET LISTENER ADD NEW APPOINTMENTS OR ADD THEM HERE?
    }
    
    /*
     * Changing this and future instances in VComponent is done by ending the previous
     * VComponent with a UNTIL date or date/time and starting a new VComponent from 
     * the selected instance.  EXDATE, RDATE and RECURRENCES are split between both
     * VComponents.  vEventNew has new settings, vEvent has former settings.
     */
    private static <U> void editThisAndFuture(
            VEvent<U> vEvent
          , VEvent<U> vEventOriginal
          , Collection<VComponent<U>> vComponents
          , Temporal startOriginalInstance
          , Temporal startInstance
          , Collection<U> instances)
    {
        // adjust original VEvent
        if (vEventOriginal.getRRule().getCount() != null) vEventOriginal.getRRule().setCount(0);
        Temporal previousDay = startOriginalInstance.minus(1, ChronoUnit.DAYS);
        Temporal untilNew = (vEvent.isWholeDay()) ? LocalDate.from(previousDay).atTime(23, 59, 59) : previousDay; // use last second of previous day, like Yahoo
        vEventOriginal.getRRule().setUntil(untilNew);
        
        // Adjust new VEvent
        long shift = ChronoUnit.DAYS.between(vEvent.getDateTimeStart(), startInstance);
        Temporal endNew = vEvent.getDateTimeEnd().plus(shift, ChronoUnit.DAYS);
        vEvent.setDateTimeEnd(endNew);
        vEvent.setDateTimeStart(startInstance);
        vEvent.setUniqueIdentifier();
        String relatedUID = (vEventOriginal.getRelatedTo() == null) ? vEventOriginal.getUniqueIdentifier() : vEventOriginal.getRelatedTo();
        vEvent.setRelatedTo(relatedUID);
        vEvent.setDateTimeStamp(LocalDateTime.now());
        System.out.println("unti2l:" + vEvent.getRRule().getUntil());
        
        // Split EXDates dates between this and newVEvent
        if (vEvent.getExDate() != null)
        {
            vEvent.getExDate().getTemporals().clear();
            final Iterator<Temporal> exceptionIterator = vEvent.getExDate().getTemporals().iterator();
            while (exceptionIterator.hasNext())
            {
                Temporal d = exceptionIterator.next();
                int result = VComponent.TEMPORAL_COMPARATOR.compare(d, startInstance);
                if (result < 0)
                {
                    exceptionIterator.remove();
                } else {
                    vEvent.getExDate().getTemporals().add(d);
                }
            }
            if (vEvent.getExDate().getTemporals().isEmpty()) vEvent.setExDate(null);
            if (vEvent.getExDate().getTemporals().isEmpty()) vEvent.setExDate(null);
        }

        // Split recurrence date/times between this and newVEvent
        if (vEvent.getRDate() != null)
        {
            vEvent.getRDate().getTemporals().clear();
            final Iterator<Temporal> recurrenceIterator = vEvent.getRDate().getTemporals().iterator();
            while (recurrenceIterator.hasNext())
            {
                Temporal d = recurrenceIterator.next();
                int result = VComponent.TEMPORAL_COMPARATOR.compare(d, startInstance);
                if (result < 0)
                {
                    recurrenceIterator.remove();
                } else {
                    vEvent.getRDate().getTemporals().add(d);
                }
            }
            if (vEvent.getRDate().getTemporals().isEmpty()) vEvent.setRDate(null);
            if (vEvent.getRDate().getTemporals().isEmpty()) vEvent.setRDate(null);
        }

        // Split instance dates between this and newVEvent
        if (vEvent.getRRule().recurrences() != null)
        {
            vEvent.getRRule().recurrences().clear();
            final Iterator<VComponent<?>> recurrenceIterator = vEvent.getRRule().recurrences().iterator();
            while (recurrenceIterator.hasNext())
            {
                VComponent<?> d = recurrenceIterator.next();
                if (VComponent.isBefore(d.getDateTimeRecurrence(), startInstance))
                {
                    recurrenceIterator.remove();
                } else {
                    vEvent.getRRule().recurrences().add(d);
                }
            }
        }
        
        // Modify COUNT for the edited vEvent
        if (vEvent.getRRule().getCount() > 0)
        {
            int countInOrginal = vEventOriginal.makeInstances().size();
            int countInNew = vEvent.getRRule().getCount() - countInOrginal;
//            final int newCount = (int) vEvent.instances()
//                    .stream()
//                    .map(a -> a.getStartLocalDateTime())
//                    .filter(d -> ! VComponent.isBefore(d, startInstance))
//                    .count();
            vEvent.getRRule().setCount(countInNew);
        }
        
        if (! vEventOriginal.isValid()) throw new RuntimeException(vEventOriginal.makeErrorString());
        vComponents.add(vEventOriginal);

        // Remove old appointments, add back ones
        Collection<U> instancesTemp = new ArrayList<>(); // use temp array to avoid unnecessary firing of Agenda change listener attached to appointments
        instancesTemp.addAll(instances);
        instancesTemp.removeIf(a -> vEventOriginal.instances().stream().anyMatch(a2 -> a2 == a));
        vEventOriginal.instances().clear(); // clear vEvent outdated collection of appointments
        instancesTemp.addAll(vEventOriginal.makeInstances()); // make new appointments and add to main collection (added to vEvent's collection in makeAppointments)
        vEvent.instances().clear(); // clear vEvent's outdated collection of appointments
        instancesTemp.addAll(vEvent.makeInstances()); // add vEventOld part of new appointments
        instances.clear();
        instances.addAll(instancesTemp);
        
//        vComponents.stream().forEach(System.out::println);
//        System.out.println("vEvent:" + vEvent);
//        System.out.println("vComponents:" + vComponents.size());
    }
    
    
    /**
     * Alert to confirm delete appointments
     * 
     * @param resources
     * @param appointmentQuantity
     * @return
     */
    @Deprecated
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
