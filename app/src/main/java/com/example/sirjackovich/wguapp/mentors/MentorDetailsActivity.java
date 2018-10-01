package com.example.sirjackovich.wguapp.mentors;

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

public class MentorDetailsActivity extends AppCompatActivity {
  private EditText name;
  private EditText phone;
  private EditText email;
  private String action;
  private String mentorFilter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_mentor_details);


    name = (EditText) findViewById(R.id.name_edit_text);
    phone = (EditText) findViewById(R.id.phone_edit_text);
    email = (EditText) findViewById(R.id.email_edit_text);

    Intent intent = getIntent();

    Uri uri = intent.getParcelableExtra("Mentor");

    if (uri == null) {
      action = Intent.ACTION_INSERT;
      setTitle(getString(R.string.new_mentor));
    } else {
      action = Intent.ACTION_EDIT;
      mentorFilter = DatabaseHelper.MENTOR_ID + "=" + uri.getLastPathSegment();
      Cursor cursor = getContentResolver().query(uri, DatabaseHelper.MENTOR_COLUMNS, mentorFilter, null, null);
      if (cursor != null) {
        cursor.moveToFirst();
        name.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.MENTOR_NAME)));
        phone.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.MENTOR_PHONE)));
        email.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.MENTOR_EMAIL)));
        cursor.close();
      }
    }
  }

  public void handleDelete(View view) {
    switch (action) {
      case Intent.ACTION_INSERT:
        onBackPressed();
      case Intent.ACTION_EDIT:
        deleteMentor();
    }
  }

  private void deleteMentor() {
    getContentResolver().delete(ItemProvider.MENTOR_CONTENT_URI, mentorFilter, null);
    setResult(RESULT_OK);
    finish();
  }

  public void handleSave(View view) {
    ContentValues values = new ContentValues();
    values.put(DatabaseHelper.MENTOR_NAME, name.getText().toString().trim());
    values.put(DatabaseHelper.MENTOR_PHONE, phone.getText().toString().trim());
    values.put(DatabaseHelper.MENTOR_EMAIL, email.getText().toString().trim());
    switch (action) {
      case Intent.ACTION_INSERT:
        insertMentor(values);
        break;
      case Intent.ACTION_EDIT:
        updateMentor(values);
    }
    finish();
  }

  private void updateMentor(ContentValues values) {
    getContentResolver().update(ItemProvider.MENTOR_CONTENT_URI, values, mentorFilter, null);
    setResult(RESULT_OK);
  }

  private void insertMentor(ContentValues values) {
    getContentResolver().insert(ItemProvider.MENTOR_CONTENT_URI, values);
    setResult(RESULT_OK);
  }
}
