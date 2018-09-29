package com.example.sirjackovich.wguapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
  private static final int DATABASE_VERSION = 1;
  private static final String DATABASE_NAME = "WGUAPP";

  public static final String ASSESSMENTS_TABLE = "Assessments";
  public static final String ASSESSMENT_ID = "_id";
  public static final String ASSESSMENT_TYPE = "type";
  public static final String ASSESSMENT_TITLE = "title";
  public static final String ASSESSMENT_DUE_DATE = "dueDate";
  public static final String ASSESSMENT_CREATED = "created";

  public static final String[] ASSESSMENT_COLUMNS = {ASSESSMENT_ID, ASSESSMENT_TYPE, ASSESSMENT_TITLE, ASSESSMENT_DUE_DATE, ASSESSMENT_CREATED};

  private static final String CREATE_ASSESSMENTS_TABLE =
    "create table " + ASSESSMENTS_TABLE + " ( " +
      ASSESSMENT_ID + " integer primary key autoincrement, " +
      ASSESSMENT_TYPE + " text, " +
      ASSESSMENT_TITLE + " text, " +
      ASSESSMENT_DUE_DATE + " text, " +
      ASSESSMENT_CREATED + " text default CURRENT_TIMESTAMP)";

  public DatabaseHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase database) {
    database.execSQL(CREATE_ASSESSMENTS_TABLE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
    database.execSQL("drop table if exists " + ASSESSMENTS_TABLE);
    onCreate(database);
  }
}
