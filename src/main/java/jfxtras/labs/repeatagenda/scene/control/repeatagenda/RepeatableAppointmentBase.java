package jfxtras.labs.repeatagenda.scene.control.repeatagenda;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableAgenda.Appointment2;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.scene.control.agenda.Agenda.AppointmentGroup;

public abstract class RepeatableAppointmentBase implements Appointment2
{
    /** Summary: */
    public ObjectProperty<String> summaryProperty() { return summaryObjectProperty; }
    final private ObjectProperty<String> summaryObjectProperty = new SimpleObjectProperty<String>(this, "summary");
    public String getSummary() { return summaryObjectProperty.getValue(); }
    public void setSummary(String value) { summaryObjectProperty.setValue(value); }
    public Appointment withSummary(String value) { setSummary(value); return this; } 
    
    /** Description: */
    public ObjectProperty<String> descriptionProperty() { return descriptionObjectProperty; }
    final private ObjectProperty<String> descriptionObjectProperty = new SimpleObjectProperty<String>(this, "description");
    public String getDescription() { return descriptionObjectProperty.getValue(); }
    public void setDescription(String value) { descriptionObjectProperty.setValue(value); }
    public Appointment withDescription(String value) { setDescription(value); return this; } 
    
    /** Location: */
    public ObjectProperty<String> locationProperty() { return locationObjectProperty; }
    final private ObjectProperty<String> locationObjectProperty = new SimpleObjectProperty<String>(this, "location");
    public String getLocation() { return locationObjectProperty.getValue(); }
    public void setLocation(String value) { locationObjectProperty.setValue(value); }
    public Appointment withLocation(String value) { setLocation(value); return this; } 
    
    /** AppointmentGroup: */
    public ObjectProperty<AppointmentGroup> appointmentGroupProperty() { return appointmentGroupObjectProperty; }
    final private ObjectProperty<AppointmentGroup> appointmentGroupObjectProperty = new SimpleObjectProperty<AppointmentGroup>(this, "appointmentGroup");
    public AppointmentGroup getAppointmentGroup() { return appointmentGroupObjectProperty.getValue(); }
    public void setAppointmentGroup(AppointmentGroup value) { appointmentGroupObjectProperty.setValue(value); }
    public Appointment withAppointmentGroup(AppointmentGroup value) { setAppointmentGroup(value); return this; }
}
