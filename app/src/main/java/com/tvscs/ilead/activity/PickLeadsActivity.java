package com.tvscs.ilead.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tvscs.ilead.adapter.LeadsReceivedViewPagerAdapter;
import com.tvscs.ilead.fragment.PickLeadFragment;
import com.tvscs.ilead.helper.SharedPreference;
import com.tvscs.ilead.model.MainMenu;
import com.tvscs.ilead.utils.Constants;
import com.tvscs.ilead.utils.MyBottomMenu;
import com.tvscs.ilead.utils.Utils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import ilead.tvscs.com.ilead.R;

import static com.tvscs.ilead.utils.Utils.ShowAlertDialog;
import static com.tvscs.ilead.utils.Utils.isNetworkAvailable;

public class PickLeadsActivity extends BaseActivity implements PickLeadFragment.ViewPageListner {


    private static final int VIEW_PAGER_0 = 0;
    private static final int VIEW_PAGER_1 = 1;
    private static final int VIEW_TYPE_1 = 1;
    private static final int VIEW_TYPE_2 = 2;
    public ViewPager viewPagerLeadsReceived;
    @BindView(R.id.img_toolbar)
    ImageView imgToolbar;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.layout_toolBar)
    LinearLayout mlayout_toolBar;
    @BindView(R.id.toolBarText)
    TextView toolBarText;
    @BindView(R.id.notification_toolbar)
    LinearLayout notification_toolbar;
    @BindView(R.id.notifcation_imageView)
    ImageView notification_imageView;
    @BindView(R.id.viewPickLead)
    View viewPickLead;
    @BindView(R.id.viewLeadsReceived)
    View viewLeadsReceived;
    private ArrayList<MainMenu> mainMenuArrayList;
    private SharedPreference sharedPreference;
    private Context mContext;


    @Override
    public int activityView() {
        return R.layout.activity_pick_leads;
    }

    @Override
    public void activityCreated() {
        ButterKnife.bind(this);
        initViews();
        setAdapter();
        setViewColor(VIEW_TYPE_1);
        setCurrentViewPager(VIEW_PAGER_0);
        setOnPageChangeListener();
        setToolBar();
        new MyBottomMenu(this, bottomMenuleadsReceivedValue);

    }

    private void setAdapter() {
        LeadsReceivedViewPagerAdapter leadsReceivedViewPagerAdapter = new LeadsReceivedViewPagerAdapter(getSupportFragmentManager());
        viewPagerLeadsReceived.setAdapter(leadsReceivedViewPagerAdapter);
    }

    private void setOnPageChangeListener() {
        viewPagerLeadsReceived.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                setViewColor(position + 1);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }


    private void setToolBar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setElevation(0);
        Utils.setSvgImageviewDrawable(imgToolbar, mContext, R.drawable.ic_back_arrow);
        Utils.setSvgImageviewDrawable(notification_imageView, mContext, R.drawable.notification);
        //notification_imageView.setBackgroundResource(R.drawable.notification);
    }

    private void initViews() {
        viewPagerLeadsReceived = findViewById(R.id.viewPagerLeadsReceived);
        mContext = PickLeadsActivity.this;
        sharedPreference = new SharedPreference(mContext);


    }

    @Nullable
    @OnClick({R.id.linearPickLead, R.id.linearLeadsReceived, R.id.layout_toolBar, R.id.bottom_menu_quick, R.id.bottom_menu_leadsgiven,
            R.id.bottom_menu_leadsreceived, R.id.bottom_menu_dashboard, R.id.bottom_menu_logout, R.id.notification_toolbar})
    public void layoutClicked(View view) {
        switch (view.getId()) {
            case R.id.linearPickLead:
                toolBarText.setText(Constants.PICK_LEADS);
                setViewColor(VIEW_TYPE_1);
                setCurrentViewPager(VIEW_PAGER_0);
                break;
            case R.id.linearLeadsReceived:
                toolBarText.setText(Constants.LEADS_RECEIVED);
                setViewColor(VIEW_TYPE_2);
                setCurrentViewPager(VIEW_PAGER_1);
                break;

            case R.id.layout_toolBar:
                startActivity(new Intent(mContext, HomeActivity.class));
                finish();
                break;
            case R.id.bottom_menu_quick:
                startActivity(new Intent(mContext, QuickLeadActivity.class));
                finish();
                break;
            case R.id.bottom_menu_leadsgiven:
                startActivity(new Intent(mContext, LeadsGivenActivity.class));
                finish();
                break;
            case R.id.bottom_menu_leadsreceived:
                startActivity(new Intent(mContext, PickLeadsActivity.class));
                finish();
                break;
            case R.id.bottom_menu_dashboard:
                startActivity(new Intent(mContext, DashboardActivity.class));
                finish();
                break;
            case R.id.bottom_menu_logout:
                alertConfirmLogout();
                break;
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
        }
    }

    private void setCurrentViewPager(int index) {
        toolBarText.setText(Constants.PICK_LEADS);
        viewPagerLeadsReceived.setCurrentItem(index);
    }

    private void setViewColor(int type) {
        switch (type) {
            case VIEW_TYPE_1:
                toolBarText.setText(Constants.PICK_LEADS);
                viewPickLead.setBackgroundColor(getResources().getColor(R.color.floatingOrange));
                viewLeadsReceived.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                break;
            case VIEW_TYPE_2:
                toolBarText.setText(Constants.LEADS_RECEIVED);
                viewPickLead.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                viewLeadsReceived.setBackgroundColor(getResources().getColor(R.color.floatingOrange));
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

    @Override
    public void onRefresh() {
        setAdapter();
        viewPagerLeadsReceived.setCurrentItem(1);
    }
}
