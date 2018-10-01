package com.example.sirjackovich.wguapp;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

public class ItemProvider extends ContentProvider {

  private static final String AUTHORITY = "com.example.sirjackovich.itemprovider";

  private static final String MENTORS_BASE_PATH = "Mentors";
  private static final String COURSES_BASE_PATH = "Courses";
  private static final String ASSESSMENTS_BASE_PATH = "Assessments";
  private static final String TERMS_BASE_PATH = "Terms";

  public static final Uri MENTOR_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + MENTORS_BASE_PATH);
  public static final Uri COURSES_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + COURSES_BASE_PATH);
  public static final Uri ASSESSMENTS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + ASSESSMENTS_BASE_PATH);
  public static final Uri TERMS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TERMS_BASE_PATH);

  private static final int MENTORS = 1;
  private static final int MENTOR_ID = 2;
  private static final int COURSES = 3;
  private static final int COURSE_ID = 4;
  private static final int ASSESSMENTS = 5;
  private static final int ASSESSMENT_ID = 6;
  private static final int TERMS = 7;
  private static final int TERM_ID = 8;

  private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

  static {
    uriMatcher.addURI(AUTHORITY, MENTORS_BASE_PATH, MENTORS);
    uriMatcher.addURI(AUTHORITY, MENTORS_BASE_PATH + "/#", MENTOR_ID);
    uriMatcher.addURI(AUTHORITY, COURSES_BASE_PATH, COURSES);
    uriMatcher.addURI(AUTHORITY, COURSES_BASE_PATH + "/#", COURSE_ID);
    uriMatcher.addURI(AUTHORITY, ASSESSMENTS_BASE_PATH, ASSESSMENTS);
    uriMatcher.addURI(AUTHORITY, ASSESSMENTS_BASE_PATH + "/#", ASSESSMENT_ID);
    uriMatcher.addURI(AUTHORITY, TERMS_BASE_PATH, TERMS);
    uriMatcher.addURI(AUTHORITY, TERMS_BASE_PATH + "/#", TERM_ID);
  }

  private SQLiteDatabase database;

  @Override
  public boolean onCreate() {
    DatabaseHelper helper = new DatabaseHelper(getContext());
    database = helper.getWritableDatabase();
    return true;
  }

  @Override
  public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
    Cursor cursor = null;

    switch (uriMatcher.match(uri)) {
      case MENTORS:
        cursor = database.query(DatabaseHelper.MENTORS_TABLE, DatabaseHelper.MENTOR_COLUMNS, selection, null, null, null, DatabaseHelper.MENTOR_CREATED + " DESC");
        break;
      case MENTOR_ID:
        selection = DatabaseHelper.MENTOR_ID + "=" + uri.getLastPathSegment();
        cursor = database.query(DatabaseHelper.MENTORS_TABLE, DatabaseHelper.MENTOR_COLUMNS, selection, null, null, null, DatabaseHelper.MENTOR_CREATED + " DESC");
        break;
      case COURSES:
        cursor = database.query(DatabaseHelper.COURSES_TABLE, DatabaseHelper.COURSE_COLUMNS, selection, null, null, null, DatabaseHelper.COURSE_CREATED + " DESC");
        break;
      case COURSE_ID:
        selection = DatabaseHelper.COURSE_ID + "=" + uri.getLastPathSegment();
        cursor = database.query(DatabaseHelper.COURSES_TABLE, DatabaseHelper.COURSE_COLUMNS, selection, null, null, null, DatabaseHelper.COURSE_CREATED + " DESC");
        break;
      case ASSESSMENTS:
        cursor = database.query(DatabaseHelper.ASSESSMENTS_TABLE, DatabaseHelper.ASSESSMENT_COLUMNS, selection, null, null, null, DatabaseHelper.ASSESSMENT_CREATED + " DESC");
        break;
      case ASSESSMENT_ID:
        selection = DatabaseHelper.ASSESSMENT_ID + "=" + uri.getLastPathSegment();
        cursor = database.query(DatabaseHelper.ASSESSMENTS_TABLE, DatabaseHelper.ASSESSMENT_COLUMNS, selection, null, null, null, DatabaseHelper.ASSESSMENT_CREATED + " DESC");
        break;
      case TERMS:
        cursor = database.query(DatabaseHelper.TERMS_TABLE, DatabaseHelper.TERM_COLUMNS, selection, null, null, null, DatabaseHelper.TERM_CREATED + " DESC");
        break;
      case TERM_ID:
        selection = DatabaseHelper.TERM_ID + "=" + uri.getLastPathSegment();
        cursor = database.query(DatabaseHelper.TERMS_TABLE, DatabaseHelper.TERM_COLUMNS, selection, null, null, null, DatabaseHelper.TERM_CREATED + " DESC");
        break;
    }
    return cursor;
  }

  @Override
  public String getType(@NonNull Uri uri) {
    return null;
  }

  @Override
  public Uri insert(@NonNull Uri uri, ContentValues values) {
    Uri newUri = null;
    switch (uriMatcher.match(uri)) {
      case MENTORS:
        newUri = Uri.parse(MENTORS_BASE_PATH + "/" + database.insert(DatabaseHelper.MENTORS_TABLE, null, values));
        break;
      case COURSES:
        newUri = Uri.parse(COURSES_BASE_PATH + "/" + database.insert(DatabaseHelper.COURSES_TABLE, null, values));
        break;
      case ASSESSMENTS:
        newUri = Uri.parse(ASSESSMENTS_BASE_PATH + "/" + database.insert(DatabaseHelper.ASSESSMENTS_TABLE, null, values));
        break;
      case TERMS:
        newUri = Uri.parse(TERMS_BASE_PATH + "/" + database.insert(DatabaseHelper.TERMS_TABLE, null, values));
        break;
    }
    return newUri;
  }

  @Override
  public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
    int result = 0;
    switch (uriMatcher.match(uri)) {
      case MENTORS:
        result = database.delete(DatabaseHelper.MENTORS_TABLE, selection, selectionArgs);
        break;
      case MENTOR_ID:
        selection = DatabaseHelper.MENTOR_ID + "=" + uri.getLastPathSegment();
        result = database.delete(DatabaseHelper.MENTORS_TABLE, selection, selectionArgs);
        break;
      case COURSES:
        result = database.delete(DatabaseHelper.COURSES_TABLE, selection, selectionArgs);
        break;
      case COURSE_ID:
        selection = DatabaseHelper.COURSE_ID + "=" + uri.getLastPathSegment();
        result = database.delete(DatabaseHelper.COURSES_TABLE, selection, selectionArgs);
        break;
      case ASSESSMENTS:
        result = database.delete(DatabaseHelper.ASSESSMENTS_TABLE, selection, selectionArgs);
        break;
      case ASSESSMENT_ID:
        selection = DatabaseHelper.ASSESSMENT_ID + "=" + uri.getLastPathSegment();
        result = database.delete(DatabaseHelper.ASSESSMENTS_TABLE, selection, selectionArgs);
        break;
      case TERMS:
        result = database.delete(DatabaseHelper.TERMS_TABLE, selection, selectionArgs);
        break;
      case TERM_ID:
        selection = DatabaseHelper.TERM_ID + "=" + uri.getLastPathSegment();
        result = database.delete(DatabaseHelper.TERMS_TABLE, selection, selectionArgs);
        break;
    }
    return result;
  }

  @Override
  public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
    int result = 0;
    switch (uriMatcher.match(uri)) {
      case MENTORS:
        result = database.update(DatabaseHelper.MENTORS_TABLE, values, selection, selectionArgs);
        break;
      case MENTOR_ID:
        selection = DatabaseHelper.MENTOR_ID + "=" + uri.getLastPathSegment();
        result = database.update(DatabaseHelper.MENTORS_TABLE, values, selection, selectionArgs);
        break;
      case COURSES:
        result = database.update(DatabaseHelper.COURSES_TABLE, values, selection, selectionArgs);
        break;
      case COURSE_ID:
        selection = DatabaseHelper.COURSE_ID + "=" + uri.getLastPathSegment();
        result = database.update(DatabaseHelper.COURSES_TABLE, values, selection, selectionArgs);
        break;
      case ASSESSMENTS:
        result = database.update(DatabaseHelper.ASSESSMENTS_TABLE, values, selection, selectionArgs);
        break;
      case ASSESSMENT_ID:
        selection = DatabaseHelper.ASSESSMENT_ID + "=" + uri.getLastPathSegment();
        result = database.update(DatabaseHelper.ASSESSMENTS_TABLE, values, selection, selectionArgs);
        break;
      case TERMS:
        result = database.update(DatabaseHelper.TERMS_TABLE, values, selection, selectionArgs);
        break;
      case TERM_ID:
        selection = DatabaseHelper.TERM_ID + "=" + uri.getLastPathSegment();
        result = database.update(DatabaseHelper.TERMS_TABLE, values, selection, selectionArgs);
        break;
    }
    return result;
  }
}