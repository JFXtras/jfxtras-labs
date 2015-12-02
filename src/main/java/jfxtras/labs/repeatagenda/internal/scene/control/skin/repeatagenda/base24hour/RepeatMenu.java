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
import jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour.controller.AppointmentEditControllerOld;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.Repeat;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableAgenda.RepeatableAppointment;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.Settings;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.scene.control.agenda.Agenda.AppointmentGroup;
import jfxtras.scene.control.agenda.Agenda.LocalDateTimeRange;

/** New stage for popup window */
public class RepeatMenu extends Stage {

    private BooleanProperty groupNameEdited = new SimpleBooleanProperty(false);
//    private BooleanProperty appointmentEdited = new SimpleBooleanProperty(false);
//    public BooleanProperty appointmentEditedProperty() { return appointmentEdited; }
//    private Iterator<Appointment> editedAppointments; // TODO
//    private BooleanProperty repeatEdited = new SimpleBooleanProperty(false);
//    public BooleanProperty repeatEditedProperty() { return repeatEdited; }

    public RepeatMenu(Appointment appointment
            , LocalDateTimeRange dateTimeRange
            , Collection<Appointment> appointments
            , Collection<Repeat> repeats
//            , Map<Appointment, Repeat> repeatMap
            , List<AppointmentGroup> appointmentGroups
            , Class<? extends RepeatableAppointment> appointmentClass
            , Class<? extends Repeat> repeatClass
            , Callback<Collection<Appointment>, Void> appointmentWriteCallback
            , Callback<Collection<AppointmentGroup>, Void> appointmentGroupWriteCallback
            , Callback<Collection<Repeat>, Void> repeatWriteCallback
            , Callback<Void, Void> refreshCallback)
    {
        String start = Settings.DATE_FORMAT_AGENDA_START.format(appointment.getStartLocalDateTime());
        String end = Settings.DATE_FORMAT_AGENDA_END.format(appointment.getEndLocalDateTime());
        String appointmentTime = start + end + " ";
        setTitle(appointment.getSummary() + ": " + appointmentTime);
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
        AppointmentEditControllerOld appointmentEditController = appointmentMenuLoader.getController();
        appointmentEditController.setupData(
                appointment
              , dateTimeRange
              , appointments
              , repeats
//              , repeatMap
              , appointmentGroups
              , appointmentClass
              , repeatClass
              , appointmentWriteCallback
//              , appointmentGroupWriteCallback
              , repeatWriteCallback
              , refreshCallback);
        Scene scene = new Scene(appointmentMenu);

        // data element change bindings
        groupNameEdited.bindBidirectional(appointmentEditController.groupNameEditedProperty());
//        appointmentEdited.bindBidirectional(appointmentEditController.appointmentEditedProperty());
//        repeatEdited.bindBidirectional(appointmentEditController.repeatEditedProperty());

        // listen for close event
        appointmentEditController.closeTypeProperty().addListener((observable, oldSelection, newSelection) -> close());

        // when popup closes write changes if occurred
        setOnHidden((windowEvent) -> 
        {
            appointmentEditController.getRepeatableController().removeRepeatBindings();
            switch (appointmentEditController.getCloseType())
            {
            case CLOSE_WITH_CHANGE:
                if (groupNameEdited.getValue()) {    // write group name changes
                    System.out.println("group change write needed");
                    appointmentGroupWriteCallback.call(appointmentGroups);
//                    AppointmentIO.writeAppointmentGroups(appointmentGroups, Settings.APPOINTMENT_GROUPS_FILE);
                }
                break;
            default:
                break;
            }
        });
        
        setScene(scene);
    }
    
}
 
