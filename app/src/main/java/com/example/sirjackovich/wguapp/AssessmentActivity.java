package com.example.sirjackovich.wguapp;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class AssessmentActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private CursorAdapter cursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment);

        insertAssessment("New Assessment");


        String[] from = {DatabaseHelper.ASSESSMENT_TITLE};
        int[] to = {android.R.id.text1};


        cursorAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, null, from, to, 0);

        ListView listView = (ListView) findViewById(R.id.listView);

        listView.setAdapter(cursorAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AssessmentActivity.this, AssessmentDetailsActivity.class);
                Uri uri = Uri.parse(AssessmentsProvider.CONTENT_URI + "/" + id);
                intent.putExtra(AssessmentsProvider.CONTENT_ITEM_TYPE, uri);
                startActivityForResult(intent, 1);
            }
        });

        getLoaderManager().initLoader(0, null, this);


//        cursorAdapter = new NotesCursorAdapter(this, null, 0);
//
//        ListView list = (ListView) findViewById(android.R.id.list);
//        list.setAdapter(cursorAdapter);
//
//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
//                Uri uri = Uri.parse(NotesProvider.CONTENT_URI + "/" + id);
//                intent.putExtra(NotesProvider.CONTENT_ITEM_TYPE, uri);
//                startActivityForResult(intent, EDITOR_REQUEST_CODE);
//            }
//        });
//
//

    }

    private void insertAssessment(String text) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.ASSESSMENT_TITLE, text);
        Uri assessmentUri = getContentResolver().insert(AssessmentsProvider.CONTENT_URI, values);
        Log.d("AssessmentsActivity", "Inserted assessment " + assessmentUri.getLastPathSegment());
    }

    //    private void insertNote(String noteText) {
//        ContentValues values = new ContentValues();
//        values.put(DBOpenHelper.NOTE_TEXT, noteText);
//        Uri noteUri = getContentResolver().insert(NotesProvider.CONTENT_URI,
//                values);
//    }
//
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        switch (id) {
//            case R.id.action_create_sample:
//                insertSampleData();
//                break;
//            case R.id.action_delete_all:
//                deleteAllNotes();
//                break;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    private void deleteAllNotes() {
//
//        DialogInterface.OnClickListener dialogClickListener =
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int button) {
//                        if (button == DialogInterface.BUTTON_POSITIVE) {
//                            //Insert Data management code here
//                            getContentResolver().delete(
//                                    NotesProvider.CONTENT_URI, null, null
//                            );
//                            restartLoader();
//
//                            Toast.makeText(MainActivity.this,
//                                    getString(R.string.all_deleted),
//                                    Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                };
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage(getString(R.string.are_you_sure))
//                .setPositiveButton(getString(android.R.string.yes), dialogClickListener)
//                .setNegativeButton(getString(android.R.string.no), dialogClickListener)
//                .show();
//    }
//
//    private void insertSampleData() {
//        insertNote("Simple note");
//        insertNote("Multi-line\nnote");
//        insertNote("Very long note with a lot of text that exceeds the width of the screen");
//        restartLoader();
//    }
//
    private void restartLoader() {
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, AssessmentsProvider.CONTENT_URI,
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
