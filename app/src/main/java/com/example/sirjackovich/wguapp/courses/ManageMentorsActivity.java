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
import android.widget.CheckBox;
import android.widget.ListView;

import com.example.sirjackovich.wguapp.CheckBoxAdapter;
import com.example.sirjackovich.wguapp.DatabaseHelper;
import com.example.sirjackovich.wguapp.ItemProvider;
import com.example.sirjackovich.wguapp.R;

import java.util.ArrayList;
import java.util.List;

public class ManageMentorsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
  private int mentorFlag = 1;
  private String action;
  private List<Long> mentors;
  private String courseID;
  private String courseFilter;
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


    adapter = new CheckBoxAdapter(this, R.layout.list_item, null, from, to, mentorFlag, courseID);

    ListView listView = (ListView) findViewById(R.id.listView);

    listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

    listView.setAdapter(adapter);

    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (courseID != null) {
          CheckBox checkbox = (CheckBox) view.findViewById(R.id.checkBox);
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
          mentors.add(id);
        }
      }
    });

    getLoaderManager().initLoader(0, null, this);


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