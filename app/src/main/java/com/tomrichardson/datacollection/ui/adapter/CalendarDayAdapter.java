package com.tomrichardson.datacollection.ui.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tomrichardson.datacollection.R;
import com.tomrichardson.datacollection.model.CalendarDayModel;

import java.util.List;

/**
 * Created by tom on 10/03/2016.
 */
public class CalendarDayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private static final int HEADER = 0;
  private static final int DAY = 1;

  private Activity context;
  private List<CalendarDayModel> models;
  private String[] dayTitles = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

  public interface OnDayClickedListener {
    void onDayClicked(CalendarDayModel model);
  }

  private OnDayClickedListener listener;

  public CalendarDayAdapter(Activity context, List<CalendarDayModel> models, OnDayClickedListener listener) {
    this.context = context;
    this.models = models;
    this.listener = listener;
  }

  @Override
  public int getItemViewType(int position) {
    if (position < 7) {
      return HEADER;
    } else {
      return DAY;
    }
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view;
    if (viewType == HEADER) {
      view = LayoutInflater.from(context).inflate(R.layout.list_calendar_title, parent, false);
      return new HeaderViewHolder(view);
    } else {
      view = LayoutInflater.from(context).inflate(R.layout.list_calendar_day, parent, false);
      return new DayViewHolder(view);
    }
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder vholder, int position) {

    if (position < 7) {
      HeaderViewHolder holder = (HeaderViewHolder) vholder;
      holder.dayName.setText(dayTitles[position]);
    } else {
      DayViewHolder holder = (DayViewHolder) vholder;
      final CalendarDayModel model = models.get(position - 7);

      holder.day.setText(String.format("%d", model.dayOfMonth));

      if (model.isEnabled) {
        holder.day.setTextColor(Color.parseColor("#76000000"));
      } else {
        holder.day.setTextColor(Color.parseColor("#40000000"));
      }

      if (model.hasModel()) {
        holder.itemView.setBackgroundResource(R.drawable.triangle_corner);
        holder.itemView.setElevation(8);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            if(listener != null){
              listener.onDayClicked(model);
            }
          }
        });
      } else {
        holder.itemView.setOnClickListener(null);
        if (model.isToday) {
          holder.itemView.setBackgroundResource(R.drawable.today_highlight);
          holder.itemView.setElevation(8);
          holder.day.setTextColor(Color.parseColor("#FFFFFF"));
        } else {
          holder.itemView.setBackgroundResource(0);
          holder.itemView.setElevation(0);
        }
      }
    }
  }

  @Override
  public int getItemCount() {
    return models.size() + 7;
  }

  public static class DayViewHolder extends RecyclerView.ViewHolder {
    TextView day;

    public DayViewHolder(View itemView) {
      super(itemView);

      this.day = (TextView) itemView.findViewById(R.id.day_textview);
    }
  }

  public static class HeaderViewHolder extends RecyclerView.ViewHolder {
    TextView dayName;

    public HeaderViewHolder(View itemView) {
      super(itemView);

      this.dayName = (TextView) itemView.findViewById(R.id.title_textview);
    }
  }
}
