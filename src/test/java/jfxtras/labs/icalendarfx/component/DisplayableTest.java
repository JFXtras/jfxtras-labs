package jfxtras.labs.icalendarfx.component;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.Arrays;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.components.VComponentLastModified;
import jfxtras.labs.icalendarfx.components.VEventNew;
import jfxtras.labs.icalendarfx.components.VJournal;
import jfxtras.labs.icalendarfx.components.VTodo;
import jfxtras.labs.icalendarfx.properties.component.change.DateTimeCreated;
import jfxtras.labs.icalendarfx.properties.component.change.LastModified;
import jfxtras.labs.icalendarfx.properties.component.change.Sequence;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Attachment;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Categories;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Classification;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Classification.ClassificationType;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Status.StatusType;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Summary;
import jfxtras.labs.icalendarfx.properties.component.recurrence.Exceptions;
import jfxtras.labs.icalendarfx.properties.component.recurrence.RecurrenceRule;
import jfxtras.labs.icalendarfx.properties.component.recurrence.Recurrences;
import jfxtras.labs.icalendarfx.properties.component.relationship.Contact;
import jfxtras.labs.icalendarfx.properties.component.relationship.RecurrenceId;
import jfxtras.labs.icalendarfx.properties.component.relationship.RelatedTo;
import jfxtras.labs.icalendarfx.properties.component.time.DateTimeStart;

/**
 * Test following components:
 * @see VEventNew
 * @see VTodo
 * @see VJournal
 * 
 * for the following properties:
 * @see Attachment - extended from Describable
 * @see Categories
 * @see Classification
 * @see Contact
 * @see DateTimeCreated
 * @see Exceptions
 * @see LastModified - extended from LastModified
 * @see RecurrenceId
 * @see RecurrenceRule - extended from Repeatable
 * @see Recurrences - extended from Repeatable
 * @see RelatedTo
 * @see Sequence
 * @see Status
 * @see Summary - extended from Describable
 * 
 * @author David Bal
 *
 */
public class DisplayableTest
{
    @Test
    public void canBuildDisplayable() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
    {
        List<VComponentLastModified<?>> components = Arrays.asList(
                new VEventNew()
                    .withStatus(StatusType.NEEDS_ACTION)
                    .withSequence(2)
                    .withRelatedTo("jsmith.part7.19960817T083000.xyzMail@example.com",
                            "RELATED-TO;RELTYPE=SIBLING:19960401-080045-4000F192713@example.com")
                    .withRecurrenceId(ZonedDateTime.of(LocalDateTime.of(2016, 1, 1, 12, 0), ZoneId.of("Z")))
                    .withSummary("a test summary")
                    .withRecurrences("RDATE:19960302T010000Z,19960303T010000Z")
                    .withRecurrenceRule("RRULE:FREQ=DAILY")
                    .withDateTimeLastModified("20160306T080000Z")
                    .withAttachments("ATTACH:CID:jsmith.part3.960817T083000.xyzMail@example.com")
                    .withDateTimeCreated("20160420T080000Z")
                    .withCategories("group03","group04","group05")
                    .withCategories("group06")
                    .withClassification(ClassificationType.PUBLIC)
                    .withContacts("CONTACT:Jim Dolittle\\, ABC Industries\\, +1-919-555-1234", "Harry Potter\\, Hogwarts\\, by owl")
                    .withExceptions("EXDATE:19960402T010000Z,19960403T010000Z,19960404T010000Z"),
                new VTodo()
                    .withStatus(StatusType.NEEDS_ACTION)
                    .withSequence(2)
                    .withRelatedTo("jsmith.part7.19960817T083000.xyzMail@example.com",
                            "RELATED-TO;RELTYPE=SIBLING:19960401-080045-4000F192713@example.com")
                    .withRecurrenceId(ZonedDateTime.of(LocalDateTime.of(2016, 1, 1, 12, 0), ZoneId.of("Z")))
                    .withSummary("a test summary")
                    .withRecurrences("RDATE:19960302T010000Z,19960303T010000Z")
                    .withRecurrenceRule("RRULE:FREQ=DAILY")
                    .withDateTimeLastModified("20160306T080000Z")
                    .withAttachments("ATTACH:CID:jsmith.part3.960817T083000.xyzMail@example.com")
                    .withDateTimeCreated("20160420T080000Z")
                    .withCategories("group03","group04","group05")
                    .withCategories("group06")
                    .withClassification(ClassificationType.PUBLIC)
                    .withContacts("CONTACT:Jim Dolittle\\, ABC Industries\\, +1-919-555-1234", "Harry Potter\\, Hogwarts\\, by owl")
                    .withExceptions("EXDATE:19960402T010000Z,19960403T010000Z,19960404T010000Z"),
                new VJournal()
                    .withStatus(StatusType.NEEDS_ACTION)
                    .withSequence(2)
                    .withRelatedTo("jsmith.part7.19960817T083000.xyzMail@example.com",
                            "RELATED-TO;RELTYPE=SIBLING:19960401-080045-4000F192713@example.com")
                    .withRecurrenceId(ZonedDateTime.of(LocalDateTime.of(2016, 1, 1, 12, 0), ZoneId.of("Z")))
                    .withSummary("a test summary")
                    .withRecurrences("RDATE:19960302T010000Z,19960303T010000Z")
                    .withRecurrenceRule("RRULE:FREQ=DAILY")
                    .withDateTimeLastModified("20160306T080000Z")
                    .withAttachments("ATTACH:CID:jsmith.part3.960817T083000.xyzMail@example.com")
                    .withDateTimeCreated("20160420T080000Z")
                    .withCategories("group03","group04","group05")
                    .withCategories("group06")
                    .withClassification(ClassificationType.PUBLIC)
                    .withContacts("CONTACT:Jim Dolittle\\, ABC Industries\\, +1-919-555-1234", "Harry Potter\\, Hogwarts\\, by owl")
                    .withExceptions("EXDATE:19960402T010000Z,19960403T010000Z,19960404T010000Z")
                );
        
        for (VComponentLastModified<?> builtComponent : components)
        {
            String componentName = builtComponent.componentType().toString();
            String expectedContent = "BEGIN:" + componentName + System.lineSeparator() +
                    "ATTACH:CID:jsmith.part3.960817T083000.xyzMail@example.com" + System.lineSeparator() +
                    "CATEGORIES:group03,group04,group05" + System.lineSeparator() +
                    "CATEGORIES:group06" + System.lineSeparator() +
                    "CLASS:PUBLIC" + System.lineSeparator() +
                    "CONTACT:Jim Dolittle\\, ABC Industries\\, +1-919-555-1234" + System.lineSeparator() +
                    "CONTACT:Harry Potter\\, Hogwarts\\, by owl" + System.lineSeparator() +
                    "CREATED:20160420T080000Z" + System.lineSeparator() +
                    "EXDATE:19960402T010000Z,19960403T010000Z,19960404T010000Z" + System.lineSeparator() +
                    "LAST-MODIFIED:20160306T080000Z" + System.lineSeparator() +
                    "RDATE:19960302T010000Z,19960303T010000Z" + System.lineSeparator() +
                    "RECURRENCE-ID:20160101T120000Z" + System.lineSeparator() +
                    "RELATED-TO:jsmith.part7.19960817T083000.xyzMail@example.com" + System.lineSeparator() +
                    "RELATED-TO;RELTYPE=SIBLING:19960401-080045-4000F192713@example.com" + System.lineSeparator() +
                    "RRULE:FREQ=DAILY" + System.lineSeparator() +
                    "SEQUENCE:2" + System.lineSeparator() +
                    "STATUS:NEEDS-ACTION" + System.lineSeparator() +
                    "SUMMARY:a test summary" + System.lineSeparator() +
                    "END:" + componentName;
                    
            VComponentLastModified<?> parsedComponent = builtComponent
                    .getClass()
                    .getConstructor(String.class)
                    .newInstance(expectedContent);
            assertEquals(parsedComponent, builtComponent);
            assertEquals(expectedContent, builtComponent.toContentLines());            
        }
    }
    
