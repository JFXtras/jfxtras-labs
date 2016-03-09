package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Popup;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.controller.SelectedOneAppointmentController;
import jfxtras.labs.icalendaragenda.scene.control.agenda.ICalendarAgenda;
import jfxtras.scene.control.agenda.Agenda.Appointment;

/**
 * Default little popup that opens when clicking on one appointment.
 * allows editing summary and buttons to open edit popup and delete
 */
public class SelectedOneAppointmentLoader extends Popup {
    
    public SelectedOneAppointmentLoader(ICalendarAgenda agenda, Appointment appointment)
    {       
        // LOAD FXML
        FXMLLoader selectOneLoader = new FXMLLoader();
        selectOneLoader.setLocation(SelectedOneAppointmentLoader.class.getResource("view/SelectedOneAppointment.fxml"));
        selectOneLoader.setResources(Settings.resources);
        AnchorPane selectedOne = null;
        try {
            selectedOne = selectOneLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SelectedOneAppointmentController popupController = selectOneLoader.getController();
        popupController.setupData(agenda, appointment);
        
        setAutoFix(true);
        setAutoHide(true);
        setHideOnEscape(true);
        getContent().add(selectedOne);
        
//        setOnHidden( (windowEvent) -> {
//            System.out.println("close select one popup");
//        });
    }
}
