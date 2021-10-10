package com.example.mylogapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

public class ShowAllLogs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_logs);

        String[] titles=FileUtil.getLogsName("data/data/com.example.mylogapp/files");
        //String[] LogsPath=new String[titles.length];
        Logs[] Logs=new Logs[titles.length];

        for(int i=0;i<titles.length;i++){
            Logs[i]=new Logs(titles[i]);
        }

        List<Logs> LogsList=new ArrayList<>();
        LogsAdapter adapter;

        for(int i=0;i<titles.length;i++){
            LogsList.add(Logs[i]);
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new LogsAdapter(LogsList);
        recyclerView.setAdapter(adapter);
    }

}
