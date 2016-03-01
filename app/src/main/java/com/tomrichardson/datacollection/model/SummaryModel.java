package com.tomrichardson.datacollection.model;

import com.tomrichardson.datacollection.model.summary.ActivitySummary;
import com.tomrichardson.datacollection.model.summary.CallSummary;
import com.tomrichardson.datacollection.model.summary.LocationSummary;
import com.tomrichardson.datacollection.model.summary.TextSummary;

import java.util.List;

import co.uk.rushorm.core.RushObject;
import co.uk.rushorm.core.annotations.RushList;

/**
 * Created by tom on 27/02/2016.
 */
public class SummaryModel extends RushObject {

  @RushList(classType = LocationSummary.class)
  public List<LocationSummary> locations;

  @RushList(classType = ActivitySummary.class)
  public List<ActivitySummary> activities;

  public CallSummary callSummary;

  public TextSummary textSummary;

  public String date;

  public SummaryModel(){}

  public SummaryModel(String date, List<LocationSummary> locations, List<ActivitySummary> activities, CallSummary callSummary, TextSummary textSummary) {
    this.date = date;
    this.locations = locations;
    this.activities = activities;
    this.callSummary = callSummary;
    this.textSummary = textSummary;
  }
}
