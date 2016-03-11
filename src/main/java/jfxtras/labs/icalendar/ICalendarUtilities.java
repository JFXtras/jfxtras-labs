package jfxtras.labs.icalendar;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javafx.util.Callback;
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
        
        static Map<ChangeDialogOption, StartEndRange> makeDialogChoices(VComponent<?> vComponent, Temporal startInstance)
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
    
    /**
     * Parse iCalendar ics file to a collection of VComponent objects
     * 
     * @param icsFilePath
     * @param makeVEvent
     * @return
     */
    public static Collection<VComponent<?>> parseICS(URI icsFilePath, Callback<String, VEvent<?,?>> makeVEvent)
    {
//        long start = System.nanoTime();
        List<VComponent<?>> vComponents = new ArrayList<>();
        ExecutorService service =  Executors.newSingleThreadExecutor();
        List<Callable<Object>> tasks = new ArrayList<>();
        try
        {
            BufferedReader br = Files.newBufferedReader(Paths.get(icsFilePath));
            Iterator<String> lineIterator = br.lines().iterator();
            while (lineIterator.hasNext())
            {
                String line = lineIterator.next();
                if (line.equals("BEGIN:VEVENT"))
                {
                    StringBuilder vEvent = new StringBuilder(line + System.lineSeparator());
                    String vEventLine;
                    do
                    {
                        vEventLine = lineIterator.next();
                        vEvent.append(vEventLine);
                        vEvent.append(System.lineSeparator());
                    } while (! vEventLine.equals("END:VEVENT"));
                    final Runnable readDataThread = new Runnable() {
                        @Override
                        public void run()
                        {
                            vComponents.add(makeVEvent.call(vEvent.toString()));
                        }
                    };
                    tasks.add(Executors.callable(readDataThread));
                }
            }
            service.invokeAll(tasks);
//            icsStream.re
        } catch (IOException | InterruptedException e)
        {
            e.printStackTrace();
        }
//        vComponents.stream().forEach(System.out::println);
//        long stop = System.nanoTime();
//        System.out.println(vComponents.size() + " " + (stop-start)/1000000);
        return vComponents;       
    }
}
