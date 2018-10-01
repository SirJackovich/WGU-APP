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
import android.widget.SimpleCursorAdapter;

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
  private CursorAdapter mentorCursorAdapter;
  private SimpleCursorAdapter assessmentCursorAdapter;
  private String courseID;
  private List<Long> mentors;
  private List<Long> assessments;
  private int mentorFlag = 1;
  private int assessmentFlag = 2;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_course_details);

    initializeFields();

    initializeMentorList();

    initializeAssessmentList();

  }

  private void initializeFields() {
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
  }

  private void initializeMentorList() {
    String[] mentorFrom = {DatabaseHelper.MENTOR_NAME};
    int[] mentorTo = {android.R.id.text1};

    mentorCursorAdapter = new CheckBoxAdapter(this, android.R.layout.simple_list_item_multiple_choice, null, mentorFrom, mentorTo, mentorFlag, courseID);

    ListView mentorListView = (ListView) findViewById(R.id.mentor_list_view);

    mentorListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

    mentorListView.setAdapter(mentorCursorAdapter);

    mentorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (courseID != null) {
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
        } else {
          mentors.add(id);
        }
      }
    });

    getLoaderManager().initLoader(mentorFlag, null, this);
  }

  private void initializeAssessmentList() {
    // TODO: combine this and initializeMentor list into one function
    String[] assessmentFrom = {DatabaseHelper.ASSESSMENT_TITLE};
    int[] assessmentTo = {android.R.id.text1};

    assessmentCursorAdapter = new CheckBoxAdapter(this, android.R.layout.simple_list_item_multiple_choice, null, assessmentFrom, assessmentTo, assessmentFlag, courseID);

    ListView assessmentListView = (ListView) findViewById(R.id.assessment_list_view);

    assessmentListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

    assessmentListView.setAdapter(assessmentCursorAdapter);

    assessmentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(courseID != null){
          CheckedTextView checkbox = (CheckedTextView) view;
          ContentValues values = new ContentValues();
          String assessmentFilter = DatabaseHelper.ASSESSMENT_ID + "=" + id;
          if (checkbox.isChecked()) {
            values.put(DatabaseHelper.ASSESSMENT_COURSE_ID, courseID);
            getContentResolver().update(ItemProvider.ASSESSMENTS_CONTENT_URI, values, assessmentFilter, null);
          } else {
            values.put(DatabaseHelper.ASSESSMENT_COURSE_ID, "");
            getContentResolver().update(ItemProvider.ASSESSMENTS_CONTENT_URI, values, assessmentFilter, null);
          }
        }else{
          assessments.add(id);
        }
      }
    });

    getLoaderManager().initLoader(assessmentFlag, null, this);
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
        updateAssessments();
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

  private void updateAssessments() {
    // TODO: combine this with updateMentors
    for (int i = 0; i < assessments.size(); i++) {
      ContentValues values = new ContentValues();
      String assessmentFilter = DatabaseHelper.ASSESSMENT_ID + "=" + assessments.get(i);
      values.put(DatabaseHelper.ASSESSMENT_COURSE_ID, courseID);
      getContentResolver().update(ItemProvider.ASSESSMENTS_CONTENT_URI, values, assessmentFilter, null);
    }
  }

  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    if(id == assessmentFlag){
      return new CursorLoader(this, ItemProvider.ASSESSMENTS_CONTENT_URI, null, null, null, null);
    }
    if(id == mentorFlag){
      return new CursorLoader(this, ItemProvider.MENTOR_CONTENT_URI, null, null, null, null);
    }
    return null;
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    int id = loader.getId();
    if(id == assessmentFlag){
      assessmentCursorAdapter.swapCursor(data);
    }
    if(id == mentorFlag){
      mentorCursorAdapter.swapCursor(data);
    }
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
    int id = loader.getId();
    if(id == assessmentFlag){
      assessmentCursorAdapter.swapCursor(null);
    }
    if(id == mentorFlag){
      mentorCursorAdapter.swapCursor(null);
    }
  }
}
