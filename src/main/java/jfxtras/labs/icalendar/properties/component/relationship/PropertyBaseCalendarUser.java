package jfxtras.labs.icalendar.properties.component.relationship;

import java.net.URI;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import jfxtras.labs.icalendar.parameters.CommonName;
import jfxtras.labs.icalendar.parameters.DirectoryEntryReference;
import jfxtras.labs.icalendar.parameters.ParameterEnum;
import jfxtras.labs.icalendar.parameters.SentBy;
import jfxtras.labs.icalendar.properties.PropertyBaseLanguage;
import jfxtras.labs.icalendar.properties.PropertyCalendarUser;

/**
 * Abstract class for properties with a CAL-ADDRESS value.
 * The value is stored as a URI object
 * 
 * CAL-ADDRESS
 * Calendar User Address
 * RFC 5545, 3.3.3, page 31
 *  
 * This value type is used to identify properties that contain a calendar user address.
 * The email address of a calendar user.
 * 
 * Example: mailto:jane_doe@example.com
 * 
 * @author David Bal
 *
 * @param <U> - subclass
 * @param <T> - property value type
 * 
 * concrete subclasses
 * @see Organizer
 * @see Attendee
 */
public abstract class PropertyBaseCalendarUser<U, T> extends PropertyBaseLanguage<U, T> implements PropertyCalendarUser<T>
{
    /**
     * CN
     * Common Name
     * RFC 5545, 3.2.2, page 15
     * 
     * To specify the common name to be associated with the calendar user specified by the property.
     * 
     * Example:
     * ORGANIZER;CN="John Smith":mailto:jsmith@example.com
     */
    @Override
    public CommonName getCommonName() { return (commonName == null) ? null : commonName.get(); }
    @Override
    public ObjectProperty<CommonName> commonNameProperty()
    {
        if (commonName == null)
        {
            commonName = new SimpleObjectProperty<>(this, ParameterEnum.COMMON_NAME.toString());
        }
        return commonName;
    }
    private ObjectProperty<CommonName> commonName;
    @Override
    public void setCommonName(CommonName commonName)
    {
        if (commonName != null)
        {
            commonNameProperty().set(commonName);
        }
    }
    public void setCommonName(String content) { setCommonName(new CommonName(content)); }
    public U withCommonName(CommonName commonName) { setCommonName(commonName); return (U) this; }
    public U withCommonName(String content) { setCommonName(content); return (U) this; }    

    /**
     * DIR
     * Directory Entry Reference
     * RFC 5545, 3.2.6, page 18
     * 
     * To specify reference to a directory entry associated with
     *     the calendar user specified by the property.
     * 
     * Example:
     * ORGANIZER;DIR="ldap://example.com:6666/o=ABC%20Industries,
     *  c=US???(cn=Jim%20Dolittle)":mailto:jimdo@example.com
     */
    @Override
    public DirectoryEntryReference getDirectoryEntryReference() { return (directoryEntryReference == null) ? null : directoryEntryReference.get(); }
    @Override
    public ObjectProperty<DirectoryEntryReference> directoryEntryReferenceProperty()
    {
        if (directoryEntryReference == null)
        {
            directoryEntryReference = new SimpleObjectProperty<>(this, ParameterEnum.COMMON_NAME.toString());
        }
        return directoryEntryReference;
    }
    private ObjectProperty<DirectoryEntryReference> directoryEntryReference;
    @Override
    public void setDirectoryEntryReference(DirectoryEntryReference directoryEntryReference)
    {
        if (directoryEntryReference != null)
        {
            directoryEntryReferenceProperty().set(directoryEntryReference);
        }
    }
    public void setDirectoryEntryReference(String content) { setDirectoryEntryReference(new DirectoryEntryReference(content)); }
    public U withDirectoryEntryReference(DirectoryEntryReference directoryEntryReference) { setDirectoryEntryReference(directoryEntryReference); return (U) this; }
    public U withDirectoryEntryReference(URI uri) { setDirectoryEntryReference(new DirectoryEntryReference(uri)); return (U) this; }
    public U withDirectoryEntryReference(String content) { setDirectoryEntryReference(content); return (U) this; }

    /**
     * SENT-BY
     * RFC 5545, 3.2.18, page 27
     * 
     * To specify the calendar user that is acting on behalf of
     * the calendar user specified by the property.
     * 
     * Example:
     * ORGANIZER;SENT-BY="mailto:sray@example.com":mailto:
     *  jsmith@example.com
     */
    @Override
    public SentBy getSentBy() { return (sentBy == null) ? null : sentBy.get(); }
    @Override
    public ObjectProperty<SentBy> sentByProperty()
    {
        if (sentBy == null)
        {
            sentBy = new SimpleObjectProperty<>(this, ParameterEnum.SENT_BY.toString());
        }
        return sentBy;
    }
    private ObjectProperty<SentBy> sentBy;
    @Override
    public void setSentBy(SentBy sentBy)
    {
        if (sentBy != null)
        {
            sentByProperty().set(sentBy);
        }
    }
    public void setSentBy(String content) { setSentBy(new SentBy(content)); }
    public U withSentBy(SentBy sentBy) { setSentBy(sentBy); return (U) this; }
    public U withSentBy(URI uri) { setSentBy(new SentBy(uri)); return (U) this; }
    public U withSentBy(String content) { setSentBy(content); return (U) this; }    

    
    /*
     * CONSTRUCTORS
     */    
    protected PropertyBaseCalendarUser(CharSequence contentLine)
    {
        super(contentLine);
    }

    // copy constructor
    public PropertyBaseCalendarUser(PropertyBaseCalendarUser<U, T> property)
    {
        super(property);
    }
    
    public PropertyBaseCalendarUser(T value)
    {
        super(value);
    }
    
//    public PropertyCalendarUserAddress() { super(); }
}