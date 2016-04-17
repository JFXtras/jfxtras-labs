package jfxtras.labs.icalendarfx.property;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jfxtras.labs.icalendarfx.properties.component.misc.RequestStatus;

public class RequestStatusTest
{
    @Test
    public void canParseRequestStatus()
    {
        String content = "REQUEST-STATUS:2.0;Success";
        RequestStatus madeProperty = new RequestStatus(content);
        assertEquals(content, madeProperty.toContentLine());
        RequestStatus expectedProperty = new RequestStatus()
                .withStatusCode(2.0)
                .withDescription("Success");
        assertEquals(expectedProperty, madeProperty);
        
        madeProperty.setStatusCode(2.81);
        assertEquals("REQUEST-STATUS:2.81;Success", madeProperty.toContentLine());
        
        madeProperty.setValue("3.7;Invalid calendar user;ATTENDEE:mailto:jsmith@example.com");
        assertEquals((Double) 3.7, madeProperty.getStatusCode());
        assertEquals("Invalid calendar user", madeProperty.getDescription());
        assertEquals("ATTENDEE:mailto:jsmith@example.com", madeProperty.getException());

    }
}
