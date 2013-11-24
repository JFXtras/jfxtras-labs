package jfxtras.labs.scene.control;

import java.io.Serializable;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.MapProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.scene.control.SelectionModel;
import javafx.util.StringConverter;

/**
 * An adapter that takes a POJO bean and internally and recursively
 * binds/un-binds it's fields to other {@link Property} components. It allows a
 * <b><code>.</code></b> separated field path to be traversed on a bean until
 * the final field name is found (last entry in the <b><code>.</code></b>
 * separated field path). Each field will have a corresponding {@link Property}
 * that is automatically generated and reused in the binding process. Each
 * {@link Property} is bean-aware and will dynamically update it's values and
 * bindings as different beans are set on the adapter. Bean's set on the adapter
 * do not need to instantiate all the sub-beans in the path(s) provided as long
 * as they contain a no-argument constructor they will be instantiated as
 * path(s) are traversed.
 * 
 * <h3>Examples:</h3>
 * <ol>
 * <li>
 * <b>Binding bean fields to multiple JavaFX control properties of different
 * types:</b>
 * 
 * <pre>
 * // Assuming &quot;age&quot; is a double field in person we can bind it to a
 * // Slider#valueProperty() of type double, but we can also bind it
 * // to a TextField#valueProperty() of type String.
 * Person person = new Person();
 * BeanPathAdapter&lt;Person&gt; personPA = new BeanPathAdapter&lt;&gt;(person);
 * Slider sl = new Slider();
 * TextField tf = new TextField();
 * personPA.bindBidirectional(&quot;age&quot;, sl.valueProperty());
 * personPA.bindBidirectional(&quot;age&quot;, tf.valueProperty());
 * </pre>
 * 
 * </li>
 * <li>
 * <b>Binding beans within beans:</b>
 * 
 * <pre>
 * // Binding a bean (Person) field called &quot;address&quot; that contains another
 * // bean (Address) that also contains a field called &quot;location&quot; with a
 * // bean (Location) field of &quot;state&quot; (the chain can be virtually endless
 * // with all beans being instantiated along the way when null).
 * Person person = new Person();
 * BeanPathAdapter&lt;Person&gt; personPA = new BeanPathAdapter&lt;&gt;(person);
 * TextField tf = new TextField();
 * personPA.bindBidirectional(&quot;address.location.state&quot;, tf.valueProperty());
 * </pre>
 * 
 * </li>
 * <li>
 * <b>Binding non-primitive bean paths to JavaFX control properties of the same
 * non-primitive type:</b>
 * 
 * <pre>
 * // Assuming &quot;address&quot; is an &quot;Address&quot; field in a &quot;Person&quot; class we can bind it
 * // to a ComboBox#valueProperty() of the same type. The &quot;Address&quot; class should
 * // override the &quot;toString()&quot; method in order to show a meaningful selection
 * // value in the example ComboBox.
 * Address a1 = new Address();
 * Address a2 = new Address();
 * a1.setStreet(&quot;1st Street&quot;);
 * a2.setStreet(&quot;2nd Street&quot;);
 * ComboBox&lt;Address&gt; cb = new ComboBox&lt;&gt;();
 * cb.getItems().addAll(a1, a2);
 * Person person = new Person();
 * BeanPathAdapter&lt;Person&gt; personPA = new BeanPathAdapter&lt;&gt;(person);
 * personPA.bindBidirectional(&quot;address&quot;, cb.valueProperty(), Address.class);
 * </pre>
 * 
 * </li>
 * <li>
 * <b>Binding collections/maps fields to/from observable collections/maps (i.e.
 * items in a JavaFX control):</b>
 * 
 * <pre>
 * // Assuming &quot;allLanguages&quot; is a collection/map field in person we can
 * // bind it to a JavaFX observable collection/map
 * Person person = new Person();
 * BeanPathAdapter&lt;Person&gt; personPA = new BeanPathAdapter&lt;&gt;(person);
 * ListView&lt;String&gt; lv = new ListView&lt;&gt;();
 * personPA.bindContentBidirectional(&quot;allLanguages&quot;, null, String.class,
 * 		lv.getItems(), String.class, null, null);
 * </pre>
 * 
 * </li>
 * <li>
 * <b>Binding collections/maps fields to/from observable collections/maps
 * selections (i.e. selections in a JavaFX control):</b>
 * 
 * <pre>
 * // Assuming &quot;languages&quot; is a collection/map field in person we can
 * // bind it to a JavaFX observable collection/map selections
 * Person person = new Person();
 * BeanPathAdapter&lt;Person&gt; personPA = new BeanPathAdapter&lt;&gt;(person);
 * ListView&lt;String&gt; lv = new ListView&lt;&gt;();
 * personPA.bindContentBidirectional(&quot;languages&quot;, null, String.class, lv
 * 		.getSelectionModel().getSelectedItems(), String.class, lv
 * 		.getSelectionModel(), null);
 * </pre>
 * 
 * </li>
 * <li>
 * <b>Binding collection/map fields to/from observable collections/maps
 * selections using an items from another observable collection/map as a
 * reference (i.e. selections in a JavaFX control that contain the same
 * instances as what are in the items being selected from):</b>
 * 
 * <pre>
 * // Assuming &quot;languages&quot; and &quot;allLanguages&quot; are a collection/map
 * // fields in person we can bind &quot;languages&quot; to selections made from
 * // the items in &quot;allLanguages&quot; to a JavaFX observable collection/map
 * // selection Person person = new Person();
 * BeanPathAdapter&lt;Person&gt; personPA = new BeanPathAdapter&lt;&gt;(person);
 * ListView&lt;String&gt; lv = new ListView&lt;&gt;();
 * personPA.bindContentBidirectional(&quot;languages&quot;, null, String.class, lv
 * 		.getSelectionModel().getSelectedItems(), String.class, lv
 * 		.getSelectionModel(), &quot;allLanguages&quot;);
 * </pre>
 * 
 * </li>
 * <li>
 * <b>Binding complex bean collection/map fields to/from observable
 * collections/maps selections and items (i.e. selections in a JavaFX control
 * that contain the same bean instances as what are in the items being
 * selected):</b>
 * 
 * <pre>
 * // Assuming "hobbies" and
 * "allHobbies" are a collection/map // fields in person and each element within
 * them contain an // instance of Hobby that has it's own field called "name" //
 * we can bind "allHobbies" and "hobbies" to the Hobby "name"s // for each Hobby
 * in the items/selections (respectively) to/from // a ListView wich will only
 * contain the String name of each Hobby // as it's items and selections Person
 * person = new Person(); BeanPathAdapter&lt;Person&gt; personPA = new
 * BeanPathAdapter&lt;&gt;(person); ListView&lt;String&gt; lv = new ListView&lt;&gt;();
 * // bind items 
 * personPA.bindContentBidirectional("allHobbies", "name", Hobby.class,
 * 		lv.getItems(), String.class, null, null);
 * // bind selections that reference the same instances within the items
 * personPA.bindContentBidirectional("languages", "name", Hobby.class,
 * 		lv.getSelectionModel().getSelectedItems(), String.class,
 * 		lv.getSelectionModel(), "allHobbiess");
 * </pre>
 * 
 * </li>
 * <li>
 * <b>Binding bean collection/map fields to/from multiple JavaFX control
 * observable collections/maps of the same type (via bean collection/map):</b>
 * 
 * <pre>
 * Person person = new Person();
 * Hobby hobby1 = new Hobby();
 * hobby1.setName(&quot;Hobby 1&quot;);
 * Hobby hobby2 = new Hobby();
 * hobby2.setName(&quot;Hobby 2&quot;);
 * person.setAllHobbies(new LinkedHashSet&lt;Hobby&gt;());
 * person.getAllHobbies().add(hobby1);
 * person.getAllHobbies().add(hobby2);
 * BeanPathAdapter&lt;Person&gt; personPA = new BeanPathAdapter&lt;&gt;(person);
 * ListView&lt;String&gt; lv = new ListView&lt;&gt;();
 * personPA.bindContentBidirectional(&quot;allHobbies&quot;, &quot;name&quot;, Hobby.class,
 * 		lv.getItems(), String.class, null, null);
 * ListView&lt;String&gt; lv2 = new ListView&lt;&gt;();
 * personPA.bindContentBidirectional(&quot;allHobbies&quot;, &quot;name&quot;, Hobby.class,
 * 		lv2.getItems(), String.class, null, null);
 * </pre>
 * 
 * </li>
 * <li>
 * <b>Binding bean collection/map fields to/from multiple JavaFX control
 * observable collections/maps of the same type (via JavaFX control observable
 * collection/map):</b>
 * 
 * <pre>
 * // When the bean collection/map field is empty/null and it is
 * // bound to a non-empty observable collection/map, the values
 * // of the observable are used to instantiate each item bean
 * // and set the item value (Hobby#setName in this case)
 * Person person = new Person();
 * final ObservableList&lt;String&gt; oc = FXCollections.observableArrayList(&quot;Hobby 1&quot;,
 * 		&quot;Hobby 2&quot;, &quot;Hobby 3&quot;);
 * BeanPathAdapter&lt;Person&gt; personPA = new BeanPathAdapter&lt;&gt;(person);
 * ListView&lt;String&gt; lv = new ListView&lt;&gt;(oc);
 * personPA.bindContentBidirectional(&quot;allHobbies&quot;, &quot;name&quot;, Hobby.class,
 * 		lv.getItems(), String.class, null, null);
 * ListView&lt;String&gt; lv2 = new ListView&lt;&gt;(); // &lt;-- notice that oc is not passed
 * personPA.bindContentBidirectional(&quot;allHobbies&quot;, &quot;name&quot;, Hobby.class,
 * 		lv2.getItems(), String.class, null, null);
 * </pre>
 * 
 * </li>
 * <li>
 * <b>Switching beans:</b>
 * 
 * <pre>
 * // Assuming &quot;age&quot; is a double field in person...
 * final Person person1 = new Person();
 * person1.setAge(1D);
 * final Person person2 = new Person();
 * person2.setAge(2D);
 * final BeanPathAdapter&lt;Person&gt; personPA = new BeanPathAdapter&lt;&gt;(person1);
 * TextField tf = new TextField();
 * personPA.bindBidirectional(&quot;age&quot;, tf.valueProperty());
 * Button btn = new Button(&quot;Toggle People&quot;);
 * btn.setOnMouseClicked(new EventHandler&lt;MouseEvent&gt;() {
 * 	public void handle(MouseEvent event) {
 * 		// all bindings will show relevant person data and changes made
 * 		// to the bound controls will be reflected in the bean that is
 * 		// set at the time of the change
 * 		personPA.setBean(personPA.getBean() == person1 ? person2 : person1);
 * 	}
 * });
 * </pre>
 * 
 * </li>
 * <li>
 * <b>{@link Date}/{@link Calendar} binding:</b>
 * 
 * <pre>
 * // Assuming &quot;dob&quot; is a java.util.Date or java.util.Calendar field
 * // in person it can be bound to a java.util.Date or
 * // java.util.Calendar JavaFX control property. Example uses a
 * // jfxtras.labs.scene.control.CalendarPicker
 * final Person person = new Person();
 * final BeanPathAdapter&lt;Person&gt; personPA = new BeanPathAdapter&lt;&gt;(person);
 * CalendarPicker calendarPicker = new CalendarPicker();
 * personPA.bindBidirectional(&quot;dob&quot;, calendarPicker.calendarProperty(),
 * 		Calendar.class);
 * </pre>
 * 
 * </li>
 * <li>
 * <b>{@link javafx.scene.control.TableView} binding:</b>
 * 
 * <pre>
 * // Assuming &quot;name&quot;/&quot;description&quot; are a java.lang.String fields in Hobby
 * // and &quot;hobbies&quot; is a List/Set/Map in Person
 * final Person person = new Person();
 * final BeanPathAdapter&lt;Person&gt; personPA = new BeanPathAdapter&lt;&gt;(person);
 * TableView&lt;Hobby&gt; table = new TableView&lt;&gt;();
 * TableColumn&lt;Hobby, String&gt; nameCol = new TableColumn&lt;&gt;(&quot;Hobby Name&quot;);
 * nameCol.setMinWidth(100);
 * nameCol.setCellValueFactory(new PropertyValueFactory&lt;Hobby, String&gt;(&quot;name&quot;));
 * TableColumn&lt;Hobby, String&gt; descCol = new TableColumn&lt;&gt;(&quot;Hobby Desc&quot;);
 * descCol.setMinWidth(100);
 * descCol.setCellValueFactory(new PropertyValueFactory&lt;Hobby, String&gt;(
 * 		&quot;description&quot;));
 * table.getColumns().addAll(nameCol, descCol);
 * personPA.bindContentBidirectional(&quot;hobbies&quot;, null, String.class,
 * 		table.getItems(), Hobby.class, null, null);
 * </pre>
 * 
 * </li>
 * <li>
 * <b>Listening for global changes:</b>
 * 
 * <pre>
 * final BeanPathAdapter&lt;Person&gt; personPA = new BeanPathAdapter&lt;&gt;(person);
 * // use the following to eliminate unwanted notifications
 * // personPA.removeFieldPathValueTypes(FieldPathValueType.BEAN_CHANGE, ...)
 * personPA.fieldPathValueProperty().addListener(
 * 		new ChangeListener&lt;FieldPathValue&gt;() {
 * 			&#064;Override
 * 			public void changed(
 * 					final ObservableValue&lt;? extends FieldPathValue&gt; observable,
 * 					final FieldPathValue oldValue, final FieldPathValue newValue) {
 * 				System.out.println(&quot;Value changed from: &quot; + oldValue + &quot; to: &quot;
 * 						+ newValue);
 * 			}
 * 		});
 * </pre>
 * 
 * </li>
 * </ol>
 * 
 * @see #bindBidirectional(String, Property)
 * @see #bindContentBidirectional(String, String, Class, ObservableList, Class,
 *      SelectionModel, String)
 * @see #bindContentBidirectional(String, String, Class, ObservableSet, Class,
 *      SelectionModel, String)
 * @see #bindContentBidirectional(String, String, Class, ObservableMap, Class,
 *      SelectionModel, String)
 * @param <B>
 *            the bean type
 */
