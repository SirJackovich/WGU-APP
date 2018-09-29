package com.example.sirjackovich.wguapp.mentors;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.example.sirjackovich.wguapp.DatabaseHelper;

public class MentorsProvider extends ContentProvider {

  private static final String AUTHORITY = "com.example.sirjackovich.mentorsprovider";
  private static final String BASE_PATH = "Mentors";
  public static final Uri CONTENT_URI =
    Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

  private static final int MENTORS = 1;
  private static final int MENTOR_ID = 2;

  private static final UriMatcher uriMatcher =
    new UriMatcher(UriMatcher.NO_MATCH);

  public static final String CONTENT_ITEM_TYPE = "Mentor";

  static {
    uriMatcher.addURI(AUTHORITY, BASE_PATH, MENTORS);
    uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", MENTOR_ID);
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

    if (uriMatcher.match(uri) == MENTOR_ID) {
      selection = DatabaseHelper.MENTOR_ID + "=" + uri.getLastPathSegment();
    }

    return database.query(DatabaseHelper.MENTORS_TABLE, DatabaseHelper.MENTOR_COLUMNS,
      selection, null, null, null,
      DatabaseHelper.MENTOR_CREATED + " DESC");
  }

  @Override
  public String getType(Uri uri) {
    return null;
  }

  @Override
  public Uri insert(Uri uri, ContentValues values) {
    long id = database.insert(DatabaseHelper.MENTORS_TABLE, null, values);
    return Uri.parse(BASE_PATH + "/" + id);
  }

  @Override
  public int delete(Uri uri, String selection, String[] selectionArgs) {
    return database.delete(DatabaseHelper.MENTORS_TABLE, selection, selectionArgs);
  }

  @Override
  public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
    return database.update(DatabaseHelper.MENTORS_TABLE, values, selection, selectionArgs);
  }
}