package com.tvscs.ilead.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tvscs.ilead.adapter.NotificationAdapter;
import com.tvscs.ilead.helper.SharedPreference;
import com.tvscs.ilead.model.CommonPojo;
import com.tvscs.ilead.model.NotificationPojo;
import com.tvscs.ilead.retrofit.APICallInterface;
import com.tvscs.ilead.retrofit.APIClient;
import com.tvscs.ilead.utils.LoaderDialog;
import com.tvscs.ilead.utils.MyBottomMenu;
import com.tvscs.ilead.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ilead.tvscs.com.ilead.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tvscs.ilead.utils.Utils.ShowAlertDialog;
import static com.tvscs.ilead.utils.Utils.isNetworkAvailable;

public class NotificationActivity extends BaseActivity {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.img_toolbar)
    ImageView imgToolbar;
    @BindView(R.id.notifcation_imageView)
    ImageView notification_imageView;
    @BindView(R.id.toolBarText)
    TextView toolBarText;
    @BindView(R.id.layout_toolBar)
    LinearLayout mlayout_toolBar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.bottom_menu_quick)
    LinearLayout bottom_menu_quick;
    @BindView(R.id.bottom_menu_leadsgiven)
    LinearLayout bottom_menu_leadsgiven;
    @BindView(R.id.bottom_menu_leadsreceived)
    LinearLayout bottom_menu_leadsreceived;
    @BindView(R.id.bottom_menu_dashboard)
    LinearLayout bottom_menu_dashboard;
    @BindView(R.id.bottom_menu_logout)
    LinearLayout bottom_menu_logout;
    @BindView(R.id.img_bottom_menu_quick)
    ImageView img_bottom_menu_quick;
    @BindView(R.id.img_bottom_menu_leadsgiven)
    ImageView img_bottom_menu_leadsgiven;
    @BindView(R.id.img_bottom_menu_leadsreceived)
    ImageView img_bottom_menu_leadsreceived;
    @BindView(R.id.img_bottom_menu_dashboard)
    ImageView img_bottom_menu_dashboard;
    @BindView(R.id.img_bottom_menu_logout)
    ImageView img_bottom_menu_logout;
    @BindView(R.id.notifyrecycler_view)
    RecyclerView notifyrecycler_view;
    @BindString(R.string.notifications)
    String title_notification;
    Context mContext;
    SharedPreference preference;
    APICallInterface mAPICallInterface;
    LoaderDialog viewDialog;
    Bundle bundle;
    JSONArray leadjsonArray;
    String intentleadString;
    ArrayList<String> notificationList;
    NotificationAdapter notificationAdapter;
    CommonPojo pojo;

    @Override
    public int activityView() {
        return R.layout.activity_notification;
    }

    @Override
    public void activityCreated() {
        ButterKnife.bind(this);
        mContext = NotificationActivity.this;
        preference = new SharedPreference(mContext);
        mAPICallInterface = APIClient.getAPIService();
        viewDialog = new LoaderDialog(mContext);
        new MyBottomMenu(this, bottomMenuDefaultValue);
        bundle = getIntent().getExtras();
        notificationList = new ArrayList<String>();
        initViews();
    }

    private void initViews() {
        setToolBar();
        if (bundle != null) {
            intentleadString = bundle.getString(sharedPrefLeadData);
            try {

                leadjsonArray = new JSONArray(intentleadString);
                for (int i = 0; i < leadjsonArray.length(); i++) {
                    notificationList.add(leadjsonArray.getString(i));
                }
                creatNotifyView(leadjsonArray);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setToolBar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Utils.setSvgImageviewDrawable(imgToolbar, mContext, R.drawable.ic_back_arrow);
        notification_imageView.setBackgroundResource(R.drawable.notification_selected);
        toolBarText.setText(title_notification);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
    }


    @OnClick({R.id.layout_toolBar})
    public void layoutClicked(LinearLayout linearLayout) {
        switch (linearLayout.getId()) {
            case R.id.layout_toolBar:
                startActivity(new Intent(mContext, HomeActivity.class));
                finish();
                break;
        }
    }


    private void creatNotifyView(JSONArray jsonArray1) {
        try {
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
            notifyrecycler_view.setLayoutManager(mLayoutManager);
            notifyrecycler_view.setItemAnimator(new DefaultItemAnimator());
             /*RecyclerView.ItemDecoration decorator = new DividerItemDecoration(ContextCompat.getDrawable(mContext, R.drawable.line_dashed));
            notifyrecycler_view.addItemDecoration(decorator);*/
            notificationAdapter = new NotificationAdapter(getDataSet(jsonArray1), mContext);
            notifyrecycler_view.setAdapter(notificationAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @SuppressWarnings("unchecked")
    private ArrayList<NotificationPojo> getDataSet(JSONArray temp) throws JSONException {
        @SuppressWarnings("rawtypes")
        ArrayList results = new ArrayList<NotificationPojo>();
        for (int index = 0; index < temp.length(); index++) {
            JSONObject tempobj = temp.getJSONObject(index);
            NotificationPojo obj = new NotificationPojo(tempobj.getString(lead_no), tempobj.getString(status),
                    tempobj.getString(first_name), tempobj.getString(Next_Action));
            results.add(index, obj);
        }
        return results;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void onClickCalled(NotificationPojo notificationPojo) {
        String leadId = notificationPojo.getLead_no();
        getLeadUpdate(leadId);
    }


    private void getLeadUpdate(final String leadId) {
        if (isNetworkAvailable(mContext)) {
            try {
                viewDialog.showProgressDialog();
                pojo = new CommonPojo();
                pojo.setUser_id(preference.getPref(USERID, ""));
                pojo.setLead_id(leadId);
                pojo.setMobile("");
                Call<ResponseBody> responseBodyCall = mAPICallInterface.getleadsReceived(pojo);
                responseBodyCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response != null) {
                            if (response.code() == 200) {
                                viewDialog.hideProgressDialog();
                                try {
                                    String responseValue = response.body().string();
                                    JSONArray jsonArray = new JSONArray(responseValue);
                                    try {
                                        if (jsonArray.length() > 0) {
                                            JSONObject jsonObject = new JSONObject(jsonArray.getString(0));
                                            preference.setPref(sharedPrefclickUpdate, jsonObject.toString());
                                            Intent in = new Intent(mContext, LeadUpdateActivity.class);
                                            in.putExtra(respleadId_, leadId);
                                            in.putExtra(className, notification);
                                            startActivity(in);
                                        } else {
                                            viewDialog.hideProgressDialog();
                                            ShowAlertDialog(mContext, message, noRecordsMsg);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        viewDialog.hideProgressDialog();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            viewDialog.hideProgressDialog();
            ShowAlertDialog(mContext, internetErrorTitle, internetError);
        }
    }


}
