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
package jfxtras.labs.scene.control.edittable.triple;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Triple<A,B,C>
{
    private final ObjectProperty<A> name = new SimpleObjectProperty<A>(this, "name");
    public ObjectProperty<A> nameProperty() { return name; }
    public void setName(A label) { this.name.set(label); }
    public A getName() { return name.getValue(); }
    public Triple<A,B,C> withName(A value) { setName(value); return this; }

    private final ObjectProperty<B> value = new SimpleObjectProperty<B>(this, "value");
    public ObjectProperty<B> valueProperty() { return value; }
    public B getValue() { return value.getValue(); }
    public void setValue(B value) {
                    this.value.set(value);
    }
    public boolean isEmpty() { return value.getValue() == null; }
    public Triple<A,B,C> withValue(B value) { setValue(value); return this; }

    private final ObjectProperty<C> primary = new SimpleObjectProperty<C>(this, "primary");
    public ObjectProperty<C> primaryProperty() { return primary; }
    public void setPrimary(C primary) { this.primary.set(primary); }
    public C isPrimary() { return primary.getValue(); }
    public Triple<A,B,C> withPrimary(C value) { setPrimary(value); return this; }
        
	@Override
	public String toString() {
		return "Triple [label=" + getName() + ", value=" + getValue() + ", primary=" + isPrimary() + "]";
	}
}
