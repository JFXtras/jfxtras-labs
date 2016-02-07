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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour.EditChoiceDialog;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.ExDate;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VComponent;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VEvent;
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
     * This alert inquires how to apply changes (one, all or this-and-future)
     * Can provide a custom choiceList, or omit the list and use the default choices.
     * 
     * @param resources
     * @param choiceList
     * @return
     */
//    public static ChangeDialogOption repeatChangeDialog(ChangeDialogOption...choiceList)
    // TODO - MOVE THIS INTO A NEW CLASS EXTENDING DIALOG
    @Deprecated
    private static ChangeDialogOption changeDialog(
              String title
            , String content
            , String header
            , Map<ChangeDialogOption, String> choices)
    {       
        List<ChangeDialogOption> choiceList = new ArrayList<>(choices.keySet());
               
        ChoiceDialog<ChangeDialogOption> dialog = new ChoiceDialog<>(null, choiceList);
        dialog.setTitle(title);
        dialog.setContentText(content);
        dialog.setHeaderText(header);
//        dialog.getDialogPane()
        // set id for testing
        dialog.getDialogPane().setId("edit_dialog");
        List<Node> buttons = getMatchingNodes(dialog.getDialogPane(), Button.class);
        ((Button) buttons.get(0)).setId("edit_dialog_button_ok");
        ((Button) buttons.get(1)).setId("edit_dialog_button_cancel");
        ComboBox<ChangeDialogOption> comboBox = (ComboBox<ChangeDialogOption>) getMatchingNodes(dialog.getDialogPane(), ComboBox.class).get(0);
        getMatchingNodes(dialog.getDialogPane(), ComboBox.class).get(0).setId("editComboBox");
        comboBox.setId("edit_dialog_combobox");

        dialog.getDialogPane().getChildrenUnmodifiable().stream().forEach(System.out::println);
        
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
    @Deprecated
    public static ChangeDialogOption editChangeDialog(Map<ChangeDialogOption, String> choices)
    {
        ResourceBundle resources = Settings.resources;
        return changeDialog(resources.getString("dialog.edit.title")
                , resources.getString("dialog.edit.content")
                , resources.getString("dialog.edit.header")
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
    @Deprecated
    public static ChangeDialogOption deleteChangeDialog(Map<ChangeDialogOption, String> choices)
    {
        ResourceBundle resources = Settings.resources;
        return changeDialog(resources.getString("dialog.delete.title")
                , resources.getString("dialog.delete.content")
                , resources.getString("dialog.delete.header")
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
    public static <U> void handleEditVComponents(
            VEvent<U> vEvent
          , VEvent<U> vEventOriginal
          , Collection<VComponent<U>> vComponents
          , Temporal startOriginalInstance
          , Temporal startInstance
          , Temporal endInstance
          , Collection<U> instances)
    {
        final RRuleType rruleType = ICalendarAgendaUtilities.getRRuleType(vEvent.getRRule(), vEventOriginal.getRRule());
        boolean incrementSequence = true;
//        System.out.println("DTEND:" + vEvent.getDateTimeStart() + " " + vEvent.getDateTimeEnd());
//        System.out.println("dates: " + startOriginalInstance + " " + startInstance + " " + endInstance);
//        System.out.println("range: " + vEvent.getStartRange() + " " + vEvent.getEndRange());
        System.out.println("rrule: " + rruleType);
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
                        choices.put(ChangeDialogOption.THIS_AND_FUTURE, future);
                    }
                    String all = VComponent.rangeToString(vEvent);
                    choices.put(ChangeDialogOption.ALL, all);
                }
                EditChoiceDialog dialog = new EditChoiceDialog(choices, Settings.resources);                
                Optional<ChangeDialogOption> result = dialog.showAndWait();
                ChangeDialogOption changeResponse = (result.isPresent()) ? result.get() : ChangeDialogOption.CANCEL;
//                ChangeDialogOption changeResponse = ICalendarAgendaUtilities.editChangeDialog(choices);
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
                                        ExDate newEXDate = v.getExDate().getClass().newInstance();
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
                                    long shift = ChronoUnit.DAYS.between(vEvent.getDateTimeStart(), startNew);
                                    endNew = vEvent.getDateTimeEnd().plus(shift, ChronoUnit.DAYS);
                                } else throw new DateTimeException("Illegal Temporal type.  Only LocalDate and LocalDateTime are supported)");
                                vEvent.setDateTimeStart(startNew);
                                vEvent.setDateTimeEnd(endNew);
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
    
//    /**
//     * @param <U>
//     * 
//     */
//    public static <U> void handleDelete(
//            VEvent<U> vEvent
//          , Collection<VComponent<U>> vComponents
//          , Temporal startOriginalInstance
//          , Temporal startInstance
//          , U instance
//          , Collection<U> instances
//)
//    {
//        int count = vEvent.instances().size();
//        if (count == 1)
//        {
//            vComponents.remove(vEvent);
//            instances.remove(instance);
//        } else // more than one instance
//        {
//            Map<ChangeDialogOption, String> choices = new LinkedHashMap<>();
//            String one = VComponent.temporalToStringPretty(startInstance);
//            choices.put(ChangeDialogOption.ONE, one);
//            if (! vEvent.isIndividual())
//            {
//                {
//                    String future = VComponent.rangeToString(vEvent, startInstance);
//                    choices.put(ChangeDialogOption.THIS_AND_FUTURE, future);
//                }
//                String all = VComponent.rangeToString(vEvent);
//                choices.put(ChangeDialogOption.ALL, all);
//            }
//            DeleteChoiceDialog dialog = new DeleteChoiceDialog(choices, Settings.resources);        
//            Optional<ChangeDialogOption> result = dialog.showAndWait();
//            ChangeDialogOption changeResponse = (result.isPresent()) ? result.get() : ChangeDialogOption.CANCEL;
//            System.out.println("changeResponse:" + changeResponse + " " + result.isPresent());
////            ChangeDialogOption changeResponse = ICalendarAgendaUtilities.deleteChangeDialog(choices);
//            switch (changeResponse)
//            {
//            case ALL:
////                String found = (count > 1) ? Integer.toString(count) : "infinite";
////                if (ICalendarUtilities.confirmDelete(found))
////                {
////                List<VComponent<Appointment>> relatedVComponents = VComponent.findRelatedVComponents(vComponents, vEvent);
//                List<VComponent<U>> relatedVComponents = new ArrayList<>();
//                if (vEvent.getDateTimeRecurrence() == null)
//                { // is parent
//                    relatedVComponents.addAll((Collection<? extends VComponent<U>>) vEvent.getRRule().recurrences());
//                    relatedVComponents.add(vEvent);
//                } else
//                { // is child (recurrence).  Find parent delete all children
//                    relatedVComponents.addAll((Collection<? extends VComponent<U>>) vEvent.getParent().getRRule().recurrences());
//                    relatedVComponents.add(vEvent.getParent());
//                }
//                System.out.println("removing:");
//                relatedVComponents.stream().forEach(v -> vComponents.remove(v));
//                vComponents.removeAll(relatedVComponents);
//                System.out.println("removed:");
//                List<U> appointmentsToRemove = relatedVComponents.stream()
//                        .flatMap(v -> v.instances().stream())
//                        .collect(Collectors.toList());
//                instances.removeAll(appointmentsToRemove);
////                }
//                break;
//            case CANCEL:
//                break;
//            case ONE:
//                if (vEvent.getExDate() == null) vEvent.setExDate(new ExDate(startOriginalInstance));
//                else vEvent.getExDate().getTemporals().add(startOriginalInstance);
//                instances.removeIf(a -> a.equals(instance));
//                break;
////            case SEGMENT:
////                System.out.println("delete segment");
////                break;
//            case THIS_AND_FUTURE:
//                if (vEvent.getRRule().getCount() == 0) vEvent.getRRule().setCount(0);
//                Temporal previousDay = startOriginalInstance.minus(1, ChronoUnit.DAYS);
//                Temporal untilNew = (vEvent.isWholeDay()) ? LocalDate.from(previousDay).atTime(23, 59, 59) : previousDay; // use last second of previous day, like Yahoo
//                vEvent.getRRule().setUntil(untilNew);
//                // TODO - am i deleteing instances?
//                // Remove old appointments, add back ones
//                Collection<U> instancesTemp = new ArrayList<>(); // use temp array to avoid unnecessary firing of Agenda change listener attached to appointments
//                instancesTemp.addAll(instances);
//                instancesTemp.removeIf(a -> vEvent.instances().stream().anyMatch(a2 -> a2 == a));
//                vEvent.instances().clear(); // clear vEvent's outdated collection of appointments
//                instancesTemp.addAll(vEvent.makeInstances()); // add vEventOld part of new appointments
//                instances.clear();
//                instances.addAll(instancesTemp);
//
//                System.out.println("until:" + untilNew);
//                break;
//            default:
//                break;
//            }
//        }
//    }
    
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
        ONE                  // individual instance
      , ALL                  // entire series
      , THIS_AND_FUTURE      // same as THIS_AND_FUTURE_ALL, but has a shorter text.  It is used when THIS_AND_FUTURE_SEGMENT does not appear
      , CANCEL;             // do nothing
                
        @Override
        public String toString() {
            return Settings.REPEAT_CHANGE_CHOICES.get(this);
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
//                System.out.println(node.getClass().getSimpleName() + " " + matchClass);
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
