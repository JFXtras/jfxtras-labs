package jfxtras.labs.util;

import javafx.beans.WeakListener;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.lang.ref.WeakReference;
import java.util.function.Function;

/**
 * A bidirectional object binding between two properties of different types.
 * It is a bindER rather than bindING for two reasons:
 * <ul>
 *     <li> We can't extend the original BidirectionalBinding since it has a private constructor </li>
 *     <li> It doesn't use static methods and it's a simple object you want to keep a reference around </li>
 * </ul>
 * The bind is effective from the constructor until unbind() is called. It is registered as a weaklistener to both properties.
 * Created by carrknight on 4/26/14.
 */
public class HeterogeneousBidirectionalBinder<A,B> implements ChangeListener<Object>, WeakListener {

    //the use of WeakReferences here is just to mimic BidirectionalBindings
    private final WeakReference<Property<A>> propertyRef1;
    private final WeakReference<Property<B>> propertyRef2;
    //here i use Transformer from Apache collection, but that's just such a simple interface i
    // can re-implement it without having to deal with dependencies
    private final Function<A,B> transformer1To2;
    private final Function<B,A> transformer2To1;

    //flag to avoid infinite recursion
    private boolean updating = false;



    private final int cachedHashCode;

    public HeterogeneousBidirectionalBinder(Property<A> property1, Property<B> property2,
                                            Function<A, B> transformer1To2, Function<B, A> transformer2To1) {
        initCheck(property1, property2,transformer1To2,transformer2To1);

        propertyRef1 = new WeakReference<>(property1);
        propertyRef2 = new WeakReference<>(property2);
        this.transformer1To2 = transformer1To2;
        this.transformer2To1 = transformer2To1;

        //well, start listening
        property1.setValue(transformer2To1.apply(property2.getValue()));
        property1.addListener(this);
        property2.addListener(this);


        cachedHashCode = propertyRef1.hashCode() * propertyRef2.hashCode() * transformer1To2.hashCode() * transformer2To1.hashCode();
    }

    private void initCheck(Property<A> property1, Property<B> property2,
                           Function<A, B> transformer1To2, Function<B, A> transformer2To1) {
        if(property1 == property2 ||property1 == null || property2 == null )
            throw new IllegalArgumentException("Properties must be different and null");
        if(transformer1To2 == null || transformer2To1 == null  )
            throw new IllegalArgumentException("Transformers can't be null!");


    }

    protected Property<A> getProperty1() {
        return propertyRef1.get();
    }

    protected Property<B> getProperty2() {
        return propertyRef2.get();
    }


    /**
     * here i forego generics entirely. Unfortunately I can't implement two typed ChangeListener interfaces, so this is the second best.
     * Admittely a far worse second, but hey.
     */
    @Override
    public void changed(ObservableValue sourceProperty, Object oldValue, Object newValue) {
        //this is basically copy-pasted from the javafx 8 source, commented at random by me
        if (!updating) {  //flag updating spares us from infinite recursion
            final Property<A> property1 = propertyRef1.get(); //get the two properties
            final Property<B> property2 = propertyRef2.get();
            if ((property1 == null) || (property2 == null)) {
                if (property1 != null) {
                    property1.removeListener(this); //don't bother listening if the other one is null
                }
                if (property2 != null) {
                    property2.removeListener(this);
                }
            } else {
                try {
                    updating = true; //set updating to true to avoid infinite recursion
                    if (property1 == sourceProperty) {
                        //grab the value, cast it, transform it and apply it!
                        A newTypedValue = (A) newValue;
                        property2.setValue(transformer1To2.apply(newTypedValue));
                    } else {
                        B newTypedValue = (B) newValue;
                        property1.setValue(transformer2To1.apply(newTypedValue));
                    }
                } catch (RuntimeException e) {
                    //if we fail, grab the old value, cast it, transform it and apply it.
                    if (property1 == sourceProperty) {
                        B oldTypedValue = (B) oldValue;
                        property1.setValue(transformer2To1.apply(oldTypedValue));
                    } else {
                        A oldTypedValue = (A) oldValue;
                        property2.setValue(transformer1To2.apply(oldTypedValue));
                    }
                    throw new RuntimeException(
                            "Bidirectional binding failed, setting to the previous value", e);
                } finally {
                    updating = false;
                }
            }
        }
    }


    @Override
    public int hashCode() {
        return cachedHashCode;
    }


    public void unbind(){

        final Property<A> property1 = propertyRef1.get();
        if(property1 != null)
            property1.removeListener(this);

        final Property<B> property2 = propertyRef2.get();
        if(property2!= null)
            property2.removeListener(this);
    }

    /**
     * copy pasted from the javafx8 source code: http://hg.openjdk.java.net/openjfx/8/master/rt/file/f89b7dc932af/modules/base/src/main/java/com/sun/javafx/binding/BidirectionalBinding.java
     * @return whether either property is null
     */
    @Override
    public boolean wasGarbageCollected() {
        return (getProperty1() == null) || (getProperty2() == null);
    }


    @Override
    public boolean equals(Object o) {
        return this == o;

        /*
         If I ever decide to build a parallel static structure like the original binders then I'll have to change
          the equals(o) to some deep-equality like below.
          The reason is that the static unbind() works by just calling removeListener(.) which in turn works through equals.

        if (o == null || getClass() != o.getClass()) return false;

        HeterogeneousBidirectionalBinder that = (HeterogeneousBidirectionalBinder) o;

        //if one is null and the other isn't
        return Objects.equals(this.propertyRef1.get(),that.propertyRef1.get())
                &&
                Objects.equals(this.propertyRef2.get(),that.propertyRef2.get())
                &&
                Objects.equals(this.transformer1To2,that.transformer1To2)
                &&
                Objects.equals(this.transformer2To1,that.transformer2To1);
*/
    }
}

