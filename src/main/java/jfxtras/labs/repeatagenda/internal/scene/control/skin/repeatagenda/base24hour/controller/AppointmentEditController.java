package jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour.controller;


import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour.AppointmentGroupGridPane;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarUtilities;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarUtilities.WindowCloseType;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VComponent;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VEvent;
import jfxtras.scene.control.LocalDateTimeTextField;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.scene.control.agenda.Agenda.AppointmentGroup;
import jfxtras.scene.control.agenda.Agenda.LocalDateTimeRange;

/**
 * Make popup to edit VEvents
 * 
 * @author David Bal
 * @see RepeatableController
 */
public class AppointmentEditController
{
//    private Appointment appointment;
    private LocalDateTime dateTimeStartInstanceOld;
    private VEvent<Appointment> vEvent;
    private VEvent<Appointment> vEventOld;
    private Collection<Appointment> appointments;
    private Collection<VComponent<Appointment>> vComponents;
    private Callback<Collection<VComponent<Appointment>>, Void> vEventWriteCallback;

    /** Indicates how the popup window closed */
//    public ObjectProperty<WindowCloseType> windowCloseTypeProperty() { return windowCloseType; }
    private ObjectProperty<WindowCloseType> windowCloseType; // default to X, meaning click on X to close window
    
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

    // Callback for LocalDateTimeTextField that is called when invalid date/time is entered
    Callback<Throwable, Void> errorCallback = (throwable) ->
    {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Invalid Date or Time");
        alert.setContentText("Please enter valid date and time");
        alert.showAndWait();
        return null;
    };
    
    @FXML public void initialize()
    {
        wholeDayCheckBox.selectedProperty().addListener((observable, oldSelection, newSelection) ->  {
            endTextField.setDisable(newSelection);
            endTextField.setLocalDateTime(startTextField.getLocalDateTime());
        });
    }
    
    /**
     * Set all data this object needs. 
     * 
     * @param appointment - selected instance
     * @param vComponent
     * @param dateTimeRange
     * @param appointments
     * @param vComponents
     * @param appointmentGroups
     * @param vEventWriteCallback
     * @param refreshCallback
     */
    public void setupData(
              Appointment appointment
            , VComponent<Appointment> vComponent
            , LocalDateTimeRange dateTimeRange
            , Collection<Appointment> appointments
            , Collection<VComponent<Appointment>> vComponents
            , List<AppointmentGroup> appointmentGroups
            , Callback<Collection<VComponent<Appointment>>, Void> vEventWriteCallback
            , ObjectProperty<WindowCloseType> popupCloseType)
    {
        dateTimeStartInstanceOld = appointment.getStartLocalDateTime();
        this.appointments = appointments;
        this.vComponents = vComponents;
        this.vEventWriteCallback = vEventWriteCallback;
        vEvent = (VEvent<Appointment>) vComponent;
        this.windowCloseType = popupCloseType;

        // Copy original VEvent
        try {
            vEventOld = vEvent.getClass().newInstance();
            vEvent.copyTo(vEventOld);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        
        summaryTextField.textProperty().bindBidirectional(vEvent.summaryProperty());
        descriptionTextArea.textProperty().bindBidirectional(vEvent.descriptionProperty());
        locationTextField.textProperty().bindBidirectional(vEvent.locationProperty());
        
        // START DATE/TIME
        Locale locale = Locale.getDefault();
        startTextField.setLocale(locale);
        startTextField.setLocalDateTime(appointment.getStartLocalDateTime());
        startTextField.setParseErrorCallback(errorCallback);

        // END DATE/TIME
        endTextField.setLocale(locale);
        endTextField.setLocalDateTime(appointment.getEndLocalDateTime());
        endTextField.setParseErrorCallback(errorCallback);
        
        // APPOINTMENT GROUP
        // store group name changes by each character typed
        appointmentGroupGridPane.appointmentGroupSelectedProperty().addListener(
            (observable, oldSelection, newSelection) ->
            {
                Integer i = appointmentGroupGridPane.getAppointmentGroupSelected();
                String newText = appointmentGroups.get(i).getDescription();
                groupTextField.setText(newText);
//                groupNameEdited.set(true); // TODO - HANDLE APPOINTMENT GROUP I/O
            });

        groupTextField.textProperty().addListener((observable, oldSelection, newSelection) ->
        {
            int i = appointmentGroupGridPane.getAppointmentGroupSelected();
            appointmentGroups.get(i).setDescription(newSelection);
            appointmentGroupGridPane.updateToolTip(i, appointmentGroups);
            vEvent.setCategories(newSelection);
//            groupNameEdited.set(true);
        });
        appointmentGroupGridPane.setupData(vComponent, appointmentGroups);

        // SETUP REPEATABLE CONTROLLER
        repeatableController.setupData(vComponent);
    }

    // AFTER CLICK SAVE VERIFY REPEAT IS VALID, IF NOT PROMPT.
    @FXML private void handleCloseButton()
    {
        LocalDateTime dateTimeStartInstanceNew = startTextField.getLocalDateTime();
        LocalDateTime dateTimeEndInstanceNew = endTextField.getLocalDateTime();
        final ICalendarUtilities.WindowCloseType result = vEvent.edit(
                dateTimeStartInstanceOld
              , dateTimeStartInstanceNew
              , dateTimeEndInstanceNew
              , vEventOld
              , appointments
              , vComponents
              , a -> ICalendarUtilities.repeatChangeDialog()
              , vEventWriteCallback);
        windowCloseType.set(result);
        if (windowCloseType.get() == WindowCloseType.CLOSE_WITH_CHANGE)
        {
//          refreshCallback.call(null);
//          System.out.println("repeat details " + appointment.getRepeat().getFrequency());
        
        }

    }
    
    
    @FXML private void handleCancelButton()
    {
        windowCloseType.set(WindowCloseType.CANCEL);
    }

    
}
