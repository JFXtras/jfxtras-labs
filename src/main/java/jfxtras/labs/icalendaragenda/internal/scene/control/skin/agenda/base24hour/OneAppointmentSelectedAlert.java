package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour;

import java.util.ResourceBundle;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import jfxtras.scene.control.agenda.Agenda.Appointment;


/**
 * Alert with options to affect one selected appointment 
 * 
 * @author David Bal
 *
 */
public class OneAppointmentSelectedAlert extends Alert
{
    public OneAppointmentSelectedAlert(Appointment appointment, ResourceBundle resources)
    {
        super(AlertType.CONFIRMATION);
        initModality(Modality.NONE);
        getDialogPane().getStyleClass().add("choice-dialog");

        // Buttons
        ButtonType editButtonType = new ButtonType(resources.getString("edit"));
        ButtonType deleteButtonType = new ButtonType(resources.getString("delete"));
        getButtonTypes().setAll(editButtonType, deleteButtonType, ButtonType.CANCEL);
        getDialogPane().lookupButton(ButtonType.CANCEL).setId("oneSelectedCancelButton");
        
        // assign labels
        setTitle(resources.getString("alert.one.appointment.title"));
        String appointmentTime = AgendaDateTimeUtilities.formatRange(appointment.getStartTemporal(), appointment.getEndTemporal());
        setHeaderText(appointment.getSummary() + System.lineSeparator() + appointmentTime);
        setContentText(resources.getString("alert.one.appointment.content"));
    }
}
