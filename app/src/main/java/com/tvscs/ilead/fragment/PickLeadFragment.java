package com.tvscs.ilead.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.Toast;

import com.tvscs.ilead.adapter.PickLeadsAdapter;
import com.tvscs.ilead.helper.SharedPreference;
import com.tvscs.ilead.model.pickleadmodel.PickLeadIDModel;
import com.tvscs.ilead.model.pickleadmodel.PickLeadsModel;
import com.tvscs.ilead.model.pickleadmodel.RefpickleadResult;
import com.tvscs.ilead.model.pickleadmodel.RefpickleadbyIDResult;
import com.tvscs.ilead.retrofit.APICallInterface;
import com.tvscs.ilead.retrofit.ServiceGenerator;
import com.tvscs.ilead.utils.Constants;
import com.tvscs.ilead.utils.LoaderDialog;
import com.tvscs.ilead.utils.RecyclerTouchListener;
import com.tvscs.ilead.utils.Utils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import ilead.tvscs.com.ilead.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tvscs.ilead.utils.Utils.isNetworkAvailable;

public class PickLeadFragment extends Fragment implements Constants {
    private static final int LOAD_LIMIT = 10;
    private static final int DEFAULT_OFFSET = 0;
    private static TextView txtView_noData;
    private final String TAG = PickLeadFragment.class.getSimpleName();
    LoaderDialog viewDialog;
    @BindView(R.id.recyclerViewPickLead)
    RecyclerView recyclerViewPickLead;
    @BindView(R.id.et_search_filter)
    EditText et_filter;
    @BindView(R.id.img_close)
    ImageView img_close;
    private Context mContext;
    private SweetAlertDialog sweetAlertDialog;
    private PickLeadsModel pickLeadsModel;
    private PickLeadsAdapter pickLeadsAdapter;
    private ArrayList<RefpickleadResult> refpickleadResults = null;
    private PickLeadIDModel pickLeadIDModel;
    private ArrayList<RefpickleadbyIDResult> refpickleadbyIDResults = new ArrayList<>();
    private int currentPage = 0;
    private SharedPreference sharedPreference;
    private ViewPageListner viewPageListner;

