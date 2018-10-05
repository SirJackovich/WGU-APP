package com.example.sirjackovich.wguapp.assessments;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.sirjackovich.wguapp.DatabaseHelper;
import com.example.sirjackovich.wguapp.ItemProvider;
import com.example.sirjackovich.wguapp.NotificationReceiver;
import com.example.sirjackovich.wguapp.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AssessmentDetailsActivity extends AppCompatActivity {
  private EditText title;
  private Spinner spinner;
  private EditText dueDate;
  private CheckBox checkBox;
  private String action;
  private String assessmentFilter;
  private String assessmentID;
  final DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_assessment_details);


    title = (EditText) findViewById(R.id.title_edit_text);
    dueDate = (EditText) findViewById(R.id.due_date_edit_text);
    spinner = (Spinner) findViewById(R.id.type_spinner);
    checkBox = (CheckBox) findViewById(R.id.due_date_alert_check_box);

    // Create an ArrayAdapter using the string array and a default spinner layout
    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
      R.array.assessment_types, android.R.layout.simple_spinner_item);
    // Specify the layout to use when the list of choices appears
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    // Apply the adapter to the spinner
    spinner.setAdapter(adapter);

    Intent intent = getIntent();

    Uri uri = intent.getParcelableExtra("Assessment");

    if (uri == null) {
      action = Intent.ACTION_INSERT;
      setTitle(getString(R.string.new_assessment));
    } else {
      action = Intent.ACTION_EDIT;
      assessmentID = uri.getLastPathSegment();
      assessmentFilter = DatabaseHelper.ASSESSMENT_ID + "=" + assessmentID;
      Cursor cursor = getContentResolver().query(uri, DatabaseHelper.ASSESSMENT_COLUMNS, assessmentFilter, null, null);
      if (cursor != null) {
        cursor.moveToFirst();
        title.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ASSESSMENT_TITLE)));
        spinner.setSelection(adapter.getPosition(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ASSESSMENT_TYPE))));
        dueDate.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ASSESSMENT_DUE_DATE)));
        if(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.ASSESSMENT_ALERT)) != 0){
          checkBox.setChecked(true);
        }
        cursor.close();
      }
    }
  }

  public void handleDelete(View view) {
    switch (action) {
      case Intent.ACTION_INSERT:
        onBackPressed();
      case Intent.ACTION_EDIT:
        deleteAssessment();
    }
  }

  private void deleteAssessment() {
    getContentResolver().delete(ItemProvider.ASSESSMENTS_CONTENT_URI, assessmentFilter, null);
    setResult(RESULT_OK);
    finish();
  }


  public boolean isDateValid(String date) {
    try {
      formatter.setLenient(false);
      formatter.parse(date);
      return true;
    } catch (ParseException e) {
      return false;
    }
  }

  public void handleSave(View view) throws ParseException {
    String dateString = dueDate.getText().toString().trim();
    if (isDateValid(dateString)) {
      ContentValues values = new ContentValues();
      values.put(DatabaseHelper.ASSESSMENT_TITLE, title.getText().toString().trim());
      values.put(DatabaseHelper.ASSESSMENT_TYPE, spinner.getSelectedItem().toString().trim());
      values.put(DatabaseHelper.ASSESSMENT_DUE_DATE, dateString);

      if (checkBox.isChecked()) {
        values.put(DatabaseHelper.ASSESSMENT_ALERT, 1);
      }else{
        values.put(DatabaseHelper.ASSESSMENT_ALERT, 0);
      }

      switch (action) {
        case Intent.ACTION_INSERT:
          insertAssessment(values);
          break;
        case Intent.ACTION_EDIT:
          updateAssessment(values);
      }

      if (checkBox.isChecked()) {
        Calendar calendar = Calendar.getInstance();
        Date date = formatter.parse(dateString);
        calendar.setTime(date);
        long time = calendar.getTimeInMillis();
        Intent intent = new Intent(AssessmentDetailsActivity.this, NotificationReceiver.class);
        Uri uri = Uri.parse(ItemProvider.ASSESSMENTS_CONTENT_URI + "/" + assessmentID);
        intent.putExtra("Assessment", uri);
        intent.putExtra("type", "Assessment");
        intent.setAction(Long.toString(System.currentTimeMillis()));
        PendingIntent sender = PendingIntent.getBroadcast(AssessmentDetailsActivity.this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, time, sender);
      }

      setResult(RESULT_OK);
      finish();
    } else {
      Toast.makeText(this, R.string.valid_date_string, Toast.LENGTH_LONG).show();
    }
  }

  private void updateAssessment(ContentValues values) {
    getContentResolver().update(ItemProvider.ASSESSMENTS_CONTENT_URI, values, assessmentFilter, null);
  }

  private void insertAssessment(ContentValues values) {
    Uri uri = getContentResolver().insert(ItemProvider.ASSESSMENTS_CONTENT_URI, values);
    if (uri != null) {
      assessmentID = uri.getLastPathSegment();
    }
  }
}
