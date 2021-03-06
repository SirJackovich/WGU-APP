package com.example.sirjackovich.wguapp;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class CheckBoxAdapter extends SimpleCursorAdapter {
  private String itemID;
  private int layout;
  private int flag;
  ListView listView;

  public CheckBoxAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags, String itemID) {
    super(context, layout, c, from, to, flags);
    this.itemID = itemID;
    this.layout = layout;
    this.flag = flags;
    this.listView = (ListView) ((AppCompatActivity) context).findViewById(R.id.listView);
  }

  @Override
  public View newView(Context context, Cursor cursor, ViewGroup parent) {
    return LayoutInflater.from(context).inflate(layout, parent, false);
  }

  @Override
  public void bindView(View view, Context context, Cursor cursor) {
    CheckedTextView checkbox = (CheckedTextView) view;
    int mentorFlag = 1;
    int assessmentFlag = 2;
    int courseFlag = 3;
    String assessment_id = cursor.getString(cursor.getColumnIndex(DatabaseHelper.ASSESSMENT_ID));
    if (this.itemID != null && this.listView != null) {
      String itemID = null;
      if (flag == mentorFlag) {
        itemID = cursor.getString(cursor.getColumnIndex(DatabaseHelper.MENTOR_COURSE_ID));
      }
      if (flag == assessmentFlag) {
        itemID = cursor.getString(cursor.getColumnIndex(DatabaseHelper.ASSESSMENT_COURSE_ID));
      }
      if (flag == courseFlag) {
        itemID = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COURSE_TERM_ID));
      }
      if (this.itemID.equals(itemID)) {
        checkbox.setChecked(true);
        this.listView.setItemChecked(cursor.getPosition(), true);
      }
    }
    String name = null;
    if (flag == mentorFlag) {
      name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.MENTOR_NAME));
    }
    if (flag == assessmentFlag) {
      name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.ASSESSMENT_TITLE));
    }
    if (flag == courseFlag) {
      name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COURSE_TITLE));
    }
    checkbox.setText(name);
  }
}