package jfxtras.labs.util;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.junit.Assert;
import org.junit.Test;

import java.util.function.Function;

/**
 * Created by carrknight on 4/26/14.
 */
public class HeterogeneousBidirectionalBinderTest {
    private static enum TestEnum{ NEGATIVE,POSITIVE}

    @Test
    public void simpleBindingTest() throws Exception {
        //binding an enum and a number
        Property<TestEnum> enumProperty = new SimpleObjectProperty<>(TestEnum.NEGATIVE);
        Property<Number> integerProperty = new SimpleIntegerProperty(10);
        Function<Number,TestEnum> toEnum = number -> number.intValue()>=0 ? TestEnum.POSITIVE : TestEnum.NEGATIVE;
        Function<TestEnum,Number> toNumber = testEnum -> testEnum.equals(TestEnum.POSITIVE) ? 1 : -1;
        //create the binding
        HeterogeneousBidirectionalBinder<TestEnum,Number> binder =
                new HeterogeneousBidirectionalBinder<>(enumProperty,integerProperty,toNumber,toEnum);
        //like the original double-binding, when binder is created the property1 is set to property 2
        Assert.assertEquals(TestEnum.POSITIVE, enumProperty.getValue());

        //if i change the integer property, i will see enum property change
        integerProperty.setValue(-5);
        Assert.assertEquals(TestEnum.NEGATIVE, enumProperty.getValue());
        integerProperty.setValue(5);
        Assert.assertEquals(TestEnum.POSITIVE, enumProperty.getValue());
        integerProperty.setValue(-5);
        Assert.assertEquals(TestEnum.NEGATIVE,enumProperty.getValue());

        //if I change the enum, i will change the integer
        enumProperty.setValue(TestEnum.POSITIVE);
        Assert.assertEquals(integerProperty.getValue().intValue(),1);
        enumProperty.setValue(TestEnum.NEGATIVE);
        Assert.assertEquals(integerProperty.getValue().intValue(),-1);

        //if i unbind, this stops being true
        binder.unbind(); //notice that it's not the static method that gets called
        integerProperty.setValue(5);
        Assert.assertEquals(TestEnum.NEGATIVE, enumProperty.getValue());
        enumProperty.setValue(TestEnum.POSITIVE);
        Assert.assertEquals(integerProperty.getValue().intValue(),5);


    }

    @Test
    public void conflictingBinding() throws Exception {
        //binding an enum and a number
        Property<TestEnum> enumProperty = new SimpleObjectProperty<>(TestEnum.NEGATIVE);
        Property<Number> integerProperty = new SimpleIntegerProperty(10);
        //the usual transfomers
        Function<Number, TestEnum> toEnum = number -> number.intValue() >= 0 ? TestEnum.POSITIVE : TestEnum.NEGATIVE;
        Function<TestEnum, Number> toNumber = testEnum -> testEnum.equals(TestEnum.POSITIVE) ? 1 : -1;
        //the inverse transformers.
        Function<Number, TestEnum> toEnum2 = number -> number.intValue() < 0 ? TestEnum.POSITIVE : TestEnum.NEGATIVE;
        Function<TestEnum, Number> toNumber2 = testEnum -> testEnum.equals(TestEnum.NEGATIVE) ? 1 : -1;
        //create the binding
        HeterogeneousBidirectionalBinder<TestEnum, Number> binder =
                new HeterogeneousBidirectionalBinder<>(enumProperty, integerProperty, toNumber, toEnum);
        //like the original double-binding, when binder is created the property1 is set to property 2
        Assert.assertEquals(TestEnum.POSITIVE, enumProperty.getValue());

        //if i change the integer property, i will see enum property change
        integerProperty.setValue(-5);
        Assert.assertEquals(TestEnum.NEGATIVE, enumProperty.getValue());
        integerProperty.setValue(5);
        Assert.assertEquals(TestEnum.POSITIVE, enumProperty.getValue());
        integerProperty.setValue(-5);
        Assert.assertEquals(TestEnum.NEGATIVE, enumProperty.getValue());

        binder.unbind();
        HeterogeneousBidirectionalBinder<TestEnum, Number> binder2 =
                new HeterogeneousBidirectionalBinder<>(enumProperty, integerProperty, toNumber2, toEnum2);
        integerProperty.setValue(5);
        Assert.assertEquals(TestEnum.NEGATIVE, enumProperty.getValue());
        integerProperty.setValue(-5);
        Assert.assertEquals(TestEnum.POSITIVE, enumProperty.getValue());



        HeterogeneousBidirectionalBinder<TestEnum, Number> binder3 =
                new HeterogeneousBidirectionalBinder<>(enumProperty, integerProperty, toNumber, toEnum);
        binder2.unbind();
        binder.unbind(); //this is ignored. Notice that this is the big difference with the Bindings class since that uses
        //statics all over the place and is forced to use deep equals to unbind correctly.

        integerProperty.setValue(5);
        Assert.assertEquals(TestEnum.POSITIVE, enumProperty.getValue());
        integerProperty.setValue(-5);
        Assert.assertEquals(TestEnum.NEGATIVE, enumProperty.getValue());





    }

}
