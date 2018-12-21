package com.tvscs.ilead.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.tvscs.ilead.adapter.FollowupAdapter;
import com.tvscs.ilead.helper.SharedPreference;
import com.tvscs.ilead.model.CommonPojo;
import com.tvscs.ilead.model.FollowupPojo;
import com.tvscs.ilead.retrofit.APICallInterface;
import com.tvscs.ilead.retrofit.APIClient;
import com.tvscs.ilead.utils.LoaderDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ilead.tvscs.com.ilead.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tvscs.ilead.utils.Constants.actionCode;
import static com.tvscs.ilead.utils.Constants.createdBy;
import static com.tvscs.ilead.utils.Constants.createdOn;
import static com.tvscs.ilead.utils.Constants.informationTxt;
import static com.tvscs.ilead.utils.Constants.internetError;
import static com.tvscs.ilead.utils.Constants.internetErrorTitle;
import static com.tvscs.ilead.utils.Constants.leadCategory;
import static com.tvscs.ilead.utils.Constants.message;
import static com.tvscs.ilead.utils.Constants.noRecordsMsg;
import static com.tvscs.ilead.utils.Constants.nxtActCode;
import static com.tvscs.ilead.utils.Constants.nxtActDate;
import static com.tvscs.ilead.utils.Constants.nxtActTime;
import static com.tvscs.ilead.utils.Constants.respLeadId_;
import static com.tvscs.ilead.utils.Constants.resultCode;
import static com.tvscs.ilead.utils.Constants.rowNo;
import static com.tvscs.ilead.utils.Constants.serverErrorMsg;
import static com.tvscs.ilead.utils.Constants.status;
import static com.tvscs.ilead.utils.Constants.tmUserID;
import static com.tvscs.ilead.utils.Utils.ShowAlertDialog;
import static com.tvscs.ilead.utils.Utils.isNetworkAvailable;
import static com.tvscs.ilead.utils.Utils.showErrorDialog;

public class FollowupFragment_BottomSheet extends BottomSheetDialogFragment {

    Context mContext;
    APICallInterface mAPICallInterface;
    SharedPreference preference;
    LoaderDialog viewDialog;
    CommonPojo pojo;
    LinearLayoutManager mLayoutManager;

    boolean showFAB = true;
    ArrayList<String> followupList;
    FollowupAdapter adapter;
    String leadId;

    @BindView(R.id.followup_list)
    RecyclerView followupRecylerView;
    @BindView(R.id.bottomsheet_layout)
    RelativeLayout bottomsheet_layout;
    @BindView(R.id.dismiss)
    ImageButton dismiss;

    static BottomSheetDialogFragment newInstance() {
        return new BottomSheetDialogFragment();
    }

    static FollowupFragment_BottomSheet newInstance(String string) {
        FollowupFragment_BottomSheet f = new FollowupFragment_BottomSheet();
        Bundle args = new Bundle();
        args.putString(respLeadId_, string);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
        mContext = getActivity();
        preference = new SharedPreference(mContext);
        mAPICallInterface = APIClient.getAPIService();
        viewDialog = new LoaderDialog(mContext);
        followupList = new ArrayList<String>();
    }


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheets_main, container, false);
        ButterKnife.bind(this, v);
        Bundle bundle = getArguments();
        if (bundle != null) {
            leadId = bundle.getString(respLeadId_, null);
        }
        getFollowupFAB(leadId);

        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return v;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog d = super.onCreateDialog(savedInstanceState);
        d.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;
                FrameLayout bottomSheet = (FrameLayout) d.findViewById(android.support.design.R.id.design_bottom_sheet);
                BottomSheetBehavior behaviour = BottomSheetBehavior.from(bottomSheet);
                behaviour.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                    @Override
                    public void onStateChanged(@NonNull View bottomSheet, int newState) {
                        if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                            //handleUserExit();
                            dismiss();
                        }
                    }

                    @Override
                    public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                    }
                });
            }
        });
        return d;
    }

    private void getFollowupFAB(String Leadid) {
        if (isNetworkAvailable(mContext)) {
            try {
                viewDialog.showProgressDialog();
                pojo = new CommonPojo();
                pojo.setLead_id(Leadid);
                Call<ResponseBody> responseBodyCall = mAPICallInterface.getFollowupData(pojo);
                responseBodyCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response != null) {
                            if (response.code() == 200) {
                                viewDialog.hideProgressDialog();
                                try {
                                    String responseValue = response.body().string();
                                    JSONArray jsonArray = new JSONArray(responseValue);
                                    if (jsonArray != null) {
                                        if (!jsonArray.getJSONObject(0).getString(respLeadId_).equals("-1")) {
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                followupList.add(jsonArray.getString(i));
                                            }
                                            bottomsheet_layout.setVisibility(View.VISIBLE);
                                            createPopupView(jsonArray);
                                        } else {
                                            dismiss();
                                            bottomsheet_layout.setVisibility(View.GONE);
                                            ShowAlertDialog(getActivity(), message, noRecordsMsg);
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                showErrorDialog(mContext, informationTxt, serverErrorMsg);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        viewDialog.hideProgressDialog();
                    }
                });


            } catch (Exception e) {
                viewDialog.hideProgressDialog();
                e.printStackTrace();
            }
        } else {
            showErrorDialog(mContext, internetErrorTitle, internetError);
        }
    }

    private ArrayList<FollowupPojo> getDataSet(JSONArray temp) throws JSONException {
        @SuppressWarnings("rawtypes")
        ArrayList results = new ArrayList<FollowupPojo>();
        for (int index = 0; index < temp.length(); index++) {
            JSONObject tempobj = temp.getJSONObject(index);
            FollowupPojo obj = new FollowupPojo(tempobj.getString(respLeadId_), tempobj.getString(rowNo),
                    tempobj.getString(tmUserID), tempobj.getString(actionCode),
                    tempobj.getString(createdOn), tempobj.getString(createdBy),
                    tempobj.getString(leadCategory), tempobj.getString(nxtActCode),
                    tempobj.getString(nxtActDate), tempobj.getString(nxtActTime),
                    tempobj.getString(resultCode), tempobj.getString(status));
            results.add(index, obj);
        }
        return results;
    }

    private void createPopupView(JSONArray jsonArray) {
        try {
            followupRecylerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
            followupRecylerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(getActivity());
            followupRecylerView.setLayoutManager(mLayoutManager);
            followupRecylerView.setItemAnimator(new DefaultItemAnimator());


           /* LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            followupRecylerView.setLayoutManager(linearLayoutManager);
            followupRecylerView.setItemAnimator(new DefaultItemAnimator());
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(followupRecylerView.getContext(),
                    linearLayoutManager.getOrientation());
            followupRecylerView.addItemDecoration(dividerItemDecoration);*/
            adapter = new FollowupAdapter(getDataSet(jsonArray));
            followupRecylerView.setAdapter(adapter);
            followupRecylerView.scrollToPosition(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        //Toast.makeText(MainApp.get(), "CANCEL", Toast.LENGTH_SHORT).show();
    }
}