    public static void setNoDataVisible(int size) {

        if (size == 0) {
            txtView_noData.setVisibility(View.VISIBLE);
        } else {
            txtView_noData.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        sharedPreference = new SharedPreference(mContext);
        viewDialog = new LoaderDialog(mContext);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = null;
        try {

            // Inflate the layout for this fragment
            rootView = inflater.inflate(R.layout.fragment_pick_lead, container, false);
            ButterKnife.bind(this, rootView);
            initViews(rootView);
            setAdapter();
            addTextChangedListener();
            initData();
            setitemClickListener();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        viewPageListner = (ViewPageListner) getActivity();
    }

   /* private void sweetDialog(Context context, final RefpickleadResult refpickleadResult) {
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(confirmMsg)
                .setContentText(refpickleadResult.getLead_id())
                .setCancelText(cancelPick)
                .setConfirmText(confirmPick)
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        pickLeadID(refpickleadResult.getLead_id());
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
    }*/

    private void setitemClickListener() {
        recyclerViewPickLead.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerViewPickLead, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                //sweetDialog(mContext, refpickleadResults.get(position));
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    public void pickLeadID(final String lead_id) {
        if (isNetworkAvailable(mContext)) {
            //Utils.showSpinnerDialog(mContext, false);
            viewDialog.showProgressDialog();
            APICallInterface apiCallInterface;
            pickLeadIDModel = new PickLeadIDModel();
            apiCallInterface = ServiceGenerator.createService(APICallInterface.class);
            pickLeadIDModel.setUser_id(sharedPreference.getPref(USERID, ""));
            pickLeadIDModel.setLead_id(lead_id);
            Call<PickLeadIDModel> call = apiCallInterface.getPickLeadbyId(pickLeadIDModel);
            call.enqueue(new Callback<PickLeadIDModel>() {
                @Override
                public void onResponse(Call<PickLeadIDModel> call, Response<PickLeadIDModel> response) {
                    //Utils.dismisssSpinnerDialog();
                    viewDialog.hideProgressDialog();
                    try {
                        PickLeadIDModel pickLeadIDModels = response.body();

                        if (pickLeadIDModels != null) {
                            String status = pickLeadIDModels.getRefpickleadbyIDResult().get(0).getD_status();
                            if (status.equals(leadPicked)) {
                                Utils.alertSuccess(mContext, success, lead_id + " " + leadPickedMsg);
                                viewPageListner.onRefresh();
                                // LeadsReceivedFragment().callLeadsReceivedService();
                                /*FragmentTransaction ft = getFragmentManager().beginTransaction();
                                ft.detach(PickLeadFragment.this).attach(PickLeadFragment.this).commit();*/
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<PickLeadIDModel> call, Throwable t) {
                    viewDialog.hideProgressDialog();
                    Utils.alertRetrofitExceptionDialog(mContext, t);

                }
            });
        } else {
            Utils.ShowAlertDialog(mContext, internetErrorTitle, internetError);
        }
    }

    private void initData() {
        refpickleadResults.clear();
        getPickLeadListFromApi(DEFAULT_OFFSET);

        pickLeadsAdapter.setOnBottomReachedListener(new PickLeadsAdapter.OnBottomReachedListener() {
            @Override
            public void onBottomReached(int position) {

                if (!pickLeadsModel.getRefpickleadResult().isEmpty()) {
                    if (et_filter.getText().toString().trim().isEmpty())
                        getPickLeadListFromApi(currentPage * LOAD_LIMIT);
                } else {
                    Toast.makeText(getActivity(), noRecordsMsg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initViews(View v) {
        txtView_noData = v.findViewById(R.id.txtView_noData);

    }

    private void getPickLeadListFromApi(final int offset) {

        recyclerViewPickLead.post(new Runnable() {
            @Override
            public void run() {
                if (refpickleadResults.size() != 0) {
                    refpickleadResults.add(null);
                    pickLeadsAdapter.notifyItemInserted(refpickleadResults.size() - 1);
                }
            }
        });

        //Generating more data
        APICallInterface apiCallInterface = ServiceGenerator.createService(APICallInterface.class);

        pickLeadsModel = new PickLeadsModel();
        pickLeadsModel.setUser_id(sharedPreference.getPref(USERID, ""));
        pickLeadsModel.setLead_id("");
        pickLeadsModel.setMobile("");
        pickLeadsModel.setScount(String.valueOf(offset + 1));
        pickLeadsModel.setEcount(String.valueOf(LOAD_LIMIT + offset));

        Call<PickLeadsModel> call = apiCallInterface.getpickLeadsResponse(pickLeadsModel);
        call.enqueue(new Callback<PickLeadsModel>() {
            @Override
            public void onResponse(Call<PickLeadsModel> call, Response<PickLeadsModel> response) {
                try {

                    pickLeadsModel = response.body();
                    if (pickLeadsModel != null) {
                        if (refpickleadResults.size() != 0) {
                            refpickleadResults.remove(refpickleadResults.size() - 1);
                            pickLeadsAdapter.notifyItemRemoved(refpickleadResults.size());
                            setNoDataVisible(refpickleadResults.size());
                        }
                        refpickleadResults.addAll(pickLeadsModel.getRefpickleadResult());
                    }
                    currentPage++;

                    pickLeadsAdapter.notifyDataSetChanged();
                    pickLeadsAdapter.setLoaded();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<PickLeadsModel> call, Throwable t) {
            }
        });
    }

    private void setAdapter() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewPickLead.setLayoutManager(layoutManager);
        recyclerViewPickLead.setHasFixedSize(true);

        refpickleadResults = new ArrayList<>();
        pickLeadsAdapter = new PickLeadsAdapter(this, recyclerViewPickLead, refpickleadResults, (AppCompatActivity) getActivity());
        recyclerViewPickLead.setAdapter(pickLeadsAdapter);
    }

    private void addTextChangedListener() {
        et_filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                pickLeadsAdapter.getFilter().filter(editable);
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

    public interface ViewPageListner {
        void onRefresh();
    }
}
