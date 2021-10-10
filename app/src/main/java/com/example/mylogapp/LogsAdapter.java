package com.example.mylogapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LogsAdapter extends RecyclerView.Adapter<LogsAdapter.ViewHolder> {
    private Context mContext;
    private List<Logs> LogsList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView logName;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            logName = (TextView) view.findViewById(R.id.log_name);
        }
    }

    public LogsAdapter(List<Logs> getLogsList){
        LogsList=getLogsList;
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
                Intent intent=new Intent(mContext,ViewLogs.class);
                intent.putExtra(ViewLogs.LOGS_NAME,logs.getName());
                mContext.startActivity(intent);
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
