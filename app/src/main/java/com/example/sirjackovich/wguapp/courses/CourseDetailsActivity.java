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
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.sirjackovich.wguapp.DatabaseHelper;
import com.example.sirjackovich.wguapp.ItemProvider;
import com.example.sirjackovich.wguapp.R;

public class CourseDetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
  private EditText title;
  private EditText startDate;
  private EditText endDate;
  private EditText status;
  private EditText note;
  private String action;
  private String courseFilter;
  private CursorAdapter cursorAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_course_details);

    String[] from = {DatabaseHelper.MENTOR_NAME};
    int[] to = {android.R.id.text1};

    cursorAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, null, from, to, 0);

    ListView listView = (ListView) findViewById(R.id.listView);

    listView.setAdapter(cursorAdapter);

//    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//      @Override
//      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Intent intent = new Intent(CoursesActivity.this, CourseDetailsActivity.class);
//        Uri uri = Uri.parse(ItemProvider.COURSES_CONTENT_URI + "/" + id);
//        intent.putExtra("Course", uri);
//        startActivityForResult(intent, 1);
//      }
//    });

    getLoaderManager().initLoader(0, null, this);

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
    } else {
      action = Intent.ACTION_EDIT;
      courseFilter = DatabaseHelper.COURSE_ID + "=" + uri.getLastPathSegment();
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

//    listView.setOnTouchListener(new View.OnTouchListener() {
//      // Setting on Touch Listener for handling the touch inside ScrollView
//      @Override
//      public boolean onTouch(View v, MotionEvent event) {
//        // Disallow the touch request for parent scroll on touch of child view
//        v.getParent().requestDisallowInterceptTouchEvent(true);
//        return false;
//      }
//    });
//
//    setListViewHeightBasedOnChildren(listView);
  }

  public void handleDelete(View view) {
    deleteCourse();
  }

  private void deleteCourse() {
    getContentResolver().delete(ItemProvider.COURSES_CONTENT_URI, courseFilter, null);
    setResult(RESULT_OK);
    finish();
  }

  public void handleSave(View view) {
    switch (action) {
      case Intent.ACTION_INSERT:
        insertCourse();
        break;
      case Intent.ACTION_EDIT:
        updateCourse();
    }
    finish();
  }

  private void updateCourse() {
    ContentValues values = new ContentValues();
    values.put(DatabaseHelper.COURSE_TITLE, title.getText().toString().trim());
    values.put(DatabaseHelper.COURSE_START_DATE, startDate.getText().toString().trim());
    values.put(DatabaseHelper.COURSE_END_DATE, endDate.getText().toString().trim());
    values.put(DatabaseHelper.COURSE_STATUS, status.getText().toString().trim());
    values.put(DatabaseHelper.COURSE_NOTE, note.getText().toString().trim());
    getContentResolver().update(ItemProvider.COURSES_CONTENT_URI, values, courseFilter, null);
    setResult(RESULT_OK);
  }

  private void insertCourse() {
    ContentValues values = new ContentValues();
    values.put(DatabaseHelper.COURSE_TITLE, title.getText().toString().trim());
    values.put(DatabaseHelper.COURSE_START_DATE, startDate.getText().toString().trim());
    values.put(DatabaseHelper.COURSE_END_DATE, endDate.getText().toString().trim());
    values.put(DatabaseHelper.COURSE_STATUS, status.getText().toString().trim());
    values.put(DatabaseHelper.COURSE_NOTE, note.getText().toString().trim());
    getContentResolver().insert(ItemProvider.COURSES_CONTENT_URI, values);
    setResult(RESULT_OK);
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



//  /**** Method for Setting the Height of the ListView dynamically.
//   **** Hack to fix the issue of not showing all the items of the ListView
//   **** when placed inside a ScrollView  ****/
//  public static void setListViewHeightBasedOnChildren(ListView listView) {
//    ListAdapter listAdapter = listView.getAdapter();
//    if (listAdapter == null)
//      return;
//
//    int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
//    int totalHeight = 0;
//    View view = null;
//    for (int i = 0; i < listAdapter.getCount(); i++) {
//      view = listAdapter.getView(i, view, listView);
//      if (i == 0)
//        view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, RadioGroup.LayoutParams.WRAP_CONTENT));
//
//      view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
//      totalHeight += view.getMeasuredHeight();
//    }
//    ViewGroup.LayoutParams params = listView.getLayoutParams();
//    params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
//    listView.setLayoutParams(params);
//  }
}
