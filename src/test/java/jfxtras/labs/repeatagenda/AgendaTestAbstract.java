package jfxtras.labs.repeatagenda;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;

import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarAgenda;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarAgendaUtilities;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VComponent;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.test.JFXtrasGuiTest;

public class AgendaTestAbstract extends JFXtrasGuiTest
{
//    final protected Map<String, Agenda.AppointmentGroup> appointmentGroupMap = new TreeMap<String, Agenda.AppointmentGroup>();
    public static LocalDateTime dateTimeStamp;
    
    @Override
    public Parent getRootNode()
    {
        Locale.setDefault(Locale.ENGLISH);
        
        vbox = new VBox();

        // setup appointment groups       
        agenda = new ICalendarAgenda();
        agenda.setDisplayedLocalDateTime(LocalDate.of(2015, 11, 8).atStartOfDay());
        agenda.setPrefSize(1000, 800);
        agenda.appointmentGroups().clear();
        agenda.appointmentGroups().addAll(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS);
        
//        for (Agenda.AppointmentGroup lAppointmentGroup : agenda.appointmentGroups()) {
//            appointmentGroupMap.put(lAppointmentGroup.getDescription(), lAppointmentGroup);
//        }
        
        // accept new appointments
        agenda.newAppointmentCallbackProperty().set(new Callback<Agenda.LocalDateTimeRange, Agenda.Appointment>()
        {
            @Override
            public Agenda.Appointment call(ICalendarAgenda.LocalDateTimeRange dateTimeRange)
            {
                Appointment appointment = new Agenda.AppointmentImplLocal()
                        .withStartLocalDateTime( dateTimeRange.getStartLocalDateTime())
                        .withEndLocalDateTime( dateTimeRange.getEndLocalDateTime())
                        .withSummary("New")
                        .withDescription("")
//                        .withAppointmentGroup(appointmentGroupMap.get("group00"));
                        .withAppointmentGroup(agenda.appointmentGroups().get(0));
                dateTimeStamp = appointment.getStartLocalDateTime();
                return appointment;
            }
        });
        
        // override default UID generator callback 
        agenda.setUidGeneratorCallback((Void) ->
        {
            String dateTime = VComponent.LOCAL_DATE_TIME_FORMATTER.format(LocalDateTime.of(2015, 11, 8, 0, 0));
            String domain = "jfxtras.org";
            return dateTime + "-" + "0" + domain;
        });
                
        vbox.getChildren().add(agenda);
        return vbox;
    }
    
    protected VBox vbox = null; // cannot make this final and assign upon construction
//    final protected Map<String, Agenda.AppointmentGroup> appointmentGroupMap = new TreeMap<String, Agenda.AppointmentGroup>();
    protected ICalendarAgenda agenda = null; // cannot make this final and assign upon construction

}
