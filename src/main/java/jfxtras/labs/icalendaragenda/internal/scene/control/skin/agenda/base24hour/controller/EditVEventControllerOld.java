package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.controller;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.List;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.scene.control.agenda.Agenda.AppointmentGroup;

@Deprecated
public class EditVEventControllerOld extends EditDisplayableComponentControllerOld<VEvent>
{

    @FXML private EditDisplayableComponentControllerOld<VEvent> displayableController;
//    private VEvent vEventEditedCopy;

    @Override
    @FXML public void initialize()
    {
    }
    
//    @Override
    @Override
    public void setupData(
            Appointment appointment,
            VEvent vComponent,
            Collection<Appointment> appointments,
            List<VEvent> vComponents,
            List<AppointmentGroup> appointmentGroups,
            Stage popup)
    {
        
        // Convert duration to date/time end - this controller can't handle VEvents with duration
        if (vComponent.getDuration() != null)
        {
            Temporal end = vComponent.getDateTimeStart().getValue().plus(vComponent.getDuration().getValue());
            vComponent.setDuration((Duration) null);
            vComponent.setDateTimeEnd(end);
        }
        
        // Copy original VEvent
//        setVComponent(vComponent);
//        vEventEditedCopy = new VEvent(vComponent);
//System.out.println(vComponent + " " + descriptionTextArea);
//        descriptionTextArea.textProperty().bindBidirectional(vComponent.getDescription().valueProperty());
//        if (vComponent.getLocation() == null)
//        {
//            vComponent.withLocation("");
//        }
//        locationTextField.textProperty().bindBidirectional(vComponent.getLocation().valueProperty());
        
        super.setupData(appointment,
                vComponent,
                appointments,
                vComponents,
                appointmentGroups,
                popup);
    }
    
//    @Override
    @Override
    void handleWholeDayChange(VEvent vComponent, Boolean newSelection)
    {
        super.handleWholeDayChange(vComponent, newSelection);
        if (newSelection)
        {
            LocalDate newDateTimeEnd = LocalDate.from(vComponent.getDateTimeEnd().getValue()).plus(1, ChronoUnit.DAYS);
            vComponent.setDateTimeEnd(newDateTimeEnd);
            LocalDateTime end = LocalDate.from(endTextField.getLocalDateTime()).plus(1, ChronoUnit.DAYS).atStartOfDay();
            endTextField.setLocalDateTime(end);
        } else
        {
            LocalDateTime end = startTextField.getLocalDateTime().plus(lastDuration);
            endTextField.setLocalDateTime(end);
            Temporal newDateTimeEnd = vComponent.getDateTimeStart().getValue().plus(lastDuration);
            vComponent.setDateTimeEnd(newDateTimeEnd);
        }
    }

}
