package com.tomrichardson.datacollection.model.service;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import com.tomrichardson.datacollection.service.ServiceUtils;

import co.uk.rushorm.core.RushSearch;

/**
 * Created by tom on 22/11/2015.
 */
public class DataServiceModel implements Parcelable {

  private Class serviceClass;
  private String[] permissions;
  private String name;
  private Class modelClass;

  public DataServiceModel(String name, Class serviceClass, String[] permissions, Class model, Context context) {
    this.serviceClass = serviceClass;
    this.name = name;
    this.permissions = permissions;
    this.modelClass = model;

    updateRunningWhenCreated(context);
  }

  protected DataServiceModel(Parcel in) {
    serviceClass = (Class) in.readSerializable();
    modelClass = (Class) in.readSerializable();
    name = in.readString();
    permissions = in.createStringArray();
  }

  private void updateRunningWhenCreated(Context context) {
    if(serviceClass != null) {
      ServiceState state = getServiceState();
      state.isRunning = ServiceUtils.isMyServiceRunning(context, serviceClass);
      state.save();
    }
  }

  public String getName() {
    return this.name;
  }

  public Class getServiceClass() {
    return this.serviceClass;
  }

  public Class getModelClass() {
    return this.modelClass;
  }

  private ServiceState getServiceState() {
    ServiceState state = new RushSearch().whereEqual("name", name).findSingle(ServiceState.class);

    if(state == null) {
      state = new ServiceState(name, false);
      state.save();
    }

    return state;
  }

  public String[] getRequiredPermissions() {
    return permissions;
  }

  public void startService(Context context) {
    ServiceState state = getServiceState();
    state.isRunning = true;
    state.save();

    if(serviceClass != null) {
      context.startService(new Intent(context, getServiceClass()));
    }
  }

  public void stopService(Context context) {
    ServiceState state = getServiceState();
    state.isRunning = false;
    state.save();

    if(serviceClass != null) {
      context.stopService(new Intent(context, getServiceClass()));
    }
  }

  public boolean isServiceRunning() {
    return getServiceState().isRunning;
  }

  /* Parcelable methods */

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeSerializable(serviceClass);
    dest.writeSerializable(modelClass);
    dest.writeString(name);
    dest.writeStringArray(permissions);
  }

  public static final Creator<DataServiceModel> CREATOR = new Creator<DataServiceModel>() {
    @Override
    public DataServiceModel createFromParcel(Parcel in) {
      return new DataServiceModel(in);
    }

    @Override
    public DataServiceModel[] newArray(int size) {
      return new DataServiceModel[size];
    }
  };
}
