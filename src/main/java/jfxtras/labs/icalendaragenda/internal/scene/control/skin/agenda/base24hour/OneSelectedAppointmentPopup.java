package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour;

import javafx.beans.property.BooleanProperty;
import javafx.stage.Popup;
import jfxtras.labs.icalendaragenda.scene.control.agenda.ICalendarAgenda;
import jfxtras.scene.control.agenda.Agenda.Appointment;

@Deprecated
public class OneSelectedAppointmentPopup extends Popup
{   
    public OneSelectedAppointmentPopup()
    {      
        setAutoFix(true);
        setAutoHide(true);
        setHideOnEscape(true);
    }

    public BooleanProperty isFinished()
    {
        return ((OneSelectedAppointmentVBox) getContent().get(0)).isFinished();
    }
    
    public void setupData(ICalendarAgenda agenda, Appointment appointment)
    {
        OneSelectedAppointmentVBox oneSelectedAppointmentVBox = (OneSelectedAppointmentVBox) getContent().get(0);
        oneSelectedAppointmentVBox.getStylesheets().add(agenda.getUserAgentStylesheet());
        oneSelectedAppointmentVBox.setupData(agenda, appointment);
        onShowingProperty().addListener((obs) -> oneSelectedAppointmentVBox.editAppointmentButton.requestFocus());
    }
}
