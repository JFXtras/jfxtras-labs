package jfxtras.labs.repeatagenda.scene.control.repeatagenda;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.Repeat.Frequency;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableUtilities.RepeatChange;


public final class Settings {
    
    private Settings() {}

    public final static Path APPOINTMENTS_FILE = Paths.get("src/jfxtras.labs.samples.repeatagenda.appointments.xml");
    public final static Path APPOINTMENT_GROUPS_FILE = Paths.get("data/appointments/appointmentGroups.xml");
    public final static Path APPOINTMENT_REPEATS_FILE = Paths.get("appointmentRepeats.xml");
    
    public static DateTimeFormatter DATE_FORMAT1; // format for output files
    public static DateTimeFormatter DATE_FORMAT2; // fancy format for displaying
    public static DateTimeFormatter DATE_FORMAT_AGENDA;
    public static DateTimeFormatter DATE_FORMAT_AGENDA_START;
    public static DateTimeFormatter DATE_FORMAT_AGENDA_END;
    public static DateTimeFormatter TIME_FORMAT_AGENDA;
    public static final boolean PRETTY_XML = true;  // true for readable indented XML output, false for small files

    public static final Map<Frequency, String> REPEAT_FREQUENCIES = new HashMap<Frequency, String>();
    public static final Map<Frequency, String> REPEAT_FREQUENCIES_PLURAL = new HashMap<Frequency, String>();
    public static final Map<Frequency, String> REPEAT_FREQUENCIES_SINGULAR = new HashMap<Frequency, String>();
    public static final Map<RepeatChange, String> REPEAT_CHANGE_CHOICES = new LinkedHashMap<RepeatChange, String>();

    public static ResourceBundle resources;
   
    public static void setup(ResourceBundle resourcesIn)
    {
        resources = resourcesIn;
        
        DATE_FORMAT1 = DateTimeFormatter.ofPattern(resourcesIn.getString("date.format1"));
        DATE_FORMAT2 = DateTimeFormatter.ofPattern(resourcesIn.getString("date.format2"));
        DATE_FORMAT_AGENDA = DateTimeFormatter.ofPattern(resourcesIn.getString("date.format.agenda"));
        DATE_FORMAT_AGENDA_START = DateTimeFormatter.ofPattern(resourcesIn.getString("date.format.agenda.start"));
        DATE_FORMAT_AGENDA_END = DateTimeFormatter.ofPattern(resourcesIn.getString("date.format.agenda.end"));
        TIME_FORMAT_AGENDA = DateTimeFormatter.ofPattern(resourcesIn.getString("time.format.agenda"));
        
        REPEAT_FREQUENCIES.put(Frequency.DAILY, resourcesIn.getString("daily"));
        REPEAT_FREQUENCIES.put(Frequency.WEEKLY, resourcesIn.getString("weekly"));
        REPEAT_FREQUENCIES.put(Frequency.MONTHLY, resourcesIn.getString("monthly"));
        REPEAT_FREQUENCIES.put(Frequency.YEARLY, resourcesIn.getString("yearly"));
        
        REPEAT_FREQUENCIES_PLURAL.put(Frequency.DAILY, resourcesIn.getString("days"));
        REPEAT_FREQUENCIES_PLURAL.put(Frequency.WEEKLY, resourcesIn.getString("weeks"));
        REPEAT_FREQUENCIES_PLURAL.put(Frequency.MONTHLY, resourcesIn.getString("months"));
        REPEAT_FREQUENCIES_PLURAL.put(Frequency.YEARLY, resourcesIn.getString("years"));
        
        REPEAT_FREQUENCIES_SINGULAR.put(Frequency.DAILY, resourcesIn.getString("day"));
        REPEAT_FREQUENCIES_SINGULAR.put(Frequency.WEEKLY, resourcesIn.getString("week"));
        REPEAT_FREQUENCIES_SINGULAR.put(Frequency.MONTHLY, resourcesIn.getString("month"));
        REPEAT_FREQUENCIES_SINGULAR.put(Frequency.YEARLY, resourcesIn.getString("year"));

        REPEAT_CHANGE_CHOICES.put(RepeatableUtilities.RepeatChange.ONE, resources.getString("dialog.repeat.change.one"));
        REPEAT_CHANGE_CHOICES.put(RepeatableUtilities.RepeatChange.ALL, resources.getString("dialog.repeat.change.all"));
        REPEAT_CHANGE_CHOICES.put(RepeatableUtilities.RepeatChange.FUTURE, resources.getString("dialog.repeat.change.future"));
        
    }

}
