package jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour.controller;


import java.util.Collection;
import java.util.ResourceBundle;

import javax.xml.parsers.ParserConfigurationException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Popup;
import jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour.AppointmentUtilities;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.Repeat;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableAgenda.RepeatableAppointment;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableUtilities;
import jfxtras.scene.control.agenda.Agenda.Appointment;

/**
 * @author David Bal
 *
 */
public class AppointmentPopupController {
    
    private Pane pane;
    private RepeatableAppointment appointment;
//    private LayoutHelp layoutHelp;
    private Popup popup;
    
    @FXML private ResourceBundle resources; // ResourceBundle that was given to the FXMLLoader

    @FXML private Button editAppointmentButton;
    @FXML private Button deleteAppointmentButton;
    @FXML private Button attendanceButton;
    @FXML private Label appointmentTimeLabel;
    @FXML private TextField nameTextField;
    private Collection<Appointment> appointments;
    private Collection<Repeat> repeats;

    @FXML public void initialize() {
   }

    public void setupData(
              Collection<Appointment> appointments
            , RepeatableAppointment appointment
            , Collection<Repeat> repeats
            , Pane pane

            //            , LayoutHelp layoutHelp
            , Popup popup) {

        this.pane = pane;
        this.appointment = appointment;
        this.appointments = appointments;
        this.repeats = repeats;
//        this.layoutHelp = layoutHelp;
        this.popup = popup;
        
        appointmentTimeLabel.setText(AppointmentUtilities.makeAppointmentTime(appointment));
        nameTextField.setText(appointment.getSummary());
        nameTextField.textProperty().addListener((observable, oldValue, newValue) ->  {
            appointment.setSummary(newValue);
        });
    }
    
    @FXML private void handleEditAppointment() {
//        AppointmentMenu appointmentMenu = new AppointmentMenu(pane, appointment, layoutHelp);
//        appointmentMenu.showMenu(null);
    }

    @FXML private void handleDeleteAppointment() throws ParserConfigurationException {
        popup.hide();
//        RepeatableUtilities.deleteAppointments(layoutHelp.skinnable.appointments()
        RepeatableUtilities.deleteAppointments(appointment, appointments, repeats);

//        layoutHelp.skin.setupAppointments();    // refresh appointment graphics
    }
    
}