public class BeanPathAdapter<B> {

	public static final char PATH_SEPARATOR = '.';
	public static final char COLLECTION_ITEM_PATH_SEPARATOR = '#';
	private FieldBean<Void, B> root;
	private FieldPathValueProperty fieldPathValueProperty = new FieldPathValueProperty();

	/**
	 * Constructor
	 * 
	 * @param bean
	 *            the bean the {@link BeanPathAdapter} is for
	 */
	public BeanPathAdapter(final B bean) {
		setBean(bean);
	}

	/**
	 * @see #bindBidirectional(String, Property, Class)
	 */
	public void bindBidirectional(final String fieldPath,
			final BooleanProperty property) {
		bindBidirectional(fieldPath, property, Boolean.class);
	}

	/**
	 * @see #bindBidirectional(String, Property, Class)
	 */
	public void bindBidirectional(final String fieldPath,
			final StringProperty property) {
		bindBidirectional(fieldPath, property, String.class);
	}

	/**
	 * @see #bindBidirectional(String, Property, Class)
	 */
	public void bindBidirectional(final String fieldPath,
			final Property<Number> property) {
		bindBidirectional(fieldPath, property, null);
	}

	/**
	 * Binds a {@link ObservableList} by traversing the bean's field tree. An
	 * additional item path can be specified when the path points to a
	 * {@link Collection} that contains beans that also need traversed in order
	 * to establish the final value. For example: If a field path points to
	 * <code>phoneNumbers</code> (relative to the {@link #getBean()}) where
	 * <code>phoneNumbers</code> is a {@link Collection} that contains
	 * <code>PhoneNumber</code> instances which in turn have a field called
	 * <code>areaCode</code> then an item path can be passed in addition to the
	 * field path with <code>areaCode</code> as it's value.
	 * 
	 * @param fieldPath
	 *            the <b><code>.</code></b> separated field paths relative to
	 *            the {@link #getBean()} that will be traversed
	 * @param itemFieldPath
	 *            the <b><code>.</code></b> separated field paths relative to
	 *            each item in the bean's underlying {@link Collection} that
	 *            will be traversed (empty/null when each item value does not
	 *            need traversed)
	 * @param itemFieldPathType
	 *            the {@link Class} of that the item path points to
	 * @param list
	 *            the {@link ObservableList} to bind to the field class type of
	 *            the property
	 * @param listValueType
	 *            the class type of the {@link ObservableList} value
	 * @param selectionModel
	 *            the {@link SelectionModel} used to set the values within the
	 *            {@link ObservableList} <b>only applicable when the
	 *            {@link ObservableList} is used for selection(s) and therefore
	 *            cannot be updated directly because it is read-only</b>
	 * @param selectionModelItemMasterPath
	 *            when binding to {@link SelectionModel} items, this will be the
	 *            optional path to the collection field that contains all the
	 *            items to select from
	 */
	public <E> void bindContentBidirectional(final String fieldPath,
			final String itemFieldPath, final Class<?> itemFieldPathType,
			final ObservableList<E> list, final Class<E> listValueType,
			final SelectionModel<E> selectionModel,
			final String selectionModelItemMasterPath) {
		FieldProperty<?, ?, ?> itemMaster = null;
		if (selectionModelItemMasterPath != null
				&& !selectionModelItemMasterPath.isEmpty()) {
			itemMaster = getRoot().performOperation(
					selectionModelItemMasterPath, list, listValueType,
					itemFieldPath, itemFieldPathType, null, null,
					FieldBeanOperation.CREATE_OR_FIND);
		}
		getRoot().performOperation(fieldPath, list, listValueType,
				itemFieldPath, itemFieldPathType, selectionModel, itemMaster,
				FieldBeanOperation.BIND);
	}

	/**
	 * Binds a {@link ObservableSet} by traversing the bean's field tree. An
	 * additional item path can be specified when the path points to a
	 * {@link Collection} that contains beans that also need traversed in order
	 * to establish the final value. For example: If a field path points to
	 * <code>phoneNumbers</code> (relative to the {@link #getBean()}) where
	 * <code>phoneNumbers</code> is a {@link Collection} that contains
	 * <code>PhoneNumber</code> instances which in turn have a field called
	 * <code>areaCode</code> then an item path can be passed in addition to the
	 * field path with <code>areaCode</code> as it's value.
	 * 
	 * @param fieldPath
	 *            the <b><code>.</code></b> separated field paths relative to
	 *            the {@link #getBean()} that will be traversed
	 * @param itemFieldPath
	 *            the <b><code>.</code></b> separated field paths relative to
	 *            each item in the bean's underlying {@link Collection} that
	 *            will be traversed (empty/null when each item value does not
	 *            need traversed)
	 * @param itemFieldPathType
	 *            the {@link Class} of that the item path points to
	 * @param set
	 *            the {@link ObservableSet} to bind to the field class type of
	 *            the property
	 * @param setValueType
	 *            the class type of the {@link ObservableSet} value
	 * @param selectionModel
	 *            the {@link SelectionModel} used to set the values within the
	 *            {@link ObservableSet} <b>only applicable when the
	 *            {@link ObservableSet} is used for selection(s) and therefore
	 *            cannot be updated directly because it is read-only</b>
	 * @param selectionModelItemMasterPath
	 *            when binding to {@link SelectionModel} items, this will be the
	 *            optional path to the collection field that contains all the
	 *            items to select from
	 */
	public <E> void bindContentBidirectional(final String fieldPath,
			final String itemFieldPath, final Class<?> itemFieldPathType,
			final ObservableSet<E> set, final Class<E> setValueType,
			final SelectionModel<E> selectionModel,
			final String selectionModelItemMasterPath) {
		FieldProperty<?, ?, ?> itemMaster = null;
		if (selectionModelItemMasterPath != null
				&& !selectionModelItemMasterPath.isEmpty()) {
			itemMaster = getRoot().performOperation(
					selectionModelItemMasterPath, set, setValueType,
					itemFieldPath, itemFieldPathType, null, null,
					FieldBeanOperation.CREATE_OR_FIND);
		}
		getRoot().performOperation(fieldPath, set, setValueType, itemFieldPath,
				itemFieldPathType, selectionModel, itemMaster,
				FieldBeanOperation.BIND);
	}

	/**
	 * Binds a {@link ObservableMap} by traversing the bean's field tree. An
	 * additional item path can be specified when the path points to a
	 * {@link Collection} that contains beans that also need traversed in order
	 * to establish the final value. For example: If a field path points to
	 * <code>phoneNumbers</code> (relative to the {@link #getBean()}) where
	 * <code>phoneNumbers</code> is a {@link Collection} that contains
	 * <code>PhoneNumber</code> instances which in turn have a field called
	 * <code>areaCode</code> then an item path can be passed in addition to the
	 * field path with <code>areaCode</code> as it's value.
	 * 
	 * @param fieldPath
	 *            the <b><code>.</code></b> separated field paths relative to
	 *            the {@link #getBean()} that will be traversed
	 * @param itemFieldPath
	 *            the <b><code>.</code></b> separated field paths relative to
	 *            each item in the bean's underlying {@link Collection} that
	 *            will be traversed (empty/null when each item value does not
	 *            need traversed)
	 * @param itemFieldPathType
	 *            the {@link Class} of that the item path points to
	 * @param map
	 *            the {@link ObservableMap} to bind to the field class type of
	 *            the property
	 * @param mapValueType
	 *            the class type of the {@link ObservableMap} value
	 * @param selectionModel
	 *            the {@link SelectionModel} used to set the values within the
	 *            {@link ObservableMap} <b>only applicable when the
	 *            {@link ObservableMap} is used for selection(s) and therefore
	 *            cannot be updated directly because it is read-only</b>
	 * @param selectionModelItemMasterPath
	 *            when binding to {@link SelectionModel} items, this will be the
	 *            optional path to the collection field that contains all the
	 *            items to select from
	 */
	public <K, V> void bindContentBidirectional(final String fieldPath,
			final String itemFieldPath, final Class<?> itemFieldPathType,
			final ObservableMap<K, V> map, final Class<V> mapValueType,
			final SelectionModel<V> selectionModel,
			final String selectionModelItemMasterPath) {
		FieldProperty<?, ?, ?> itemMaster = null;
		if (selectionModelItemMasterPath != null
				&& !selectionModelItemMasterPath.isEmpty()) {
			itemMaster = getRoot().performOperation(
					selectionModelItemMasterPath, map, mapValueType,
					itemFieldPath, itemFieldPathType, null, null,
					FieldBeanOperation.CREATE_OR_FIND);
		}
		getRoot().performOperation(fieldPath, map, mapValueType, itemFieldPath,
				itemFieldPathType, selectionModel, itemMaster,
				FieldBeanOperation.BIND);
	}

	/**
	 * Binds a {@link Property} by traversing the bean's field tree
	 * 
	 * @param fieldPath
	 *            the <b><code>.</code></b> separated field paths relative to
	 *            the {@link #getBean()} that will be traversed
	 * @param property
	 *            the {@link Property} to bind to the field class type of the
	 *            property
	 * @param propertyType
	 *            the class type of the {@link Property} value
	 */
	@SuppressWarnings("unchecked")
	public <T> void bindBidirectional(final String fieldPath,
			final Property<T> property, final Class<T> propertyType) {
		Class<T> clazz = propertyType != null ? propertyType
				: propertyValueClass(property);
		if (clazz == null && property.getValue() != null) {
			clazz = (Class<T>) property.getValue().getClass();
		}
		if (clazz == null || clazz == Object.class) {
			throw new UnsupportedOperationException(String.format(
					"Unable to determine property value class for %1$s "
							+ "and declared type %2$s", property, propertyType));
		}
		getRoot().performOperation(fieldPath, property, clazz,
				FieldBeanOperation.BIND);
	}

	/**
	 * Unbinds a {@link Property} by traversing the bean's field tree
	 * 
	 * @param fieldPath
	 *            the <b><code>.</code></b> separated field paths relative to
	 *            the {@link #getBean()} that will be traversed
	 * @param property
	 *            the {@link Property} to bind to the field class type of the
	 *            property
	 */
	public <T> void unBindBidirectional(final String fieldPath,
			final Property<T> property) {
		getRoot().performOperation(fieldPath, property, null,
				FieldBeanOperation.UNBIND);
	}

	/**
	 * @return the bean of the {@link BeanPathAdapter}
	 */
	public B getBean() {
		return getRoot().getBean();
	}

	/**
	 * Sets the root bean of the {@link BeanPathAdapter}. Any existing
	 * properties will be updated with the values relative to the paths within
	 * the bean.
	 * 
	 * @param bean
	 *            the bean to set
	 */
	public void setBean(final B bean) {
		if (bean == null) {
			throw new NullPointerException();
		}
		if (getRoot() == null) {
			this.root = new FieldBean<>(null, bean, null,
					fieldPathValueProperty);
		} else {
			getRoot().setBean(bean);
		}
		if (hasFieldPathValueTypes(FieldPathValueType.BEAN_CHANGE)) {
			fieldPathValueProperty.set(new FieldPathValue(null, getBean(),
					getBean(), FieldPathValueType.BEAN_CHANGE));
		}
	}

	/**
	 * @return the root/top level {@link FieldBean}
	 */
	protected final FieldBean<Void, B> getRoot() {
		return this.root;
	}

	/**
	 * @see #addFieldPathValueTypes(FieldPathValueType...)
	 * @see #removeFieldPathValueTypes(FieldPathValueType...)
	 * @see #hasFieldPathValueTypes(FieldPathValueType...)
	 * @return the {@link ReadOnlyObjectProperty} that contains the last path
	 *         that was changed in the {@link BeanPathAdapter}. For
	 *         notifications for items bound using content bindings
	 *         (collections/maps)
	 */
	public final ReadOnlyObjectProperty<FieldPathValue> fieldPathValueProperty() {
		return fieldPathValueProperty.getReadOnlyProperty();
	}

	/**
	 * Provides the underlying value class for a given {@link Property}
	 * 
	 * @param property
	 *            the {@link Property} to check
	 * @return the value class of the {@link Property}
	 */
	@SuppressWarnings("unchecked")
	protected static <T> Class<T> propertyValueClass(final Property<T> property) {
		Class<T> clazz = null;
		if (property != null) {
			if (StringProperty.class.isAssignableFrom(property.getClass())) {
				clazz = (Class<T>) String.class;
			} else if (IntegerProperty.class.isAssignableFrom(property
					.getClass())) {
				clazz = (Class<T>) Integer.class;
			} else if (BooleanProperty.class.isAssignableFrom(property
					.getClass())) {
				clazz = (Class<T>) Boolean.class;
			} else if (DoubleProperty.class.isAssignableFrom(property
					.getClass())) {
				clazz = (Class<T>) Double.class;
			} else if (FloatProperty.class
					.isAssignableFrom(property.getClass())) {
				clazz = (Class<T>) Float.class;
			} else if (LongProperty.class.isAssignableFrom(property.getClass())) {
				clazz = (Class<T>) Long.class;
			} else if (ListProperty.class.isAssignableFrom(property.getClass())) {
				clazz = (Class<T>) List.class;
			} else if (MapProperty.class.isAssignableFrom(property.getClass())) {
				clazz = (Class<T>) Map.class;
			} else {
				clazz = (Class<T>) Object.class;
			}
		}
		return clazz;
	}

	/**
	 * Adds {@link FieldPathValueType}(s) {@link FieldPathValueType}(s) that
	 * {link notifyProperty()} will use
	 * 
	 * @param types
	 *            the {@link FieldPathValueType} to add
	 */
	public void addFieldPathValueTypes(final FieldPathValueType... types) {
		fieldPathValueProperty.addRemoveTypes(true, types);
	}

	/**
	 * Removes {@link FieldPathValueType}(s) {@link FieldPathValueType}(s) that
	 * {link notifyProperty()} will use
	 * 
	 * @param types
	 *            the {@link FieldPathValueType}(s) to remove
	 */
	public void removeFieldPathValueTypes(final FieldPathValueType... types) {
		fieldPathValueProperty.addRemoveTypes(false, types);
	}

