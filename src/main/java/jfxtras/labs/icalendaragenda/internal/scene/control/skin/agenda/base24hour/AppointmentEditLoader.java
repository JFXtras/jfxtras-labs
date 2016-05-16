package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour;

import java.io.IOException;
import java.util.Collection;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.controller.AppointmentEditController;
import jfxtras.labs.icalendaragenda.scene.control.agenda.ICalendarAgenda;
import jfxtras.labs.icalendarfx.components.VComponentDisplayable;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.scene.control.agenda.Agenda.AppointmentGroup;

/** Makes new stage for popup window to edit VEvent with Agenda.Appointment instances
 * 
 * @author David Bal
 * @see AppointmentEditController
 */
public class AppointmentEditLoader extends Stage
{
    // CONSTRUCTOR
    public AppointmentEditLoader(
              Appointment appointment // selected instance
            , VComponentDisplayable<?> vComponent
            , ICalendarAgenda agenda
            , Callback<Collection<AppointmentGroup>, Void> appointmentGroupWriteCallback
            , Callback<Collection<VComponentDisplayable<?>>, Void> veventWriteCallback)
    {
        String appointmentTime = AgendaDateTimeUtilities.formatRange(appointment.getStartTemporal(), appointment.getEndTemporal());
//        VEventOld<Appointment,?> vEvent = (VEventOld<Appointment,?>) vComponent;
        setTitle(vComponent.getSummary().getValue() + ": " + appointmentTime);
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
        
        AppointmentEditController appointmentEditController = appointmentMenuLoader.getController();
        Scene scene = new Scene(appointmentPopup);
        scene.getStylesheets().addAll(ICalendarAgenda.ICALENDAR_STYLE_SHEET);
        
        appointmentEditController.setupData(
                appointment
              , vComponent
              , agenda.getDateTimeRange()
              , agenda.appointments()
              , agenda.getVCalendar()
              , agenda.appointmentGroups()
              , veventWriteCallback
              , this);

        setResizable(false);
        setScene(scene);
    }

    
}
 
