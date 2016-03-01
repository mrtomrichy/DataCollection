package com.tomrichardson.datacollection.model.service;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.util.Log;

import com.tomrichardson.datacollection.service.ServiceUtils;

/**
 * Created by tom on 22/11/2015.
 */
public class RunnableServiceModel extends RunnableService {

  private Class modelClass;

  public RunnableServiceModel(String name, Class serviceClass, String[] permissions, Class model) {
    super(name, serviceClass, permissions);
    this.modelClass = model;
  }

  public RunnableServiceModel(Parcel in) {
    super(in);
    this.modelClass =  (Class) in.readSerializable();
    Log.d("MODEL", this.modelClass.getName());
  }

  public Class getModelClass() {
    return this.modelClass;
  }

  @Override
  public void enable(Context context) {
    context.startService(new Intent(context, getServiceClass()));
  }

  @Override
  public void disable(Context context) {
    context.stopService(new Intent(context, getServiceClass()));
  }

  @Override
  public boolean isRunning(Context context) {
    return ServiceUtils.isMyServiceRunning(context, getServiceClass());
  }

  /* Parcelable methods */

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    dest.writeSerializable(modelClass);
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
