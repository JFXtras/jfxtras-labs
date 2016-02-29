package jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour;

import java.io.IOException;
import java.util.Collection;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Popup;
import jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour.controller.SelectOneController;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.util.NodeUtil;

/**
 * Default little popup that opens when clicking on one appointment.
 * allows editing summary and buttons to open edit popup and delete
 */
public class SelectOneLoader extends Popup {

    private AnchorPane selectOne;
    
    public SelectOneLoader(
              Agenda agenda
            , Appointment appointment
            , Collection<Appointment> appointments)
    {       
        // LOAD FXML
        FXMLLoader selectOneLoader = new FXMLLoader();
        selectOneLoader.setLocation(SelectOneLoader.class.getResource("view/SelectOne.fxml"));
        selectOneLoader.setResources(Settings.resources);
        try {
            selectOne = selectOneLoader.load();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        SelectOneController popupController = selectOneLoader.getController();
        popupController.setupData(agenda, appointment, appointments);
//        AppointmentManageController.setupData(appointment, layoutHelp, newStage);
//        Scene scene2 = new Scene(appointmentMenuPane);
        
        System.out.println("sizes " + NodeUtil.screenY(agenda));// + " " + node.getHeight());

        
        setAutoFix(true);
        setAutoHide(true);
        setHideOnEscape(true);
        getContent().add(selectOne);
        
//        setX(NodeUtil.screenX(agenda));
//        setY(NodeUtil.screenY(agenda)); // - node.getHeight());
//        layoutHelp.skinnable.getUserAgentStylesheet();
        
//        appointmentManage.getStyleClass().add("Agenda" + "Popup");
        
        setOnHidden( (windowEvent) -> {
            System.out.println("close select one popup");
        });
        
        
    }
}
