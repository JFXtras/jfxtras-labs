package jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour.controller;


import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.Repeat.EndCriteria;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VComponentAbstract;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.freq.Frequency;
import jfxtras.scene.control.agenda.Agenda.LocalDateTimeRange;

public class RepeatableController {

final private static int EXCEPTION_CHOICE_LIMIT = 50;
    
//private RepeatableAppointment appointment;
private VComponentAbstract vcomponent;

@FXML private ResourceBundle resources; // ResourceBundle that was given to the FXMLLoader

@FXML private CheckBox repeatableCheckBox;
@FXML private GridPane repeatableGridPane;
@FXML private ComboBox<Frequency.FrequencyType> frequencyComboBox;
@FXML private Spinner<Integer> intervalSpinner;
@FXML private Label frequencyLabel;
@FXML private Label eventLabel;
@FXML private Label weeklyLabel;
@FXML private HBox weeklyHBox;
@FXML private CheckBox sundayCheckBox;
@FXML private CheckBox mondayCheckBox;
@FXML private CheckBox tuesdayCheckBox;
@FXML private CheckBox wednesdayCheckBox;
@FXML private CheckBox thursdayCheckBox;
@FXML private CheckBox fridayCheckBox;
@FXML private CheckBox saturdayCheckBox;
@FXML private VBox monthlyVBox;
@FXML private Label monthlyLabel;
@FXML private RadioButton dayOfMonthRadioButton;
@FXML private RadioButton dayOfWeekRadioButton;
@FXML private DatePicker startDatePicker;
@FXML private RadioButton endNeverRadioButton;
@FXML private RadioButton endAfterRadioButton;
@FXML private Spinner<Integer> endAfterEventsSpinner;
@FXML private RadioButton endOnRadioButton;
@FXML private DatePicker endOnDatePicker;
private ToggleGroup endGroup;
@FXML private Label repeatSummaryLabel;

@FXML ComboBox<LocalDateTime> exceptionComboBox;
@FXML Button addExceptionButton;
@FXML ListView<LocalDateTime> exceptionsListView; // list of date/times to be skipped (deleted events)
@FXML Button removeExceptionButton;

@FXML private Button closeButton;
@FXML private Button cancelButton;

private Set<LocalDateTime> invalidExceptions = new HashSet<LocalDateTime>();


@FXML public void initialize()
{
    
    
}


/**
 * 
 * @param rrule
 * @param dateTimeRange : date range for current agenda skin
 */
    public void setupData(
            VComponentAbstract vcomponent
          , LocalDateTimeRange dateTimeRange)
    {

    }
    
        
    private void setEndGroup(EndCriteria endCriteria) {
        switch (endCriteria) {
        case NEVER:
            endGroup.selectToggle(endNeverRadioButton);
            break;
        case AFTER:
            endGroup.selectToggle(endAfterRadioButton);
            break;
        case UNTIL:
            endGroup.selectToggle(endOnRadioButton);
            break;
        default:
            break;
        }
    }
    
    // Displays an alert notifying user number input is not valid
    private static void notNumberAlert(String validFormat)
    {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Invalid Number");
        alert.setHeaderText("Please enter valid numbers.");
        alert.setContentText("Accepted format: " + validFormat);
        ButtonType buttonTypeOk = new ButtonType("OK", ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeOk);
        Optional<ButtonType> result = alert.showAndWait();
    }
    
    @FXML private void handleAddException()
    {
        LocalDateTime d = exceptionComboBox.getValue();
        exceptionsListView.getItems().add(d);
//        makeExceptionDates();
//        exceptionComboBox.getItems().remove(d);
        Collections.sort(exceptionsListView.getItems()); // Maintain sorted list
        if (exceptionComboBox.getValue() == null) addExceptionButton.setDisable(true);
    }

    @FXML private void handleRemoveException()
    {
        System.out.println("Remove Exception");
        LocalDateTime d = exceptionsListView.getSelectionModel().getSelectedItem();
//        makeExceptionDates();
//        exceptionComboBox.getItems().add(d);
        exceptionsListView.getItems().remove(d);
        if (exceptionsListView.getSelectionModel().getSelectedItem() == null) removeExceptionButton.setDisable(true);
    }
    
}
