package com.example.sirjackovich.wguapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class CheckBoxAdapter extends SimpleCursorAdapter {
  private String courseID;
  private Context context;
  private int layout;

  public CheckBoxAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags, String courseID) {
    super(context, layout, c, from, to, flags);
    this.courseID = courseID;
    this.layout = layout;
    this.context = context;
  }

  @Override
  public View newView(Context context, Cursor cursor, ViewGroup parent) {
    return LayoutInflater.from(context).inflate(layout, parent, false);
  }

  @Override
  public void bindView(View view, Context context, Cursor cursor) {
    CheckedTextView checkbox = (CheckedTextView)view;
    if(this.courseID != null){
      String courseID = cursor.getString(cursor.getColumnIndex(DatabaseHelper.MENTOR_COURSE_ID));
      if (this.courseID.equals(courseID)) {
        checkbox.setChecked(true);
      }
    }
    String mentorName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.MENTOR_NAME));
    checkbox.setText(mentorName);
  }
}