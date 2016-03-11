package jfxtras.labs.icalendar.components;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.util.Callback;
import javafx.util.Pair;
import jfxtras.labs.icalendar.VCalendar;

public final class VCalendarUtilities
{
    private VCalendarUtilities() { }
    
    
    /**
     * Parse iCalendar ics file to a collection of VComponent objects
     * 
     * @param icsFilePath
     * @param makeVEvent
     * @return
     */
    public static VCalendar parseICalendarICS(URI icsFilePath, Callback<String, VEvent<?,?>> makeVEvent)
    {
//        long start = System.nanoTime();
        
        // divide up by begin and end - the components - then parse the components
        VCalendar vcalendar = new VCalendar();
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
        return vcalendar;       
    }
    
    /**
     * Parses the property-value pair to the matching property, if a match is found.
     * If no matching property, does nothing.
     * 
     * @param vCalendar - object to add property values
     * @param propertyValuePair - property name-value pair (e.g. DTSTART and TZID=America/Los_Angeles:20160214T110000)
     */
    public static void parse(VCalendar vCalendar, Pair<String, String> propertyValuePair)
    {
        String propertyName = propertyValuePair.getKey();
        String value = propertyValuePair.getValue();
        VCalendarProperty vCalendarProperty = VCalendarProperty.propertyFromString(propertyName);
        if (vCalendarProperty != null)
        {
            vCalendarProperty.parseAndSetProperty(vCalendar, value);
        }
    }
    
    public enum VCalendarProperty
    {
        CALENDAR_SCALE ("CALSCALE") {
            @Override
            public void parseAndSetProperty(VCalendar vCalendar, String value)
            {
                if (vCalendar.getCalendarScale() == null)
                {
                    vCalendar.setCalendarScale(value);
                } else
                {
                    throw new IllegalArgumentException(toString() + " can only appear once in calendar");                    
                }
            }
        } ,
        OBJECT_METHOD ("METHOD") {
            @Override
            public void parseAndSetProperty(VCalendar vCalendar, String value)
            {
                if (vCalendar.getObjectMethod() == null)
                {
                    vCalendar.setObjectMethod(value);
                } else
                {
                    throw new IllegalArgumentException(toString() + " can only appear once in calendar");                    
                }
            }
        } ,
        PRODUCT_IDENTIFIER ("PRODID") {
            @Override
            public void parseAndSetProperty(VCalendar vCalendar, String value)
            {
                if (vCalendar.getProductIdentifier() == null)
                {
                    vCalendar.setProductIdentifier(value);
                } else
                {
                    throw new IllegalArgumentException(toString() + " can only appear once in calendar");                    
                }
            }
        } ,
        ALARM_COMPONENT ("VALARM") {
            @Override
            public void parseAndSetProperty(VCalendar vCalendar, String value)
            {
                throw new RuntimeException(toString() + " not supported.");
            }
        } ,
        EVENT_COMPONENT ("VEVENT") {
            @Override
            public void parseAndSetProperty(VCalendar vCalendar, String value)
            {
                vCalendar.vEvents().add( vCalendar.getMakeVEventCallback().call(value) );
            }
        } ,
        ICALENDAR_SPECIFICATION_VERSION ("VERSION") {
            @Override
            public void parseAndSetProperty(VCalendar vCalendar, String value)
            {
                if (vCalendar.getICalendarSpecificationVersion() == null)
                {
                    vCalendar.setICalendarSpecificationVersion(value);
                } else
                {
                    throw new IllegalArgumentException(toString() + " can only appear once in calendar");                    
                }
            }
        } ,
        FREE_BUSY_COMPONENT ("VFREEBUSY") {
            @Override
            public void parseAndSetProperty(VCalendar vCalendar, String value)
            {
                throw new RuntimeException(toString() + " not supported.");
            }
        } ,
        JOURNALCOMPONENT ("VJOURNAL") {
            @Override
            public void parseAndSetProperty(VCalendar vCalendar, String value)
            {
                vCalendar.vJournals().add( vCalendar.getMakeVJournalCallback().call(value) );
            }
        } ,
        TIME_ZONE_COMPONENT ("VTIMEZONE") {
            @Override
            public void parseAndSetProperty(VCalendar vCalendar, String value)
            {
                throw new RuntimeException(toString() + " not supported.");
            }
        } ,
        TO_DO_COMPONENT ("VTODO") {
            @Override
            public void parseAndSetProperty(VCalendar vCalendar, String value)
            {
                vCalendar.vTodos().add( vCalendar.getMakeVTodoCallback().call(value) );
            }
        };
        
        // Map to match up string tag to VCalendarProperty enum
        private static Map<String, VCalendarProperty> propertyFromTagMap = makePropertiesFromNameMap();
        private static Map<String, VCalendarProperty> makePropertiesFromNameMap()
        {
            Map<String, VCalendarProperty> map = new HashMap<>();
            VCalendarProperty[] values = VCalendarProperty.values();
            for (int i=0; i<values.length; i++)
            {
                map.put(values[i].toString(), values[i]);
            }
            return map;
        }
        
        private String name;
        
        VCalendarProperty(String name)
        {
            this.name = name;
        }
        
        /** Returns the iCalendar property name (e.g. DTSTAMP) */
        @Override
        public String toString() { return name; }
        
        /*
         * ABSTRACT METHODS
         */
       
        /** sets VComponent's property for this VComponentProperty to parameter value
         * value is a string that is parsed if necessary to the appropriate type
         */
        public abstract void parseAndSetProperty(VCalendar vCalendar, String value);
        
        /*
         * STATIC METHODS
         */
    
        /** get VCalendarProperty enum from property name */
        public static VCalendarProperty propertyFromString(String propertyName)
        {
            return propertyFromTagMap.get(propertyName.toUpperCase());
        }
    }
}
