package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.controller.EditDisplayableComponentController;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.controller.EditVEventController;
import jfxtras.labs.icalendaragenda.scene.control.agenda.ICalendarAgenda;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.scene.control.agenda.Agenda.AppointmentGroup;

/** Makes new stage for popup window to edit VEvent with Agenda.Appointment instances
 * 
 * @author David Bal
 * @see EditDisplayableComponentController
 */
public class EditVEventLoader extends Stage
{
    // CONSTRUCTOR
    public EditVEventLoader(
            Appointment appointment, // selected instance
            Collection<Appointment> appointments,
            VEvent vComponent,
            List<VEvent> components,
            List<AppointmentGroup> appointmentGroups
//            Callback<Collection<AppointmentGroup>, Void> appointmentGroupWriteCallback,
//            Callback<Collection<VComponentDisplayable<?>>, Void> veventWriteCallback
            )
    {
        String appointmentTime = AgendaDateTimeUtilities.formatRange(appointment.getStartTemporal(), appointment.getEndTemporal());
//        VEventOld<Appointment,?> vEvent = (VEventOld<Appointment,?>) vComponent;
//        System.out.println("component:" + vComponent.hashCode());
        setTitle(vComponent.getSummary().getValue() + ": " + appointmentTime);
        initModality(Modality.APPLICATION_MODAL);
        
        // LOAD FXML
        FXMLLoader appointmentMenuLoader = new FXMLLoader();
        appointmentMenuLoader.setLocation(EditVEventLoader.class.getResource("view/EditVEvent.fxml"));
        if (Settings.resources != null)
        {
            appointmentMenuLoader.setResources(Settings.resources);
        }
        Parent appointmentPopup = null;
        try {
            appointmentPopup = appointmentMenuLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        EditVEventController appointmentEditController = appointmentMenuLoader.getController();
        Scene scene = new Scene(appointmentPopup);
        ICalendarAgenda.class.getResource(ICalendarAgenda.class.getSimpleName() + ".css").toExternalForm();
//        System.out.println("sheet:" + ICalendarAgenda.ICALENDAR_STYLE_SHEET);
        scene.getStylesheets().addAll(ICalendarAgenda.ICALENDAR_STYLE_SHEET);
        String agendaSheet = Agenda.class.getResource("/jfxtras/internal/scene/control/skin/agenda/" + Agenda.class.getSimpleName() + ".css").toExternalForm();
        scene.getStylesheets().addAll(ICalendarAgenda.ICALENDAR_STYLE_SHEET, agendaSheet);
        
        appointmentEditController.setupData(
              appointment,
              vComponent,
              appointments,
              components,
              appointmentGroups,
//              veventWriteCallback,
              this
              );

        setResizable(false);
        setScene(scene);
    }
//
//    public EditVEventLoader(Appointment appointment, VComponentDisplayable<?> vComponent,
//            ObservableList<VEvent> vEvents, ObservableList<Appointment> appointments,
//            ObservableList<AppointmentGroup> appointmentGroups)
//    {
//        // TODO Auto-generated constructor stub
//    }
}
 
