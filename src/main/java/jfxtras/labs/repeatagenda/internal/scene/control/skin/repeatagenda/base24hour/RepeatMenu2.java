package jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour.controller.AppointmentEditController3;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.Settings;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VComponent;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VEvent;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.scene.control.agenda.Agenda.AppointmentGroup;
import jfxtras.scene.control.agenda.Agenda.LocalDateTimeRange;

/** Makes new stage for popup window to edit VEvent and Agenda.Appointment
 * 
 * @author David Bal
 * @see AppointmentEditController3
 */
public class RepeatMenu2 extends Stage {

    private BooleanProperty groupNameEdited = new SimpleBooleanProperty(false);

    // CONSTRUCTOR
    public RepeatMenu2(
              Appointment appointment
            , VComponent vevent
            , LocalDateTimeRange dateTimeRange
            , Collection<Appointment> appointments
            , Collection<VComponent> repeats
            , List<AppointmentGroup> appointmentGroups
            , Callback<Collection<AppointmentGroup>, Void> appointmentGroupWriteCallback
            , Callback<Collection<VComponent>, Void> veventWriteCallback
            , Callback<Void, Void> refreshCallback)
    {
        String start = Settings.DATE_FORMAT_AGENDA_START.format(appointment.getStartLocalDateTime());
        String end = Settings.DATE_FORMAT_AGENDA_END.format(appointment.getEndLocalDateTime());
        String appointmentTime = start + end + " ";
//        setTitle(vevent.getSummary() + ": " + appointmentTime);
        initModality(Modality.APPLICATION_MODAL);
        
//        // LOAD FXML
        FXMLLoader appointmentMenuLoader = new FXMLLoader();
        appointmentMenuLoader.setLocation(RepeatMenu.class.getResource("view/AppointmentEdit.fxml"));
        appointmentMenuLoader.setResources(Settings.resources);
        Control appointmentMenu = null;
        try {
            appointmentMenu = appointmentMenuLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        AppointmentEditController3 appointmentEditController = appointmentMenuLoader.getController();
        appointmentEditController.setupData(
                (VEvent) vevent
              , dateTimeRange
              , appointments
              , repeats
              , appointmentGroups
              , veventWriteCallback
              , refreshCallback);
        Scene scene = new Scene(appointmentMenu);

//        groupNameEdited.bindBidirectional(appointmentEditController.groupNameEditedProperty());

        // when popup closes write changes if occurred
        setOnHidden((windowEvent) -> 
        {
//            appointmentEditController.getRepeatableController().removeRepeatBindings();
//            switch (appointmentEditController.getCloseType())
//            {
//            case CLOSE_WITH_CHANGE:
//                if (groupNameEdited.getValue()) {    // write group name changes
//                    System.out.println("group change write needed");
//                    appointmentGroupWriteCallback.call(appointmentGroups);
////                    AppointmentIO.writeAppointmentGroups(appointmentGroups, Settings.APPOINTMENT_GROUPS_FILE);
//                }
//                break;
//            default:
//                break;
//            }
        });
        
        setScene(scene);
    }

    
}
 
