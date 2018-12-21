package com.tvscs.ilead.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tvscs.ilead.activity.NotificationActivity;
import com.tvscs.ilead.model.NotificationPojo;
import com.tvscs.ilead.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import ilead.tvscs.com.ilead.R;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    private List<NotificationPojo> notifyList;
    private Context mContext;

    public NotificationAdapter(ArrayList<NotificationPojo> myDataset, Context context) {
        notifyList = myDataset;
        this.mContext = context;
    }

    public NotificationPojo getItem(int position) {
        return notifyList.get(position);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final NotificationPojo notifyResponse = getItem(position);

        holder.leadId.setText(notifyResponse.getLead_no());
        holder.customerName.setText(notifyResponse.getFirst_name());
        holder.leadStatus.setText(notifyResponse.getStatus());
        holder.leadDateTime.setText(notifyResponse.getNext_Action());

        String productCode = notifyResponse.getLead_no().substring(4, 6);

        switch (productCode) {
            case Constants.AL:
                holder.productImage.setImageResource(R.drawable.ic_trans_auto);
                break;
            case Constants.AP:
                holder.productImage.setImageResource(R.drawable.ic_trans_usedtractor);
                break;
            case Constants.CA:
                holder.productImage.setImageResource(R.drawable.ic_trans_car);
                break;
            case Constants.CD:
                holder.productImage.setImageResource(R.drawable.ic_trans_commercial);
                break;
            case Constants.DC:
                holder.productImage.setImageResource(R.drawable.ic_trans_car);
                break;
            case Constants.LN:
                holder.productImage.setImageResource(R.drawable.ic_trans_usedtractor);
                break;
            case Constants.TR:
                holder.productImage.setImageResource(R.drawable.ic_trans_tractor);
                break;
            case Constants.TW:
                holder.productImage.setImageResource(R.drawable.ic_trans_bike);
                break;
            case Constants.UT:
                holder.productImage.setImageResource(R.drawable.ic_usedbike);
                break;
            case Constants.UV:
                holder.productImage.setImageResource(R.drawable.ic_trans_commercial);
                break;
            default:
                holder.productImage.setImageResource(R.drawable.ic_trans_auto);
                break;


        }
        if (position == getItemCount() - 1) //check if this is the last child, if yes then hide the divider
            holder.divider.setVisibility(View.GONE);


        holder.notification_itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NotificationActivity) mContext).onClickCalled(notifyList.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return notifyList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout notification_itemLayout;
        private TextView leadId, customerName, leadStatus, leadDateTime;
        private ImageButton productImage;
        private View divider;

        public MyViewHolder(View view) {
            super(view);
            notification_itemLayout = (RelativeLayout) view.findViewById(R.id.notification_itemLayout);
            leadId = (TextView) view.findViewById(R.id.notifyLeadid);
            customerName = (TextView) view.findViewById(R.id.notifyName);
            leadStatus = (TextView) view.findViewById(R.id.notifyStatus);
            leadDateTime = (TextView) view.findViewById(R.id.notifyDateTime);
            productImage = (ImageButton) view.findViewById(R.id.notify_productImage);
            divider = (View) view.findViewById(R.id.notifcationView);
        }
    }
}
