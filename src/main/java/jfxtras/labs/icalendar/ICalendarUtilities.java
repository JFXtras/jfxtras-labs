package jfxtras.labs.icalendar;

import java.time.temporal.Temporal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javafx.util.Pair;
import jfxtras.labs.icalendar.VComponent.StartEndRange;

public final class ICalendarUtilities
{
    private ICalendarUtilities() { };
    
    final private static Comparator<? super Pair<String, String>> DTSTART_COMPARATOR = (p1, p2) ->
    {
        String propertyName1 = p1.getKey();
        return (propertyName1.equals("DTSTART")) ? -1 : 1;
    };
    
    /*
     * Returns list of property name and property value list wrapped in a Pair.  DTSTART
     * property is put on top if present.
     */
    public static List<Pair<String,String>> ComponentStringToPropertyNameAndValueList(String string)
    {        
        return Arrays
            .stream(string.split(System.lineSeparator()))
            .map(line -> {
                int propertyValueSeparatorIndex = 0;
                for (int i=0; i<line.length(); i++)
                {
                    if ((line.charAt(i) == ';') || (line.charAt(i) == ':'))
                    {
                        propertyValueSeparatorIndex = i;
                        break;
                    }
                }
                if (propertyValueSeparatorIndex == 0)
                {
                    return null; // line doesn't contain a property, skip
                }
                String propertyName = line.substring(0, propertyValueSeparatorIndex);
                String value = line.substring(propertyValueSeparatorIndex + 1).trim();
                if (value.isEmpty())
                { // skip empty properties
                    return null;
                }
                return new Pair<String,String>(propertyName, value);
            })
            .filter(p -> p != null)
            .sorted(DTSTART_COMPARATOR)
            .collect(Collectors.toList());
    }
    
//    /** formats by either LocalDate or LocalDateTime Temporal to an easy-to-read format
//     * Example: Dec 5, 2015 - Feb 6, 2016
//     *          Nov 12, 2015 - forever
//     */
//    static String temporalToStringPretty(Temporal temporal)
//    {
//        if (temporal.isSupported(ChronoUnit.NANOS))
//        {
//            return Settings.DATE_TIME_FORMAT.format(temporal);
//        } else
//        {
//            return Settings.DATE_FORMAT_AGENDA_EXCEPTION_DATEONLY.format(temporal);
//        }
//    };
    
    // TODO - PROBABLY THESE METHODS SHOULD GO TO ICALENDARUTILITIES
 
    
    /**
     * Options available when editing or deleting a repeatable appointment.
     * Sometimes all options are not available.  For example, a one-part repeating
     * event doesn't have the SEGMENT option.
     */
    public enum ChangeDialogOption
    {
        ONE                  // individual instance
      , ALL                  // entire series
      , THIS_AND_FUTURE      // same as THIS_AND_FUTURE_ALL, but has a shorter text.  It is used when THIS_AND_FUTURE_SEGMENT does not appear
      , CANCEL;              // do nothing

        static Map<ChangeDialogOption, StartEndRange> makeDialogChoices(VComponent<?> vComponent, Temporal startInstance)
        {
            Map<ChangeDialogOption, StartEndRange> choices = new LinkedHashMap<>();
            choices.put(ChangeDialogOption.ONE, new StartEndRange(startInstance, startInstance));
            Temporal end = vComponent.lastStartTemporal();
            if (! vComponent.isIndividual())
            {
                if (! vComponent.isLastRecurrence(startInstance))
                {
                    Temporal start = (startInstance == null) ? vComponent.getDateTimeStart() : startInstance; // set initial start
                    choices.put(ChangeDialogOption.THIS_AND_FUTURE, new StartEndRange(start, end));
                }
                choices.put(ChangeDialogOption.ALL, new StartEndRange(vComponent.getDateTimeStart(), end));
            }
            return choices;
        }
        
        
//      private List<ChangeDialogOption> makeDialogChoices(Temporal startInstance)
//      {
//          Map<ChangeDialogOption, String> choices = new LinkedHashMap<>();
//          String one = ICalendarUtilities.temporalToStringPretty(startInstance);
//          choices.put(ChangeDialogOption.ONE, one);
//          if (! this.isIndividual())
//          {
//              if (! isLastRecurrence(startInstance))
//              {
//                  String future = ICalendarUtilities.rangeToString(this, startInstance);
//                  choices.put(ChangeDialogOption.THIS_AND_FUTURE, future);
//              }
//              String all = ICalendarUtilities.rangeToString(this);
//              choices.put(ChangeDialogOption.ALL, all);
//          }
//          return choices;
//      }
//        @Override
//        public String toString() {
//            return Settings.REPEAT_CHANGE_CHOICES.get(this);
//        }
    }
    
    // takeWhile - From http://stackoverflow.com/questions/20746429/limit-a-stream-by-a-predicate
    // will be obsolete with Java 9
    static <T> Spliterator<T> takeWhile(
            Spliterator<T> splitr, Predicate<? super T> predicate) {
          return new Spliterators.AbstractSpliterator<T>(splitr.estimateSize(), 0) {
            boolean stillGoing = true;
            @Override public boolean tryAdvance(Consumer<? super T> consumer) {
              if (stillGoing) {
                boolean hadNext = splitr.tryAdvance(elem -> {
                  if (predicate.test(elem)) {
                    consumer.accept(elem);
                  } else {
                    stillGoing = false;
                  }
                });
                return hadNext && stillGoing;
              }
              return false;
            }
          };
        }

    public static <T> Stream<T> takeWhile(Stream<T> stream, Predicate<? super T> predicate) {
       return StreamSupport.stream(takeWhile(stream.spliterator(), predicate), false);
    }
}
