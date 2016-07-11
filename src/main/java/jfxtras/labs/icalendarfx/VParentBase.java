package jfxtras.labs.icalendarfx;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javafx.util.Callback;
import jfxtras.labs.icalendarfx.content.ContentLineStrategy;

/**
 * Base class for parent calendar components.  Uses an {@link Orderer} to keep track of child object order.  Ordering
 * requires registering {@link Orderer} listeners to child properties.
 * 
 * @author David Bal
 */
public abstract class VParentBase implements VParent
{
    /*
     * SORT ORDER FOR CHILD ELEMENTS
     */
    final private Orderer orderer = new OrdererBase(this);
    public Orderer orderer() { return orderer; }
    
    private ContentLineStrategy contentLineGenerator;
    protected void setContentLineGenerator(ContentLineStrategy contentLineGenerator)
    {
        System.out.println("set22");
        this.contentLineGenerator = contentLineGenerator;
        System.out.println("set223" + contentLineGenerator);
    }
    
//    /*
//     * CONSTRUCTOR
//     */
//    public VParentBase(ContentLineStrategy contentLineGenerator)
//    {
//        orderer = new OrdererBase(this);
//        this.contentLineGenerator = contentLineGenerator;
//    }
    
    /** Strategy to copy subclass's children */
    protected Callback<VChild, Void> copyChildCallback()
    {
        throw new RuntimeException("Copy child callback not overridden in subclass " + this.getClass());
    };

    /** returns read-only collection of child elements following the sort order controlled by {@link Orderer} */
    @Override
    public Collection<VChild> childrenUnmodifiable()
    {
        return Collections.unmodifiableList(
                orderer().elementSortOrderMap().entrySet().stream()
                .sorted((Comparator<? super Entry<VChild, Integer>>) (e1, e2) -> e1.getValue().compareTo(e2.getValue()))
                .map(e -> e.getKey())
                .collect(Collectors.toList())
                );
    }
    
    /** Copy parameters, properties, and subcomponents from source into this component,
    * essentially making a copy of source 
    * 
    * Note: this method only overwrites properties found in source.  If there are properties in
    * this component that are not present in source then those will remain unchanged.
    * */
    @Override
    public void copyChildrenFrom(VParent source)
    {
         source.childrenUnmodifiable().forEach((e) -> copyChildCallback().call(e));
    }
    
    @Override
    public List<String> errors()
    {
        return childrenUnmodifiable().stream()
                .flatMap(c -> c.errors().stream())
                .collect(Collectors.toList());
    }

//    final private String contentDelimeter;
//    final private String lineDelimeter; // 
    @Override
    public String toContent()
    {
        if (contentLineGenerator == null)
        {
            throw new RuntimeException("Can't produce content lines before contentLineGenerator is set");
        }
        return contentLineGenerator.execute();
//        StringBuilder builder = getContentBuilder();
//        builder.append(firstContentLine());
//        String content = orderer().sortedContent().stream()
//              .collect(Collectors.joining(contentDelimeter));
////        String content = childrenUnmodifiable().stream()
////                .map(c -> c.toContent())
////                .collect(Collectors.joining(System.lineSeparator()));
//        if (content != null)
//        {
//            builder.append(content + lineDelimeter);
//        }
//        builder.append(lastContentLine());
//        return builder.toString();
    }
//    abstract protected StringBuilder getContentBuilder();
//    protected String firstContentLine() { return null; }
//    protected String lastContentLine() { return null; }
}
