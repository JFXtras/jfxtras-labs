package jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour.controller.AppointmentEditController;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.Repeat;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.Settings;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.scene.control.agenda.Agenda.AppointmentGroup;
import jfxtras.scene.control.agenda.Agenda.LocalDateTimeRange;

// New stage for popup window
public class RepeatMenu extends Stage {

//    final private AppointmentEditController appointmentEditController;
//    final private LayoutHelp layoutHelp;
    private BooleanProperty groupNameEdited = new SimpleBooleanProperty(false);
    private BooleanProperty appointmentEdited = new SimpleBooleanProperty(false);
    public BooleanProperty appointmentEditedProperty() { return appointmentEdited; }
//    private Collection<Appointment> editedAppointments;
    private Iterator<Appointment> editedAppointments; // TODO
    private BooleanProperty repeatEdited = new SimpleBooleanProperty(false);
    public BooleanProperty repeatEditedProperty() { return repeatEdited; }

    //    private Pane pane;
//    private List<AppointmentGroup> appointmentGroups;
//    private Collection<Repeat> repeats;
//    private Collection<Appointment> appointments;
//    private Appointment appointment;

    public RepeatMenu(Appointment appointment
            , LocalDateTimeRange dateTimeRange
            , Collection<Appointment> appointments
            , Collection<Repeat> repeats
            , List<AppointmentGroup> appointmentGroups
            , Callback<LocalDateTimeRange, Appointment> newAppointmentCallback
            , Callback<Collection<Appointment>, Void> appointmentWriteCallback
            , Callback<Collection<Repeat>, Void> repeatWriteCallback)
    {
////        this.layoutHelp = layoutHelp;
//        this.pane = pane;
//        this.appointments = appointments;
//        this.repeats = repeats;
//        this.appointmentGroups = appointmentGroups;
//    }
//    
//    public void setup(Appointment appointment)
//    {
//        LayoutHelp layoutHelp = data.layoutHelp;
//        Appointment appointment = data.appointment;
//        Pane pane = data.pane;
        setTitle(AppointmentUtilities.makeAppointmentName(appointment));
        initModality(Modality.APPLICATION_MODAL);
        
        // LOAD FXML
        FXMLLoader appointmentMenuLoader = new FXMLLoader();
        appointmentMenuLoader.setLocation(Repeat.class.getResource("internal/scene/control/skin/agenda/base24hour/view/AppointmentEdit.fxml"));
        appointmentMenuLoader.setResources(Settings.resources);
        Control appointmentMenu = null;
        try {
            appointmentMenu = appointmentMenuLoader.load();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        AppointmentEditController appointmentEditController = appointmentMenuLoader.getController();
        appointmentEditController.setupData(
                appointment
              , dateTimeRange
              , appointments
              , repeats
              , appointmentGroups
              , newAppointmentCallback
              , appointmentWriteCallback
              , repeatWriteCallback);
        Scene scene = new Scene(appointmentMenu);

        // data element change bindings
        groupNameEdited.bindBidirectional(appointmentEditController.groupNameEditedProperty());
        appointmentEdited.bindBidirectional(appointmentEditController.appointmentEditedProperty());
        repeatEdited.bindBidirectional(appointmentEditController.repeatEditedProperty());

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
                    AppointmentIO.writeAppointmentGroups(appointmentGroups, Settings.APPOINTMENT_GROUPS_FILE);
                }
                break;
            }
//            layoutHelp.skin.setupAppointments();    // refresh appointment graphics
            System.out.println("RepeatMenuStage refresh " + appointmentEdited.get() + " " +  repeatEdited.get()); // refresh - use callback?
        });
        
        setScene(scene);
        // show it just below the menu icon
//        setX(NodeUtil.screenX(pane));
//        setY(NodeUtil.screenY(pane));
    }

//    public RepeatMenuStage(Appointment a,
//            Collection<Appointment> appointments,
//            Collection<Repeat> repeats,
//            List<AppointmentGroup> appointmentGroups, Pane pane) {
//        // TODO Auto-generated constructor stub
//    }

    
//    public void setAppointmentAndShow(Appointment appointment)
//    {
//        
//        
//        show();
//    }
    
    
    
    
}
 
