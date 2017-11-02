package com.nanoddegree.newsfeedapp;

import android.text.TextUtils;
import android.util.Log;

import com.nanoddegree.newsfeedapp.models.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


public final class QueryUtils {


  private static final String LOG_TAG = "Log: ";
  private static final int READ_TIMEOUT = 10000;
  private static final int SET_CONNECTION_TIMEOUT = 15000;

  private static final String KEY_RESPONSE = "response";
  private static final String KEY_RESULTS = "results";
  private static final String KEY_SECTION_NAME = "sectionName";
  private static final String KEY_WEB_PUBLICATION_DATE = "webPublicationDate";
  private static final String KEY_WEB_TITLE = "webTitle";

  private static URL createUrl(String stringUrl) {
    URL url = null;
    try {
      url = new URL(stringUrl);
    } catch (MalformedURLException e) {
      Log.e(LOG_TAG, "Problem building the URL ", e);
    }
    return url;
  }


  private static String makeHttpRequest(URL url) throws IOException {
    String jsonResponse = "";

    if (url == null) {
      return jsonResponse;
    }

    HttpURLConnection urlConnection = null;
    InputStream inputStream = null;
    try {
      urlConnection = (HttpURLConnection) url.openConnection();
      urlConnection.setReadTimeout(READ_TIMEOUT);
      urlConnection.setConnectTimeout(SET_CONNECTION_TIMEOUT);
      urlConnection.setRequestMethod("GET");
      urlConnection.connect();

      if (urlConnection.getResponseCode() == 200) {
        inputStream = urlConnection.getInputStream();
        jsonResponse = readFromStream(inputStream);
      } else {
        Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
      }
    } catch (IOException e) {
      Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
    } finally {
      if (urlConnection != null) {
        urlConnection.disconnect();
      }
      if (inputStream != null) {
        inputStream.close();
      }
    }
    return jsonResponse;
  }

  private static String readFromStream(InputStream inputStream) throws IOException {
    StringBuilder output = new StringBuilder();
    if (inputStream != null) {
      InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
      BufferedReader reader = new BufferedReader(inputStreamReader);
      String line = reader.readLine();
      while (line != null) {
        output.append(line);
        line = reader.readLine();
      }
    }
    return output.toString();
  }


  private static List<News> extractFeatureFromJson(String itemJSON) {

    if (TextUtils.isEmpty(itemJSON)) {
      return null;
    }

    List<News> newsList = new ArrayList<>();

    try {

      JSONObject baseJsonResponse = new JSONObject(itemJSON);

      JSONObject getNewsObject = baseJsonResponse.getJSONObject(KEY_RESPONSE);
      JSONArray newsArray = getNewsObject.optJSONArray(KEY_RESULTS);
      for (int i = 0; i < newsArray.length(); i++) {

        JSONObject currentNews = newsArray.getJSONObject(i);
        String sectionName = currentNews.getString(KEY_SECTION_NAME);
        String sectionPublicationDate = currentNews.getString(KEY_WEB_PUBLICATION_DATE);

        JSONArray tags = currentNews.getJSONArray("tags");
        String author = "";
        for (int j = 0; j < tags.length(); j++) {
          // Get a single news at position i within the list of news
          JSONObject currentTag = tags.getJSONObject(i);
          // In the tags the webtitle is the author of the news
          author = currentTag.optString("webTitle");
        }

        News newsObject = new News(sectionName, sectionPublicationDate, author);
        if (newsList != null)
          newsList.add(newsObject);
      }
    } catch (JSONException e) {
      Log.e("QueryUtils", "Problem parsing the news JSON results", e);
    }

    return newsList;
  }

  public static List<News> fetchBookData(String requestUrl) {
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    URL url = createUrl(requestUrl);

    String jsonResponse = null;
    try {
      jsonResponse = makeHttpRequest(url);
    } catch (IOException e) {
      Log.e(LOG_TAG, "Problem making the HTTP request.", e);
    }

    List<News> newsList = extractFeatureFromJson(jsonResponse);
    return newsList;
  }

}