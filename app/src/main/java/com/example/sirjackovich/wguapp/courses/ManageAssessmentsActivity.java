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

public class ManageAssessmentsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
  private int assessmentFlag = 2;
  private String action;
  private List<Long> assessments;
  private String courseID;
  private String courseFilter;
  private CheckBoxAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_manage_assessments);

    Intent intent = getIntent();

    Uri uri = intent.getParcelableExtra("Course");

    if (uri == null) {
      action = Intent.ACTION_INSERT;
      assessments = new ArrayList<>();
      setTitle(getString(R.string.add_assessments_text));
    } else {
      action = Intent.ACTION_EDIT;
      courseID = uri.getLastPathSegment();
      setTitle(getString(R.string.edit_assessments_text));
    }

    String[] from = {DatabaseHelper.ASSESSMENT_TITLE};
    int[] to = {android.R.id.text1};


    adapter = new CheckBoxAdapter(this, R.layout.list_item, null, from, to, assessmentFlag, courseID);

    ListView listView = (ListView) findViewById(R.id.listView);

    listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

    listView.setAdapter(adapter);

    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (courseID != null) {
          CheckBox checkbox = (CheckBox) view.findViewById(R.id.checkBox);
          ContentValues values = new ContentValues();
          String filter = DatabaseHelper.ASSESSMENT_ID + "=" + id;
          if (checkbox.isChecked()) {
            values.put(DatabaseHelper.ASSESSMENT_COURSE_ID, courseID);
            getContentResolver().update(ItemProvider.ASSESSMENTS_CONTENT_URI, values, filter, null);
          } else {
            values.put(DatabaseHelper.ASSESSMENT_COURSE_ID, "");
            getContentResolver().update(ItemProvider.ASSESSMENTS_CONTENT_URI, values, filter, null);
          }
        } else {
          assessments.add(id);
        }
      }
    });

    getLoaderManager().initLoader(0, null, this);


  }

//  private void handleSave(View view) {
//    for (int i = 0; i < assessments.size(); i++) {
//      ContentValues values = new ContentValues();
//      String assessmentFilter = DatabaseHelper.ASSESSMENT_ID + "=" + assessments.get(i);
//      values.put(DatabaseHelper.ASSESSMENT_COURSE_ID, courseID);
//      getContentResolver().update(ItemProvider.ASSESSMENTS_CONTENT_URI, values, assessmentFilter, null);
//    }
//  }

  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    return new CursorLoader(this, ItemProvider.ASSESSMENTS_CONTENT_URI, null, null, null, null);
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
