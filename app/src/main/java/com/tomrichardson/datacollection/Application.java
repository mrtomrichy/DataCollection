package com.tomrichardson.datacollection;

import android.util.Log;

import com.tomrichardson.datacollection.model.ActivityModel;
import com.tomrichardson.datacollection.model.LocationModel;
import com.tomrichardson.datacollection.model.ScreenStateModel;
import com.tomrichardson.datacollection.model.SummaryModel;
import com.tomrichardson.datacollection.model.service.StaticServiceModel;
import com.tomrichardson.datacollection.model.summary.ActivitySummary;
import com.tomrichardson.datacollection.model.summary.CallSummary;
import com.tomrichardson.datacollection.model.summary.LocationSummary;
import com.tomrichardson.datacollection.model.summary.TextSummary;

import java.util.ArrayList;
import java.util.List;

import co.uk.rushorm.android.AndroidInitializeConfig;
import co.uk.rushorm.core.InitializeListener;
import co.uk.rushorm.core.Rush;
import co.uk.rushorm.core.RushCore;

/**
 * Created by tom on 18/11/2015.
 */
public class Application extends android.app.Application {
  @Override
  public void onCreate() {
    super.onCreate();

    AndroidInitializeConfig config = new AndroidInitializeConfig(getApplicationContext());

    // Turn off encryption for now for testing purposes
    //config.setRushStatementRunner(new AndroidRushStatementRunnerSQLCipher(getApplicationContext(), "DataCollection", config.getRushConfig()));

    List<Class<? extends Rush>> classes = new ArrayList<>();
    classes.add(LocationModel.class);
    classes.add(ActivityModel.class);
    classes.add(ScreenStateModel.class);
    classes.add(StaticServiceModel.ServiceState.class);
    classes.add(SummaryModel.class);
    classes.add(LocationSummary.class);
    classes.add(CallSummary.class);
    classes.add(TextSummary.class);
    classes.add(ActivitySummary.class);
    config.setClasses(classes);

    config.setInitializeListener(new InitializeListener() {
      @Override
      public void initialized(boolean b) {
        Log.d("APP", "Init complete");
      }
    });
    RushCore.initialize(config);
  }
}
