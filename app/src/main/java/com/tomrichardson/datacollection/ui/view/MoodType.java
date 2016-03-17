package com.tomrichardson.datacollection.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tomrichardson.datacollection.R;

/**
 * Created by tom on 17/03/2016.
 */
public class MoodType extends FrameLayout {
  private TextView text;
  private ImageView icon;

  public MoodType(Context context) {
    super(context);
    init();
  }

  public MoodType(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public MoodType(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    inflate(getContext(), R.layout.view_icon_text, this);
    text = (TextView) findViewById(R.id.icon_text_text);
    icon = (ImageView) findViewById(R.id.icon_text_icon);
  }

  public void setPercentage(int percentage) {
    text.setText(String.format("%d%%", percentage));
  }

  public void setIcon(int icon) {
    this.icon.setImageResource(icon);
  }

  public void setIconTint(String color) {
    this.icon.setColorFilter(Color.parseColor(color));
  }

  public void setVisible(boolean visible) {
    setVisibility(visible ? View.VISIBLE : View.GONE);
  }
}
