package jfxtras.labs.icalendar;

import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
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
import jfxtras.labs.icalendar.components.VComponent;
import jfxtras.labs.icalendar.components.VComponent.StartEndRange;

public final class ICalendarUtilities
{
    private ICalendarUtilities() { };
    
    final private static Comparator<? super Pair<String, String>> DTSTART_COMPARATOR = (p1, p2) ->
    {
        String propertyName1 = p1.getKey();
        return (propertyName1.equals("DTSTART")) ? -1 : 1;
    };
    
    /**
     * Returns list of property name and property value list wrapped in a Pair.
     * DTSTART property is put on top, if present.
     * 
     * This method unwraps multi-line content lines as defined in iCalendar RFC5545 3.1, page 9
     */
    public static List<Pair<String,String>> ComponentStringToPropertyNameAndValueList(String string)
    {
        List<Pair<String,String>> propertyPairs = new ArrayList<>();
        String storedLine = "";
        Iterator<String> lineIterator = Arrays.stream( string.split(System.lineSeparator()) ).iterator();
        while (lineIterator.hasNext())
        {
            String startLine;
            if (storedLine.isEmpty())
            {
                startLine = lineIterator.next();
            } else
            {
                startLine = storedLine;
                storedLine = "";
            }
            StringBuilder builder = new StringBuilder(startLine);
            while (lineIterator.hasNext())
            {
                String anotherLine = lineIterator.next();
                if ((anotherLine.charAt(0) == ' ') || (anotherLine.charAt(0) == '\t'))
                { // unwrap anotherLine into line
                    builder.append(anotherLine.substring(1, anotherLine.length()));
                } else
                {
                    storedLine = anotherLine;
                    break;  // exit while loop
                }
            }
            String line = builder.toString();
            Pair<String, String> pair = parsePropertyLine(line);
            if (pair != null) { propertyPairs.add(pair); }
        }
        Collections.sort(propertyPairs, DTSTART_COMPARATOR);
        return propertyPairs;
    }

    @Deprecated
    public static List<Pair<String,String>> ComponentStringToPropertyNameAndValueListNoUnwrap(String string)
    {
        return Arrays
                .stream(string.split(System.lineSeparator()))
                .map(line -> {
                    return parsePropertyLine(line);
                })
                .filter(p -> p != null)
                .sorted(DTSTART_COMPARATOR)
                .collect(Collectors.toList());        
    }

    /**
     * Splits a content line into its property name/value pair
     * For example:
     * RRULE:FREQ=YEARLY;BYWEEKNO=20;BYDAY=MO
     * key = RRULE
     * value = FREQ=YEARLY;BYWEEKNO=20;BYDAY=MO
     * 
     * DTSTART;TZID=America/Los_Angeles:19970512T090000
     * key = DTSTART
     * value = TZID=America/Los_Angeles:19970512T090000
     * 
     * @param line - single content line from calendar
     * @return - pair where key is property name and value is its value as a string
     */
    public static Pair<String, String> parsePropertyLine(String line)
    {
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
    }
    
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
        
        public static Map<ChangeDialogOption, StartEndRange> makeDialogChoices(VComponent<?> vComponent, Temporal startInstance)
        {
            Map<ChangeDialogOption, StartEndRange> choices = new LinkedHashMap<>();
            choices.put(ChangeDialogOption.ONE, new StartEndRange(startInstance, startInstance));
            Temporal end = vComponent.lastRecurrence();
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
