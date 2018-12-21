package com.tvscs.ilead.adapter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tvscs.ilead.activity.LeadUpdateActivity;
import com.tvscs.ilead.fragment.LeadsReceivedFragment;
import com.tvscs.ilead.model.leadsreceivedmodel.LeadsReceivedModel;
import com.tvscs.ilead.utils.Constants;
import com.tvscs.ilead.utils.PermissionInterface;
import com.tvscs.ilead.utils.Utils;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import ilead.tvscs.com.ilead.R;

public class LeadsReceivedAdapter extends RecyclerView.Adapter<LeadsReceivedAdapter.MyViewHolder> implements Filterable, PermissionInterface, Constants {

    private Context mContext;
    private ArrayList<LeadsReceivedModel> refLeadsreceivedResults;
    private ArrayList<LeadsReceivedModel> mFilteredList = new ArrayList<>();
    private int CALL_PERMISSION_CODE = 23;


    public LeadsReceivedAdapter(Context mContext, ArrayList<LeadsReceivedModel> refLeadsreceivedResults) {
        this.mContext = mContext;
        this.refLeadsreceivedResults = refLeadsreceivedResults;
        this.mFilteredList.addAll(refLeadsreceivedResults);
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public LeadsReceivedAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_list_leadsreceived, parent, false);
        return new LeadsReceivedAdapter.MyViewHolder(view);
    }


    public void onBindViewHolder(@NonNull LeadsReceivedAdapter.MyViewHolder holder, final int position) {
        Utils.setValueInTextview(holder.lr_txtViewLeadId, mFilteredList.get(position).getLead_id());
        Utils.setValueInTextview(holder.leadstatus_textView, mFilteredList.get(position).getStatus());
        Utils.setValueInTextview(holder.lr_txtViewName, mFilteredList.get(position).getFirst_name());
        Utils.setValueInTextview(holder.lr_txtViewDate, mFilteredList.get(position).getCreated_on().substring(0, mFilteredList.get(position).getCreated_on().indexOf(' ')));
        Utils.setValueInTextview(holder.lr_txtViewMobile, mFilteredList.get(position).getPhone_mobile1());
        Utils.setValueInTextview(holder.lr_txtViewFirstLetter, mFilteredList.get(position).getFirst_name().substring(0, 1));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(mContext, LeadUpdateActivity.class);
                in.putExtra(Constants.respleadId_, mFilteredList.get(position).getLead_id());
                in.putExtra(Constants.className, LEADS_RECEIVED);
                mContext.startActivity(in);
            }
        });

        holder.lr_call_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog_Call(mFilteredList.get(position));
            }
        });
    }


    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString().toLowerCase(Locale.getDefault());
                FilterResults filterResults = new FilterResults();
                if (charString == null || charString.length() == 0 || charString.isEmpty()) {
                    filterResults.values = refLeadsreceivedResults;
                    filterResults.count = refLeadsreceivedResults.size();
                } else {
                    ArrayList<LeadsReceivedModel> filteredList = new ArrayList<>();
                    for (LeadsReceivedModel docum : refLeadsreceivedResults) {
                        if (docum.getPhone_mobile1().toLowerCase(Locale.getDefault()).contains(charString) || docum.getFirst_name().toLowerCase().contains(charString)) {
                            filteredList.add(docum);
                        }

                    }
                    filterResults.values = filteredList;
                    filterResults.count = refLeadsreceivedResults.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList.clear();
                mFilteredList.addAll((ArrayList<LeadsReceivedModel>) filterResults.values);
                notifyDataSetChanged();
                LeadsReceivedFragment.setNoDataVisible(mFilteredList.size());
            }
        };
    }

    public void alertDialog_Call(final LeadsReceivedModel leadsReceivedModel) {
        new SweetAlertDialog(mContext, SweetAlertDialog.NORMAL_TYPE)
                .setTitleText(confirmMsg)
                .setContentText(callDialogTitle + leadsReceivedModel.getFirst_name())
                .setCancelText(cancelPick)
                .setConfirmText(callDialogconfirmPick)
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        if (isCallAllowed()) {
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + leadsReceivedModel.getPhone_mobile1()));
                            mContext.startActivity(intent);
                            sweetAlertDialog.cancel();
                            return;
                        }
                        requestStoragePermission();
                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .show();
    }

    private boolean isCallAllowed() {
        int result = ContextCompat.checkSelfPermission((Activity) mContext, Manifest.permission.CALL_PHONE);
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;
        return false;
    }

    private void requestStoragePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, Manifest.permission.CALL_PHONE)) {
        }
        ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.CALL_PHONE}, CALL_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CALL_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText((Activity) mContext, "Permission granted now you can call", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText((Activity) mContext, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public final View mView;

        @BindView(R.id.lr_txtViewLeadId)
        TextView lr_txtViewLeadId;
        @BindView(R.id.leadstatus_textView)
        TextView leadstatus_textView;
        @BindView(R.id.lr_txtViewName)
        TextView lr_txtViewName;
        @BindView(R.id.lr_txtViewDate)
        TextView lr_txtViewDate;
        @BindView(R.id.lr_txtViewMobile)
        TextView lr_txtViewMobile;
        @BindView(R.id.lr_txtViewFirstLetter)
        TextView lr_txtViewFirstLetter;
        @BindView(R.id.lr_call_layout)
        LinearLayout lr_call_layout;


        public MyViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }
}

