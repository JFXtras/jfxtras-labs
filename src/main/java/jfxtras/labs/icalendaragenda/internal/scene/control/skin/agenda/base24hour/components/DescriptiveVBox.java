package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.components;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.CategorySelectionGridPane;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.Settings;
import jfxtras.labs.icalendarfx.components.VComponentDisplayable;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Summary;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities;
import jfxtras.scene.control.LocalDateTextField;
import jfxtras.scene.control.LocalDateTimeTextField;
import jfxtras.scene.control.agenda.TemporalUtilities;

/** Makes new TabPane for editing a {@link VComponentDisplayable} component
 * 
 * @author David Bal
 */
public abstract class DescriptiveVBox<T extends VComponentDisplayable<?>> extends VBox
{
    @FXML private ResourceBundle resources; // ResourceBundle that was given to the FXMLLoader
    public ResourceBundle getResources() { return resources; }

    @FXML GridPane timeGridPane;
    LocalDateTimeTextField startDateTimeTextField = new LocalDateTimeTextField(); // start of recurrence
    LocalDateTextField startDateTextField = new LocalDateTextField(); // start of recurrence when wholeDayCheckBox is selected
    protected final static LocalTime DEFAULT_START_TIME = LocalTime.of(10, 0); // default time

    @FXML Label endLabel;
    
    @FXML private CheckBox wholeDayCheckBox;
    @FXML TextField summaryTextField;
    @FXML TextArea descriptionTextArea;
    @FXML Label locationLabel;
    @FXML TextField locationTextField;
    @FXML private TextField categoryTextField;
    @FXML private CategorySelectionGridPane categorySelectionGridPane;
    @FXML private Button saveComponentButton;
    @FXML private Button cancelComponentButton;
    @FXML private Button saveRepeatButton;
    @FXML private Button cancelRepeatButton;
    @FXML private Button deleteComponentButton;
    @FXML private Tab appointmentTab;
    @FXML private Tab repeatableTab;
    
    public DescriptiveVBox( )
    {
        super();
        loadFxml(DescriptiveVBox.class.getResource("EditDescriptive.fxml"), this);
        categorySelectionGridPane.getStylesheets().addAll(getStylesheets());
        startDateTimeTextField.setId("startDateTimeTextField");
        startDateTextField.setId("startDateTextField");
        startDateTimeTextField.setId("startDateTimeTextField");
    }

    final private ChangeListener<? super LocalDate> startDateTextListener = (observable, oldValue, newValue) -> synchStartDate(oldValue, newValue);

    /** Update startDateTimeTextField when startDateTextField changes */
    void synchStartDate(LocalDate oldValue, LocalDate newValue)
    {
        System.out.println("start date:" + newValue);
        startRecurrenceProperty.set(newValue);
        startDateTimeTextField.localDateTimeProperty().removeListener(startDateTimeTextListener);
        LocalDateTime newDateTime = startDateTimeTextField.getLocalDateTime().with(newValue);
        startDateTimeTextField.setLocalDateTime(newDateTime);
        startDateTimeTextField.localDateTimeProperty().addListener(startDateTimeTextListener);
    }
            
    final private ChangeListener<? super LocalDateTime> startDateTimeTextListener = (observable, oldValue, newValue) -> synchStartDateTime(oldValue, newValue);

    /** Update startDateTextField when startDateTimeTextField changes */
    void synchStartDateTime(LocalDateTime oldValue, LocalDateTime newValue)
    {
        System.out.println("start date2:" + newValue);
        if (startOriginalRecurrence.isSupported(ChronoUnit.NANOS)) // ZoneDateTime, LocalDateTime
        {
            startRecurrenceProperty.set(startOriginalRecurrence.with(newValue));            
        } else // LocalDate
        {
            startRecurrenceProperty.set(newValue);
        }
        startDateTextField.localDateProperty().removeListener(startDateTextListener);
        LocalDate newDate = LocalDate.from(startDateTimeTextField.getLocalDateTime());
        startDateTextField.setLocalDate(newDate);
        startDateTextField.localDateProperty().addListener(startDateTextListener);
    }
    
    // Callback for LocalDateTimeTextField that is called when invalid date/time is entered
    protected final Callback<Throwable, Void> errorCallback = (throwable) ->
    {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Invalid Date or Time");
        alert.setContentText("Please enter valid date and time");
        alert.showAndWait();
        return null;
    };
    
