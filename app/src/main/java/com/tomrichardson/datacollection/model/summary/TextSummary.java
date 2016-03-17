package com.tomrichardson.datacollection.model.summary;

import android.os.Parcel;
import android.os.Parcelable;

import co.uk.rushorm.core.RushObject;

/**
 * Created by tom on 01/03/2016.
 */
public class TextSummary extends RushObject implements Parcelable {

  public int wordCount;
  public int negativeWordCount;
  public int positiveWordCount;

  public TextSummary() {
  }

  public TextSummary(int wordCount, int negativeWordCount, int positiveWordCount) {
    this.wordCount = wordCount;
    this.negativeWordCount = negativeWordCount;
    this.positiveWordCount = positiveWordCount;
  }

  @Override
  public String toString() {
    return "[wordCount: " + this.wordCount + ", "
        + "negativeWordCount: " + this.negativeWordCount + ", "
        + "positiveWordCount: " + this.positiveWordCount + "]";
  }

  // Parcelable methods

  protected TextSummary(Parcel in) {
    wordCount = in.readInt();
    negativeWordCount = in.readInt();
    positiveWordCount = in.readInt();
  }

  public static final Creator<TextSummary> CREATOR = new Creator<TextSummary>() {
    @Override
    public TextSummary createFromParcel(Parcel in) {
      return new TextSummary(in);
    }

    @Override
    public TextSummary[] newArray(int size) {
      return new TextSummary[size];
    }
  };

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(wordCount);
    dest.writeInt(negativeWordCount);
    dest.writeInt(positiveWordCount);
  }
}
