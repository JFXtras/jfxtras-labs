package jfxtras.labs.icalendaragenda;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.util.Locale;

import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.EditChoiceDialog;
import jfxtras.labs.icalendaragenda.scene.control.agenda.ICalendarAgenda;
import jfxtras.labs.icalendaragenda.scene.control.agenda.ICalendarAgendaUtilities;
import jfxtras.labs.icalendaragenda.scene.control.agenda.RecurrenceHelper.CallbackTwoParameters;
import jfxtras.labs.icalendarfx.VCalendar;
import jfxtras.labs.icalendarfx.components.VComponentLocatable;
import jfxtras.labs.icalendarfx.components.VComponentRepeatable;
import jfxtras.labs.icalendarfx.components.VJournal;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.test.JFXtrasGuiTest;

public class AgendaTestAbstract extends JFXtrasGuiTest
{
//    final protected Map<String, Agenda.AppointmentGroup> appointmentGroupMap = new TreeMap<String, Agenda.AppointmentGroup>();
    public static LocalDateTime dateTimeStamp;
    private VCalendar calendar = new VCalendar();
    
    @Override
    public Parent getRootNode()
    {
        Locale.setDefault(Locale.ENGLISH);
        
        vbox = new VBox();

        // setup appointment groups       
        agenda = new ICalendarAgenda(calendar);
        agenda.setDisplayedLocalDateTime(LocalDate.of(2015, 11, 8).atStartOfDay());
//        agenda.setDisplayedLocalDateTime(LocalDate.of(2014, 1, 1).atStartOfDay());
        agenda.setPrefSize(1000, 800);
        agenda.appointmentGroups().clear();
        agenda.appointmentGroups().addAll(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS);
        agenda.setOneAllThisAndFutureDialogCallback(EditChoiceDialog.EDIT_DIALOG_CALLBACK);
        
//        for (Agenda.AppointmentGroup lAppointmentGroup : agenda.appointmentGroups()) {
//            appointmentGroupMap.put(lAppointmentGroup.getDescription(), lAppointmentGroup);
//        }
        
        // accept new appointments
        agenda.newAppointmentCallbackProperty().set(new Callback<Agenda.LocalDateTimeRange, Agenda.Appointment>()
        {
            @Override
            public Agenda.Appointment call(ICalendarAgenda.LocalDateTimeRange dateTimeRange)
            {
                return new Agenda.AppointmentImplTemporal()
                        .withStartTemporal( dateTimeRange.getStartLocalDateTime().atZone(ZoneId.systemDefault()))
                        .withEndTemporal( dateTimeRange.getEndLocalDateTime().atZone(ZoneId.systemDefault()))
                        .withSummary("New")
                        .withDescription("")
                        .withAppointmentGroup(agenda.appointmentGroups().get(0));
//                Appointment appointment = new Agenda.AppointmentImplLocal()
//                        .withStartLocalDateTime( dateTimeRange.getStartLocalDateTime())
//                        .withEndLocalDateTime( dateTimeRange.getEndLocalDateTime())
//                        .withSummary("New")
//                        .withDescription("")
////                        .withAppointmentGroup(appointmentGroupMap.get("group00"));
//                        .withAppointmentGroup(agenda.appointmentGroups().get(0));
//                return appointment;
            }
        });
        
        // override default UID generator callback 
        agenda.setUidGeneratorCallback((Void) ->
        {
            String dateTime = DateTimeUtilities.LOCAL_DATE_TIME_FORMATTER.format(LocalDateTime.of(2015, 11, 8, 0, 0));
            String domain = "jfxtras.org";
            return dateTime + "-" + "0" + domain;
        });
                
        vbox.getChildren().add(agenda);
        return vbox;
    }
    
    protected VBox vbox = null; // cannot make this final and assign upon construction
//    final protected Map<String, Agenda.AppointmentGroup> appointmentGroupMap = new TreeMap<String, Agenda.AppointmentGroup>();
    protected ICalendarAgenda agenda = null; // cannot make this final and assign upon construction

    /** Callback to make appointment from VComponent and Temporal */
    public static final CallbackTwoParameters<VComponentRepeatable<?>, Temporal, Appointment> MAKE_APPOINTMENT_TEST_CALLBACK_LOCATABLE = (vComponentEdited, startTemporal) ->
    {
        Boolean isWholeDay = vComponentEdited.getDateTimeStart().getValue() instanceof LocalDate;
        VComponentLocatable<?> vComponentLocatable = (VComponentLocatable<?>) vComponentEdited;
        final TemporalAmount adjustment = vComponentLocatable.getActualDuration();
        Temporal endTemporal = startTemporal.plus(adjustment);

        // Make appointment
        Appointment appt = new Agenda.AppointmentImplTemporal()
                .withStartTemporal(startTemporal)
                .withEndTemporal(endTemporal)
                .withDescription( (vComponentLocatable.getDescription() != null) ? vComponentLocatable.getDescription().getValue() : null )
                .withSummary( (vComponentLocatable.getSummary() != null) ? vComponentLocatable.getSummary().getValue() : null)
                .withLocation( (vComponentLocatable.getLocation() != null) ? vComponentLocatable.getLocation().getValue() : null)
                .withWholeDay(isWholeDay);
        return appt;
    };
    
    /** Callback to make appointment from VComponent and Temporal */
    public static final CallbackTwoParameters<VComponentRepeatable<?>, Temporal, Appointment> MAKE_APPOINTMENT_TEST_CALLBACK_VJOURNAL = (vComponentEdited, startTemporal) ->
    {
        Boolean isWholeDay = vComponentEdited.getDateTimeStart().getValue() instanceof LocalDate;
        VJournal vComponent = (VJournal) vComponentEdited;
//        final TemporalAmount adjustment = vComponentLocatable.getActualDuration();
//        Temporal endTemporal = startTemporal.plus(adjustment);

        // Make appointment
        Appointment appt = new Agenda.AppointmentImplTemporal()
                .withStartTemporal(startTemporal)
//                .withEndTemporal(endTemporal)
                .withDescription( (vComponent.getDescriptions() != null) ? vComponent.getDescriptions().get(0).getValue() : null )
                .withSummary( (vComponent.getSummary() != null) ? vComponent.getSummary().getValue() : null)
                .withWholeDay(isWholeDay);
        return appt;
    };
}
