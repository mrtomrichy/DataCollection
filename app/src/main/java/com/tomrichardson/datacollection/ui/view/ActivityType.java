package com.tomrichardson.datacollection.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tomrichardson.datacollection.R;

/**
 * Created by tom on 16/03/2016.
 */
public class ActivityType extends FrameLayout {

  private TextView time;
  private ImageView icon;

  public ActivityType(Context context) {
    super(context);
    init();
  }

  public ActivityType(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public ActivityType(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    inflate(getContext(), R.layout.view_icon_text, this);
    time = (TextView) findViewById(R.id.icon_text_text);
    icon = (ImageView) findViewById(R.id.icon_text_icon);
  }

  public void setTime(int time) {
    int hours = time / 3600;
    int remainder = time - (hours * 3600);
    int mins = remainder / 60;

    if(hours == 0 && mins == 0) {
      this.time.setText(String.format("%ds", time ));
    } else {
      this.time.setText(String.format("%d:%2d", hours, mins));
    }
  }

  public void setIcon(int icon) {
    this.icon.setImageResource(icon);
    this.icon.setBackgroundResource(icon);
  }

  public void setVisible(boolean visible) {
    setVisibility(visible ? View.VISIBLE : View.GONE);
  }
}
