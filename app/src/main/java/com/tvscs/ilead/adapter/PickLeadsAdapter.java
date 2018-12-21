package com.tvscs.ilead.adapter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tvscs.ilead.fragment.PickLeadFragment;
import com.tvscs.ilead.model.pickleadmodel.RefpickleadResult;
import com.tvscs.ilead.utils.Constants;
import com.tvscs.ilead.utils.PermissionInterface;
import com.tvscs.ilead.utils.Utils;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import ilead.tvscs.com.ilead.R;

public class PickLeadsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable, Constants, PermissionInterface {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnBottomReachedListener onBottomReachedListener;
    private boolean isLoading;
    private AppCompatActivity activity;
    private ArrayList<RefpickleadResult> mRefPickLeadResults;
    private ArrayList<RefpickleadResult> mFilteredList;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private RecyclerView recyclerView;
    private PickLeadFragment mPickLeadFragment;
    private int CALL_PERMISSION_CODE = 23;

    public PickLeadsAdapter(PickLeadFragment pickLeadFragment, RecyclerView recyclerView, ArrayList<RefpickleadResult> refPickLeadResults, AppCompatActivity activity) {
        mPickLeadFragment = pickLeadFragment;
        this.mRefPickLeadResults = refPickLeadResults;
        this.mFilteredList = refPickLeadResults;
        this.activity = activity;
        this.recyclerView = recyclerView;

    }

    public void setOnBottomReachedListener(OnBottomReachedListener onBottomReachedListener) {
        this.onBottomReachedListener = onBottomReachedListener;
    }

    @Override
    public int getItemViewType(int position) {
        return mFilteredList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(activity).inflate(R.layout.item_list_pickleads, parent, false);
            return new PickLeadsHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(activity).inflate(R.layout.process_spinner, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (position == mFilteredList.size() - 1) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            totalItemCount = linearLayoutManager.getItemCount();
            lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                if (onBottomReachedListener != null) {
                    onBottomReachedListener.onBottomReached(position);
                }
                isLoading = true;
            }
        }

        if (holder instanceof PickLeadsHolder) {
            PickLeadsHolder userViewHolder = (PickLeadsHolder) holder;

            String date = mFilteredList.get(position).getCreated_on();
            String[] parts = date.split(" ");

            Utils.setValueInTextview(userViewHolder.txtViewName, mFilteredList.get(position).getFirst_name());
            Utils.setValueInTextview(userViewHolder.leadstatus_textView, mFilteredList.get(position).getStatus());
            Utils.setValueInTextview(userViewHolder.txtViewLeadId, mFilteredList.get(position).getLead_id());
            Utils.setValueInTextview(userViewHolder.txtViewMobile, mFilteredList.get(position).getPhone_mobile1());
            Utils.setValueInTextview(userViewHolder.txtViewDate, parts[0]);
            Utils.setValueInTextview(userViewHolder.txtViewFirstLetter, mFilteredList.get(position).getFirst_name().substring(0, 1));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sweetDialog(activity, mFilteredList.get(position));
                }
            });

            ((PickLeadsHolder) holder).item_call_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog_Call(mFilteredList.get(position));
                }
            });

            setTypeface((PickLeadsHolder) holder);
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            //loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mFilteredList = mRefPickLeadResults;
                } else {
                    ArrayList<RefpickleadResult> filteredList = new ArrayList<>();
                    for (RefpickleadResult row : mRefPickLeadResults) {
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
                try {
                    mFilteredList = (ArrayList<RefpickleadResult>) filterResults.values;
                    notifyDataSetChanged();
                    setLoaded();

                    PickLeadFragment.setNoDataVisible(mFilteredList.size());
                } catch (NullPointerException n) {
                    n.printStackTrace();
                }

            }
        };
    }


    private void setTypeface(PickLeadsHolder holder) {
        Typeface typefaceRegular = Typeface.createFromAsset(activity.getAssets(), Constants.TYPEFACE_REGULAR);
        Typeface typefaceBold = Typeface.createFromAsset(activity.getAssets(), Constants.TYPEFACE_BOLD);
        //holder.txtViewName.setTypeface(typefaceBold);
        holder.txtViewLeadId.setTypeface(typefaceRegular);
        holder.txtViewDate.setTypeface(typefaceRegular);
        holder.txtViewMobile.setTypeface(typefaceRegular);
        holder.txtViewFirstLetter.setTypeface(typefaceRegular);
    }

    @Override
    public int getItemCount() {
        return mFilteredList == null ? 0 : mFilteredList.size();
    }

    public void setLoaded() {
        isLoading = false;
    }

    public void alertDialog_Call(final RefpickleadResult refLeadsgivenResult) {
        new SweetAlertDialog(activity, SweetAlertDialog.NORMAL_TYPE)
                .setTitleText(confirmMsg)
                .setContentText(callDialogTitle + refLeadsgivenResult.getFirst_name())
                .setCancelText(cancelTxt)
                .setConfirmText(callDialogconfirmPick)
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        if (isCallAllowed()) {
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + refLeadsgivenResult.getPhone_mobile1()));
                            activity.startActivity(intent);
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
        int result = ContextCompat.checkSelfPermission((Activity) activity, Manifest.permission.CALL_PHONE);
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;
        return false;
    }

    private void requestStoragePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) activity, Manifest.permission.CALL_PHONE)) {
        }
        ActivityCompat.requestPermissions((Activity) activity, new String[]{Manifest.permission.CALL_PHONE}, CALL_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CALL_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText((Activity) activity, "Permission granted now you can call", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText((Activity) activity, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void sweetDialog(Context context, final RefpickleadResult refpickleadResult) {
        new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE)
                .setTitleText(confirmMsg)
                .setContentText(PickMsg + refpickleadResult.getLead_id())
                .setCancelText(cancelPick)
                .setConfirmText(confirmPick)
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        mPickLeadFragment.pickLeadID(refpickleadResult.getLead_id());
                        sweetAlertDialog.cancel();
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

    public interface OnBottomReachedListener {
        void onBottomReached(int position);
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        //public com.github.rahatarmanahmed.cpv.CircularProgressView progressBar;
        ProgressBar progressBar;

        public LoadingViewHolder(View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.progress_spinner);
        }
    }

    public class PickLeadsHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txtViewName)
        TextView txtViewName;
        @BindView(R.id.leadstatus_textView)
        TextView leadstatus_textView;
        @BindView(R.id.txtViewLeadId)
        TextView txtViewLeadId;
        @BindView(R.id.txtViewDate)
        TextView txtViewDate;
        @BindView(R.id.txtViewMobile)
        TextView txtViewMobile;
        @BindView(R.id.txtViewFirstLetter)
        TextView txtViewFirstLetter;
        @BindView(R.id.item_call_layout)
        LinearLayout item_call_layout;


        public PickLeadsHolder(View view) {
            super(view);
            view = itemView;
            ButterKnife.bind(this, view);
        }
    }

}