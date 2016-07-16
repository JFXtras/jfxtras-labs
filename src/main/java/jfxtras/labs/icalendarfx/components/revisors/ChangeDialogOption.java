package jfxtras.labs.icalendarfx.components.revisors;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.Temporal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javafx.util.Pair;
import jfxtras.labs.icalendarfx.components.VComponentDisplayable;
import jfxtras.labs.icalendarfx.properties.PropertyType;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities;

/**
 * Options available when editing or deleting a repeatable appointment.
 * Sometimes all options are not available.  For example, a one-part repeating
 * event doesn't have the SEGMENT option.
 */
public enum ChangeDialogOption
{
    ONE                  // individual instance
  , ALL                  // entire series
  , THIS_AND_FUTURE      // selected instance and all in the future
  , CANCEL;              // do nothing
    
   /** Produce the map of change dialog options and the date range the option affects */
   public static <U extends VComponentDisplayable<U>> Map<ChangeDialogOption, Pair<Temporal,Temporal>> makeDialogChoices(
            U vComponentOriginal,
            U vComponentEdited,
            Temporal startInstance,
            List<PropertyType> changedProperties)
   {
       Map<ChangeDialogOption, Pair<Temporal,Temporal>> choices = new LinkedHashMap<>();
       if (! changedProperties.contains(PropertyType.RECURRENCE_RULE))
       {
           choices.put(ChangeDialogOption.ONE, new Pair<Temporal,Temporal>(startInstance, startInstance));
       }
        
       Temporal lastRecurrence = vComponentEdited.lastRecurrence();
       Temporal firstRecurrence = vComponentEdited.streamRecurrences().findFirst().get();
       System.out.println("first,last:" + lastRecurrence );
       boolean isLastRecurrence = (lastRecurrence == null) ? false : startInstance.equals(lastRecurrence);
       boolean isAfterLastRecurrence = (lastRecurrence == null) ? false : DateTimeUtilities.isAfter(startInstance, lastRecurrence);
       boolean isFirstRecurrence = startInstance.equals(firstRecurrence);
       boolean isDTStartChanged = ! vComponentEdited.getDateTimeStart().equals(vComponentOriginal.getDateTimeStart());
       boolean isFirstOrLastChanged = ! (isLastRecurrence || isFirstRecurrence);
       if (! isAfterLastRecurrence)
       {
           if (isFirstOrLastChanged || isDTStartChanged)
           {
               Temporal start = (startInstance == null) ? vComponentEdited.getDateTimeStart().getValue() : startInstance; // set initial start
               Period dateTimeStartShift = Period.between(LocalDate.from(vComponentEdited.getDateTimeStart().getValue()),
                       LocalDate.from(vComponentOriginal.getDateTimeStart().getValue()));
               start = start.plus(dateTimeStartShift);
               choices.put(ChangeDialogOption.THIS_AND_FUTURE, new Pair<Temporal,Temporal>(start, lastRecurrence));
           }
       }
       choices.put(ChangeDialogOption.ALL, new Pair<Temporal,Temporal>(vComponentEdited.getDateTimeStart().getValue(), lastRecurrence));
       return choices;
    }        
}
