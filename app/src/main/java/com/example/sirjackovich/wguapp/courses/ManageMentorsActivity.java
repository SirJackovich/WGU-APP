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
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.ListView;

import com.example.sirjackovich.wguapp.CheckBoxAdapter;
import com.example.sirjackovich.wguapp.DatabaseHelper;
import com.example.sirjackovich.wguapp.ItemProvider;
import com.example.sirjackovich.wguapp.R;

import java.util.ArrayList;

public class ManageMentorsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
  private String action;
  private ArrayList<String> mentors;
  private String courseID;
  private CheckBoxAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_manage_mentors);

    Intent intent = getIntent();

    Uri uri = intent.getParcelableExtra("Course");

    if (uri == null) {
      action = Intent.ACTION_INSERT;
      mentors = new ArrayList<>();
      setTitle(getString(R.string.add_mentors_text));
    } else {
      setTitle(getString(R.string.edit_mentors_text));
      action = Intent.ACTION_EDIT;
      courseID = uri.getLastPathSegment();
    }

    String[] from = {DatabaseHelper.MENTOR_NAME};
    int[] to = {android.R.id.text1};

    ListView listView = (ListView) findViewById(R.id.listView);

    adapter = new CheckBoxAdapter(this, android.R.layout.simple_list_item_multiple_choice, null, from, to, 1, courseID);

    listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

    listView.setAdapter(adapter);

    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CheckedTextView checkbox = (CheckedTextView) view;
        if (courseID != null) {
          ContentValues values = new ContentValues();
          String filter = DatabaseHelper.MENTOR_ID + "=" + id;
          if (checkbox.isChecked()) {
            values.put(DatabaseHelper.MENTOR_COURSE_ID, courseID);
            getContentResolver().update(ItemProvider.MENTOR_CONTENT_URI, values, filter, null);
          } else {
            values.put(DatabaseHelper.MENTOR_COURSE_ID, "");
            getContentResolver().update(ItemProvider.MENTOR_CONTENT_URI, values, filter, null);
          }
        } else {
          if (checkbox.isChecked()) {
            mentors.add(Long.toString(id));
          }else{
            mentors.remove(Long.toString(id));
          }
        }
      }
    });

    getLoaderManager().initLoader(0, null, this);
  }

  public void handleSave(View view) {
    switch (action) {
      case Intent.ACTION_INSERT:
        Intent intent = new Intent();
        intent.putStringArrayListExtra("Mentors", mentors);
        setResult(RESULT_OK, intent);
        finish();
      case Intent.ACTION_EDIT:
        setResult(RESULT_OK);
        finish();
    }
  }

  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    return new CursorLoader(this, ItemProvider.MENTOR_CONTENT_URI, null, null, null, null);
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    adapter.swapCursor(data);
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
    adapter.swapCursor(null);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch(item.getItemId()) {
      case android.R.id.home:
        onBackPressed();
        return true;
      default:
        break;
    }
    return super.onOptionsItemSelected(item);
  }
}
