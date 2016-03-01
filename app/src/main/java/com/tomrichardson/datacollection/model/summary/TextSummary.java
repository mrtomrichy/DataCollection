package com.tomrichardson.datacollection.model.summary;

import co.uk.rushorm.core.RushObject;

/**
 * Created by tom on 01/03/2016.
 */
public class TextSummary extends RushObject {

  public int wordCount;
  public int negativeWordCount;
  public int positiveWordCount;

  public TextSummary() {}

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
}
