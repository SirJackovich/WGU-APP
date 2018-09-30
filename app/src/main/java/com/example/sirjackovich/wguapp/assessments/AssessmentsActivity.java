package com.example.sirjackovich.wguapp.assessments;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.sirjackovich.wguapp.DatabaseHelper;
import com.example.sirjackovich.wguapp.ItemProvider;
import com.example.sirjackovich.wguapp.R;


public class AssessmentsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
  private CursorAdapter cursorAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_assessment);

    String[] from = {DatabaseHelper.ASSESSMENT_TITLE};
    int[] to = {android.R.id.text1};

    cursorAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, null, from, to, 0);

    ListView listView = (ListView) findViewById(R.id.listView);

    listView.setAdapter(cursorAdapter);

    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(AssessmentsActivity.this, AssessmentDetailsActivity.class);
        Uri uri = Uri.parse(ItemProvider.ASSESSMENTS_CONTENT_URI + "/" + id);
        intent.putExtra("Assessment", uri);
        startActivityForResult(intent, 1);
      }
    });

    getLoaderManager().initLoader(0, null, this);
  }


  private void restartLoader() {
    getLoaderManager().restartLoader(0, null, this);
  }

  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    return new CursorLoader(this, ItemProvider.ASSESSMENTS_CONTENT_URI,
      null, null, null, null);
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    cursorAdapter.swapCursor(data);
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
    cursorAdapter.swapCursor(null);
  }

  public void createAssessment(View view) {
    Intent intent = new Intent(this, AssessmentDetailsActivity.class);
    startActivityForResult(intent, 1);

  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == 1 && resultCode == RESULT_OK) {
      restartLoader();
    }
  }
}
