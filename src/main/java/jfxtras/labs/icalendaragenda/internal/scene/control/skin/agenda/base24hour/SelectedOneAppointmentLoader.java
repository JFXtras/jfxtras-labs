package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Popup;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.controller.SelectedOneAppointmentController;
import jfxtras.labs.icalendaragenda.scene.control.agenda.ICalendarAgenda;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.util.NodeUtil;

/**
 * Default little popup that opens when clicking on one appointment.
 * allows editing summary and buttons to open edit popup and delete
 */
public class SelectedOneAppointmentLoader extends Popup {

    private AnchorPane selectedOne;
    
    public SelectedOneAppointmentLoader(ICalendarAgenda agenda, Appointment appointment)
    {       
        // LOAD FXML
        FXMLLoader selectOneLoader = new FXMLLoader();
        selectOneLoader.setLocation(SelectedOneAppointmentLoader.class.getResource("view/SelectedOneAppointment.fxml"));
        selectOneLoader.setResources(Settings.resources);
        try {
            selectedOne = selectOneLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SelectedOneAppointmentController popupController = selectOneLoader.getController();
        popupController.setupData(agenda, appointment);
        
        System.out.println("sizes " + NodeUtil.screenY(agenda));// + " " + node.getHeight());

        
        setAutoFix(true);
        setAutoHide(true);
        setHideOnEscape(true);
        getContent().add(selectedOne);
        
//        setX(NodeUtil.screenX(agenda));
//        setY(NodeUtil.screenY(agenda)); // - node.getHeight());
//        layoutHelp.skinnable.getUserAgentStylesheet();
        
//        appointmentManage.getStyleClass().add("Agenda" + "Popup");
        
        setOnHidden( (windowEvent) -> {
            System.out.println("close select one popup");
        });
        
        
    }
}
