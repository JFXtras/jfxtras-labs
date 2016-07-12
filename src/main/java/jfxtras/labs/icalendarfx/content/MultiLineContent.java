package jfxtras.labs.icalendarfx.content;

import java.util.stream.Collectors;

import jfxtras.labs.icalendarfx.Orderer;

public class MultiLineContent extends ContentLineBase
{
    final private String firstContentLine;
    final private String lastContentLine;
    final private int builderSize;
    
    public MultiLineContent(
            Orderer orderer,
            String firstContentLine,
            String lastContentLine,
            int builderSize)
    {
        super(orderer);
        this.firstContentLine = firstContentLine;
        this.lastContentLine = lastContentLine;
        this.builderSize = builderSize;
    }
    
    @Override
    public String execute()
    {
        StringBuilder builder = new StringBuilder(builderSize);
        builder.append(firstContentLine + System.lineSeparator());
        String content = orderer().sortedContent().stream()
              .collect(Collectors.joining(System.lineSeparator()));
        if (content != null)
        {
            builder.append(content + System.lineSeparator());
        }
        builder.append(lastContentLine);
        return builder.toString();
    }
}
