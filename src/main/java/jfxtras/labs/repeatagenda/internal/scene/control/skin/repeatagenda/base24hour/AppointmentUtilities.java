package jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.Settings;
import jfxtras.scene.control.agenda.Agenda.Appointment;

public class AppointmentUtilities {

    private AppointmentUtilities() {}

    public static String makeAppointmentName(Appointment appointment) {
        return appointment.getSummary() + ": " + makeAppointmentTime(appointment);
    }
    
    public static String makeAppointmentTime(Appointment appointment) {
        
        String start = Settings.DATE_FORMAT_AGENDA_START.format(appointment.getStartLocalDateTime());
        String end = Settings.DATE_FORMAT_AGENDA_END.format(appointment.getEndLocalDateTime());
        return start + end + " ";
    }
}
