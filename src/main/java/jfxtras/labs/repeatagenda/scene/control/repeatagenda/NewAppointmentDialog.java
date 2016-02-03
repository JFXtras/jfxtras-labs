package jfxtras.labs.repeatagenda.scene.control.repeatagenda;

import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.util.Callback;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarAgenda.AppointmentGroupImpl;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.scene.control.agenda.Agenda.AppointmentGroup;

/**
 * Produces new appointment dialog
 * allows changing summary and appointmentGroup (category)
 * has buttons to create event, cancel, or do advanced edit
 * 
 * @author David Bal
 *
 */
public class NewAppointmentDialog extends Dialog<Appointment>
{    
    NewAppointmentDialog(Appointment appointment, ObservableList<AppointmentGroup> appointmentGroups, ResourceBundle resources)
    {
//        getDialogPane().getStylesheets().add(ICalendarAgenda.AGENDA_STYLE_SHEET);
        initModality(Modality.APPLICATION_MODAL);
        setTitle(resources.getString("dialog.event.new.title"));
        setHeaderText(resources.getString("dialog.event.new.header"));
        appointmentGroups.get(5).setDescription("test description");
        
        // Buttons
        ButtonType createButton = new ButtonType(resources.getString("dialog.event.new.create"), ButtonData.OK_DONE);
        ButtonType editButton = new ButtonType(resources.getString("dialog.event.new.edit"), ButtonData.OTHER);
        getDialogPane().getButtonTypes().addAll(createButton, editButton, ButtonType.CANCEL);
        
        // Edit Summary and appointmentGroup
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField summary = new TextField();
        summary.setPromptText(resources.getString("new.event"));
        ComboBox<AppointmentGroup> comboBox = new ComboBox<>();
        comboBox.setItems(appointmentGroups);
        Callback<ListView<AppointmentGroup>, ListCell<AppointmentGroup>> cellFactory = p -> new ListCell<AppointmentGroup>()
        {
            private final Rectangle rectangle;
            {
//                setContentDisplay(ContentDisplay.GRAPHIC_ONLY); 
                rectangle = new Rectangle(10, 10);
            }
            
            @Override protected void updateItem(AppointmentGroup item, boolean empty)
            {
                super.updateItem(item, empty);                
                if (item == null || empty) {
                    setGraphic(null);
                } else {
                    AppointmentGroupImpl item2 = (AppointmentGroupImpl) item;
                    rectangle.setFill(Color.BLUE);
                    setGraphic(item2.getIcon());
//                    setGraphic(rectangle);
                    setText(item.getDescription());
                }
            }
        };
        comboBox.setCellFactory(cellFactory);
//        ListView<AppointmentGroup> a;
        comboBox.setButtonCell(cellFactory.call(null));

        grid.add(new Label(resources.getString("summary")), 0, 0);
        grid.add(summary, 1, 0);
        grid.add(new Label("Category:"), 0, 1);
        grid.add(comboBox, 1, 1);
        
        getDialogPane().setContent(grid);

        // Request focus on the summary field by default.
        Platform.runLater(() -> summary.requestFocus());
    }
}
