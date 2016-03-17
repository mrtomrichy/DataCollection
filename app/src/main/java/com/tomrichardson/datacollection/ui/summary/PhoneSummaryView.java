package com.tomrichardson.datacollection.ui.summary;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.tomrichardson.datacollection.R;

/**
 * Created by tom on 17/03/2016.
 */
public class PhoneSummaryView extends SummaryView {

  private ImageView icon;
  private TextView timeText;
  private TextView freqText;
  private TextView numText;

  public PhoneSummaryView(Context context) {
    super(context);
  }

  public PhoneSummaryView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public PhoneSummaryView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  protected void init() {
    super.init();

    icon = (ImageView) findViewById(R.id.icon_text_icon);
    timeText = (TextView) findViewById(R.id.icon_text_text);
    freqText = (TextView) findViewById(R.id.freq_text);
    numText = (TextView) findViewById(R.id.numText);

    icon.setImageResource(R.drawable.ic_phone);
    icon.setColorFilter(Color.parseColor("#2196F3"));
  }

  public void setData(String time, int freq, String number) {
    timeText.setText(time);
    freqText.setText(String.format("Frequency: %d", freq));
    numText.setText(String.format("Most Called: %s", number));
  }

  @Override
  protected int getLayoutId() {
    return R.layout.summary_phone;
  }
}
