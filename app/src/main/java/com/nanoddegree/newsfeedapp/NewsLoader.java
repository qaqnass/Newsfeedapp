package com.nanoddegree.newsfeedapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.nanoddegree.newsfeedapp.models.News;

import java.util.List;


public class NewsLoader extends AsyncTaskLoader<List<News>> {

  private String mUrl;

  public NewsLoader(Context context, String url) {
    super(context);
    mUrl = url;
  }

  private static final String LOG_TAG = NewsLoader.class.getName();

  @Override
  protected void onStartLoading() {
    forceLoad();
  }

  /**
   * This is on a background thread.
   */
  @Override
  public List<News> loadInBackground() {
    if (mUrl == null) {
      return null;
    }

    List<News> newsList = QueryUtils.fetchBookData(mUrl);
    return newsList;
  }
}
