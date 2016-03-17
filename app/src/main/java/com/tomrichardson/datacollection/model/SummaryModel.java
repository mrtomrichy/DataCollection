package com.tomrichardson.datacollection.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tomrichardson.datacollection.model.summary.ActivitySummary;
import com.tomrichardson.datacollection.model.summary.CallSummary;
import com.tomrichardson.datacollection.model.summary.LocationSummary;
import com.tomrichardson.datacollection.model.summary.TextSummary;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import co.uk.rushorm.core.RushObject;
import co.uk.rushorm.core.annotations.RushIgnore;
import co.uk.rushorm.core.annotations.RushList;

/**
 * Created by tom on 27/02/2016.
 */
public class SummaryModel extends RushObject implements Parcelable {

  @RushList(classType = LocationSummary.class)
  public List<LocationSummary> locations;

  @RushList(classType = ActivitySummary.class)
  public List<ActivitySummary> activities;

  public CallSummary callSummary;

  public TextSummary textSummary;

  public String date;

  @RushIgnore
  private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());

  public SummaryModel() {
  }

  public SummaryModel(String date, List<LocationSummary> locations, List<ActivitySummary> activities, CallSummary callSummary, TextSummary textSummary) {
    this.date = date;
    this.locations = locations;
    this.activities = activities;
    this.callSummary = callSummary;
    this.textSummary = textSummary;
  }

  public static String getDateString(Date date) {
    return dateFormat.format(date.getTime());
  }

  public Calendar getDate() {
    try{
      Calendar c = Calendar.getInstance();
      c.setTime(dateFormat.parse(date));
      return c;
    } catch(ParseException e) {
      return null;
    }
  }

  // Parcelable methods

  protected SummaryModel(Parcel in) {
    locations = in.createTypedArrayList(LocationSummary.CREATOR);
    activities = in.createTypedArrayList(ActivitySummary.CREATOR);
    callSummary = in.readParcelable(CallSummary.class.getClassLoader());
    textSummary = in.readParcelable(TextSummary.class.getClassLoader());
    date = in.readString();
  }

  public static final Creator<SummaryModel> CREATOR = new Creator<SummaryModel>() {
    @Override
    public SummaryModel createFromParcel(Parcel in) {
      return new SummaryModel(in);
    }

    @Override
    public SummaryModel[] newArray(int size) {
      return new SummaryModel[size];
    }
  };

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeTypedList(locations);
    dest.writeTypedList(activities);
    dest.writeParcelable(callSummary, flags);
    dest.writeParcelable(textSummary, flags);
    dest.writeString(date);
  }
}
