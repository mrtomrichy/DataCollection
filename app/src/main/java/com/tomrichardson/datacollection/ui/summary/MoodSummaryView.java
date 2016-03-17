package com.tomrichardson.datacollection.ui.summary;

import android.content.Context;
import android.util.AttributeSet;

import com.tomrichardson.datacollection.R;
import com.tomrichardson.datacollection.ui.view.MoodType;

/**
 * Created by tom on 17/03/2016.
 */
public class MoodSummaryView extends SummaryView {

  private MoodType positive;
  private MoodType negative;

  public MoodSummaryView(Context context) {
    super(context);
  }

  public MoodSummaryView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public MoodSummaryView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public void init() {
    super.init();

    positive = (MoodType) findViewById(R.id.positive_mood);
    positive.setIcon(R.drawable.ic_positive);
    positive.setIconTint("#00E676");
    negative = (MoodType) findViewById(R.id.negative_mood);
    negative.setIcon(R.drawable.ic_negative);
    negative.setIconTint("#F44336");
  }

  public void setData(int positiveCount, int negativeCount) {
    positive.setPercentage(calcPercentage(positiveCount, positiveCount + negativeCount));
    negative.setPercentage(calcPercentage(negativeCount, positiveCount + negativeCount));
  }

  private int calcPercentage(int amount, int total) {
    return (int) (((double) amount / (double) total) * 100.0);
  }

  @Override
  protected int getLayoutId() {
    return R.layout.summary_mood;
  }
}
