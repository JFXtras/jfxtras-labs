package jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour;

import java.io.IOException;
import java.util.Collection;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Popup;
import jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour.controller.LittlePopupController;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.Settings;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.util.NodeUtil;

/**
 * Default little popup that opens when clicking on one appointment.
 * allows editing summary and buttons to open edit popup and delete
 */
public class LittlePopupLoader extends Popup {

    private AnchorPane appointmentManage;
    
    public LittlePopupLoader(
              Appointment appointment
            , Collection<Appointment> appointments
            , Node node)
    {
        
        // LOAD FXML
        FXMLLoader appointmentManageLoader = new FXMLLoader();
        appointmentManageLoader.setLocation(LittlePopupLoader.class.getResource("view/AppointmentPopup.fxml"));
        appointmentManageLoader.setResources(Settings.resources);
        try {
            appointmentManage = appointmentManageLoader.load();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        LittlePopupController popupController = appointmentManageLoader.getController();
        popupController.setupData(appointment, appointments, this);
//        AppointmentManageController.setupData(appointment, layoutHelp, newStage);
//        Scene scene2 = new Scene(appointmentMenuPane);
        
        System.out.println("sizes " + NodeUtil.screenY(node));// + " " + node.getHeight());

        
        this.setAutoFix(true);
        this.setAutoHide(true);
        this.setHideOnEscape(true);
        this.getContent().add(appointmentManage);
        
        this.setX(NodeUtil.screenX(node));
        this.setY(NodeUtil.screenY(node)); // - node.getHeight());
//        layoutHelp.skinnable.getUserAgentStylesheet();
        
//        appointmentManage.getStyleClass().add("Agenda" + "Popup");
        
        this.setOnHidden( (windowEvent) -> {
            System.out.println("close menage popup");
        });
        
        
    }
}
