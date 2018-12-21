package com.tvscs.ilead.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tvscs.ilead.adapter.LeadsGivenAdapter;
import com.tvscs.ilead.helper.SharedPreference;
import com.tvscs.ilead.model.leadgivenmodel.LeadsGivenModel;
import com.tvscs.ilead.model.leadgivenmodel.RefLeadsgivenResult;
import com.tvscs.ilead.retrofit.APICallInterface;
import com.tvscs.ilead.retrofit.ServiceGenerator;
import com.tvscs.ilead.utils.Constants;
import com.tvscs.ilead.utils.LoaderDialog;
import com.tvscs.ilead.utils.MyBottomMenu;
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

import static com.tvscs.ilead.utils.Utils.ShowAlertDialog;
import static com.tvscs.ilead.utils.Utils.isNetworkAvailable;

public class LeadsGivenActivity extends BaseActivity {

    @BindView(R.id.recyclerViewLeadsGiven)
    RecyclerView recyclerViewLeadsGiven;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.layout_toolBar)
    LinearLayout mlayout_toolBar;
    @BindView(R.id.et_search_filter)
    EditText et_filter;
    @BindView(R.id.img_close)
    ImageView img_close;
    @BindView(R.id.img_toolbar)
    AppCompatImageView imgToolbar;
    @BindView(R.id.toolBarText)
    TextView toolBarText;
    @BindView(R.id.notification_toolbar)
    LinearLayout notification_toolbar;
    @BindView(R.id.notifcation_imageView)
    ImageView notification_imageView;

    @BindView(R.id.txtView_noData)
    TextView txtView_noData;


    private Context mContext;
    private LeadsGivenModel leadsGivenModel;
    private LeadsGivenAdapter leadsGivenAdapter;
    private SharedPreference sharedPreference;
    private LoaderDialog viewDialog;
    private ArrayList<RefLeadsgivenResult> refLeadsgivenResults = new ArrayList<>();

    @Override
    public int activityView() {
        return R.layout.activity_leads_given;
    }

    @Override
    public void activityCreated() {
        initViews();
        changeListener();
        setToolBar();
        callGetService();
    }


    private void initViews() {
        ButterKnife.bind(this);
        mContext = LeadsGivenActivity.this;
        viewDialog = new LoaderDialog(this);
        sharedPreference = new SharedPreference(mContext);
        new MyBottomMenu(this, bottomMenuleadsGivenValue);
    }

    @OnClick(R.id.notification_toolbar)
    public void onClick(View view) {
        try {
            if (isNetworkAvailable(mContext)) {
                Utils.NotificationUpdates(mContext, sharedPreference.getPref(USERID, ""));

            } else {
                ShowAlertDialog(mContext, internetErrorTitle, internetError);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setToolBar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        notification_imageView.setBackgroundResource(R.drawable.notification);
        Utils.setSvgImageviewDrawable(imgToolbar, mContext, R.drawable.ic_back_arrow);
        toolBarText.setText(Constants.LEADS_GIVEN);
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
                if (leadsGivenAdapter != null)
                    leadsGivenAdapter.getFilter().filter(editable);
            }
        });
    }

    /**
     * Here Calling Network API
     */
    private void callGetService() {
        if (isNetworkAvailable(mContext)) {
            viewDialog.showProgressDialog();
            APICallInterface apiCallInterface;
            leadsGivenModel = new LeadsGivenModel();
            apiCallInterface = ServiceGenerator.createService(APICallInterface.class);
            leadsGivenModel.setUser_id(sharedPreference.getPref(USERID, ""));
            leadsGivenModel.setLead_id("");
            leadsGivenModel.setMobile("");
            Call<LeadsGivenModel> call = apiCallInterface.getLeadsGivenResponse(leadsGivenModel);
            call.enqueue(new Callback<LeadsGivenModel>() {
                @Override
                public void onResponse(Call<LeadsGivenModel> call, Response<LeadsGivenModel> response) {
                    viewDialog.hideProgressDialog();
                    try {
                        LeadsGivenModel leadsGivenModels = response.body();

                        if (response.code() == (Integer.parseInt(SUCCESSCODE))) {
                            if (leadsGivenModels != null) {
                                refLeadsgivenResults = leadsGivenModels.getRefLeadsgivenResult();
                                leadsGivenAdapter = new LeadsGivenAdapter(LeadsGivenActivity.this, refLeadsgivenResults, leadsGivenModels);
                                LinearLayoutManager llm = new LinearLayoutManager(LeadsGivenActivity.this);
                                llm.setOrientation(LinearLayoutManager.VERTICAL);
                                recyclerViewLeadsGiven.setLayoutManager(llm);
                                recyclerViewLeadsGiven.setAdapter(leadsGivenAdapter);
                            }
                        } else {
                            viewDialog.hideProgressDialog();
                            showErrorDialog(mContext, internetErrorTitle, response.code() + " Error");

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<LeadsGivenModel> call, Throwable t) {
                    viewDialog.hideProgressDialog();
                    Utils.alertRetrofitExceptionDialog(mContext, t);

                }
            });
        } else {
            Utils.showErrorDialog(mContext, internetErrorTitle, internetError);
        }
    }


    @OnClick({R.id.img_close, R.id.layout_toolBar, R.id.bottom_menu_quick, R.id.bottom_menu_leadsgiven, R.id.bottom_menu_leadsreceived, R.id.bottom_menu_dashboard, R.id.bottom_menu_logout})
    public void layoutClicked(View view) {
        switch (view.getId()) {
            case R.id.img_close:
                et_filter.getText().clear();
                break;
            case R.id.layout_toolBar:
                startActivity(new Intent(LeadsGivenActivity.this, HomeActivity.class));
                finish();
                break;
            case R.id.bottom_menu_quick:
                startActivity(new Intent(LeadsGivenActivity.this, QuickLeadActivity.class));
                finish();
                break;
            case R.id.bottom_menu_leadsgiven:
                startActivity(new Intent(LeadsGivenActivity.this, LeadsGivenActivity.class));
                finish();
                break;
            case R.id.bottom_menu_leadsreceived:
                startActivity(new Intent(LeadsGivenActivity.this, PickLeadsActivity.class));
                finish();
                break;
            case R.id.bottom_menu_dashboard:
                startActivity(new Intent(LeadsGivenActivity.this, DashboardActivity.class));
                finish();
                break;
            case R.id.bottom_menu_logout:
                alertConfirmLogout();
                break;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Utils.closeKeyboard(this);
        Intent in = new Intent(mContext, HomeActivity.class);
        startActivity(in);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void setNoDataVisible(int size) {
        if (size == 0) {
            txtView_noData.setVisibility(View.VISIBLE);
        } else {
            txtView_noData.setVisibility(View.GONE);
        }
    }

    private void alertConfirmLogout() {
        SweetAlertDialog dialog = new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE);
        dialog
                .setTitleText(confirmMsg)
                .setContentText(logoutMsg)
                .setConfirmText(OkTxt)
                .setCancelText(cancelTxt)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sharedPreference.logoutUser();
                        sDialog.dismissWithAnimation();
                    }
                })
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .show();

    }

    public void showErrorDialog(final Context context, final String title, String message) {
        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(title)
                .setContentText(message)
                .setConfirmText("Ok")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        startActivity(new Intent(LeadsGivenActivity.this, HomeActivity.class));
                        finish();
                    }
                })
                .show();
    }
}
