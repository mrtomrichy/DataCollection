package com.tomrichardson.datacollection.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.maps.MapView;
import com.tomrichardson.datacollection.Application;
import com.tomrichardson.datacollection.R;
import com.tomrichardson.datacollection.Utils;
import com.tomrichardson.datacollection.model.CalendarDayModel;
import com.tomrichardson.datacollection.model.SummaryModel;
import com.tomrichardson.datacollection.model.summary.ActivitySummary;
import com.tomrichardson.datacollection.model.summary.CallSummary;
import com.tomrichardson.datacollection.model.summary.LocationSummary;
import com.tomrichardson.datacollection.model.summary.TextSummary;
import com.tomrichardson.datacollection.ui.adapter.CalendarDayAdapter;
import com.tomrichardson.datacollection.ui.utils.OnSwipeTouchListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import co.uk.rushorm.core.RushSearch;

/**
 * Created by tom on 10/03/2016.
 */
public class CalendarActivity extends AppCompatActivity {

  private static final String TAG = "CalendarActivity";

  private List<CalendarDayModel> daysOfMonth;
  private CalendarDayAdapter adapter;
  private Calendar currentMonth;
  private int todayDay, todayMonth, todayYear;

  private TextView monthOutput;
  private ImageView previousMonthButton;
  private ImageView nextMonthButton;

  private boolean canGoForward = false;

