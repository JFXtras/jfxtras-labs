package jfxtras.labs.icalendarfx.property;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jfxtras.labs.icalendarfx.properties.component.descriptive.Comment;

public class CommentTest
{
    @Test
    public void canParseComment()
    {
        String content = "COMMENT:The meeting needs to be canceled";
        Comment madeProperty = Comment.parse(content);
        assertEquals(content, madeProperty.toContentLines());
        Comment expectedProperty = Comment.parse("The meeting needs to be canceled");
        assertEquals(expectedProperty, madeProperty);
    }
    
    @Test
    public void canParseComment2()
    {
        String content = "COMMENT;ALTREP=\"CID:part3.msg.970415T083000@example.com\";LANGUAGE=en:The meeting needs to be canceled";
        Comment madeProperty = Comment.parse(content);
        assertEquals(content, madeProperty.toContentLines());
        Comment expectedProperty = Comment.parse("The meeting needs to be canceled")
                .withAlternateText("CID:part3.msg.970415T083000@example.com")
                .withLanguage("en");
        assertEquals(expectedProperty, madeProperty);
    }
    
    @Test
    public void canParseComment3()
    {
        String content = "The meeting needs to be canceled";
        Comment madeProperty = Comment.parse(content);
        assertEquals("COMMENT:" + content, madeProperty.toContentLines());
        Comment expectedProperty = Comment.parse("The meeting needs to be canceled");
        assertEquals(expectedProperty, madeProperty);
    }
}
