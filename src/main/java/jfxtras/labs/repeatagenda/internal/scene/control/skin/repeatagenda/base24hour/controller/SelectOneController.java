package jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour.controller;


import java.util.Collection;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Popup;
import jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour.DateTimeUtilities;
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
//    private Collection<VComponent<Appointment>> repeats;
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
    @FXML private Label nameLabel;

    @FXML public void initialize()
    {
//        nameLabel.setFocusTraversable(false); // this is the only way I can avoid having this text field focused when popup opens.  If it is focused then escape doesn't close popup.
    }

    public void setupData(
            Agenda agenda
          , Appointment appointment
          , Collection<Appointment> appointments) {

//        this.pane = pane;
        this.agenda = agenda;
        this.appointment = appointment;
        this.appointments = appointments;
//        this.repeats = repeats;
//        this.repeatMap = repeatMap;
//        this.layoutHelp = layoutHelp;
//        this.popup = popup;
        
        // TODO - FORMAT DIFFERENT DATE-TIME TYPES DIFFERENTLY
//        String start = Settings.DATE_TIME_FORMAT.format(appointment.getStartTemporal());
//        String end = Settings.DATE_FORMAT_AGENDA_END.format(appointment.getEndTemporal());
//        String appointmentTime = start + end + " ";

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
        // TODO - HOW DO I RUN THIS HERE?
        // NEED LISTENERS
        agenda.getEditAppointmentCallback().call(appointment);
//        AppointmentMenu appointmentMenu = new AppointmentMenu(pane, appointment, layoutHelp);
//        appointmentMenu.showMenu(null);
    }

    @FXML private void handleDeleteAppointment()
    {
        appointments.remove(appointment);
//        popup.hide();
//        RepeatableUtilities.deleteAppointments(layoutHelp.skinnable.appointments()
//        RepeatableUtilities.deleteAppointments(appointment, appointments, repeats);

//        layoutHelp.skin.setupAppointments();    // refresh appointment graphics
    }
    
}
