package com.example.mylogapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ShowAllLogs extends AppCompatActivity {
    private boolean onlyRead = false;
    public void refresh() {
        onCreate(null);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_logs);
        String[] titles = FileUtil.getLogsName("data/data/com.example.mylogapp/files");
        //以列表的形式获取文件名
        Logs[] Logs = new Logs[titles.length];
        for (int i = 0; i < titles.length; i++) {
            Logs[i] = new Logs(titles[i]);
        }
        List<Logs> LogsList = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            LogsList.add(Logs[i]);
        }
        //初始化布局
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        LogsAdapter adapter;
        adapter = new LogsAdapter(LogsList, onlyRead);
        recyclerView.setAdapter(adapter);
        //选择是否只读
        FloatingActionButton switch_read = (FloatingActionButton) findViewById(R.id.switch_read);
        switch_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onlyRead = !onlyRead;
                if (onlyRead)
                    Toast.makeText(ShowAllLogs.this, "Only Read Now!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(ShowAllLogs.this, "Write&Read Now!", Toast.LENGTH_SHORT).show();
                LogsAdapter adapter;
                adapter = new LogsAdapter(LogsList, onlyRead);
                recyclerView.setAdapter(adapter);
            }
        });
    }
}
