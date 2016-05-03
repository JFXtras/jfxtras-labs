package jfxtras.labs.icalendarfx.properties.component.recurrence.rrule;

public enum RecurrenceRuleElement
{
    FREQUENCY ("FREQ") {
        @Override
        public Object geElement(RecurrenceRule2 rrule)
        {
            return rrule.getFrequency();
        }
    },
    UNTIL ("UNTIL") {
        @Override
        public Object geElement(RecurrenceRule2 rrule)
        {
            return rrule.getUntil();
        }
    },
    COUNT ("COUNT") {
        @Override
        public Object geElement(RecurrenceRule2 rrule)
        {
            return rrule.getCount();
        }
    },
    INTERVAL ("INTERVAL") {
        @Override
        public Object geElement(RecurrenceRule2 rrule)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    BY_SECOND ("BYSECOND") {
        @Override
        public Object geElement(RecurrenceRule2 rrule)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    BY_MINUTE ("BYMINUTE") {
        @Override
        public Object geElement(RecurrenceRule2 rrule)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    BY_HOUR ("BYHOUR") {
        @Override
        public Object geElement(RecurrenceRule2 rrule)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    BY_DAY ("BYDAY") {
        @Override
        public Object geElement(RecurrenceRule2 rrule)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    BY_MONTH_DAY ("BYMONTHDAY") {
        @Override
        public Object geElement(RecurrenceRule2 rrule)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    BY_YEAR_DAY ("BYYEARDAY") {
        @Override
        public Object geElement(RecurrenceRule2 rrule)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    BY_WEEK_NUMBER ("BYWEEKNO") {
        @Override
        public Object geElement(RecurrenceRule2 rrule)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    BY_MONTH ("BYMONTH") {
        @Override
        public Object geElement(RecurrenceRule2 rrule)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    BY_SET_POSITION ("BYSETPOS") {
        @Override
        public Object geElement(RecurrenceRule2 rrule)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    WEEK_START ("WKST") {
        @Override
        public Object geElement(RecurrenceRule2 rrule)
        {
            // TODO Auto-generated method stub
            return null;
        }
    };
    
    private String name;
    @Override
    public String toString() { return name; }
  
    RecurrenceRuleElement(String name)
    {
        this.name = name;
    }
 
    /*
     * ABSTRACT METHODS
     */
    /** Returns associated Property<?> or List<Property<?>> */
    abstract public Object geElement(RecurrenceRule2 rrule);

//    /** Parses string and sets property.  Called by {@link VComponentBase#parseContent()} */
//    abstract public void parse(VComponentNew<?> vComponent, String propertyContent);
//
//    /** copies the associated property from the source component to the destination component */
//    abstract public void copyProperty(VComponentNew<?> source, VComponentNew<?> destination);

}
