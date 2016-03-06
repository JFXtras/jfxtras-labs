package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.controller;

import java.util.Collection;

import javafx.fxml.FXML;
import javafx.stage.Popup;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.Agenda.Appointment;

public class NewAppointmentController extends Popup
{
    private Collection<Appointment> appointments;
    private Agenda agenda;
    private Appointment appointment;
    private Popup popup;
    
    @FXML public void initialize() { }
    
    public void setupData(
            Agenda agenda
          , Appointment appointment
          , Collection<Appointment> appointments) {

//        this.pane = pane;
        this.agenda = agenda;
        this.appointment = appointment;
        this.appointments = appointments;

//        this.repeatMap = repeatMap;
//        this.layoutHelp = layoutHelp;
//        this.popup = popup;
        
//        String start = Settings.DATE_TIME_FORMAT.format(appointment.getStartTemporal());
//        String end = Settings.DATE_FORMAT_AGENDA_END.format(appointment.getEndTemporal());
//        String appointmentTime = DateTimeType.formatRange(appointment.getStartTemporal(), appointment.getEndTemporal());
//        String appointmentTime = start + end + " ";
//        appointmentTimeLabel.setText(appointmentTime);
//        nameLabel.setText(appointment.getSummary());
//        nameLabel.textProperty().addListener((observable, oldValue, newValue) ->  {
//            appointment.setSummary(newValue);
//        });
//        editAppointmentButton.requestFocus();
    }
    
    @FXML private void handleCreateEvent()
    {
    }

    @FXML private void handleEditAppointment()
    {
        agenda.getEditAppointmentCallback().call(appointment);
        popup.hide();
    }
}
