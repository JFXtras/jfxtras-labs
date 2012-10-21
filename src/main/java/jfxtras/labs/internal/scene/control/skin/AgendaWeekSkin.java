/**
 * Copyright (c) 2011, JFXtras
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *	 * Redistributions of source code must retain the above copyright
 *	   notice, this list of conditions and the following disclaimer.
 *	 * Redistributions in binary form must reproduce the above copyright
 *	   notice, this list of conditions and the following disclaimer in the
 *	   documentation and/or other materials provided with the distribution.
 *	 * Neither the name of the <organization> nor the
 *	   names of its contributors may be used to endorse or promote products
 *	   derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
// TODO: refactor the layout code in showMenu
// TODO: manual edit of all properties, location, description
// TODO: concept of tasks; appointment with only a start date (not whole day). Rendered as a line. 
// TODO: dropping an area event in the header and then back into the day; take the location of the drop into account as the start time (instead of the last start time)
// TODO: drop a wholeday in the day, drag it down so it spans two days, drag it in the header again, drag it back to the day again: height = 0 
// TODO: allow dragging on day spanning events on the not-the-first areas
// TODO: undo feature on all actions (remove, add, ...)
// TODO: should we use an intermediate appointment in the popup and have an ok button which copies all data (vs calling the setters directly)?
// TODO: single day view
// TODO: reminders?
// TODO: callbacks to check if a delete is ok, etc
// TODO: repeating appointments; is that something we want to do or are we letting the provider of the appointments handle that?
package jfxtras.labs.internal.scene.control.skin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.ScrollPaneBuilder;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import jfxtras.labs.animation.Timer;
import jfxtras.labs.internal.scene.control.behavior.AgendaBehavior;
import jfxtras.labs.scene.control.Agenda;
import jfxtras.labs.scene.control.Agenda.Appointment;
import jfxtras.labs.scene.control.CalendarTextField;
import jfxtras.labs.util.NodeUtil;

import com.sun.javafx.scene.control.skin.SkinBase;

/**
 * @author Tom Eugelink
 */
public class AgendaWeekSkin extends SkinBase<Agenda, AgendaBehavior>
{
	// ==================================================================================================================
	// CONSTRUCTOR
	
	/**
	 * 
	 */
	public AgendaWeekSkin(Agenda control)
	{
		super(control, new AgendaBehavior(control));
		construct();
	}

	/*
	 * construct the component
	 */
	private void construct()
	{	
		// setup component
		createNodes();

		// react to changes in the locale 
		getSkinnable().localeProperty().addListener(new InvalidationListener() 
		{
			@Override
			public void invalidated(Observable observable)
			{
				refreshLocale();
			} 
		});
		refreshLocale();
		
		// react to changes in the displayed calendar 
		getSkinnable().displayedCalendar().addListener(new InvalidationListener()
		{			
			@Override
			public void invalidated(Observable observable)
			{
				assignCalendarToTheDayPanes();
				setupAppointments();
			}
		});
		assignCalendarToTheDayPanes();
		
		// react to changes in the appointments 
		getSkinnable().appointments().addListener(new ListChangeListener<Agenda.Appointment>() 
		{
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends Appointment> arg0)
			{
				setupAppointments();
			} 
		});
		setupAppointments();
		