	/**
	 * Determines if the {@link FieldPathValueType}(s) are being used by the
	 * {link notifyProperty()}
	 * 
	 * @param types
	 *            the {@link FieldPathValueType}(s) to check for
	 * @return true if all of the specified {@link FieldPathValueType}(s) exist
	 */
	public boolean hasFieldPathValueTypes(final FieldPathValueType... types) {
		return fieldPathValueProperty.hasTypes(types);
	}

	/**
	 * The {@link ReadOnlyObjectWrapper} that contains the last path that was
	 * changed in the {@link BeanPathAdapter}
	 */
	static class FieldPathValueProperty extends
			ReadOnlyObjectWrapper<FieldPathValue> {

		private final Set<FieldPathValueType> types;

		/**
		 * Constructor
		 */
		public FieldPathValueProperty() {
			super();
			this.types = new HashSet<>();
			addRemoveTypes(true, FieldPathValueType.values());
		}

		/**
		 * Adds/Removes {@link FieldPathValueType}(s)
		 * 
		 * @param add
		 *            true to add, false to remove
		 * @param types
		 *            the {@link FieldPathValueType}(s) to add/remove
		 */
		public void addRemoveTypes(final boolean add,
				final FieldPathValueType... types) {
			if (types.length <= 0) {
				return;
			}
			if (add) {
				Collections.addAll(this.types, types);
			} else {
				for (final FieldPathValueType t : types) {
					this.types.remove(t);
				}
			}
		}

		/**
		 * Determines if the {link getTypes()} has all of the specified
		 * {@link FieldPathValueType}(s)
		 * 
		 * @param types
		 *            the {@link FieldPathValueType}(s) to check for
		 * @return true if all of the specified {@link FieldPathValueType}(s)
		 *         exist
		 */
		public boolean hasTypes(final FieldPathValueType... types) {
			if (types.length <= 0) {
				return false;
			}
			for (final FieldPathValueType type : types) {
				if (!this.types.contains(type)) {
					return false;
				}
			}
			return true;
		}
	}

	/**
	 * Field {@link #getPath()}/{@link #getValue()}
	 */
	public static class FieldPathValue {

		private final String path;
		private final Object bean;
		private final Object value;
		private final FieldPathValueType type;

		/**
		 * Constructor
		 * 
		 * @param path
		 *            the {@link #getPath()}
		 * @param bean
		 *            the {@link #getBean()}
		 * @param value
		 *            the {@link #getValue()}
		 * @param type
		 */
		public FieldPathValue(final String path, final Object bean,
				final Object value, final FieldPathValueType type) {
			this.path = path;
			this.bean = bean;
			this.value = value;
			this.type = type;
		}

		/**
		 * Generates a hash code using {@link #getPath()}, {@link #getBean()},
		 * {@link #getValue()}, and {link isFromItemSelection()}
		 * 
		 * @return the hash code
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((bean == null) ? 0 : bean.hashCode());
			result = prime * result + ((path == null) ? 0 : path.hashCode());
			result = prime * result + ((value == null) ? 0 : value.hashCode());
			result = prime * result + ((type == null) ? 0 : type.hashCode());
			return result;
		}

		/**
		 * Determines equality based upon {@link #getPath()}, {@link #getBean()}
		 * , {@link #getValue()}, and {link isFromItemSelection()}
		 * 
		 * @param obj
		 *            the {@link Object} to check for equality
		 * @return true when equal
		 */
		@Override
		public boolean equals(final Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			FieldPathValue other = (FieldPathValue) obj;
			if (bean == null) {
				if (other.bean != null) {
					return false;
				}
			} else if (!bean.equals(other.bean)) {
				return false;
			}
			if (path == null) {
				if (other.path != null) {
					return false;
				}
			} else if (!path.equals(other.path)) {
				return false;
			}
			if (value == null) {
				if (other.value != null) {
					return false;
				}
			} else if (!value.equals(other.value)) {
				return false;
			}
			if (type != other.type) {
				return false;
			}
			return true;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			return FieldPathValue.class.getSimpleName() + " [path=" + path
					+ ", value=" + value + ", type=" + type + ", bean=" + bean
					+ "]";
		}

		/**
		 * @return the {@link BeanPathAdapter#PATH_SEPARATOR} separated path of
		 *         the field value that changed. When the path involves an item
		 *         within a collection the path to the item will be separated
		 *         with {@link BeanPathAdapter#COLLECTION_ITEM_PATH_SEPARATOR}
		 */
		public String getPath() {
			return path;
		}

		/**
		 * @return the bean that the {@link #getValue()} belongs to
		 */
		public Object getBean() {
			return bean;
		}

		/**
		 * @return value of the field path that changed
		 */
		public Object getValue() {
			return value;
		}

		/**
		 * @return the {@link FieldPathValueType}
		 */
		public FieldPathValueType getType() {
			return type;
		}
	}

	/**
	 * {@link FieldPathValue} types used for {@link FieldPathValueProperty}
	 * changes
	 */
	public static enum FieldPathValueType {
		/**
		 * Root bean change (from a {@link BeanPathAdapter#setBean(Object)}
		 * operation)
		 */
		BEAN_CHANGE,
		/**
		 * General field binding change (not from a
		 * {@link BeanPathAdapter#setBean(Object)} operation)
		 */
		FIELD_CHANGE,
		/** Item added via content binding */
		CONTENT_ITEM_ADD,
		/** Item removed via content binding */
		CONTENT_ITEM_REMOVE,
		/** Selection item added via content binding */
		CONTENT_ITEM_ADD_SELECT,
		/** Selection item removed via content binding */
		CONTENT_ITEM_REMOVE_SELECT;
	}

	/**
	 * {@link FieldBean} operations
	 */
	public static enum FieldBeanOperation {
		BIND,
		UNBIND,
		CREATE_OR_FIND;
	}

	/**
	 * A POJO bean extension that allows binding based upon a <b><code>.</code>
	 * </b> separated field path that will be traversed on a bean until the
	 * final field name is found. Each bean may contain child {@link FieldBean}s
	 * when an operation is perfomed with a direct descendant field that is a
	 * non-primitive type. Any primitive types are added as a
	 * {@link FieldProperty} reference to the {@link FieldBean}.
	 * 
	 * @param <PT>
	 *            the parent bean type
	 * @param <BT>
	 *            the bean type
	 */
	protected static class FieldBean<PT, BT> implements Serializable {

		private static final long serialVersionUID = 7397535724568852021L;
		private final FieldPathValueProperty notifyProperty;
		private final Map<String, FieldBean<BT, ?>> fieldBeans = new HashMap<>();
		private final Map<String, FieldProperty<BT, ?, ?>> fieldProperties = new HashMap<>();
		private final Map<String, FieldProperty<BT, ?, ?>> fieldSelectionProperties = new HashMap<>();
		private final Map<Class<?>, FieldStringConverter<?>> stringConverters = new HashMap<>();
		private FieldHandle<PT, BT> fieldHandle;
		private final FieldBean<?, PT> parent;
		private BT bean;

		/**
		 * Creates a {@link FieldBean}
		 * 
		 * @param parent
		 *            the parent {@link FieldBean} (should not be null)
		 * @param fieldHandle
		 *            the {@link FieldHandle} (should not be null)
		 * @param notifyProperty
		 *            the {@link FieldPathValueProperty} that will be set every
		 *            time the {@link FieldBean#setBean(Object)} is changed
		 */
		protected FieldBean(final FieldBean<?, PT> parent,
				final FieldHandle<PT, BT> fieldHandle,
				final FieldPathValueProperty notifyProperty) {
			this.parent = parent;
			this.fieldHandle = fieldHandle;
			this.bean = this.fieldHandle.setDerivedValueFromAccessor();
			this.notifyProperty = notifyProperty;
			if (getParent() != null) {
				getParent().addFieldBean(this);
			}
		}

		/**
		 * Creates a {@link FieldBean} with a generated {@link FieldHandle} that
		 * targets the supplied bean and is projected on the parent
		 * {@link FieldBean}. It assumes that the supplied {@link FieldBean} has
		 * been set on the parent {@link FieldBean}.
		 * 
		 * @see #createFieldHandle(Object, Object, String)
		 * @param parent
		 *            the parent {@link FieldBean} (null when it's the root)
		 * @param bean
		 *            the bean that the {@link FieldBean} is for
		 * @param fieldName
		 *            the field name of the parent {@link FieldBean} for which
		 *            the new {@link FieldBean} is for
		 * @param notifyProperty
		 *            the {@link FieldPathValueProperty} that will be set every
		 *            time the {@link FieldBean#setBean(Object)} is changed
		 */
		protected FieldBean(final FieldBean<?, PT> parent, final BT bean,
				final String fieldName,
				final FieldPathValueProperty notifyProperty) {
			if (bean == null) {
				throw new NullPointerException("Bean cannot be null");
			}
			this.parent = parent;
			this.bean = bean;
			this.notifyProperty = notifyProperty;
			this.fieldHandle = getParent() != null ? createFieldHandle(
					getParent().getBean(), bean, fieldName) : null;
			if (getParent() != null) {
				getParent().addFieldBean(this);
			}
		}

		/**
		 * Generates a {@link FieldHandle} that targets the supplied bean and is
		 * projected on the parent {@link FieldBean} that has
		 * 
		 * @param parentBean
		 *            the parent bean
		 * @param bean
		 *            the child bean
		 * @param fieldName
		 *            the field name of the child within the parent
		 * @return the {@link FieldHandle}
		 */
		@SuppressWarnings("unchecked")
		protected FieldHandle<PT, BT> createFieldHandle(final PT parentBean,
				final BT bean, final String fieldName) {
			return new FieldHandle<PT, BT>(parentBean, fieldName,
					(Class<BT>) getBean().getClass());
		}

		/**
		 * @see #setParentBean(Object)
		 * @return the bean that the {@link FieldBean} represents
		 */
		public BT getBean() {
			return bean;
		}

		/**
		 * Adds a child {@link FieldBean} if it doesn't already exist. NOTE: It
		 * does <b>NOT</b> ensure the child bean has been set on the parent.
		 * 
		 * @param fieldBean
		 *            the {@link FieldBean} to add
		 */
		protected void addFieldBean(final FieldBean<BT, ?> fieldBean) {
			if (!getFieldBeans().containsKey(fieldBean.getFieldName())) {
				getFieldBeans().put(fieldBean.getFieldName(), fieldBean);
			}
		}

		/**
		 * Adds or updates a child {@link FieldProperty}. When the child already
		 * exists it will {@link FieldProperty#setTarget(Object)} using the bean
		 * of the {@link FieldProperty}.
		 * 
		 * @param fieldProperty
		 *            the {@link FieldProperty} to add or update
		 */
		protected void addOrUpdateFieldProperty(
				final FieldProperty<BT, ?, ?> fieldProperty) {
			final String pkey = fieldProperty.getName();
			if (getFieldProperties().containsKey(pkey)) {
				getFieldProperties().get(pkey).setTarget(
						fieldProperty.getBean());
			} else if (getFieldSelectionProperties().containsKey(pkey)) {
				getFieldSelectionProperties().get(pkey).setTarget(
						fieldProperty.getBean());
			} else if (fieldProperty.hasItemMaster()) {
				getFieldSelectionProperties().put(pkey, fieldProperty);
			} else {
				getFieldProperties().put(pkey, fieldProperty);
			}
		}

		/**
		 * Sets the bean of the {@link FieldBean} and it's underlying
		 * {@link #getFieldBeans()}, {@link #getFieldProperties()}, and
		 * {@link #getFieldSelectionProperties()}
		 * 
		 * @see #setParentBean(Object)
		 * @param bean
		 *            the bean to set
		 */
		public void setBean(final BT bean) {
			if (bean == null) {
				throw new NullPointerException("Bean cannot be null");
			}
			this.bean = bean;
			for (final Map.Entry<String, FieldBean<BT, ?>> fn : getFieldBeans()
					.entrySet()) {
				fn.getValue().setParentBean(getBean());
			}
			// selections need to be set before non-selections so that item
			// master listeners in the selection properties will have the
			// updated values by the time changes are detected on the item
			// masters
			for (final Map.Entry<String, FieldProperty<BT, ?, ?>> fp : getFieldSelectionProperties()
					.entrySet()) {
				fp.getValue().setTarget(getBean());
			}
			for (final Map.Entry<String, FieldProperty<BT, ?, ?>> fp : getFieldProperties()
					.entrySet()) {
				fp.getValue().setTarget(getBean());
			}
		}

		/**
		 * Binds a parent bean to the {@link FieldBean} and it's underlying
		 * {@link #getFieldBeans()}, {@link #getFieldProperties()}, and
		 * {@link #getFieldSelectionProperties()}
		 * 
		 * @see #setBean(Object)
		 * @param bean
		 *            the parent bean to bind to
		 */
		public void setParentBean(final PT bean) {
			if (bean == null) {
				throw new NullPointerException("Cannot bind to a null bean");
			} else if (fieldHandle == null) {
				throw new IllegalStateException("Cannot bind to a root "
						+ FieldBean.class.getSimpleName());
			}
			fieldHandle.setTarget(bean);
			setBean(fieldHandle.setDerivedValueFromAccessor());
		}

		/**
		 * @see BeanPathAdapter.FieldBean#performOperation(String, String,
		 *      Class, String, Observable, Class, SelectionModel, FieldProperty,
		 *      FieldBeanOperation)
		 */
		public <T> FieldProperty<?, ?, ?> performOperation(
				final String fieldPath, final Property<T> property,
				final Class<T> propertyValueClass,
				final FieldBeanOperation operation) {
			return performOperation(fieldPath, fieldPath, propertyValueClass,
					null, (Observable) property, null, null, null, operation);
		}

		/**
		 * @see BeanPathAdapter.FieldBean#performOperation(String, String,
		 *      Class, String, Observable, Class, SelectionModel, FieldProperty,
		 *      FieldBeanOperation)
		 */
		public <T> FieldProperty<?, ?, ?> performOperation(
				final String fieldPath, final ObservableList<T> observableList,
				final Class<T> listValueClass, final String collectionItemPath,
				final Class<?> collectionItemPathType,
				final SelectionModel<T> selectionModel,
				final FieldProperty<?, ?, ?> itemMaster,
				final FieldBeanOperation operation) {
			return performOperation(fieldPath, fieldPath, listValueClass,
					collectionItemPath, (Observable) observableList,
					collectionItemPathType, selectionModel, itemMaster,
					operation);
		}

