package com.nanoddegree.newsfeedapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nanoddegree.newsfeedapp.models.News;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

  public static final String LOG_TAG = MainActivity.class.getName();

  private TextView warningMessage;
  private ProgressBar progressBar;

  private static final int NEWS_LOADER_ID = 1;
  private static final int FETCH_NEWS = 15000;

  NewsAdapter newsAdapter;
  private LoaderManager loaderManager;
  ListView newsListView;

  private static String url =
      "https://content.guardianapis.com/search?q=android&show-tags=contributor&order-by=relevance&api-key=test";

  private Handler mHandler;
  private Runnable mRunnable = new Runnable() {
    @Override
    public void run() {
      updatNewsList();
      mHandler.postDelayed(mRunnable, FETCH_NEWS);
    }
  };


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    newsListView = (ListView) findViewById(R.id.list_news);

    warningMessage = (TextView) findViewById(R.id.warning_message);
    newsListView.setEmptyView(warningMessage);

    progressBar = (ProgressBar) findViewById(R.id.loading_spinner);

    newsAdapter = new NewsAdapter(this, new ArrayList<News>());
    newsListView.setAdapter(newsAdapter);

    if (checkConnectivity()) {
      loaderManager = getLoaderManager();
      loaderManager.initLoader(NEWS_LOADER_ID, null, this);
    } else {
      View loadingIndicator = findViewById(R.id.loading_spinner);
      loadingIndicator.setVisibility(View.GONE);
      warningMessage.setText(R.string.no_internet);
    }
  }

  private boolean checkConnectivity() {
    ConnectivityManager connMgr = (ConnectivityManager)
        getSystemService(Context.CONNECTIVITY_SERVICE);

    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

    return networkInfo != null && networkInfo.isConnected();
  }

  private void updatNewsList() {
    if (checkConnectivity()) {
      progressBar.setVisibility(View.VISIBLE);
      loaderManager = getLoaderManager();
      loaderManager.restartLoader(0, null, MainActivity.this);
    } else {
      progressBar.setVisibility(View.GONE);
      warningMessage.setText(R.string.no_internet);
    }
  }

  @Override
  public Loader<List<News>> onCreateLoader(int id, Bundle args) {
    return new NewsLoader(this, url);
  }

  @Override
  public void onLoadFinished(Loader<List<News>> loader, List<News> books) {
    progressBar.setVisibility(View.GONE);
    warningMessage.setText(R.string.no_news);
    newsAdapter.clear();
    if (books != null && !books.isEmpty()) {
      newsAdapter.addAll(books);
    }
  }

  @Override
  public void onLoaderReset(Loader<List<News>> loader) {
    newsAdapter.clear();
  }

}
