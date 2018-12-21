package com.tvscs.ilead.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ravindu1024.indicatorlib.ViewPagerIndicator;
import com.tvscs.ilead.adapter.CustomSpinerAdapter;
import com.tvscs.ilead.adapter.HomeAdapter;
import com.tvscs.ilead.adapter.HomeViewPagerAdapter;
import com.tvscs.ilead.helper.SharedPreference;
import com.tvscs.ilead.model.MainMenu;
import com.tvscs.ilead.model.homechartmodel.HomeChartsModel;
import com.tvscs.ilead.model.homechartmodel.RefConenewResult;
import com.tvscs.ilead.model.homechartmodel.Lstuserdtls;
import com.tvscs.ilead.model.homechartmodel.homesubchartmodel.HomeSubChartModel;
import com.tvscs.ilead.retrofit.APICallInterface;
import com.tvscs.ilead.retrofit.APIClient;
import com.tvscs.ilead.retrofit.ServiceGenerator;
import com.tvscs.ilead.utils.ApkFileDownload;
import com.tvscs.ilead.utils.LoaderDialog;
import com.tvscs.ilead.utils.RecyclerTouchListener;
import com.tvscs.ilead.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import ilead.tvscs.com.ilead.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tvscs.ilead.utils.Utils.ShowAlertDialog;
import static com.tvscs.ilead.utils.Utils.getVersionNumber;
import static com.tvscs.ilead.utils.Utils.isNetworkAvailable;
import static com.tvscs.ilead.utils.Utils.showDialogOK;
import static com.tvscs.ilead.utils.Utils.showErrorDialog;

