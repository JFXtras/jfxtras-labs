/**
 * SpinnerDock.java
 *
 * Copyright (c) 2011-2013, JFXtras
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the <organization> nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package jfxtras.labs.jemmy.spinner;
import org.jemmy.control.Wrap;
import org.jemmy.env.Environment;
import org.jemmy.interfaces.Parent;
import org.jemmy.lookup.LookupCriteria;
public class SpinnerDock  extends org.jemmy.fx.control.ControlDock {
	/**Creates dock for a previously found control*/
	public SpinnerDock(Environment env, jfxtras.labs.scene.control.ListSpinner control) {
		super(org.jemmy.fx.NodeWrap.wrap(env, jfxtras.labs.scene.control.ListSpinner.class, control));
	}
	/**Creates dock for a wrapped control*/
	public SpinnerDock(Wrap<? extends jfxtras.labs.scene.control.ListSpinner> wrap) {
		super(wrap);
	}
	/**Looks for an <code>index</code>'th <code>jfxtras.labs.scene.control.Spinner</code> by a criteria within <code>parent</code>*/
	public SpinnerDock(Parent<? super jfxtras.labs.scene.control.ListSpinner> parent, int index, LookupCriteria<jfxtras.labs.scene.control.ListSpinner>... criteria) {
		this(lookup(parent, jfxtras.labs.scene.control.ListSpinner.class, index, criteria));
	}
	/**Looks for a <code>jfxtras.labs.scene.control.Spinner</code> by a criteria within <code>parent</code>*/
	public SpinnerDock(Parent<? super jfxtras.labs.scene.control.ListSpinner> parent, LookupCriteria<jfxtras.labs.scene.control.ListSpinner>... criteria) {
		this(parent, 0, criteria);
	}
	/**Looks for an <code>index</code>'th <code>jfxtras.labs.scene.control.Spinner</code> by id within <code>parent</code>*/
	public SpinnerDock(Parent<? super jfxtras.labs.scene.control.ListSpinner> parent, int index, java.lang.String id) {
		this(parent, index, org.jemmy.fx.NodeWrap.idLookup(jfxtras.labs.scene.control.ListSpinner.class, id));
	}
	/**Looks for a <code>jfxtras.labs.scene.control.Spinner</code> by id within <code>parent</code>*/
	public SpinnerDock(Parent<? super jfxtras.labs.scene.control.ListSpinner> parent, java.lang.String id) {
		this(parent, org.jemmy.fx.NodeWrap.idLookup(jfxtras.labs.scene.control.ListSpinner.class, id));
	}
	/**Looks for an <code>index</code>'th <code>jfxtras.labs.scene.control.Spinner</code> by type within <code>parent</code>*/
	public SpinnerDock(Parent<? super jfxtras.labs.scene.control.ListSpinner> parent, int index, java.lang.Class<?> subtype) {
		this(parent, index, org.jemmy.fx.NodeWrap.typeLookup(jfxtras.labs.scene.control.ListSpinner.class, subtype));
	}
	/**Looks for a <code>jfxtras.labs.scene.control.Spinner</code> by type within <code>parent</code>*/
	public SpinnerDock(Parent<? super jfxtras.labs.scene.control.ListSpinner> parent, java.lang.Class<?> subtype) {
		this(parent, org.jemmy.fx.NodeWrap.typeLookup(jfxtras.labs.scene.control.ListSpinner.class, subtype));
	}
	/**Returns wrap*/
	@Override
	public jfxtras.labs.jemmy.spinner.SpinnerWrap<? extends jfxtras.labs.scene.control.ListSpinner> wrap() {
		return (jfxtras.labs.jemmy.spinner.SpinnerWrap<? extends jfxtras.labs.scene.control.ListSpinner>)super.wrap();
	}
	/**Allows to use as <code>org.jemmy.interfaces.Selectable</code>*/
	public org.jemmy.interfaces.Selectable asSelectable() {
		return wrap().as(org.jemmy.interfaces.Selectable.class);
	}
}