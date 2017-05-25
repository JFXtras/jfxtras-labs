package jfxtras.labs.scene.control.triple;

import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import javafx.scene.control.Skin;
import jfxtras.labs.internal.scene.control.skin.edittable.triple.TripleEditTableSkinStringStringBoolean;
import jfxtras.labs.scene.control.edittable.triple.TripleConverter;
import jfxtras.labs.scene.control.edittable.triple.TripleEditTable;

public class EmailEditTable extends TripleEditTable<Email,String,String,Boolean>
{
	private final static Pattern EMAIL_REGEX = Pattern.compile("\\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}\\b", Pattern.CASE_INSENSITIVE);
	private static Predicate<String> validateValue = (Predicate<String>) (v) -> EMAIL_REGEX.matcher(v).matches();
//	private static String valueName = "email";
	private static TripleConverter<Email,String,String,Boolean> converter = new EmailConverter();

	private static String[] alertTexts = new String[] {
			"Invalid Email Number",
			"Can't save email address",
			"Enter valid email address",
			"OK"
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
	public EmailEditTable()
	{
		super(
			validateValue,
//			valueName,
			converter,
			alertTexts,
			nameOptions,
			customBundle
			);
		setId("emailEditTable");
	}
	
	@Override
	public Skin<?> createDefaultSkin()
	{
		return new TripleEditTableSkinStringStringBoolean<Email>(
				validateValue,
				tripleList,
				converter,
				synchBeanItemTripleChangeLister,
				alertTexts,
				nameOptions,
				resources,
				this
				);
	}
}
