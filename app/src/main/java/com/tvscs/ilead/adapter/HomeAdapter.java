package com.tvscs.ilead.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tvscs.ilead.model.MainMenu;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ilead.tvscs.com.ilead.R;

/**
 * Created by ITSoftSupport on 28/12/2017.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<MainMenu> mainMenuArrayList;

    public HomeAdapter(Context mContext, ArrayList<MainMenu> mainMenus) {
        this.mContext = mContext;
        mainMenuArrayList = mainMenus;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_grid_homescreen, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final MainMenu mainMenu = mainMenuArrayList.get(position);

        if (position == 0) {
            ((GradientDrawable) holder.circle_layout.getBackground()).setColor(mContext.getResources().getColor(R.color.home_position0));
            holder.imgViewDashboard.setColorFilter(ContextCompat.getColor(mContext, R.color.home_icon0), android.graphics.PorterDuff.Mode.SRC_IN);
        } else if (position == 1) {
            ((GradientDrawable) holder.circle_layout.getBackground()).setColor(mContext.getResources().getColor(R.color.home_position1));
            holder.imgViewDashboard.setColorFilter(ContextCompat.getColor(mContext, R.color.home_icon1), android.graphics.PorterDuff.Mode.SRC_IN);
        } else if (position == 2) {
            ((GradientDrawable) holder.circle_layout.getBackground()).setColor(mContext.getResources().getColor(R.color.home_position2));
            holder.imgViewDashboard.setColorFilter(ContextCompat.getColor(mContext, R.color.home_icon2), android.graphics.PorterDuff.Mode.SRC_IN);
        } else if (position == 3) {
            ((GradientDrawable) holder.circle_layout.getBackground()).setColor(mContext.getResources().getColor(R.color.home_position3));
            holder.imgViewDashboard.setColorFilter(ContextCompat.getColor(mContext, R.color.home_icon3), android.graphics.PorterDuff.Mode.SRC_IN);
        }

        holder.txtViewDashboard.setText(mainMenuArrayList.get(position).getMenuName().toString());
        holder.imgViewDashboard.setImageResource(mainMenuArrayList.get(position).getImageId());
    }

    @Override
    public int getItemCount() {
        return mainMenuArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txtViewDashboard)
        TextView txtViewDashboard;
        @BindView(R.id.imgViewDashboard)
        ImageView imgViewDashboard;
        @BindView(R.id.cardViewDashboard)
        CardView cardViewDashboard;
        @BindView(R.id.circle_layout)
        LinearLayout circle_layout;


        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}