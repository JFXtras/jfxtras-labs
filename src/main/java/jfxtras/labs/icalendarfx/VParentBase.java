package jfxtras.labs.icalendarfx;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
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
    
    /* Strategy to build iCalendar content lines */
    private ContentLineStrategy contentLineGenerator;
    protected void setContentLineGenerator(ContentLineStrategy contentLineGenerator)
    {
        this.contentLineGenerator = contentLineGenerator;
    }
    
    /** Strategy to copy subclass's children */
    protected Callback<VChild, Void> copyChildCallback()
    {
        throw new RuntimeException("Copy child callback is not overridden in subclass " + this.getClass());
    };

    /** returns read-only list of child elements following the sort order controlled by {@link Orderer} */
    @Override
    public List<VChild> childrenUnmodifiable()
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
//        orderer().elementSortOrderMap().clear();
        // TODO - FIND WAY TO ORDER UNIQUE CHILDREN
        source.childrenUnmodifiable().forEach((e) -> copyChildCallback().call(e));
    }
    
    @Override
    public List<String> errors()
    {
        return childrenUnmodifiable().stream()
                .flatMap(c -> c.errors().stream())
                .collect(Collectors.toList());
    }

    @Override
    public String toContent()
    {
        if (contentLineGenerator == null)
        {
            throw new RuntimeException("Can't produce content lines before contentLineGenerator is set");
        }
        return contentLineGenerator.execute();
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (obj == this) return true;
        if((obj == null) || (obj.getClass() != getClass())) {
            return false;
        }
        VParent testObj = (VParent) obj;
        
        Collection<VChild> c1 = childrenUnmodifiable();
        Collection<VChild> c2 = testObj.childrenUnmodifiable();
        if (c1.size() == c2.size())
        {
            Iterator<VChild> i1 = childrenUnmodifiable().iterator();
            Iterator<VChild> i2 = testObj.childrenUnmodifiable().iterator();
            for (int i=0; i<c1.size(); i++)
            {
                VChild child1 = i1.next();
                VChild child2 = i2.next();
                if (! child1.equals(child2))
                {
                    return false;
                }
            }
        } else
        {
            return false;
        }
        return true;
    }
    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        for (VChild child : childrenUnmodifiable())
        {
            result = prime * result + child.hashCode();
        }
        return result;
    }
}
