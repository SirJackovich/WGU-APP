package com.example.sirjackovich.wguapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
  private static final int DATABASE_VERSION = 1;
  private static final String DATABASE_NAME = "WGUAPP";

  public static final String TERMS_TABLE = "Terms";
  public static final String TERM_ID = "_id";
  public static final String TERM_TITLE = "title";
  public static final String TERM_START_DATE = "startDate";
  public static final String TERM_END_DATE = "endDate";
  public static final String TERM_CREATED = "created";

  public static final String[] TERM_COLUMNS = {TERM_ID, TERM_TITLE, TERM_START_DATE, TERM_END_DATE, TERM_CREATED};

  private static final String CREATE_TERMS_TABLE =
    "create table " + TERMS_TABLE + " ( " +
      TERM_ID + " integer primary key autoincrement, " +
      TERM_TITLE + " text, " +
      TERM_START_DATE + " text, " +
      TERM_END_DATE + " text, " +
      TERM_CREATED + " text default CURRENT_TIMESTAMP)";

  public static final String COURSES_TABLE = "Courses";
  public static final String COURSE_ID = "_id";
  public static final String COURSE_TITLE = "title";
  public static final String COURSE_START_DATE = "startDate";
  public static final String COURSE_START_DATE_ALERT = "startDateAlert";
  public static final String COURSE_END_DATE = "endDate";
  public static final String COURSE_END_DATE_ALERT = "endDateAlert";
  public static final String COURSE_STATUS = "status";
  public static final String COURSE_NOTE = "note";
  public static final String COURSE_TERM_ID = "termID";
  public static final String COURSE_CREATED = "created";

  public static final String[] COURSE_COLUMNS = {COURSE_ID, COURSE_TITLE, COURSE_START_DATE, COURSE_START_DATE_ALERT, COURSE_END_DATE, COURSE_END_DATE_ALERT, COURSE_STATUS, COURSE_NOTE, COURSE_TERM_ID, COURSE_CREATED};

  private static final String CREATE_COURSES_TABLE =
    "create table " + COURSES_TABLE + " ( " +
      COURSE_ID + " integer primary key autoincrement, " +
      COURSE_TITLE + " text, " +
      COURSE_START_DATE + " text, " +
      COURSE_START_DATE_ALERT + " int default 0, " +
      COURSE_END_DATE + " text, " +
      COURSE_END_DATE_ALERT + " int default 0, " +
      COURSE_STATUS + " text, " +
      COURSE_NOTE + " text, " +
      COURSE_TERM_ID + " integer, " +
      COURSE_CREATED + " text default CURRENT_TIMESTAMP, " +
      "FOREIGN KEY(" + COURSE_TERM_ID + ") REFERENCES " + TERMS_TABLE + "(" + TERM_ID + " ))";

  public static final String ASSESSMENTS_TABLE = "Assessments";
  public static final String ASSESSMENT_ID = "_id";
  public static final String ASSESSMENT_TYPE = "type";
  public static final String ASSESSMENT_TITLE = "title";
  public static final String ASSESSMENT_DUE_DATE = "dueDate";
  public static final String ASSESSMENT_ALERT = "alert";
  public static final String ASSESSMENT_COURSE_ID = "courseID";
  public static final String ASSESSMENT_CREATED = "created";

  public static final String[] ASSESSMENT_COLUMNS = {ASSESSMENT_ID, ASSESSMENT_TYPE, ASSESSMENT_TITLE, ASSESSMENT_DUE_DATE, ASSESSMENT_ALERT, ASSESSMENT_COURSE_ID, ASSESSMENT_CREATED};

  private static final String CREATE_ASSESSMENTS_TABLE =
    "create table " + ASSESSMENTS_TABLE + " ( " +
      ASSESSMENT_ID + " integer primary key autoincrement, " +
      ASSESSMENT_TYPE + " text, " +
      ASSESSMENT_TITLE + " text, " +
      ASSESSMENT_DUE_DATE + " text, " +
      ASSESSMENT_ALERT + " int default 0, " +
      ASSESSMENT_COURSE_ID + " integer, " +
      ASSESSMENT_CREATED + " text default CURRENT_TIMESTAMP, " +
      "FOREIGN KEY(" + ASSESSMENT_COURSE_ID + ") REFERENCES " + COURSES_TABLE + "(" + COURSE_ID + " ))";

  public static final String MENTORS_TABLE = "Mentors";
  public static final String MENTOR_ID = "_id";
  public static final String MENTOR_NAME = "name";
  public static final String MENTOR_PHONE = "phone";
  public static final String MENTOR_EMAIL = "email";
  public static final String MENTOR_COURSE_ID = "courseID";
  public static final String MENTOR_CREATED = "created";

  public static final String[] MENTOR_COLUMNS = {MENTOR_ID, MENTOR_NAME, MENTOR_PHONE, MENTOR_EMAIL, MENTOR_COURSE_ID, MENTOR_CREATED};

  private static final String CREATE_MENTORS_TABLE =
    "create table " + MENTORS_TABLE + " ( " +
      MENTOR_ID + " integer primary key autoincrement, " +
      MENTOR_NAME + " text, " +
      MENTOR_PHONE + " text, " +
      MENTOR_EMAIL + " text, " +
      MENTOR_COURSE_ID + " integer, " +
      MENTOR_CREATED + " text default CURRENT_TIMESTAMP, " +
      "FOREIGN KEY(" + MENTOR_COURSE_ID + ") REFERENCES " + COURSES_TABLE + "(" + COURSE_ID + " ))";

  public DatabaseHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase database) {
    database.execSQL(CREATE_ASSESSMENTS_TABLE);
    database.execSQL(CREATE_MENTORS_TABLE);
    database.execSQL(CREATE_COURSES_TABLE);
    database.execSQL(CREATE_TERMS_TABLE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
    database.execSQL("drop table if exists " + ASSESSMENTS_TABLE);
    database.execSQL("drop table if exists " + MENTORS_TABLE);
    database.execSQL("drop table if exists " + COURSES_TABLE);
    database.execSQL("drop table if exists " + TERMS_TABLE);
    onCreate(database);
  }
}
