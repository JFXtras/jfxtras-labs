package jfxtras.labs.scene.control.scheduler.skin;

import jfxtras.labs.scene.control.scheduler.Scheduler;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tom Eugelink
 * @author Islam Khachmakhov
 */
public class SchedulerMonthSkin extends SchedulerSkinAbstract<SchedulerWeekSkin> {

    /**
     *
     */
    public SchedulerMonthSkin(Scheduler control) {
        super(control);
    }

    /**
     * Assign a calendar to each day, so it knows what it must draw.
     */
    protected List<LocalDate> determineDisplayedLocalDates()
    {
        // the result
        List<LocalDate> lLocalDates = new ArrayList<>();

        // From 1st day of this month to the end
        LocalDate lStartLocalDate = LocalDate.now().withDayOfMonth(1);
        for (int i = 0; i < LocalDate.now().lengthOfMonth(); i++) {
            lLocalDates.add(lStartLocalDate.plusDays(i));
        }

        // done
        return lLocalDates;
    }
}
