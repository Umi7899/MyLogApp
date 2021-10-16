package com.example.mylogapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

public class LogsAdapter extends RecyclerView.Adapter<LogsAdapter.ViewHolder> {
    private Context mContext;
    private List<Logs> LogsList;
    private boolean ifRead;


    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView logName;
        ImageButton delete;
        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            logName = (TextView) view.findViewById(R.id.log_name);
            delete=(ImageButton)view.findViewById(R.id.delete);
        }
    }

    public LogsAdapter(List<Logs> getLogsList,boolean onlyRead){

        LogsList=getLogsList;
        ifRead=onlyRead;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        if(mContext==null){
            mContext=parent.getContext();
        }

        View view= LayoutInflater.from(mContext).inflate(R.layout.logs_layout,parent,false);

        final ViewHolder holder=new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position =holder.getAdapterPosition();
                Logs logs=LogsList.get(position);
                if(!ifRead) {
                    Intent intent = new Intent(mContext, ViewLogs.class);
                    intent.putExtra(ViewLogs.LOGS_NAME, logs.getName());
                    mContext.startActivity(intent);
                }
                else {
                    Intent intent_onlyR = new Intent(mContext, ViewLogs_only_Read.class);
                    intent_onlyR.putExtra(ViewLogs.LOGS_NAME, logs.getName());
                    mContext.startActivity(intent_onlyR);
                }
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                int position =holder.getAdapterPosition();
                Logs logs=LogsList.get(position);
                LogsList.remove(position);
                notifyDataSetChanged();
                String logsName="data/data/com.example.mylogapp/files/"+logs.getName();
                File file = new File(logsName);
                // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
                if (file.exists() && file.isFile()) {
                    Toast.makeText(mContext,"Delete!",Toast.LENGTH_SHORT).show();
                    file.delete();
                }
            }
        });

        return holder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder ,int position){
        Logs log=LogsList.get(position);
        holder.logName.setText(log.getName());
    }
    public int getItemCount() {
        return LogsList.size();
    }

}
