package com.example.sirjackovich.wguapp.courses;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.example.sirjackovich.wguapp.DatabaseHelper;
import com.example.sirjackovich.wguapp.R;

public class CourseDetailsActivity extends AppCompatActivity {
  private EditText title;
  private EditText startDate;
  private EditText endDate;
  private EditText status;
  private EditText note;
  private String action;
  private String courseFilter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_course_details);


    title = (EditText) findViewById(R.id.title_edit_text);
    startDate = (EditText) findViewById(R.id.start_date_edit_text);
    endDate = (EditText) findViewById(R.id.end_date_edit_text);
    status = (EditText) findViewById(R.id.status_edit_text);
    note = (EditText) findViewById(R.id.note_edit_text);

    Intent intent = getIntent();

    Uri uri = intent.getParcelableExtra(CoursesProvider.CONTENT_ITEM_TYPE);

    if (uri == null) {
      action = Intent.ACTION_INSERT;
      setTitle(getString(R.string.new_course_text));
    } else {
      action = Intent.ACTION_EDIT;
      courseFilter = DatabaseHelper.COURSE_ID + "=" + uri.getLastPathSegment();
      Cursor cursor = getContentResolver().query(uri, DatabaseHelper.COURSE_COLUMNS, courseFilter, null, null);
      cursor.moveToFirst();
      title.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COURSE_TITLE)));
      startDate.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COURSE_START_DATE)));
      endDate.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COURSE_END_DATE)));
      status.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COURSE_STATUS)));
      note.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COURSE_NOTE)));
    }
  }

  public void handleDelete(View view) {
    deleteCourse();
  }

  private void deleteCourse() {
    getContentResolver().delete(CoursesProvider.CONTENT_URI, courseFilter, null);
    setResult(RESULT_OK);
    finish();
  }

  public void handleSave(View view) {
    switch (action) {
      case Intent.ACTION_INSERT:
        insertCourse();
        break;
      case Intent.ACTION_EDIT:
        updateCourse();
    }
    finish();
  }

  private void updateCourse() {
    ContentValues values = new ContentValues();
    values.put(DatabaseHelper.COURSE_TITLE, title.getText().toString().trim());
    values.put(DatabaseHelper.COURSE_START_DATE, startDate.getText().toString().trim());
    values.put(DatabaseHelper.COURSE_END_DATE, endDate.getText().toString().trim());
    values.put(DatabaseHelper.COURSE_STATUS, status.getText().toString().trim());
    values.put(DatabaseHelper.COURSE_NOTE, note.getText().toString().trim());
    getContentResolver().update(CoursesProvider.CONTENT_URI, values, courseFilter, null);
    setResult(RESULT_OK);
  }

  private void insertCourse() {
    ContentValues values = new ContentValues();
    values.put(DatabaseHelper.COURSE_TITLE, title.getText().toString().trim());
    values.put(DatabaseHelper.COURSE_START_DATE, startDate.getText().toString().trim());
    values.put(DatabaseHelper.COURSE_END_DATE, endDate.getText().toString().trim());
    values.put(DatabaseHelper.COURSE_STATUS, status.getText().toString().trim());
    values.put(DatabaseHelper.COURSE_NOTE, note.getText().toString().trim());
    getContentResolver().insert(CoursesProvider.CONTENT_URI, values);
    setResult(RESULT_OK);
  }
}