    T vComponentEdited;
    private String initialCategory;
    Temporal startOriginalRecurrence;
    // TODO - MAKE A LOCALDATE AND A LOCALDATETIME VERSION
    /** Contains the start recurrence Temporal LocalDate or LocalDateTime */
    ObjectProperty<Temporal> startRecurrenceProperty;

    public void setupData(
            T vComponent,
            Temporal startRecurrence,
            Temporal endRecurrence,
            List<String> categories)
    {
        startOriginalRecurrence = startRecurrence;
        startRecurrenceProperty = new SimpleObjectProperty<>(startRecurrence);
        vComponentEdited = vComponent;
        
        // Disable repeat rules for events with recurrence-id
        if (vComponentEdited.getRecurrenceDates() != null)
        { // recurrence recurrences can't add repeat rules (only parent can have repeat rules)
            repeatableTab.setDisable(true);
            repeatableTab.setTooltip(new Tooltip(resources.getString("repeat.tab.unavailable")));
        }
        
        // String bindings
        if (vComponentEdited.getSummary() == null)
        {
            vComponentEdited.setSummary(Summary.parse(""));
        }
        summaryTextField.textProperty().bindBidirectional(vComponentEdited.getSummary().valueProperty());
        
        // START DATE/TIME
        startDateTimeTextField.setLocale(Locale.getDefault());
        startDateTextField.setLocale(Locale.getDefault());
        startDateTimeTextField.setParseErrorCallback(errorCallback);
        startDateTextField.setParseErrorCallback(errorCallback);
        final LocalDateTime start1;
        final LocalDate start2 = LocalDate.from(startRecurrence);
        if (vComponentEdited.isWholeDay())
        {
            start1 = LocalDate.from(startRecurrence).atTime(DEFAULT_START_TIME);
        } else
        {
            start1 = TemporalUtilities.toLocalDateTime(startRecurrence);     
        }
        startDateTimeTextField.setLocalDateTime(start1);
        startDateTextField.setLocalDate(start2);
        startDateTimeTextField.localDateTimeProperty().addListener(startDateTimeTextListener);
        startDateTextField.localDateProperty().addListener(startDateTextListener);

        // WHOLE DAY
        wholeDayCheckBox.setSelected(vComponentEdited.isWholeDay());
        handleWholeDayChange(vComponentEdited, wholeDayCheckBox.isSelected()); 
        wholeDayCheckBox.selectedProperty().addListener((observable, oldSelection, newSelection) -> handleWholeDayChange(vComponent, newSelection));

        // CATEGORIES
        // initialize with empty category, to be removed later if not populated with other value.
        if (vComponentEdited.getCategories() == null)
        {
            vComponentEdited.withCategories("");
        }
        categorySelectionGridPane.categorySelectedProperty().addListener(
            (observable, oldSelection, newSelection) ->
            {
                Integer i = categorySelectionGridPane.getCategorySelected();
                String newText = categories.get(i);
                categoryTextField.setText(newText);
            });

        // store group name changes by each character typed
        categoryTextField.textProperty().addListener((observable, oldSelection, newSelection) ->
        {
            int i = categorySelectionGridPane.getCategorySelected();
            if (! categories.get(i).equals(newSelection))
            {
                // ideally, categories list will be a LinkedList to reduce cost of element removal and addition
                categories.remove(i);
                categories.add(i, newSelection);
            }
            categorySelectionGridPane.updateToolTip(i, categories.get(i));
            vComponentEdited.getCategories().get(0).setValue(FXCollections.observableArrayList(newSelection));
        });
        // verify category is unique
        categoryTextField.focusedProperty().addListener((obs, oldValue, newValue) ->
        {
            if (newValue)
            {
                // save initial value
                initialCategory = categoryTextField.getText();
            } else
            {
                int selectedIndex = categorySelectionGridPane.getCategorySelected();
                int otherMatch = -1;
                for (int i=0; i<categories.size(); i++)
                {
                    if (i == selectedIndex) continue;
                    if (categories.get(i).equals(categoryTextField.getText()))
                    {
                        otherMatch = ++i;
                        break;
                    }
                }
                if (otherMatch >= 0)
                {
                    invalidCategoryAlert(categoryTextField.getText(), otherMatch);
                    categoryTextField.setText(initialCategory);
                }
            }
        });
        String initialCategory = vComponentEdited.getCategories().get(0).getValue().get(0);
        categorySelectionGridPane.setupData(initialCategory, categories);
        
        vComponentEdited.getDateTimeStart().valueProperty().addListener(dateTimeStartListener);

    }
    
