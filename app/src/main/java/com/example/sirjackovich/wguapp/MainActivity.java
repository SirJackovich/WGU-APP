package com.example.sirjackovich.wguapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void showAssessments(View view) {
        showActivity(AssessmentActivity.class);
    }

    public void showActivity(Class activityClass){
        // Create an Intent to start the activity
        Intent intent = new Intent(this, activityClass);

        // Start the new activity.
        startActivity(intent);
    }
}
