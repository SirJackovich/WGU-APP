package com.example.sirjackovich.wguapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.example.sirjackovich.wguapp.assessments.AssessmentsActivity;
import com.example.sirjackovich.wguapp.courses.CoursesActivity;
import com.example.sirjackovich.wguapp.mentors.MentorsActivity;

public class MainActivity extends ActionBarActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  public void showAssessments(View view) {
    showActivity(AssessmentsActivity.class);
  }


  public void showMentors(View view) {
    showActivity(MentorsActivity.class);
  }

  public void showCourses(View view) {
    showActivity(CoursesActivity.class);
  }

  public void showActivity(Class activityClass) {
    // Create an Intent to start the activity
    Intent intent = new Intent(this, activityClass);

    // Start the new activity.
    startActivity(intent);
  }
}
