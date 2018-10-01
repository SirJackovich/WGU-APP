package com.example.sirjackovich.wguapp.courses;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.sirjackovich.wguapp.CheckBoxAdapter;
import com.example.sirjackovich.wguapp.DatabaseHelper;
import com.example.sirjackovich.wguapp.ItemProvider;
import com.example.sirjackovich.wguapp.R;

import java.util.ArrayList;
import java.util.List;

public class CourseDetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
  private EditText title;
  private EditText startDate;
  private EditText endDate;
  private EditText status;
  private EditText note;
  private String action;
  private String courseFilter;
  private CursorAdapter cursorAdapter;
  private String courseID;
  private List<Long> mentors;

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

    Uri uri = intent.getParcelableExtra("Course");

    if (uri == null) {
      action = Intent.ACTION_INSERT;
      setTitle(getString(R.string.new_course_text));
      mentors = new ArrayList<>();
    } else {
      action = Intent.ACTION_EDIT;
      courseID = uri.getLastPathSegment();
      courseFilter = DatabaseHelper.COURSE_ID + "=" + courseID;
      Cursor cursor = getContentResolver().query(uri, DatabaseHelper.COURSE_COLUMNS, courseFilter, null, null);
      if (cursor != null) {
        cursor.moveToFirst();
        title.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COURSE_TITLE)));
        startDate.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COURSE_START_DATE)));
        endDate.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COURSE_END_DATE)));
        status.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COURSE_STATUS)));
        note.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COURSE_NOTE)));
        cursor.close();
      }

    }


    String[] from = {DatabaseHelper.MENTOR_NAME};
    int[] to = {android.R.id.text1};

    cursorAdapter = new CheckBoxAdapter(this, android.R.layout.simple_list_item_multiple_choice, null, from, to, 0, courseID);

    ListView listView = (ListView) findViewById(R.id.listView);

    listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

    listView.setAdapter(cursorAdapter);

    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(courseID != null){
          CheckedTextView checkbox = (CheckedTextView) view;
          ContentValues values = new ContentValues();
          String mentorFilter = DatabaseHelper.MENTOR_ID + "=" + id;
          if (checkbox.isChecked()) {
            values.put(DatabaseHelper.MENTOR_COURSE_ID, courseID);
            getContentResolver().update(ItemProvider.MENTOR_CONTENT_URI, values, mentorFilter, null);
          } else {
            values.put(DatabaseHelper.MENTOR_COURSE_ID, "");
            getContentResolver().update(ItemProvider.MENTOR_CONTENT_URI, values, mentorFilter, null);
          }
        }else{
          mentors.add(id);
        }
      }
    });

    getLoaderManager().initLoader(0, null, this);
  }

  public void handleDelete(View view) {
    switch (action) {
      case Intent.ACTION_INSERT:
        onBackPressed();
      case Intent.ACTION_EDIT:
        deleteCourse();
    }
  }

  private void deleteCourse() {
    getContentResolver().delete(ItemProvider.COURSES_CONTENT_URI, courseFilter, null);
    setResult(RESULT_OK);
    finish();
  }

  public void handleSave(View view) {
    ContentValues values = new ContentValues();
    values.put(DatabaseHelper.COURSE_TITLE, title.getText().toString().trim());
    values.put(DatabaseHelper.COURSE_START_DATE, startDate.getText().toString().trim());
    values.put(DatabaseHelper.COURSE_END_DATE, endDate.getText().toString().trim());
    values.put(DatabaseHelper.COURSE_STATUS, status.getText().toString().trim());
    values.put(DatabaseHelper.COURSE_NOTE, note.getText().toString().trim());
    switch (action) {
      case Intent.ACTION_INSERT:
        courseID = insertCourse(values);
        updateMentors();
        break;
      case Intent.ACTION_EDIT:
        updateCourse(values);
    }
    setResult(RESULT_OK);
    finish();
  }

  private void updateCourse(ContentValues values) {
    getContentResolver().update(ItemProvider.COURSES_CONTENT_URI, values, courseFilter, null);
  }

  private String insertCourse(ContentValues values) {
    Uri uri = getContentResolver().insert(ItemProvider.COURSES_CONTENT_URI, values);
    if (uri != null) {
      return uri.getLastPathSegment();
    }
    return "";
  }

  private void updateMentors() {
    for (int i = 0; i < mentors.size(); i++) {
      ContentValues values = new ContentValues();
      String mentorFilter = DatabaseHelper.MENTOR_ID + "=" + mentors.get(i);
      values.put(DatabaseHelper.MENTOR_COURSE_ID, courseID);
      getContentResolver().update(ItemProvider.MENTOR_CONTENT_URI, values, mentorFilter, null);
    }
  }

  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    return new CursorLoader(this, ItemProvider.MENTOR_CONTENT_URI, null, null, null, null);
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    cursorAdapter.swapCursor(data);
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
    cursorAdapter.swapCursor(null);
  }
}
