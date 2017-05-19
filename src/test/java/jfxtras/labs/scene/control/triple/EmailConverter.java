package jfxtras.labs.scene.control.triple;

public class EmailConverter implements TripleConverter<Email>
{
	@Override
	public Triple fromBeanElement(Email e) {
		return new Triple("email")
		.withLabel(e.getName())
		.withValue(e.getEmailAddress())
		.withPrimary(e.isPrimary());
	}

	@Override
	public Email toBeanElement(Triple t) {
		return new Email(t.getName(), t.getValue(), t.isPrimary());
	}

}
