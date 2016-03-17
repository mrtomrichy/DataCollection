package com.tomrichardson.datacollection.service.summary;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.Pair;

import com.google.android.gms.maps.model.LatLng;
import com.tomrichardson.datacollection.Utils;
import com.tomrichardson.datacollection.model.ActivityModel;
import com.tomrichardson.datacollection.model.LocationModel;
import com.tomrichardson.datacollection.model.PhoneCallModel;
import com.tomrichardson.datacollection.model.SummaryModel;
import com.tomrichardson.datacollection.model.service.RunnableService;
import com.tomrichardson.datacollection.model.summary.ActivitySummary;
import com.tomrichardson.datacollection.model.summary.CallSummary;
import com.tomrichardson.datacollection.model.summary.LocationSummary;
import com.tomrichardson.datacollection.model.summary.TextSummary;
import com.tomrichardson.datacollection.service.Services;
import com.tomrichardson.datacollection.service.activity.ActivityService;
import com.tomrichardson.datacollection.service.location.LocationService;
import com.tomrichardson.datacollection.service.phone.PhoneService;
import com.tomrichardson.datacollection.service.textmessage.TextMessageService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import co.uk.rushorm.core.RushSearch;

/**
 * Created by tom on 07/02/2016.
 */
public class SummaryService {

  private static final String TAG = "SummaryService";

  public static void summarise(Context context) {
    Date today = Utils.getTodayDate();


    List<LocationSummary> locations = isServiceEnabled(LocationService.class, context) ? summariseLocations(today) : null;
    CallSummary callSummary = isServiceEnabled(PhoneService.class, context) ? summarisePhoneData(today, context) : null;
    TextSummary textSummary = isServiceEnabled(TextMessageService.class, context) ? summariseSentiment(today, context) : null;
    List<ActivitySummary> activities = isServiceEnabled(ActivityService.class, context) ? summariseActivities(today) : null;

    String date = SummaryModel.getDateString(today);
    SummaryModel model = new RushSearch().whereEqual("date", date).findSingle(SummaryModel.class);
    if (model == null) {
      model = new SummaryModel(SummaryModel.getDateString(today), locations, activities, callSummary, textSummary);
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
    context.sendBroadcast(new Intent(SummaryModel.class.getName()));
  }

  private static boolean isServiceEnabled(Class serviceClass, Context context) {
    for(RunnableService service : Services.getInstance().getSupportedDataServices()) {
      if(serviceClass.equals(service.getServiceClass())) {
        return service.isRunning(context);
      }
    }

    return false;
  }

  private static List<LocationSummary> summariseLocations(Date today) {
    List<LocationModel> locations = new RushSearch().whereGreaterThan("date", today.getTime())
        .and().whereLessThan("date", today.getTime() + Utils.DAY_LENGTH_MS)
        .orderAsc("date")
        .find(LocationModel.class);

    Log.d(TAG, locations.size() + " locations found today");

    HashMap<String, Pair<Integer, LatLng>> locationFrequency = new HashMap<>();

    for (LocationModel location : locations) {
      if (locationFrequency.containsKey(location.placeName)) {
        locationFrequency.put(location.placeName, new Pair<>(locationFrequency.get(location.placeName).first + 1, locationFrequency.get(location.placeName).second));
      } else {
        locationFrequency.put(location.placeName, new Pair<>(1, new LatLng(location.latitude, location.longitude)));
      }
    }

    List<LocationSummary> frequencies = new ArrayList<>();

    for (String key : locationFrequency.keySet()) {
      frequencies.add(new LocationSummary(key, locationFrequency.get(key).first.intValue(), locationFrequency.get(key).second.latitude, locationFrequency.get(key).second.longitude));
    }

    Collections.sort(frequencies);
    return frequencies;
  }

  private static List<ActivitySummary> summariseActivities(Date today) {
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

  private static CallSummary summarisePhoneData(Date today, Context context) {
    List<PhoneCallModel> calls = PhoneService.getPhoneCallData(context, today);

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

  private static TextSummary summariseSentiment(Date today, Context context) {
    return TextMessageService.getSentimentAnalysis(context, today);
  }

  private static void summarisePhoneState() {

  }
}
