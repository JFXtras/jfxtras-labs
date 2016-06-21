package jfxtras.labs.icalendarfx.components;

import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.properties.component.misc.IANAProperty;
import jfxtras.labs.icalendarfx.properties.component.misc.NonStandardProperty;

public abstract class VComponentCommonBase<T> extends VComponentBase implements VComponentCommon<T>
{
    /**
     * 3.8.8.2.  Non-Standard Properties
     * Any property name with a "X-" prefix
     * 
     * Example:
     * X-ABC-MMSUBJ;VALUE=URI;FMTTYPE=audio/basic:http://www.example.
     *  org/mysubj.au
     */
    @Override
    public ObservableList<NonStandardProperty> getNonStandardProperties() { return nonStandardProps; }
    private ObservableList<NonStandardProperty> nonStandardProps;
    @Override
    public void setNonStandardProperties(ObservableList<NonStandardProperty> nonStandardProps)
    {
        this.nonStandardProps = nonStandardProps;
//        nonStandardProps.addListener(sortOrderListChangeListener);
    }
    /** add comma separated nonStandardProps into separate nonStandardProps objects */
    
    /**
     * 3.8.8.1.  IANA Properties
     * An IANA-registered property name
     * 
     * Examples:
     * NON-SMOKING;VALUE=BOOLEAN:TRUE
     * DRESSCODE:CASUAL
     */
    @Override
    public ObservableList<IANAProperty> getIANAProperties() { return ianaProps; }
    private ObservableList<IANAProperty> ianaProps;
    @Override
    public void setIANAProperties(ObservableList<IANAProperty> ianaProps) { this.ianaProps = ianaProps; }

    /*
     * CONSTRUCTORS
     */
    VComponentCommonBase() { }
    
    /** Parse content lines into calendar component */
    VComponentCommonBase(String contentLines)
    {
        parseContent(contentLines);
    }
    
    /** Copy constructor */
    public VComponentCommonBase(VComponentCommonBase<T> source)
    {
        this();
        copyComponentFrom(source);
    }
    
    @Override
    void addListeners()
    {
        super.addListeners();
//        getNonStandardProperties().addListener(sortOrderListChangeListener);
//        getIANAProperties().addListener(sortOrderListChangeListener);
    }
}
