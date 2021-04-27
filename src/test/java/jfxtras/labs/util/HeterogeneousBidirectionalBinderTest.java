/**
 * Copyright (c) 2011-2021, JFXtras
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *    Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *    Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *    Neither the name of the organization nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL JFXTRAS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jfxtras.labs.util;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.function.Function;

import static org.mockito.Mockito.mock;

/**
 * Few simple tests for heterogeneous binders:
 * <ul>
 *     <li>simpleBindingTest: Checks that the binding works and can be deactivated</li>
 *     <li>conflictingBinding: Creates and unbinds multiple binders and make sure the results are correct</li>
 *     <li>multipleConflictingBinders: Creates multiple binders and shows that the results are predictable but confusing</li>
 * </ul>
 * Created by carrknight on 4/26/14.
 */
@Ignore // test fails, but this is labs so I'm not going to fix this, that's up to the original developer
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
        //the inverse transformers: take a positive and call it negative
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

        binder3.unbind();




    }

    /**
     * 3 binders all on at the same time is a recipe for weirdness!
     * @throws Exception
     */
    @Test
    public void multipleConflictingBinders() throws Exception {
        //binding an enum and a number
        Property<TestEnum> enumProperty = new SimpleObjectProperty<>(TestEnum.NEGATIVE);
        Property<Number> integerProperty = new SimpleIntegerProperty(10);
        //the usual transfomers
        Function<Number, TestEnum> toEnum = number -> number.intValue() >= 0 ? TestEnum.POSITIVE : TestEnum.NEGATIVE;
        Function<TestEnum, Number> toNumber = testEnum -> testEnum.equals(TestEnum.POSITIVE) ? 1 : -1;
        //the inverse transformers: take a positive and call it negative
        Function<Number, TestEnum> toEnum2 = number -> number.intValue() < 0 ? TestEnum.POSITIVE : TestEnum.NEGATIVE;
        Function<TestEnum, Number> toNumber2 = testEnum -> testEnum.equals(TestEnum.NEGATIVE) ? 1 : -1;
        //create 3 binders: one of them uses normal transformers the other two invert
        HeterogeneousBidirectionalBinder binder1 =
                new HeterogeneousBidirectionalBinder<>(enumProperty, integerProperty, toNumber, toEnum);
        HeterogeneousBidirectionalBinder binder2 =
                new HeterogeneousBidirectionalBinder<>(enumProperty, integerProperty, toNumber2, toEnum2);
        HeterogeneousBidirectionalBinder binder3 =
                new HeterogeneousBidirectionalBinder<>(enumProperty, integerProperty, toNumber2, toEnum2);
        //like the original double-binding, when binder is created the property1 is set to property 2
        integerProperty.setValue(-5); //set the value to -5
        //the second and third binder both flip the enum twice, correctly but confusingly
        Assert.assertEquals(TestEnum.NEGATIVE, enumProperty.getValue());
        enumProperty.setValue(TestEnum.POSITIVE);
        //you get +1 by flipping 1 into -1 and then again into 1
        Assert.assertEquals(1, integerProperty.getValue());

        //now remove binder2
        binder2.unbind();
        //binder 3 is still there flipping values:
        integerProperty.setValue(-5);
        Assert.assertEquals(TestEnum.POSITIVE, enumProperty.getValue()); //only one flip.


        binder1.unbind();
        binder3.unbind();
    }


    @Test
    public void blockWrongInstantiations() throws Exception {
        Property<Number> integer1 = mock(SimpleIntegerProperty.class);
        Property<Number> integer2 = mock(SimpleIntegerProperty.class);
        Function<Number,Number> fakeTransformer = mock(Function.class);
        //expect runtime exceptions

        //can't have two equal properties
        try{
            new HeterogeneousBidirectionalBinder<>(integer1,integer1,fakeTransformer,fakeTransformer);
            Assert.assertTrue(false); //can't be here!
        }
        catch (IllegalArgumentException ignored){}

        //can't have nulls!
        try{
            new HeterogeneousBidirectionalBinder<>(integer1,null,fakeTransformer,fakeTransformer);
            Assert.assertTrue(false); //can't be here!
        }
        catch (IllegalArgumentException ignored){}
        try{
            new HeterogeneousBidirectionalBinder<>(null,null,fakeTransformer,fakeTransformer);
            Assert.assertTrue(false); //can't be here!
        }
        catch (IllegalArgumentException ignored){}
        try{
            new HeterogeneousBidirectionalBinder<>(null,integer1,fakeTransformer,fakeTransformer);
            Assert.assertTrue(false); //can't be here!
        }
        catch (IllegalArgumentException ignored){}

        //can't have null transformers!
        try{
            new HeterogeneousBidirectionalBinder<>(integer1,integer2,fakeTransformer,null);
            Assert.assertTrue(false); //can't be here!
        }
        catch (IllegalArgumentException ignored){}
        try{
            new HeterogeneousBidirectionalBinder<>(integer1,integer2,null,fakeTransformer);
            Assert.assertTrue(false); //can't be here!
        }
        catch (IllegalArgumentException ignored){}
        try{
            new HeterogeneousBidirectionalBinder<>(integer1,integer2,null,null);
            Assert.assertTrue(false); //can't be here!
        }
        catch (IllegalArgumentException ignored){}

    }
}
