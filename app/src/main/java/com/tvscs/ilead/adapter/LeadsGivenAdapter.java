package com.tvscs.ilead.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tvscs.ilead.activity.LeadsGivenActivity;
import com.tvscs.ilead.model.leadgivenmodel.LeadsGivenModel;
import com.tvscs.ilead.model.leadgivenmodel.RefLeadsgivenResult;
import com.tvscs.ilead.utils.Constants;
import com.tvscs.ilead.utils.Utils;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import ilead.tvscs.com.ilead.R;

public class LeadsGivenAdapter extends RecyclerView.Adapter<LeadsGivenAdapter.LeadsGivenHolder> implements Filterable, Constants {

    private Context mContext;
    private LeadsGivenModel leadsGivenModel;

    private ArrayList<RefLeadsgivenResult> refLeadsgivenResults;
    private ArrayList<RefLeadsgivenResult> mFilteredList;

    public LeadsGivenAdapter(Context mContext, ArrayList<RefLeadsgivenResult> refLeadsgivenResults, LeadsGivenModel leadsGiven) {
        this.mContext = mContext;
        this.refLeadsgivenResults = refLeadsgivenResults;
        this.leadsGivenModel = leadsGiven;
        this.mFilteredList = refLeadsgivenResults;
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
    public LeadsGivenHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_list_leadsgiven, parent, false);
        return new LeadsGivenHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeadsGivenHolder holder, final int position) {
        Utils.setValueInTextview(holder.name_txtViewLeadsGiven, mFilteredList.get(position).getFirst_name());
        if (!mFilteredList.get(position).getStatus().equalsIgnoreCase("")) {
            Utils.setValueInTextview(holder.leadstatus_textView, mFilteredList.get(position).getStatus());
            holder.viewLeadsGiven.setVisibility(View.VISIBLE);
        } else {
            Utils.setValueInTextview(holder.leadstatus_textView, "");
            holder.viewLeadsGiven.setVisibility(View.GONE);
        }

        Utils.setValueInTextview(holder.leadid_txtViewLeadsGiven, mFilteredList.get(position).getLead_id());
        Utils.setValueInTextview(holder.mobile_txtViewLeadsGiven, mFilteredList.get(position).getPhone_mobile1());
        Utils.setValueInTextview(holder.date_txtViewLeadsGiven, mFilteredList.get(position).getCreated_on());
        Utils.setValueInTextview(holder.firstLetter_txtViewLeadsGiven, mFilteredList.get(position).getFirst_name().substring(0, 1));
        setTypeface(holder);

        holder.mobileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog_Call(mFilteredList.get(position));
            }
        });

        holder.imgViewSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog_Sms(mFilteredList.get(position));
            }
        });
    }

    private void setTypeface(LeadsGivenHolder holder) {
        Typeface typefaceRegular = Typeface.createFromAsset(mContext.getAssets(), Constants.TYPEFACE_REGULAR);
        Typeface typefaceBold = Typeface.createFromAsset(mContext.getAssets(), Constants.TYPEFACE_BOLD);

        holder.name_txtViewLeadsGiven.setTypeface(typefaceRegular);
        holder.leadstatus_textView.setTypeface(typefaceRegular);
        holder.leadid_txtViewLeadsGiven.setTypeface(typefaceRegular);
        holder.mobile_txtViewLeadsGiven.setTypeface(typefaceRegular);
        holder.date_txtViewLeadsGiven.setTypeface(typefaceRegular);
        holder.firstLetter_txtViewLeadsGiven.setTypeface(typefaceRegular);
    }


    //Filter the adapter interface
    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mFilteredList = refLeadsgivenResults;
                } else {
                    ArrayList<RefLeadsgivenResult> filteredList = new ArrayList<>();
                    for (RefLeadsgivenResult row : refLeadsgivenResults) {
                        if (row.getPhone_mobile1().toLowerCase(Locale.getDefault()).contains(charString) || row.getFirst_name().toLowerCase().contains(charString)) {
                            filteredList.add(row);
                        }
                    }
                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                mFilteredList = (ArrayList<RefLeadsgivenResult>) filterResults.values;
                notifyDataSetChanged();

                ((LeadsGivenActivity) mContext).setNoDataVisible(mFilteredList.size());

            }
        };
    }

    public void alertDialog_Call(final RefLeadsgivenResult refLeadsgivenResult) {
        SweetAlertDialog dialog = new SweetAlertDialog(mContext, SweetAlertDialog.NORMAL_TYPE)
                .setTitleText(confirmMsg)
                .setContentText(callDialogTitle + refLeadsgivenResult.getFirst_name())
                .setCancelText(cancelTxt)
                .setConfirmText(callDialogconfirmPick)
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        try {
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + refLeadsgivenResult.getPhone_mobile1()));
                            mContext.startActivity(intent);
                            sweetAlertDialog.dismiss();
                        } catch (Exception se) {
                            se.printStackTrace();
                        }
                        return;
                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                });
        dialog.show();
    }

    public void alertDialog_Sms(final RefLeadsgivenResult refLeadsgivenResult) {

        final EditText editText = new EditText(mContext);
        editText.setHint(smsHint);
        SweetAlertDialog dialog = new SweetAlertDialog(mContext, SweetAlertDialog.NORMAL_TYPE)
                .setTitleText(confirmSMS)
                .setContentText(smsDialogTitle + refLeadsgivenResult.getFirst_name())
                .setCancelText(cancelTxt)
                .setConfirmText(smsDialogconfirmPick)
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        // Utils.closeKeyboard(mContext);
                        sweetAlertDialog.dismiss();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        String text = editText.getText().toString();

                        if (!text.isEmpty()) {
                            startSms(refLeadsgivenResult.getPhone_mobile1(), text);
                            sweetAlertDialog.dismiss();
                        } else {
                            Toast.makeText(mContext, msgValidationText, Toast.LENGTH_LONG).show();
                        }

                    }

                });

        //  Utils.showKeyboard(mContext);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();
        LinearLayout linearLayout = (LinearLayout) dialog.findViewById(R.id.loading);
        int index = linearLayout.indexOfChild(linearLayout.findViewById(R.id.content_text));
        linearLayout.addView(editText, index + 1);
    }

    public void startSms(String mobile, String text) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("sms:" + mobile));
        intent.putExtra("sms_body", text);
        mContext.startActivity(intent);
        return;
    }

    class LeadsGivenHolder extends RecyclerView.ViewHolder {

        public final View mView;

        @BindView(R.id.name_txtViewLeadsGiven)
        TextView name_txtViewLeadsGiven;
        @BindView(R.id.viewLeadsGiven)
        View viewLeadsGiven;
        @BindView(R.id.leadstatus_textView)
        TextView leadstatus_textView;
        @BindView(R.id.leadid_txtViewLeadsGiven)
        TextView leadid_txtViewLeadsGiven;
        @BindView(R.id.mobile_txtViewLeadsGiven)
        TextView mobile_txtViewLeadsGiven;
        @BindView(R.id.date_txtViewLeadsGiven)
        TextView date_txtViewLeadsGiven;
        @BindView(R.id.txt_leadsGiven)
        TextView firstLetter_txtViewLeadsGiven;
        @BindView(R.id.mobileLayout)
        LinearLayout mobileLayout;
        @BindView(R.id.imgViewSMS)
        AppCompatImageView imgViewSMS;


        public LeadsGivenHolder(View itemView) {
            super(itemView);
            mView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }
}

