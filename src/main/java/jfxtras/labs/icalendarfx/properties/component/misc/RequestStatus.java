package jfxtras.labs.icalendarfx.properties.component.misc;

import java.text.DecimalFormat;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import jfxtras.labs.icalendarfx.components.VEventNew;
import jfxtras.labs.icalendarfx.components.VFreeBusy;
import jfxtras.labs.icalendarfx.components.VJournal;
import jfxtras.labs.icalendarfx.components.VTodo;
import jfxtras.labs.icalendarfx.properties.PropertyBaseLanguage;

/**
 * REQUEST-STATUS
 * RFC 5545, 3.8.8.3, page 141
 * 
 * This property defines the status code returned for a scheduling request.
 * 
 * Examples:
 * REQUEST-STATUS:2.0;Success
 * REQUEST-STATUS:3.1;Invalid property value;DTSTART:96-Apr-01
 * 
 * @author David Bal
 * 
 * The property can be specified in following components:
 * @see VEventNew
 * @see VTodo
 * @see VJournal
 * @see VFreeBusy
 */
public class RequestStatus extends PropertyBaseLanguage<String, RequestStatus>
{
    /** Hierarchical, numeric return status code
     * 
   +--------+----------------------------------------------------------+
   | Short  | Longer Return Status Description                         |
   | Return |                                                          |
   | Status |                                                          |
   | Code   |                                                          |
   +--------+----------------------------------------------------------+
   | 1.xx   | Preliminary success.  This class of status code          |
   |        | indicates that the request has been initially processed  |
   |        | but that completion is pending.                          |
   |        |                                                          |
   | 2.xx   | Successful.  This class of status code indicates that    |
   |        | the request was completed successfully.  However, the    |
   |        | exact status code can indicate that a fallback has been  |
   |        | taken.                                                   |
   |        |                                                          |
   | 3.xx   | Client Error.  This class of status code indicates that  |
   |        | the request was not successful.  The error is the result |
   |        | of either a syntax or a semantic error in the client-    |
   |        | formatted request.  Request should not be retried until  |
   |        | the condition in the request is corrected.               |
   |        |                                                          |
   | 4.xx   | Scheduling Error.  This class of status code indicates   |
   |        | that the request was not successful.  Some sort of error |
   |        | occurred within the calendaring and scheduling service,  |
   |        | not directly related to the request itself.              |
   +--------+----------------------------------------------------------+
    */
    public Double getStatusCode() { return statusCode.get(); }
    ObjectProperty<Double> statusCodeProperty() { return statusCode; }
    private ObjectProperty<Double> statusCode = new SimpleObjectProperty<Double>(this, "statcode");
    public void setStatusCode(Double statusCode) { this.statusCode.set(statusCode); }
    public RequestStatus withStatusCode(Double statusCode) { setStatusCode(statusCode); return this; }
    private final static DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.0#");
    
    /** Textual status description */
    public String getDescription() { return description.get(); }
    StringProperty descriptionProperty() { return description; }
    private StringProperty description = new SimpleStringProperty(this, "statdesc");
    public void setDescription(String description) { this.description.set(description); }
    public RequestStatus withDescription(String description) { setDescription(description); return this; }

    /** Textual exception data.  For example, the offending property name and value or complete property line. */
    public String getException() { return exception.get(); }
    StringProperty exceptionProperty() { return exception; }
    private StringProperty exception = new SimpleStringProperty(this, "extdata");
    public void setException(String exception) { this.exception.set(exception); }
    public RequestStatus withException(String exception) { setException(exception); return this; }
    
    public RequestStatus(CharSequence contentLine)
    {
        super(contentLine);
        setupListeners();
        updateParts(getValue());
    }

    public RequestStatus(RequestStatus source)
    {
        super(source);
        setupListeners();
        updateParts(getValue());
    }
    
    public RequestStatus()
    {
        super();
        setupListeners();
    }
    
    /*
     * LISTENERS
     * Used to keep statusCode, description and exception synchronized with the property value
     */
    private void setupListeners()
    {
        descriptionProperty().addListener(stringChangeListener);
        exceptionProperty().addListener(stringChangeListener);
        statusCodeProperty().addListener(doubleChangeListener);
        valueProperty().addListener(valueChangeListener);        
    }
    
    private final ChangeListener<? super String> valueChangeListener = (observable, oldValue, newValue) -> updateParts(newValue);
    
    private void updateParts(String newValue)
    {
        descriptionProperty().removeListener(stringChangeListener);
        exceptionProperty().removeListener(stringChangeListener);
        statusCodeProperty().removeListener(doubleChangeListener);
        String[] values = newValue.split(";");
        setStatusCode(Double.parseDouble(values[0]));
        setDescription(values[1]);
        if (values.length == 3)
        {
            setException(values[2]);
        }
        descriptionProperty().addListener(stringChangeListener);
        exceptionProperty().addListener(stringChangeListener);
        statusCodeProperty().addListener(doubleChangeListener);
    }
    
    private final ChangeListener<? super String> stringChangeListener = (observable, oldValue, newValue) -> buildNewValue();
    
    private final ChangeListener<? super Double> doubleChangeListener = (observable, oldValue, newValue) -> buildNewValue();
    
    private void buildNewValue()
    {
        valueProperty().removeListener(valueChangeListener);
        StringBuilder builder = new StringBuilder(100);
        builder.append(DECIMAL_FORMAT.format(getStatusCode()) + ";");
        builder.append(getDescription());
        if (getException() != null)
        {
            builder.append(";" + getException());            
        }
        setValue(builder.toString());
        valueProperty().addListener(valueChangeListener);
    }    
}
