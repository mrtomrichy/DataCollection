package com.tomrichardson.datacollection.model;

/**
 * Created by tom on 10/03/2016.
 */
public class CalendarDayModel {

  public int dayOfMonth;
  public boolean isEnabled;
  public boolean isToday;
  public SummaryModel model;

  public CalendarDayModel(int dayOfMonth, boolean isEnabled, boolean isToday, SummaryModel model) {
    this.dayOfMonth = dayOfMonth;
    this.isEnabled = isEnabled;
    this.isToday = isToday;
    this.model = model;
  }

  public boolean hasModel() {
    return this.model != null;
  }
}
