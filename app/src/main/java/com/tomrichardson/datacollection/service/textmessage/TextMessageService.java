package com.tomrichardson.datacollection.service.textmessage;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.provider.Telephony;
import android.support.annotation.Nullable;
import android.util.Log;

import com.tomrichardson.datacollection.R;
import com.tomrichardson.datacollection.model.SMSModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by tom on 14/12/2015.
 */
public class TextMessageService extends Service {

  private static final String TAG = "SMSService";

  public static void getSentimentAnalysis(Context context) {
    long startTime = System.currentTimeMillis();

    String[] positiveWords = context.getResources().getStringArray(R.array.positive_words);
    String[] negativeWords = context.getResources().getStringArray(R.array.negative_words);

    HashMap<String, Integer> positiveMap = new HashMap<>();
    HashMap<String, Integer> negativeMap = new HashMap<>();

    for (String word : positiveWords) positiveMap.put(word, 0);
    for (String word : negativeWords) negativeMap.put(word, 0);

    List<SMSModel> sentSMS = getSentMessages(context);

    Log.d(TAG, "SMS count: " + sentSMS.size());
    int totalWordCount = 0;

    List<String> positiveMatches = new ArrayList<>();
    List<String> negativeMatches = new ArrayList<>();

    for (SMSModel sms : sentSMS) {
      String[] words = sms.body.split("\\W+");
      totalWordCount += words.length;
      for (String s : words) {
        if (positiveMap.containsKey(s)) positiveMatches.add(s);
        else if (negativeMap.containsKey(s)) negativeMatches.add(s);
      }
    }

    long timeTaken = System.currentTimeMillis() - startTime;

    Log.d(TAG, "Positive words matched: " + positiveMatches.size() + "\nNegative words matched: " + negativeMatches.size()
        + "\nof " + totalWordCount + " total");
    Log.d(TAG, "Time to process: " + timeTaken + "ms");

    String positiveMatchesString = "";
    String negativeMatchesString = "";

    for (String s : positiveMatches) positiveMatchesString += s + "; ";
    for (String s : negativeMatches) negativeMatchesString += s + "; ";

    Log.d(TAG, "Positive: " + positiveMatchesString);
    Log.d(TAG, "Negative: " + negativeMatchesString);
  }

  private static List<SMSModel> getSentMessages(Context context) {
    List<SMSModel> sentSMS = new ArrayList<>();
    ContentResolver cr = context.getContentResolver();

    Cursor c = cr.query(Telephony.Sms.Sent.CONTENT_URI,
        new String[]{Telephony.Sms.Sent.BODY, Telephony.Sms.Sent.DATE},
        null,
        null,
        Telephony.Sms.Sent.DATE);

    int totalSMS = c.getCount();

    if (c.moveToFirst()) {
      for (int i = 0; i < totalSMS; i++) {
        sentSMS.add(new SMSModel(c.getString(0), new Date(c.getLong(1))));
        c.moveToNext();
      }
    } else {
      Log.d(TAG, "You have no SMS in Inbox");
    }

    c.close();

    return sentSMS;
  }

  @Override
  public void onCreate() {
    getSentimentAnalysis(this);
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }
}
