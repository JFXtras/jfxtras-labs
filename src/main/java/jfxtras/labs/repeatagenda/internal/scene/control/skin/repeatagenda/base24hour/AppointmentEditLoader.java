package jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour;

import java.io.IOException;
import java.util.Collection;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import jfxtras.labs.icalendar.DateTimeType;
import jfxtras.labs.icalendar.VComponent;
import jfxtras.labs.icalendar.VEvent;
import jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour.controller.AppointmentEditController;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarAgenda;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.scene.control.agenda.Agenda.AppointmentGroup;

/** Makes new stage for popup window to edit VEvent with Agenda.Appointment instances
 * 
 * @author David Bal
 * @see AppointmentEditController
 */
public class AppointmentEditLoader extends Stage {

    private BooleanProperty groupNameEdited = new SimpleBooleanProperty(false);
//    private ObjectProperty<WindowCloseType> popupCloseType = new SimpleObjectProperty<WindowCloseType>(WindowCloseType.X); // default to X, meaning click on X to close window)

    // CONSTRUCTOR
    public AppointmentEditLoader(
              Appointment appointment // selected instance
            , VComponent<Appointment> vComponent
            , ICalendarAgenda agenda
            , Callback<Collection<AppointmentGroup>, Void> appointmentGroupWriteCallback
            , Callback<Collection<VComponent<Appointment>>, Void> veventWriteCallback
            , Callback<Void, Void> refreshCallback)
    {
        // TODO - FORMAT DIFFERENTLY FOR DIFFERENT DATE-TIME TYPES
        // NEED TO ACCOMIDATE RANGES THAT SPAN DAYS DIFFERENTLY THAN ONE DAY
        // MAYBE STATIC IS NEEDED
//        String appointmentTime = dateTimeType.format(appointment.getStartTemporal(), appointment.getEndTemporal());
//        String start = Settings.DATE_TIME_FORMAT_START.format(appointment.getStartTemporal());
//        String end = Settings.DATE_FORMAT_AGENDA_END.format(appointment.getEndTemporal());
        String appointmentTime = DateTimeType.formatRange(appointment.getStartTemporal(), appointment.getEndTemporal());
//        String appointmentTime = start + end + " ";
        VEvent<Appointment,?> vEvent = (VEvent<Appointment,?>) vComponent;
        setTitle(vEvent.getSummary() + ": " + appointmentTime);
        initModality(Modality.APPLICATION_MODAL);
        
        // LOAD FXML
        FXMLLoader appointmentMenuLoader = new FXMLLoader();
        appointmentMenuLoader.setLocation(AppointmentEditLoader.class.getResource("view/AppointmentEdit.fxml"));
        appointmentMenuLoader.setResources(Settings.resources);
        Control appointmentPopup = null;
        try {
            appointmentPopup = appointmentMenuLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        appointmentPopup.setId("editAppointmentPopup");
        AppointmentEditController appointmentEditController = appointmentMenuLoader.getController();
        Scene scene = new Scene(appointmentPopup);
        scene.getStylesheets().addAll(ICalendarAgenda.iCalendarStyleSheet);
        
        appointmentEditController.setupData(
                appointment
              , vComponent
              , agenda.getDateTimeRange()
              , agenda.appointments()
              , agenda.vComponents()
              , agenda.appointmentGroups()
              , veventWriteCallback
              , this);

        setResizable(false);
        setScene(scene);
    }

    
}
 
