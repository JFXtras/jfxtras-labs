package jfxtras.labs.repeatagenda;

import java.time.LocalDate;
import java.util.Locale;

import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarAgenda;
import jfxtras.scene.control.agenda.Agenda;

public class ICalendarAgendaTestAbstract extends ICalendarTestAbstract
{

    public Parent getRootNode()
    {
        Locale.setDefault(Locale.ENGLISH);
        
        vbox = new VBox();

        agenda = new ICalendarAgenda();
        agenda.setDisplayedLocalDateTime(LocalDate.of(2014, 1, 1).atStartOfDay());
        agenda.setPrefSize(1000, 800);
        
        // setup appointment groups
//        for (Agenda.AppointmentGroup lAppointmentGroup : agenda.appointmentGroups()) {
//            appointmentGroupMap.put(lAppointmentGroup.getDescription(), lAppointmentGroup);
//        }

        // accept new appointments
        agenda.newAppointmentCallbackProperty().set(new Callback<Agenda.LocalDateTimeRange, Agenda.Appointment>()
        {
            @Override
            public Agenda.Appointment call(Agenda.LocalDateTimeRange calendarRange)
            {
                return new Agenda.AppointmentImplLocal()
                        .withStartLocalDateTime(calendarRange.getStartLocalDateTime())
                        .withEndLocalDateTime(calendarRange.getEndLocalDateTime())
                        .withSummary("new")
                        .withDescription("new")
                        .withAppointmentGroup(ICalendarTestAbstract.DEFAULT_APPOINTMENT_GROUPS.get(0));
            }
        });
        
        vbox.getChildren().add(agenda);
        return vbox;
    }
    protected VBox vbox = null; // cannot make this final and assign upon construction
//    final protected Map<String, Agenda.AppointmentGroup> appointmentGroupMap = new TreeMap<String, Agenda.AppointmentGroup>();
    protected ICalendarAgenda agenda = null; // cannot make this final and assign upon construction

}
