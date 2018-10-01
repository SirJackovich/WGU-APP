package com.example.sirjackovich.wguapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.SimpleCursorAdapter;

public class CheckBoxAdapter extends SimpleCursorAdapter {
  private String itemID;
  private int layout;
  private int mentorFlag = 1;
  private int assessmentFlag = 2;
  private int courseFlag = 3;
  private int flag;

  public CheckBoxAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags, String itemID) {
    super(context, layout, c, from, to, flags);
    this.itemID = itemID;
    this.layout = layout;
    this.flag = flags;
  }

  @Override
  public View newView(Context context, Cursor cursor, ViewGroup parent) {
    return LayoutInflater.from(context).inflate(layout, parent, false);
  }

  @Override
  public void bindView(View view, Context context, Cursor cursor) {
    CheckedTextView checkbox = (CheckedTextView)view;
    if(this.itemID != null){
      String itemID = null;
      if(flag == mentorFlag){
        itemID = cursor.getString(cursor.getColumnIndex(DatabaseHelper.MENTOR_COURSE_ID));
      }
      if(flag == assessmentFlag){
        itemID = cursor.getString(cursor.getColumnIndex(DatabaseHelper.ASSESSMENT_COURSE_ID));
      }
      if(flag == courseFlag){
        itemID = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COURSE_TERM_ID));
      }
      if (this.itemID.equals(itemID)) {
        checkbox.setChecked(true);
      }
    }
    String name = null;
    if(flag == mentorFlag){
      name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.MENTOR_NAME));
    }
    if(flag == assessmentFlag){
      name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.ASSESSMENT_TITLE));
    }
    if(flag == courseFlag){
      name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COURSE_TITLE));
    }
    checkbox.setText(name);
  }
}