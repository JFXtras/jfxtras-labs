package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import jfxtras.labs.icalendarfx.components.VComponentDisplayable;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.scene.control.agenda.Agenda.AppointmentGroup;

/** 
 * Makes a TabPane for editing a VEvent, VTodo or VJournal - to be specified by type
 * T in a subclass
 * 
 * @author David Bal
 */
public abstract class EditDisplayableTabPane<T extends VComponentDisplayable<?>> extends TabPane
{
    DescriptiveVBox<T> editDescriptiveVBox;
//    void setDescriptiveVBox(DescriptiveVBox<T> descriptiveVBox) { this.editDescriptiveVBox = descriptiveVBox; }
//    DescriptiveVBox<T> getDescriptiveVBox() { return editDescriptiveVBox; }

    @FXML private ResourceBundle resources; // ResourceBundle that was given to the FXMLLoader
    @FXML private AnchorPane descriptiveAnchorPane;
    AnchorPane getDescriptiveAnchorPane() { return descriptiveAnchorPane; }
    @FXML private TabPane editDisplayableTabPane;
    @FXML private Tab descriptiveTab;
    @FXML private Tab recurrenceRuleTab;
    @FXML private RecurrenceRuleVBox recurrenceRuleVBox;
    
    // Becomes true when pane should be closed
    ObjectProperty<Boolean> isFinished = new SimpleObjectProperty<>(false);
    public ObjectProperty<Boolean> isFinished() { return isFinished; }
    
    public EditDisplayableTabPane( )
    {
        super();
        loadFxml(DescriptiveVBox.class.getResource("view/EditDisplayable.fxml"), this);
//        System.out.println("editDescriptive:" + editDescriptive);
//        editVEventDescriptive = new EditVEventDescriptiveVBox();
//        descriptiveAnchorPane.getChildren().add(getEditDescriptive());
    }
    
    // Checks to see if start date has been changed, and a date shift is required, and then runs ordinary handleSave method.
    @FXML private void handleRepeatSave()
    {
        editDescriptiveVBox.handleSave();
    }
    
    @FXML private void handleCancelButton()
    {
////        vEventOriginal.copyTo(vEvent);
//        vComponent.copyComponentFrom(vEventOriginal);
//        popup.close();
    }
    
    public void setupData(
            Appointment appointment,
            T vComponent,
            List<T> vComponents,
            List<AppointmentGroup> appointmentGroups
//            Stage stage
            )
    {
        editDescriptiveVBox.setupData(appointment, vComponent, vComponents, appointmentGroups);
        // recurrences can't add repeat rules (only parent can have repeat rules)
        if (vComponent.getRecurrenceDates() != null)
        {
            recurrenceRuleTab.setDisable(true);
            recurrenceRuleTab.setTooltip(new Tooltip(resources.getString("repeat.tab.unavailable")));
        }
        recurrenceRuleVBox.setupData(vComponent, editDescriptiveVBox.startRecurrenceProperty);
        
        // When Appointment tab is selected make sure start and end times are valid, adjust if not
        editDisplayableTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
        {
            if (newValue == descriptiveTab)
            {
                Runnable alertRunnable = editDescriptiveVBox.validateStartRecurrence();
                if (alertRunnable != null)
                {
                    Platform.runLater(alertRunnable); // display alert after tab change refresh
                }
            }
        });
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
 
