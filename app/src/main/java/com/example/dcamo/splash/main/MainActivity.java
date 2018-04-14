package com.example.dcamo.splash.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.dcamo.splash.R;
import com.example.dcamo.splash.support.SimpleScannerActivity;

public class MainActivity extends AppCompatActivity {
    Button qr, booking, random;
    public static String ip = "http://192.168.1.3:3001";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        qr = findViewById(R.id.qrButton);
        booking = findViewById(R.id.bookingButton);
        random = findViewById(R.id.button2);
        qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getBaseContext(), SimpleScannerActivity.class);
                startActivity(intent);
            }
        });

        booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getBaseContext(), BookingActivity.class);
                startActivity(intent);
            }
        });


    }

}
