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
import com.example.sirjackovich.wguapp.ItemProvider;
import com.example.sirjackovich.wguapp.R;

import java.util.ArrayList;

public class CourseDetailsActivity extends AppCompatActivity {
  private EditText title;
  private EditText startDate;
  private EditText endDate;
  private EditText status;
  private EditText note;
  private String action;
  private String courseFilter;
  private String courseID;
  private ArrayList<String> assessments;
  private ArrayList<String> mentors;

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
      assessments = new ArrayList<>();
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

  public void shareNote(View view) {
    Intent sendIntent = new Intent();
    sendIntent.setAction(Intent.ACTION_SEND);
    sendIntent.putExtra(Intent.EXTRA_TEXT, note.getText().toString().trim());
    sendIntent.setType("text/plain");
    startActivity(sendIntent);
  }

  public void handleDelete(View view) {
    switch (action) {
      case Intent.ACTION_INSERT:
        onBackPressed();
      case Intent.ACTION_EDIT:
        deleteCourse();
    }
  }

  public void handleMentors(View view) {
    Intent intent = new Intent(CourseDetailsActivity.this, ManageMentorsActivity.class);
    if (courseID != null) {
      Uri uri = Uri.parse(ItemProvider.COURSES_CONTENT_URI + "/" + courseID);
      intent.putExtra("Course", uri);
    }
    startActivityForResult(intent, 2);
  }

  public void handleAssessments(View view) {
    Intent intent = new Intent(CourseDetailsActivity.this, ManageAssessmentsActivity.class);
    if (courseID != null) {
      Uri uri = Uri.parse(ItemProvider.COURSES_CONTENT_URI + "/" + courseID);
      intent.putExtra("Course", uri);
    }
    startActivityForResult(intent, 1);
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
        addAssessments();
        addMentors();
        break;
      case Intent.ACTION_EDIT:
        updateCourse(values);
    }
    setResult(RESULT_OK);
    finish();
  }

  private void addAssessments() {
    for (int i = 0; i < assessments.size(); i++) {
      ContentValues values = new ContentValues();
      String assessmentFilter = DatabaseHelper.ASSESSMENT_ID + "=" + assessments.get(i);
      values.put(DatabaseHelper.ASSESSMENT_COURSE_ID, courseID);
      getContentResolver().update(ItemProvider.ASSESSMENTS_CONTENT_URI, values, assessmentFilter, null);
    }
  }

  private void addMentors() {
    for (int i = 0; i < mentors.size(); i++) {
      ContentValues values = new ContentValues();
      String mentorFilter = DatabaseHelper.MENTOR_ID + "=" + mentors.get(i);
      values.put(DatabaseHelper.MENTOR_COURSE_ID, courseID);
      getContentResolver().update(ItemProvider.MENTOR_CONTENT_URI, values, mentorFilter, null);
    }
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

  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if(action.equals(Intent.ACTION_INSERT)){
      if (requestCode == 1) {
        if (resultCode == RESULT_OK) {
          assessments = data.getStringArrayListExtra("Assessments");
        }
      }
      if (requestCode == 2) {
        if (resultCode == RESULT_OK) {
          mentors = data.getStringArrayListExtra("Mentors");
        }
      }
    }
  }
}
