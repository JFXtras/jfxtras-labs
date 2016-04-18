package jfxtras.labs.icalendarfx.components;

import java.time.temporal.Temporal;
import java.util.Arrays;
import java.util.stream.Stream;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.properties.PropertyEnum;
import jfxtras.labs.icalendarfx.properties.component.change.DateTimeStamp;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Attachment;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Comment;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Description;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Summary;
import jfxtras.labs.icalendarfx.properties.component.misc.RequestStatus;
import jfxtras.labs.icalendarfx.properties.component.recurrence.RecurrenceRule;
import jfxtras.labs.icalendarfx.properties.component.recurrence.Recurrences;
import jfxtras.labs.icalendarfx.properties.component.relationship.Attendee;
import jfxtras.labs.icalendarfx.properties.component.relationship.Organizer;
import jfxtras.labs.icalendarfx.properties.component.relationship.UniformResourceLocator;
import jfxtras.labs.icalendarfx.properties.component.relationship.UniqueIdentifier;
import jfxtras.labs.icalendarfx.properties.component.time.DateTimeStart;

/**
 * Going to become a superclass of VEvent, VTodo and VJournal
 * 
 * @author david
 *
 * @param <T>
 * @see VEventNew
 * @see VTodo
 * @see VJournal
 * 
 * Note: can't be used for VAlarm
 */
public abstract class VComponentDescribableBase<T> extends VComponentBase<T> implements VComponentDescribable, VComponentPersonal, VComponentRepeatable
{
    /**
     * ATTACH
     * Attachment
     * RFC 5545, 3.8.1.1, page 80
     * 
     * This property provides the capability to associate a document object with a calendar component.
     * 
     * Examples:
     * ATTACH:CID:jsmith.part3.960817T083000.xyzMail@example.com
     * ATTACH;FMTTYPE=application/postscript:ftp://example.com/pub/
     *  reports/r-960812.ps
     * */
    @Override
    public ObservableList<Attachment<?>> getAttachments()
    {
        return attachments;
    }
    private ObservableList<Attachment<?>> attachments;
    @Override
    public void setAttachments(ObservableList<Attachment<?>> attachments) { this.attachments = attachments; }
    /** add comma separated attachments into separate comment objects */
    public T withAttachments(ObservableList<Attachment<?>> attachments) { setAttachments(attachments); return (T) this; }
    public T withAttachments(String...attachments)
    {
//        if (this.attachments == null)
//        {
//            this.attachments = FXCollections.observableArrayList();
//        }
        Arrays.stream(attachments).forEach(c -> PropertyEnum.ATTACHMENT.parse(this, c));
//            boolean isBinary = c.toUpperCase().contains("VALUE=BINARY");
//            if (isBinary)
//            {
//                getAttachments().add(new Attachment<String>(String.class, c));
//            } else
//            {
//                getAttachments().add(new Attachment<URI>(URI.class, c));
//            }
//        });
        return (T) this;
    }

    @Override
    public ObjectProperty<Description> descriptionProperty()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Description getDescription()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setDescription(Description description)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Summary getSummary()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ObjectProperty<Summary> summaryProperty()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setSummary(Summary summary)
    {
        // TODO Auto-generated method stub
        
    }
    
    /*
     * CONSTRUCTORS
     */
    VComponentDescribableBase() { }
    
    VComponentDescribableBase(String contentLines)
    {
        super(contentLines);
    }
    @Override
    public VComponentEnum componentType()
    {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public ObservableList<Comment> getComments()
    {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public void setComments(ObservableList<Comment> properties)
    {
        // TODO Auto-generated method stub
        
    }
    @Override
    public DateTimeStart<? extends Temporal> getDateTimeStart()
    {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public ObjectProperty<DateTimeStart<? extends Temporal>> dateTimeStartProperty()
    {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public void setDateTimeStart(DateTimeStart<? extends Temporal> dtStart)
    {
        // TODO Auto-generated method stub
        
    }
    @Override
    public ObservableList<Attendee> getAttendees()
    {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public void setAttendees(ObservableList<Attendee> properties)
    {
        // TODO Auto-generated method stub
        
    }
    @Override
    public DateTimeStamp getDateTimeStamp()
    {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public ObjectProperty<DateTimeStamp> dateTimeStampProperty()
    {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public void setDateTimeStamp(DateTimeStamp dtStamp)
    {
        // TODO Auto-generated method stub
        
    }
    @Override
    public Organizer getOrganizer()
    {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public ObjectProperty<Organizer> organizerProperty()
    {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public void setOrganizer(Organizer organizer)
    {
        // TODO Auto-generated method stub
        
    }
    @Override
    public ObservableList<RequestStatus> getRequestStatus()
    {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public void setRequestStatus(ObservableList<RequestStatus> properties)
    {
        // TODO Auto-generated method stub
        
    }
    @Override
    public UniqueIdentifier getUniqueIdentifier()
    {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public ObjectProperty<UniqueIdentifier> uniqueIdentifierProperty()
    {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public void setUniqueIdentifier(UniqueIdentifier uid)
    {
        // TODO Auto-generated method stub
        
    }
    @Override
    public UniformResourceLocator getUniformResourceLocator()
    {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public ObjectProperty<UniformResourceLocator> uniformResourceLocatorProperty()
    {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public void setUniformResourceLocator(UniformResourceLocator uri)
    {
        // TODO Auto-generated method stub
        
    }
    @Override
    public ObservableList<Recurrences<? extends Temporal>> getRecurrences()
    {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public void setRecurrences(ObservableList<Recurrences<? extends Temporal>> recurrences)
    {
        // TODO Auto-generated method stub
        
    }
    @Override
    public RecurrenceRule getRecurrenceRule()
    {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public ObjectProperty<RecurrenceRule> recurrenceRuleProperty()
    {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public void setRecurrenceRule(RecurrenceRule rRule)
    {
        // TODO Auto-generated method stub
        
    }
    @Override
    public Stream<Temporal> streamRecurrences(Temporal startTemporal)
    {
        // TODO Auto-generated method stub
        return null;
    }

}
