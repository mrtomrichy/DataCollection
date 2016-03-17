package com.tomrichardson.datacollection.ui.summary;

import android.content.Context;
import android.util.AttributeSet;

import com.tomrichardson.datacollection.R;
import com.tomrichardson.datacollection.model.summary.ActivitySummary;
import com.tomrichardson.datacollection.ui.view.ActivityType;

/**
 * Created by tom on 16/03/2016.
 */
public class ActivitySummaryView extends SummaryView {

  private ActivityType walkingView;
  private ActivityType runningView;
  private ActivityType vehicleView;
  private ActivityType bicycleView;

  public ActivitySummaryView(Context context) {
    super(context);
  }

  public ActivitySummaryView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public ActivitySummaryView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  protected void init() {
    super.init();

    walkingView = (ActivityType) findViewById(R.id.activity_walking);
    runningView = (ActivityType) findViewById(R.id.activity_running);
    vehicleView = (ActivityType) findViewById(R.id.activity_vehicle);
    bicycleView = (ActivityType) findViewById(R.id.activity_bicycle);
  }

  public void setTimes(ActivitySummary walking, ActivitySummary running, ActivitySummary vehicle, ActivitySummary bicycle) {
    if (walking != null) {
      walkingView.setTime(walking.totalTime);
      walkingView.setIcon(R.drawable.ic_walking);
    } else {
      walkingView.setVisible(false);
    }

    if (running != null) {
      runningView.setTime(running.totalTime);
      runningView.setIcon(R.drawable.ic_running);
    } else {
      runningView.setVisible(false);
    }

    if (vehicle != null) {
      vehicleView.setTime(vehicle.totalTime);
      vehicleView.setIcon(R.drawable.ic_vehicle);
    } else {
      vehicleView.setVisible(false);
    }

    if (bicycle != null) {
      bicycleView.setTime(bicycle.totalTime);
      bicycleView.setIcon(R.drawable.ic_bicycle);
    } else {
      bicycleView.setVisible(false);
    }
  }

  @Override
  protected int getLayoutId() {
    return R.layout.summary_activity;
  }

}
