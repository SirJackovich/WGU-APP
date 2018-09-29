package com.example.sirjackovich.wguapp.assessments;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.example.sirjackovich.wguapp.DatabaseHelper;

public class AssessmentsProvider extends ContentProvider {

  private static final String AUTHORITY = "com.example.sirjackovich.assessmentsprovider";
  private static final String BASE_PATH = "Assessments";
  public static final Uri CONTENT_URI =
    Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

  private static final int ASSESSMENTS = 1;
  private static final int ASSESSMENT_ID = 2;

  private static final UriMatcher uriMatcher =
    new UriMatcher(UriMatcher.NO_MATCH);

  public static final String CONTENT_ITEM_TYPE = "Assessment";

  static {
    uriMatcher.addURI(AUTHORITY, BASE_PATH, ASSESSMENTS);
    uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", ASSESSMENT_ID);
  }

  private SQLiteDatabase database;

  @Override
  public boolean onCreate() {
    DatabaseHelper helper = new DatabaseHelper(getContext());
    database = helper.getWritableDatabase();
    return true;
  }

  @Override
  public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

    if (uriMatcher.match(uri) == ASSESSMENT_ID) {
      selection = DatabaseHelper.ASSESSMENT_ID + "=" + uri.getLastPathSegment();
    }

    return database.query(DatabaseHelper.ASSESSMENTS_TABLE, DatabaseHelper.ASSESSMENT_COLUMNS,
      selection, null, null, null,
      DatabaseHelper.ASSESSMENT_CREATED + " DESC");
  }

  @Override
  public String getType(Uri uri) {
    return null;
  }

  @Override
  public Uri insert(Uri uri, ContentValues values) {
    long id = database.insert(DatabaseHelper.ASSESSMENTS_TABLE, null, values);
    return Uri.parse(BASE_PATH + "/" + id);
  }

  @Override
  public int delete(Uri uri, String selection, String[] selectionArgs) {
    return database.delete(DatabaseHelper.ASSESSMENTS_TABLE, selection, selectionArgs);
  }

  @Override
  public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
    return database.update(DatabaseHelper.ASSESSMENTS_TABLE, values, selection, selectionArgs);
  }
}