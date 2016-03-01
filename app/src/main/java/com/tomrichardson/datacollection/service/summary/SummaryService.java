package com.tomrichardson.datacollection.service.summary;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.tomrichardson.datacollection.Utils;
import com.tomrichardson.datacollection.model.ActivityModel;
import com.tomrichardson.datacollection.model.LocationModel;
import com.tomrichardson.datacollection.model.PhoneCallModel;
import com.tomrichardson.datacollection.model.SummaryModel;
import com.tomrichardson.datacollection.model.summary.ActivitySummary;
import com.tomrichardson.datacollection.model.summary.CallSummary;
import com.tomrichardson.datacollection.model.summary.LocationSummary;
import com.tomrichardson.datacollection.model.summary.TextSummary;
import com.tomrichardson.datacollection.service.phone.PhoneService;
import com.tomrichardson.datacollection.service.textmessage.TextMessageService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import co.uk.rushorm.core.RushSearch;

/**
 * Created by tom on 07/02/2016.
 */
public class SummaryService extends Service {

  private static final String TAG = "SummaryService";

  private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());

  @Override
  public void onCreate() {
    Date today = Utils.getTodayDate();

    List<LocationSummary> locations = summariseLocations(today);
    CallSummary callSummary = summarisePhoneData(today);
    TextSummary textSummary = summariseSentiment(today);
    List<ActivitySummary> activities = summariseActivities(today);

    String date = dateFormat.format(today.getTime());
    SummaryModel model = new RushSearch().whereEqual("date", date).findSingle(SummaryModel.class);
    if (model == null) {
      model = new SummaryModel(dateFormat.format(today.getTime()), locations, activities, callSummary, textSummary);
    } else {
      if (model.locations != null) {
        for (LocationSummary location : model.locations) {
          location.delete();
        }
      }

      if (model.activities != null) {
        for (ActivitySummary activity : model.activities) {
          activity.delete();
        }
      }

      if (model.callSummary != null) {
        model.callSummary.delete();
      }

      if (model.textSummary != null) {
        model.textSummary.delete();
      }

      model.locations = locations;
      model.activities = activities;
      model.callSummary = callSummary;
      model.textSummary = textSummary;
    }

    model.save();
  }

  private List<LocationSummary> summariseLocations(Date today) {
    List<LocationModel> locations = new RushSearch().whereGreaterThan("date", today.getTime())
        .and().whereLessThan("date", today.getTime() + Utils.DAY_LENGTH_MS)
        .orderAsc("date")
        .find(LocationModel.class);

    Log.d(TAG, locations.size() + " locations found today");

    HashMap<String, Integer> locationFrequency = new HashMap<>();

    for (LocationModel location : locations) {
      if (locationFrequency.containsKey(location.placeName)) {
        locationFrequency.put(location.placeName, locationFrequency.get(location.placeName) + 1);
      } else {
        locationFrequency.put(location.placeName, 1);
      }
    }

    List<LocationSummary> frequencies = new ArrayList<>();

    for (String key : locationFrequency.keySet()) {
      frequencies.add(new LocationSummary(key, locationFrequency.get(key)));
    }

    Collections.sort(frequencies);
    return frequencies;
  }

  private List<ActivitySummary> summariseActivities(Date today) {
    List<ActivityModel> activities = new RushSearch().whereGreaterThan("time", today.getTime())
        .and().whereLessThan("time", today.getTime() + Utils.DAY_LENGTH_MS)
        .orderAsc("time")
        .find(ActivityModel.class);

    HashMap<Integer, Long> activityMap = new HashMap<>();

    for (int i = 0; i < activities.size() - 1; i++) {
      ActivityModel activity = activities.get(i);

      if (activityMap.containsKey(activity.activityType)) {
        activityMap.put(activity.activityType, activityMap.get(activity.activityType) + (activities.get(i + 1).time - activity.time));
      } else {
        activityMap.put(activity.activityType, activities.get(i + 1).time - activity.time);
      }
    }

    List<ActivitySummary> summary = new ArrayList<>();

    for (Integer type : activityMap.keySet()) {
      summary.add(new ActivitySummary(type, (int)(activityMap.get(type)/1000)));
    }

    Collections.sort(summary);

    return summary;
  }

  private CallSummary summarisePhoneData(Date today) {
    List<PhoneCallModel> calls = PhoneService.getPhoneCallData(this, today);

    int callCount = calls.size();
    String mostCalled = null;
    int mostCalledCount = 0;
    int timeSpent = 0;
    HashMap<String, Integer> callCountMap = new HashMap<>();

    for (PhoneCallModel call : calls) {
      timeSpent += call.duration;
      if (callCountMap.containsKey(call.number)) {
        callCountMap.put(call.number, callCountMap.get(call.number) + 1);
      } else {
        callCountMap.put(call.number, 1);
      }
    }

    for (String key : callCountMap.keySet()) {
      if (callCountMap.get(key) > mostCalledCount) {
        mostCalled = key;
        mostCalledCount = callCountMap.get(key);
      }
    }

    int hours = timeSpent / 3600;
    timeSpent -= hours * 3600;
    int minutes = timeSpent / 60;
    timeSpent -= minutes * 60;
    int seconds = timeSpent;
    String timeSpentStr = String.format("%02d:%02d:%02d", hours, minutes, seconds);

    return new CallSummary(callCount, mostCalled, timeSpentStr);
  }

  private TextSummary summariseSentiment(Date today) {
    return TextMessageService.getSentimentAnalysis(this, today);
  }

  private void summarisePhoneState() {

  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }
}
