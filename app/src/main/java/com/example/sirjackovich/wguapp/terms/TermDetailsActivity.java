package com.example.sirjackovich.wguapp.terms;

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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sirjackovich.wguapp.CheckBoxAdapter;
import com.example.sirjackovich.wguapp.DatabaseHelper;
import com.example.sirjackovich.wguapp.ItemProvider;
import com.example.sirjackovich.wguapp.R;
import com.example.sirjackovich.wguapp.courses.ManageAssessmentsActivity;

import java.util.ArrayList;
import java.util.List;

public class TermDetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
  private EditText title;
  private EditText startDate;
  private EditText endDate;
  private String action;
  private String termFilter;
  private CheckBoxAdapter courseAdapter;
  private String termID;
  private ArrayList<String> courses;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_term_details);

    title = (EditText) findViewById(R.id.title_edit_text);
    startDate = (EditText) findViewById(R.id.start_date_edit_text);
    endDate = (EditText) findViewById(R.id.end_date_edit_text);

    Intent intent = getIntent();

    Uri uri = intent.getParcelableExtra("Term");

    if (uri == null) {
      action = Intent.ACTION_INSERT;
      setTitle(getString(R.string.new_term_text));
      courses = new ArrayList<>();
    } else {
      action = Intent.ACTION_EDIT;
      termID = uri.getLastPathSegment();
      termFilter = DatabaseHelper.TERM_ID + "=" + termID;
      Cursor cursor = getContentResolver().query(uri, DatabaseHelper.TERM_COLUMNS, termFilter, null, null);
      if (cursor != null) {
        cursor.moveToFirst();
        title.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TERM_TITLE)));
        startDate.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TERM_START_DATE)));
        endDate.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TERM_END_DATE)));
        cursor.close();
      }
    }
  }

  public void handleDelete(View view) {
    switch (action) {
      case Intent.ACTION_INSERT:
        onBackPressed();
      case Intent.ACTION_EDIT:
        deleteTerm();
    }
  }

  private void deleteTerm() {
    String[] courseTermIDs = {DatabaseHelper.COURSE_TERM_ID};
    String filter = DatabaseHelper.COURSE_TERM_ID + "=" + termID;
    Cursor cursor = getContentResolver().query(ItemProvider.COURSES_CONTENT_URI, courseTermIDs, filter, null, null);
    if (cursor != null) {
      if (cursor.moveToFirst()){
        Toast.makeText(TermDetailsActivity.this, R.string.delete_term_text, Toast.LENGTH_LONG).show();
        cursor.close();
      }else{
        getContentResolver().delete(ItemProvider.TERMS_CONTENT_URI, termFilter, null);
        setResult(RESULT_OK);
        finish();
      }
    }
  }

  public void handleSave(View view) {
    ContentValues values = new ContentValues();
    values.put(DatabaseHelper.TERM_TITLE, title.getText().toString().trim());
    values.put(DatabaseHelper.TERM_START_DATE, startDate.getText().toString().trim());
    values.put(DatabaseHelper.TERM_END_DATE, endDate.getText().toString().trim());
    switch (action) {
      case Intent.ACTION_INSERT:
        termID = insertTerm(values);
        updateCourses();
        break;
      case Intent.ACTION_EDIT:
        updateTerm(values);
    }
    setResult(RESULT_OK);
    finish();
  }

  private void updateTerm(ContentValues values) {
    getContentResolver().update(ItemProvider.TERMS_CONTENT_URI, values, termFilter, null);
  }

  private String insertTerm(ContentValues values) {
    Uri uri = getContentResolver().insert(ItemProvider.TERMS_CONTENT_URI, values);
    if (uri != null) {
      return uri.getLastPathSegment();
    }
    return "";
  }

  private void updateCourses() {
    for (int i = 0; i < courses.size(); i++) {
      ContentValues values = new ContentValues();
      String courseFilter = DatabaseHelper.COURSE_ID + "=" + courses.get(i);
      values.put(DatabaseHelper.COURSE_TERM_ID, termID);
      getContentResolver().update(ItemProvider.COURSES_CONTENT_URI, values, courseFilter, null);
    }
  }

  public void handleCourses(View view) {
    Intent intent = new Intent(TermDetailsActivity.this, ManageCoursesActivity.class);
    if (termID != null) {
      Uri uri = Uri.parse(ItemProvider.TERMS_CONTENT_URI + "/" + termID);
      intent.putExtra("Term", uri);
    }
    startActivityForResult(intent, 1);
  }

  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    return new CursorLoader(this, ItemProvider.COURSES_CONTENT_URI, null, null, null, null);
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    courseAdapter.swapCursor(data);
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
    courseAdapter.swapCursor(null);
  }

  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if(action.equals(Intent.ACTION_INSERT)){
      if (requestCode == 1) {
        if (resultCode == RESULT_OK) {
          courses = data.getStringArrayListExtra("Courses");
        }
      }
    }
  }
}
