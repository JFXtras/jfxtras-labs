package jfxtras.labs.scene.control.triple;

import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class EmailHBox extends TripleEditTable<Email>
{
	public void initialize()
	{
		dataColumn.setText("Email");
		super.initialize();
	}

	private final static Pattern EMAIL_REGEX = Pattern.compile("\\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}\\b", Pattern.CASE_INSENSITIVE);
	private static Predicate<String> validateValue = (Predicate<String>) (v) -> EMAIL_REGEX.matcher(v).matches();
	private static String valueName = "email";
	private static TripleConverter<Email> converter = new EmailConverter();
//	private static Callback<Triple, Email>  createBeanItemCallback = t -> 
//	{
//		return new Email(t.getName(), t.getValue(), t.isPrimary());
//	};
	private static String[] alertTexts = new String[] {
			"Invalid Email Number",
			"Can't save email address",
			"Enter valid email address"
	};
	private static String[] nameOptions = new String[] {
			"Personal",
			"Work",
			"Mom",
			"Dad",
			"Other"
	};
	private static ResourceBundle customBundle = null;
	
	// CONSTRUCTOR
	public EmailHBox()
	{
		super(
			validateValue,
			valueName,
			converter,
//			createBeanItemCallback,
			alertTexts,
			nameOptions,
			customBundle
			);
	}
}
