package jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour.controller;


import java.util.Collection;
import java.util.ResourceBundle;

import javax.xml.parsers.ParserConfigurationException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Popup;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.Settings;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VComponent;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.Agenda.Appointment;

/**
 * @author David Bal
 *
 */
public class SelectOneController extends Rectangle
{
    
//    private Pane pane;
    private Collection<Appointment> appointments;
    private Collection<VComponent<Appointment>> repeats;
//    private Map<Appointment, Repeat> repeatMap;
    private Agenda agenda;
    private Appointment appointment;
    private Popup popup;
    
    @FXML private ResourceBundle resources; // ResourceBundle that was given to the FXMLLoader

    @FXML private AnchorPane anchorPane;
    @FXML private VBox vBox;
    @FXML private Button editAppointmentButton;
    @FXML private Button deleteAppointmentButton;
    @FXML private Button attendanceButton;
    @FXML private Label appointmentTimeLabel;
    @FXML private TextField nameTextField;

    @FXML public void initialize()
    {
        nameTextField.setFocusTraversable(false); // this is the only way I can avoid having this text field focused when popup opens.  If it is focused then escape doesn't close popup.
//        anchorPane.requestFocus();
    }

    public void setupData(
            Agenda agenda
          , Appointment appointment
          , Collection<Appointment> appointments) {

//        this.pane = pane;
        this.agenda = agenda;
        this.appointment = appointment;
        this.appointments = appointments;
        this.repeats = repeats;
//        this.repeatMap = repeatMap;
//        this.layoutHelp = layoutHelp;
//        this.popup = popup;
        
        String start = Settings.DATE_FORMAT_AGENDA_START.format(appointment.getStartLocalDateTime());
        String end = Settings.DATE_FORMAT_AGENDA_END.format(appointment.getEndLocalDateTime());
        String appointmentTime = start + end + " ";
        appointmentTimeLabel.setText(appointmentTime);
        nameTextField.setText(appointment.getSummary());
        nameTextField.textProperty().addListener((observable, oldValue, newValue) ->  {
            appointment.setSummary(newValue);
        });
        editAppointmentButton.requestFocus();
    }
    
    @FXML private void handleEditAppointment()
    {
        // TODO - HOW DO I RUN THIS HERE?
        // NEED LISTENERS
        agenda.getEditAppointmentCallback().call(appointment);
//        AppointmentMenu appointmentMenu = new AppointmentMenu(pane, appointment, layoutHelp);
//        appointmentMenu.showMenu(null);
    }

    @FXML private void handleDeleteAppointment() throws ParserConfigurationException {
        popup.hide();
//        RepeatableUtilities.deleteAppointments(layoutHelp.skinnable.appointments()
//        RepeatableUtilities.deleteAppointments(appointment, appointments, repeats);

//        layoutHelp.skin.setupAppointments();    // refresh appointment graphics
    }
    
}