		/**
		 * @see BeanPathAdapter.FieldBean#performOperation(String, String,
		 *      Class, String, Observable, Class, SelectionModel, FieldProperty,
		 *      FieldBeanOperation)
		 */
		public <T> FieldProperty<?, ?, ?> performOperation(
				final String fieldPath, final ObservableSet<T> observableSet,
				final Class<T> setValueClass, final String collectionItemPath,
				final Class<?> collectionItemPathType,
				final SelectionModel<T> selectionModel,
				final FieldProperty<?, ?, ?> itemMaster,
				final FieldBeanOperation operation) {
			return performOperation(fieldPath, fieldPath, setValueClass,
					collectionItemPath, (Observable) observableSet,
					collectionItemPathType, selectionModel, itemMaster,
					operation);
		}

		/**
		 * @see BeanPathAdapter.FieldBean#performOperation(String, String,
		 *      Class, String, Observable, Class, SelectionModel, FieldProperty,
		 *      FieldBeanOperation)
		 */
		public <K, V> FieldProperty<?, ?, ?> performOperation(
				final String fieldPath,
				final ObservableMap<K, V> observableMap,
				final Class<V> mapValueClass, final String collectionItemPath,
				final Class<?> collectionItemPathType,
				final SelectionModel<V> selectionModel,
				final FieldProperty<?, ?, ?> itemMaster,
				final FieldBeanOperation operation) {
			return performOperation(fieldPath, fieldPath, mapValueClass,
					collectionItemPath, (Observable) observableMap,
					collectionItemPathType, selectionModel, itemMaster,
					operation);
		}

		/**
		 * Performs a {@link FieldBeanOperation} by generating a
		 * {@link FieldProperty} based upon the supplied <b><code>.</code> </b>
		 * separated path to the field by traversing the matching children of
		 * the {@link FieldBean} until the corresponding {@link FieldProperty}
		 * is found (target bean uses the POJO from {@link FieldBean#getBean()}
		 * ). If the operation is bind and the {@link FieldProperty} doesn't
		 * exist all relative {@link FieldBean}s in the path will be
		 * instantiated using a no-argument constructor until the
		 * {@link FieldProperty} is created and bound to the supplied
		 * {@link Property}. The process is reciprocated until all path
		 * {@link FieldBean} and {@link FieldProperty} attributes of the field
		 * path are extinguished.
		 * 
		 * @see Bindings#bindBidirectional(Property, Property)
		 * @see Bindings#unbindBidirectional(Property, Property)
		 * @param fullFieldPath
		 *            the full <code>.</code> separated field names (used in
		 *            recursion of method call to maintain the original path and
		 *            should not be used in initial method invocation)
		 * @param fieldPath
		 *            the <code>.</code> separated field names
		 * @param propertyValueClass
		 *            the class of the {@link Property} value type (only needed
		 *            when binding)
		 * @param collectionItemPath
		 *            the the <code>.</code> separated field names of the
		 *            {@link Observable} collection (only applicable when the
		 *            {@link Observable} is a {@link ObservableList},
		 *            {@link ObservableSet}, or {@link ObservableMap})
		 * @param observable
		 *            the {@link Property}, {@link ObservableList},
		 *            {@link ObservableSet}, or {@link ObservableMap} to perform
		 *            the {@link FieldBeanOperation} on
		 * @param collectionItemType
		 *            the {@link Observable} {@link Class} of each item in the
		 *            {@link Observable} collection (only applicable when the
		 *            {@link Observable} is a {@link ObservableList},
		 *            {@link ObservableSet}, or {@link ObservableMap})
		 * @param selectionModel
		 *            the {@link SelectionModel} used to set the values within
		 *            the {@link Observable} <b>only applicable when the
		 *            {@link Observable} is used for selection(s) and therefore
		 *            cannot be updated directly because it is read-only</b>
		 * @param itemMaster
		 *            the {@link FieldProperty} that contains the item(s) that
		 *            the {@link SelectionModel} can select from
		 * @param operation
		 *            the {@link FieldBeanOperation}
		 * @return the {@link FieldProperty} the operation was performed on
		 *         (null when the operation was not performed on any
		 *         {@link FieldProperty}
		 */
		protected <T> FieldProperty<?, ?, ?> performOperation(
				final String fullFieldPath, final String fieldPath,
				final Class<T> propertyValueClass,
				final String collectionItemPath, final Observable observable,
				final Class<?> collectionItemType,
				final SelectionModel<T> selectionModel,
				final FieldProperty<?, ?, ?> itemMaster,
				final FieldBeanOperation operation) {
			final String[] fieldNames = fieldPath.split("\\" + PATH_SEPARATOR);
			final boolean isField = fieldNames.length == 1;
			final String pkey = isField ? fieldNames[0] : "";
			final boolean isFieldProp = isField
					&& getFieldProperties().containsKey(pkey);
			final boolean isFieldSelProp = isField && !isFieldProp
					&& getFieldSelectionProperties().containsKey(pkey);
			if (isFieldProp || isFieldSelProp) {
				final FieldProperty<BT, ?, ?> fp = isFieldSelProp ? getFieldSelectionProperties()
						.get(pkey) : getFieldProperties().get(pkey);
				performOperation(fp, observable, propertyValueClass, operation);
				return fp;
			} else if (!isField && getFieldBeans().containsKey(fieldNames[0])) {
				// progress to the next child field/bean in the path chain
				final String nextFieldPath = fieldPath.replaceFirst(
						fieldNames[0] + PATH_SEPARATOR, "");
				return getFieldBeans().get(fieldNames[0]).performOperation(
						fullFieldPath, nextFieldPath, propertyValueClass,
						collectionItemPath, observable, collectionItemType,
						selectionModel, itemMaster, operation);
			} else if (operation != FieldBeanOperation.UNBIND) {
				// add a new bean/property chain
				if (isField) {
					final Class<?> fieldClass = FieldHandle.getAccessorType(
							getBean(), fieldNames[0]);
					final FieldProperty<BT, ?, ?> childProp = new FieldProperty/*won't compile in JDK8: <>*/(
							getBean(), fullFieldPath, fieldNames[0],
							notifyProperty,
							propertyValueClass == fieldClass ? fieldClass
									: Object.class, collectionItemPath,
							observable, collectionItemType, selectionModel,
							itemMaster);
					addOrUpdateFieldProperty(childProp);
					return performOperation(fullFieldPath, fieldNames[0],
							propertyValueClass, collectionItemPath, observable,
							collectionItemType, selectionModel, itemMaster,
							operation);
				} else {
					// create a handle to set the bean as a child of the current
					// bean
					// if the child bean exists on the bean it will remain
					// unchanged
					final FieldHandle<BT, Object> pfh = new FieldHandle<>(
							getBean(), fieldNames[0], Object.class);
					final FieldBean<BT, ?> childBean = new FieldBean<>(this,
							pfh, notifyProperty);
					// progress to the next child field/bean in the path chain
					final String nextFieldPath = fieldPath.substring(fieldPath
							.indexOf(fieldNames[1]));
					return childBean.performOperation(fullFieldPath,
							nextFieldPath, propertyValueClass,
							collectionItemPath, observable, collectionItemType,
							selectionModel, itemMaster, operation);
				}
			}
			return null;
		}

		/**
		 * Performs a {@link FieldBeanOperation} on a {@link FieldProperty} and
		 * an {@link Observable}
		 * 
		 * @param fp
		 *            the {@link FieldProperty}
		 * @param observable
		 *            the {@link Property}, {@link ObservableList},
		 *            {@link ObservableSet}, or {@link ObservableMap} to perform
		 *            the {@link FieldBeanOperation} on
		 * @param observableValueClass
		 *            the {@link Class} of the {@link Observable} value
		 * @param operation
		 *            the {@link FieldBeanOperation}
		 */
		@SuppressWarnings("unchecked")
		protected <T> void performOperation(final FieldProperty<BT, ?, ?> fp,
				final Observable observable,
				final Class<T> observableValueClass,
				final FieldBeanOperation operation) {
			if (operation == FieldBeanOperation.CREATE_OR_FIND) {
				return;
			}
			// because of the inverse relationship of the bidirectional
			// bind the initial value needs to be captured and reset as
			// a dirty value or the bind operation will overwrite the
			// initial value with the value of the passed property
			final Object val = fp.getDirty();
			if (Property.class.isAssignableFrom(observable.getClass())) {
				if (operation == FieldBeanOperation.UNBIND) {
					Bindings.unbindBidirectional((Property<T>) fp,
							(Property<T>) observable);
				} else if (operation == FieldBeanOperation.BIND) {
					if (fp.getFieldType() == fp.getDeclaredFieldType()) {
						Bindings.bindBidirectional((Property<T>) fp,
								(Property<T>) observable);
					} else {
						Bindings.bindBidirectional(
								(Property<String>) fp,
								(Property<T>) observable,
								(StringConverter<T>) getFieldStringConverter(observableValueClass));
					}
				}
			} else if (fp.getCollectionObservable() != null
					&& observable != null
					&& fp.getCollectionObservable() != observable) {
				// handle scenario where multiple observable collections/maps
				// are being bound to the same field property
				if (operation == FieldBeanOperation.UNBIND) {
					Bindings.unbindContentBidirectional(
							fp.getCollectionObservable(), observable);
				} else if (operation == FieldBeanOperation.BIND) {
					if (FieldProperty.isObservableList(observable)
							&& fp.isObservableList()) {
						Bindings.bindContentBidirectional(
								(ObservableList<Object>) observable,
								(ObservableList<Object>) fp
										.getCollectionObservable());
					} else if (FieldProperty.isObservableSet(observable)
							&& fp.isObservableSet()) {
						Bindings.bindContentBidirectional(
								(ObservableSet<Object>) observable,
								(ObservableSet<Object>) fp
										.getCollectionObservable());
					} else if (FieldProperty.isObservableMap(observable)
							&& fp.isObservableMap()) {
						Bindings.bindContentBidirectional(
								(ObservableMap<Object, Object>) observable,
								(ObservableMap<Object, Object>) fp
										.getCollectionObservable());
					} else {
						throw new UnsupportedOperationException(
								String.format(
										"Incompatible observable collection/map types cannot be bound %1$s and %2$s",
										fp.getCollectionObservable(),
										observable));
					}
				}
			} else if (operation == FieldBeanOperation.UNBIND) {
				fp.set(null);
			}
			// reset initial dirty value
			final Object currVal = fp.getDirty();
			if (val != null && val.toString() != null
					&& !val.toString().isEmpty() && !val.equals(currVal)
					&& !fp.hasDefaultDerived()) {
				fp.setDirty(val);
			}
		}

		/**
		 * @return the field name that the {@link FieldBean} represents in it's
		 *         parent (null when the {@link FieldBean} is root)
		 */
		public String getFieldName() {
			return fieldHandle != null ? fieldHandle.getFieldName() : null;
		}

		/**
		 * @return the parent {@link FieldBean} (null when the {@link FieldBean}
		 *         is root)
		 */
		public FieldBean<?, PT> getParent() {
			return parent;
		}

		/**
		 * @see #getFieldProperties()
		 * @see #getFieldSelectionProperties()
		 * @return the {@link Map} of fields that belong to the
		 *         {@link FieldBean} that are not a {@link FieldProperty}, but
		 *         rather exist as a {@link FieldBean} that may or may not
		 *         contain their own {@link FieldProperty} instances
		 */
		protected Map<String, FieldBean<BT, ?>> getFieldBeans() {
			return fieldBeans;
		}

		/**
		 * @see #getFieldSelectionProperties()
		 * @see #getFieldBeans()
		 * @return the {@link Map} of fields that belong to the
		 *         {@link FieldBean} that are not {@link FieldBean}s, but rather
		 *         exist as a {@link FieldProperty} and are not
		 *         {@link #getFieldSelectionProperties()}
		 */
		protected Map<String, FieldProperty<BT, ?, ?>> getFieldProperties() {
			return fieldProperties;
		}

		/**
		 * @see #getFieldProperties()
		 * @see #getFieldBeans()
		 * @return the {@link Map} of fields that belong to the
		 *         {@link FieldBean} that are not {@link FieldBean}s, but rather
		 *         exist as a {@link FieldProperty} that are not
		 *         {@link #getFieldProperties()}
		 */
		protected Map<String, FieldProperty<BT, ?, ?>> getFieldSelectionProperties() {
			return fieldSelectionProperties;
		}

		/**
		 * @see #getFieldProperties()
		 * @see #getFieldSelectionProperties()
		 * @return the {@link FieldProperty} with the given name that belongs to
		 *         the {@link FieldBean} (null when the name does not exist)
		 */
		public FieldProperty<BT, ?, ?> getFieldProperty(
				final String proptertyName) {
			if (getFieldProperties().containsKey(proptertyName)) {
				return getFieldProperties().get(proptertyName);
			} else if (getFieldSelectionProperties().containsKey(proptertyName)) {
				return getFieldSelectionProperties().get(proptertyName);
			}
			return null;
		}

		/**
		 * Gets/Creates (if not already created) a {@link FieldStringConverter}.
		 * 
		 * @param targetClass
		 *            the target class of the {@link FieldStringConverter}
		 * @return the {@link FieldStringConverter}
		 */
		@SuppressWarnings("unchecked")
		public <FCT, SMT> FieldStringConverter<FCT> getFieldStringConverter(
				final Class<FCT> targetClass) {
			if (stringConverters.containsKey(targetClass)) {
				return (FieldStringConverter<FCT>) stringConverters
						.get(targetClass);
			} else {
				final FieldStringConverter<FCT> fsc = new FieldStringConverter<>(
						targetClass);
				stringConverters.put(targetClass, fsc);
				return fsc;
			}
		}
	}

