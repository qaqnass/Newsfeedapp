package com.nanoddegree.newsfeedapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nanoddegree.newsfeedapp.models.News;

import java.util.ArrayList;

/**
 * Created by qaqnass on 11/10/17.
 */

public class NewsAdapter extends ArrayAdapter<News> {

  public NewsAdapter(Context context, ArrayList<News> arrayList) {
    super(context, 0, arrayList);
  }

  @NonNull
  @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    View listItemView = convertView;

    if (listItemView == null) {
      listItemView = LayoutInflater.from(getContext()).inflate(
          R.layout.list_news, parent, false);
    }

    News news = getItem(position);

    TextView sectionName = (TextView) listItemView.findViewById(R.id.sectionName);
    sectionName.setText(news.getSectionName());

    TextView publicationDate = (TextView) listItemView.findViewById(R.id.publicationDate);
    publicationDate.setText(news.getPublicationDate());

    TextView title = (TextView) listItemView.findViewById(R.id.webTitle);
    title.setText(news.getTitle());

    return listItemView;
  }
}
