package com.example.mylogapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageButton log_today=(ImageButton) findViewById(R.id.log_today);
        log_today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(MainActivity.this,LogToday.class);
                intent1.putExtra("loadjudge","Y");
                startActivity(intent1);
            }
        });

        ImageButton log_before=(ImageButton) findViewById(R.id.log_before);
        log_before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2=new Intent(MainActivity.this,ShowAllLogs.class);
                startActivity(intent2);
            }
        });

    }
}