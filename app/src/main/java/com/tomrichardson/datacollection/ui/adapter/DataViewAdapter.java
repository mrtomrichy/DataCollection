package com.tomrichardson.datacollection.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tomrichardson.datacollection.R;

import java.lang.reflect.Field;
import java.util.List;

import co.uk.rushorm.core.RushObject;

/**
 * Created by tom on 23/11/2015.
 */
public class DataViewAdapter extends RecyclerView.Adapter<DataViewAdapter.ViewHolder> {

  private Context context;
  private List<RushObject> objects;
  private List<Field> fields;

  public DataViewAdapter(Context context, List<RushObject> objects, List<Field> fields) {
    this.context = context;
    this.objects = objects;
    this.fields = fields;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(context).inflate(R.layout.list_dataview, parent, false);
    return new ViewHolder(v);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    RushObject object = objects.get(position);

    String outputString = "";

    for(Field f : fields) {
      try {
        outputString += "<b>" + f.getName() + ":</b> " + getObjectOutput(f.get(object));
        if(fields.indexOf(f) < fields.size()-1) {
          outputString += "<br/>";
        }
      } catch(IllegalAccessException ex) {
        ex.printStackTrace();
      }
    }

    holder.dataOutput.setText(Html.fromHtml(outputString));
  }

  private String getObjectOutput(Object o) {
    String output;
    if(o instanceof List) {
      List l = (List) o;
      output = "[";
      for(Object obj : l) {
        output += getObjectOutput(obj) + ", ";
      }
      output = output.substring(0, output.length() - 2);
      output += "]";
    } else {
      output = o == null ? "null" : o.toString();
    }

    return output;
  }

  @Override
  public int getItemCount() {
    return objects.size();
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {

    public TextView dataOutput;

    public ViewHolder(View itemView) {
      super(itemView);

      dataOutput = (TextView) itemView.findViewById(R.id.dataoutput);
    }
  }
}
