package jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;

import jfxtras.labs.icalendar.DateTimeType;

public class DateTimeUtilities
{
    public static String formatRange(Temporal start, Temporal end)
  {
      DateTimeFormatter startFormatter = (DateTimeType.of(start) == DateTimeType.DATE) ? Settings.DATE_FORMAT : Settings.DATE_TIME_FORMAT;
      Period days = Period.between(LocalDate.from(start), LocalDate.from(end));
      final String startString = startFormatter.format(start);
      final String endString;
      if (days.isZero()) // same day
      {
          endString = Settings.TIME_FORMAT_END.format(end);
      } else
      {
          DateTimeFormatter endFormatterDifferentDay = (DateTimeType.of(start) == DateTimeType.DATE) ? Settings.DATE_FORMAT : Settings.DATE_TIME_FORMAT;
          endString = endFormatterDifferentDay.format(end);            
      }
      return startString + " - " + endString;
  }
}