	/**
	 * Coercible {@link StringConverter} that handles conversions between
	 * strings and a target class when used in the binding process
	 * {@link Bindings#bindBidirectional(Property, Property, StringConverter)}
	 * 
	 * @see StringConverter
	 * @param <T>
	 *            the target class type that is used in the coercion of the
	 *            string
	 */
	protected static class FieldStringConverter<T> extends StringConverter<T> {

		public static final SimpleDateFormat SDF = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ssz");
		private final Class<T> targetClass;

		/**
		 * Constructor
		 * 
		 * @param targetClass
		 *            the class that the {@link FieldStringConverter} is
		 *            targeting
		 */
		public FieldStringConverter(final Class<T> targetClass) {
			this.targetClass = targetClass;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public T fromString(final String string) {
			return coerce(string, targetClass);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString(final T object) {
			return coerceToString(object);
		}

		/**
		 * @return the target class that is used in the coercion of the string
		 */
		public Class<T> getTargetClass() {
			return targetClass;
		}

		/**
		 * Attempts to coerce a value into a {@link String}
		 * 
		 * @param v
		 *            the value to coerce
		 * @return the coerced value (null when value failed to be coerced)
		 */
		public static <VT> String coerceToString(final VT v) {
			String cv = null;
			if (v != null
					&& SelectionModel.class.isAssignableFrom(v.getClass())) {
				cv = ((SelectionModel<?>) v).getSelectedItem() != null ? ((SelectionModel<?>) v)
						.getSelectedItem().toString() : null;
			} else if (v != null
					&& (Calendar.class.isAssignableFrom(v.getClass()) || Date.class
							.isAssignableFrom(v.getClass()))) {
				final Date date = Date.class.isAssignableFrom(v.getClass()) ? (Date) v
						: ((Calendar) v).getTime();
				cv = SDF.format(date);
			} else if (v != null) {
				cv = v.toString();
			}
			return cv;
		}

		/**
		 * Attempts to coerce a value into the specified class
		 * 
		 * @param v
		 *            the value to coerce
		 * @param targetClass
		 *            the class to coerce to
		 * @return the coerced value (null when value failed to be coerced)
		 */
		@SuppressWarnings("unchecked")
		public static <VT> VT coerce(final Object v, final Class<VT> targetClass) {
			if (targetClass == Object.class) {
				return (VT) v;
			}
			VT val;
			final boolean isStringType = targetClass.equals(String.class);
			if (v == null
					|| (!isStringType && v.toString() != null && v.toString()
							.isEmpty())) {
				val = (VT) FieldHandle.defaultValue(targetClass);
			} else if (isStringType
					|| (v != null && targetClass.isAssignableFrom(v.getClass()))) {
				val = (VT) targetClass.cast(v);
			} else if (v != null && Date.class.isAssignableFrom(targetClass)) {
				if (Calendar.class.isAssignableFrom(v.getClass())) {
					val = (VT) ((Calendar) v).getTime();
				} else {
					try {
						val = (VT) SDF.parse(v.toString());
					} catch (final Throwable t) {
						throw new IllegalArgumentException(String.format(
								"Unable to convert %1$s to %2$s", v,
								targetClass), t);
					}
				}
			} else if (v != null
					&& Calendar.class.isAssignableFrom(targetClass)) {
				final Calendar cal = Calendar.getInstance();
				Date date = null;
				try {
					date = Date.class.isAssignableFrom(v.getClass()) ? (Date) v
							: SDF.parse(v.toString());
					cal.setTime(date);
					val = (VT) cal;
				} catch (final Throwable t) {
					throw new IllegalArgumentException(String.format(
							"Unable to convert %1$s to %2$s", v, targetClass),
							t);
				}
			} else {
				val = FieldHandle.valueOf(targetClass, v.toString());
			}
			return val;
		}
	}

	/**
	 * A {@link Property} extension that uses a bean's getter/setter to define
	 * the {@link Property}'s value.
	 * 
	 * @param <BT>
	 *            the bean type
	 * @param <T>
	 *            the field type
	 * @param <PT>
	 *            the {@link FieldProperty#get()} type
	 */
	public static class FieldProperty<BT, T, PT> extends ObjectPropertyBase<PT>
			implements ListChangeListener<Object>, SetChangeListener<Object>,
			MapChangeListener<Object, Object>, ChangeListener<Object> {

		private final FieldPathValueProperty notifyProperty;
		private final String fullPath;
		private final FieldHandle<BT, T> fieldHandle;
		private boolean isDirty;
		private boolean isDirtyCollection;
		private boolean isCollectionListening;
		private final String collectionItemPath;
		private final WeakReference<Observable> collectionObservable;
		private final Class<?> collectionType;
		private final SelectionModel<Object> collectionSelectionModel;
		private final FieldProperty<?, ?, ?> itemMaster;

		/**
		 * Constructor
		 * 
		 * @param bean
		 *            the bean that the path belongs to
		 * @param fullPath
		 *            the full <code>.</code> separated path to the
		 *            {@link FieldProperty}
		 * @param fieldName
		 *            the name of the field within the bean
		 * @param notifyProperty
		 *            the {@link FieldPathValueProperty} that will be set every
		 *            time the {@link FieldProperty#setValue(Object)} is
		 *            performed or an item within the value is changed
		 * @param declaredFieldType
		 *            the declared {@link Class} of the field
		 * @param collectionItemPath
		 *            the the <code>.</code> separated field names of the
		 *            {@link Observable} collection (only applicable when the
		 *            {@link Observable} is a {@link ObservableList},
		 *            {@link ObservableSet}, or {@link ObservableMap})
		 * @param collectionObservable
		 *            the {@link Observable} {@link Collection} used to bind to
		 *            the {@link FieldProperty} <b>OR</b> when the
		 *            {@link SelectionModel} is specified this is the
		 *            {@link Observable} {@link Collection} of available items
		 *            to select from
		 * @param collectionType
		 *            the {@link Collection} {@link Class} used to attempt to
		 *            transform the underlying field {@link Observable}
		 *            {@link Collection} to the {@link Collection} {@link Class}
		 *            (only applicable when the actual field is a
		 *            {@link Collection})
		 * @param collectionSelectionModel
		 *            the {@link SelectionModel} used to set the values within
		 *            the {@link Observable} <b>only applicable when the
		 *            {@link Observable} is used for selection(s) and therefore
		 *            cannot be updated directly because it is read-only</b>
		 * @param itemMaster
		 *            the {@link FieldProperty} that contains the item(s) that
		 *            the {@link SelectionModel} can select from
		 */
		@SuppressWarnings("unchecked")
		protected FieldProperty(final BT bean, final String fullPath,
				final String fieldName,
				final FieldPathValueProperty notifyProperty,
				final Class<T> declaredFieldType,
				final String collectionItemPath,
				final Observable collectionObservable,
				final Class<?> collectionType,
				final SelectionModel<?> collectionSelectionModel,
				final FieldProperty<?, ?, ?> itemMaster) {
			super();
			this.fullPath = fullPath;
			this.notifyProperty = notifyProperty;
			this.fieldHandle = new FieldHandle<BT, T>(bean, fieldName,
					declaredFieldType);
			this.itemMaster = itemMaster;
			this.collectionObservable = new WeakReference<Observable>(
					collectionObservable);
			this.collectionItemPath = collectionItemPath;
			this.collectionType = collectionType;
			this.collectionSelectionModel = (SelectionModel<Object>) collectionSelectionModel;
			if (this.collectionSelectionModel != null
					&& this.itemMaster != null) {
				this.itemMaster.addListener(this);
			}
			setDerived();
		}

		/**
		 * {@inheritDoc}
		 */
		@SuppressWarnings("unchecked")
		@Override
		public PT get() {
			try {
				final Object dv = getDirty();
				if (dv != null && getDeclaredFieldType() != getFieldType()) {
					return (PT) FieldStringConverter.coerceToString(dv);
				}
				return (PT) dv;
			} catch (final Throwable t) {
				throw new RuntimeException("Unable to get value", t);
			}
		}

		/**
		 * Sets the {@link FieldHandle#deriveValueFromAccessor()} value
		 */
		protected void setDerived() {
			final T derived = fieldHandle.deriveValueFromAccessor();
			set(derived);
		}

		/**
		 * Flags the {@link Property} value as dirty and calls
		 * {@link #set(Object)}
		 * 
		 * @param v
		 *            the value to set
		 */
		public void setDirty(final Object v) {
			isDirty = true;
			set(v);
		}

		/**
		 * Sets an {@link Object} value
		 * 
		 * @param v
		 *            the value to set
		 */
		@Override
		public void set(final Object v) {
			try {
				final Object cv = fieldHandle.getAccessor().invoke();
				final Class<?> clazz = cv != null ? cv.getClass() : fieldHandle
						.getFieldType();
				if (v != null
						&& (Collection.class.isAssignableFrom(v.getClass()) || Map.class
								.isAssignableFrom(v.getClass()))) {
					fieldHandle.getSetter().invoke(v);
					postSet(cv);
				} else if (isDirty || cv != v) {
					final Object val = FieldStringConverter.coerce(v, clazz);
					fieldHandle.getSetter().invoke(val);
					postSet(cv);
				}
			} catch (final Throwable t) {
				throw new IllegalArgumentException(String.format(
						"Unable to set object value: %1$s on %2$s", v,
						fieldHandle.getFieldName()), t);
			}
		}

		/**
		 * Executes any post processing that needs to take place after set
		 * operation takes place
		 * 
		 * @param prevValue
		 *            the {@link #getValue()} before {@link #setValue(Object)}
		 *            was called
		 * @throws Throwable
		 *             thrown when any errors occur when processing a post set
		 *             operation
		 */
		protected final void postSet(final Object prevValue) throws Throwable {
			final Boolean colChanged = populateObservableCollection();
			if (colChanged == null || colChanged) {
				invalidated();
				fireValueChangedEvent();
			}
			try {
				// all collection/map item value changes will be captured at the
				// collection/map level unless the collection/map level types
				// are not registered (in which case a normal change will be
				// evaluated
				if (!isDirty
						&& (fullPath.indexOf(COLLECTION_ITEM_PATH_SEPARATOR) < 0 || (notifyProperty
								.hasTypes(FieldPathValueType.FIELD_CHANGE)
								&& !hasFieldPathValueTypeAddOrRemove(true) && !hasFieldPathValueTypeAddOrRemove(false)))) {
					final Object cv = getDirty();
					if ((cv == null && prevValue != null)
							|| (cv != null && !cv.equals(prevValue))) {
						notifyProperty
								.set(new FieldPathValue(fullPath, getBean(),
										cv, FieldPathValueType.FIELD_CHANGE));
					}
				}
			} finally {
				isDirty = false;
			}
		}

		/**
		 * Updates the {@link Observable} when the field represents a supported
		 * {@link Collection}. If the {@link #collectionType} is defined an
		 * attempt will be made to transform the {@link Observable}
		 * {@link Collection} to it.
		 * 
		 * @throws Throwable
		 *             thrown when {@link FieldHandle#getSetter()} cannot be
		 *             invoked, the {@link #getDirty()} cannot be cast to
		 *             {@link FieldHandle#getFieldType()}, or the
		 *             {@link #getDirty()} cannot be transformed using the
		 *             {@link #collectionType}
		 * @return true when the collection has been populated
		 */
		private Boolean populateObservableCollection() throws Throwable {
			final Observable oc = getCollectionObservable();
			Boolean changed = null;
			if (isList() || isSet()) {
				addRemoveCollectionListener(oc, false);
				Collection<?> items = (Collection<?>) getDirty();
				if (items == null) {
					items = new LinkedHashSet<>();
					fieldHandle.getSetter().invoke(items);
				}
				changed = syncCollectionValues(items, false, false, null, null,
						null);
			} else if (isMap()) {
				addRemoveCollectionListener(oc, false);
				Map<?, ?> items = (Map<?, ?>) getDirty();
				if (items == null) {
					items = new HashMap<>();
					fieldHandle.getSetter().invoke(items);
				}
				changed = syncCollectionValues(items, false, false, null, null,
						null);
			} else {
				return changed;
			}
			addRemoveCollectionListener(oc, true);
			return changed;
		}

		/**
		 * Synchronizes the collection items used in the {@link FieldProperty}
		 * and {@link Observable} collection.
		 * {@link Bindings#bindContentBidirectional(ObservableList, ObservableList)}
		 * variants are/cannot be not used.
		 * 
		 * @param values
		 *            the {@link List}, {@link Set}, or {@link Map} that should
		 *            be synchronized
		 * @param toField
		 *            true when synchronization needs to occur on the
		 *            {@link FieldProperty} collection, false when
		 *            synchronization needs to occur on the {@link Observable}
		 *            collection
		 * @param fromItemMasterChange
		 *            true when the synchronization is from a change made to the
		 *            {@link #itemMaster}
		 * @param listChange
		 *            any {@link ListChangeListener.Change}
		 * @param setChange
		 *            any {@link SetChangeListener.Change}
		 * @param mapChange
		 *            any {@link MapChangeListener.Change}
		 * @return true when the synchronization resulted in a change to the
		 *         {@link Collection}/{@link Map}
		 */
		@SuppressWarnings("unchecked")
		private boolean syncCollectionValues(final Object values,
				final boolean toField, final boolean fromItemMasterChange,
				final ListChangeListener.Change<?> listChange,
				final SetChangeListener.Change<?> setChange,
				final MapChangeListener.Change<?, ?> mapChange) {
			boolean changed = false;
			if (isDirtyCollection) {
				return changed;
			}
			if (collectionSelectionModel != null
					&& itemMaster != null
					&& itemMaster.isDirtyCollection
					&& (listChange != null || setChange != null || mapChange != null)) {
				// selections shouldn't get synchronized while the item master
				// is in the middle of getting synchronized
				return changed;
			}
			try {
				isDirtyCollection = true;
				// TODO : Use a more elegant technique to synchronize the
				// observable
				// and the bean collections that doesn't require clearing and
				// resetting them? (see commented onChange methods from revision
				// 204)
				if (this.collectionObservable.get() != null
						&& Collection.class
								.isAssignableFrom(this.collectionObservable
										.get().getClass())) {
					final Collection<Object> oc = (Collection<Object>) this.collectionObservable
							.get();
					if (Collection.class.isAssignableFrom(values.getClass())) {
						final Collection<Object> col = (Collection<Object>) values;
						if (toField) {
							changed = syncCollectionValuesFromObservable(col,
									oc);
						} else {
							final boolean wasColEmpty = col.isEmpty();
							if (collectionSelectionModel == null
									&& (!wasColEmpty || isDirty)) {
								oc.clear();
								changed = true;
							} else if (collectionSelectionModel == null) {
								changed = syncCollectionValuesFromObservable(
										col, oc);
							}
							if (!wasColEmpty) {
								syncObservableFromCollectionValues(col, oc);
							}
						}
					} else if (Map.class.isAssignableFrom(values.getClass())) {
						final Map<Object, Object> map = (Map<Object, Object>) values;
						if (toField) {
							changed = syncCollectionValuesFromObservable(map,
									oc);
						} else {
							final boolean wasColEmpty = map.isEmpty();
							if (collectionSelectionModel == null
									&& (!wasColEmpty || isDirty)) {
								oc.clear();
								changed = true;
							} else if (collectionSelectionModel == null) {
								changed = syncCollectionValuesFromObservable(
										map, oc);
							}
							if (!wasColEmpty) {
								syncObservableFromCollectionValues(map, oc);
							}
						}
					}
				} else if (this.collectionObservable.get() instanceof ObservableMap) {
					final ObservableMap<Object, Object> oc = (ObservableMap<Object, Object>) this.collectionObservable
							.get();
					if (Collection.class.isAssignableFrom(values.getClass())) {
						final Collection<Object> col = (Collection<Object>) values;
						if (toField) {
							changed = syncCollectionValuesFromObservable(col,
									oc);
						} else {
							final boolean wasColEmpty = col.isEmpty();
							if (collectionSelectionModel == null
									&& (!wasColEmpty || isDirty)) {
								oc.clear();
								changed = true;
							} else if (collectionSelectionModel == null) {
								changed = syncCollectionValuesFromObservable(
										col, oc);
							}
							if (!wasColEmpty) {
								syncObservableFromCollectionValues(col, oc);
							}
						}
					} else if (Map.class.isAssignableFrom(values.getClass())) {
						final Map<Object, Object> map = (Map<Object, Object>) values;
						if (toField) {
							changed = syncCollectionValuesFromObservable(map,
									oc);
						} else {
							final boolean wasColEmpty = map.isEmpty();
							if (collectionSelectionModel == null
									&& (!wasColEmpty || isDirty)) {
								oc.clear();
								changed = true;
							} else if (collectionSelectionModel == null) {
								changed = syncCollectionValuesFromObservable(
										map, oc);
							}
							if (!wasColEmpty) {
								syncObservableFromCollectionValues(map, oc);
							}
						}
					}
				}
				return changed || listChange != null || setChange != null
						|| mapChange != null;
			} finally {
				isDirtyCollection = false;
			}
		}

		/**
		 * Synchronizes the {@link Collection} values to the supplied
		 * {@link Observable} {@link Collection}
		 * 
		 * @param fromCol
		 *            the {@link Collection} that synchronization will derive
		 *            from
		 * @param oc
		 *            the {@link Observable} {@link Collection} that should be
		 *            synchronized to
		 * @return true when the synchronization resulted in a change to the
		 *         {@link Collection}/{@link Map}
		 */
		private boolean syncObservableFromCollectionValues(
				final Collection<Object> fromCol, final Collection<Object> oc) {
			boolean changed = false;
			boolean missing = false;
			FieldProperty<?, ?, ?> fp;
			Object fpv;
			int i = -1;
			final boolean isOcList = List.class.isAssignableFrom(oc.getClass());
			for (final Object item : fromCol) {
				fp = genFieldProperty(item, null);
				fpv = fp != null ? fp.getDirty() : item;
				missing = !oc.contains(fpv);
				changed = !changed ? missing : changed;
				if (collectionSelectionModel == null) {
					if (isOcList) {
						((List<Object>) oc).add(++i, fpv);
					} else {
						oc.add(fpv);
					}
				} else {
					selectCollectionValue(fpv);
				}
			}
			return changed;
		}

		/**
		 * Synchronizes the {@link Collection} values to the supplied
		 * {@link Observable} {@link Map}
		 * 
		 * @param fromCol
		 *            the {@link Collection} that synchronization will derive
		 *            from
		 * @param oc
		 *            the {@link Observable} {@link Map} that should be
		 *            synchronized to
		 * @return true when the synchronization resulted in a change to the
		 *         {@link Collection}/{@link Map}
		 */
		private boolean syncObservableFromCollectionValues(
				final Collection<Object> fromCol, final Map<Object, Object> oc) {
			boolean changed = false;
			boolean missing = false;
			FieldProperty<?, ?, ?> fp;
			Object fpv;
			int i = -1;
			for (final Object item : fromCol) {
				fp = genFieldProperty(item, null);
				fpv = fp != null ? fp.getDirty() : item;
				missing = !oc.containsValue(fpv);
				changed = !changed ? missing : changed;
				if (collectionSelectionModel == null) {
					oc.put(++i, fpv);
				} else {
					selectCollectionValue(fpv);
				}
			}
			return changed;
		}

		/**
		 * Synchronizes the {@link Map} values to the supplied
		 * {@link Observable} {@link Collection}
		 * 
		 * @param fromMap
		 *            the {@link Map} that synchronization will derive from
		 * @param oc
		 *            the {@link Observable} {@link Collection} that should be
		 *            synchronized to
		 * @return true when the synchronization resulted in a change to the
		 *         {@link Collection}/{@link Map}
		 */
		private boolean syncObservableFromCollectionValues(
				final Map<Object, Object> fromMap, final Collection<Object> oc) {
			boolean changed = false;
			boolean missing = false;
			FieldProperty<?, ?, ?> fp;
			Object fpv;
			int i = -1;
			final boolean isOcList = List.class.isAssignableFrom(oc.getClass());
			for (final Object item : fromMap.values()) {
				fp = genFieldProperty(item, null);
				fpv = fp != null ? fp.getDirty() : item;
				missing = !oc.contains(fpv);
				changed = !changed ? missing : changed;
				if (collectionSelectionModel == null) {
					if (isOcList) {
						((List<Object>) oc).add(++i, fpv);
					} else {
						oc.add(fpv);
					}
				} else {
					selectCollectionValue(fpv);
				}
			}
			return changed;
		}

		/**
		 * Synchronizes the {@link Map} values to the supplied
		 * {@link Observable} {@link Map}
		 * 
		 * @param fromMap
		 *            the {@link Map} that synchronization will derive from
		 * @param oc
		 *            the {@link Observable} {@link Map} that should be
		 *            synchronized to
		 * @return true when the synchronization resulted in a change to the
		 *         {@link Collection}/{@link Map}
		 */
		private boolean syncObservableFromCollectionValues(
				final Map<Object, Object> fromMap, final Map<Object, Object> oc) {
			boolean changed = false;
			boolean missing = false;
			FieldProperty<?, ?, ?> fp;
			Object fpv;
			int i = -1;
			for (final Map.Entry<Object, Object> item : fromMap.entrySet()) {
				fp = genFieldProperty(item.getValue(), null);
				fpv = fp != null ? fp.getDirty() : item.getValue();
				missing = !oc.containsValue(fpv);
				changed = !changed ? missing : changed;
				if (collectionSelectionModel == null) {
					oc.put(++i, fpv);
				} else {
					selectCollectionValue(fpv);
				}
			}
			return changed;
		}

		/**
		 * Calls the {@link SelectionModel#select(Object)} the specified value
		 * 
		 * @param value
		 *            the value to select
		 */
		private void selectCollectionValue(final Object value) {
			if (collectionSelectionModel == null) {
				return;
			}
			collectionSelectionModel.select(value);
		}

		/**
		 * Synchronizes the {@link Observable} {@link Collection} values to the
		 * supplied {@link Collection}
		 * 
		 * @param toCol
		 *            the {@link Collection} that should be synchronized to
		 * @param oc
		 *            the {@link Observable} {@link Collection} that
		 *            synchronization will derive from
		 * @return true when the synchronization resulted in a change to the
		 *         {@link Collection}/{@link Map}
		 */
		private boolean syncCollectionValuesFromObservable(
				final Collection<Object> toCol, final Collection<Object> oc) {
			boolean changed = false;
			boolean missing = false;
			final List<FieldPathValue> fvs = new ArrayList<>();
			FieldProperty<?, ?, ?> fp;
			Object fpv;
			final List<Object> nc = new ArrayList<>();
			for (final Object item : oc) {
				if (item != null) {
					fp = genFieldProperty(null, item);
					fpv = fp == null ? item : fp.getBean();
					missing = !toCol.contains(fpv);
					changed = !changed ? missing : changed;
					nc.add(fpv);
					if (missing && hasFieldPathValueTypeAddOrRemove(true)) {
						fvs.add(newSyncCollectionFieldPathValue(fp, fpv, true));
					}
				}
			}
			if (hasFieldPathValueTypeAddOrRemove(false)) {
				for (final Object item : toCol) {
					if (!nc.contains(item)) {
						fp = genFieldProperty(item, null);
						fpv = fp == null ? item : fp.getBean();
						fvs.add(newSyncCollectionFieldPathValue(fp, fpv, false));
					}
				}
			}
			toCol.clear();
			toCol.addAll(nc);
			setFieldPathValues(fvs);
			return changed;
		}

		/**
		 * Synchronizes the {@link Observable} {@link Collection} values to the
		 * supplied {@link Map}
		 * 
		 * @param toMap
		 *            the {@link Map} that should be synchronized to
		 * @param oc
		 *            the {@link Observable} {@link Map} that synchronization
		 *            will derive from
		 * @return true when the synchronization resulted in a change to the
		 *         {@link Collection}/{@link Map}
		 */
		private boolean syncCollectionValuesFromObservable(
				final Map<Object, Object> toMap, final Collection<Object> oc) {
			boolean changed = false;
			boolean missing = false;
			final List<FieldPathValue> fvs = new ArrayList<>();
			FieldProperty<?, ?, ?> fp;
			Object fpv;
			int i = -1;
			final Map<Object, Object> nc = new HashMap<>();
			for (final Object item : oc) {
				if (item != null) {
					fp = genFieldProperty(null, item);
					fpv = fp == null ? item : fp.getBean();
					missing = !toMap.containsValue(fpv);
					changed = !changed ? missing : changed;
					nc.put(++i, fpv);
					if (missing && hasFieldPathValueTypeAddOrRemove(true)) {
						fvs.add(newSyncCollectionFieldPathValue(fp, fpv, true));
					}
				}
			}
			if (hasFieldPathValueTypeAddOrRemove(false)) {
				for (final Object item : toMap.values()) {
					if (!nc.containsValue(item)) {
						fp = genFieldProperty(item, null);
						fpv = fp == null ? item : fp.getBean();
						fvs.add(newSyncCollectionFieldPathValue(fp, fpv, false));
					}
				}
			}
			toMap.clear();
			toMap.putAll(nc);
			setFieldPathValues(fvs);
			return changed;
		}

		/**
		 * Synchronizes the {@link Observable} {@link Map} values to the
		 * supplied {@link Collection}
		 * 
		 * @param toCol
		 *            the {@link Collection} that should be synchronized to
		 * @param oc
		 *            the {@link Observable} {@link Map} that synchronization
		 *            will derive from
		 * @return true when the synchronization resulted in a change to the
		 *         {@link Collection}/{@link Map}
		 */
		private boolean syncCollectionValuesFromObservable(
				final Collection<Object> toCol,
				final ObservableMap<Object, Object> oc) {
			boolean changed = false;
			boolean missing = false;
			final List<FieldPathValue> fvs = new ArrayList<>();
			FieldProperty<?, ?, ?> fp;
			Object fpv;
			final List<Object> nc = new ArrayList<>();
			for (final Map.Entry<Object, Object> item : oc.entrySet()) {
				if (item != null && item.getValue() != null) {
					fp = genFieldProperty(null, item.getValue());
					fpv = fp == null ? item.getValue() : fp.getBean();
					missing = !toCol.contains(fpv);
					changed = !changed ? missing : changed;
					nc.add(fpv);
					if (missing && hasFieldPathValueTypeAddOrRemove(true)) {
						fvs.add(newSyncCollectionFieldPathValue(fp, fpv, true));
					}
				}
			}
			if (hasFieldPathValueTypeAddOrRemove(false)) {
				for (final Object item : toCol) {
					if (!nc.contains(item)) {
						fp = genFieldProperty(item, null);
						fpv = fp == null ? item : fp.getBean();
						fvs.add(newSyncCollectionFieldPathValue(fp, fpv, false));
					}
				}
			}
			toCol.clear();
			toCol.addAll(nc);
			setFieldPathValues(fvs);
			return changed;
		}

		/**
		 * Synchronizes the {@link Observable} {@link Map} values to the
		 * supplied {@link Map}
		 * 
		 * @param toMap
		 *            the {@link Map} that should be synchronized to
		 * @param oc
		 *            the {@link Observable} {@link Collection} that
		 *            synchronization will derive from
		 * @return true when the synchronization resulted in a change to the
		 *         {@link Collection}/{@link Map}
		 */
		private boolean syncCollectionValuesFromObservable(
				final Map<Object, Object> toMap,
				final ObservableMap<Object, Object> oc) {
			boolean changed = false;
			boolean missing = false;
			final List<FieldPathValue> fvs = new ArrayList<>();
			FieldProperty<?, ?, ?> fp;
			Object fpv;
			int i = -1;
			final Map<Object, Object> nc = new HashMap<>();
			for (final Map.Entry<Object, Object> item : oc.entrySet()) {
				if (item != null && item.getValue() != null) {
					fp = genFieldProperty(null, item.getValue());
					fpv = fp == null ? item.getValue() : fp.getBean();
					missing = !toMap.containsValue(fpv);
					changed = !changed ? missing : changed;
					nc.put(i, fpv);
					if (missing && hasFieldPathValueTypeAddOrRemove(true)) {
						fvs.add(newSyncCollectionFieldPathValue(fp, fpv, true));
					}
				}
			}
			if (hasFieldPathValueTypeAddOrRemove(false)) {
				for (final Object item : toMap.values()) {
					if (!nc.containsValue(item)) {
						fp = genFieldProperty(item, null);
						fpv = fp == null ? item : fp.getBean();
						fvs.add(newSyncCollectionFieldPathValue(fp, fpv, false));
					}
				}
			}
			toMap.clear();
			toMap.putAll(nc);
			setFieldPathValues(fvs);
			return changed;
		}

		/**
		 * Creates a new {@link FieldPathValue} using specified
		 * {@link FieldProperty} or the current {@link FieldProperty} when the
		 * specified {@link FieldProperty} is null.
		 * 
		 * @param fp
		 *            the {@link FieldProperty} (optional)
		 * @param fpv
		 *            the {@link FieldProperty#getValue()}
		 * @param isAdd
		 *            true when adding an item
		 * @return the {@link FieldPathValue}
		 */
		protected FieldPathValue newSyncCollectionFieldPathValue(
				final FieldProperty<?, ?, ?> fp, final Object fpv,
				final boolean isAdd) {
			FieldPathValueType type;
			if (collectionSelectionModel != null) {
				type = isAdd ? FieldPathValueType.CONTENT_ITEM_ADD_SELECT
						: FieldPathValueType.CONTENT_ITEM_REMOVE_SELECT;
			} else {
				type = isAdd ? FieldPathValueType.CONTENT_ITEM_ADD
						: FieldPathValueType.CONTENT_ITEM_REMOVE;
			}
			if (fp == null) {
				return new FieldPathValue(fullPath, getBean(), fpv, type);
			} else {
				return new FieldPathValue(fp.fullPath, fp.getBean(), fpv, type);
			}
		}

		/**
		 * Determines if the {@link FieldPathValueProperty} is registered for
		 * adds or removals
		 * 
		 * @param add
		 *            true to check for add, false to check for remove
		 * @return true when the {@link FieldPathValueProperty} is registered
		 *         for the add or remove
		 */
		protected boolean hasFieldPathValueTypeAddOrRemove(final boolean add) {
			return (add && collectionSelectionModel != null && notifyProperty
					.hasTypes(FieldPathValueType.CONTENT_ITEM_ADD_SELECT))
					|| (add && collectionSelectionModel == null && notifyProperty
							.hasTypes(FieldPathValueType.CONTENT_ITEM_ADD))
					|| (!add && collectionSelectionModel != null && notifyProperty
							.hasTypes(FieldPathValueType.CONTENT_ITEM_REMOVE_SELECT))
					|| (!add && collectionSelectionModel == null && notifyProperty
							.hasTypes(FieldPathValueType.CONTENT_ITEM_REMOVE));
		}

		/**
		 * Sets a {@link Collection} of {@link FieldPathValue}(s) on the
		 * {@link #notifyProperty}
		 * 
		 * @param fieldPathValues
		 *            the {@link Collection} of {@link FieldPathValue}(s) to set
		 */
		protected void setFieldPathValues(
				final Collection<FieldPathValue> fieldPathValues) {
			if (notifyProperty != null) {
				for (final FieldPathValue o : fieldPathValues) {
					notifyProperty.set(o);
				}
			}
		}

		/**
		 * Generates a {@link FieldProperty} using the specified bean and sets
		 * the optional value on the bean (when the value is not null). The
		 * returned {@link FieldProperty} will contain the same value instance
		 * contained with the item master. When the item master is not available
		 * an attempt will be made to get the item value from the
		 * {@link #getDirty()} collection/map.
		 * 
		 * @param itemBeanValue
		 *            the collection {@link FieldBean} value to add/update. when
		 *            {@code null} the existing {@link FieldBean} value will
		 *            will be used unless it is {@code null} as well- in which
		 *            case an attempt will be made to instantiate a new instance
		 *            of the bean using a no-argument constructor
		 * @param itemBeanPropertyValue
		 *            the collection {@link FieldBean}'s {@link FieldProperty}
		 *            value to add/update (null when no update should be made to
		 *            the {@link FieldBean}'s {@link FieldProperty} value)
		 * @return the {@link FieldProperty} for the collection item (null when
		 *         none is required)
		 */
		protected FieldProperty<?, ?, ?> genFieldProperty(
				final Object itemBeanValue, final Object itemBeanPropertyValue) {
			try {
				// simple collection items that do not have a path do not
				// require an update
				if (collectionItemPath == null || collectionItemPath.isEmpty()) {
					return null;
				} else if (itemBeanValue == null
						&& itemBeanPropertyValue == null) {
					throw new NullPointerException(
							"Both itemBeanValue and itemBeanPropertyValue cannot be null");
				}
				Object value = itemBeanPropertyValue;
				Object bean = itemBeanValue == null ? collectionType
						.newInstance() : itemBeanValue;
				FieldProperty<?, ?, ?> fp = genCollectionFieldProperty(bean);
				if (value != null) {
					fp.setDirty(value);
				} else {
					value = fp.getDirty();
				}
				if (itemBeanPropertyValue != null) {
					// ensure that any selection values come from the item
					// master and any updates to an existing bean return a field
					// property of the same bean reference/target
					Object im = itemMaster != null ? itemMaster.getDirty()
							: getDirty();
					FieldProperty<?, ?, ?> imfp;
					if (Collection.class.isAssignableFrom(im.getClass())) {
						for (final Object ib : (Collection<?>) im) {
							imfp = genCollectionFieldProperty(ib);
							if (imfp.getDirty() == value) {
								return imfp;
							}
						}
					} else if (Map.class.isAssignableFrom(im.getClass())) {
						for (final Map.Entry<?, ?> ib : ((Map<?, ?>) im)
								.entrySet()) {
							imfp = genCollectionFieldProperty(ib.getValue());
							if (imfp.getDirty() == value) {
								return imfp;
							}
						}
					}
				}
				return fp;
			} catch (InstantiationException | IllegalAccessException e) {
				throw new UnsupportedOperationException(String.format(
						"Cannot create collection item bean using %1$s",
						collectionType), e);
			}
		}

		/**
		 * Generates a {@link FieldProperty} for a
		 * {@link #getCollectionItemPath()} and
		 * {@link #getCollectionSelectionModel()} when applicable
		 * 
		 * @param bean
		 *            the bean to generate a {@link FieldProperty}
		 * @return the generated {@link FieldProperty}
		 */
		protected FieldProperty<?, ?, ?> genCollectionFieldProperty(
				final Object bean) {
			FieldBean<Void, Object> fb;
			FieldProperty<?, ?, ?> fp;
			fb = new FieldBean<>(null, bean, null, notifyProperty);
			fp = fb.performOperation(fullPath + COLLECTION_ITEM_PATH_SEPARATOR
					+ collectionItemPath, collectionItemPath, Object.class,
					null, null, null, collectionSelectionModel, null,
					FieldBeanOperation.CREATE_OR_FIND);
			return fp;
		}

		/**
		 * Updates the underlying collection item value
		 * 
		 * see updateCollectionItemBean(int, Object, Object)
		 * @param itemBeanPropertyValue
		 *            the collection {@link FieldBean}'s {@link FieldProperty}
		 *            value to add/update
		 * @return {@link FieldProperty#getBean()} when the collection item has
		 *         it's own bean path, the <code>
		 *         itemBeanPropertyValue</code> when it does not
		 */
		protected Object updateCollectionItemProperty(
				final Object itemBeanPropertyValue) {
			final FieldProperty<?, ?, ?> fp = genFieldProperty(null,
					itemBeanPropertyValue);
			return fp == null ? itemBeanPropertyValue : fp.getBean();
		}

		/**
		 * Adds/Removes the {@link FieldProperty} as a collection listener
		 * 
		 * @param observable
		 *            the {@link Observable} collection/map to listen for
		 *            changes on
		 * @param add
		 *            true to add, false to remove
		 */
		protected void addRemoveCollectionListener(final Observable observable,
				final boolean add) {
			final boolean isCol = getCollectionObservable() == observable;
			if (isCol
					&& ((this.isCollectionListening && add) || (this.isCollectionListening && !add))) {
				return;
			}
			Boolean change = null;
			if (observable instanceof ObservableList) {
				final ObservableList<?> ol = (ObservableList<?>) observable;
				if (add) {
					ol.addListener(this);
					change = true;
				} else {
					ol.removeListener(this);
					change = false;
				}
			} else if (observable instanceof ObservableSet) {
				final ObservableSet<?> os = (ObservableSet<?>) observable;
				if (add) {
					os.addListener(this);
					change = true;
				} else {
					os.removeListener(this);
					change = false;
				}
			} else if (observable instanceof ObservableMap) {
				final ObservableMap<?, ?> om = (ObservableMap<?, ?>) observable;
				if (add) {
					om.addListener(this);
					change = true;
				} else {
					om.removeListener(this);
					change = false;
				}
			} else if (observable == null) {
				throw new IllegalStateException(String.format(
						"Observable collection/map bound to %1$s (item path: %2$s) "
								+ "has been garbage collected",
						this.fieldHandle.getFieldName(),
						this.collectionItemPath, observable,
						this.getFieldType()));
			} else {
				throw new UnsupportedOperationException(String.format(
						"%1$s (item path: %2$s) of type \"%4$s\" "
								+ "must be bound to a supported "
								+ "observable collection/map type... "
								+ "Found observable: %3$s",
						this.fieldHandle.getFieldName(),
						this.collectionItemPath, observable,
						this.getFieldType()));
			}
			if (isCol && change != null) {
				this.isCollectionListening = change;
			}
		}

		/**
		 * Detects {@link #itemMaster} changes for selection synchronization
		 */
		@Override
		public void changed(final ObservableValue<? extends Object> observable,
				final Object oldValue, final Object newValue) {
			syncCollectionValues(getDirty(), false, true, null, null, null);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public final void onChanged(
				ListChangeListener.Change<? extends Object> change) {
			syncCollectionValues(getDirty(), true, false, change, null, null);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public final void onChanged(
				SetChangeListener.Change<? extends Object> change) {
			syncCollectionValues(getDirty(), true, false, null, change, null);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public final void onChanged(
				MapChangeListener.Change<? extends Object, ? extends Object> change) {
			syncCollectionValues(getDirty(), true, false, null, null, change);
		}

		/**
		 * @return the dirty value before conversion takes place
		 */
		public Object getDirty() {
			try {
				return fieldHandle.getAccessor().invoke();
			} catch (final Throwable t) {
				throw new RuntimeException("Unable to get dirty value", t);
			}
		}

		/**
		 * Binds a new target to the {@link FieldHandle}
		 * 
		 * @param bean
		 *            the target bean to bind to
		 */
		protected void setTarget(final BT bean) {
			isDirty = true;
			fieldHandle.setTarget(bean);
			setDerived();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public BT getBean() {
			return fieldHandle.getTarget();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getName() {
			return fieldHandle.getFieldName();
		}

		/**
		 * @return the {@link FieldHandle#getFieldType()}
		 */
		@SuppressWarnings("unchecked")
		public Class<T> getFieldType() {
			return (Class<T>) fieldHandle.getFieldType();
		}

		/**
		 * @return the {@link FieldHandle#getDeclaredFieldType()}
		 */
		public Class<?> getDeclaredFieldType() {
			return fieldHandle.getDeclaredFieldType();
		}

		/**
		 * @return the {@link FieldHandle#hasDefaultDerived()}
		 */
		protected boolean hasDefaultDerived() {
			return fieldHandle.hasDefaultDerived();
		}

		/**
		 * @return true when the {@link FieldProperty} is for a {@link List}
		 */
		public boolean isList() {
			return List.class.isAssignableFrom(this.fieldHandle.getFieldType());
		}

		/**
		 * @return true when the {@link FieldProperty} is for a {@link Set}
		 */
		public boolean isSet() {
			return Set.class.isAssignableFrom(this.fieldHandle.getFieldType());
		}

		/**
		 * @return true when the {@link FieldProperty} is for a {@link Map}
		 */
		public boolean isMap() {
			return Map.class.isAssignableFrom(this.fieldHandle.getFieldType());
		}

		/**
		 * @return true when the {@link FieldProperty} is bound to an
		 *         {@link ObservableList}
		 */
		protected boolean isObservableList() {
			return isObservableList(getCollectionObservable());
		}

		/**
		 * @return true when the {@link FieldProperty} is bound to an
		 *         {@link ObservableSet}
		 */
		protected boolean isObservableSet() {
			return isObservableSet(getCollectionObservable());
		}

		/**
		 * @return true when the {@link FieldProperty} is bound to an
		 *         {@link ObservableMap}
		 */
		protected boolean isObservableMap() {
			return isObservableMap(getCollectionObservable());
		}

		/**
		 * @param observable
		 *            the {@link Observable} to check
		 * @return true when the {@link Observable} is an {@link ObservableList}
		 */
		protected static boolean isObservableList(final Observable observable) {
			return observable != null
					&& ObservableList.class.isAssignableFrom(observable
							.getClass());
		}

		/**
		 * @param observable
		 *            the {@link Observable} to check
		 * @return true when the {@link Observable} is an {@link ObservableSet}
		 */
		protected static boolean isObservableSet(final Observable observable) {
			return observable != null
					&& ObservableSet.class.isAssignableFrom(observable
							.getClass());
		}

		/**
		 * @param observable
		 *            the {@link Observable} to check
		 * @return true when the {@link Observable} is an {@link ObservableMap}
		 */
		protected static boolean isObservableMap(final Observable observable) {
			return observable != null
					&& ObservableMap.class.isAssignableFrom(observable
							.getClass());
		}

		/**
		 * Extracts the collections {@link FieldProperty} from an associated
		 * {@link FieldBean}
		 * 
		 * @param fieldBean
		 *            the {@link FieldBean} to extract from
		 * @return the extracted {@link FieldProperty}
		 */
		protected FieldProperty<Object, ?, ?> extractCollectionItemFieldProperty(
				final FieldBean<Void, Object> fieldBean) {
			final String[] cip = collectionItemPath.split("\\.");
			return fieldBean.getFieldProperty(cip[cip.length - 1]);
		}

		/**
		 * @return true when each item in the underlying collection has a path
		 *         to it's own field value
		 */
		public boolean hasCollectionItemPath() {
			return this.collectionItemPath != null
					&& !this.collectionItemPath.isEmpty();
		}

		/**
		 * @return a <code>.</code> separated field name that represents each
		 *         item in the underlying collection
		 */
		public String getCollectionItemPath() {
			return this.collectionItemPath;
		}

		/**
		 * @return a {@link SelectionModel} for the {@link FieldProperty} when
		 *         the field references a collection/map for item selection or
		 *         {@code null} when not a selection {@link FieldProperty}
		 */
		protected SelectionModel<Object> getCollectionSelectionModel() {
			return collectionSelectionModel;
		}

		/**
		 * @return an {@link Observable} used to represent an
		 *         {@link ObservableList}, {@link ObservableSet}, or
		 *         {@link ObservableMap} (null when either the observable
		 *         collection has been garbage collected or the
		 *         {@link FieldProperty} does not represent a collection)
		 */
		protected Observable getCollectionObservable() {
			return this.collectionObservable.get();
		}

		/**
		 * @return true when the {@link FieldProperty} has an item master that
		 *         it's using to reference {@link #get()} for
		 */
		public boolean hasItemMaster() {
			return this.itemMaster != null;
		}
	}

	/**
	 * Field handle to {@link FieldHandle#getAccessor()} and
	 * {@link FieldHandle#getSetter()} for a given
	 * {@link FieldHandle#getTarget()}.
	 * 
	 * @param <T>
	 *            the {@link FieldHandle#getTarget()} type
	 * @param <F>
	 *            the {@link FieldHandle#getDeclaredFieldType()} type
	 */
	protected static class FieldHandle<T, F> {

		private static final Map<Class<?>, Class<?>> PRIMS = new HashMap<>();
		static {
			PRIMS.put(boolean.class, Boolean.class);
			PRIMS.put(char.class, Character.class);
			PRIMS.put(double.class, Double.class);
			PRIMS.put(float.class, Float.class);
			PRIMS.put(long.class, Long.class);
			PRIMS.put(int.class, Integer.class);
			PRIMS.put(short.class, Short.class);
			PRIMS.put(long.class, Long.class);
			PRIMS.put(byte.class, Byte.class);
		}
		private static final Map<Class<?>, Object> DFLTS = new HashMap<>();
		static {
			DFLTS.put(Boolean.class, Boolean.FALSE);
			DFLTS.put(boolean.class, false);
			DFLTS.put(Byte.class, Byte.valueOf("0"));
			DFLTS.put(byte.class, Byte.valueOf("0").byteValue());
			DFLTS.put(Number.class, 0L);
			DFLTS.put(Short.class, Short.valueOf("0"));
			DFLTS.put(short.class, Short.valueOf("0").shortValue());
			DFLTS.put(Character.class, Character.valueOf(' '));
			DFLTS.put(char.class, ' ');
			DFLTS.put(Integer.class, Integer.valueOf(0));
			DFLTS.put(int.class, 0);
			DFLTS.put(Long.class, Long.valueOf(0));
			DFLTS.put(long.class, 0L);
			DFLTS.put(Float.class, Float.valueOf(0F));
			DFLTS.put(float.class, 0F);
			DFLTS.put(Double.class, Double.valueOf(0D));
			DFLTS.put(double.class, 0D);
			DFLTS.put(BigInteger.class, BigInteger.valueOf(0L));
			DFLTS.put(BigDecimal.class, BigDecimal.valueOf(0D));
		}
		private final String fieldName;
		private MethodHandle accessor;
		private MethodHandle setter;
		private final Class<F> declaredFieldType;
		private T target;
		private boolean hasDefaultDerived;

		/**
		 * Constructor
		 * 
		 * @param target
		 *            the {@link #getTarget()} for the {@link MethodHandle}s
		 * @param fieldName
		 *            the field name defined in the {@link #getTarget()}
		 * @param declaredFieldType
		 *            the declared field type for the {@link #getFieldName()}
		 */
		protected FieldHandle(final T target, final String fieldName,
				final Class<F> declaredFieldType) {
			super();
			this.fieldName = fieldName;
			this.declaredFieldType = declaredFieldType;
			this.target = target;
			updateMethodHandles();
		}

		/**
		 * Updates the {@link #getAccessor()} and {@link #getSetter()} using the
		 * current {@link #getTarget()} and {@link #getFieldName()}.
		 * {@link MethodHandle}s are immutable so new ones are created.
		 */
		protected void updateMethodHandles() {
			this.accessor = buildAccessorWithLikelyPrefixes(getTarget(),
					getFieldName());
			this.setter = buildSetter(getAccessor(), getTarget(),
					getFieldName());
		}

		/**
		 * Gets the {@link #buildAccessorWithLikelyPrefixes(Object, String)}
		 * {@link MethodHandle#type()}
		 * 
		 * @param target
		 *            the accessor target
		 * @param fieldName
		 *            the field name of the target
		 * @return the accessor return type
		 */
		public static Class<?> getAccessorType(final Object target,
				final String fieldName) {
			return buildAccessorWithLikelyPrefixes(target, fieldName).type()
					.returnType();
		}

		/**
		 * Attempts to build a {@link MethodHandle} accessor for the field name
		 * using common prefixes used for methods to access a field
		 * 
		 * @param target
		 *            the target object that the accessor is for
		 * @param fieldName
		 *            the field name that the accessor is for
		 * @return the accessor {@link MethodHandle}
		 */
		protected static MethodHandle buildAccessorWithLikelyPrefixes(
				final Object target, final String fieldName) {
			final MethodHandle mh = buildAccessor(target, fieldName, "get",
					"is", "has", "use");
			if (mh == null) {
				// throw new NoSuchMethodException(fieldName + " on " + target);
				throw new IllegalArgumentException(fieldName + " on " + target);
			}
			return mh;
		}

		/**
		 * Attempts to build a {@link MethodHandle} accessor for the field name
		 * using common prefixes used for methods to access a field
		 * 
		 * @param target
		 *            the target object that the accessor is for
		 * @param fieldName
		 *            the field name that the accessor is for
		 * @return the accessor {@link MethodHandle}
		 * @param fieldNamePrefix
		 *            the prefix of the method for the field name
		 * @return the accessor {@link MethodHandle}
		 */
		protected static MethodHandle buildAccessor(final Object target,
				final String fieldName, final String... fieldNamePrefix) {
			final String accessorName = buildMethodName(fieldNamePrefix[0],
					fieldName);
			try {
				return MethodHandles
						.lookup()
						.findVirtual(
								target.getClass(),
								accessorName,
								MethodType.methodType(target.getClass()
										.getMethod(accessorName)
										.getReturnType())).bindTo(target);
			} catch (final NoSuchMethodException e) {
				return fieldNamePrefix.length <= 1 ? null : buildAccessor(
						target, fieldName, Arrays.copyOfRange(fieldNamePrefix,
								1, fieldNamePrefix.length));
			} catch (final Throwable t) {
				throw new IllegalArgumentException(
						"Unable to resolve accessor " + accessorName, t);
			}
		}

		/**
		 * Builds a setter {@link MethodHandle}
		 * 
		 * @param accessor
		 *            the field's accesssor that will be used as the parameter
		 *            type for the setter
		 * @param target
		 *            the target object that the setter is for
		 * @param fieldName
		 *            the field name that the setter is for
		 * @return the setter {@link MethodHandle}
		 */
		protected static MethodHandle buildSetter(final MethodHandle accessor,
				final Object target, final String fieldName) {
			try {
				final MethodHandle mh1 = MethodHandles
						.lookup()
						.findVirtual(
								target.getClass(),
								buildMethodName("set", fieldName),
								MethodType.methodType(void.class, accessor
										.type().returnType())).bindTo(target);
				return mh1;
			} catch (final Throwable t) {
				throw new IllegalArgumentException("Unable to resolve setter "
						+ fieldName, t);
			}
		}

		/**
		 * Attempts to invoke a <code>valueOf</code> using the
		 * {@link #getDeclaredFieldType()} class
		 * 
		 * @param value
		 *            the value to invoke the <code>valueOf</code> method on
		 * @return the result (null if the operation fails)
		 */
		public F valueOf(final String value) {
			return valueOf(getDeclaredFieldType(), value);
		}

		/**
		 * Attempts to invoke a <code>valueOf</code> using the specified class
		 * 
		 * @param valueOfClass
		 *            the class to attempt to invoke a <code>valueOf</code>
		 *            method on
		 * @param value
		 *            the value to invoke the <code>valueOf</code> method on
		 * @return the result (null if the operation fails)
		 */
		@SuppressWarnings("unchecked")
		public static <VT> VT valueOf(final Class<VT> valueOfClass,
				final Object value) {
			if (value != null && String.class.isAssignableFrom(valueOfClass)) {
				return (VT) value.toString();
			}
			final Class<?> clazz = PRIMS.containsKey(valueOfClass) ? PRIMS
					.get(valueOfClass) : valueOfClass;
			MethodHandle mh1 = null;
			try {
				mh1 = MethodHandles.lookup().findStatic(clazz, "valueOf",
						MethodType.methodType(clazz, String.class));
			} catch (final Throwable t) {
				// class doesn't support it- do nothing
			}
			if (mh1 != null) {
				try {
					return (VT) mh1.invoke(value);
				} catch (final Throwable t) {
					throw new IllegalArgumentException(String.format(
							"Unable to invoke valueOf on %1$s using %2$s",
							value, valueOfClass), t);
				}
			}
			return null;
		}

		/**
		 * Determines if a {@link Class} has a default value designated
		 * 
		 * @param clazz
		 *            the {@link Class} to check
		 * @return true when a {@link Class} has a default value designated for
		 *         it
		 */
		public static boolean hasDefault(final Class<?> clazz) {
			return clazz == null ? false : DFLTS.containsKey(clazz);
		}

		/**
		 * Gets a default value for the {@link #getDeclaredFieldType()}
		 * 
		 * @return the default value
		 */
		public F defaultValue() {
			return defaultValue(getDeclaredFieldType());
		}

		/**
		 * Gets a default value for the specified class
		 * 
		 * @param clazz
		 *            the class
		 * @return the default value
		 */
		@SuppressWarnings("unchecked")
		public static <VT> VT defaultValue(final Class<VT> clazz) {
			return (VT) (DFLTS.containsKey(clazz) ? DFLTS.get(clazz) : null);
		}

		/**
		 * Builds a method name using a prefix and a field name
		 * 
		 * @param prefix
		 *            the method's prefix
		 * @param fieldName
		 *            the method's field name
		 * @return the method name
		 */
		protected static String buildMethodName(final String prefix,
				final String fieldName) {
			return (fieldName.startsWith(prefix) ? fieldName : prefix
					+ fieldName.substring(0, 1).toUpperCase()
					+ fieldName.substring(1));
		}

		/**
		 * Sets the derived value from {@link #deriveValueFromAccessor()} using
		 * {@link #getSetter()}
		 * 
		 * @see #deriveValueFromAccessor()
		 * @return the accessor's return target value
		 */
		public F setDerivedValueFromAccessor() {
			F derived = null;
			try {
				derived = deriveValueFromAccessor();
				getSetter().invoke(derived);
			} catch (final Throwable t) {
				throw new RuntimeException(String.format(
						"Unable to set %1$s on %2$s", derived, getTarget()), t);
			}
			return derived;
		}

		/**
		 * Gets an accessor's return target value obtained by calling the
		 * accessor's {@link MethodHandle#invoke(Object...)} method. When the
		 * value returned is <code>null</code> an attempt will be made to
		 * instantiate it using either by using a default value from
		 * {@link #DFLTS} (for primatives) or {@link Class#newInstance()} on the
		 * accessor's {@link MethodType#returnType()} method.
		 * 
		 * @return the accessor's return target value
		 */
		@SuppressWarnings("unchecked")
		protected F deriveValueFromAccessor() {
			F targetValue = null;
			try {
				targetValue = (F) getAccessor().invoke();
			} catch (final Throwable t) {
				targetValue = null;
			}
			if (targetValue == null) {
				try {
					if (DFLTS.containsKey(getFieldType())) {
						targetValue = (F) DFLTS.get(getFieldType());
					} else {
						final Class<F> clazz = (Class<F>) getAccessor().type()
								.returnType();
						if (List.class.isAssignableFrom(clazz)) {
							targetValue = (F) new ArrayList<>();
						} else if (Set.class.isAssignableFrom(clazz)) {
							targetValue = (F) new LinkedHashSet<>();
						} else if (Map.class.isAssignableFrom(clazz)) {
							targetValue = (F) new HashMap<>();
						} else if (!Calendar.class
								.isAssignableFrom(getFieldType())
								&& !String.class
										.isAssignableFrom(getFieldType())) {
							targetValue = clazz.newInstance();
						}
					}
					hasDefaultDerived = true;
				} catch (final Exception e) {
					throw new IllegalArgumentException(
							String.format(
									"Unable to get accessor return instance for %1$s using %2$s.",
									getAccessor(), getAccessor().type()
											.returnType()));
				}
			} else {
				hasDefaultDerived = false;
			}
			return targetValue;
		}

		/**
		 * Binds a new target to the {@link FieldHandle}
		 * 
		 * @param target
		 *            the target to bind to
		 */
		public void setTarget(final T target) {
			if (getTarget().equals(target)) {
				return;
			}
			this.target = target;
			updateMethodHandles();
		}

		public T getTarget() {
			return target;
		}

		public String getFieldName() {
			return fieldName;
		}

		/**
		 * @return the getter
		 */
		protected MethodHandle getAccessor() {
			return accessor;
		}

		/**
		 * @return the setter
		 */
		protected MethodHandle getSetter() {
			return setter;
		}

		/**
		 * @return the declared field type of the property value
		 */
		public Class<F> getDeclaredFieldType() {
			return declaredFieldType;
		}

		/**
		 * @return the field type from {@link #getAccessor()} of the property
		 *         value
		 */
		public Class<?> getFieldType() {
			return getAccessor().type().returnType();
		}

		/**
		 * @return true if a default value has been derived
		 */
		public boolean hasDefaultDerived() {
			return hasDefaultDerived;
		}
	}
}