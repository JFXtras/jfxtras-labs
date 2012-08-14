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
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javafx.application.Platform;
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
import javafx.beans.property.StringProperty;
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
 * binds/un-binds it's fields to other {@linkplain Property} components. It
 * allows a <b><code>.</code></b> separated field path to be traversed on a bean
 * until the final field name is found (last entry in the <b><code>.</code></b>
 * separated field path). Each field will have a corresponding
 * {@linkplain Property} that is automatically generated and reused in the
 * binding process. Each {@linkplain Property} is bean-aware and will
 * dynamically update it's values and bindings as different beans are set on the
 * adapter. Bean's set on the adapter do not need to instantiate all the
 * sub-beans in the path(s) provided as long as they contain a no-argument
 * constructor they will be instantiated as path(s) are traversed.
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
 * person = new Person(); BeanPathAdapter<Person> personPA = new
 * BeanPathAdapter<>(person); ListView<String> lv = new ListView<>(); 
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
 * <b>{@linkplain Date}/{@linkplain Calendar} binding:</b>
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

	private FieldBean<Void, B> root;

	/**
	 * Constructor
	 * 
	 * @param bean
	 *            the bean the {@linkplain BeanPathAdapter} is for
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
	 * Binds a {@linkplain ObservableList} by traversing the bean's field tree.
	 * An additional item path can be specified when the path points to a
	 * {@linkplain Collection} that contains beans that also need traversed in
	 * order to establish the final value. For example: If a field path points
	 * to <code>phoneNumbers</code> (relative to the {@linkplain #getBean()})
	 * where <code>phoneNumbers</code> is a {@linkplain Collection} that
	 * contains <code>PhoneNumber</code> instances which in turn have a field
	 * called <code>areaCode</code> then an item path can be passed in addition
	 * to the field path with <code>areaCode</code> as it's value.
	 * 
	 * @param fieldPath
	 *            the <b><code>.</code></b> separated field paths relative to
	 *            the {@linkplain #getBean()} that will be traversed
	 * @param itemFieldPath
	 *            the <b><code>.</code></b> separated field paths relative to
	 *            each item in the bean's underlying {@linkplain Collection}
	 *            that will be traversed (empty/null when each item value does
	 *            not need traversed)
	 * @param itemFieldPathType
	 *            the {@linkplain Class} of that the item path points to
	 * @param list
	 *            the {@linkplain ObservableList} to bind to the field class
	 *            type of the property
	 * @param listValueType
	 *            the class type of the {@linkplain ObservableList} value
	 * @param selectionModel
	 *            the {@linkplain SelectionModel} used to set the values within
	 *            the {@linkplain ObservableList} <b>only applicable when the
	 *            {@linkplain ObservableList} is used for selection(s) and
	 *            therefore cannot be updated directly because it is
	 *            read-only</b>
	 * @param selectionModelItemMasterPath
	 *            when binding to {@linkplain SelectionModel} items, this will
	 *            be the optional path to the collection field that contains all
	 *            the items to select from
	 */
	public <E> void bindContentBidirectional(final String fieldPath,
			final String itemFieldPath, final Class<?> itemFieldPathType,
			final ObservableList<E> list, final Class<E> listValueType,
			final SelectionModel<E> selectionModel,
			final String selectionModelItemMasterPath) {
		FieldProperty<?, ?> itemMaster = null;
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
	 * Binds a {@linkplain ObservableSet} by traversing the bean's field tree.
	 * An additional item path can be specified when the path points to a
	 * {@linkplain Collection} that contains beans that also need traversed in
	 * order to establish the final value. For example: If a field path points
	 * to <code>phoneNumbers</code> (relative to the {@linkplain #getBean()})
	 * where <code>phoneNumbers</code> is a {@linkplain Collection} that
	 * contains <code>PhoneNumber</code> instances which in turn have a field
	 * called <code>areaCode</code> then an item path can be passed in addition
	 * to the field path with <code>areaCode</code> as it's value.
	 * 
	 * @param fieldPath
	 *            the <b><code>.</code></b> separated field paths relative to
	 *            the {@linkplain #getBean()} that will be traversed
	 * @param itemFieldPath
	 *            the <b><code>.</code></b> separated field paths relative to
	 *            each item in the bean's underlying {@linkplain Collection}
	 *            that will be traversed (empty/null when each item value does
	 *            not need traversed)
	 * @param itemFieldPathType
	 *            the {@linkplain Class} of that the item path points to
	 * @param set
	 *            the {@linkplain ObservableSet} to bind to the field class type
	 *            of the property
	 * @param setValueType
	 *            the class type of the {@linkplain ObservableSet} value
	 * @param selectionModel
	 *            the {@linkplain SelectionModel} used to set the values within
	 *            the {@linkplain ObservableSet} <b>only applicable when the
	 *            {@linkplain ObservableSet} is used for selection(s) and
	 *            therefore cannot be updated directly because it is
	 *            read-only</b>
	 * @param selectionModelItemMasterPath
	 *            when binding to {@linkplain SelectionModel} items, this will
	 *            be the optional path to the collection field that contains all
	 *            the items to select from
	 */
	public <E> void bindContentBidirectional(final String fieldPath,
			final String itemFieldPath, final Class<?> itemFieldPathType,
			final ObservableSet<E> set, final Class<E> setValueType,
			final SelectionModel<E> selectionModel,
			final String selectionModelItemMasterPath) {
		FieldProperty<?, ?> itemMaster = null;
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
	 * Binds a {@linkplain ObservableMap} by traversing the bean's field tree.
	 * An additional item path can be specified when the path points to a
	 * {@linkplain Collection} that contains beans that also need traversed in
	 * order to establish the final value. For example: If a field path points
	 * to <code>phoneNumbers</code> (relative to the {@linkplain #getBean()})
	 * where <code>phoneNumbers</code> is a {@linkplain Collection} that
	 * contains <code>PhoneNumber</code> instances which in turn have a field
	 * called <code>areaCode</code> then an item path can be passed in addition
	 * to the field path with <code>areaCode</code> as it's value.
	 * 
	 * @param fieldPath
	 *            the <b><code>.</code></b> separated field paths relative to
	 *            the {@linkplain #getBean()} that will be traversed
	 * @param itemFieldPath
	 *            the <b><code>.</code></b> separated field paths relative to
	 *            each item in the bean's underlying {@linkplain Collection}
	 *            that will be traversed (empty/null when each item value does
	 *            not need traversed)
	 * @param itemFieldPathType
	 *            the {@linkplain Class} of that the item path points to
	 * @param map
	 *            the {@linkplain ObservableMap} to bind to the field class type
	 *            of the property
	 * @param mapValueType
	 *            the class type of the {@linkplain ObservableMap} value
	 * @param selectionModel
	 *            the {@linkplain SelectionModel} used to set the values within
	 *            the {@linkplain ObservableMap} <b>only applicable when the
	 *            {@linkplain ObservableMap} is used for selection(s) and
	 *            therefore cannot be updated directly because it is
	 *            read-only</b>
	 * @param selectionModelItemMasterPath
	 *            when binding to {@linkplain SelectionModel} items, this will
	 *            be the optional path to the collection field that contains all
	 *            the items to select from
	 */
	public <K, V> void bindContentBidirectional(final String fieldPath,
			final String itemFieldPath, final Class<?> itemFieldPathType,
			final ObservableMap<K, V> map, final Class<V> mapValueType,
			final SelectionModel<V> selectionModel,
			final String selectionModelItemMasterPath) {
		FieldProperty<?, ?> itemMaster = null;
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
	 * Binds a {@linkplain Property} by traversing the bean's field tree
	 * 
	 * @param fieldPath
	 *            the <b><code>.</code></b> separated field paths relative to
	 *            the {@linkplain #getBean()} that will be traversed
	 * @param property
	 *            the {@linkplain Property} to bind to the field class type of
	 *            the property
	 * @param propertyType
	 *            the class type of the {@linkplain Property} value
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
	 * Unbinds a {@linkplain Property} by traversing the bean's field tree
	 * 
	 * @param fieldPath
	 *            the <b><code>.</code></b> separated field paths relative to
	 *            the {@linkplain #getBean()} that will be traversed
	 * @param property
	 *            the {@linkplain Property} to bind to the field class type of
	 *            the property
	 */
	public <T> void unBindBidirectional(final String fieldPath,
			final Property<T> property) {
		getRoot().performOperation(fieldPath, property, null,
				FieldBeanOperation.UNBIND);
	}

	/**
	 * @return the bean of the {@linkplain BeanPathAdapter}
	 */
	public B getBean() {
		return getRoot().getBean();
	}

	/**
	 * Sets the root bean of the {@linkplain BeanPathAdapter}. Any existing
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
			this.root = new FieldBean<>(null, bean, null);
		} else {
			getRoot().setBean(bean);
		}
	}

	/**
	 * @return the root/top level {@linkplain FieldBean}
	 */
	protected final FieldBean<Void, B> getRoot() {
		return this.root;
	}

	/**
	 * Provides the underlying value class for a given {@linkplain Property}
	 * 
	 * @param property
	 *            the {@linkplain Property} to check
	 * @return the value class of the {@linkplain Property}
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
	 * {@linkplain FieldBean} operations
	 */
	public static enum FieldBeanOperation {
		BIND, UNBIND, CREATE_OR_FIND;
	}

	/**
	 * A POJO bean extension that allows binding based upon a <b><code>.</code>
	 * </b> separated field path that will be traversed on a bean until the
	 * final field name is found. Each bean may contain child
	 * {@linkplain FieldBean}s when an operation is perfomed with a direct
	 * descendant field that is a non-primitive type. Any primitive types are
	 * added as a {@linkplain FieldProperty} reference to the
	 * {@linkplain FieldBean}.
	 * 
	 * @param <PT>
	 *            the parent bean type
	 * @param <BT>
	 *            the bean type
	 */
	protected static class FieldBean<PT, BT> implements Serializable {

		private static final long serialVersionUID = 7397535724568852021L;
		private final Map<String, FieldBean<BT, ?>> fieldBeans = new HashMap<>();
		private final Map<String, FieldProperty<BT, ?>> fieldProperties = new HashMap<>();
		private final Map<Class<?>, FieldStringConverter<?>> stringConverters = new HashMap<>();
		private FieldHandle<PT, BT> fieldHandle;
		private final FieldBean<?, PT> parent;
		private BT bean;

		/**
		 * Creates a {@linkplain FieldBean}
		 * 
		 * @param parent
		 *            the parent {@linkplain FieldBean} (should not be null)
		 * @param fieldHandle
		 *            the {@linkplain FieldHandle} (should not be null)
		 */
		protected FieldBean(final FieldBean<?, PT> parent,
				final FieldHandle<PT, BT> fieldHandle) {
			this.parent = parent;
			this.fieldHandle = fieldHandle;
			this.bean = this.fieldHandle.setDerivedValueFromAccessor();
			if (getParent() != null) {
				getParent().addFieldBean(this);
			}
		}

		/**
		 * Creates a {@linkplain FieldBean} with a generated
		 * {@linkplain FieldHandle} that targets the supplied bean and is
		 * projected on the parent {@linkplain FieldBean}. It assumes that the
		 * supplied {@linkplain FieldBean} has been set on the parent
		 * {@linkplain FieldBean}.
		 * 
		 * @see #createFieldHandle(Object, Object, String)
		 * @param parent
		 *            the parent {@linkplain FieldBean} (null when it's the
		 *            root)
		 * @param bean
		 *            the bean that the {@linkplain FieldBean} is for
		 * @param fieldName
		 *            the field name of the parent {@linkplain FieldBean} for
		 *            which the new {@linkplain FieldBean} is for
		 */
		protected FieldBean(final FieldBean<?, PT> parent, final BT bean,
				final String fieldName) {
			if (bean == null) {
				throw new NullPointerException("Bean cannot be null");
			}
			this.parent = parent;
			this.bean = bean;
			this.fieldHandle = getParent() != null ? createFieldHandle(
					getParent().getBean(), bean, fieldName) : null;
			if (getParent() != null) {
				getParent().addFieldBean(this);
			}
		}

		/**
		 * Generates a {@linkplain FieldHandle} that targets the supplied bean
		 * and is projected on the parent {@linkplain FieldBean} that has
		 * 
		 * @param parentBean
		 *            the parent bean
		 * @param bean
		 *            the child bean
		 * @param fieldName
		 *            the field name of the child within the parent
		 * @return the {@linkplain FieldHandle}
		 */
		@SuppressWarnings("unchecked")
		protected FieldHandle<PT, BT> createFieldHandle(final PT parentBean,
				final BT bean, final String fieldName) {
			return new FieldHandle<PT, BT>(parentBean, fieldName,
					(Class<BT>) getBean().getClass());
		}

		/**
		 * Adds a child {@linkplain FieldBean} if it doesn't already exist.
		 * NOTE: It does <b>NOT</b> ensure the child bean has been set on the
		 * parent.
		 * 
		 * @param fieldBean
		 *            the {@linkplain FieldBean} to add
		 */
		protected void addFieldBean(final FieldBean<BT, ?> fieldBean) {
			if (!getFieldBeans().containsKey(fieldBean.getFieldName())) {
				getFieldBeans().put(fieldBean.getFieldName(), fieldBean);
			}
		}

		/**
		 * Adds or updates a child {@linkplain FieldProperty}. When the child
		 * already exists it will {@linkplain FieldProperty#setTarget(Object)}
		 * using the bean of the {@linkplain FieldProperty}.
		 * 
		 * @param fieldProperty
		 *            the {@linkplain FieldProperty} to add or update
		 */
		protected void addOrUpdateFieldProperty(
				final FieldProperty<BT, ?> fieldProperty) {
			final String pkey = fieldProperty.getName();
			if (getFieldProperties().containsKey(pkey)) {
				getFieldProperties().get(pkey).setTarget(
						fieldProperty.getBean());
			} else {
				getFieldProperties().put(pkey, fieldProperty);
			}
		}

		/**
		 * @see #setParentBean(Object)
		 * @return the bean that the {@linkplain FieldBean} represents
		 */
		public BT getBean() {
			return bean;
		}

		/**
		 * Sets the bean of the {@linkplain FieldBean} and it's underlying
		 * {@linkplain #getFieldBeans()} and {@linkplain #getFieldProperties()}
		 * 
		 * @see #setParentBean(Object)
		 * @param bean
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
			for (final Map.Entry<String, FieldProperty<BT, ?>> fp : getFieldProperties()
					.entrySet()) {
				fp.getValue().setTarget(getBean());
			}
		}

		/**
		 * Binds a parent bean to the {@linkplain FieldBean} and it's underlying
		 * {@linkplain #getFieldBeans()} and {@linkplain #getFieldProperties()}
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
		 * @see BeanPathAdapter.FieldBean#performOperation(String, Class,
		 *      String, Observable, Class, SelectionModel, FieldProperty,
		 *      FieldBeanOperation)
		 */
		public <T> FieldProperty<?, ?> performOperation(final String fieldPath,
				final Property<T> property, final Class<T> propertyValueClass,
				final FieldBeanOperation operation) {
			return performOperation(fieldPath, propertyValueClass, null,
					(Observable) property, null, null, null, operation);
		}

		/**
		 * @see BeanPathAdapter.FieldBean#performOperation(String, Class,
		 *      String, Observable, Class, SelectionModel, FieldProperty,
		 *      FieldBeanOperation)
		 */
		public <T> FieldProperty<?, ?> performOperation(final String fieldPath,
				final ObservableList<T> observableList,
				final Class<T> listValueClass, final String collectionItemPath,
				final Class<?> collectionItemPathType,
				final SelectionModel<T> selectionModel,
				final FieldProperty<?, ?> itemMaster,
				final FieldBeanOperation operation) {
			return performOperation(fieldPath, listValueClass,
					collectionItemPath, (Observable) observableList,
					collectionItemPathType, selectionModel, itemMaster,
					operation);
		}

		/**
		 * @see BeanPathAdapter.FieldBean#performOperation(String, Class,
		 *      String, Observable, Class, SelectionModel, FieldProperty,
		 *      FieldBeanOperation)
		 */
		public <T> FieldProperty<?, ?> performOperation(final String fieldPath,
				final ObservableSet<T> observableSet,
				final Class<T> setValueClass, final String collectionItemPath,
				final Class<?> collectionItemPathType,
				final SelectionModel<T> selectionModel,
				final FieldProperty<?, ?> itemMaster,
				final FieldBeanOperation operation) {
			return performOperation(fieldPath, setValueClass,
					collectionItemPath, (Observable) observableSet,
					collectionItemPathType, selectionModel, itemMaster,
					operation);
		}

		/**
		 * @see BeanPathAdapter.FieldBean#performOperation(String, Class,
		 *      String, Observable, Class, SelectionModel, FieldProperty,
		 *      FieldBeanOperation)
		 */
		public <K, V> FieldProperty<?, ?> performOperation(
				final String fieldPath,
				final ObservableMap<K, V> observableMap,
				final Class<V> mapValueClass, final String collectionItemPath,
				final Class<?> collectionItemPathType,
				final SelectionModel<V> selectionModel,
				final FieldProperty<?, ?> itemMaster,
				final FieldBeanOperation operation) {
			return performOperation(fieldPath, mapValueClass,
					collectionItemPath, (Observable) observableMap,
					collectionItemPathType, selectionModel, itemMaster,
					operation);
		}

		/**
		 * Performs a {@linkplain FieldBeanOperation} by generating a
		 * {@linkplain FieldProperty} based upon the supplied <b><code>.</code>
		 * </b> separated path to the field by traversing the matching children
		 * of the {@linkplain FieldBean} until the corresponding
		 * {@linkplain FieldProperty} is found (target bean uses the POJO from
		 * {@linkplain FieldBean#getBean()}). If the operation is bind and the
		 * {@linkplain FieldProperty} doesn't exist all relative
		 * {@linkplain FieldBean}s in the path will be instantiated using a
		 * no-argument constructor until the {@linkplain FieldProperty} is
		 * created and bound to the supplied {@linkplain Property}. The process
		 * is reciprocated until all path {@linkplain FieldBean} and
		 * {@linkplain FieldProperty} attributes of the field path are
		 * extinguished.
		 * 
		 * @see Bindings#bindBidirectional(Property, Property)
		 * @see Bindings#unbindBidirectional(Property, Property)
		 * @param fieldPath
		 *            the <code>.</code> separated field names
		 * @param propertyValueClass
		 *            the class of the {@linkplain Property} value type (only
		 *            needed when binding)
		 * @param collectionItemPath
		 *            the the <code>.</code> separated field names of the
		 *            {@linkplain Observable} collection (only applicable when
		 *            the {@linkplain Observable} is a
		 *            {@linkplain ObservableList}, {@linkplain ObservableSet},
		 *            or {@linkplain ObservableMap})
		 * @param observable
		 *            the {@linkplain Property}, {@linkplain ObservableList},
		 *            {@linkplain ObservableSet}, or {@linkplain ObservableMap}
		 *            to perform the {@linkplain FieldBeanOperation} on
		 * @param collectionItemType
		 *            the {@linkplain Observable} {@linkplain Class} of each
		 *            item in the {@linkplain Observable} collection (only
		 *            applicable when the {@linkplain Observable} is a
		 *            {@linkplain ObservableList}, {@linkplain ObservableSet},
		 *            or {@linkplain ObservableMap})
		 * @param selectionModel
		 *            the {@linkplain SelectionModel} used to set the values
		 *            within the {@linkplain Observable} <b>only applicable when
		 *            the {@linkplain Observable} is used for selection(s) and
		 *            therefore cannot be updated directly because it is
		 *            read-only</b>
		 * @param itemMaster
		 *            the {@linkplain FieldProperty} that contains the item(s)
		 *            that the {@linkplain SelectionModel} can select from
		 * @param operation
		 *            the {@linkplain FieldBeanOperation}
		 * @return the {@linkplain FieldProperty} the operation was performed on
		 *         (null when the operation was not performed on any
		 *         {@linkplain FieldProperty}
		 */
		protected <T> FieldProperty<?, ?> performOperation(final String fieldPath,
				final Class<T> propertyValueClass,
				final String collectionItemPath, final Observable observable,
				final Class<?> collectionItemType,
				final SelectionModel<T> selectionModel,
				final FieldProperty<?, ?> itemMaster,
				final FieldBeanOperation operation) {
			final String[] fieldNames = fieldPath.split("\\.");
			final boolean isField = fieldNames.length == 1;
			final String pkey = isField ? fieldNames[0] : "";
			if (isField && getFieldProperties().containsKey(pkey)) {
				final FieldProperty<BT, ?> fp = getFieldProperties().get(pkey);
				performOperation(fp, observable, propertyValueClass, operation);
				return fp;
			} else if (!isField && getFieldBeans().containsKey(fieldNames[0])) {
				// progress to the next child field/bean in the path chain
				final String nextFieldPath = fieldPath.substring(fieldPath
						.indexOf(fieldNames[1]));
				return getFieldBeans().get(fieldNames[0]).performOperation(
						nextFieldPath, propertyValueClass, collectionItemPath,
						observable, collectionItemType, selectionModel,
						itemMaster, operation);
			} else if (operation != FieldBeanOperation.UNBIND) {
				// add a new bean/property chain
				if (isField) {
					final FieldProperty<BT, ?> childProp = new FieldProperty<>(
							getBean(), fieldNames[0], Object.class,
							collectionItemPath, observable, collectionItemType,
							selectionModel, itemMaster);
					addOrUpdateFieldProperty(childProp);
					return performOperation(fieldNames[0], propertyValueClass,
							collectionItemPath, observable, collectionItemType,
							selectionModel, itemMaster, operation);
				} else {
					// create a handle to set the bean as a child of the current
					// bean
					// if the child bean exists on the bean it will remain
					// unchanged
					final FieldHandle<BT, Object> pfh = new FieldHandle<>(
							getBean(), fieldNames[0], Object.class);
					final FieldBean<BT, ?> childBean = new FieldBean<>(this,
							pfh);
					// progress to the next child field/bean in the path chain
					final String nextFieldPath = fieldPath.substring(fieldPath
							.indexOf(fieldNames[1]));
					return childBean.performOperation(nextFieldPath,
							propertyValueClass, collectionItemPath, observable,
							collectionItemType, selectionModel, itemMaster,
							operation);
				}
			}
			return null;
		}

		/**
		 * Performs a {@linkplain FieldBeanOperation} on a
		 * {@linkplain FieldProperty} and an {@linkplain Observable}
		 * 
		 * @param fp
		 *            the {@linkplain FieldProperty}
		 * @param observable
		 *            the {@linkplain Property}, {@linkplain ObservableList},
		 *            {@linkplain ObservableSet}, or {@linkplain ObservableMap}
		 *            to perform the {@linkplain FieldBeanOperation} on
		 * @param observableValueClass
		 *            the {@linkplain Class} of the {@linkplain Observable}
		 *            value
		 * @param operation
		 *            the {@linkplain FieldBeanOperation}
		 */
		@SuppressWarnings("unchecked")
		protected <T> void performOperation(
				final FieldProperty<BT, ?> fp, final Observable observable,
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
					Bindings.bindBidirectional(
							(Property<String>) fp,
							(Property<T>) observable,
							(StringConverter<T>) getFieldStringConverter(observableValueClass));
				}
			} else if (fp.getObservableCollection() != null
					&& observable != null
					&& fp.getObservableCollection() != observable) {
				// handle scenario where multiple observable collections/maps
				// are being bound to the same field property
				if (operation == FieldBeanOperation.UNBIND) {
					Bindings.unbindContentBidirectional(
							fp.getObservableCollection(), observable);
				} else if (operation == FieldBeanOperation.BIND) {
					if (FieldProperty.isObservableList(observable)
							&& fp.isObservableList()) {
						Bindings.bindContentBidirectional(
								(ObservableList<Object>) observable,
								(ObservableList<Object>) fp
										.getObservableCollection());
					} else if (FieldProperty.isObservableSet(observable)
							&& fp.isObservableSet()) {
						Bindings.bindContentBidirectional(
								(ObservableSet<Object>) observable,
								(ObservableSet<Object>) fp
										.getObservableCollection());
					} else if (FieldProperty.isObservableMap(observable)
							&& fp.isObservableMap()) {
						Bindings.bindContentBidirectional(
								(ObservableMap<Object, Object>) observable,
								(ObservableMap<Object, Object>) fp
										.getObservableCollection());
					} else {
						throw new UnsupportedOperationException(
								String.format(
										"Incompatible observable collection/map types cannot be bound %1$s and %2$s",
										fp.getObservableCollection(),
										observable));
					}
				}
			} else if (operation == FieldBeanOperation.UNBIND) {
				fp.set(null);
			}
			// reset initial dirty value
			final Object currVal = fp.getDirty();
			if (val != null && !val.toString().isEmpty()
					&& !val.equals(currVal) && !fp.hasDefaultDerived()) {
				fp.setDirty(val);
			}
		}

		/**
		 * @return the field name that the {@linkplain FieldBean} represents in
		 *         it's parent (null when the {@linkplain FieldBean} is root)
		 */
		public String getFieldName() {
			return fieldHandle != null ? fieldHandle.getFieldName() : null;
		}

		/**
		 * Determines if the {@linkplain FieldBean} contains a field with the
		 * specified name
		 * 
		 * @param fieldName
		 *            the field name to check for
		 * @return true when the field exists
		 */
		public boolean hasField(final String fieldName) {
			return getFieldBeans().containsKey(fieldName)
					|| getFieldProperties().containsKey(
							getFieldProperties().get(fieldName));
		}

		/**
		 * @return the parent {@linkplain FieldBean} (null when the
		 *         {@linkplain FieldBean} is root)
		 */
		public FieldBean<?, PT> getParent() {
			return parent;
		}

		/**
		 * @see #getFieldProperties()
		 * @return the {@linkplain Map} of fields that belong to the
		 *         {@linkplain FieldBean} that are not a
		 *         {@linkplain FieldProperty}, but rather exist as a
		 *         {@linkplain FieldBean} that may or may not contain their own
		 *         {@linkplain FieldProperty} instances
		 */
		protected Map<String, FieldBean<BT, ?>> getFieldBeans() {
			return fieldBeans;
		}

		/**
		 * @see #getFieldBeans()
		 * @return the {@linkplain Map} of fields that belong to the
		 *         {@linkplain FieldBean} that are not {@linkplain FieldBean}s,
		 *         but rather exist as a {@linkplain FieldProperty}
		 */
		protected Map<String, FieldProperty<BT, ?>> getFieldProperties() {
			return fieldProperties;
		}

		/**
		 * @see #getFieldProperties()
		 * @return the {@linkplain FieldProperty} with the given name that
		 *         belongs to the {@linkplain FieldBean} (null when the name
		 *         does not exist)
		 */
		public FieldProperty<BT, ?> getFieldProperty(final String proptertyName) {
			if (getFieldProperties().containsKey(proptertyName)) {
				return getFieldProperties().get(proptertyName);
			}
			return null;
		}

		/**
		 * Gets/Creates (if not already created) a
		 * {@linkplain FieldStringConverter}.
		 * 
		 * @param targetClass
		 *            the target class of the {@linkplain FieldStringConverter}
		 * @return the {@linkplain FieldStringConverter}
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
	 * Coercible {@linkplain StringConverter} that handles conversions between
	 * strings and a target class when used in the binding process
	 * {@linkplain Bindings#bindBidirectional(Property, Property, StringConverter)}
	 * 
	 * @see StringConverter
	 * @param <T>
	 *            the target class type that is used in the coercion of the
	 *            string
	 */
	protected static class FieldStringConverter<T> extends StringConverter<T> {

		public static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz");
		private final Class<T> targetClass;

		/**
		 * Constructor
		 * 
		 * @param targetClass
		 *            the class that the {@linkplain FieldStringConverter} is
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
		 * Attempts to coerce a value into a {@linkplain String}
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
				final Date date = Date.class
						.isAssignableFrom(v.getClass()) ? (Date) v
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
			if (v == null || (!isStringType && v.toString().isEmpty())) {
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
	 * A {@linkplain Property} extension that uses a bean's getter/setter to
	 * define the {@linkplain Property}'s value.
	 * 
	 * @param <BT>
	 *            the bean type
	 * @param <T>
	 *            the field type
	 */
	protected static class FieldProperty<BT, T> extends
			ObjectPropertyBase<String> implements ListChangeListener<Object>,
			SetChangeListener<Object>, MapChangeListener<Object, Object> {

		private final FieldHandle<BT, T> fieldHandle;
		private boolean isDirty;
		private boolean isDirtyCollection;
		private boolean isCollectionListening;
		private final String collectionItemPath;
		private final WeakReference<Observable> collectionObservable;
		private final Class<?> collectionType;
		private final SelectionModel<Object> collectionSelectionModel;
		private final FieldProperty<?, ?> itemMaster;

		/**
		 * Constructor
		 * 
		 * @param bean
		 *            the bean that the path belongs to
		 * @param fieldName
		 *            the name of the field within the bean
		 * @param declaredFieldType
		 *            the declared {@linkplain Class} of the field
		 * @param collectionItemPath
		 *            the the <code>.</code> separated field names of the
		 *            {@linkplain Observable} collection (only applicable when
		 *            the {@linkplain Observable} is a
		 *            {@linkplain ObservableList}, {@linkplain ObservableSet},
		 *            or {@linkplain ObservableMap})
		 * @param collectionObservable
		 *            the {@linkplain Observable} {@linkplain Collection} used
		 *            to bind to the {@linkplain FieldProperty} <b>OR</b> when
		 *            the {@linkplain SelectionModel} is specified this is the
		 *            {@linkplain Observable} {@linkplain Collection} of
		 *            available items to select from
		 * @param collectionType
		 *            the {@linkplain Collection} {@linkplain Class} used to
		 *            attempt to transform the underlying field
		 *            {@linkplain Observable} {@linkplain Collection} to the
		 *            {@linkplain Collection} {@linkplain Class} (only
		 *            applicable when the actual field is a
		 *            {@linkplain Collection})
		 * @param collectionSelectionModel
		 *            the {@linkplain SelectionModel} used to set the values
		 *            within the {@linkplain Observable} <b>only applicable when
		 *            the {@linkplain Observable} is used for selection(s) and
		 *            therefore cannot be updated directly because it is
		 *            read-only</b>
		 * @param itemMaster
		 *            the {@linkplain FieldProperty} that contains the item(s)
		 *            that the {@linkplain SelectionModel} can select from
		 */
		@SuppressWarnings("unchecked")
		protected FieldProperty(final BT bean, final String fieldName,
				final Class<T> declaredFieldType,
				final String collectionItemPath,
				final Observable collectionObservable,
				final Class<?> collectionType,
				final SelectionModel<?> collectionSelectionModel,
				final FieldProperty<?, ?> itemMaster) {
			super();
			this.fieldHandle = new FieldHandle<BT, T>(bean, fieldName,
					declaredFieldType);
			this.itemMaster = itemMaster;
			this.collectionObservable = new WeakReference<Observable>(collectionObservable);
			this.collectionItemPath = collectionItemPath;
			this.collectionType = collectionType;
			this.collectionSelectionModel = (SelectionModel<Object>) collectionSelectionModel;
			setDerived();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String get() {
			try {
				final Object dv = getDirty();
				return FieldStringConverter.coerceToString(dv);
			} catch (final Throwable t) {
				throw new RuntimeException("Unable to get value", t);
			}
		}

		/**
		 * Sets the {@link FieldHandle#deriveValueFromAccessor()} value
		 */
		protected void setDerived() {
			final T derived = fieldHandle.deriveValueFromAccessor();
			setObject(derived);
		}

		/**
		 * Flags the {@linkplain Property} value as dirty and calls
		 * {@linkplain #set(String)}
		 * 
		 * @param v
		 *            the value to set
		 */
		public void setDirty(final Object v) {
			isDirty = true;
			setObject(v);
		}

		/**
		 * Sets an {@linkplain Object} value
		 * 
		 * @param v
		 *            the value to set
		 */
		private void setObject(final Object v) {
			try {
				if (v != null
						&& (Collection.class.isAssignableFrom(v.getClass()) || Map.class
								.isAssignableFrom(v.getClass()))) {
					fieldHandle.getSetter().invoke(v);
					postSet();
				} else if (v != null
						&& (Calendar.class.isAssignableFrom(v.getClass()) || Date.class
								.isAssignableFrom(v.getClass()))) {
					final Object cv = fieldHandle.getAccessor().invoke();
					if (cv != v) {
						final Object val = FieldStringConverter.coerce(
								v,
								cv != null ? cv.getClass() : fieldHandle
										.getFieldType());
						fieldHandle.getSetter().invoke(val);
						postSet();
					}
				} else {
					set(v != null ? v.toString() : null);
				}
			} catch (final Throwable t) {
				throw new IllegalArgumentException(String.format(
						"Unable to set object value: %1$s on %2$s", v,
						fieldHandle.getFieldName()), t);
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void set(final String v) {
			try {
				// final MethodHandle mh2 = MethodHandles.insertArguments(
				// fieldHandle.getSetter(), 0, v);
				final Object cv = fieldHandle.getAccessor().invoke();
				if (!isDirty && v == cv) {
					return;
				}
				final Object val = FieldStringConverter
						.coerce(v,
								cv != null ? cv.getClass() : fieldHandle
										.getFieldType());
				fieldHandle.getSetter().invoke(val);
				postSet();
			} catch (final Throwable t) {
				throw new IllegalArgumentException("Unable to set value: " + v,
						t);
			}
		};

		/**
		 * Executes any post processing that needs to take place after set
		 * operation takes place
		 * 
		 * @throws Throwable
		 *             thrown when any errors occur when processing a post set
		 *             operation
		 */
		protected final void postSet() throws Throwable {
			populateObservableCollection();
			invalidated();
			fireValueChangedEvent();
			isDirty = false;
		}

		/**
		 * Updates the {@linkplain Observable} when the field represents a
		 * supported {@linkplain Collection}. If the
		 * {@linkplain #collectionType} is defined an attempt will be made to
		 * transform the {@linkplain Observable} {@linkplain Collection} to it.
		 * 
		 * @throws Throwable
		 *             thrown when {@linkplain FieldHandle#getSetter()} cannot
		 *             be invoked, the {@linkplain #getDirty()} cannot be cast
		 *             to {@linkplain FieldHandle#getFieldType()}, or the
		 *             {@linkplain #getDirty()} cannot be transformed using the
		 *             {@linkplain #collectionType}
		 */
		private void populateObservableCollection() throws Throwable {
			if (isList() || isSet()) {
				addRemoveCollectionListener(false);
				Collection<?> items = (Collection<?>) getDirty();
				if (items == null) {
					items = new LinkedHashSet<>();
					fieldHandle.getSetter().invoke(items);
				}
				syncCollectionValues(items, false);
			} else if (isMap()) {
				addRemoveCollectionListener(false);
				Map<?, ?> items = (Map<?, ?>) getDirty();
				if (items == null) {
					items = new HashMap<>();
					fieldHandle.getSetter().invoke(items);
				}
				syncCollectionValues(items, false);
			} else {
				return;
			}
			addRemoveCollectionListener(true);
		}

		/**
		 * Synchronizes the collection items used in the
		 * {@linkplain FieldProperty} and {@linkplain Observable} collection.
		 * {@linkplain Bindings#bindContentBidirectional(ObservableList, ObservableList)}
		 * variants are/cannot be not used.
		 * 
		 * @param values
		 *            the {@linkplain List}, {@linkplain Set}, or
		 *            {@linkplain Map} that should be synchronized
		 * @param toField
		 *            true when synchronization needs to occur on the
		 *            {@linkplain FieldProperty} collection, false when
		 *            synchronization needs to occur on the
		 *            {@linkplain Observable} collection
		 */
		@SuppressWarnings("unchecked")
		private void syncCollectionValues(final Object values,
				final boolean toField) {
			if (isDirtyCollection) {
				return;
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
							syncCollectionValuesFromObservable(col, oc);
						} else {
							final boolean wasColEmpty = col.isEmpty();
							if (collectionSelectionModel == null
									&& (!wasColEmpty || isDirty)) {
								oc.clear();
							} else if (collectionSelectionModel == null) {
								syncCollectionValuesFromObservable(col, oc);
							}
							if (!wasColEmpty) {
								syncObservableFromCollectionValues(col, oc);
							}
						}
					} else if (Map.class.isAssignableFrom(values.getClass())) {
						final Map<Object, Object> map = (Map<Object, Object>) values;
						if (toField) {
							syncCollectionValuesFromObservable(map, oc);
						} else {
							final boolean wasColEmpty = map.isEmpty();
							if (collectionSelectionModel == null
									&& (!wasColEmpty || isDirty)) {
								oc.clear();
							} else if (collectionSelectionModel == null) {
								syncCollectionValuesFromObservable(map, oc);
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
							syncCollectionValuesFromObservable(col, oc);
						} else {
							final boolean wasColEmpty = col.isEmpty();
							if (collectionSelectionModel == null
									&& (!wasColEmpty || isDirty)) {
								oc.clear();
							} else if (collectionSelectionModel == null) {
								syncCollectionValuesFromObservable(col, oc);
							}
							if (!wasColEmpty) {
								syncObservableFromCollectionValues(col, oc);
							}
						}
					} else if (Map.class.isAssignableFrom(values.getClass())) {
						final Map<Object, Object> map = (Map<Object, Object>) values;
						if (toField) {
							syncCollectionValuesFromObservable(map, oc);
						} else {
							final boolean wasColEmpty = map.isEmpty();
							if (collectionSelectionModel == null
									&& (!wasColEmpty || isDirty)) {
								oc.clear();
							} else if (collectionSelectionModel == null) {
								syncCollectionValuesFromObservable(map, oc);
							}
							if (!wasColEmpty) {
								syncObservableFromCollectionValues(map, oc);
							}
						}
					}
				}
			} finally {
				isDirtyCollection = false;
			}
		}

		/**
		 * Synchronizes the {@linkplain Collection} values to the supplied
		 * {@linkplain Observable} {@linkplain Collection}
		 * 
		 * @param fromCol
		 *            the {@linkplain Collection} that synchronization will
		 *            derive from
		 * @param oc
		 *            the {@linkplain Observable} {@linkplain Collection} that
		 *            should be synchronized to
		 */
		private void syncObservableFromCollectionValues(
				final Collection<Object> fromCol, final Collection<Object> oc) {
			FieldProperty<?, ?> fp;
			Object fpv;
			int i = -1;
			final boolean isOcList = List.class.isAssignableFrom(oc.getClass());
			for (final Object item : fromCol) {
				fp = updateCollectionItemBean(++i, item, null);
				fpv = fp != null ? fp.getDirty() : item;
				if (collectionSelectionModel == null) {
					if (isOcList) {
						((List<Object>) oc).add(i, fpv);
					} else {
						oc.add(fpv);
					}
				} else {
					// call later to give items a chance to update
					final Object fpvt = fpv;
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							collectionSelectionModel.select(fpvt);
						}
					});
				}
			}
		}

		/**
		 * Synchronizes the {@linkplain Collection} values to the supplied
		 * {@linkplain Observable} {@linkplain Map}
		 * 
		 * @param fromCol
		 *            the {@linkplain Collection} that synchronization will
		 *            derive from
		 * @param oc
		 *            the {@linkplain Observable} {@linkplain Map} that should
		 *            be synchronized to
		 */
		private void syncObservableFromCollectionValues(
				final Collection<Object> fromCol, final Map<Object, Object> oc) {
			FieldProperty<?, ?> fp;
			Object fpv;
			int i = -1;
			for (final Object item : fromCol) {
				fp = updateCollectionItemBean(++i, item, null);
				fpv = fp != null ? fp.getDirty() : item;
				if (collectionSelectionModel == null) {
					oc.put(i, fpv);
				} else {
					// call later to give items a chance to update
					final Object fpvt = fpv;
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							collectionSelectionModel.select(fpvt);
						}
					});
				}
			}
		}

		/**
		 * Synchronizes the {@linkplain Map} values to the supplied
		 * {@linkplain Observable} {@linkplain Collection}
		 * 
		 * @param fromMap
		 *            the {@linkplain Map} that synchronization will derive from
		 * @param oc
		 *            the {@linkplain Observable} {@linkplain Collection} that
		 *            should be synchronized to
		 */
		private void syncObservableFromCollectionValues(
				final Map<Object, Object> fromMap, final Collection<Object> oc) {
			FieldProperty<?, ?> fp;
			Object fpv;
			int i = -1;
			final boolean isOcList = List.class.isAssignableFrom(oc.getClass());
			for (final Object item : fromMap.values()) {
				fp = updateCollectionItemBean(++i, item, null);
				fpv = fp != null ? fp.getDirty() : item;
				if (collectionSelectionModel == null) {
					if (isOcList) {
						((List<Object>) oc).add(i, fpv);
					} else {
						oc.add(fpv);
					}
				} else {
					// call later to give items a chance to update
					final Object fpvt = fpv;
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							collectionSelectionModel.select(fpvt);
						}
					});
				}
			}
		}

		/**
		 * Synchronizes the {@linkplain Map} values to the supplied
		 * {@linkplain Observable} {@linkplain Map}
		 * 
		 * @param fromMap
		 *            the {@linkplain Map} that synchronization will derive from
		 * @param oc
		 *            the {@linkplain Observable} {@linkplain Map} that should
		 *            be synchronized to
		 */
		private void syncObservableFromCollectionValues(
				final Map<Object, Object> fromMap, final Map<Object, Object> oc) {
			FieldProperty<?, ?> fp;
			Object fpv;
			int i = -1;
			for (final Map.Entry<Object, Object> item : fromMap.entrySet()) {
				fp = updateCollectionItemBean(++i, item, null);
				fpv = fp != null ? fp.getDirty() : item;
				if (collectionSelectionModel == null) {
					oc.put(i, fpv);
				} else {
					// call later to give items a chance to update
					final Object fpvt = fpv;
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							collectionSelectionModel.select(fpvt);
						}
					});
				}
			}
		}

		/**
		 * Synchronizes the {@linkplain Observable} {@linkplain Collection}
		 * values to the supplied {@linkplain Collection}
		 * 
		 * @param toCol
		 *            the {@linkplain Collection} that should be synchronized to
		 * @param oc
		 *            the {@linkplain Observable} {@linkplain Collection} that
		 *            synchronization will derive from
		 */
		private void syncCollectionValuesFromObservable(
				final Collection<Object> toCol, final Collection<Object> oc) {
			Object fpv;
			int i = -1;
			final List<Object> nc = new ArrayList<>();
			for (final Object item : oc) {
				fpv = updateCollectionItemProperty(++i, item);
				nc.add(fpv);
			}
			toCol.clear();
			toCol.addAll(nc);
		}

		/**
		 * Synchronizes the {@linkplain Observable} {@linkplain Collection}
		 * values to the supplied {@linkplain Map}
		 * 
		 * @param toMap
		 *            the {@linkplain Map} that should be synchronized to
		 * @param oc
		 *            the {@linkplain Observable} {@linkplain Map} that
		 *            synchronization will derive from
		 */
		private void syncCollectionValuesFromObservable(
				final Map<Object, Object> toMap,
				final Collection<Object> oc) {
			Object fpv;
			int i = -1;
			final Map<Object, Object> nc = new HashMap<>();
			for (final Object item : oc) {
				fpv = updateCollectionItemProperty(++i, item);
				nc.put(i, fpv);
			}
			toMap.clear();
			toMap.putAll(nc);
		}

		/**
		 * Synchronizes the {@linkplain Observable} {@linkplain Map} values to
		 * the supplied {@linkplain Collection}
		 * 
		 * @param toCol
		 *            the {@linkplain Collection} that should be synchronized to
		 * @param oc
		 *            the {@linkplain Observable} {@linkplain Map} that
		 *            synchronization will derive from
		 */
		private void syncCollectionValuesFromObservable(
				final Collection<Object> toCol,
				final ObservableMap<Object, Object> oc) {
			Object fpv;
			int i = -1;
			final List<Object> nc = new ArrayList<>();
			for (final Map.Entry<Object, Object> item : oc.entrySet()) {
				fpv = updateCollectionItemProperty(++i, item.getValue());
				nc.add(fpv);
			}
			toCol.clear();
			toCol.addAll(nc);
		}

		/**
		 * Synchronizes the {@linkplain Observable} {@linkplain Map} values to
		 * the supplied {@linkplain Map}
		 * 
		 * @param toMap
		 *            the {@linkplain Map} that should be synchronized to
		 * @param oc
		 *            the {@linkplain Observable} {@linkplain Collection} that
		 *            synchronization will derive from
		 */
		private void syncCollectionValuesFromObservable(
				final Map<Object, Object> toMap,
				final ObservableMap<Object, Object> oc) {
			Object fpv;
			int i = -1;
			final Map<Object, Object> nc = new HashMap<>();
			for (final Map.Entry<Object, Object> item : oc.entrySet()) {
				fpv = updateCollectionItemProperty(++i, item.getValue());
				nc.put(i, fpv);
			}
			toMap.clear();
			toMap.putAll(nc);
		}

		/**
		 * Creates/Updates a collection item value of the
		 * {@linkplain FieldProperty} for the
		 * {@linkplain #getCollectionItemPath()}
		 * 
		 * @param index
		 *            the index of the collection item to update
		 * @param itemBeanValue
		 *            the collection {@linkplain FieldBean} value to add/update.
		 *            when {@code null} the existing {@linkplain FieldBean}
		 *            value will will be used unless it is {@code null} as well-
		 *            in which case an attempt will be made to instantiate a new
		 *            instance of the bean using a no-argument constructor
		 * @param itemBeanPropertyValue
		 *            the collection {@linkplain FieldBean}'s
		 *            {@linkplain FieldProperty} value to add/update (null when
		 *            no update should be made to the {@linkplain FieldBean}'s
		 *            {@linkplain FieldProperty} value)
		 * @return the {@linkplain FieldProperty} for the collection item (null
		 *         when none is required)
		 */
		protected FieldProperty<?, ?> updateCollectionItemBean(
				final int index, final Object itemBeanValue,
				final Object itemBeanPropertyValue) {
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
				// TODO : Prevent unneeded instantiation of beans when checking
				// for existing references 
				Object value = itemBeanPropertyValue;
				Object bean = itemBeanValue == null ? collectionType
						.newInstance() : itemBeanValue;
				FieldProperty<?, ?> fp = genCollectionFieldProperty(bean);
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
					FieldProperty<?, ?> imfp;
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
		 * Generates a {@linkplain FieldProperty} for a
		 * {@linkplain #getCollectionItemPath()} and
		 * {@linkplain #getCollectionSelectionModel()} when applicable
		 * 
		 * @param bean
		 *            the bean to generate a {@linkplain FieldProperty}
		 * @return the generated {@linkplain FieldProperty}
		 */
		protected FieldProperty<?, ?> genCollectionFieldProperty(final Object bean) {
			FieldBean<Void, Object> fb;
			FieldProperty<?, ?> fp;
			fb = new FieldBean<>(null, bean, null);
			fp = fb.performOperation(collectionItemPath, Object.class, null,
					null, null, collectionSelectionModel, null,
					FieldBeanOperation.CREATE_OR_FIND);
			return fp;
		}

		/**
		 * Updates the underlying collection item value
		 * 
		 * @see #updateCollectionItemBean(int, Object, Object)
		 * @param index
		 *            the index of the collection item to update
		 * @param itemBeanPropertyValue
		 *            the collection {@linkplain FieldBean}'s
		 *            {@linkplain FieldProperty} value to add/update
		 * @return {@linkplain FieldProperty#getBean()} when the collection item
		 *         has it's own bean path, the <code>
		 *         itemBeanPropertyValue</code> when it does not
		 */
		protected Object updateCollectionItemProperty(final int index,
				final Object itemBeanPropertyValue) {
			final FieldProperty<?, ?> fp = updateCollectionItemBean(index,
					null, itemBeanPropertyValue);
			return fp == null ? itemBeanPropertyValue : fp.getBean();
		}

		/**
		 * Adds/Removes the {@linkplain FieldProperty} as a collection listener
		 * 
		 * @param add
		 *            true to add, false to remove
		 */
		protected void addRemoveCollectionListener(final boolean add) {
			if ((this.isCollectionListening && add)
					|| (this.isCollectionListening && !add)) {
				return;
			}
			if (this.collectionObservable.get() instanceof ObservableList) {
				final ObservableList<?> ol = ((ObservableList<?>) this.collectionObservable
						.get());
				if (add) {
					ol.addListener(this);
					this.isCollectionListening = true;
				} else {
					ol.removeListener(this);
					this.isCollectionListening = false;
				}
			} else if (this.collectionObservable.get() instanceof ObservableSet) {
				final ObservableSet<?> os = ((ObservableSet<?>) this.collectionObservable
						.get());
				if (add) {
					os.addListener(this);
					this.isCollectionListening = true;
				} else {
					os.removeListener(this);
					this.isCollectionListening = false;
				}
			} else if (this.collectionObservable.get() instanceof ObservableMap) {
				final ObservableMap<?, ?> om = ((ObservableMap<?, ?>) this.collectionObservable
						.get());
				if (add) {
					om.addListener(this);
					this.isCollectionListening = true;
				} else {
					om.removeListener(this);
					this.isCollectionListening = false;
				}
			} else if (this.collectionObservable.get() == null) {
				throw new IllegalStateException(String.format(
						"Observable collection/map bound to %1$s (item path: %2$s) "
								+ "has been garbage collected",
						this.fieldHandle.getFieldName(),
						this.collectionItemPath,
						this.collectionObservable.get(), this.getFieldType()));
			} else {
				throw new UnsupportedOperationException(String.format(
						"%1$s (item path: %2$s) of type \"%4$s\" "
								+ "must be bound to a supported "
								+ "observable collection/map type... "
								+ "Found observable: %3$s",
						this.fieldHandle.getFieldName(),
						this.collectionItemPath,
						this.collectionObservable.get(), this.getFieldType()));
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public final void onChanged(
				MapChangeListener.Change<? extends Object, ? extends Object> change) {
			syncCollectionValues(getDirty(), true);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public final void onChanged(
				SetChangeListener.Change<? extends Object> change) {
			syncCollectionValues(getDirty(), true);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public final void onChanged(
				ListChangeListener.Change<? extends Object> change) {
			syncCollectionValues(getDirty(), true);
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
		 * Binds a new target to the {@linkplain FieldHandle}
		 * 
		 * @param bean
		 *            the target bean to bind to
		 */
		public void setTarget(final BT bean) {
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
		 * @return the {@linkplain FieldHandle#getFieldType()}
		 */
		@SuppressWarnings("unchecked")
		public Class<T> getFieldType() {
			return (Class<T>) fieldHandle.getFieldType();
		}

		/**
		 * @return the {@linkplain FieldHandle#hasDefaultDerived()}
		 */
		public boolean hasDefaultDerived() {
			return fieldHandle.hasDefaultDerived();
		}

		/**
		 * @return true when the {@linkplain FieldProperty} is for a
		 *         {@linkplain List}
		 */
		public boolean isList() {
			return List.class.isAssignableFrom(this.fieldHandle.getFieldType());
		}

		/**
		 * @return true when the {@linkplain FieldProperty} is for a
		 *         {@linkplain Set}
		 */
		public boolean isSet() {
			return Set.class.isAssignableFrom(this.fieldHandle.getFieldType());
		}

		/**
		 * @return true when the {@linkplain FieldProperty} is for a
		 *         {@linkplain Map}
		 */
		public boolean isMap() {
			return Map.class.isAssignableFrom(this.fieldHandle.getFieldType());
		}

		/**
		 * @return true when the {@linkplain FieldProperty} is bound to an
		 *         {@linkplain ObservableList}
		 */
		public boolean isObservableList() {
			return isObservableList(getObservableCollection());
		}

		/**
		 * @return true when the {@linkplain FieldProperty} is bound to an
		 *         {@linkplain ObservableSet}
		 */
		public boolean isObservableSet() {
			return isObservableSet(getObservableCollection());
		}

		/**
		 * @return true when the {@linkplain FieldProperty} is bound to an
		 *         {@linkplain ObservableMap}
		 */
		public boolean isObservableMap() {
			return isObservableMap(getObservableCollection());
		}

		/**
		 * @param observable
		 *            the {@linkplain Observable} to check
		 * @return true when the {@linkplain Observable} is an
		 *         {@linkplain ObservableList}
		 */
		public static boolean isObservableList(final Observable observable) {
			return observable != null
					&& ObservableList.class.isAssignableFrom(observable
							.getClass());
		}

		/**
		 * @param observable
		 *            the {@linkplain Observable} to check
		 * @return true when the {@linkplain Observable} is an
		 *         {@linkplain ObservableSet}
		 */
		public static boolean isObservableSet(final Observable observable) {
			return observable != null
					&& ObservableSet.class.isAssignableFrom(observable
							.getClass());
		}

		/**
		 * @param observable
		 *            the {@linkplain Observable} to check
		 * @return true when the {@linkplain Observable} is an
		 *         {@linkplain ObservableMap}
		 */
		public static boolean isObservableMap(final Observable observable) {
			return observable != null
					&& ObservableMap.class.isAssignableFrom(observable
							.getClass());
		}

		/**
		 * Extracts the collections {@linkplain FieldProperty} from an
		 * associated {@linkplain FieldBean}
		 * 
		 * @param fieldBean
		 *            the {@linkplain FieldBean} to extract from
		 * @return the extracted {@linkplain FieldProperty}
		 */
		protected FieldProperty<Object, ?> extractCollectionItemFieldProperty(
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
		 * @return a {@linkplain SelectionModel} for the
		 *         {@linkplain FieldProperty} when the field references a
		 *         collection/map for item selection or {@code null} when not a
		 *         selection {@linkplain FieldProperty}
		 */
		public SelectionModel<Object> getCollectionSelectionModel() {
			return collectionSelectionModel;
		}

		/**
		 * @return an {@linkplain Observable} used to represent an
		 *         {@linkplain ObservableList}, {@linkplain ObservableSet}, or
		 *         {@linkplain ObservableMap} (null when either the observable
		 *         collection has been garbage collected or the
		 *         {@linkplain FieldProperty} does not represent a collection)
		 */
		public Observable getObservableCollection() {
			return this.collectionObservable.get();
		}
	}

	/**
	 * Field handle to {@linkplain FieldHandle#getAccessor()} and
	 * {@linkplain FieldHandle#getSetter()} for a given
	 * {@linkplain FieldHandle#getTarget()}.
	 * 
	 * @param <T>
	 *            the {@linkplain FieldHandle#getTarget()} type
	 * @param <F>
	 *            the {@linkplain FieldHandle#getDeclaredFieldType()} type
	 */
	protected static class FieldHandle<T, F> {

		private static final Map<Class<?>, MethodHandle> VALUE_OF_MAP = new HashMap<>(
				1);
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
		 *            the {@linkplain #getTarget()} for the
		 *            {@linkplain MethodHandle}s
		 * @param fieldName
		 *            the field name defined in the {@linkplain #getTarget()}
		 * @param declaredFieldType
		 *            the declared field type for the
		 *            {@linkplain #getFieldName()}
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
		 * Updates the {@linkplain #getAccessor()} and {@linkplain #getSetter()}
		 * using the current {@linkplain #getTarget()} and
		 * {@linkplain #getFieldName()}. {@linkplain MethodHandle}s are
		 * immutable so new ones are created.
		 */
		protected void updateMethodHandles() {
			this.accessor = buildAccessorWithLikelyPrefixes(getTarget(),
					getFieldName());
			this.setter = buildSetter(getAccessor(), getTarget(),
					getFieldName());
		}

		/**
		 * Attempts to build a {@linkplain MethodHandle} accessor for the field
		 * name using common prefixes used for methods to access a field
		 * 
		 * @param target
		 *            the target object that the accessor is for
		 * @param fieldName
		 *            the field name that the accessor is for
		 * @return the accessor {@linkplain MethodHandle}
		 * @throws NoSuchMethodException
		 *             thrown when an accessor cannot be found for the field
		 */
		protected static MethodHandle buildAccessorWithLikelyPrefixes(
				final Object target, final String fieldName) {
			final MethodHandle mh = buildAccessor(target, fieldName, "get",
					"is", "has");
			if (mh == null) {
				// throw new NoSuchMethodException(fieldName + " on " + target);
				throw new IllegalArgumentException(fieldName + " on " + target);
			}
			return mh;
		}

		/**
		 * Attempts to build a {@linkplain MethodHandle} accessor for the field
		 * name using common prefixes used for methods to access a field
		 * 
		 * @param target
		 *            the target object that the accessor is for
		 * @param fieldName
		 *            the field name that the accessor is for
		 * @return the accessor {@linkplain MethodHandle}
		 * @param fieldNamePrefix
		 *            the prefix of the method for the field name
		 * @return the accessor {@linkplain MethodHandle}
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
		 * Builds a setter {@linkplain MethodHandle}
		 * 
		 * @param accessor
		 *            the field's accesssor that will be used as the parameter
		 *            type for the setter
		 * @param target
		 *            the target object that the setter is for
		 * @param fieldName
		 *            the field name that the setter is for
		 * @return the setter {@linkplain MethodHandle}
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
		 * Puts a <code>valueOf</code> {@linkplain MethodHandle} value using the
		 * target class as a key
		 * 
		 * @param target
		 *            the target object that the <code>valueOf</code> is for
		 */
		protected static void putValueOf(final Class<?> target) {
			if (VALUE_OF_MAP.containsKey(target)) {
				return;
			}
			try {
				final MethodHandle mh1 = MethodHandles.lookup().findStatic(
						target, "valueOf",
						MethodType.methodType(target, String.class));
				VALUE_OF_MAP.put(target, mh1);
			} catch (final Throwable t) {
				// class doesn't support it- do nothing
			}
		}

		/**
		 * Attempts to invoke a <code>valueOf</code> using the
		 * {@linkplain #getDeclaredFieldType()} class
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
			if (!VALUE_OF_MAP.containsKey(valueOfClass)) {
				putValueOf(valueOfClass);
			}
			if (VALUE_OF_MAP.containsKey(valueOfClass)) {
				try {
					return (VT) VALUE_OF_MAP.get(valueOfClass).invoke(value);
				} catch (final Throwable t) {
					throw new IllegalArgumentException(String.format(
							"Unable to invoke valueOf on %1$s using %2$s",
							value, valueOfClass), t);
				}
			}
			return null;
		}

		/**
		 * Gets a default value for the {@linkplain #getDeclaredFieldType()}
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
		 * Sets the derived value from {@linkplain #deriveValueFromAccessor()}
		 * using {@linkplain #getSetter()}
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
		 * accessor's {@linkplain MethodHandle#invoke(Object...)} method. When
		 * the value returned is <code>null</code> an attempt will be made to
		 * instantiate it using either by using a default value from
		 * {@linkplain #DFLTS} (for primatives) or
		 * {@linkplain Class#newInstance()} on the accessor's
		 * {@linkplain MethodType#returnType()} method.
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
						} else if (!Calendar.class.isAssignableFrom(getFieldType())) {
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
		 * Binds a new target to the {@linkplain FieldHandle}
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
		 * @return the field type from {@linkplain #getAccessor()} of the
		 *         property value
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