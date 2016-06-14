package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.components.DescriptiveVBox;

public class OneSelectedAppointmentPopup extends Popup
{
    @FXML private ResourceBundle resources; // ResourceBundle that was given to the FXMLLoader

    @FXML private AnchorPane selectedOneAppointmentAnchorPane;
    @FXML private VBox vBox;
    @FXML private Button editAppointmentButton;
    @FXML private Button deleteAppointmentButton;
    @FXML private Button attendanceButton;
    @FXML private Label appointmentTimeLabel;
    @FXML private Label nameLabel;
    
    public OneSelectedAppointmentPopup( )
    {
        super();
        loadFxml(DescriptiveVBox.class.getResource("SelectedOneAppointment.fxml"), this);
        categorySelectionGridPane.getStylesheets().addAll(getStylesheets());
        startDateTimeTextField.setId("startDateTimeTextField");
        startDateTextField.setId("startDateTextField");
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
