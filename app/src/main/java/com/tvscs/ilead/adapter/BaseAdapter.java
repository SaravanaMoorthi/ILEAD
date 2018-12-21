package com.tvscs.ilead.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class BaseAdapter extends RecyclerView.Adapter<BaseAdapter.BaseView> {


    @NonNull
    @Override
    public BaseView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseView holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class BaseView extends RecyclerView.ViewHolder {

        public BaseView(View itemView) {
            super(itemView);
        }
    }
}
