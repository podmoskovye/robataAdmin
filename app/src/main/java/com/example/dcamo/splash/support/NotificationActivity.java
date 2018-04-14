package com.example.dcamo.splash.support;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.dcamo.splash.R;

public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Intent intent = new Intent(NotificationActivity.this, MainActivity.class);
//        startActivity(intent);
//        this.finish();
        setContentView(R.layout.activity_notification);
    }
}
