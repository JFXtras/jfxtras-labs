package jfxtras.labs.scene.control.scheduler.skin;

import jfxtras.labs.scene.control.scheduler.Scheduler;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author Tom Eugelink
 */
public class SchedulerWeekSkin extends SchedulerSkinAbstract<SchedulerWeekSkin> {

    /**
     *
     */
    public SchedulerWeekSkin(Scheduler control) {
        super(control);
    }

    /**
     * Assign a calendar to each day, so it knows what it must draw.
     */
    protected List<LocalDate> determineDisplayedLocalDates()
    {
        // the result
        List<LocalDate> lLocalDates = new ArrayList<>();

        // 7 days stating at the first day of week
        LocalDate lStartLocalDate = getFirstDayOfWeekLocalDate();
        for (int i = 0; i < 7; i++) {
            lLocalDates.add(lStartLocalDate.plusDays(i));
        }

        // done
        return lLocalDates;
    }


    /**
     * get the date of the first day of the week
     */
    private LocalDate getFirstDayOfWeekLocalDate()
    {
        Locale lLocale = getSkinnable().getLocale();
        WeekFields lWeekFields = WeekFields.of(lLocale);
        int lFirstDayOfWeek = lWeekFields.getFirstDayOfWeek().getValue();
        LocalDate lDisplayedDateTime = getSkinnable().getDisplayedLocalDateTime().toLocalDate();
        int lCurrentDayOfWeek = lDisplayedDateTime.getDayOfWeek().getValue();

        if (lFirstDayOfWeek <= lCurrentDayOfWeek) {
            lDisplayedDateTime = lDisplayedDateTime.plusDays(-lCurrentDayOfWeek + lFirstDayOfWeek);
        }
        else {
            lDisplayedDateTime = lDisplayedDateTime.plusDays(-lCurrentDayOfWeek - (7-lFirstDayOfWeek));
        }

        return lDisplayedDateTime;
    }
}
