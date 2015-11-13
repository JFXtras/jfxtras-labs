package jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule.byxxx;

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

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule.freq.Frequency;

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
    public ByRule withWeekNumbers(int... weekNumbers) { setWeekNumbers(weekNumbers); return this; }
    
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
            return inStream.flatMap(date -> 
            { // Expand to include matching days in all months
                DayOfWeek dayOfWeek = startDateTime.getDayOfWeek();
                WeekFields weekFields = WeekFields.of(Locale.getDefault());
                List<LocalDateTime> dates = new ArrayList<LocalDateTime>();
                for (int myWeekNumber: getWeekNumbers())
                {
                    LocalDateTime newDate = date.with(TemporalAdjusters.next(dayOfWeek));
                    int newDateWeekNumber = newDate.get(weekFields.weekOfWeekBasedYear());
                    int weekShift = myWeekNumber - newDateWeekNumber;
                    dates.add(newDate.plusWeeks(weekShift));
                }
                return dates.stream();
            });
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
