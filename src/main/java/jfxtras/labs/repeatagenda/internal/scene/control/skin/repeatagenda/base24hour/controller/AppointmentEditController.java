package jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour.controller;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
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
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarAgenda.VComponentFactory;
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
    private Appointment appointment;
    private LocalDateTime dateTimeStartInstanceOld;
    private VEvent<Appointment> vEvent;
    private VEvent<Appointment> vEventOld;
    private Collection<Appointment> appointments;
    private Collection<VComponent<Appointment>> vComponents;
    private Callback<Collection<VComponent<Appointment>>, Void> vEventWriteCallback;

    /** Indicates how the popup window closed */
//    public ObjectProperty<WindowCloseType> windowCloseTypeProperty() { return windowCloseType; }
    private ObjectProperty<WindowCloseType> popupCloseType; // default to X, meaning click on X to close window
    
    @FXML private ResourceBundle resources; // ResourceBundle that was given to the FXMLLoader
    @FXML private LocalDateTimeTextField startTextField; // DTSTART
    @FXML private LocalDateTimeTextField endTextField; // DTEND
    @FXML private AppointmentGroupGridPane appointmentGroupGridPane;
    @FXML private TextField locationTextField; // LOCATION
    @FXML private TextField summaryTextField; // SUMMARY
    @FXML private TextArea descriptionTextArea; // DESCRIPTION
    @FXML private CheckBox wholeDayCheckBox;
    @FXML private TextField groupTextField; // CATEGORIES
    @FXML private Button closeAppointmentButton;
    @FXML private Button cancelAppointmentButton;
    @FXML private Button closeRepeatButton;
    @FXML private Button cancelRepeatButton;
    @FXML private Button saveAdvancedButton;
    @FXML private Button deleteAppointmentButton;
    @FXML private RepeatableController<Appointment> repeatableController;
    
    private Temporal lastDateTimeStart = null;
    private Temporal lastDateTimeEnd = null;

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
    @SuppressWarnings("unchecked")
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
        this.appointment = appointment;
        this.vComponents = vComponents;
        this.vEventWriteCallback = vEventWriteCallback;
        vEvent = (VEvent<Appointment>) vComponent;
        this.popupCloseType = popupCloseType;

        // Copy original VEvent
        vEventOld = (VEvent<Appointment>) VComponentFactory.newVComponent(vEvent);
        
        // String bindings
        summaryTextField.textProperty().bindBidirectional(vEvent.summaryProperty());
        descriptionTextArea.textProperty().bindBidirectional(vEvent.descriptionProperty());
        locationTextField.textProperty().bindBidirectional(vEvent.locationProperty());
        
        // WHOLE DAY
        boolean wholeDay = vEvent.getDateTimeStart() instanceof LocalDate;
        wholeDayCheckBox.setSelected(wholeDay);
        
        wholeDayCheckBox.selectedProperty().addListener((observable, oldSelection, newSelection) ->
        {
            // TODO - TRY STACK PANE TO REPLACE LocalDateTimeTextField WITH LocalDateTextField WHEN WHOLE DAY
            if (newSelection)
            {
                lastDateTimeStart = vEvent.getDateTimeStart();
                lastDateTimeEnd = vEvent.getDateTimeEnd();
                vEvent.setDateTimeStart(null);
                vEvent.setDateTimeEnd(null);

                LocalDateTime startDate = startTextField.getLocalDateTime().toLocalDate().atStartOfDay();
                if (startDate.equals(startTextField.getLocalDateTime()))
                { // no change - listener will not fire
                    vEvent.setDateTimeStart(startTextField.getLocalDateTime().toLocalDate());
                } else
                {
                    startTextField.setLocalDateTime(startDate);                    
                }
                
                LocalDateTime endDate = endTextField.getLocalDateTime().toLocalDate().plusDays(1).atStartOfDay();
                if (endDate.equals(endTextField.getLocalDateTime()))
                { // no change - listener will not fire
                    vEvent.setDateTimeStart(endTextField.getLocalDateTime().toLocalDate());
                } else
                {
                    endTextField.setLocalDateTime(endDate);                    
                }
            } else
            {
                if ((lastDateTimeStart != null) & (lastDateTimeEnd != null))
                {
                    vEvent.setDateTimeStart(null);
                    vEvent.setDateTimeEnd(null);
                    startTextField.setLocalDateTime(VComponent.localDateTimeFromTemporal(lastDateTimeStart));
                    endTextField.setLocalDateTime(VComponent.localDateTimeFromTemporal(lastDateTimeEnd));  
                } else
                {
                    vEvent.setDateTimeStart(null);
                    vEvent.setDateTimeEnd(null);
                    vEvent.setDateTimeStart(startTextField.getLocalDateTime());
                    vEvent.setDateTimeEnd(endTextField.getLocalDateTime());
                }
            }
        });
        
        // START DATE/TIME
        // TODO - ICALENDAR REQUIRES DTEND TO BE EXCLUSIVE OF DATE - EVEN WHOLE DAY
        Locale locale = Locale.getDefault();
        startTextField.setLocale(locale);
        startTextField.setLocalDateTime(VComponent.localDateTimeFromTemporal(vEvent.getDateTimeStart()));
        startTextField.setParseErrorCallback(errorCallback);
        startTextField.localDateTimeProperty().addListener((observable, oldSelection, newSelection) ->
        {
            if (wholeDayCheckBox.isSelected())
            {
                LocalDateTime date = newSelection.toLocalDate().atStartOfDay();
                startTextField.setLocalDateTime(date);
                vEvent.setDateTimeStart(newSelection.toLocalDate());
            } else
            {
                vEvent.setDateTimeStart(newSelection);
            }
        });

        // END DATE/TIME
        endTextField.setLocale(locale);
        endTextField.setLocalDateTime(VComponent.localDateTimeFromTemporal(vEvent.getDateTimeEnd()));
        endTextField.setParseErrorCallback(errorCallback);
        endTextField.localDateTimeProperty().addListener((observable, oldSelection, newSelection) ->
        {
            if (wholeDayCheckBox.isSelected())
            {
                LocalDateTime date = newSelection.toLocalDate().atStartOfDay();
                endTextField.setLocalDateTime(date);
                vEvent.setDateTimeEnd(newSelection.toLocalDate());
            } else
            {
                vEvent.setDateTimeEnd(newSelection);
            }
        });
        
        // APPOINTMENT GROUP
        appointmentGroupGridPane.appointmentGroupSelectedProperty().addListener(
            (observable, oldSelection, newSelection) ->
            {
                Integer i = appointmentGroupGridPane.getAppointmentGroupSelected();
                String newText = appointmentGroups.get(i).getDescription();
                groupTextField.setText(newText);
//                groupNameEdited.set(true); // TODO - HANDLE APPOINTMENT GROUP I/O
            });
        // store group name changes by each character typed
        groupTextField.textProperty().addListener((observable, oldSelection, newSelection) ->
        {
            int i = appointmentGroupGridPane.getAppointmentGroupSelected();
            appointmentGroups.get(i).setDescription(newSelection);
            appointmentGroupGridPane.updateToolTip(i, appointmentGroups);
            vEvent.setCategories(newSelection);
            // TODO - ensure groupTextField is unique description text
//            groupNameEdited.set(true);
        });
        appointmentGroupGridPane.setupData(vComponent, appointmentGroups);

        // SETUP REPEATABLE CONTROLLER
        repeatableController.setupData(vComponent, startTextField.getLocalDateTime());
    }

    // AFTER CLICK SAVE VERIFY REPEAT IS VALID, IF NOT PROMPT.
    @FXML private void handleCloseButton()
    {
        if (! vEvent.isValid()) throw new IllegalArgumentException(vEvent.makeErrorString());
        final ICalendarUtilities.WindowCloseType result = vEvent.edit(
                dateTimeStartInstanceOld
              , appointment
              , vEventOld
              , appointments
              , vComponents
              , a -> ICalendarUtilities.repeatChangeDialog()
              , vEventWriteCallback);
        popupCloseType.set(result);
        if (popupCloseType.get() == WindowCloseType.CLOSE_WITHOUT_CHANGE)
        {
        }

    }
    
    
    @FXML private void handleCancelButton()
    {
        popupCloseType.set(WindowCloseType.CANCEL);
        vEventOld.copyTo(vEvent);
    }

    @FXML private void handleDeleteButton()
    {
//        windowCloseType.set(WindowCloseType.X);
//        System.out.println("delete:" + vEvent.getRRule());
//        LocalDateTime dateTimeStartInstanceNew = startTextField.getLocalDateTime();
        Temporal dateOrDateTime = (appointment.isWholeDay()) ? 
                appointment.getStartLocalDateTime().toLocalDate()
              : appointment.getStartLocalDateTime();
        final ICalendarUtilities.WindowCloseType result = vEvent.delete(
                dateOrDateTime
//              , appointments
              , vComponents
              , a -> ICalendarUtilities.repeatChangeDialog()
              , a -> ICalendarUtilities.confirmDelete(a)
              , vEventWriteCallback);
        System.out.println("result: " + result);
//        System.out.println("delete:" + vComponents.size() + " " + appointments.size());
        popupCloseType.set(result);
    }
    
}
