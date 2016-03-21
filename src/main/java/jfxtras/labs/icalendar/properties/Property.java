package jfxtras.labs.icalendar.properties;

import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import jfxtras.labs.icalendar.parameters.ICalendarParameter;

public interface Property
{
    /**
     * other-param, 3.2 RFC 5545m page 14
     */
    ObservableList<Object> getOtherParameters();
    void setOtherParameters(ObservableList<Object> other);
    
//    String toContentLine();
    
    /**
     * Return property content line for iCalendar output files.  See RFC 5545 3.5
     * Contains component property with its value and any populated parameters.
     * 
     * For example: SUMMARY;LANGUAGE=en-US:Company Holiday Party
     * 
     * @return - the content line
     */
    default String toContentLine()
    {
        return ICalendarParameter.values(getClass())
                .stream()
                .map(p -> p.toContentLine(this))
                .filter(s -> ! (s == null))
                .collect(Collectors.joining());        
    }
}
