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
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.Settings;
import jfxtras.labs.icalendarfx.components.VComponentDisplayable;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Summary;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.Interval;

/** 
 * Makes a TabPane for editing a VEvent, VTodo or VJournal - to be specified by type
 * T in a subclass
 * 
 * @author David Bal
 */
public abstract class EditDisplayableTabPane<T extends VComponentDisplayable<?>, U extends DescriptiveVBox<T>> extends TabPane
{
    U editDescriptiveVBox;
    RecurrenceRuleVBox<T> recurrenceRuleVBox;
    
//    void setDescriptiveVBox(DescriptiveVBox<T> descriptiveVBox) { this.editDescriptiveVBox = descriptiveVBox; }
//    DescriptiveVBox<T> getDescriptiveVBox() { return editDescriptiveVBox; }

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
        if (editDescriptiveVBox.summaryTextField.getText().isEmpty())
        {
            vComponent.setSummary((Summary) null); 
        }
       // nullify Interval if value equals default (avoid unnecessary content output)
        if ((vComponent.getRecurrenceRule() != null) && (recurrenceRuleVBox.intervalSpinner.getValue() == Interval.DEFAULT_INTERVAL))
        {
            vComponent.getRecurrenceRule().getValue().setInterval((Interval) null); 
        }
        // additional functionality in subclasses
    }
    
    @FXML private void handleCancelButton()
    {
        System.out.println("handlecancel:");
        vComponent.copyComponentFrom(vComponentOriginalCopy);
        isFinished.set(true);
////        vEventOriginal.copyTo(vEvent);
//        vComponent.copyComponentFrom(vEventOriginal);
//        popup.close();
    }
    
    @FXML private void handleDeleteButton()
    {
        isFinished.set(true);
//        vEvent.handleDelete(
//                vComponents
//              , startRecurrence
//              , appointment
//              , appointments
//              , DeleteChoiceDialog.DELETE_DIALOG_CALLBACK);
//        popup.close();
    } 
    
    T vComponent;
    T vComponentOriginalCopy;
    List<T> vComponents;

    public void setupData(
//            Appointment appointment,
            T vComponent,
            List<T> vComponents,
            Temporal startRecurrence,
            Temporal endRecurrence,
            List<String> categories
//            List<AppointmentGroup> appointmentGroups
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
        
//        // When Appointment tab is selected make sure start and end times are valid, adjust if not
//        editDisplayableTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
//        {
//            if (newValue == descriptiveTab)
//            {
//                Runnable alertRunnable = editDescriptiveVBox.validateStartRecurrence();
//                if (alertRunnable != null)
//                {
//                    Platform.runLater(alertRunnable); // display alert after tab change refresh
//                }
//            }
//        });
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
 
