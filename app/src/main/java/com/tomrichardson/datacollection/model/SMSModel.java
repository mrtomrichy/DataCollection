package com.tomrichardson.datacollection.model;

import java.util.Date;

/**
 * Created by tom on 14/12/2015.
 */
public class SMSModel {
  public String body;
  public Date date;

  public SMSModel(){}

  public SMSModel(String body, Date date) {
    this.body = body;
    this.date = date;
  }
}
