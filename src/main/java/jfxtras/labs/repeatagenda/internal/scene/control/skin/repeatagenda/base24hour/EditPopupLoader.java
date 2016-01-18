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
import jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour.controller.AppointmentEditController;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarAgenda;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.Settings;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VComponent;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VEvent;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.scene.control.agenda.Agenda.AppointmentGroup;

/** Makes new stage for popup window to edit VEvent with Agenda.Appointment instances
 * 
 * @author David Bal
 * @see AppointmentEditController
 */
public class EditPopupLoader extends Stage {

    private BooleanProperty groupNameEdited = new SimpleBooleanProperty(false);
//    private ObjectProperty<WindowCloseType> popupCloseType = new SimpleObjectProperty<WindowCloseType>(WindowCloseType.X); // default to X, meaning click on X to close window)

    // CONSTRUCTOR
    public EditPopupLoader(
              Appointment appointment // selected instance
            , VComponent<Appointment> vComponent
            , ICalendarAgenda agenda
            , Callback<Collection<AppointmentGroup>, Void> appointmentGroupWriteCallback
            , Callback<Collection<VComponent<Appointment>>, Void> veventWriteCallback
            , Callback<Void, Void> refreshCallback)
    {
        String start = Settings.DATE_FORMAT_AGENDA_START.format(appointment.getStartLocalDateTime());
        String end = Settings.DATE_FORMAT_AGENDA_END.format(appointment.getEndLocalDateTime());
        String appointmentTime = start + end + " ";
        VEvent<Appointment> vEvent = (VEvent<Appointment>) vComponent;
//        VComponent<Appointment> vComponentOld = VComponentFactory.newVComponent(vComponent);
        setTitle(vEvent.getSummary() + ": " + appointmentTime);
        initModality(Modality.APPLICATION_MODAL);
        
        // LOAD FXML
        FXMLLoader appointmentMenuLoader = new FXMLLoader();
        appointmentMenuLoader.setLocation(EditPopupLoader.class.getResource("view/AppointmentEdit.fxml"));
        appointmentMenuLoader.setResources(Settings.resources);
        Control appointmentPopup = null;
        try {
            appointmentPopup = appointmentMenuLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        appointmentPopup.setId("editAppointmentPopup");
        AppointmentEditController appointmentEditController = appointmentMenuLoader.getController();

        appointmentEditController.setupData(
                appointment
              , vComponent
              , agenda.getDateTimeRange()
              , agenda.appointments()
              , agenda.vComponents()
              , agenda.appointmentGroups()
              , veventWriteCallback
              , this);
        Scene scene = new Scene(appointmentPopup);
        setScene(scene);

        //        groupNameEdited.bindBidirectional(appointmentEditController.groupNameEditedProperty());

        // listen for close trigger
//        popupCloseType.addListener((observable, oldSelection, newSelection) -> close());
        
//        // when popup closes write changes if occurred
//        setOnHidden((windowEvent) -> 
//        {
////            System.out.println("group:" + appointment.getAppointmentGroup().getDescription());
////            agenda.appointmentGroups().stream().map(a -> a.getDescription()).forEach(System.out::println);
//            switch (popupCloseType.get())
//            {
//            case CLOSE_WITH_CHANGE:
//                agenda.refresh();
////                System.out.println("close popup");
//                if (groupNameEdited.getValue()) {    // TODO write group name changes
////                    System.out.println("group change write needed");
//                    appointmentGroupWriteCallback.call(agenda.appointmentGroups());
////                    AppointmentIO.writeAppointmentGroups(appointmentGroups, Settings.APPOINTMENT_GROUPS_FILE);
//                }
//                
//                break;
//            case CANCEL:
//            case X:
//                System.out.println("copy back to original:" + vComponentOld.getDateTimeStart() + " " 
//                        + vComponent.getDateTimeStart());                
//                vComponentOld.copyTo(vComponent);
//                System.out.println("copy back to original:" + vComponent.getDateTimeStart() + " " );
//                break;
//            default:
//                break;
//            }
//            System.out.println("VComponent:" + vComponent.toString());
//        });
        
//        setScene(scene);
    }

    
}
 
