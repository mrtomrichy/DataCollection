package com.tomrichardson.datacollection.ui.summary;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by tom on 16/03/2016.
 */
public abstract class SummaryView extends LinearLayout {
  public SummaryView(Context context) {
    super(context);
    init();
  }

  public SummaryView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public SummaryView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  @CallSuper
  protected void init() {
    inflate(getContext(), getLayoutId(), this);
  }

  public void setVisible(boolean visible) {
    setVisibility(visible ? View.VISIBLE : View.GONE);
  }

  protected abstract int getLayoutId();

}
