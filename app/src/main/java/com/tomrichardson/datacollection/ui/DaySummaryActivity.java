package com.tomrichardson.datacollection.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.tomrichardson.datacollection.R;
import com.tomrichardson.datacollection.Utils;
import com.tomrichardson.datacollection.model.SummaryModel;
import com.tomrichardson.datacollection.model.summary.ActivitySummary;
import com.tomrichardson.datacollection.ui.summary.ActivitySummaryView;
import com.tomrichardson.datacollection.ui.summary.LocationSummaryView;
import com.tomrichardson.datacollection.ui.summary.MoodSummaryView;
import com.tomrichardson.datacollection.ui.summary.PhoneSummaryView;

import java.util.Calendar;

/**
 * Created by tom on 16/03/2016.
 */
public class DaySummaryActivity extends AppCompatActivity implements OnMapReadyCallback {

  public static final String SUMMARY_KEY = "summary_model";
  private SummaryModel summary;


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_day_summary);

    Bundle b = getIntent().getExtras();
    if (b.containsKey(SUMMARY_KEY)) {
      summary = b.getParcelable(SUMMARY_KEY);
      if (summary == null) {
        finish();
        return;
      }
    } else {
      finish();
      return;
    }

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    Calendar day = summary.getDate();
    getSupportActionBar().setTitle(day.get(Calendar.DAY_OF_MONTH) + Utils.getDaySuffix(day.get(Calendar.DAY_OF_MONTH))
        + " " + Utils.MONTH_NAMES[day.get(Calendar.MONTH)] + " " + day.get(Calendar.YEAR));
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    LocationSummaryView locationSummary = (LocationSummaryView) findViewById(R.id.location_summary);
    ActivitySummaryView activitySummary = (ActivitySummaryView) findViewById(R.id.activity_summary);
    MoodSummaryView moodSummary = (MoodSummaryView) findViewById(R.id.mood_summary);
    PhoneSummaryView phoneSummary = (PhoneSummaryView) findViewById(R.id.phone_summary);

    // Locations
    if (summary.locations != null && summary.locations.size() > 0) {
      locationSummary.initData(summary.locations.get(0), getSupportFragmentManager());
    } else {
      locationSummary.setVisible(false);
    }

    // Activities
    if (summary.activities != null && summary.activities.size() > 0) {
      ActivitySummary walking = null, running = null, vehicle = null, bicycle = null;

      for (ActivitySummary s : summary.activities) {
        switch (s.activityType) {
          case DetectedActivity.WALKING:
            walking = s;
            continue;
          case DetectedActivity.RUNNING:
            running = s;
            continue;
          case DetectedActivity.IN_VEHICLE:
            vehicle = s;
            continue;
          case DetectedActivity.ON_BICYCLE:
            bicycle = s;
            continue;
        }
      }

      if (walking == null && running == null && vehicle == null && bicycle == null) {
        activitySummary.setVisible(false);
      } else {
        activitySummary.setTimes(walking, running, vehicle, bicycle);
      }
    } else {
      activitySummary.setVisible(false);
    }

    // Mood
    if(summary.textSummary != null) {
      moodSummary.setData(summary.textSummary.positiveWordCount, summary.textSummary.negativeWordCount, summary.textSummary.wordCount);
    } else {
      moodSummary.setVisible(false);
    }

    // Phone
    if(summary.callSummary != null) {
      phoneSummary.setData(summary.callSummary.timeSpentOnPhone, summary.callSummary.callCount, summary.callSummary.mostCalledNumber);
    } else {
      phoneSummary.setVisible(false);
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem menuItem) {
    if (menuItem.getItemId() == android.R.id.home) {
      finish();
    }
    return super.onOptionsItemSelected(menuItem);
  }

  @Override
  public void onMapReady(GoogleMap googleMap) {

  }
}
