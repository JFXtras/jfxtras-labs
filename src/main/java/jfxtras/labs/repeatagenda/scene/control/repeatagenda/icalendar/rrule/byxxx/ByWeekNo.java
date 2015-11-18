package jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.byxxx;

import static java.time.temporal.ChronoUnit.WEEKS;

import java.security.InvalidParameterException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.freq.Frequency;

/** BYWEEKNO from RFC 5545, iCalendar 3.3.10, page 42 */
public class ByWeekNo extends ByRuleAbstract
{
    /** sorted array of weeks of the year
     * (i.e. 5, 10 = 5th and 10th weeks of the year, -3 = 3rd from last week of the year)
     * Uses a varargs parameter to allow any number of value.
     */
    public int[] getWeekNumbers() { return weekNumbers; }
    private int[] weekNumbers;
    public void setWeekNumbers(int... weekNumbers)
    {
        for (int w : weekNumbers)
        {
            if (w < -53 || w > 53 || w == 0) throw new InvalidParameterException("Invalid BYWEEKNO value (" + w + "). Valid values are 1 to 53 or -53 to -1.");
        }
        this.weekNumbers = weekNumbers;
    }
    public ByWeekNo withWeekNumbers(int... weekNumbers) { setWeekNumbers(weekNumbers); return this; }

    /** Start of week - default start of week is Monday */
    public DayOfWeek getWeekStart() { return weekStart; }
    private DayOfWeek weekStart = DayOfWeek.MONDAY;
    public void setWeekStart(DayOfWeek weekStart) { this.weekStart = weekStart; }

    
    /** Constructor requires weeks of the year value(s) */
    public ByWeekNo(Frequency frequency, int...weekNumbers)
    {
        super(frequency);
        setWeekNumbers(weekNumbers);
    }

    @Override
    public Stream<LocalDateTime> stream(Stream<LocalDateTime> inStream, LocalDateTime startDateTime)
    {
        switch (getFrequency().getChronoUnit())
        {
        case YEARS:
            getFrequency().setChronoUnit(WEEKS);
            Locale oldLocale = null;
            WeekFields weekFields = WeekFields.of(Locale.getDefault());
            DayOfWeek firstDayOfWeek = weekFields.getFirstDayOfWeek();
            if (firstDayOfWeek != getWeekStart())
            {
                switch (weekStart)
                { // Pick a Locale that matches the first day of week specified.
                case MONDAY:
                    oldLocale = Locale.getDefault();
                    Locale.setDefault(Locale.FRANCE);
                    break;
                case SUNDAY:
                    oldLocale = Locale.getDefault();
                    Locale.setDefault(Locale.US);
                    break;
                case FRIDAY:
                case SATURDAY:
                case THURSDAY:
                case TUESDAY:
                case WEDNESDAY:
                default:
                    throw new RuntimeException("Not implemented start of week " + weekStart);
                }
            }
            WeekFields weekFields2 = WeekFields.of(Locale.getDefault());
            if (weekFields2.getFirstDayOfWeek() != getWeekStart()) throw new RuntimeException("Can't match first day of week " + getWeekStart());

            // Make output stream
            Stream<LocalDateTime> outStream = inStream.flatMap(date -> 
            { // Expand to include matching days in all months
                DayOfWeek dayOfWeek = startDateTime.getDayOfWeek();
                List<LocalDateTime> dates = new ArrayList<LocalDateTime>();
                for (int myWeekNumber: getWeekNumbers())
                {
                    LocalDateTime newDate = date.with(TemporalAdjusters.next(dayOfWeek));
                    int newDateWeekNumber = newDate.get(weekFields2.weekOfWeekBasedYear());
                    int weekShift = myWeekNumber - newDateWeekNumber;
                    dates.add(newDate.plusWeeks(weekShift));
                }
                return dates.stream();
            });
            if (oldLocale != null) Locale.setDefault(oldLocale); // if changed, return Locale to former setting
            return outStream;
        case DAYS:
        case WEEKS:
        case MONTHS:
        case HOURS:
        case MINUTES:
        case SECONDS:
            throw new InvalidParameterException("BYWEEKNO is not available for " + getFrequency().getChronoUnit() + " frequency."); // Not available
        default:
            break;
        }
        return null;    
    }

}
