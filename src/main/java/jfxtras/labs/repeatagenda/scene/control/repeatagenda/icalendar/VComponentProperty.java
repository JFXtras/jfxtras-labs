package jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.RRule;

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
        CATEGORIES ("CATEGORIES", true)
        {
            @Override
            public boolean setVComponent(VComponent<?> vComponent, String value)
            {
                vComponent.setCategories(value);
                return true;
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
        }
      , COMMENT ("COMMENT", true)
        {
            @Override
            public boolean setVComponent(VComponent<?> vComponent, String value)
            {
                vComponent.setComment(value); // TODO - collect multiple values - comma separate? Use list?            
                return true;
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
        }
      , CREATED ("CREATED", false)
        {
            @Override
            public boolean setVComponent(VComponent<?> vComponent, String value)
            {
                ZonedDateTime dateTime = ZonedDateTime.parse(value, VComponent.ZONED_DATE_TIME_UTC_FORMATTER);
                vComponent.setDateTimeCreated(dateTime);        
                return true;
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
        }
      , DATE_TIME_STAMP ("DTSTAMP", false)
            {
            @Override
            public boolean setVComponent(VComponent<?> vComponent, String value)
            {
                ZonedDateTime dateTime = ZonedDateTime.parse(value, VComponent.ZONED_DATE_TIME_UTC_FORMATTER);
                vComponent.setDateTimeStamp(dateTime);        
                return true;
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
        }
      , DATE_TIME_START ("DTSTART", true)
        {
            @Override
            public boolean setVComponent(VComponent<?> vComponent, String value)
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
        }
      , EXCEPTIONS ("EXDATE", false)
        {
            @Override
            public boolean setVComponent(VComponent<?> vComponent, String value)
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
                                .collect(Collectors.joining());
                    }
                }
            }

            @Override
            public boolean isPropertyEqual(VComponent<?> v1, VComponent<?> v2)
            {
                return (v1.getExDate() == null) ? (v2.getExDate() == null) : v1.getExDate().equals(v2.getExDate());
            }
        }
      , LAST_MODIFIED ("LAST-MODIFIED", false)
        {
            @Override
            public boolean setVComponent(VComponent<?> vComponent, String value)
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
        }
      , ORGANIZER ("ORGANIZER", true)
        {
            @Override
            public boolean setVComponent(VComponent<?> vComponent, String value)
            {
                vComponent.setOrganizer(value);
                return true;
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
        }
      , RECURRENCES ("RDATE", false)
        {
            @Override
            public boolean setVComponent(VComponent<?> vComponent, String value)
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
        }
      , RECURRENCE_ID ("RECURRENCE-ID", false)
        {
            @Override
            public boolean setVComponent(VComponent<?> vComponent, String value)
            {
                LocalDateTime dateTime = LocalDateTime.parse(value,VComponent.LOCAL_DATE_TIME_FORMATTER);
                vComponent.setDateTimeRecurrence(dateTime);
                return true;
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
        }
      , RECURRENCE_RULE ("RRULE", true)
        {
            @Override
            public boolean setVComponent(VComponent<?> vComponent, String value)
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
        }
      , RELATED_TO ("RELATED-TO", false)
        {
            @Override
            public boolean setVComponent(VComponent<?> vComponent, String value)
            {
                vComponent.setRelatedTo(value); // TODO - collect multiple values - comma separate? Use list?
                return true;
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
        }
      , SEQUENCE ("SEQUENCE", false)
        {
            @Override
            public boolean setVComponent(VComponent<?> vComponent, String value)
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
        }
      , SUMMARY ("SUMMARY", true)
        {
            @Override
            public boolean setVComponent(VComponent<?> vComponent, String value)
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
        }
      , UNIQUE_IDENTIFIER ("UID", false)
        {
            @Override
            public boolean setVComponent(VComponent<?> vComponent, String value)
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
        
        @Override
        public String toString() { return name; }
        public boolean isDialogRequired() { return dialogRequired; }
        
        /** get VComponentProperty enum from property name */
        public static VComponentProperty propertyFromString(String propertyName)
        {
            return propertyFromTagMap.get(propertyName.toUpperCase());
        }
        
        /** sets enum's associated VEvent's property from parameter value
         * returns true, if property was found and set */
        public abstract boolean setVComponent(VComponent<?> vComponent, String value);
        
        /** makes content line (RFC 5545 3.1) from a vComponent property  */
        public abstract String makeContentLine(VComponent<?> vComponent);       

        /** Checks is corresponding property is equal between v1 and v2 */
        public abstract boolean isPropertyEqual(VComponent<?> v1, VComponent<?> v2);    
}