package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour;

import java.util.ResourceBundle;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import jfxtras.labs.icalendaragenda.scene.control.agenda.ICalendarAgenda;
import jfxtras.scene.control.agenda.Agenda.Appointment;

public class OneSelectedAppointmentVBox extends VBox
{
    private ICalendarAgenda agenda;
    private Appointment appointment;
    private BooleanProperty isFinished = new SimpleBooleanProperty();
    public BooleanProperty isFinished() { return isFinished; }
    
    @FXML private ResourceBundle resources; // ResourceBundle that was given to the FXMLLoader

    @FXML private VBox popupVBox;
    @FXML Button editAppointmentButton;
    @FXML private Button deleteAppointmentButton;
    @FXML private Button attendanceButton;
    @FXML private Label appointmentTimeLabel;
    @FXML private Label nameLabel;
    @FXML private ImageView closeIcon;
    
    public OneSelectedAppointmentVBox( )
    {
        super();
        FXMLUtilities.loadFxml(OneSelectedAppointmentVBox.class.getResource("OneSelectedAppointment.fxml"), this);
        Tooltip.install(closeIcon, new Tooltip(resources.getString("close")));
    }
    
    public void setupData(
            ICalendarAgenda agenda
          , Appointment appointment)
    {
        this.agenda = agenda;
        this.appointment = appointment;

        String appointmentTime = AgendaDateTimeUtilities.formatRange(appointment.getStartTemporal(), appointment.getEndTemporal());
        appointmentTimeLabel.setText(appointmentTime);
        nameLabel.setText(appointment.getSummary());
        nameLabel.textProperty().addListener((observable, oldValue, newValue) ->  {
            appointment.setSummary(newValue);
        });
//        editAppointmentButton.requestFocus(); // TODO - put in onshowing listener - STILL didn't work.  why?
        if (appointment.getAppointmentGroup() != null)
        {
            getStyleClass().add(appointment.getAppointmentGroup().getStyleClass());
        }
    }
    
    @FXML private void handleEditAppointment()
    {
        agenda.getEditAppointmentCallback().call(appointment);
    }

    @FXML private void handleDeleteAppointment()
    {
//        Temporal startInstance = appointment.getStartTemporal();
//        VComponentDisplayable<?> vComponent = agenda.findVComponent(appointment);
//        agenda.appointments().removeListener(agenda.getAppointmentsListChangeListener());
////        vComponent.handleDelete(
////                agenda.vComponents()
////              , startInstance
////              , appointment
////              , agenda.appointments()
////              , DeleteChoiceDialog.DELETE_DIALOG_CALLBACK);
//        agenda.appointments().addListener(agenda.getAppointmentsListChangeListener());
    }
    
    @FXML private void handleClose()
    {
        System.out.println("close");
        isFinished.set(true);
    }
    
    @FXML private void handleEnterClose(KeyEvent event)
    {
        if (event.getCode() == KeyCode.ENTER)
        {
            System.out.println("close");
            isFinished.set(true);
        }
    }

}
