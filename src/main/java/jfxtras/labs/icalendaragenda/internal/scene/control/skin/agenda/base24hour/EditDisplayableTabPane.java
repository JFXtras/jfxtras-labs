package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
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
    protected DescriptiveVBox<T> editDescriptive;
    void setEditDescriptive(DescriptiveVBox<T> editDescriptive) { this.editDescriptive = editDescriptive; }
    DescriptiveVBox<T> getEditDescriptive() { return editDescriptive; }

    @FXML private AnchorPane descriptiveAnchorPane;
    AnchorPane getDescriptiveAnchorPane() { return descriptiveAnchorPane; }
    @FXML private TabPane editDisplayableTabPane;
    @FXML private Tab descriptiveTab;
    @FXML private Tab recurrenceRuleTab;
    
    public EditDisplayableTabPane( )
    {
        super();
        loadFxml(DescriptiveVBox.class.getResource("view/EditDisplayable.fxml"), this);
//        editVEventDescriptive = new EditVEventDescriptiveVBox();
//        descriptiveAnchorPane.getChildren().add(getEditDescriptive());
    }
    
    // Checks to see if start date has been changed, and a date shift is required, and then runs ordinary handleSave method.
    @FXML private void handleRepeatSave()
    {

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
            List<AppointmentGroup> appointmentGroups)
    {
        getEditDescriptive().setupData(appointment, vComponent, vComponents, appointmentGroups);
        
        // When Appointment tab is selected make sure start and end times are valid, adjust if not
        editDisplayableTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
        {
            if (newValue == descriptiveTab)
            {
                Runnable alertRunnable = getEditDescriptive().validateStartRecurrence();
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
 
