package com.tvscs.ilead.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tvscs.ilead.helper.SharedPreference;
import com.tvscs.ilead.model.MainMenu;
import com.tvscs.ilead.model.dashboard.DashboardModel;
import com.tvscs.ilead.model.dashboard.DashboardModels;
import com.tvscs.ilead.model.dashboard.Ref_Dashboard_NewResult;
import com.tvscs.ilead.retrofit.APICallInterface;
import com.tvscs.ilead.retrofit.ServiceGenerator;
import com.tvscs.ilead.utils.Constants;
import com.tvscs.ilead.utils.LoaderDialog;
import com.tvscs.ilead.utils.MyBottomMenu;
import com.tvscs.ilead.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;

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
import static com.tvscs.ilead.utils.Utils.showErrorDialog;

public class DashboardActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.img_toolbar)
    AppCompatImageView imgToolbar;
    @BindView(R.id.layout_toolBar)
    LinearLayout mlayout_toolBar;
    @BindView(R.id.toolBarText)
    TextView toolBarText;
    @BindView(R.id.notification_toolbar)
    LinearLayout notification_toolbar;

    @BindView(R.id.viewAddLinear)
    LinearLayout viewAddLinear;

    @BindView(R.id.notifcation_imageView)
    ImageView notification_imageView;
    LoaderDialog viewDialog;
    HashSet<String> hashSet = new HashSet<String>();
    private Context mContext;
    private DashboardModels dashboardModels;
    private ArrayList<DashboardModel> dashboardModelArrayList = new ArrayList<>();
    private ArrayList<MainMenu> mainMenuArrayList;
    private SharedPreference sharedPreference;
    private int CALL_PERMISSION_CODE = 23;

    @Override
    public int activityView() {
        return R.layout.activity_new_dashboard;
    }

    @Override
    public void activityCreated() {
        initViews();
        new MyBottomMenu(this, bottomMenuDashboardValue);
        callDashboardService();
        setToolBar();
    }

    private void setToolBar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Utils.setSvgImageviewDrawable(imgToolbar, mContext, R.drawable.ic_back_arrow);
        notification_imageView.setBackgroundResource(R.drawable.notification);
        toolBarText.setText(Constants.DASHBOARD);
    }

    private void initViews() {
        ButterKnife.bind(this);
        mContext = DashboardActivity.this;
        viewDialog = new LoaderDialog(this);
        sharedPreference = new SharedPreference(mContext);
    }

    @OnClick(R.id.notification_toolbar)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.notification_toolbar:
                try {
                    if (isNetworkAvailable(mContext)) {
                        Utils.NotificationUpdates(mContext, sharedPreference.getPref(USERID, ""));
                    } else {
                        ShowAlertDialog(mContext, internetErrorTitle, internetError);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    /**
     * Here Calling Network API
     */
    private void callDashboardService() {
        if (isNetworkAvailable(mContext)) {
            viewDialog.showProgressDialog();
            APICallInterface apiCallInterface;
            dashboardModels = new DashboardModels();
            apiCallInterface = ServiceGenerator.createService(APICallInterface.class);
            dashboardModels.setUser_id(sharedPreference.getPref(USERID, ""));
            Call<DashboardModels> call = apiCallInterface.getDashboardNew(dashboardModels);
            call.enqueue(new Callback<DashboardModels>() {
                @Override
                public void onResponse(Call<DashboardModels> call, Response<DashboardModels> response) {
                    viewDialog.hideProgressDialog();
                    try {
                        DashboardModels dashboardModels = response.body();
                        if (response.code() == (Integer.parseInt(SUCCESSCODE))) {
                            if (dashboardModels != null) {
                                groupingSection(dashboardModels);
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
                public void onFailure(Call<DashboardModels> call, Throwable t) {
                    viewDialog.hideProgressDialog();
                    Utils.alertRetrofitExceptionDialog(mContext, t);

                }
            });
        } else {
            showErrorDialog(mContext, internetErrorTitle, internetError);
        }
    }

    private void groupingSection(DashboardModels dashboardModels) {
        if (dashboardModels.getRef_Dashboard_NewResult() != null) {
            int length = dashboardModels.getRef_Dashboard_NewResult().size();
            for (int i = 0; i < length; i++) {
                hashSet.add(dashboardModels.getRef_Dashboard_NewResult().get(i).getProduct());
            }
            ArrayList<String> arrayList = new ArrayList<String>(hashSet);
            ArrayList<ArrayList<Ref_Dashboard_NewResult>> mfilterarray = new ArrayList<>();

            for (int j = 0; j < arrayList.size(); j++) {
                String str_obj = arrayList.get(j);
                if (str_obj != null) {
                    ArrayList<Ref_Dashboard_NewResult> taxsection_heading = new ArrayList<Ref_Dashboard_NewResult>();
                    for (int k = 0; k < length; k++) {
                        if (str_obj.equals(dashboardModels.getRef_Dashboard_NewResult().get(k).getProduct())) {
                            taxsection_heading.add(dashboardModels.getRef_Dashboard_NewResult().get(k));
                        }
                    }
                    mfilterarray.add(taxsection_heading);
                }
            }
            getvalueArrayOfArray(mfilterarray);
        }
    }

    private void getvalueArrayOfArray(ArrayList<ArrayList<Ref_Dashboard_NewResult>> mfilterarray) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        int length = mfilterarray.size();

        for (int i = 0; i < length; i++) {
            final View house_view = inflater.inflate(R.layout.dashboard_single_item, null);
            LinearLayout addChildView = house_view.findViewById(R.id.addChildView);
            ImageView product_image = house_view.findViewById(R.id.product_image);
            TextView product_name = house_view.findViewById(R.id.product_name);
            TextView currentMonth = house_view.findViewById(R.id.currentMonth);

            int innerlength = mfilterarray.get(i).size();
            for (int j = 0; j < innerlength; j++) {

                if (!mfilterarray.get(i).isEmpty() && mfilterarray.get(i).size() != 0) {
                    String mSTr = mfilterarray.get(i).get(j).getProductdesc();
                    // product_image.setImageResource(getDrawableId(this, mSTr.toLowerCase()));
                    setProductImage(mfilterarray.get(i).get(j).getProduct(), product_image);
                    // product_image.setImageResource(R.mipmap.bike);
                    product_name.setText(mSTr);
                    //  product_name.setText(mSTr.split("-")[1].trim());

                    currentMonth.setText(Utils.MONTH_FORMAT.format(Calendar.getInstance().getTimeInMillis()));

                    if (mfilterarray.get(i).get(j).getProductdesc().equalsIgnoreCase(mSTr)) {
                        LayoutInflater inflaters = LayoutInflater.from(mContext);
                        final View mChildView = inflaters.inflate(R.layout.item_new_dashboard, null);
                        TextView textViewName = mChildView.findViewById(R.id.textViewName);
                        TextView textViewZero = mChildView.findViewById(R.id.textViewZero);
                        TextView textViewCone = mChildView.findViewById(R.id.textViewCone);
                        TextView textViewCtwo = mChildView.findViewById(R.id.textViewCtwo);
                        TextView textViewCthree = mChildView.findViewById(R.id.textViewCthree);
                        TextView textViewCfour = mChildView.findViewById(R.id.textViewCfour);
                        final ImageView imgViewCall = mChildView.findViewById(R.id.imgViewCall);
                        imgViewCall.setTag(mfilterarray.get(i).get(j));

                        imgViewCall.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Ref_Dashboard_NewResult ref_dashboard_newResult = (Ref_Dashboard_NewResult) imgViewCall.getTag();
                                if (ref_dashboard_newResult.getMobile().isEmpty() || ref_dashboard_newResult.getMobile().equals("")) {
                                    Utils.ShowAlertDialog(mContext, informationTxt, invalidMobileNum);
                                } else {
                                    alertDialog_Call(ref_dashboard_newResult);
                                }

                            }
                        });

                        Utils.setValueInTextview(textViewName, mfilterarray.get(i).get(j).getUserName());
                        Utils.setValueInTextview(textViewZero, mfilterarray.get(i).get(j).getCzero());
                        Utils.setValueInTextview(textViewCone, mfilterarray.get(i).get(j).getCone());
                        Utils.setValueInTextview(textViewCtwo, mfilterarray.get(i).get(j).getCtwo());
                        Utils.setValueInTextview(textViewCthree, mfilterarray.get(i).get(j).getCthree());
                        Utils.setValueInTextview(textViewCfour, mfilterarray.get(i).get(j).getCfour());
                        addChildView.addView(mChildView);
                    }

                }

            }

            viewAddLinear.addView(house_view);
        }
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent in = new Intent(mContext, HomeActivity.class);
        startActivity(in);
        finish();
    }

    public int getDrawableId(Context context, String name) {
        Resources resources = context.getResources();
        int resourceId = 0;

        resourceId = resources.getIdentifier(name, "drawable",
                context.getPackageName());

        if (resourceId == 0)
            resourceId = R.drawable.trk;

        return resourceId;
    }

    public void alertDialog_Call(final Ref_Dashboard_NewResult ref_dashboard_newResult) {
        new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(callDialogTitle)
                .setContentText(ref_dashboard_newResult.getUserName())
                .setCancelText(cancelTxt)
                .setConfirmText(callDialogconfirmPick)
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        if (isCallAllowed()) {
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + ref_dashboard_newResult.getMobile()));
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

    private void setProductImage(String productName, ImageView imageView) {
        switch (productName) {
            case AL:
                Utils.setSvgImageviewDrawable(imageView, mContext, R.drawable.ic_trans_auto);
                break;
            case AP:
                Utils.setSvgImageviewDrawable(imageView, mContext, R.drawable.ic_trans_usedtractor);
                break;
            case CA:
                Utils.setSvgImageviewDrawable(imageView, mContext, R.drawable.ic_trans_car);
                break;
            case CD:
                Utils.setSvgImageviewDrawable(imageView, mContext, R.drawable.ic_trans_commercial);
                break;
            case DC:
                Utils.setSvgImageviewDrawable(imageView, mContext, R.drawable.ic_trans_car);
                break;
            case LN:
                Utils.setSvgImageviewDrawable(imageView, mContext, R.drawable.ic_trans_usedtractor);
                break;
            case TR:
                Utils.setSvgImageviewDrawable(imageView, mContext, R.drawable.ic_trans_tractor);
                break;
            case TW:
                Utils.setSvgImageviewDrawable(imageView, mContext, R.drawable.ic_trans_bike);
                break;
            case UT:
                Utils.setSvgImageviewDrawable(imageView, mContext, R.drawable.ic_usedbike);
                break;
            case UV:
                Utils.setSvgImageviewDrawable(imageView, mContext, R.drawable.ic_trans_commercial);
                break;
            default:
                Utils.setSvgImageviewDrawable(imageView, mContext, R.drawable.ic_trans_auto);
                break;
        }

    }


}
