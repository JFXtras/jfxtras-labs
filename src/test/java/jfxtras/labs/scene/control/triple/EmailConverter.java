package jfxtras.labs.scene.control.triple;

import jfxtras.labs.scene.control.edittable.triple.Triple;
import jfxtras.labs.scene.control.edittable.triple.TripleConverter;

public class EmailConverter implements TripleConverter<Email,String,String,Boolean>
{
	@Override
	public Triple<String,String,Boolean> fromBeanElement(Email e) {
		return new Triple<String,String,Boolean>()
			.withName(e.getName())
			.withValue(e.getEmailAddress())
			.withPrimary(e.isPrimary());
	}

	@Override
	public Email toBeanElement(Triple<String,String,Boolean> t) {
		return new Email(t.getName(), t.getValue(), t.isPrimary());
	}
}
