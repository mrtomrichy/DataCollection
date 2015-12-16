package com.tomrichardson.datacollection.service.textmessage;

import android.app.Service;
import android.content.ContentResolver;
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


  @Override
  public void onCreate() {
    super.onCreate();

    long startTime = System.currentTimeMillis();

    String[] positiveWords = this.getResources().getStringArray(R.array.positive_words);
    String[] negativeWords = this.getResources().getStringArray(R.array.negative_words);

    HashMap<String, Integer> positiveMap = new HashMap<>();
    HashMap<String, Integer> negativeMap = new HashMap<>();

    for(String word : positiveWords) positiveMap.put(word, 0);
    for(String word : negativeWords) negativeMap.put(word, 0);

    List<SMSModel> sentSMS = getSentMessages();
    int positive = 0, negative = 0;

    Log.d(TAG, "SMS count: " + sentSMS.size());
    int totalWordCount = 0;

    for(SMSModel sms : sentSMS) {
      String[] words = sms.body.split("\\W+");
      totalWordCount += words.length;
      for(String s : words) {
        if(positiveMap.containsKey(s))      positive++;
        else if(negativeMap.containsKey(s)) negative++;
      }
    }

    long timeTaken = System.currentTimeMillis() - startTime;

    Log.d(TAG, "Positive words matched: " + positive + "\nNegative words matched: " + negative
                + "\nof " + totalWordCount + " total");
    Log.d(TAG, "Time to process: " + timeTaken + "ms");
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
  }

  private List<SMSModel> getSentMessages() {
    List<SMSModel> sentSMS = new ArrayList<>();
    ContentResolver cr = this.getContentResolver();

    Cursor c = cr.query(Telephony.Sms.Sent.CONTENT_URI,
        new String[] { Telephony.Sms.Sent.BODY, Telephony.Sms.Sent.DATE },
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
}
