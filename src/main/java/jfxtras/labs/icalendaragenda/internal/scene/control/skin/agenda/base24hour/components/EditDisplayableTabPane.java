package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.components;

import java.io.IOException;
import java.net.URL;
import java.time.temporal.Temporal;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.DeleteChoiceDialog;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.Settings;
import jfxtras.labs.icalendarfx.components.VComponentDisplayable;
import jfxtras.labs.icalendarfx.components.deleters.SimpleDeleterFactory;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Summary;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.FrequencyType;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.Interval;

/** 
 * Makes a TabPane for editing a VEvent, VTodo or VJournal - to be specified by type
 * T in a subclass
 * 
 * @author David Bal
 */
public abstract class EditDisplayableTabPane<T extends VComponentDisplayable<T>, U extends DescriptiveVBox<T>> extends TabPane
{
    U editDescriptiveVBox;
    RecurrenceRuleVBox<T> recurrenceRuleVBox;

    @FXML private ResourceBundle resources; // ResourceBundle that was given to the FXMLLoader
    @FXML AnchorPane descriptiveAnchorPane;
    @FXML AnchorPane recurrenceRuleAnchorPane;
    @FXML private TabPane editDisplayableTabPane;
    @FXML private Tab descriptiveTab;
    @FXML private Tab recurrenceRuleTab;

    // Becomes true when control should be closed
    ObjectProperty<Boolean> isFinished = new SimpleObjectProperty<>(false);
    public ObjectProperty<Boolean> isFinished() { return isFinished; }
    
    public EditDisplayableTabPane( )
    {
        super();
        loadFxml(DescriptiveVBox.class.getResource("EditDisplayable.fxml"), this);
    }
    
    @FXML
    void handleSaveButton()
    {
        removeEmptyProperties();
    }

    void removeEmptyProperties()
    {
        if (recurrenceRuleVBox.frequencyComboBox.getValue() == FrequencyType.WEEKLY && recurrenceRuleVBox.dayOfWeekList.isEmpty())
        {
            canNotHaveZeroDaysOfWeek();
        } else if (! vComponent.getRecurrenceRule().isValid())
        {
            throw new RuntimeException("Unhandled component error" + System.lineSeparator() + vComponent.errors());
        }
        
        if (editDescriptiveVBox.summaryTextField.getText().isEmpty())
        {
            vComponent.setSummary((Summary) null); 
        }

       // nullify Interval if value equals default (avoid unnecessary content output)
        if ((vComponent.getRecurrenceRule() != null) && (recurrenceRuleVBox.intervalSpinner.getValue() == Interval.DEFAULT_INTERVAL))
        {
            vComponent.getRecurrenceRule().getValue().setInterval((Interval) null); 
        }
    }
    
    @FXML private void handleCancelButton()
    {
        vComponent.copyChildrenFrom(vComponentOriginalCopy);
        removeEmptyProperties();
        isFinished.set(true);
    }
    
    @FXML private void handleDeleteButton()
    {
        removeEmptyProperties();
        Object[] params = new Object[] {
                DeleteChoiceDialog.DELETE_DIALOG_CALLBACK,
                editDescriptiveVBox.startOriginalRecurrence,
                vComponents
        };
        boolean result = SimpleDeleterFactory.newDeleter(vComponent, params).delete();
        isFinished.set(result);
    }
    
    @FXML private void handlePressEnter(KeyEvent e)
    {
        if (e.getCode().equals(KeyCode.ENTER))
        {
            handleSaveButton();
        }
    }
    
    T vComponent;
    T vComponentOriginalCopy;
    List<T> vComponents;

    public void setupData(
            T vComponent,
            List<T> vComponents,
            Temporal startRecurrence,
            Temporal endRecurrence,
            List<String> categories
            )
    {
        this.vComponent = vComponent;
        this.vComponents = vComponents;
        editDescriptiveVBox.setupData(vComponent, startRecurrence, endRecurrence, categories);
        
        /* 
         * Shut off repeat tab if vComponent is not a parent
         * Components with RECURRENCE-ID can't add repeat rules (only parent can have repeat rules)
         */
        if (vComponent.getRecurrenceId() != null)
        {
            recurrenceRuleTab.setDisable(true);
            recurrenceRuleTab.setTooltip(new Tooltip(resources.getString("repeat.tab.unavailable")));
        }
        recurrenceRuleVBox.setupData(vComponent, editDescriptiveVBox.startRecurrenceProperty);
    }
    
    // Displays an alert notifying at least one day of week must be present for weekly frequency
    private static void canNotHaveZeroDaysOfWeek()
    {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Invalid Modification");
        alert.setHeaderText("Please select at least one day of the week.");
        alert.setContentText("Weekly repeat must have at least one selected day");
        ButtonType buttonTypeOk = new ButtonType("OK", ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeOk);
        
        // set id for testing
        alert.getDialogPane().setId("zero_day_of_week_alert");
        alert.getDialogPane().lookupButton(buttonTypeOk).setId("zero_day_of_week_alert_button_ok");
        
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
 
