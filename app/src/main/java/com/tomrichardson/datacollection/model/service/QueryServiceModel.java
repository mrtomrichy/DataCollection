package com.tomrichardson.datacollection.model.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.util.Log;

/**
 * Created by tom on 29/02/2016.
 */
public class QueryServiceModel extends RunnableService {

  private static final String SHARED_PREFS = "QUERY_SERVICE_PREFERENCES";

  public QueryServiceModel(String name, Class clazz, String[] permissions) {
    super(name, clazz, permissions);
  }

  @Override
  public void enable(Context context) {
    Log.d("QUERY", "Enabling " + getServiceClass().getName());
    context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE).edit()
        .putBoolean(getServiceClass().getName(), true).apply();
  }

  @Override
  public void disable(Context context) {
    Log.d("QUERY", "Disabling " + getServiceClass().getName());
    context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE).edit()
        .putBoolean(getServiceClass().getName(), false).apply();
  }

  @Override
  public boolean isRunning(Context context) {
    SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
    boolean result = prefs.getBoolean(getServiceClass().getName(), false);

    Log.d("QUERY", "Result " + result + " - " + getServiceClass().getName());

    return result;
  }

  public static final Creator<RunnableServiceModel> CREATOR = new Creator<RunnableServiceModel>() {
    @Override
    public RunnableServiceModel createFromParcel(Parcel in) {
      return new RunnableServiceModel(in);
    }

    @Override
    public RunnableServiceModel[] newArray(int size) {
      return new RunnableServiceModel[size];
    }
  };
}
