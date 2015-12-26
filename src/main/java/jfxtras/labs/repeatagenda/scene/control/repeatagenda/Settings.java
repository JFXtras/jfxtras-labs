package jfxtras.labs.repeatagenda.scene.control.repeatagenda;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarUtilities.ChangeDialogOptions;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.freq.Frequency.FrequencyType;


public final class Settings
{
    private Settings() {}

    public final static Path APPOINTMENTS_FILE = Paths.get("src/jfxtras.labs.samples.repeatagenda.appointments.xml");
    public final static Path APPOINTMENT_GROUPS_FILE = Paths.get("data/appointments/appointmentGroups.xml");
    public final static Path APPOINTMENT_REPEATS_FILE = Paths.get("appointmentRepeats.xml");
    
    public static DateTimeFormatter DATE_FORMAT1; // format for output files
    public static DateTimeFormatter DATE_FORMAT2; // fancy format for displaying
    public static DateTimeFormatter DATE_FORMAT_AGENDA;
    public static DateTimeFormatter DATE_FORMAT_AGENDA_EXCEPTION;
    public static DateTimeFormatter DATE_FORMAT_AGENDA_DATEONLY;
    public static DateTimeFormatter DATE_FORMAT_AGENDA_MONTHDAY; // for yearly summary
    public static DateTimeFormatter DATE_FORMAT_AGENDA_START;
    public static DateTimeFormatter DATE_FORMAT_AGENDA_END;
    public static DateTimeFormatter TIME_FORMAT_AGENDA;
    public static final boolean PRETTY_XML = true;  // true for readable indented XML output, false for small files

    public static final Map<FrequencyType, String> REPEAT_FREQUENCIES = new HashMap<>();
    public static final Map<FrequencyType, String> REPEAT_FREQUENCIES_PLURAL = new HashMap<>();
    public static final Map<FrequencyType, String> REPEAT_FREQUENCIES_SINGULAR = new HashMap<>();
    public static final Map<ChangeDialogOptions, String> REPEAT_CHANGE_CHOICES = new LinkedHashMap<>();

    public static final Map<Integer, String> ORDINALS = new HashMap<>();

    public static final Map<DayOfWeek, String> DAYS_OF_WEEK = new HashMap<>();

    public static ResourceBundle resources;
   
    public static void setup(ResourceBundle resourcesIn)
    {
        resources = resourcesIn;
        
        DATE_FORMAT1 = DateTimeFormatter.ofPattern(resourcesIn.getString("date.format1"));
        DATE_FORMAT2 = DateTimeFormatter.ofPattern(resourcesIn.getString("date.format2"));
        DATE_FORMAT_AGENDA = DateTimeFormatter.ofPattern(resourcesIn.getString("date.format.agenda"));
        DATE_FORMAT_AGENDA_EXCEPTION = DateTimeFormatter.ofPattern(resourcesIn.getString("date.format.agenda.exception"));
        DATE_FORMAT_AGENDA_DATEONLY = DateTimeFormatter.ofPattern(resourcesIn.getString("date.format.agenda.dateonly"));
        DATE_FORMAT_AGENDA_MONTHDAY = DateTimeFormatter.ofPattern(resourcesIn.getString("date.format.agenda.monthday"));
        DATE_FORMAT_AGENDA_START = DateTimeFormatter.ofPattern(resourcesIn.getString("date.format.agenda.start"));
        DATE_FORMAT_AGENDA_END = DateTimeFormatter.ofPattern(resourcesIn.getString("date.format.agenda.end"));
        TIME_FORMAT_AGENDA = DateTimeFormatter.ofPattern(resourcesIn.getString("time.format.agenda"));
        
        REPEAT_FREQUENCIES.put(FrequencyType.DAILY, resourcesIn.getString("daily"));
        REPEAT_FREQUENCIES.put(FrequencyType.WEEKLY, resourcesIn.getString("weekly"));
        REPEAT_FREQUENCIES.put(FrequencyType.MONTHLY, resourcesIn.getString("monthly"));
        REPEAT_FREQUENCIES.put(FrequencyType.YEARLY, resourcesIn.getString("yearly"));
        
        REPEAT_FREQUENCIES_PLURAL.put(FrequencyType.DAILY, resourcesIn.getString("days"));
        REPEAT_FREQUENCIES_PLURAL.put(FrequencyType.WEEKLY, resourcesIn.getString("weeks"));
        REPEAT_FREQUENCIES_PLURAL.put(FrequencyType.MONTHLY, resourcesIn.getString("months"));
        REPEAT_FREQUENCIES_PLURAL.put(FrequencyType.YEARLY, resourcesIn.getString("years"));
        
        REPEAT_FREQUENCIES_SINGULAR.put(FrequencyType.DAILY, resourcesIn.getString("day"));
        REPEAT_FREQUENCIES_SINGULAR.put(FrequencyType.WEEKLY, resourcesIn.getString("week"));
        REPEAT_FREQUENCIES_SINGULAR.put(FrequencyType.MONTHLY, resourcesIn.getString("month"));
        REPEAT_FREQUENCIES_SINGULAR.put(FrequencyType.YEARLY, resourcesIn.getString("year"));

        REPEAT_CHANGE_CHOICES.put(ChangeDialogOptions.ONE, resources.getString("dialog.repeat.change.one"));
        REPEAT_CHANGE_CHOICES.put(ChangeDialogOptions.ALL, resources.getString("dialog.repeat.change.all"));
        REPEAT_CHANGE_CHOICES.put(ChangeDialogOptions.THIS_AND_FUTURE, resources.getString("dialog.repeat.change.future"));
        
        ORDINALS.put(1, resourcesIn.getString("first"));
        ORDINALS.put(2, resourcesIn.getString("second"));
        ORDINALS.put(3, resourcesIn.getString("third"));
        ORDINALS.put(4, resourcesIn.getString("fourth"));
        ORDINALS.put(5, resourcesIn.getString("fifth"));

        DAYS_OF_WEEK.put(DayOfWeek.MONDAY, resourcesIn.getString("monday"));
        DAYS_OF_WEEK.put(DayOfWeek.TUESDAY, resourcesIn.getString("tuesday"));
        DAYS_OF_WEEK.put(DayOfWeek.WEDNESDAY, resourcesIn.getString("wednesday"));
        DAYS_OF_WEEK.put(DayOfWeek.THURSDAY, resourcesIn.getString("thursday"));
        DAYS_OF_WEEK.put(DayOfWeek.FRIDAY, resourcesIn.getString("friday"));
        DAYS_OF_WEEK.put(DayOfWeek.SATURDAY, resourcesIn.getString("saturday"));
        DAYS_OF_WEEK.put(DayOfWeek.SUNDAY, resourcesIn.getString("sunday"));
    }

}
