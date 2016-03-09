package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.controller;


import java.time.temporal.Temporal;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import jfxtras.labs.icalendar.VComponent;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.DateTimeUtilities;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.DeleteChoiceDialog;
import jfxtras.labs.icalendaragenda.scene.control.agenda.ICalendarAgenda;
import jfxtras.scene.control.agenda.Agenda.Appointment;

/**
 * @author David Bal
 *
 */
public class SelectedOneAppointmentController extends Rectangle
{
    private ICalendarAgenda agenda;
    private Appointment appointment;
//    VComponent<Appointment> vComponent;
//    private Collection<Appointment> appointments;

    
    @FXML private ResourceBundle resources; // ResourceBundle that was given to the FXMLLoader

    @FXML private AnchorPane selectedOneAppointmentAnchorPane;
    @FXML private VBox vBox;
    @FXML private Button editAppointmentButton;
    @FXML private Button deleteAppointmentButton;
    @FXML private Button attendanceButton;
    @FXML private Label appointmentTimeLabel;
    @FXML private Label nameLabel;

    @FXML public void initialize()
    {
//        nameLabel.setFocusTraversable(false); // this is the only way I can avoid having this text field focused when popup opens.  If it is focused then escape doesn't close popup.
    }

    public void setupData(
            ICalendarAgenda agenda
          , Appointment appointment)
    {
        this.agenda = agenda;
        this.appointment = appointment;

        String appointmentTime = DateTimeUtilities.formatRange(appointment.getStartTemporal(), appointment.getEndTemporal());
        appointmentTimeLabel.setText(appointmentTime);
        nameLabel.setText(appointment.getSummary());
        nameLabel.textProperty().addListener((observable, oldValue, newValue) ->  {
            appointment.setSummary(newValue);
        });
        editAppointmentButton.requestFocus();
    }
    
    @FXML private void handleEditAppointment()
    {
        agenda.getEditAppointmentCallback().call(appointment);
    }

    @FXML private void handleDeleteAppointment()
    {
        Temporal startInstance = appointment.getStartTemporal();
        VComponent<Appointment> vComponent = agenda.findVComponent(appointment);
        agenda.appointments().removeListener(agenda.getAppointmentsListChangeListener());
        vComponent.handleDelete(
                agenda.vComponents()
              , startInstance
              , appointment
              , agenda.appointments()
              , DeleteChoiceDialog.DELETE_DIALOG_CALLBACK);
        agenda.appointments().addListener(agenda.getAppointmentsListChangeListener());
    }    
}
