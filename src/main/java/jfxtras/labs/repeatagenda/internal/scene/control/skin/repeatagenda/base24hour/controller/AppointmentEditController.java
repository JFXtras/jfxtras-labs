package jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour.controller;


import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour.AppointmentGroupGridPane;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VComponent;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VEvent;
import jfxtras.scene.control.LocalDateTimeTextField;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.scene.control.agenda.Agenda.AppointmentGroup;
import jfxtras.scene.control.agenda.Agenda.LocalDateTimeRange;

/**
 * 
 * @author David Bal
 * @see RepeatableController
 */
public class AppointmentEditController
{
    
    private VEvent vEvent;
    private VEvent vEventOld;
    
    @FXML private ResourceBundle resources; // ResourceBundle that was given to the FXMLLoader
    
    @FXML private LocalDateTimeTextField startTextField; // DTSTART
    @FXML private LocalDateTimeTextField endTextField; // DTEND
    @FXML private AppointmentGroupGridPane appointmentGroupGridPane;
    
    @FXML private TextField locationTextField; // LOCATION
    @FXML private TextField summaryTextField; // SUMMARY
    @FXML private TextArea descriptionTextArea; // DESCRIPTION
    @FXML private CheckBox wholeDayCheckBox;
    @FXML private TextField groupTextField;
    @FXML private Button closeAppointmentButton;
    @FXML private Button cancelAppointmentButton;
    @FXML private Button closeRepeatButton;
    @FXML private Button cancelRepeatButton;

    @FXML private Button saveAdvancedButton;
    
    @FXML private RepeatableController repeatableController;
    
    @FXML public void initialize()
    {
        wholeDayCheckBox.selectedProperty().addListener((observable, oldSelection, newSelection) ->  {
            endTextField.setDisable(newSelection);
            endTextField.setLocalDateTime(startTextField.getLocalDateTime());
        });
    }
    
    public void setupData(VEvent vEvent
            , LocalDateTimeRange dateTimeRange
            , Collection<Appointment> appointments
            , Collection<VComponent> vEvents
            , List<AppointmentGroup> appointmentGroups
            , Callback<Collection<VComponent>, Void> vEventWriteCallback
            , Callback<Void, Void> refreshCallback)
    {
        Locale locale = Locale.getDefault();
        summaryTextField.textProperty().bindBidirectional(vEvent.summaryProperty());
    }

    // AFTER CLICK SAVE VERIFY REPEAT IS VALID, IF NOT PROMPT.
    @FXML private void handleCloseButton() {


    }
    
    @FXML private void handleCancelButton() {
    }


    
}