public class HomeActivity extends BaseActivity {

    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    @BindView(R.id.recyclerViewHome)
    RecyclerView recyclerViewHome;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.img_toolbar)
    ImageView imgToolbar;
    @BindView(R.id.logout_toolbar)
    ImageView logout_toolbar;
    @BindView(R.id.toolBarText)
    TextView toolBarText;
    @BindString(R.string.appname)
    String title_appname;
    @BindView(R.id.viewPagerHomeChart)
    ViewPager viewPagerHomeChart;
    @BindView(R.id.pager_indicator)
    ViewPagerIndicator viewPagerIndicator;
    @BindView(R.id.notifcation_imageView)
    ImageView notification_imageView;
    @BindView(R.id.notification_toolbar)
    LinearLayout notification_toolbar;
    @BindView(R.id.spinnerHomepage)
    Spinner userSpinner;
    MainMenu mainMenuResponse;
    APICallInterface mAPICallInterface;
    LoaderDialog viewDialog;
    private ArrayList<MainMenu> mainMenuArrayList;
    private SharedPreference sharedPreference;
    private Context mContext;
    private HomeChartsModel homeChartsModel;

    private ArrayList<String> userCategoriesArray = new ArrayList<>();

    @Override
    public int activityView() {
        return R.layout.activity_home;
    }

    @Override
    public void activityCreated() {
        ButterKnife.bind(this);
        mContext = HomeActivity.this;
        sharedPreference = new SharedPreference(mContext);
        viewDialog = new LoaderDialog(mContext);
        mAPICallInterface = APIClient.getAPIService();
        setToolBar();
        //checkJson();
        loadData();
        setAdapter();
        setClickLisstener();
        callGetRefcone();
        UpdateVersion();


    }

    private void callGetRefcone() {
        if (isNetworkAvailable(mContext)) {
            viewDialog.showProgressDialog();
            APICallInterface apiCallInterface;
            homeChartsModel = new HomeChartsModel();
            apiCallInterface = ServiceGenerator.createService(APICallInterface.class);
            homeChartsModel.setUser_id(sharedPreference.getPref(USERID, ""));
            //homeChartsModel.setUser_id("5021037");
            Call<HomeChartsModel> call = apiCallInterface.getRefcone(homeChartsModel);
            call.enqueue(new Callback<HomeChartsModel>() {
                @Override
                public void onResponse(Call<HomeChartsModel> call, Response<HomeChartsModel> response) {
                    viewDialog.hideProgressDialog();
                    try {
                        HomeChartsModel homeChartsModel = response.body();
                        if (homeChartsModel != null) {

                            if (response.code() == (Integer.parseInt(SUCCESSCODE))) {
                                if (!homeChartsModel.getRefConeResult().get(0).getLstuserdtls().isEmpty()) {
                                    userSpinner.setVisibility(View.VISIBLE);
                                    setUserSpinner(homeChartsModel.getRefConeResult().get(0).getLstuserdtls(), mContext);

                                } else {
                                    userSpinner.setVisibility(View.GONE);

                                    viewPagerHomeChart.setAdapter(new HomeViewPagerAdapter(HomeActivity.this, homeChartsModel.getRefConeResult().get(0).getRefConenewResult()));
                                    viewPagerHomeChart.setOffscreenPageLimit(homeChartsModel.getRefConeResult().get(0).getRefConenewResult().size());
                                    setPageIndicator();

                                    NUM_PAGES = homeChartsModel.getRefConeResult().get(0).getRefConenewResult().size();

                                    final Timer swipeTimer = new Timer();

                                    // Auto start of viewpager
                                    final Handler handler = new Handler();
                                    final Runnable Update = new Runnable() {
                                        public void run() {
                                            if (currentPage == NUM_PAGES) {
                                                currentPage = 0;
                                                swipeTimer.cancel();
                                            }
                                            viewPagerHomeChart.setCurrentItem(currentPage++, true);
                                        }
                                    };

                                    swipeTimer.schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            handler.post(Update);
                                        }
                                    }, 4000, 4000);


                                }


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
                public void onFailure(Call<HomeChartsModel> call, Throwable t) {
                    viewDialog.hideProgressDialog();
                    Utils.alertRetrofitExceptionDialog(mContext, t);
                }
            });
        } else {
            Utils.ShowAlertDialog(mContext, internetErrorTitle, internetError);
        }
    }

    private void setPageIndicator() {
        try {
            if (viewPagerHomeChart.getAdapter().getCount() > 1) {
                viewPagerIndicator.setVisibility(View.VISIBLE);
                viewPagerIndicator.setPager(viewPagerHomeChart);
            } else {
                viewPagerIndicator.setVisibility(View.GONE);
            }
        } catch (Exception e) {

        }
    }

    public void setUserSpinner(final ArrayList<Lstuserdtls> userCategoriesArray, final Context mContext) {

        setSpinnerAdapter(userSpinner, userCategoriesArray, mContext);

        userSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                String mSpinnerValue = userCategoriesArray.get(position).getUserid();
                //Toast.makeText(mContext, mSpinnerValue, Toast.LENGTH_LONG).show();
                callGetUserConeDetails(mSpinnerValue);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void callGetUserConeDetails(String mSpinnerValue) {

        if (isNetworkAvailable(mContext)) {
            viewDialog.showProgressDialog();
            APICallInterface apiCallInterface;
            final HomeSubChartModel homeSubChartModel = new HomeSubChartModel();
            apiCallInterface = ServiceGenerator.createService(APICallInterface.class);
            //  homeChartsModel.setUser_id(sharedPreference.getPref(USERID, ""));
            homeSubChartModel.setUser_id(mSpinnerValue);
            Call<HomeSubChartModel> call = apiCallInterface.getUsercone(homeSubChartModel);
            call.enqueue(new Callback<HomeSubChartModel>() {
                @Override
                public void onResponse(Call<HomeSubChartModel> call, Response<HomeSubChartModel> response) {
                    Log.e("dfshjh", "mLstconedtls" + response.body());
                    viewDialog.hideProgressDialog();
                    try {
                        HomeSubChartModel getRefConenewResult = response.body();

                        ArrayList<RefConenewResult> RefConenewResult = getRefConenewResult.getRefConenewResult();

                        Log.e("dfshjh", "mLstconedtls" + getRefConenewResult);
                        if (RefConenewResult != null) {


                            viewPagerHomeChart.setAdapter(new HomeViewPagerAdapter(HomeActivity.this, RefConenewResult));
                            Log.e("consss", "consss" + getRefConenewResult);
                            viewPagerHomeChart.setOffscreenPageLimit(RefConenewResult.size());
                            viewPagerHomeChart.getAdapter().notifyDataSetChanged();
                            setPageIndicator();

                            NUM_PAGES = RefConenewResult.size();

                            final Timer swipeTimer = new Timer();

                            // Auto start of viewpager
                            final Handler handler = new Handler();
                            final Runnable Update = new Runnable() {
                                public void run() {
                                    if (currentPage == NUM_PAGES) {
                                        currentPage = 0;
                                        swipeTimer.cancel();
                                    }
                                    viewPagerHomeChart.setCurrentItem(currentPage++, true);
                                }
                            };

                            swipeTimer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    handler.post(Update);
                                }
                            }, 4000, 4000);


                        }


                       /* } else {
                            viewDialog.hideProgressDialog();
                            showErrorDialog(mContext, internetErrorTitle, response.code() + " Error");

                        }*/
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<HomeSubChartModel> call, Throwable t) {
                    viewDialog.hideProgressDialog();
                    Utils.alertRetrofitExceptionDialog(mContext, t);
                    Log.e("dfshjh", "mLstconedtls t" + t);
                }
            });
        } else {
            Utils.ShowAlertDialog(mContext, internetErrorTitle, internetError);
        }
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

    private void setClickLisstener() {
        recyclerViewHome.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerViewHome, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                try {
                    String menuName = mainMenuArrayList.get(position).getMenuName();
                    if (menuName.equalsIgnoreCase(DASHBOARD)) {
                        startActivity(new Intent(HomeActivity.this, DashboardActivity.class));
                        finish();
                    } else if (menuName.equalsIgnoreCase(LEADS_GIVEN)) {
                        startActivity(new Intent(HomeActivity.this, LeadsGivenActivity.class));
                        finish();
                    } else if (menuName.equalsIgnoreCase(LEADS_RECEIVED)) {
                        startActivity(new Intent(HomeActivity.this, PickLeadsActivity.class));
                        finish();
                    } else if (menuName.equalsIgnoreCase(QUICK_LEADS)) {
                        startActivity(new Intent(HomeActivity.this, QuickLeadActivity.class));
                        finish();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    @OnClick({R.id.logout_toolbar})
    void onButtonClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.logout_toolbar:
                    alertConfirmLogout();
                    break;
            }
        } catch (NullPointerException n) {
            n.printStackTrace();
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

    private void checkJson() {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray("[{\"SubmenuText\":\"Dashboard\"},{\"SubmenuText\":\"Leads Given\"},{\"SubmenuText\":\"Leads Received\"},{\"SubmenuText\":\"Quick leads\"}]");
            // jsonArray = new JSONArray("[{\"SubmenuText\":\"Leads Given\"},{\"SubmenuText\":\"Leads Received\"},{\\\"SubmenuText\\\":\\\"Quick leads\\\"}]");
            sharedPreference.setPref(SHARED_PREF_LOGIN_INFO, jsonArray.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setAdapter() {
        HomeAdapter homeAdapter = new HomeAdapter(getApplicationContext(), mainMenuArrayList);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerViewHome.setLayoutManager(layoutManager);
        recyclerViewHome.setAdapter(homeAdapter);
    }


    private void loadData() {
        try {
            JSONArray menuJsonArray = new JSONArray(sharedPreference.getPref(LOGININFORMATION));
            mainMenuArrayList = new ArrayList<>();

            for (int i = 0; i < menuJsonArray.length(); i++) {
                JSONObject subMenuJsonObject = menuJsonArray.getJSONObject(i);

                MainMenu mainMenu = new MainMenu();
                if (subMenuJsonObject.getString(SUB_MENU_TEXT).equalsIgnoreCase(QUICK_LEADS)) {
                    mainMenu.setMenuName(QUICK_LEADS);
                    mainMenu.setImageId(R.drawable.ic_quickleads);
                } else if (subMenuJsonObject.getString(SUB_MENU_TEXT).equalsIgnoreCase(LEADS_GIVEN)) {
                    mainMenu.setMenuName(LEADS_GIVEN);
                    mainMenu.setImageId(R.drawable.ic_leadsgiven);
                } else if (subMenuJsonObject.getString(SUB_MENU_TEXT).equalsIgnoreCase(LEADS_RECEIVED)) {
                    mainMenu.setMenuName(LEADS_RECEIVED);
                    mainMenu.setImageId(R.drawable.ic_leadsreceived);
                } else if (subMenuJsonObject.getString(SUB_MENU_TEXT).equalsIgnoreCase(DASHBOARD)) {
                    mainMenu.setMenuName(DASHBOARD);
                    mainMenu.setImageId(R.drawable.ic_dashboard);
                }

                mainMenuArrayList.add(mainMenu);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setToolBar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        imgToolbar.setBackgroundResource(R.drawable.leaduat);
        logout_toolbar.setVisibility(View.VISIBLE);
        notification_imageView.setBackgroundResource(R.drawable.notification);
        toolBarText.setText(title_appname);
    }

    @Override
    public void onBackPressed() {
        SweetAlertDialog dialog = new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE);
        dialog.setTitleText(confirmMsg)
                .setContentText(exitMsg)
                .setConfirmText(OkTxt)
                .setCancelText(cancelTxt)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        HomeActivity.super.onBackPressed();
                        finish();
                        moveTaskToBack(true);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            finishAffinity();
                        }
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

    private void UpdateVersion() {
        if (isNetworkAvailable(HomeActivity.this)) {
            try {
                mainMenuResponse = new MainMenu();
                mainMenuResponse.setAppName1(getResources().getString(R.string.appname));
                mainMenuResponse.setVersion(getVersionNumber(mContext));

                Call<ResponseBody> responseBodyCall = mAPICallInterface.getVersionUpdate(mainMenuResponse);
                responseBodyCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            if (response != null) {
                                if (response.code() == 200) {
                                    String responseValue = response.body().string();
                                    if (responseValue.length() > 0) {

                                        final JSONObject jsonObject = new JSONObject(responseValue);
                                        if (!(jsonObject.getString(SP_URL).equalsIgnoreCase("0"))) {
                                            showDialogOK(HomeActivity.this, informationTxt, appUpdateMsg, OkTxt, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.cancel();
                                                    try {
                                                        ApkFileDownload checkUpdate = new ApkFileDownload(HomeActivity.this, downloadUpdateTxt);
                                                        checkUpdate.execute(jsonObject.getString(SP_URL));
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }

                                                }
                                            });
                                        }
                                    }

                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            ShowAlertDialog(HomeActivity.this, internetErrorTitle, internetError);
        }
    }

    public void setSpinnerAdapter(Spinner userSpinner, ArrayList<Lstuserdtls> mystringList, Context mContext) {
        /*ArrayList<String> stringList = new ArrayList<>();
        stringList.add("");
        for (String s : mystringList) {
            stringList.add(s);
        }*/
        CustomSpinerAdapter adapter_state = new CustomSpinerAdapter(mContext, mystringList);
        userSpinner.setAdapter(adapter_state);

    }


}
