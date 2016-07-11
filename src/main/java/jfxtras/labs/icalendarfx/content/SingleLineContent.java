package jfxtras.labs.icalendarfx.content;

import java.util.stream.Collectors;

import jfxtras.labs.icalendarfx.Orderer;

public class SingleLineContent extends ContentLineBase
{
    final private int builderSize;
    final private String name;
    
    public SingleLineContent(
            Orderer orderer,
            String name,
            int builderSize)
    {
        super(orderer);
        this.name = name;
        this.builderSize = builderSize;
    }
    
    @Override
    public String execute()
    {
        StringBuilder builder = new StringBuilder(builderSize);
        builder.append(name);
        builder.append(orderer().sortedContent().stream()
                .collect(Collectors.joining(";")));
        return builder.toString();
    }
}