    @Test (expected = DateTimeException.class)
    @Ignore // can't catch exception in listener
    public void canHandleDTStartTypeChange()
    {
        VEventNew component = new VEventNew()
            .withDateTimeStart(LocalDate.of(1997, 3, 1))
            .withExceptions("EXDATE;VALUE=DATE:19970304,19970504,19970704,19970904");
//      Platform.runLater(() -> component.setDateTimeStart("20160302T223316Z"));      
        component.setDateTimeStart("20160302T223316Z"); // invalid
    }
    
    @Test (expected = DateTimeException.class)
    public void canCatchWrongDateType()
    {
        VEventNew component = new VEventNew()
                .withDateTimeStart(LocalDate.of(1997, 3, 1));
        ObservableList<Exceptions<? extends Temporal>> exceptions = FXCollections.observableArrayList();
        exceptions.add(new Exceptions<LocalDateTime>("20160228T093000"));
        component.setExceptions(exceptions); // invalid        
    }
    
    @Test //(expected = DateTimeException.class)
    @Ignore // JUnit won't recognize exception - exception is thrown in listener is cause
    public void canCatchWrongRecurrenceIdType()
    {
        new VEventNew()
                .withDateTimeStart(LocalDate.of(1997, 3, 1))
                .withRecurrenceId("20160306T080000Z");
    }
    
    @Test (expected = DateTimeException.class)
    @Ignore // JUnit won't recognize exception - exception is thrown in listener is cause
    public void canCatchWrongRecurrenceIdType2()
    {
       new VEventNew()
                .withRecurrenceId("20160306T080000Z")
                .withDateTimeStart(LocalDate.of(1997, 3, 1));
    }
    
    @Test (expected = DateTimeException.class)
    @Ignore // JUnit won't recognize exception - exception is thrown in listener is cause
    public void canCatchWrongRecurrenceIdType3()
    {
        VEventNew builtComponent = new VEventNew();
        builtComponent.setDateTimeStart(new DateTimeStart<LocalDate>(LocalDate.of(1997, 3, 1)));
        builtComponent.setRecurrenceId(new RecurrenceId<LocalDateTime>(LocalDateTime.of(2016, 3, 6, 8, 0)));
    }
}
