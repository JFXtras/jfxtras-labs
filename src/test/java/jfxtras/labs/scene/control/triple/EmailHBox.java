package jfxtras.labs.scene.control.triple;

import java.util.function.Predicate;
import java.util.regex.Pattern;

import javafx.util.Callback;

public class EmailHBox extends TripleHBox<Email>
{
	public void initialize()
	{
		dataColumn.setText("Email");
		super.initialize();
	}

	private final static Pattern EMAIL_REGEX = Pattern.compile("\\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}\\b", Pattern.CASE_INSENSITIVE);
	private static Predicate<String> validateElement = (Predicate<String>) (v) -> EMAIL_REGEX.matcher(v).matches();
	private static String valueName = "email";
	private static Callback<Triple, Email>  createBeanItemCallback = t -> 
	{
		Email e = new Email(t.getLabel(), t.getValue(), t.isPrimary());
		return e;
	};
	static String[] alertTexts = new String[] {
			"Invalid Email Number",
			"Can't save email address",
			"Enter valid email address"
	};
	static String[] nameOptions = new String[] {
			"Personal",
			"Work",
			"Mom",
			"Dad",
			"Other"
	};
	
	// CONSTRUCTOR
	public EmailHBox()
	{
		super(
			validateElement,
			valueName,
			createBeanItemCallback,
			alertTexts,
			nameOptions,
			null
			);
	}
}
