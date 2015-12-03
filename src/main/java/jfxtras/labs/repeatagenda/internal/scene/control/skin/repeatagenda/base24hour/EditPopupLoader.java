package jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour.controller.AppointmentEditController;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarUtilities.WindowCloseType;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.Settings;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.VEventImpl;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VComponent;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.scene.control.agenda.Agenda.AppointmentGroup;
import jfxtras.scene.control.agenda.Agenda.LocalDateTimeRange;

/** Makes new stage for popup window to edit VEvent and Agenda.Appointment
 * 
 * @author David Bal
 * @see AppointmentEditController
 */
public class EditPopupLoader extends Stage {

    private BooleanProperty groupNameEdited = new SimpleBooleanProperty(false);
    private ObjectProperty<WindowCloseType> popupCloseType = new SimpleObjectProperty<WindowCloseType>(WindowCloseType.X); // default to X, meaning click on X to close window)

    // CONSTRUCTOR
    public EditPopupLoader(
              Appointment appointment // selected instance
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
        appointmentMenuLoader.setLocation(RepeatMenuOld.class.getResource("view/AppointmentEdit.fxml"));
        appointmentMenuLoader.setResources(Settings.resources);
        Control appointmentMenu = null;
        try {
            appointmentMenu = appointmentMenuLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        AppointmentEditController appointmentEditController = appointmentMenuLoader.getController();
        appointmentEditController.setupData(
                appointment
              , (VEventImpl) vevent // TODO - can I find a way to remove this cast?  Can I put a method in VComponent?
              , dateTimeRange
              , appointments
              , repeats
              , appointmentGroups
              , veventWriteCallback
              , popupCloseType);
        Scene scene = new Scene(appointmentMenu);

//        groupNameEdited.bindBidirectional(appointmentEditController.groupNameEditedProperty());

        // listen for close event
        popupCloseType.addListener((observable, oldSelection, newSelection) -> close());
        
        // when popup closes write changes if occurred
        setOnHidden((windowEvent) -> 
        {
//            appointmentEditController.getRepeatableController().removeRepeatBindings();
            switch (popupCloseType.get())
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
 
