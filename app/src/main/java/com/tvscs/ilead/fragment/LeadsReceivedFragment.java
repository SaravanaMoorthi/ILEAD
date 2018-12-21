package com.tvscs.ilead.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tvscs.ilead.adapter.LeadsReceivedAdapter;
import com.tvscs.ilead.helper.SharedPreference;
import com.tvscs.ilead.model.leadsreceivedmodel.LeadsReceivedModel;
import com.tvscs.ilead.retrofit.APICallInterface;
import com.tvscs.ilead.retrofit.ServiceGenerator;
import com.tvscs.ilead.utils.Constants;
import com.tvscs.ilead.utils.LoaderDialog;
import com.tvscs.ilead.utils.Utils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ilead.tvscs.com.ilead.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tvscs.ilead.utils.Utils.isNetworkAvailable;

public class LeadsReceivedFragment extends Fragment implements Constants {

    public static LeadsReceivedAdapter leadsReceivedAdapter;
    private static TextView txtView_noData;
    @BindView(R.id.recyclerViewLeadsReceived)
    RecyclerView recyclerReceived;
    @BindView(R.id.et_search_filter)
    EditText et_filter;
    @BindView(R.id.img_close)
    ImageView img_close;
    LoaderDialog viewDialog;
    private Context mContext;
    private LeadsReceivedModel leadsReceivedModel;
    private ArrayList<LeadsReceivedModel> refLeadsreceivedResults = null;
    private SharedPreference sharedPreference;

    public static void setNoDataVisible(int size) {

        if (size == 0) {
            txtView_noData.setVisibility(View.VISIBLE);
        } else {
            txtView_noData.setVisibility(View.GONE);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leads_received, container, false);
        ButterKnife.bind(this, view);
        initViews(view);
        return view;
    }

    private void initViews(View v) {
        mContext = v.getContext();
        sharedPreference = new SharedPreference(mContext);
        viewDialog = new LoaderDialog(mContext);
        txtView_noData = v.findViewById(R.id.txtView_noData);
        callLeadsReceivedService();
        changeListener();
    }

    /**
     * Here Calling Network API
     */
    public void callLeadsReceivedService() {
        if (isNetworkAvailable(mContext)) {
            viewDialog.showProgressDialog();
            APICallInterface apiCallInterface;
            leadsReceivedModel = new LeadsReceivedModel();
            apiCallInterface = ServiceGenerator.createService(APICallInterface.class);
            leadsReceivedModel.setUser_id(sharedPreference.getPref(USERID, ""));
            leadsReceivedModel.setLead_id("");
            leadsReceivedModel.setMobile("");
            Call<ArrayList<LeadsReceivedModel>> call = apiCallInterface.getLeadsReceievedResponse(leadsReceivedModel);
            call.enqueue(new Callback<ArrayList<LeadsReceivedModel>>() {
                @Override
                public void onResponse(Call<ArrayList<LeadsReceivedModel>> call, Response<ArrayList<LeadsReceivedModel>> response) {
                    viewDialog.hideProgressDialog();
                    try {
                        ArrayList<LeadsReceivedModel> leadsReceivedModels = response.body();
                        if (leadsReceivedModels != null) {
                            refLeadsreceivedResults = new ArrayList<>();
                            refLeadsreceivedResults.addAll(leadsReceivedModels);
                            leadsReceivedAdapter = new LeadsReceivedAdapter(getActivity(), refLeadsreceivedResults);
                            recyclerReceived.setLayoutManager(new LinearLayoutManager(getActivity()));
                            recyclerReceived.setAdapter(leadsReceivedAdapter);
                            setNoDataVisible(refLeadsreceivedResults.size());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<LeadsReceivedModel>> call, Throwable t) {
                    viewDialog.hideProgressDialog();
                    Utils.alertRetrofitExceptionDialog(mContext, t);

                }
            });
        } else {
            Utils.ShowAlertDialog(mContext, internetErrorTitle, internetError);

        }
    }

    private void changeListener() {
        et_filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    leadsReceivedAdapter.getFilter().filter(editable);
                } catch (NullPointerException n) {
                    n.printStackTrace();
                }
            }
        });
    }

    @OnClick({R.id.img_close})
    public void viewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_close:
                et_filter.getText().clear();
                break;
        }
    }

    public void update() {
        callLeadsReceivedService();
    }
}
