package jfxtras.labs.icalendaragenda.scene.control.agenda.behavior;

import javafx.scene.control.Control;
import javafx.scene.layout.Pane;
import jfxtras.internal.scene.control.skin.agenda.AgendaSkin;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.components.EditComponentPopupStage;
import jfxtras.labs.icalendaragenda.scene.control.agenda.ICalendarAgenda;
import jfxtras.labs.icalendarfx.components.VComponentDisplayable;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.util.NodeUtil;

public abstract class DisplayableBehavior<T extends VComponentDisplayable<T>> implements Behavior
{
    ICalendarAgenda agenda;
    private EditComponentPopupStage<?> editPopup;
    
    public DisplayableBehavior(ICalendarAgenda agenda) { this.agenda = agenda; }
    
    @Override
    public void iCalendarEditPopup(Appointment appointment)
    {
//        VComponentDisplayable<?> vComponent = agenda.appointmentVComponentMap.get(System.identityHashCode(appointment));
//        if (vComponent == null)
//        {
//            // NOTE: Can't throw exception here because in Agenda there is a mouse event that isn't consumed.
//            // Throwing an exception will leave the mouse unresponsive.
//            System.out.println("ERROR: no component found - popup can'b be displayed");
//        } else
//        {
//            appointments().removeListener(appointmentsListChangeListener); // remove listener to prevent making extra vEvents during edit
//            EditComponentPopupStage<?> editPopup = EditComponentPopupStage.editComponentPopupStageFactory(
//                    vComponent,
//                    agenda.getVCalendar(),
//                    appointment.getStartTemporal(),
//                    appointment.getEndTemporal(),
//                    agenda.getCategories()
//                    );
            EditComponentPopupStage<?> editPopup = editPopup(appointment);
    
            editPopup.getScene().getStylesheets().addAll(agenda.getUserAgentStylesheet(), ICalendarAgenda.ICALENDAR_STYLE_SHEET);
    
            // remove listeners during edit (to prevent creating extra vEvents when making appointments)
            editPopup.setOnShowing((windowEvent) -> agenda.appointments().removeListener(agenda.appointmentsListChangeListener));
            
            /* POSITION POPUP
             * Position popup to left or right of bodyPane, where there is room.
             * Note: assumes the control is displayed at its preferred height and width */
            Pane bodyPane = (Pane) ((AgendaSkin) agenda.getSkin()).getNodeForPopup(appointment);
            double prefHeightControl = ((Control) editPopup.getScene().getRoot()).getPrefHeight();
            double prefWidthControl = ((Control) editPopup.getScene().getRoot()).getPrefWidth();
            double xLeft = NodeUtil.screenX(bodyPane) - prefWidthControl - 5;
            double xRight = NodeUtil.screenX(bodyPane) + bodyPane.getWidth() + 5;
            double x = (xLeft > 0) ? xLeft : xRight;
            double y = NodeUtil.screenY(bodyPane) - prefHeightControl/2;
            editPopup.setX(x);
            editPopup.setY(y);
            editPopup.show();
            
            editPopup.getEditDisplayableTabPane().isFinished().addListener((obs) -> editPopup.hide());
            // return listener after edit
            editPopup.setOnHiding((windowEvent) ->  agenda.appointments().addListener(agenda.appointmentsListChangeListener));
        }
//    }

}
