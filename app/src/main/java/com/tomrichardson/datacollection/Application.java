package com.tomrichardson.datacollection;

import com.tomrichardson.datacollection.model.ActivityModel;
import com.tomrichardson.datacollection.model.LocationModel;

import java.util.ArrayList;
import java.util.List;

import co.uk.rushorm.android.AndroidInitializeConfig;
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
    config.setClasses(classes);
    RushCore.initialize(config);
  }
}
