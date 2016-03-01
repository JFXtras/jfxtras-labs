package jfxtras.labs.icalendar;

import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jfxtras.labs.icalendar.rrule.RRule;

/**
     * VComponent properties with the following data and methods:
     * iCalendar property name
     * setVComponent - parse string method
     * makeContentLine - toString method
     * isPropertyEqual - tests equality for the property between to VComponents
     * 
     * @author David Bal
     *
     */
    public enum VComponentProperty
    {
        /**
         * CATEGORIES: RFC 5545 iCalendar 3.8.1.12. page 81
         * This property defines the categories for a calendar component.
         * Example:
         * CATEGORIES:APPOINTMENT,EDUCATION
         * CATEGORIES:MEETING
         */
        CATEGORIES ("CATEGORIES", true)
        {
            @Override
            public boolean parseAndSetProperty(VComponent<?> vComponent, String value)
            {
                vComponent.setCategories(value);
                return true;
            }

            @Override
            public Object getPropertyValue(VComponent<?> vComponent)
            {
                return vComponent.getCategories();
            }
            
            @Override
            public String makeContentLine(VComponent<?> vComponent)
            {
                return ((vComponent.getCategories() == null) || (vComponent.getCategories().isEmpty())) ? null : vComponent.categoriesProperty().getName()
                        + ":" + vComponent.getCategories();
            }

            @Override
            public boolean isPropertyEqual(VComponent<?> v1, VComponent<?> v2)
            {
                return (v1.getCategories() == null) ? (v2.getCategories() == null) : v1.getCategories().equals(v2.getCategories());
            }

            @Override
            public void copyProperty(VComponent<?> source, VComponent<?> destination)
            {
                destination.setCategories(source.getCategories());
            }
        }
        /**
         *  COMMENT: RFC 5545 iCalendar 3.8.1.12. page 83
         * This property specifies non-processing information intended
          to provide a comment to the calendar user.
         * Example:
         * COMMENT:The meeting really needs to include both ourselves
             and the customer. We can't hold this meeting without them.
             As a matter of fact\, the venue for the meeting ought to be at
             their site. - - John
         * */
      , COMMENT ("COMMENT", true)
        {
            @Override
            public boolean parseAndSetProperty(VComponent<?> vComponent, String value)
            {
                vComponent.setComment(value); // TODO - collect multiple values - comma separate? Use list?            
                return true;
            }

            @Override
            public Object getPropertyValue(VComponent<?> vComponent)
            {
                return vComponent.getComment();
            }
            
            @Override
            public String makeContentLine(VComponent<?> vComponent)
            {
                return ((vComponent.getComment() == null) || (vComponent.getComment().isEmpty())) ? null : vComponent.commentProperty().getName()
                        + ":" + vComponent.getComment();
            }

            @Override
            public boolean isPropertyEqual(VComponent<?> v1, VComponent<?> v2)
            {
                return (v1.getComment() == null) ? (v2.getComment() == null) : v1.getComment().equals(v2.getComment());
            }

            @Override
            public void copyProperty(VComponent<?> source, VComponent<?> destination)
            {
                destination.setComment(source.getComment());
            }
        }
      /**
       * CREATED: Date-Time Created, from RFC 5545 iCalendar 3.8.7.1 page 136
       * This property specifies the date and time that the calendar information was created.
       * This is analogous to the creation date and time for a file in the file system.
       * The value MUST be specified in the UTC time format.
       */
      , CREATED ("CREATED", false)
        {
            @Override
            public boolean parseAndSetProperty(VComponent<?> vComponent, String value)
            {
                if (vComponent.getDateTimeCreated() == null)
                {
                    ZonedDateTime dateTime = ZonedDateTime.parse(value, VComponent.ZONED_DATE_TIME_UTC_FORMATTER);
                    vComponent.setDateTimeCreated(dateTime);
                    return true;
                } else
                {
                    throw new IllegalArgumentException(toString() + " can only appear once in calendar component");                    
                }
            }

            @Override
            public Object getPropertyValue(VComponent<?> vComponent)
            {
                return vComponent.getDateTimeCreated();
            }
            
            @Override
            public String makeContentLine(VComponent<?> vComponent)
            {
                return (vComponent.getDateTimeCreated() == null) ? null : vComponent.dateTimeCreatedProperty().getName() + ":"
                        + VComponent.ZONED_DATE_TIME_UTC_FORMATTER.format(vComponent.getDateTimeCreated());
            }

            @Override
            public boolean isPropertyEqual(VComponent<?> v1, VComponent<?> v2)
            {
                return (v1.getDateTimeCreated() == null) ? (v2.getDateTimeCreated() == null) : v1.getDateTimeCreated().equals(v2.getDateTimeCreated());
            }

            @Override
            public void copyProperty(VComponent<?> source, VComponent<?> destination)
            {
                destination.setDateTimeCreated(source.getDateTimeCreated());
            }
        }
      /**
       * DTSTAMP: Date-Time Stamp, from RFC 5545 iCalendar 3.8.7.2 page 137
       * This property specifies the date and time that the instance of the
       * iCalendar object was created
       * The value MUST be specified in the UTC time format.
       */
      , DATE_TIME_STAMP ("DTSTAMP", false)
            {
            @Override
            public boolean parseAndSetProperty(VComponent<?> vComponent, String value)
            {
                if (vComponent.getDateTimeStamp() == null)
                {
                    ZonedDateTime dateTime = ZonedDateTime.parse(value, VComponent.ZONED_DATE_TIME_UTC_FORMATTER);
                    vComponent.setDateTimeStamp(dateTime);
                    return true;
                } else
                {
                    throw new IllegalArgumentException(toString() + " can only appear once in calendar component");                    
                }
            }

            @Override
            public Object getPropertyValue(VComponent<?> vComponent)
            {
                return vComponent.getDateTimeStamp();
            }
            
            @Override
            public String makeContentLine(VComponent<?> vComponent)
            {
                return (vComponent.getDateTimeStamp() == null) ? null : vComponent.dateTimeStampProperty().getName() + ":"
                        + VComponent.ZONED_DATE_TIME_UTC_FORMATTER.format(vComponent.getDateTimeStamp());
            }

            @Override
            public boolean isPropertyEqual(VComponent<?> v1, VComponent<?> v2)
            {
                return (v1.getDateTimeStamp() == null) ? (v2.getDateTimeStamp() == null) : v1.getDateTimeStamp().equals(v2.getDateTimeStamp());
            }

            @Override
            public void copyProperty(VComponent<?> source, VComponent<?> destination)
            {
                destination.setDateTimeStamp(source.getDateTimeStamp());
            }
        }
      /**
       * DTSTART: Date-Time Start, from RFC 5545 iCalendar 3.8.2.4 page 97
       * Start date/time of repeat rule.  Used as a starting point for making the Stream<LocalDateTime> of valid
       * start date/times of the repeating events.  Can be either type LocalDate or LocalDateTime
       */
      , DATE_TIME_START ("DTSTART", true)
        {
            @Override
            public boolean parseAndSetProperty(VComponent<?> vComponent, String value)
            {
                if (vComponent.getDateTimeStart() == null)
                {
                    Temporal dateTime = VComponent.parseTemporal(value);
                    vComponent.setDateTimeStart(dateTime);
                    return true;
                } else
                {
                    throw new IllegalArgumentException(toString() + " can only appear once in calendar component");                    
                }
            }

            @Override
            public Object getPropertyValue(VComponent<?> vComponent)
            {
                return vComponent.getDateTimeStart();
            }
            
            @Override
            public String makeContentLine(VComponent<?> vComponent)
            {
                if (vComponent.getDateTimeStart() == null)
                {
                    return null;
                } else
                {
                    String tag = VComponent.makeDateTimePropertyTag(vComponent.dateTimeStartProperty().getName(), vComponent.getDateTimeStart());
                    return tag + VComponent.temporalToString(vComponent.getDateTimeStart());
                }
            }

            @Override
            public boolean isPropertyEqual(VComponent<?> v1, VComponent<?> v2)
            {
                return (v1.getDateTimeStart() == null) ? (v2.getDateTimeStart() == null) : v1.getDateTimeStart().equals(v2.getDateTimeStart());
            }

            @Override
            public void copyProperty(VComponent<?> source, VComponent<?> destination)
            {
                destination.setDateTimeStart(source.getDateTimeStart());
            }
        }
      /**
       * EXDATE: Set of date/times exceptions for recurring events, to-dos, journal entries.
       * 3.8.5.1, RFC 5545 iCalendar
       */
      , EXCEPTIONS ("EXDATE", false)
        {
            @Override
            public boolean parseAndSetProperty(VComponent<?> vComponent, String value)
            {
                Collection<Temporal> temporals = RecurrenceComponent.parseTemporals(value);
                if (vComponent.getExDate() == null)
                {
                    vComponent.setExDate(new ExDate());
                }                  
                vComponent.getExDate().getTemporals().addAll(temporals);
                return true;
            }

            @Override
            public Object getPropertyValue(VComponent<?> vComponent)
            {
                return vComponent.getExDate();
            }
            
            @Override
            public String makeContentLine(VComponent<?> vComponent)
            {
                if (vComponent.getExDate() == null)
                {
                    return null;
                } else
                {
                    if (vComponent.isExDatesOnOneLine())
                    {
                        Temporal firstTemporal = vComponent.getExDate().getTemporals().iterator().next();
                        String tag = VComponent.makeDateTimePropertyTag(vComponent.exDateProperty().getName(), firstTemporal);
                        return tag + vComponent.getExDate().toString();
                    } else
                    {
                        Temporal firstTemporal = vComponent.getExDate().getTemporals().iterator().next();
                        String tag = VComponent.makeDateTimePropertyTag(vComponent.exDateProperty().getName(), firstTemporal);
                        return vComponent.getExDate()
                                .getTemporals()
                                .stream()
                                .map(t -> tag + VComponent.temporalToString(t) + System.lineSeparator())
                                .collect(Collectors.joining())
                                .trim();
                    }
                }
            }

            @Override
            public boolean isPropertyEqual(VComponent<?> v1, VComponent<?> v2)
            {
                return (v1.getExDate() == null) ? (v2.getExDate() == null) : v1.getExDate().equals(v2.getExDate());
            }

            @Override
            public void copyProperty(VComponent<?> source, VComponent<?> destination)
            {
                if (source.getExDate() != null)
                {
                    if (destination.getExDate() == null)
                    { // make new EXDate object for destination if necessary
                        try {
                            ExDate newEXDate = source.getExDate().getClass().newInstance();
                            destination.setExDate(newEXDate);
                        } catch (InstantiationException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                    source.getExDate().copyTo(destination.getExDate());
                }
            }
        }
      /**
       * LAST-MODIFIED: Date-Time Last Modified, from RFC 5545 iCalendar 3.8.7.3 page 138
       * This property specifies the date and time that the information associated with
       * the calendar component was last revised.
       * 
       * The property value MUST be specified in the UTC time format.
       */
      , LAST_MODIFIED ("LAST-MODIFIED", false)
        {
            @Override
            public boolean parseAndSetProperty(VComponent<?> vComponent, String value)
            {
                if (vComponent.getDateTimeLastModified() == null)
                {
                    ZonedDateTime dateTime = ZonedDateTime.parse(value, VComponent.ZONED_DATE_TIME_UTC_FORMATTER);
                    vComponent.setDateTimeLastModified(dateTime);
                    return true;
                } else
                {
                    throw new IllegalArgumentException(toString() + " can only appear once in calendar component");                    
                }        
            }

            @Override
            public Object getPropertyValue(VComponent<?> vComponent)
            {
                return vComponent.getDateTimeLastModified();
            }
            
            @Override
            public String makeContentLine(VComponent<?> vComponent)
            {
                return (vComponent.getDateTimeLastModified() == null) ? null : vComponent.dateTimeLastModifiedProperty().getName() + ":"
                        + VComponent.ZONED_DATE_TIME_UTC_FORMATTER.format(vComponent.getDateTimeLastModified());
            }

            @Override
            public boolean isPropertyEqual(VComponent<?> v1, VComponent<?> v2)
            {
                return (v1.getDateTimeLastModified() == null) ? (v2.getDateTimeLastModified() == null) : v1.getDateTimeLastModified().equals(v2.getDateTimeLastModified());
            }

            @Override
            public void copyProperty(VComponent<?> source, VComponent<?> destination)
            {
                destination.setDateTimeLastModified(source.getDateTimeLastModified());
            }
        }
      /**
       *  ORGANIZER: RFC 5545 iCalendar 3.8.4.3. page 111
       * This property defines the organizer for a calendar component
       * Example:
       * ORGANIZER;CN=John Smith:mailto:jsmith@example.com
       * 
       * The property is stored as a simple string.  The implementation is
       * responsible to extract any contained data elements such as CN, DIR, SENT-BY
       * */
      , ORGANIZER ("ORGANIZER", true)
        {
            @Override
            public boolean parseAndSetProperty(VComponent<?> vComponent, String value)
            {
                vComponent.setOrganizer(value);
                return true;
            }

            @Override
            public Object getPropertyValue(VComponent<?> vComponent)
            {
                return vComponent.getOrganizer();
            }
            
            @Override
            public String makeContentLine(VComponent<?> vComponent)
            {
                return ((vComponent.getOrganizer() == null) || (vComponent.getOrganizer().isEmpty())) ? null : vComponent.organizerProperty().getName() + ":"
                        + vComponent.getOrganizer().toString();
            }

            @Override
            public boolean isPropertyEqual(VComponent<?> v1, VComponent<?> v2)
            {
                return (v1.getOrganizer() == null) ? (v2.getOrganizer() == null) : v1.getOrganizer().equals(v2.getOrganizer());
            }

            @Override
            public void copyProperty(VComponent<?> source, VComponent<?> destination)
            {
                destination.setOrganizer(source.getOrganizer());
            }
        }
      /**
       * RDATE: Set of date/times for recurring events, to-dos, journal entries.
       * 3.8.5.2, RFC 5545 iCalendar
       */
      , RECURRENCES ("RDATE", false)
        {
            @Override
            public boolean parseAndSetProperty(VComponent<?> vComponent, String value)
            {
                Collection<Temporal> temporals = RecurrenceComponent.parseTemporals(value);
                if (vComponent.getRDate() == null)
                {
                    vComponent.setRDate(new RDate());
                }                  
                vComponent.getRDate().getTemporals().addAll(temporals);
                return true;
            }

            @Override
            public Object getPropertyValue(VComponent<?> vComponent)
            {
                return vComponent.getRDate();
            }
            
            @Override
            public String makeContentLine(VComponent<?> vComponent)
            {
                if (vComponent.getRDate() == null)
                {
                    return null;
                } else
                {
                    Temporal firstTemporal = vComponent.getRDate().getTemporals().iterator().next();
                    String tag = VComponent.makeDateTimePropertyTag(vComponent.rDateProperty().getName(), firstTemporal);
                    return tag + vComponent.getRDate().toString();
                }
            }

            @Override
            public boolean isPropertyEqual(VComponent<?> v1, VComponent<?> v2)
            {
                return (v1.getRDate() == null) ? (v2.getRDate() == null) : v1.getRDate().equals(v2.getRDate()); // required 
            }

            @Override
            public void copyProperty(VComponent<?> source, VComponent<?> destination)
            {
                if (source.getRDate() != null)
                {
                    if (destination.getRDate() == null)
                    { // make new RDate object for destination if necessary
                        try {
                            RDate newRDate = source.getRDate().getClass().newInstance();
                            destination.setRDate(newRDate);
                        } catch (InstantiationException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                    source.getRDate().copyTo(destination.getRDate());
                }
            }
        }

      /**
       * RECURRENCE-ID: date or date-time recurrence, from RFC 5545 iCalendar 3.8.4.4 page 112
       * The property value is the original value of the "DTSTART" property of the 
       * recurrence instance.
       */
      , RECURRENCE_ID ("RECURRENCE-ID", false)
        {
            @Override
            public boolean parseAndSetProperty(VComponent<?> vComponent, String value)
            {
                if (vComponent.getDateTimeRecurrence() == null)
                {
                    Temporal dateTime = VComponent.parseTemporal(value);
                    vComponent.setDateTimeRecurrence(dateTime);
                    return true;
                } else
                {
                    throw new IllegalArgumentException(toString() + " can only appear once in calendar component");                    
                }
            }

            @Override
            public Object getPropertyValue(VComponent<?> vComponent)
            {
                return vComponent.getDateTimeRecurrence();
            }
            
            @Override
            public String makeContentLine(VComponent<?> vComponent)
            {
                if (vComponent.getDateTimeRecurrence() == null)
                {
                    return null;
                } else
                {
                    String tag = VComponent.makeDateTimePropertyTag(vComponent.dateTimeRecurrenceProperty().getName()
                            , vComponent.getDateTimeRecurrence());
                    return tag + VComponent.temporalToString(vComponent.getDateTimeRecurrence());
                }
            }

            @Override
            public boolean isPropertyEqual(VComponent<?> v1, VComponent<?> v2)
            {
                return (v1.getDateTimeRecurrence() == null) ? (v2.getDateTimeRecurrence() == null) : v1.getDateTimeRecurrence().equals(v2.getDateTimeRecurrence());
            }

            @Override
            public void copyProperty(VComponent<?> source, VComponent<?> destination)
            {
                if (source.getDateTimeRecurrence() != null)
                {
                    destination.setDateTimeRecurrence(source.getDateTimeRecurrence());                    
                }
            }
        }
      /**
       * RRULE, Recurrence Rule as defined in RFC 5545 iCalendar 3.8.5.3, page 122.
       * This property defines a rule or repeating pattern for recurring events, 
       * to-dos, journal entries, or time zone definitions
       * If component is not repeating the value is null.
       */
      , RECURRENCE_RULE ("RRULE", true)
        {
            @Override
            public boolean parseAndSetProperty(VComponent<?> vComponent, String value)
            {
                if (vComponent.getRRule() == null)
                {
                    vComponent.setRRule(RRule.parseRRule(value));
                    return true;
                } else
                {
                    throw new IllegalArgumentException(toString() + " can only appear once in calendar component");                    
                }
            }

            @Override
            public Object getPropertyValue(VComponent<?> vComponent)
            {
                return vComponent.getRRule();
            }
            
            @Override
            public String makeContentLine(VComponent<?> vComponent)
            {
                return (vComponent.getRRule() == null) ? null : vComponent.rRuleProperty().getName() + ":"
                        + vComponent.getRRule().toString();
            }

            @Override
            public boolean isPropertyEqual(VComponent<?> v1, VComponent<?> v2)
            {
                return (v1.getRRule() == null) ? (v2.getRRule() == null) : v1.getRRule().equals(v2.getRRule());
            }

            @Override
            public void copyProperty(VComponent<?> source, VComponent<?> destination)
            {
                if (source.getRRule() != null)
                {
                    if (destination.getRRule() == null)
                    { // make new RRule object for destination if necessary
                        try {
                            RRule newRRule = source.getRRule().getClass().newInstance();
                            destination.setRRule(newRRule);
                        } catch (InstantiationException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                    source.getRRule().copyTo(destination.getRRule());
                }
            }
        }
      /**
       * RELATED-TO: This property is used to represent a relationship or reference between
       * one calendar component and another.  By default, the property value points to another
       * calendar component's UID that has a PARENT relationship to the referencing object.
       * This field is null unless the object contains as RECURRENCE-ID value.
       * 3.8.4.5, RFC 5545 iCalendar
       */
      , RELATED_TO ("RELATED-TO", false)
        {
            @Override
            public boolean parseAndSetProperty(VComponent<?> vComponent, String value)
            {
                vComponent.setRelatedTo(value); // TODO - collect multiple values - comma separate? Use list?
                return true;
            }

            @Override
            public Object getPropertyValue(VComponent<?> vComponent)
            {
                return vComponent.getRelatedTo();
            }
            
            @Override
            public String makeContentLine(VComponent<?> vComponent)
            {
                return ((vComponent.getRelatedTo() == null) || (vComponent.getRelatedTo().isEmpty())) ? null : vComponent.relatedToProperty().getName() + ":"
                        + vComponent.getRelatedTo().toString();
            }

            @Override
            public boolean isPropertyEqual(VComponent<?> v1, VComponent<?> v2)
            {
                return (v1.getRelatedTo() == null) ? (v2.getRelatedTo() == null) : v1.getRelatedTo().equals(v2.getRelatedTo());
            }

            @Override
            public void copyProperty(VComponent<?> source, VComponent<?> destination)
            {
                destination.setRelatedTo(source.getRelatedTo());
            }
        }
      /**
       *  SEQUENCE: RFC 5545 iCalendar 3.8.7.4. page 138
       * This property defines the revision sequence number of the calendar component within a sequence of revisions.
       * Example:  The following is an example of this property for a calendar
        component that was just created by the "Organizer":

         SEQUENCE:0

        The following is an example of this property for a calendar
        component that has been revised two different times by the
        "Organizer":

         SEQUENCE:2
       */
      , SEQUENCE ("SEQUENCE", false)
        {
            @Override
            public boolean parseAndSetProperty(VComponent<?> vComponent, String value)
            {
                if (vComponent.getSequence() == 0)
                {
                    vComponent.setSequence(Integer.parseInt(value));
                    return true;
                } else
                {
                    throw new IllegalArgumentException(toString() + " can only appear once in calendar component");                    
                }            
            }

            @Override
            public Object getPropertyValue(VComponent<?> vComponent)
            {
                return vComponent.getSequence();
            }
            
            @Override
            public String makeContentLine(VComponent<?> vComponent)
            {
                return (vComponent.getSequence() == 0) ? null : vComponent.sequenceProperty().getName() + ":"
                        + Integer.toString(vComponent.getSequence());
            }

            @Override
            public boolean isPropertyEqual(VComponent<?> v1, VComponent<?> v2)
            {
                return v1.getSequence() == v2.getSequence();
            }

            @Override
            public void copyProperty(VComponent<?> source, VComponent<?> destination)
            {
                destination.setSequence(source.getSequence());
            }
        }
      /**
       *  SUMMARY: RFC 5545 iCalendar 3.8.1.12. page 83
       * This property defines a short summary or subject for the calendar component 
       * Example:
       * SUMMARY:Department Party
       * */
      , SUMMARY ("SUMMARY", true)
        {
            @Override
            public boolean parseAndSetProperty(VComponent<?> vComponent, String value)
            {
                if (vComponent.getSummary() == null)
                {
                    vComponent.setSummary(value);
                    return true;
                } else
                {
                    throw new IllegalArgumentException(toString() + " can only appear once in calendar component");                    
                }        
            }

            @Override
            public Object getPropertyValue(VComponent<?> vComponent)
            {
                return vComponent.getSummary();
            }
            
            @Override
            public String makeContentLine(VComponent<?> vComponent)
            {
                return ((vComponent.getSummary() == null) || (vComponent.getSummary().isEmpty())) ? null : vComponent.summaryProperty().getName() + ":"
                        + vComponent.getSummary();
            }

            @Override
            public boolean isPropertyEqual(VComponent<?> v1, VComponent<?> v2)
            {
                return (v1.getSummary() == null) ? (v2.getSummary() == null) : v1.getSummary().equals(v2.getSummary());
            }

            @Override
            public void copyProperty(VComponent<?> source, VComponent<?> destination)
            {
                destination.setSummary(source.getSummary());
            }
        }
      /**
       * UID, Unique identifier, as defined by RFC 5545, iCalendar 3.8.4.7 page 117
       * A globally unique identifier for the calendar component.
       * Included is an example UID generator.  Other UID generators can be provided by
       * setting the UID callback.
       */
      , UNIQUE_IDENTIFIER ("UID", false)
        {
            @Override
            public boolean parseAndSetProperty(VComponent<?> vComponent, String value)
            {
                if (vComponent.getUniqueIdentifier() == null)
                {
                    vComponent.setUniqueIdentifier(value);
                    return true;
                } else
                {
                    throw new IllegalArgumentException(toString() + " can only appear once in calendar component");                    
                }       
            }

            @Override
            public Object getPropertyValue(VComponent<?> vComponent)
            {
                return vComponent.getUniqueIdentifier();
            }
            
            @Override
            public String makeContentLine(VComponent<?> vComponent)
            {
                return ((vComponent.getUniqueIdentifier() == null) || (vComponent.getUniqueIdentifier().isEmpty())) ? null : vComponent.uniqueIdentifierProperty().getName()
                        + ":" + vComponent.getUniqueIdentifier();
            }

            @Override
            public boolean isPropertyEqual(VComponent<?> v1, VComponent<?> v2)
            {
                return (v1.getUniqueIdentifier() == null) ? (v2.getUniqueIdentifier() == null) : v1.getUniqueIdentifier().equals(v2.getUniqueIdentifier());
            }

            @Override
            public void copyProperty(VComponent<?> source, VComponent<?> destination)
            {
                destination.setUniqueIdentifier(source.getUniqueIdentifier());
            }
        };
      
        // Map to match up string tag to ICalendarProperty enum
        private static Map<String, VComponentProperty> propertyFromTagMap = makePropertiesFromNameMap();
        private static Map<String, VComponentProperty> makePropertiesFromNameMap()
        {
            Map<String, VComponentProperty> map = new HashMap<>();
            VComponentProperty[] values = VComponentProperty.values();
            for (int i=0; i<values.length; i++)
            {
                map.put(values[i].toString(), values[i]);
            }
            return map;
        }
        private String name;
        /* indicates if providing a dialog to allow user to confirm edit is required. 
         * False means no confirmation is required or property is only modified by the implementation, not by the user */
        boolean dialogRequired;
        
        VComponentProperty(String name, boolean dialogRequired)
        {
            this.name = name;
            this.dialogRequired = dialogRequired;
        }
        
        /** Returns the iCalendar property name (e.g. DTSTAMP) */
        @Override
        public String toString() { return name; }
        public boolean isDialogRequired() { return dialogRequired; }
        
        /** get VComponentProperty enum from property name */
        public static VComponentProperty propertyFromString(String propertyName)
        {
            return propertyFromTagMap.get(propertyName.toUpperCase());
        }

        /**
         * Tests equality between two VComponent objects.  Treats v1 as expected.  Produces a JUnit-like
         * output if objects are not equal.
         * 
         * @param v1 - expected VComponent
         * @param v2 - actual VComponent
         * @param verbose - true = display list of unequal properties, false no display output
         * @return - equality result
         */
        public static <T> boolean isEqualTo(VComponent<?> v1, VComponent<?> v2, boolean verbose)
        {
            List<String> changedProperties = new ArrayList<>();
            Arrays.stream(VComponentProperty.values())
            .forEach(p -> 
            {
                if (! (p.isPropertyEqual(v1, v2)))
                {
                    changedProperties.add(p.toString() + " not equal:" + p.getPropertyValue(v1) + " " + p.getPropertyValue(v2));
                }
            });

            if (changedProperties.size() == 0)
            {
                return true;
            } else
            {
                if (verbose)
                {
                System.out.println("VComponent not Equal:"
                        + System.lineSeparator()
                        + "expecting:" + System.lineSeparator()
                        + v1 + System.lineSeparator()
                        + "but was:" + System.lineSeparator()
                        + v2 + System.lineSeparator()
                        + changedProperties.stream().collect(Collectors.joining(System.lineSeparator()))
                        );
                }
                return false;
            }
        }
        
        
        // TODO - MOVE COPYTO HERE
        
        /*
         * ABSTRACT METHODS
         */
       
        /** sets VComponent's property for this VComponentProperty to parameter value
         * value is a string that is parsed if necessary to the appropriate type
         * returns true, if property was found and set */
        public abstract boolean parseAndSetProperty(VComponent<?> vComponent, String value);

        /** gets VComponent's property value for this VComponentProperty */
        public abstract Object getPropertyValue(VComponent<?> vComponent);
                
        /** makes content line (RFC 5545 3.1) from a vComponent property  */
        public abstract String makeContentLine(VComponent<?> vComponent);       

        /** Checks is corresponding property is equal between v1 and v2 */
        public abstract boolean isPropertyEqual(VComponent<?> v1, VComponent<?> v2);
        
        /** Copies property value from one v1 to v2 */
        public abstract void copyProperty(VComponent<?> source, VComponent<?> destination);
}