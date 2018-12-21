package com.tvscs.ilead.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tvscs.ilead.model.FollowupPojo;

import java.util.List;

import ilead.tvscs.com.ilead.R;

public class FollowupAdapter extends RecyclerView.Adapter<FollowupAdapter.MyViewHolder> {

    private List<FollowupPojo> moviesList;

    public FollowupAdapter(List<FollowupPojo> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.followup_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        FollowupPojo movie = moviesList.get(position);
        holder.actionCode.setText(movie.getAction_code());
        holder.naCode.setText(movie.getNxt_act_code());
        holder.resultCode.setText(movie.getResult_code());
        holder.naDate.setText(movie.getNxt_act_date());
        holder.createdOn.setText(movie.getCreated_On());
        holder.createBy.setText(movie.getCreated_by());
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView actionCode, naCode, resultCode, naDate, createdOn, createBy;

        public MyViewHolder(View view) {
            super(view);
            actionCode = (TextView) view.findViewById(R.id.actioncodeValue_textView);
            naCode = (TextView) view.findViewById(R.id.nacodeValue_textView);
            resultCode = (TextView) view.findViewById(R.id.resultcodeValue_textView);
            naDate = (TextView) view.findViewById(R.id.nadateValue_textView);
            createdOn = (TextView) view.findViewById(R.id.createdonValue_textView);
            createBy = (TextView) view.findViewById(R.id.createdbyValue_textView);
        }
    }
}