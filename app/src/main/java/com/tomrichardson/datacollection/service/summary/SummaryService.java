package com.tomrichardson.datacollection.service.summary;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by tom on 07/02/2016.
 */
public class SummaryService extends IntentService {

  private static final String TAG = "SummaryService";

  public SummaryService() {
    super(TAG);
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    // This will run every night at 1PM (times could adjust). It will collect a summary
    // of the data collected from that day and store it in a fresh table which will be
    // shown in the UI to the user.
  }
}
