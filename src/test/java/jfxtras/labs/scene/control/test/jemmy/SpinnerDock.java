package jfxtras.labs.scene.control.test.jemmy;
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
	public jfxtras.labs.scene.control.test.jemmy.SpinnerWrap<? extends jfxtras.labs.scene.control.ListSpinner> wrap() {
		return (jfxtras.labs.scene.control.test.jemmy.SpinnerWrap<? extends jfxtras.labs.scene.control.ListSpinner>)super.wrap();
	}
	/**Allows to use as <code>org.jemmy.interfaces.Selectable</code>*/
	public org.jemmy.interfaces.Selectable asSelectable() {
		return wrap().as(org.jemmy.interfaces.Selectable.class);
	}
}