package jfxtras.labs.repeatagenda;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarAgenda;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VComponent;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.scene.control.agenda.Agenda.AppointmentGroup;
import jfxtras.test.JFXtrasGuiTest;

public class ICalendarAgendaTestAbstract extends JFXtrasGuiTest
{
    
    public Parent getRootNode()
    {
        Locale.setDefault(Locale.ENGLISH);
        
        vbox = new VBox();

        agenda = new ICalendarAgenda();
        agenda.setDisplayedLocalDateTime(LocalDate.of(2014, 1, 1).atStartOfDay());
        agenda.setPrefSize(1000, 800);
        
        final ObservableList<AppointmentGroup> DEFAULT_APPOINTMENT_GROUPS
        = javafx.collections.FXCollections.observableArrayList(
                IntStream
                .range(0, 24)
                .mapToObj(i -> new ICalendarAgenda.AppointmentGroupImpl()
                       .withStyleClass("group" + i)
//                       .withKey(i)
                       .withDescription("group" + (i < 10 ? "0" : "") + i))
                .collect(Collectors.toList()));
        
        agenda.appointmentGroups().clear();
        agenda.appointmentGroups().addAll(DEFAULT_APPOINTMENT_GROUPS);
        
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
                        .withAppointmentGroup(agenda.appointmentGroups().get(0));
                return appointment;
            }
        });
        
        // override default UID generator callback 
        agenda.setUidGeneratorCallback((Void) ->
        {
            String dateTime = VComponent.DATE_TIME_FORMATTER.format(LocalDateTime.of(2014, 1, 1, 0, 0));
            String domain = "jfxtras.org";
            return dateTime + "-" + "0" + domain;
        });
                
        vbox.getChildren().add(agenda);
        return vbox;
    }
    
    // Causes Not on FX application thread; currentThread = main exception - how to fix?
    public void shiftOneWeek()
    {
        LocalDateTime date = agenda.getDisplayedLocalDateTime();
        agenda.setDisplayedLocalDateTime(date.plusWeeks(1));
    }
    protected VBox vbox = null; // cannot make this final and assign upon construction
//    final protected Map<String, Agenda.AppointmentGroup> appointmentGroupMap = new TreeMap<String, Agenda.AppointmentGroup>();
    protected ICalendarAgenda agenda = null; // cannot make this final and assign upon construction

}
