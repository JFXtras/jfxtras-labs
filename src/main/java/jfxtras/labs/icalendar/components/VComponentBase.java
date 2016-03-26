package jfxtras.labs.icalendar.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * 
 * @author David Bal
 *
 * @param <T> - implementation class
 * @see VEvent
 * @see VTodo
 * @see VJournal
 * @see VFreeBusy
 * @see VTimeZone
 * @see VAlarm
 */
public class VComponentBase<T> implements VComponent
{
    @Override public ObservableList<String> getXProperties() { return xProperties; }
    private ObservableList<String> xProperties = FXCollections.observableArrayList();
    @Override public void setXProperties(ObservableList<String> xprop) { xProperties = xprop; }
    public T withXProperties(String... xProperties) { getXProperties().addAll(xProperties); return (T) this; }

    @Override public ObservableList<String> getIANAProperties() { return ianaProperties; }
    private ObservableList<String> ianaProperties = FXCollections.observableArrayList();
    @Override public void setIANAProperties(ObservableList<String> iana) { ianaProperties = iana; }
    public T withIANAProperties(String... ianaProperties) { getIANAProperties().addAll(ianaProperties); return (T) this; }
}
