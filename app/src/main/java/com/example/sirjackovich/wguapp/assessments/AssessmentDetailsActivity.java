package com.example.sirjackovich.wguapp.assessments;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.sirjackovich.wguapp.DatabaseHelper;
import com.example.sirjackovich.wguapp.R;

public class AssessmentDetailsActivity extends AppCompatActivity {
  private EditText title;
  private Spinner spinner;
  private EditText dueDate;
  private String action;
  private String assessmentFilter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_assessment_details);


    title = (EditText) findViewById(R.id.title_edit_text);
    dueDate = (EditText) findViewById(R.id.due_date_edit_text);
    spinner = (Spinner) findViewById(R.id.type_spinner);

    // Create an ArrayAdapter using the string array and a default spinner layout
    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
      R.array.assessment_types, android.R.layout.simple_spinner_item);
    // Specify the layout to use when the list of choices appears
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    // Apply the adapter to the spinner
    spinner.setAdapter(adapter);

    Intent intent = getIntent();

    Uri uri = intent.getParcelableExtra(AssessmentsProvider.CONTENT_ITEM_TYPE);

    if (uri == null) {
      action = Intent.ACTION_INSERT;
      setTitle(getString(R.string.new_assessment));
    } else {
      action = Intent.ACTION_EDIT;
      assessmentFilter = DatabaseHelper.ASSESSMENT_ID + "=" + uri.getLastPathSegment();
      Cursor cursor = getContentResolver().query(uri, DatabaseHelper.ASSESSMENT_COLUMNS, assessmentFilter, null, null);
      cursor.moveToFirst();
      title.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ASSESSMENT_TITLE)));
      spinner.setSelection(adapter.getPosition(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ASSESSMENT_TYPE))));
      dueDate.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ASSESSMENT_DUE_DATE)));
    }
  }

  public void handleDelete(View view) {
    deleteAssessment();
  }

  private void deleteAssessment() {
    getContentResolver().delete(AssessmentsProvider.CONTENT_URI, assessmentFilter, null);
    setResult(RESULT_OK);
    finish();
  }

  public void handleSave(View view) {
    switch (action) {
      case Intent.ACTION_INSERT:
        insertAssessment();
        break;
      case Intent.ACTION_EDIT:
        updateAssessment();
    }
    finish();
  }

  private void updateAssessment() {
    ContentValues values = new ContentValues();
    values.put(DatabaseHelper.ASSESSMENT_TITLE, title.getText().toString().trim());
    values.put(DatabaseHelper.ASSESSMENT_TYPE, spinner.getSelectedItem().toString().trim());
    values.put(DatabaseHelper.ASSESSMENT_DUE_DATE, dueDate.getText().toString().trim());
    getContentResolver().update(AssessmentsProvider.CONTENT_URI, values, assessmentFilter, null);
    setResult(RESULT_OK);
  }

  private void insertAssessment() {
    ContentValues values = new ContentValues();
    values.put(DatabaseHelper.ASSESSMENT_TITLE, title.getText().toString().trim());
    values.put(DatabaseHelper.ASSESSMENT_TYPE, spinner.getSelectedItem().toString().trim());
    values.put(DatabaseHelper.ASSESSMENT_DUE_DATE, dueDate.getText().toString().trim());
    getContentResolver().insert(AssessmentsProvider.CONTENT_URI, values);
    setResult(RESULT_OK);
  }
}