    /** Synch recurrence dates when DTSTART is modified (can occur when {@link synchStartDatePickerAndComponent#startDatePicker} changes */
    ChangeListener<? super Temporal> dateTimeStartListener = (obs, oldValue, newValue) -> 
    {
        // If recurrence before or equal to DTSTART adjust to match DTSTART
        // if recurrence is after DTSTART don't adjust it
        LocalDate d1 = LocalDate.from(startRecurrenceProperty.get());
        LocalDate d2 = LocalDate.from(newValue);
        LocalDate d3 = LocalDate.from(oldValue);
        if ((! DateTimeUtilities.isAfter(d1, d2)) || d1.equals(d3))
        {
            Temporal r = newValue.with(startDateTextField.getLocalDate());
            TemporalAmount shift = DateTimeUtilities.temporalAmountBetween(r, newValue);
            LocalDateTime startNew = startDateTimeTextField.getLocalDateTime().plus(shift);
            startDateTimeTextField.setLocalDateTime(startNew);
        } else
        {
//            shiftAmount = DateTimeUtilities.temporalAmountBetween(oldValue, newValue);
        }
    };
        
    /*
     * When
     */
    void handleWholeDayChange(T vComponent, Boolean newSelection)
    {
        startDateTimeTextField.localDateTimeProperty().removeListener(startDateTimeTextListener);
        startDateTextField.localDateProperty().removeListener(startDateTextListener);
        if (newSelection)
        {
            timeGridPane.getChildren().remove(startDateTimeTextField);
            timeGridPane.add(startDateTextField, 1, 0);
            startRecurrenceProperty.set(startDateTextField.getLocalDate());
        } else
        {
            timeGridPane.getChildren().remove(startDateTextField);
            timeGridPane.add(startDateTimeTextField, 1, 0);
            startRecurrenceProperty.set(startDateTimeTextField.getLocalDateTime());
        }
        startDateTextField.localDateProperty().addListener(startDateTextListener);
        startDateTimeTextField.localDateTimeProperty().addListener(startDateTimeTextListener);
    }
    
    /* If startRecurrence isn't valid due to a RRULE change, changes startRecurrence and
     * endRecurrence to closest valid values
     */
    void synchRecurrenceDates(Temporal oldValue, Temporal newValue)
    {
        if (! vComponentEdited.isRecurrence(startRecurrenceProperty.get()))
        {
            TemporalAmount shift = DateTimeUtilities.temporalAmountBetween(oldValue, newValue);
            LocalDateTime startNew = startDateTimeTextField.getLocalDateTime().plus(shift);
            startDateTimeTextField.setLocalDateTime(startNew);
        }
    }
    
    /* Displays an alert notifying that startInstance has changed due to changes in the Repeat tab.
     * These changes can include the day of the week is not valid or the start date has shifted.
     * The closest valid date is substituted.
    */
    // TODO - PUT COMMENTS IN RESOURCES
    @Deprecated // possibly remove - just update without alert
    protected void startRecurrenceChangedAlert(Temporal t1, Temporal t2)
    {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.getDialogPane().setId("startInstanceChangedAlert");
        alert.getDialogPane().lookupButton(ButtonType.OK).setId("startInstanceChangedAlertOkButton");
        alert.setHeaderText("Time not valid due to repeat rule change");
        alert.setContentText(Settings.DATE_FORMAT_AGENDA_EXCEPTION.format(t1) + " is no longer valid." + System.lineSeparator()
                    + "It has been replaced by " + Settings.DATE_FORMAT_AGENDA_EXCEPTION.format(t2));
        ButtonType buttonTypeOk = new ButtonType("OK", ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeOk);
        alert.showAndWait();
    }
    
    protected void invalidCategoryAlert(String newString, int otherMatch)
    {
        Alert alert = new Alert(AlertType.ERROR);
        alert.getDialogPane().setId("invalidCategoryAlert");
        alert.getDialogPane().lookupButton(ButtonType.OK).setId("invalidCategoryAlertOkButton");
        alert.setTitle("Edit Component Error");
        alert.setHeaderText("Invalid Category Name.");
        alert.setContentText("The Category name must be unique." + System.lineSeparator() +  "The name \"" + newString + "\" matches category #" + otherMatch);
        ButtonType buttonTypeOk = new ButtonType("OK", ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeOk);
        alert.showAndWait();
    }
    
    protected static void loadFxml(URL fxmlFile, Object rootController)
    {
        FXMLLoader loader = new FXMLLoader(fxmlFile);
        loader.setController(rootController);
        loader.setRoot(rootController);
        loader.setResources(Settings.resources);
        try {
            loader.load();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
}
 
