package jfxtras.labs.icalendarfx.components;

import java.util.Arrays;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.properties.PropertyEnum;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Description;

public class VJournal extends VComponentDisplayableBase<VJournal>
{
    @Override
    public VComponentEnum componentType()
    {
        return VComponentEnum.VJOURNAL;
    }
    
    /*
     * CONSTRUCTORS
     */
    public VJournal() { }
    
    public VJournal(String contentLines)
    {
        super(contentLines);
    }

    /**
     * DESCRIPTION:
     * RFC 5545 iCalendar 3.8.1.12. page 84
     * This property provides a more complete description of the
     * calendar component than that provided by the "SUMMARY" property.
     * Example:
     * DESCRIPTION:Meeting to provide technical review for "Phoenix"
     *  design.\nHappy Face Conference Room. Phoenix design team
     *  MUST attend this meeting.\nRSVP to team leader.
     *  
     *  VJournal can have multiple description properties.
     */
    public ObservableList<Description> getDescriptions() { return descriptions; }
    private ObservableList<Description> descriptions;
    public void setDescriptions(ObservableList<Description> descriptions) { this.descriptions = descriptions; }
    public VJournal withDescriptions(ObservableList<Description> descriptions) { setDescriptions(descriptions); return this; }
    public VJournal withDescriptions(String...descriptions)
    {
        Arrays.stream(descriptions).forEach(c -> PropertyEnum.DESCRIPTION.parse(this, c));
        return this;
    }
    public VJournal withDescriptions(Description...descriptions)
    {
        if (getDescriptions() == null)
        {
            setDescriptions(FXCollections.observableArrayList(descriptions));
        } else
        {
            getDescriptions().addAll(descriptions);
        }
        return this;
    }
}
