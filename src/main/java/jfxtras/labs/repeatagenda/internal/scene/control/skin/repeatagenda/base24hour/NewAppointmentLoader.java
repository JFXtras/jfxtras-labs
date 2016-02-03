package jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour;

import java.io.IOException;
import java.util.Collection;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Popup;
import jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour.controller.NewAppointmentController;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.Settings;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.util.NodeUtil;

public class NewAppointmentLoader extends Popup {

    private AnchorPane selectOne;
    
    public NewAppointmentLoader(
              Agenda agenda
            , Appointment appointment
            , Collection<Appointment> appointments)
    {
        // LOAD FXML
        FXMLLoader simpleAppointmentEditLoader = new FXMLLoader();
        simpleAppointmentEditLoader.setLocation(NewAppointmentLoader.class.getResource("view/NewAppointment.fxml"));
        simpleAppointmentEditLoader.setResources(Settings.resources);
        try {
            selectOne = simpleAppointmentEditLoader.load();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        NewAppointmentController popupController = simpleAppointmentEditLoader.getController();
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
