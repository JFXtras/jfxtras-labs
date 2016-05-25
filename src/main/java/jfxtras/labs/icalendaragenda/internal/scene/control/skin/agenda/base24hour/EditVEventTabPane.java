package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.controller.EditDisplayableComponentController;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.scene.control.agenda.Agenda.AppointmentGroup;

/** Makes new stage for popup window to edit VEvent with Agenda.Appointment instances
 * 
 * @author David Bal
 * @see EditDisplayableComponentController
 */
public class EditVEventTabPane extends TabPane
{
    @FXML private EditVEventDescriptiveVBox editVEventDescriptive;
    @FXML private TabPane editDisplayableTabPane;
    @FXML private Tab descriptiveTab;
    @FXML private Tab recurrenceRuleTab;
    
    public EditVEventTabPane( )
    {
        super();
        loadFxml(EditDescriptiveVBox.class.getResource("view/EditDisplayable.fxml"), this);
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
            VEvent vComponent,
            List<VEvent> vComponents,
            List<AppointmentGroup> appointmentGroups)
    {
        editVEventDescriptive.setupData(appointment, vComponent, vComponents, appointmentGroups);
        
        // When Appointment tab is selected make sure start and end times are valid, adjust if not
        editDisplayableTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
        {
            if (newValue == descriptiveTab)
            {
                Runnable alertRunnable = editVEventDescriptive.validateStartRecurrence();
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
 