  private BroadcastReceiver dbReadyListener = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      updateCalendar();
      unregisterReceiver(dbReadyListener);
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_calendar);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    getSupportActionBar().setTitle("View your data");

    daysOfMonth = new ArrayList<>();

    RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.calendar_view);

    RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 7);
    mRecyclerView.setLayoutManager(mLayoutManager);
    //mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(7, 28, false));

    adapter = new CalendarDayAdapter(this, daysOfMonth, new CalendarDayAdapter.OnDayClickedListener() {
      @Override
      public void onDayClicked(CalendarDayModel model) {
        if(model.hasModel()) {
          Intent i = new Intent(CalendarActivity.this, DaySummaryActivity.class);
          i.putExtra(DaySummaryActivity.SUMMARY_KEY, model.model);
          startActivity(i);
        }
      }
    });
    mRecyclerView.setAdapter(adapter);

    currentMonth = Calendar.getInstance();
    currentMonth.set(Calendar.MINUTE, 0);
    currentMonth.set(Calendar.HOUR_OF_DAY, 0);

    todayDay = currentMonth.get(Calendar.DAY_OF_MONTH);
    todayMonth = currentMonth.get(Calendar.MONTH);
    todayYear = currentMonth.get(Calendar.YEAR);

    currentMonth.set(Calendar.DAY_OF_MONTH, 1);

    monthOutput = (TextView) findViewById(R.id.month_textview);

    previousMonthButton = (ImageView) findViewById(R.id.previous_month_button);
    nextMonthButton = (ImageView) findViewById(R.id.next_month_button);

    previousMonthButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        currentMonth.add(Calendar.MONTH, -1);
        updateCalendar();
      }
    });

    nextMonthButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        currentMonth.add(Calendar.MONTH, 1);
        updateCalendar();
      }
    });

    mRecyclerView.setOnTouchListener(new OnSwipeTouchListener(this) {
      @Override
      public void onSwipeRight() {
        currentMonth.add(Calendar.MONTH, -1);
        updateCalendar();
      }

      @Override
      public void onSwipeLeft() {
        if (canGoForward) {
          currentMonth.add(Calendar.MONTH, 1);
          updateCalendar();
        }
      }
    });

    if(Application.RUSH_INITIALISED) {
      updateCalendar();
    } else {
      registerReceiver(dbReadyListener, new IntentFilter(Application.RUSH_INIT_COMPLETE));
    }

    slowLoadMapHack();
  }

  private void slowLoadMapHack() {
    // Fixing Later Map loading Delay
    new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          MapView mv = new MapView(getApplicationContext());
          mv.onCreate(null);
          mv.onPause();
          mv.onDestroy();
        }catch (Exception ignored){

        }
      }
    }).start();
  }

  private void addTestSummary(String date) {
    Log.d(TAG, "AddingTestSummary");
    List<LocationSummary> locations = new ArrayList<>();
    locations.add(new LocationSummary("University of Manchester", 5, 53.467466, -2.234046));
    List<ActivitySummary> activities = new ArrayList<>();
    activities.add(new ActivitySummary(DetectedActivity.WALKING, 8403));
    activities.add(new ActivitySummary(DetectedActivity.RUNNING, 2487));
    activities.add(new ActivitySummary(DetectedActivity.IN_VEHICLE, 1427));
    new SummaryModel(date, locations, activities, new CallSummary(15, "07984039091", "00:34:23"), new TextSummary(1152, 146, 203)).save();
  }

  public void updateCalendar() {
//    addTestSummary("2016/03/15");
    int daysInMonth = currentMonth.getActualMaximum(Calendar.DAY_OF_MONTH);
    daysOfMonth.removeAll(daysOfMonth);

    if (currentMonth.get(Calendar.DAY_OF_WEEK) > 1) {
      Calendar temp = (Calendar) currentMonth.clone();
      temp.add(Calendar.MONTH, -1);

      int daysOfLastMonth = temp.getActualMaximum(Calendar.DAY_OF_MONTH);

      for (int i = currentMonth.get(Calendar.DAY_OF_WEEK); i > 1; i--) {
        daysOfMonth.add(new CalendarDayModel((daysOfLastMonth) - (i - 2), false, false, null));
      }
    }

    boolean todayFound = false;
    for (int i = 1; i < daysInMonth + 1; i++) {
      SummaryModel model = new RushSearch().whereEqual("date",
          SummaryModel.getDateString(new GregorianCalendar(currentMonth.get(Calendar.YEAR), currentMonth.get(Calendar.MONTH), i).getTime()))
          .findSingle(SummaryModel.class);
      if (isToday(i, currentMonth.get(Calendar.MONTH), currentMonth.get(Calendar.YEAR))) {
        daysOfMonth.add(new CalendarDayModel(i, true, true, null));
        todayFound = true;
      } else if (todayFound) {
        daysOfMonth.add(new CalendarDayModel(i, false, false, null));
      } else {
        daysOfMonth.add(new CalendarDayModel(i, true, false, model));
      }
    }

    currentMonth.set(Calendar.DAY_OF_MONTH, daysInMonth);

    int day = 1;
    for (int i = currentMonth.get(Calendar.DAY_OF_WEEK); i < 7; i++) {
      daysOfMonth.add(new CalendarDayModel(day++, false, false, null));
    }

    currentMonth.set(Calendar.DAY_OF_MONTH, 1);

    monthOutput.setText(String.format("%s %d", Utils.MONTH_NAMES[currentMonth.get(Calendar.MONTH)], currentMonth.get(Calendar.YEAR)));

    if (todayFound) {
      canGoForward = false;
      nextMonthButton.setEnabled(false);
      nextMonthButton.setVisibility(View.INVISIBLE);
    } else {
      canGoForward = true;
      nextMonthButton.setEnabled(true);
      nextMonthButton.setVisibility(View.VISIBLE);
    }

    adapter.notifyDataSetChanged();
  }

  private boolean isToday(int day, int month, int year) {
    return day == todayDay && month == todayMonth && year == todayYear;
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = new MenuInflater(this);
    inflater.inflate(R.menu.menu_main, menu);

    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem menuItem) {
    if (menuItem.getItemId() == R.id.action_settings) {
      startActivity(new Intent(this, ServiceViewActivity.class));
    }
    return super.onOptionsItemSelected(menuItem);
  }

  @Override
  protected void onResume() {
    super.onResume();
  }

  @Override
  protected void onPause() {
    super.onPause();
  }
}