		// react to changes in the appointments 
		getSkinnable().selectedAppointments().addListener(new ListChangeListener<Agenda.Appointment>() 
		{
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends Appointment> changes)
			{
				setOrRemoveSelected();
			} 
		});
		setOrRemoveSelected();
	}
	
	/**
	 * set or remove the Selected class from the appointments
	 */
	private void setOrRemoveSelected()
	{
		// update the styleclass
		for (DayPane lDayPane : weekPane.dayPanes)
		{
			for (AbstractAppointmentPane lAppointmentPane : lDayPane.allAbstractAppointmentPanes())
			{
				// remove 
				if ( lAppointmentPane.getStyleClass().contains("Selected") == true
				  && getSkinnable().selectedAppointments().contains(lAppointmentPane.appointment) == false
				   )
				{
					lAppointmentPane.getStyleClass().remove("Selected");
				}
				// add
				if ( lAppointmentPane.getStyleClass().contains("Selected") == false
				  && getSkinnable().selectedAppointments().contains(lAppointmentPane.appointment) == true
				   )
				{
					lAppointmentPane.getStyleClass().add("Selected");
				}
			}
		}		
	}
	
	/**
	 * Assign a calendar to each day, so it knows what it must draw.
	 */
	private void assignCalendarToTheDayPanes()
	{
		// get the first day of week calendar
		Calendar lCalendar = getFirstDayOfWeekCalendar();
		Calendar lStartCalendar = (Calendar)lCalendar.clone();
		
		// assign it to each day pane
		Calendar lEndCalendar = null;
		for (int i = 0; i < 7; i++)
		{
			weekPane.dayPanes.get(i).calendarObjectProperty.set( (Calendar)lCalendar.clone() );
			if (i== 6) lEndCalendar = (Calendar)lCalendar.clone();
			lCalendar.add(Calendar.DATE, 1);
		}		
		
		// place the now line
		nowUpdateRunnable.run(); 
		
		// tell the skin what range is displayed, so it can update the appointments
		if (getSkinnable().getCalendarRangeCallback() != null)
		{
			Agenda.CalendarRange lCalendarRange = new Agenda.CalendarRange(lStartCalendar, lEndCalendar);
			getSkinnable().getCalendarRangeCallback().call(lCalendarRange);
		}
	}
	
	/**
	 * 
	 */
	private void refreshLocale()
	{
		// create the formatter to use
		dayOfWeekDateFormat = new SimpleDateFormat("E", getSkinnable().getLocale());
		dateFormat = (SimpleDateFormat)SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT, getSkinnable().getLocale());
		
		// force redraw the dayHeaders by reassigning the calendar
		for (DayPane lDay : weekPane.dayPanes)
		{
			if (lDay.calendarObjectProperty.get() != null)
			{
				lDay.calendarObjectProperty.set( (Calendar)lDay.calendarObjectProperty.get().clone() );
			}
		}
	}
	private SimpleDateFormat dayOfWeekDateFormat = null;
	private SimpleDateFormat dateFormat = null;
	final static private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

	/**
	 * Have all days reconstruct the appointments
	 */
	private void setupAppointments()
	{
		calculateSizes();
		for (DayPane lDay : weekPane.dayPanes)
		{
			lDay.setupAppointments();
		}
		calculateSizes(); // TODO: when dropping a wholeday appointment into another day header, the header height is not increased unless this call is present. Figure out why.
		nowUpdateRunnable.run(); // set the history
	}

	
	// ==================================================================================================================
	// DRAW
	
	/**
	 * construct the nodes
	 */
	private void createNodes()
	{
		// we use a borderpane
		borderPane = new BorderPane();
		
		// center
		weekPane = new WeekPane();
		weekScrollPane = ScrollPaneBuilder.create()
			.content(weekPane)
			.hbarPolicy(ScrollBarPolicy.NEVER)
			.pannable(false) // panning would conflict with creating a new appointment
			.build();
		borderPane.setCenter(weekScrollPane);
		// bind the size of the week to the scrollpane's viewport
		weekScrollPane.viewportBoundsProperty().addListener(new InvalidationListener()
		{
			@Override
			public void invalidated(Observable viewportBoundsProperty)
			{
				calculateSizes();
				nowUpdateRunnable.run();
			}
		});
		
		// top: header have to be created after the week, because there is a binding to days
		weekHeaderPane = new WeekHeaderPane();
		weekHeaderPane.setTranslateX(1); // correct for the scrollpane
		borderPane.setTop(weekHeaderPane);
		
		// create a transparent pane where dragging an appointment is visualized 
		dragPane = new Pane();
		dragPane.prefWidthProperty().bind(widthProperty()); // the drag pane is the same size as the whole skin
		dragPane.prefHeightProperty().bind(heightProperty());
		// the borderpane is placed in the drag pane and sized to match 
		dragPane.getChildren().add(borderPane);
		borderPane.prefWidthProperty().bind(dragPane.widthProperty());
		borderPane.prefHeightProperty().bind(dragPane.heightProperty());
		
		// add to self
		this.getStyleClass().add(this.getClass().getSimpleName()); // always add self as style class, because CSS should relate to the skin not the control		
		getChildren().add(dragPane);
		
		// load the close icon
		closeIconImage = new Image(this.getClass().getResourceAsStream(this.getClass().getSimpleName() + "PopupCloseWindowIcon.png"));
	}
	private Pane dragPane = null;
	private BorderPane borderPane = null;
	private WeekHeaderPane weekHeaderPane = null;
	private ScrollPane weekScrollPane = null;
	private WeekPane weekPane = null;
	private Image closeIconImage = null;

	// ==================================================================================================================
	// PANES
	
	/**
	 * Responsible for rendering the day headers
	 */
	class WeekHeaderPane extends Pane
	{
		/**
		 * 
		 */
		public WeekHeaderPane()
		{
			// 7 days per week
			for (int i = 0; i < 7; i++)
			{
				DayHeaderPane lDayHeader = new DayHeaderPane(weekPane.dayPanes.get(i)); // associate with a day, so we can use its administration. This needs only be done once
				lDayHeader.layoutXProperty().bind(weekPane.dayPanes.get(i).layoutXProperty());			
				lDayHeader.layoutYProperty().set(0);
				lDayHeader.prefWidthProperty().bind(weekPane.dayPanes.get(i).prefWidthProperty());			
				lDayHeader.prefHeightProperty().bind(heightProperty());			
				getChildren().add(lDayHeader);
				dayHeaderPanes.add(lDayHeader);
			}
		}
		final List<DayHeaderPane> dayHeaderPanes = new ArrayList<DayHeaderPane>();
	}

	/**
	 * Responsible for rendering the day header (whole day appointments).
	 * This class is connected to the daypane and uses its data.
	 */
	class DayHeaderPane extends Pane
	{
		public DayHeaderPane(DayPane dayPane)
		{
			// for debugging setStyle("-fx-border-color:PINK;-fx-border-width:4px;");
			getStyleClass().add("DayHeader");
			
			// link up the day and day header panes
			this.dayPane = dayPane;
			dayPane.dayHeaderPane = this; // two way link
			
			// set label
			calendarText = new Text("?");
			double lX = (dayWidthProperty.get() - calendarText.prefWidth(0)) / 2;
			calendarText.setX( padding ); // align left
			calendarText.setY( calendarText.prefHeight(0) );
			Rectangle lClip = new Rectangle(0,0,0,0);
			lClip.widthProperty().bind(widthProperty().subtract(padding));
			lClip.heightProperty().bind(heightProperty());
			calendarText.setClip(lClip);
			getChildren().add(calendarText);
			// react to changes in the calendar by updating the label
			dayPane.calendarObjectProperty.addListener(new InvalidationListener()
			{
				@Override
				public void invalidated(Observable arg0)
				{
					String lLabel = DayHeaderPane.this.dayPane.calendarObjectProperty.get() == null ? "" : dayOfWeekDateFormat.format(DayHeaderPane.this.dayPane.calendarObjectProperty.get().getTime()) + " " + dateFormat.format(DayHeaderPane.this.dayPane.calendarObjectProperty.get().getTime());
					calendarText.setText(lLabel);
				}
			});
			
			// change the layout related to the size
			widthProperty().addListener(new InvalidationListener()
			{
				@Override
				public void invalidated(Observable arg0)
				{
					relayout();
				}
			});
			heightProperty().addListener(new InvalidationListener()
			{
				@Override
				public void invalidated(Observable arg0)
				{
					relayout();
				}
			});
			
			// layout
			relayout();
		}
		DayPane dayPane = null;
		Text calendarText = null;
		
		/**
		 * 
		 */
		public void relayout()
		{
			// create headers
			int lOffset = highestNumberOfWholedayAppointmentsProperty.get() - appointmentHeaderPanes.size(); // to make sure the appointments are renders aligned bottom
			for (AppointmentHeaderPane lAppointmentHeaderPane : appointmentHeaderPanes)
			{
				int lIdx = appointmentHeaderPanes.indexOf(lAppointmentHeaderPane);
				lAppointmentHeaderPane.setLayoutX(lIdx * wholedayAppointmentWidth); // each pane is cascade offset to the right to allow connecting to the wholeday appointment on the day pane 
				lAppointmentHeaderPane.setLayoutY( titleCalendarHeightProperty.get() + ((lIdx + lOffset) * wholedayTitleHeightProperty.get()) ); // each pane is cascaded offset down so the title label is visible 
				lAppointmentHeaderPane.setPrefSize(dayWidthProperty.get() - (lIdx * wholedayAppointmentWidth), (appointmentHeaderPanes.size() - lIdx) * wholedayTitleHeightProperty.get()); // make sure the size matches the cascading
			}
		}
		
		/**
		 * 
		 */
		public void setupAppointments()
		{
			// remove all appointments and create new ones
			getChildren().removeAll(appointmentHeaderPanes);
			appointmentHeaderPanes.clear();
			// for all wholeday appointments on the day pane, create a header appointment pane as well
			for (WholedayAppointmentPane lAppointmentPane : dayPane.wholedayAppointmentPanes)  
			{
				AppointmentHeaderPane lAppointmentHeaderPane = new AppointmentHeaderPane(lAppointmentPane.appointment); 
				getChildren().add(lAppointmentHeaderPane);				
				appointmentHeaderPanes.add(lAppointmentHeaderPane);	
			}
			
			// and layout
			relayout();
		}
		final List<AppointmentHeaderPane> appointmentHeaderPanes = new ArrayList<AgendaWeekSkin.AppointmentHeaderPane>();		
	}
	
	/**
	 * Responsible for rendering a single whole day appointment on a day header.
	 * 
	 */
	class AppointmentHeaderPane extends AbstractAppointmentPane
	{
		/**
		 * 
		 * @param calendar
		 * @param appointment
		 */
		public AppointmentHeaderPane(Agenda.Appointment appointment)
		{
			super(appointment);
			
			// for debugging setStyle("-fx-border-color:GREEN;-fx-border-width:4px;");
			getStyleClass().add("Appointment");
			getStyleClass().add(appointment.getAppointmentGroup().getStyleClass());

			// add a text node
			Text lSummaryText = new Text(appointment.getSummary());
			lSummaryText.getStyleClass().add("AppointmentLabel");
			lSummaryText.setX( padding );
			lSummaryText.setY( textHeight1MProperty.get() );
			Rectangle lClip = new Rectangle(0,0,0,0);
			lClip.widthProperty().bind(widthProperty().subtract(padding));
			lClip.heightProperty().bind(heightProperty());
			lSummaryText.setClip(lClip);
			getChildren().add(lSummaryText);			
			
			// add the menu header
			getChildren().add(menuIcon);
			
			// historical visualizer
			historicalVisualizer = new HistoricalVisualizer(this);
			getChildren().add(historicalVisualizer);
		}
		final Rectangle historicalVisualizer;
	}
	
	/**
	 * Responsible for rendering the days
	 */
	class WeekPane extends Pane
	{
		final List<DayPane> dayPanes = new ArrayList<DayPane>();

		/**
		 * 
		 */
		public WeekPane()
		{
			getStyleClass().add("WeekPane");
			
			// draw hours
			for (int lHour = 0; lHour < 24; lHour++)
			{
				// hour line
				{
					Line l = new Line(0,10,100,10);
					l.getStyleClass().add("HourLine");
					l.startXProperty().set(0.0);
					l.startYProperty().bind(hourHeighProperty.multiply(lHour));
					l.endXProperty().bind(widthProperty());
					l.endYProperty().bind(l.startYProperty());
					getChildren().add(l);
				}
				// half hour line
				{
					Line l = new Line(0,10,100,10);
					l.getStyleClass().add("HalfHourLine");
					l.startXProperty().bind(timeWidthProperty);
					l.endXProperty().bind(widthProperty());
					l.startYProperty().bind(hourHeighProperty.multiply(lHour + 0.5));
					l.endYProperty().bind(l.startYProperty());
					getChildren().add(l);
				}
				// hour text
				{
					Text t = new Text(lHour + ":00");
					t.xProperty().bind(timeWidthProperty.subtract(t.getBoundsInParent().getWidth()));
					t.yProperty().bind(hourHeighProperty.multiply(lHour));
					t.setTranslateY(t.getBoundsInParent().getHeight()); // move it under the line
					t.getStyleClass().add("HourLabel");
					t.setFontSmoothingType(FontSmoothingType.LCD);
					getChildren().add(t);
				}
			}

			// 7 days per week
			for (int i = 0; i < 7; i++)
			{
				DayPane lDay = new DayPane();
				lDay.layoutXProperty().bind(dayWidthProperty.multiply(i).add(dayFirstColumnXProperty));
				lDay.layoutYProperty().set(0.0);
				lDay.prefWidthProperty().bind(dayWidthProperty);
				lDay.prefHeightProperty().bind(dayHeightProperty);
				getChildren().add(lDay);
				
				// remember
				dayPanes.add(lDay);
			}
		}
	}
	
	
	/**
	 * Responsible for rendering the appointments within a day 
	 */
	class DayPane extends Pane
	{
		// this daypane is representing this calendar (date)
		ObjectProperty<Calendar> calendarObjectProperty = new SimpleObjectProperty<Calendar>(DayPane.this, "calendar");
		
		// know your header
		DayHeaderPane dayHeaderPane = null;
		
		public DayPane()
		{
			// for debugging setStyle("-fx-border-color:PINK;-fx-border-width:4px;");		
			getStyleClass().add("Day");
			
			// the appointments layout is too complex to cleanly calculate using binding, so we'll listen to size changes
			widthProperty().addListener(new InvalidationListener()
			{
				@Override
				public void invalidated(Observable arg0)
				{
					relayout();
				}
			});
			heightProperty().addListener(new InvalidationListener()
			{
				@Override
				public void invalidated(Observable arg0)
				{
					relayout();
				}
			});
			
			// ---------
			
			// start new appointment
			setOnMousePressed(new EventHandler<MouseEvent>()
			{
				@Override
				public void handle(MouseEvent mouseEvent)
				{
					// if there is no one to handle the result, don't eve bother
					if (getSkinnable().createAppointmentCallbackProperty().get() == null) return;
					
					// show the rectangle
					DayPane.this.setCursor(Cursor.V_RESIZE);
					double lY = mouseEvent.getScreenY() - NodeUtil.screenY(DayPane.this);
					resizeRectangle = new Rectangle(0, lY, dayWidthProperty.get(), 10);
					resizeRectangle.getStyleClass().add("GhostRectangle");
					DayPane.this.getChildren().add(resizeRectangle);
					
					// this event should not be processed by the appointment area
					mouseEvent.consume();
					dragged = false;
					getSkinnable().selectedAppointments().clear();
				}
			});
			// visualize resize
			setOnMouseDragged(new EventHandler<MouseEvent>()
			{
				@Override
				public void handle(MouseEvent mouseEvent)
				{
					if (resizeRectangle == null) return;
					
					// - calculate the number of pixels from onscreen nodeY (layoutY) to onscreen mouseY					
					double lHeight = mouseEvent.getScreenY() - NodeUtil.screenY(resizeRectangle);
					if (lHeight < 5) lHeight = 5;
					resizeRectangle.setHeight(lHeight);
					
					// no one else
					mouseEvent.consume();
					dragged = true;
				}
			});
			// end resize
			setOnMouseReleased(new EventHandler<MouseEvent>()
			{
				@Override
				public void handle(MouseEvent mouseEvent)
				{
					if (resizeRectangle == null) return;
					
					// no one else
					mouseEvent.consume();
					
					// reset ui
					DayPane.this.setCursor(Cursor.HAND);
					DayPane.this.getChildren().remove(resizeRectangle);
					
					// must have dragged (otherwise it is considered an "unselect all" action)
					if (dragged == false) return;
					
					// calculate the starttime
					Calendar lStartCalendar = setTimeTo0000((Calendar)DayPane.this.calendarObjectProperty.get().clone());
					lStartCalendar.add(Calendar.MILLISECOND, (int)(resizeRectangle.getY() * durationInMSPerPixelProperty.get()));
					setTimeToNearestMinutes(lStartCalendar, 5);
					
					// calculate the new end date for the appointment (recalculating the duration)
					Calendar lEndCalendar = (Calendar)lStartCalendar.clone();					
					lEndCalendar.add(Calendar.MILLISECOND, (int)(resizeRectangle.getHeight() * durationInMSPerPixelProperty.get()));
					setTimeToNearestMinutes(lEndCalendar, 5);
					
					// clean up
					resizeRectangle = null;					
					
					// ask the control to create a new appointment (null may be returned)
					Agenda.Appointment lAppointment = getSkinnable().createAppointmentCallbackProperty().get().call(new Agenda.CalendarRange(lStartCalendar, lEndCalendar));
					if (lAppointment != null) 
					{
						getSkinnable().appointments().add(lAppointment); // the appointments collection is listened to, so they will automatically be refreshed
					}
				}
			});
		}
		Rectangle resizeRectangle = null;
		boolean dragged = false;
		
		/**
		 * 
		 * @return
		 */
		public List<AbstractAppointmentPane> allAbstractAppointmentPanes()
		{
			List<AbstractAppointmentPane> lPanes = new ArrayList<AgendaWeekSkin.AbstractAppointmentPane>(regularAppointmentPanes);
			lPanes.addAll(wholedayAppointmentPanes);
			lPanes.addAll(dayHeaderPane.appointmentHeaderPanes);
			return lPanes;
		}
		
		/**
		 * 
		 */
		private void relayout()
		{
			// first add all the whole day appointments
			int lWholedayCnt = 0;
			for (WholedayAppointmentPane lAppointmentPane : wholedayAppointmentPanes)
			{
				lAppointmentPane.setLayoutX(lWholedayCnt * wholedayAppointmentWidth);
				lAppointmentPane.setLayoutY(0);
				lAppointmentPane.setPrefSize(wholedayAppointmentWidth, dayHeightProperty.get());
				lWholedayCnt++;
			}
			
			// then add all regular appointments to the day
			// calculate how much room is remaining for the regular appointments
			double lRemainingWidthForRegularAppointments = dayContentWidthProperty.get() - (lWholedayCnt * wholedayAppointmentWidth);			
			for (RegularAppointmentPane lAppointmentPane : regularAppointmentPanes)
			{
				// the X is determine by offsetting the wholeday appointments and then calculate the X of the track the appointment is placed in (available width / number of tracks) 
				lAppointmentPane.setLayoutX((lWholedayCnt * wholedayAppointmentWidth) + (lRemainingWidthForRegularAppointments / lAppointmentPane.clusterOwner.clusterTracks.size() * lAppointmentPane.clusterTrackIdx));
				
				// the Y is determined by the start time in minutes projected onto the total day height (being 24 hours)
				int lTimeFactor = (lAppointmentPane.start.get(Calendar.HOUR_OF_DAY) * 60) + lAppointmentPane.start.get(Calendar.MINUTE);
				lAppointmentPane.setLayoutY(dayHeightProperty.get() / (24 * 60) * lTimeFactor );
				
				// the width is the remaining width (subtracting the wholeday appointments) divided by the number of tracks in the cluster
				double lW = (dayContentWidthProperty.get() - (wholedayAppointmentPanes.size() * wholedayAppointmentWidth)) * (1.0 / (((double)lAppointmentPane.clusterOwner.clusterTracks.size())));
				// all but the most right appointment get 50% extra width, so they underlap the next track 
				if (lAppointmentPane.clusterTrackIdx < lAppointmentPane.clusterOwner.clusterTracks.size() - 1) lW *= 1.5;
				lAppointmentPane.setPrefWidth(lW);
				
				// the height is determing by the duration projected against the total dayHeight (being 24 hours)
				double lH = (dayHeightProperty.get() / (24 * 60) * (lAppointmentPane.durationInMS / 1000 / 60) );
				// the height has a minimum size, in order to be able to render sensibly
				if (lH < 2 * padding) lH = 2 * padding; 
				lAppointmentPane.setPrefHeight(lH);
			}
		}			

		/**
		 * This method prepares a day for being drawn.
		 * The appointments within one day might overlap, this method will create a data structure so it is clear how these overlapping appointments should be drawn.
		 * All appointments in one day are process based on their start time; earliest first, and if there are more with the same start time, longest duration first.
		 * The appointments are then place onto (parallel) tracks; an appointment initially is placed in track 0. 
		 * But if there is already an (partially overlapping) appointment there, then the appointment is moved to track 1. 
		 * Unless there also is an appointment already in that track 1, then the next track is tried, and so forth, until a free track is found.
		 * For example (the letters are not the sequence in which the appointments are processed, they're just for identifying them):
		 * 
		 *  tracks
		 *  0 1 2 3
		 *  -------
		 *  . . . .
		 *  . . . .
		 *  A . . .
		 *  A B C .
		 *  A B C D
		 *  A B . D
		 *  A . . D
		 *  A E . D
		 *  A . . D
		 *  . . . D
		 *  . . . D
		 *  F . . D
		 *  F H . D 
		 *  . . . .
		 *  G . . . 
		 *  . . . .
		 * 
		 * Appointment A was rendered first and put into track 0 and its start time.
		 * Then appointment B was added, initially it was put in track 0, but appointment A already uses the that slot, so B was moved into track 1.
		 * C moved from track 0, conflicting with A, to track 1, conflicting with B, and ended up in track 2. And so forth.
		 * F and H show that even though D overlaps them, they could perfectly be placed in lower tracks.
		 * 
		 * A cluster of appointments always starts with a free standing appointment in track 0, for example A or G, such appointment is called the cluster owner.
		 * When the next appointment is added to the tracks, and finds that it cannot be put in track 0, it will be added as a member to the cluster represented by the appointment in track 0.
		 * Special attention must be paid to an appointment that is placed in track 0, but is linked to a cluster by a earlier appointment in a higher track; such an appointment is not the cluster owner.
		 * In the example above, F is linked through D to the cluster owned by A. So F is not a cluster owner, but a member of the cluster owned by A.
		 * And appointment H through F is also part of the cluster owned by A.  
		 * G finally starts a new cluster.
		 * The cluster owner knows all members and how many tracks there are, each member knows in what track it is and has a direct link to the cluster owner. 
		 *  
		 * When rendering the appointments above, parallel appointments are rendered narrower & indented, so appointments partially overlap and the left side of an appointment is always visible to the user.
		 * In the example above the single appointment G is rendered full width, while for example A, B, C and D are overlapping.
		 * F and H are drawn in the same dimensions as A and B in order to allow D to overlap then.
		 * The size and amount of indentation depends on the number of appointments that are rendered next to each other.
		 * In order to compute its location and size, each appointment needs to know:
		 * - its start and ending time,
		 * - its track number,
		 * - its total number of tracks,
		 * - and naturally the total width and height available to draw the day.
		 * 
		 */
		public void setupAppointments()
		{
			// clear
			getChildren().removeAll(regularAppointmentPanes);
			regularAppointmentPanes.clear();
			getChildren().removeAll(wholedayAppointmentPanes);
			wholedayAppointmentPanes.clear();			
			if (calendarObjectProperty.get() == null) return;
			
			// scan all appointments and filter the ones for this day
			for (Agenda.Appointment lAppointment : getSkinnable().appointments())
			{
				// different panes depending on the appointment time
				if (lAppointment.isWholeDay())
				{
					// if appointment falls on the same date as this day pane
					if (isSameDay(calendarObjectProperty.get(), lAppointment.getStartTime()))
					{
						WholedayAppointmentPane lAppointmentPane = new WholedayAppointmentPane(lAppointment, this);
						wholedayAppointmentPanes.add(lAppointmentPane);
					}
				}
				else
				{
					// appointments may span multiple days, but the appointment pane will clamp the start and end date
					RegularAppointmentPane lAppointmentPane = new RegularAppointmentPane(lAppointment, this);
					
					// check if the appointment falls in the same day as this day pane
					if ( isSameDay(calendarObjectProperty.get(), lAppointmentPane.start) 
					  && isSameDay(calendarObjectProperty.get(), lAppointmentPane.end)
					   )
					{
						regularAppointmentPanes.add(lAppointmentPane);
					}
				}
			}
			
			// sort on start time and then decreasing duration
			Collections.sort(regularAppointmentPanes, new Comparator<AbstractDayAppointmentPane>()
			{
				@Override
				public int compare(AbstractDayAppointmentPane o1, AbstractDayAppointmentPane o2)
				{
					if (o1.startAsString.equals(o2.startAsString) == false)
					{
						return o1.startAsString.compareTo(o2.startAsString);
					}
					return o1.durationInMS > o2.durationInMS ? -1 : 1;
				}
			});
			
			// start placing appointments in the tracks
			RegularAppointmentPane lClusterOwner = null;
			for (RegularAppointmentPane lAppointmentPane : regularAppointmentPanes)
			{
				// if there is no cluster owner
				if (lClusterOwner == null)
				{
					// than the current becomes an owner
					// only create a minimal cluster, because it will be setup fully in the code below
					lClusterOwner = lAppointmentPane;
					lClusterOwner.clusterTracks = new ArrayList<List<RegularAppointmentPane>>();
				}
				
				// in which track should it be added
				int lTrackNr = determineTrackWhereAppointmentCanBeAdded(lClusterOwner.clusterTracks, lAppointmentPane);
				// if it can be added to track 0, then we have a "situation". Track 0 could mean
				// - we must start a new cluster
				// - the appointment is still linked to the running cluster by means of a linking appointment in the higher tracks
				if (lTrackNr == 0)
				{
					// So let's see if there is a linking appointment higher up
					boolean lOverlaps = false;
					for (int i = 1; i < lClusterOwner.clusterTracks.size() && lOverlaps == false; i++)
					{
						lOverlaps = checkIfTheAppointmentOverlapsAnAppointmentAlreadyInThisTrack(lClusterOwner.clusterTracks, i, lAppointmentPane);
					}
					
					// if it does not overlap, we start a new cluster
					if (lOverlaps == false)
					{
						lClusterOwner = lAppointmentPane;
						lClusterOwner.clusterMembers = new ArrayList<RegularAppointmentPane>(); 
						lClusterOwner.clusterTracks = new ArrayList<List<RegularAppointmentPane>>();
						lClusterOwner.clusterTracks.add(new ArrayList<RegularAppointmentPane>());
					}
				}
				
				// add it to the track (and setup all other cluster data)
				lClusterOwner.clusterMembers.add(lAppointmentPane);
				lClusterOwner.clusterTracks.get(lTrackNr).add(lAppointmentPane);
				lAppointmentPane.clusterOwner = lClusterOwner;
				lAppointmentPane.clusterTrackIdx = lTrackNr;				
				// for debug  System.out.println("----"); for (int i = 0; i < lClusterOwner.clusterTracks.size(); i++) { System.out.println(i + ": " + lClusterOwner.clusterTracks.get(i) ); } System.out.println("----");
			}
			
			// and layout
			getChildren().addAll(wholedayAppointmentPanes);
			getChildren().addAll(regularAppointmentPanes);
			// laying out the appointments is fairly complex, so we use listeners and a relayout method instead of binding
			relayout();
			
			// we're done, now have the header updated
			dayHeaderPane.setupAppointments();
		}
		final List<RegularAppointmentPane> regularAppointmentPanes = new ArrayList<RegularAppointmentPane>(); 
		final List<WholedayAppointmentPane> wholedayAppointmentPanes = new ArrayList<WholedayAppointmentPane>(); 
	}
	
	/**
	 * Responsible for rendering a whoelday appointment on a single day.
	 * 
	 */
	class WholedayAppointmentPane extends AbstractDayAppointmentPane
	{
		/**
		 * 
		 * @param calendar
		 * @param appointment
		 */
		public WholedayAppointmentPane(Agenda.Appointment appointment, DayPane dayPane)
		{
			super(appointment, dayPane);
			isDraggable = false;
			
			// start
			this.start = setTimeTo0000( (Calendar)appointment.getStartTime().clone() );
			this.end = setTimeTo2359( (Calendar)appointment.getStartTime().clone() );
			durationInMS = this.end.getTimeInMillis() - this.start.getTimeInMillis();
			
			// strings
			this.startAsString = timeFormat.format(this.start.getTime());
			this.endAsString = timeFormat.format(this.end.getTime());
			
			// history visualizer
			historicalVisualizer = new HistoricalVisualizer(this);
			getChildren().add(historicalVisualizer);
		}

		/**
		 * 
		 */
		public String toString()
		{
			return super.toString()
				 + ";" + startAsString + " wholeday"
				 + ";" + appointment.getSummary()
				 ;
		}
	}

	/**
	 * Responsible for rendering a regular appointment on a day.
	 * Appointments may span multiple days, each day gets its own appointment pane.
	 * 
	 */
	class RegularAppointmentPane extends AbstractDayAppointmentPane
	{
		// for the role of cluster owner
		List<RegularAppointmentPane> clusterMembers = null; 
		List<List<RegularAppointmentPane>> clusterTracks = null;
		
		// for the role of cluster member
		RegularAppointmentPane clusterOwner = null;
		int clusterTrackIdx = -1;

		/**
		 * 
		 * @param dayPaneCalendar
		 * @param appointment
		 */
		public RegularAppointmentPane(Agenda.Appointment appointment, DayPane dayPane)
		{
			super(appointment, dayPane);
			
			// start
			Calendar lDayStartCalendar = setTimeTo0000( (Calendar)dayPane.calendarObjectProperty.get().clone() );
			this.start = (appointment.getStartTime().before(lDayStartCalendar) ? lDayStartCalendar : (Calendar)appointment.getStartTime().clone());			
			// end
			Calendar lDayEndCalendar = setTimeTo2359( (Calendar)dayPane.calendarObjectProperty.get().clone() );
			this.end = (appointment.getEndTime().after(lDayEndCalendar) ? lDayEndCalendar : (Calendar)appointment.getEndTime().clone());
			// duration
			durationInMS = this.end.getTimeInMillis() - this.start.getTimeInMillis();
			
			// may span multiple days; mark which one this is 
			isFirstAreaOfAppointment = this.start.equals(appointment.getStartTime()); 
			isLastAreaOfAppointment = this.end.equals(appointment.getEndTime()); 
		
			// strings
			this.startAsString = timeFormat.format(this.start.getTime());
			this.endAsString = timeFormat.format(this.end.getTime());
			
			// add the duration as text
			Text lTimeText = new Text(startAsString + "-" + endAsString);
			{
				lTimeText.getStyleClass().add("AppointmentTimeLabel");
				lTimeText.setX( padding );
				lTimeText.setY(lTimeText.prefHeight(0));
				Rectangle lClip = new Rectangle(0,0,0,0);
				lClip.widthProperty().bind(widthProperty().subtract(padding));
				lClip.heightProperty().bind(heightProperty());
				lTimeText.setClip(lClip);
				getChildren().add(lTimeText);
			}
			// add summary
			Text lSummaryText = new Text(appointment.getSummary());
			{
				lSummaryText.getStyleClass().add("AppointmentLabel");
				lSummaryText.setX( padding );
				lSummaryText.setY( lTimeText.getY() + textHeight1MProperty.get());
				lSummaryText.wrappingWidthProperty().bind(widthProperty().subtract(padding));
				Rectangle lClip = new Rectangle(0,0,0,0);
				lClip.widthProperty().bind(widthProperty());
				lClip.heightProperty().bind(heightProperty().subtract(padding));
				lSummaryText.setClip(lClip);
				getChildren().add(lSummaryText);			
			}
			
			// duration dragger
			if (isLastAreaOfAppointment == true)
			{
				durationDragger = new DurationDragger(this);
				getChildren().add(durationDragger);
			}
			
			// add the menu header
			if (appointment.isWholeDay() == false) 
			{
				getChildren().add(menuIcon);
			}
			
			// history visualizer
			historicalVisualizer = new HistoricalVisualizer(this);
			getChildren().add(historicalVisualizer);
		}
		
		/**
		 * 
		 */
		public String toString()
		{
			return super.toString()
				 + ";" + startAsString + "-" + endAsString
				 + ";" + durationInMS + "ms"
				 + ";" + appointment.getSummary()
				 ;
		}
	}

			
	// ==================================================================================================================
	// HISTORICAL
	
	/**
	 * For whiting-out an appointment pane
	 */
	class HistoricalVisualizer extends Rectangle
	{
		public HistoricalVisualizer(Pane pane)
		{
			// it 100% overlays the pane it is bound to
			setMouseTransparent(true);
			xProperty().set(0);
			yProperty().set(0);
			widthProperty().bind(pane.prefWidthProperty());
			heightProperty().bind(pane.prefHeightProperty());
			setVisible(false);
			getStyleClass().add("History");			
		}
	}
	
	
	// ==================================================================================================================
	// MENU
	
	/**
	 * for the popup menu
	 */
	class MenuIcon extends Rectangle
	{
		public MenuIcon(final AbstractAppointmentPane abstractAppointmentPane)
		{
			// layout
			setX(padding);
			setY(padding);
			setWidth(6);
			setHeight(3);
			
			// style
			getStyleClass().add("MenuIcon");
			
			// play with the mouse pointer to show something can be done here
			setOnMouseEntered(new EventHandler<MouseEvent>()
			{
				@Override
				public void handle(MouseEvent mouseEvent)
				{
					if (!mouseEvent.isPrimaryButtonDown())
					{						
						MenuIcon.this.setCursor(Cursor.HAND);
						
						// no one else
						mouseEvent.consume();
					}
				}
			});
			setOnMouseExited(new EventHandler<MouseEvent>()
			{
				@Override
				public void handle(MouseEvent mouseEvent)
				{
					if (!mouseEvent.isPrimaryButtonDown())
					{
						MenuIcon.this.setCursor(Cursor.DEFAULT);
						
						// no one else
						mouseEvent.consume();
					}
				}
			});
			setOnMousePressed(new EventHandler<MouseEvent>() // these just need to be captured, so they are not processed by the underlying pane
			{
				@Override
				public void handle(MouseEvent mouseEvent)
				{
					mouseEvent.consume();
				}
			});
			setOnMouseReleased(new EventHandler<MouseEvent>() // these just need to be captured, so they are not processed by the underlying pane
			{
				@Override
				public void handle(MouseEvent mouseEvent)
				{
					mouseEvent.consume();
				}
			});
			setOnMouseClicked(new EventHandler<MouseEvent>()
			{
				@Override
				public void handle(MouseEvent mouseEvent)
				{
					mouseEvent.consume();
					showMenu(mouseEvent, abstractAppointmentPane);
				}
			});
		}
	}
	
	
	// ==================================================================================================================
	// DURATION
	
	/**
	 * 
	 */
	class DurationDragger extends Rectangle
	{
		public DurationDragger(AbstractDayAppointmentPane appointmentPane)
		{
			// remember
			this.appointmentPane = appointmentPane;
			
			// bind
			xProperty().bind(appointmentPane.widthProperty().multiply(0.25));
			yProperty().bind(appointmentPane.heightProperty().subtract(5));
			widthProperty().bind(appointmentPane.widthProperty().multiply(0.5));
			setHeight(3);
			
			// styling
			getStyleClass().add("DurationDragger");

			// play with the mouse pointer to show something can be done here
			setOnMouseEntered(new EventHandler<MouseEvent>()
			{
				@Override
				public void handle(MouseEvent mouseEvent)
				{
					if (!mouseEvent.isPrimaryButtonDown())
					{						
						DurationDragger.this.setCursor(Cursor.HAND);
						
						// no one else
						mouseEvent.consume();
					}
				}
			});
			setOnMouseExited(new EventHandler<MouseEvent>()
			{
				@Override
				public void handle(MouseEvent mouseEvent)
				{
					if (!mouseEvent.isPrimaryButtonDown())
					{
						DurationDragger.this.setCursor(Cursor.DEFAULT);
						
						// no one else
						mouseEvent.consume();
					}
				}
			});
			// start resize
			setOnMousePressed(new EventHandler<MouseEvent>()
			{
				@Override
				public void handle(MouseEvent mouseEvent)
				{
					// // record a delta distance for the drag and drop operation.
					// dragDelta.x = stage.getX() - mouseEvent.getScreenX();
					// dragDelta.y = stage.getY() - mouseEvent.getScreenY();
					DurationDragger.this.setCursor(Cursor.V_RESIZE);
					resizeRectangle = new Rectangle(DurationDragger.this.appointmentPane.getLayoutX(), DurationDragger.this.appointmentPane.getLayoutY(), DurationDragger.this.appointmentPane.getWidth(), DurationDragger.this.appointmentPane.getHeight());
					resizeRectangle.getStyleClass().add("GhostRectangle");
					DurationDragger.this.appointmentPane.dayPane.getChildren().add(resizeRectangle);
					
					// this event should not be processed by the appointment area
					mouseEvent.consume();
				}
			});
			// visualize resize
			setOnMouseDragged(new EventHandler<MouseEvent>()
			{
				@Override
				public void handle(MouseEvent mouseEvent)
				{
					// - calculate the number of pixels from onscreen nodeY (layoutY) to onscreen mouseY					
					double lNodeScreenY = NodeUtil.screenY(DurationDragger.this.appointmentPane);
					double lMouseY = mouseEvent.getScreenY();
					double lHeight = lMouseY - lNodeScreenY;
					if (lHeight < 5) lHeight = 5;
					resizeRectangle.setHeight(lHeight);
					
					// no one else
					mouseEvent.consume();
				}
			});
			// end resize
			setOnMouseReleased(new EventHandler<MouseEvent>()
			{
				@Override
				public void handle(MouseEvent mouseEvent)
				{					
					// - calculate the new end date for the appointment (recalculating the duration)
					int ms = (int)(resizeRectangle.getHeight() * durationInMSPerPixelProperty.get());
					Calendar lCalendar = (Calendar)DurationDragger.this.appointmentPane.appointment.getStartTime().clone();					
					lCalendar.add(Calendar.MILLISECOND, ms);
					
					// align to X minutes accuracy
					setTimeToNearestMinutes(lCalendar, 5);
					
					// set the new enddate
					DurationDragger.this.appointmentPane.appointment.setEndTime(lCalendar);
					
					// redo whole week
					setupAppointments();
									
					// reset ui
					DurationDragger.this.setCursor(Cursor.HAND);
					DurationDragger.this.appointmentPane.dayPane.getChildren().remove(resizeRectangle);
					resizeRectangle = null;					
					
					// no one else
					mouseEvent.consume();
				}
			});
		}
		final AbstractDayAppointmentPane appointmentPane;
		Rectangle resizeRectangle;
	}
	
	
	// ==================================================================================================================
	// GENERIC APPOINTMENT AREA

	/**
	 * This class has the shared parts of day (non header) appointment panes
	 *
	 */
	abstract class AbstractDayAppointmentPane extends AbstractAppointmentPane
	{
		public AbstractDayAppointmentPane(Agenda.Appointment appointment, DayPane dayPane)
		{
			super(appointment);
			this.dayPane = dayPane;
			
			// for debugging setStyle("-fx-border-color:BLUE;-fx-border-width:4px;");
			getStyleClass().add("Appointment");
			getStyleClass().add(appointment.getAppointmentGroup().getStyleClass());	
		}
		final DayPane dayPane;
		
		Rectangle historicalVisualizer = null;
		Calendar start = null;
		String startAsString = null;
		Calendar end = null;
		String endAsString = null;
		long durationInMS = 0;
		DurationDragger durationDragger = null;
	}
	
	/**
	 * This class handles shared logic for all (both day and header) appointment panes, like dragging and focusable. 
	 */
	abstract class AbstractAppointmentPane extends Pane
	{
		Agenda.Appointment appointment = null;
		boolean isFirstAreaOfAppointment = true;
		boolean isLastAreaOfAppointment = true;
		boolean isDraggable = true;

		/**
		 * 
		 */
		public AbstractAppointmentPane(Agenda.Appointment appointment)
		{
			// remember
			this.appointment = appointment;

			// tooltip
			Tooltip.install(this, new Tooltip(appointment.getSummary()));
			
			// setup menu arrow
			menuIcon = new MenuIcon(this);

			// ------------
			// dragging
			
			// start drag
			setOnMousePressed(new EventHandler<MouseEvent>()
			{
				@Override
				public void handle(MouseEvent mouseEvent)
				{					
					// no one else
					mouseEvent.consume();
					if (mouseEvent.isPrimaryButtonDown() == false) return;
					if (isDraggable == false) return;

					// no drag yet
					dragEventHasOccurred = mouseEvent.isPrimaryButtonDown() ? false : true; // if not primary mouse, then just assume drag from the start 
					if (isFirstAreaOfAppointment == false) return; // TODO: temporarily

					// place the rectangle
					AbstractAppointmentPane.this.setCursor(Cursor.MOVE);
					double lX = NodeUtil.screenX(AbstractAppointmentPane.this) - NodeUtil.screenX(AgendaWeekSkin.this);
					double lY = NodeUtil.screenY(AbstractAppointmentPane.this) - NodeUtil.screenY(AgendaWeekSkin.this);
					dragRectangle = new Rectangle(lX, lY, AbstractAppointmentPane.this.getWidth(), (AbstractAppointmentPane.this.appointment.isWholeDay() ? titleCalendarHeightProperty.get() : AbstractAppointmentPane.this.getHeight()) );
					dragRectangle.getStyleClass().add("GhostRectangle");
					dragPane.getChildren().add(dragRectangle);
					
					// remember
					startX = mouseEvent.getScreenX();
					startY = mouseEvent.getScreenY();
				}
			});
			// visualize dragging
			setOnMouseDragged(new EventHandler<MouseEvent>()
			{
				@Override
				public void handle(MouseEvent mouseEvent)
				{
					// no one else
					mouseEvent.consume();
					if (mouseEvent.isPrimaryButtonDown() == false) return;

					// no dragged
					dragEventHasOccurred = true;
					if (dragRectangle == null) return;
					
					double lDeltaX = mouseEvent.getScreenX() - startX;
					double lDeltaY = mouseEvent.getScreenY() - startY;
					double lX = NodeUtil.screenX(AbstractAppointmentPane.this) - NodeUtil.screenX(AgendaWeekSkin.this) + lDeltaX;
					double lY = NodeUtil.screenY(AbstractAppointmentPane.this) - NodeUtil.screenY(AgendaWeekSkin.this) + lDeltaY;
					dragRectangle.setX(lX);
					dragRectangle.setY(lY);
					
					// no one else
					mouseEvent.consume();
				}
			});
			// end drag
			setOnMouseReleased(new EventHandler<MouseEvent>()
			{
				@Override
				public void handle(MouseEvent mouseEvent)
				{
					// no one else
					mouseEvent.consume();

					// reset ui
					boolean lDragRectangleWasVisible = (dragRectangle != null);
					AbstractAppointmentPane.this.setCursor(Cursor.HAND);
					dragPane.getChildren().remove(dragRectangle);
					dragRectangle = null;					
					
					// -----
					// select
					
					// if have not dragged (even if the drag rectangle was shown), then we're selecting
					if (dragEventHasOccurred == false)
					{
						// if not shift pressed, clear the selection
						if (mouseEvent.isShiftDown() == false)
						{
							getSkinnable().selectedAppointments().clear();
						}
						// add to selection if not already added
						if (getSkinnable().selectedAppointments().contains(AbstractAppointmentPane.this.appointment) == false)
						{
							getSkinnable().selectedAppointments().add(AbstractAppointmentPane.this.appointment);
						}
						return;
					}
					
					// ------------
					// dragging
					
					if (lDragRectangleWasVisible == false) return;
					
					// find out where it was dropped
					for (DayPane lDayPane : weekPane.dayPanes)
					{
						double lDayX = NodeUtil.screenX(lDayPane); 
						double lDayY = NodeUtil.screenY(lDayPane); 
						if ( lDayX <= mouseEvent.getScreenX() && mouseEvent.getScreenX() < lDayX + lDayPane.getWidth()
						  && lDayY <= mouseEvent.getScreenY() && mouseEvent.getScreenY() < lDayY + lDayPane.getHeight()
						   )
						{
							// get the appointment that needs handling
							Appointment lAppointment = AbstractAppointmentPane.this.appointment;
							Calendar lDroppedOnCalendar = lDayPane.calendarObjectProperty.get();
		
							// is wholeday now, will become partial
							if (lAppointment.isWholeDay())
							{
								// calculate new start
								Calendar lStartCalendar = copyYMD( lDroppedOnCalendar, (Calendar)lAppointment.getStartTime().clone() );
								// and end times
								Calendar lEndCalendar = lAppointment.getEndTime() == null ? setTimeTo2359( (Calendar)lDroppedOnCalendar.clone() ) : copyYMD( lDroppedOnCalendar, (Calendar)lAppointment.getEndTime().clone() );
								
								// set the new enddate
								lAppointment.setStartTime(lStartCalendar);
								lAppointment.setEndTime(lEndCalendar);
								
								// no longer whole day
								lAppointment.setWholeDay(false);
							}
							else
							{
								// duration
								long lDurationInMS = lAppointment.getEndTime().getTimeInMillis() - lAppointment.getStartTime().getTimeInMillis();
								
								// calculate new start
								Calendar lStartCalendar = copyYMD(lDroppedOnCalendar, (Calendar)lAppointment.getStartTime().clone());
	
								// also add the delta Y minutes
								int lDeltaDurationInMS = (int)((mouseEvent.getScreenY() - startY) * durationInMSPerPixelProperty.get());
								lStartCalendar.add(Calendar.MILLISECOND, lDeltaDurationInMS);
								setTimeToNearestMinutes(lStartCalendar, 5);
								while (isSameDay(lStartCalendar, lDroppedOnCalendar) == false && lStartCalendar.before(lDroppedOnCalendar)) { lStartCalendar.add(Calendar.MINUTE, 1);  }// the delta may have pushed it out of today 
								while (isSameDay(lStartCalendar, lDroppedOnCalendar) == false && lStartCalendar.after(lDroppedOnCalendar)) { lStartCalendar.add(Calendar.MINUTE, -1);  }// the delta may have pushed it out of today
								
								// calculate
								Calendar lEndCalendar = (Calendar)lStartCalendar.clone();
								lEndCalendar.add(Calendar.MILLISECOND, (int)lDurationInMS);
								
								// set the new enddate
								lAppointment.setStartTime(lStartCalendar);
								lAppointment.setEndTime(lEndCalendar);
							}
						}
					}
					
					// find out where it was dropped
					for (DayHeaderPane lDayHeaderPane : weekHeaderPane.dayHeaderPanes)
					{
						double lDayX = NodeUtil.screenX(lDayHeaderPane); 
						double lDayY = NodeUtil.screenY(lDayHeaderPane); 
						if ( lDayX <= mouseEvent.getScreenX() && mouseEvent.getScreenX() < lDayX + lDayHeaderPane.getWidth()
						  && lDayY <= mouseEvent.getScreenY() && mouseEvent.getScreenY() < lDayY + lDayHeaderPane.getHeight()
						   )
						{
							// get the appointment that needs handling
							Appointment lAppointment = AbstractAppointmentPane.this.appointment;
							
							// calculate new start
							Calendar lStartCalendar = copyYMD(lDayHeaderPane.dayPane.calendarObjectProperty.get(), (Calendar)lAppointment.getStartTime().clone() );
							
							// set the new start date
							lAppointment.setStartTime(lStartCalendar);
							
							// enddate can be ignored
							
							// now a whole day (just in case it wasn't)
							lAppointment.setWholeDay(true);
						}
					}
					
					// redo whole week
					setupAppointments();					
				}
			});
		}
		Rectangle dragRectangle;
		double startX = 0;
		double startY = 0;
		boolean dragEventHasOccurred = false;
		Rectangle menuIcon = null;
		
//		public String describe()
//		{
//			// strings
//			StringBuilder lStringBuilder = new StringBuilder();
//			lStringBuilder.append( dateFormat.format(this.appointment.getStartTime().getTime()) );
//			if (this.appointment.isWholeDay() == false)
//			{
//				lStringBuilder.append( " " );
//				lStringBuilder.append( timeFormat.format(this.appointment.getStartTime().getTime()) );
//				lStringBuilder.append( " - " );
//				if (isSameDay(this.appointment.getStartTime(), this.appointment.getEndTime()) == false)
//				{
//					lStringBuilder.append( dateFormat.format(this.appointment.getEndTime().getTime()) );
//					lStringBuilder.append( " " );
//				}
//				lStringBuilder.append( timeFormat.format(this.appointment.getEndTime().getTime()) );
//			}
//			return lStringBuilder.toString();
//		}
	}
	AbstractAppointmentPane focused = null;

	
	// ==================================================================================================================
	// NOW
	
	final Rectangle nowLine = new Rectangle(0,0,0,0);
	
	/**
	 * This is implemented as a runnable so it can be called from a timer, but also directly
	 */
	Runnable nowUpdateRunnable = new Runnable()
	{
		{
			nowLine.getStyleClass().add("Now");
		}
		
		@Override
		public void run()
		{
			//  get now
			Calendar lNow = Calendar.getInstance();
			
			// see if we are displaying now (this has to do with the fact that now may slide in or out of the view)
			// check all days
			boolean lFound = false;
			for (DayPane lDayPane : weekPane.dayPanes)
			{
				// if the calendar of the day is the same day as now
				if (isSameDay(lDayPane.calendarObjectProperty.get(), lNow))
				{
					lFound = true;
					
					// add if not present
					if (weekPane.getChildren().contains(nowLine) == false)
					{
						weekPane.getChildren().add(nowLine);
					}

					// place it
					int lOffsetY = (lNow.get(Calendar.HOUR_OF_DAY) * 60) + lNow.get(Calendar.MINUTE);
					nowLine.setX(lDayPane.getLayoutX());
					nowLine.setY(dayHeightProperty.get() / (24 * 60) * lOffsetY );
					nowLine.setHeight(3);
					nowLine.setWidth(dayWidthProperty.get());	
				}
				
				// display history
				for (RegularAppointmentPane lAppointmentPane : lDayPane.regularAppointmentPanes)
				{
					lAppointmentPane.historicalVisualizer.setVisible( lAppointmentPane.start.before(lNow));
				}
				for (WholedayAppointmentPane lAppointmentPane : lDayPane.wholedayAppointmentPanes)
				{
					lAppointmentPane.historicalVisualizer.setVisible( lAppointmentPane.start.before(lNow));
				}
			}
			
			// if cannot be placed, remove
			if (lFound == false)
			{
				weekPane.getChildren().remove(nowLine);
			}
			
			// also for headers
			for (DayHeaderPane lDayHeaderPane : weekHeaderPane.dayHeaderPanes)
			{
				for (AppointmentHeaderPane lAppointmentHeaderPane : lDayHeaderPane.appointmentHeaderPanes)
				{
					lAppointmentHeaderPane.historicalVisualizer.setVisible(lAppointmentHeaderPane.appointment.getStartTime().before(lNow));
				}
			}
		}
	};
	
	/**
	 * This timer takes care of updating NOW
	 */
	Timer nowTimer = new Timer(nowUpdateRunnable)
		.withCycleDuration(new Duration(60 * 1000)) // every minute
		.withDelay(new Duration( (60 - Calendar.getInstance().get(Calendar.SECOND)) * 1000)) // trigger exactly on each new minute
		.start();  

	
	// ==================================================================================================================
	// POPUP

	/*
	 * 
	 */
	private void showMenu(MouseEvent evt, final AbstractAppointmentPane abstractAppointmentPane)
	{
		// create popup
		final Popup lPopup = new Popup();
		lPopup.setAutoFix(true);
		lPopup.setAutoHide(true);
		lPopup.setHideOnEscape(true);
		lPopup.setOnHidden(new EventHandler<WindowEvent>()
		{
			@Override
			public void handle(WindowEvent arg0)
			{
				setupAppointments();
			}
		});

		BorderPane lBorderPane = new BorderPane();
		lBorderPane.getStyleClass().add(this.getClass().getSimpleName() + "_popup");
		lPopup.getContent().add(lBorderPane);
		
		// close icon
		ImageView lImageView = new ImageView(closeIconImage);
		lImageView.setPickOnBounds(true);
		lImageView.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override public void handle(MouseEvent evt)
			{
				lPopup.hide(); 
			}
		});
		lBorderPane.setRight(lImageView);

		// initial layout
		VBox lMenuVBox = new VBox(padding);
		lBorderPane.setCenter(lMenuVBox);

		// time
		lMenuVBox.getChildren().add(new Text("Time:"));
		// start
		final CalendarTextField lStartCalendarTextField = new CalendarTextField().withShowTime(true);
		lStartCalendarTextField.setLocale(getSkinnable().getLocale());
		lStartCalendarTextField.setValue(abstractAppointmentPane.appointment.getStartTime());
		lMenuVBox.getChildren().add(lStartCalendarTextField);
		// end
		final CalendarTextField lEndCalendarTextField = new CalendarTextField().withShowTime(true);
		lEndCalendarTextField.setLocale(getSkinnable().getLocale());
		lEndCalendarTextField.setValue(abstractAppointmentPane.appointment.getEndTime());
		lMenuVBox.getChildren().add(lEndCalendarTextField);
		lEndCalendarTextField.valueProperty().addListener(new ChangeListener<Calendar>()
		{
			@Override
			public void changed(ObservableValue<? extends Calendar> arg0, Calendar oldValue, Calendar newValue)
			{
				abstractAppointmentPane.appointment.setEndTime(newValue);
				// refresh is done upon popup close
			}
		});
		lEndCalendarTextField.setVisible(abstractAppointmentPane.appointment.getEndTime() != null);
		// wholeday
		final CheckBox lWholedayCheckBox = new CheckBox("Wholeday");
		lWholedayCheckBox.selectedProperty().set(abstractAppointmentPane.appointment.isWholeDay());
		lMenuVBox.getChildren().add(lWholedayCheckBox);
		lWholedayCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>()
		{
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldValue, Boolean newValue)
			{
				abstractAppointmentPane.appointment.setWholeDay(newValue);
				if (newValue == true) 
				{
					abstractAppointmentPane.appointment.setEndTime(null);
				}
				else
				{
					Calendar lEndTime = (Calendar)abstractAppointmentPane.appointment.getStartTime().clone();
					lEndTime.add(Calendar.MINUTE, 30);
					abstractAppointmentPane.appointment.setEndTime(lEndTime);
					lEndCalendarTextField.setValue(abstractAppointmentPane.appointment.getEndTime());
				}
				lEndCalendarTextField.setVisible(abstractAppointmentPane.appointment.getEndTime() != null);
				// refresh is done upon popup close
			}
		});
		// event handling
		lStartCalendarTextField.valueProperty().addListener(new ChangeListener<Calendar>()
		{
			@Override
			public void changed(ObservableValue<? extends Calendar> arg0, Calendar oldValue, Calendar newValue)
			{
				// enddate
				if (abstractAppointmentPane.appointment.isWholeDay())
				{
					abstractAppointmentPane.appointment.setStartTime(newValue);
				}
				else
				{
					// calculate duration
					long lDurationInMS = abstractAppointmentPane.appointment.getEndTime().getTimeInMillis() - abstractAppointmentPane.appointment.getStartTime().getTimeInMillis();
					
					// set
					abstractAppointmentPane.appointment.setStartTime(newValue);
					
					// end date
					Calendar lEndCalendar = (Calendar)abstractAppointmentPane.appointment.getStartTime().clone();
					lEndCalendar.add(Calendar.MILLISECOND, (int)lDurationInMS);
					abstractAppointmentPane.appointment.setEndTime(lEndCalendar);
					
					// update field
					lEndCalendarTextField.setValue(abstractAppointmentPane.appointment.getEndTime());
					
					// refresh is done upon popup close
				}
			}
		});
		
		// summary
		lMenuVBox.getChildren().add(new Text("Summary:"));
		TextField lSummaryTextField = new TextField();
		lSummaryTextField.setText(abstractAppointmentPane.appointment.getSummary());
		lSummaryTextField.textProperty().addListener(new ChangeListener<String>()
		{
			@Override
			public void changed(ObservableValue<? extends String> arg0, String oldValue, String newValue)
			{
				abstractAppointmentPane.appointment.setSummary(newValue);
				// refresh is done upon popup close
			}
		});
		lMenuVBox.getChildren().add(lSummaryTextField);
		
		// location
		lMenuVBox.getChildren().add(new Text("Location:"));
		TextField lLocationTextField = new TextField();
		lLocationTextField.setText( abstractAppointmentPane.appointment.getLocation() == null ? "" : abstractAppointmentPane.appointment.getLocation());
		lLocationTextField.textProperty().addListener(new ChangeListener<String>()
		{
			@Override
			public void changed(ObservableValue<? extends String> arg0, String oldValue, String newValue)
			{
				abstractAppointmentPane.appointment.setLocation(newValue);
				// refresh is done upon popup close
			}
		});
		lMenuVBox.getChildren().add(lLocationTextField);
		
		// actions
		lMenuVBox.getChildren().add(new Text("Actions:"));
		HBox lHBox = new HBox();
		lMenuVBox.getChildren().add(lHBox);
		// delete
		{
			ImageButton lImageButton = new ImageButton( new Image(this.getClass().getResourceAsStream("jqueryMobileBlack16x16/delete.png")) );
			lImageButton.setOnMouseClicked(new EventHandler<MouseEvent>()
			{
				@Override public void handle(MouseEvent evt)
				{
					lPopup.hide();
					getSkinnable().selectedAppointments().remove(abstractAppointmentPane.appointment); // TODO: we should make sure this happens in the control when the appointment is remove from the appointments collection
					getSkinnable().appointments().remove(abstractAppointmentPane.appointment);
					// refresh is done via the collection events
				}
			});
			Tooltip.install(lImageButton, new Tooltip("Delete"));
			lHBox.getChildren().add(lImageButton);
		}
		
		// construct a area of appointment groups
		lMenuVBox.getChildren().add(new Text("Group:"));
		GridPane lAppointmentGroupGridPane = new GridPane();
		lMenuVBox.getChildren().add(lAppointmentGroupGridPane);
		lAppointmentGroupGridPane.getStyleClass().add("AppointmentGroups");
		lAppointmentGroupGridPane.setHgap(2);
		lAppointmentGroupGridPane.setVgap(2);
		int lCnt = 0;
		for (Agenda.AppointmentGroup lAppointmentGroup : getSkinnable().appointmentGroups())
		{
			// create the appointment group
			final Pane lPane = new Pane();			
			lPane.setPrefSize(15, 15);
			lPane.getStyleClass().addAll("AppointmentGroup", lAppointmentGroup.getStyleClass());
			lAppointmentGroupGridPane.add(lPane, lCnt % 10, lCnt / 10 );
			lCnt++;
			
			// tooltip
			Tooltip.install(lPane, new Tooltip(lAppointmentGroup.getDescription()));
			 
			// mouse reactions
			lPane.setOnMouseEntered(new EventHandler<MouseEvent>()
			{
				@Override
				public void handle(MouseEvent mouseEvent)
				{
					if (!mouseEvent.isPrimaryButtonDown())
					{						
						mouseEvent.consume();
						lPane.setCursor(Cursor.HAND);
					}
				}
			});
			lPane.setOnMouseExited(new EventHandler<MouseEvent>()
			{
				@Override
				public void handle(MouseEvent mouseEvent)
				{
					if (!mouseEvent.isPrimaryButtonDown())
					{
						mouseEvent.consume();
						lPane.setCursor(Cursor.DEFAULT);
					}
				}
			});
			final Agenda.AppointmentGroup lAppointmentGroupFinal = lAppointmentGroup; 
			lPane.setOnMouseClicked(new EventHandler<MouseEvent>()
			{
				@Override
				public void handle(MouseEvent mouseEvent)
				{
					mouseEvent.consume();
					
					// assign appointment group
					abstractAppointmentPane.appointment.setAppointmentGroup(lAppointmentGroupFinal);
					
					// refresh is done upon popup close
					lPopup.hide();
				}
			});
		}
		
		// show it just below the menu icon
		lPopup.show(abstractAppointmentPane, NodeUtil.screenX(abstractAppointmentPane), NodeUtil.screenY(abstractAppointmentPane.menuIcon) + abstractAppointmentPane.menuIcon.getHeight());
	}

	// ==================================================================================================================
	// SUPPORT

	/**
	 * 
	 */
	private void calculateSizes()
	{
		// generic
		double lScrollbarSize = new ScrollBar().getWidth();
		textHeight1MProperty.set( new Text("X").getBoundsInParent().getHeight() );
		
		// header
		highestNumberOfWholedayAppointmentsProperty.set(0);
		for (DayPane lDay : weekPane.dayPanes)
		{
			if (lDay.wholedayAppointmentPanes.size() > highestNumberOfWholedayAppointmentsProperty.get())
			{
				highestNumberOfWholedayAppointmentsProperty.set( lDay.wholedayAppointmentPanes.size() );
			}
		}
		titleCalendarHeightProperty.set( 1.5 * textHeight1MProperty.get() ); 
		wholedayTitleHeightProperty.set( textHeight1MProperty.get() + 5 ); // not sure why the 5 is needed
		headerHeightProperty.set( titleCalendarHeightProperty.get() + (highestNumberOfWholedayAppointmentsProperty.get() * wholedayTitleHeightProperty.get()) );

		// time column
		int lTimeColumnWhitespace = 10;
		timeWidthProperty.set( new Text("88:88").getBoundsInParent().getWidth() + lTimeColumnWhitespace );
		
		// day columns
		dayFirstColumnXProperty.set( timeWidthProperty.get() + lTimeColumnWhitespace );
		dayWidthProperty.set( (getSkinnable().getWidth() - timeWidthProperty.get() - lScrollbarSize) / 7 ); // 7 days per week
		if (weekScrollPane.viewportBoundsProperty().get() != null) 
		{
			dayWidthProperty.set( (weekScrollPane.viewportBoundsProperty().get().getWidth() - timeWidthProperty.get()) / 7 ); // 7 days per week
		}
		dayContentWidthProperty.set( dayWidthProperty.get() - 10 ); // the 10 is a margin at the right so that there is always room to start a new appointment
		
		// hour height
		hourHeighProperty.set( (2 * textHeight1MProperty.get()) + 10 ); // 10 is padding
		if (weekScrollPane.viewportBoundsProperty().get() != null && (weekScrollPane.viewportBoundsProperty().get().getHeight() - lScrollbarSize) > hourHeighProperty.get() * 24)
		{
			// if there is more room than absolutely required, let the height grow with the available room
			hourHeighProperty.set( (weekScrollPane.viewportBoundsProperty().get().getHeight() - lScrollbarSize) / 24 );
		}
		dayHeightProperty.set(hourHeighProperty.get() * 24);
		durationInMSPerPixelProperty.set( (24 * 60 * 60 * 1000) / dayHeightProperty.get() );
		
		// if the viewport is active
		if (weekScrollPane.viewportBoundsProperty() != null && weekScrollPane.viewportBoundsProperty().get() != null)
		{
			// make sure the border pane uses these sizes
			weekHeaderPane.setPrefSize(weekScrollPane.viewportBoundsProperty().get().getWidth(), headerHeightProperty.get());
			weekPane.setPrefSize(weekScrollPane.viewportBoundsProperty().get().getWidth(), 24 * hourHeighProperty.get());				
		}
	}
	private final double padding = 3;
	private final double wholedayAppointmentWidth = 5;
	private final IntegerProperty highestNumberOfWholedayAppointmentsProperty = new SimpleIntegerProperty(0);
	private final DoubleProperty textHeight1MProperty = new SimpleDoubleProperty(0);
	private final DoubleProperty titleCalendarHeightProperty = new SimpleDoubleProperty(0);
	private final DoubleProperty headerHeightProperty = new SimpleDoubleProperty(0);
	private final DoubleProperty wholedayTitleHeightProperty = new SimpleDoubleProperty(0);
	private final DoubleProperty timeWidthProperty = new SimpleDoubleProperty(0); 
	private final DoubleProperty dayFirstColumnXProperty = new SimpleDoubleProperty(0); 
	private final DoubleProperty dayWidthProperty = new SimpleDoubleProperty(0); 
	private final DoubleProperty dayContentWidthProperty = new SimpleDoubleProperty(0); 
	private final DoubleProperty dayHeightProperty = new SimpleDoubleProperty(0);  
	private final DoubleProperty durationInMSPerPixelProperty = new SimpleDoubleProperty(0);
	private final DoubleProperty hourHeighProperty = new SimpleDoubleProperty(0); 
	
	
	/**
	 * get the calendar for the first day of the week
	 */
	protected Calendar getFirstDayOfWeekCalendar()
	{
		// result
		Calendar lLocalCalendar = Calendar.getInstance(getSkinnable().getLocale());
		int lFirstDayOfWeek = lLocalCalendar.getFirstDayOfWeek();
		
		// now get the displayed calendar
		Calendar lDisplayedCalendar = getSkinnable().getDisplayedCalendar();
		
		// this is the first day of week calendar
		Calendar lFirstDayOfWeekCalendar = (Calendar)getSkinnable().getDisplayedCalendar().clone();
		
		// if not on the first day of the week, correct with the appropriate amount
		lFirstDayOfWeekCalendar.add(Calendar.DATE, lFirstDayOfWeek - lFirstDayOfWeekCalendar.get(Calendar.DAY_OF_WEEK));
		
		// make sure we are in the same week
		while ( lFirstDayOfWeekCalendar.get(Calendar.YEAR) > lDisplayedCalendar.get(Calendar.YEAR)
			 || (lFirstDayOfWeekCalendar.get(Calendar.YEAR) == lDisplayedCalendar.get(Calendar.YEAR) && lFirstDayOfWeekCalendar.get(Calendar.WEEK_OF_YEAR) > lDisplayedCalendar.get(Calendar.WEEK_OF_YEAR))
			  )
		{
			lFirstDayOfWeekCalendar.add(Calendar.DATE, -7);
		}
		while ( lFirstDayOfWeekCalendar.get(Calendar.YEAR) < lDisplayedCalendar.get(Calendar.YEAR)
		     || (lFirstDayOfWeekCalendar.get(Calendar.YEAR) == lDisplayedCalendar.get(Calendar.YEAR) && lFirstDayOfWeekCalendar.get(Calendar.WEEK_OF_YEAR) < lDisplayedCalendar.get(Calendar.WEEK_OF_YEAR))
		      )
		{
			lFirstDayOfWeekCalendar.add(Calendar.DATE, 7);
		}
		
		// done
		return lFirstDayOfWeekCalendar;
	}

	/**
	 * 
	 * @param c1
	 * @param c2
	 * @return
	 */
	private boolean isSameDay(Calendar c1, Calendar c2)
	{
		return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
			&& c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)
			&& c1.get(Calendar.DATE) == c2.get(Calendar.DATE)
			 ; 
	}

	/**
	 * 
	 * @param tracks
	 * @param appointmentPane
	 * @return
	 */
	private int determineTrackWhereAppointmentCanBeAdded(List<List<RegularAppointmentPane>> tracks, RegularAppointmentPane appointmentPane)
	{
		int lTrackNr = 0;
		while (true)
		{
			// make sure there is a arraylist for this track
			if (lTrackNr == tracks.size()) tracks.add(new ArrayList<RegularAppointmentPane>());
			
			// scan all existing appointments in this track and see if there is an overlap
			if (checkIfTheAppointmentOverlapsAnAppointmentAlreadyInThisTrack(tracks, lTrackNr, appointmentPane) == false)
			{
				// no overlap, it can be added here
				return lTrackNr;
			}

			// overlap, try next track
			lTrackNr++;
		}
	}
	
	/**
	 * 
	 * @param tracks
	 * @param tracknr
	 * @param appointmentPane
	 * @return
	 */
	private boolean checkIfTheAppointmentOverlapsAnAppointmentAlreadyInThisTrack(List<List<RegularAppointmentPane>> tracks, int tracknr, RegularAppointmentPane appointmentPane)
	{
		// get the track
		List<RegularAppointmentPane> lTrack = tracks.get(tracknr);
		
		// scan all existing appointments in this track
		for (RegularAppointmentPane lAppointmentPane : lTrack)
		{
			// There is an overlap:
			// if the start time of the already placed appointment is before or equals the new appointment's end time 
			// and the end time of the already placed appointment is after or equals the new appointment's start time
			// ...PPPPPPPPP...
			// .NNNN.......... -> Ps <= Ne & Pe >= Ns -> overlap
			// .....NNNNN..... -> Ps <= Ne & Pe >= Ns -> overlap
			// ..........NNN.. -> Ps <= Ne & Pe >= Ns -> overlap
			// .NNNNNNNNNNNNN. -> Ps <= Ne & Pe >= Ns -> overlap
			// .N............. -> false	& Pe >= Ns -> no overlap
			// .............N. -> Ps <= Ne & false	-> no overlap
			if ( (lAppointmentPane.start.equals(appointmentPane.start) || lAppointmentPane.start.before(appointmentPane.end)) 
			  && (lAppointmentPane.end.equals(appointmentPane.start) || lAppointmentPane.end.after(appointmentPane.start))
			   )
			{
				// overlap
				return true;
			}
		}
		
		// no overlap
		return false;
	}
	
	/**
	 * 
	 * @param c
	 * @return
	 */
	private Calendar setTimeTo0000(Calendar c)
	{
		// start
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c;
	}
	
	/**
	 * 
	 * @param c
	 * @return
	 */
	private Calendar setTimeTo2359(Calendar c)
	{
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);
		return c;
	}
	
	/**
	 * 
	 * @param c
	 * @param minutes
	 * @return
	 */
	private Calendar setTimeToNearestMinutes(Calendar c, int minutes)
	{
		// align to X minutes accuracy
		c.set(Calendar.MILLISECOND, 0);
		c.set(Calendar.SECOND, 0);
		int lMinutes = c.get(Calendar.MINUTE) % minutes;
		if (lMinutes < (minutes/2)) c.add(Calendar.MINUTE, -1 * lMinutes);
		else c.add(Calendar.MINUTE, minutes - lMinutes);
		return c;
	}
	
	/**
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	private Calendar copyYMD(Calendar from, Calendar to)
	{
		to.set(Calendar.YEAR, from.get(Calendar.YEAR));
		to.set(Calendar.MONTH, from.get(Calendar.MONTH));
		to.set(Calendar.DATE, from.get(Calendar.DATE));
		return to;
	}
	
	class ImageButton extends ImageView
	{
		public ImageButton(Image i)
		{
			super(i);
			setPickOnBounds(true);
			setOnMouseEntered(new EventHandler<MouseEvent>()
			{
				@Override
				public void handle(MouseEvent mouseEvent)
				{
					if (!mouseEvent.isPrimaryButtonDown())
					{						
						ImageButton.this.setCursor(Cursor.HAND);
					}
				}
			});
			setOnMouseExited(new EventHandler<MouseEvent>()
			{
				@Override
				public void handle(MouseEvent mouseEvent)
				{
					if (!mouseEvent.isPrimaryButtonDown())
					{
						ImageButton.this.setCursor(Cursor.DEFAULT);
					}
				}
			});
		}
	}
}
