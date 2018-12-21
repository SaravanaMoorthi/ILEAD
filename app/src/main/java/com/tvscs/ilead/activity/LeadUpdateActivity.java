package com.tvscs.ilead.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tvscs.ilead.adapter.ViewPagerAdapter;
import com.tvscs.ilead.helper.SharedPreference;
import com.tvscs.ilead.utils.Utils;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ilead.tvscs.com.ilead.R;

import static com.tvscs.ilead.utils.Utils.ShowAlertDialog;
import static com.tvscs.ilead.utils.Utils.isNetworkAvailable;

public class LeadUpdateActivity extends BaseActivity {

    private static final int VIEW_PAGER_0 = 0;
    private static final int VIEW_PAGER_1 = 1;
    private static final int VIEW_TYPE_1 = 1;
    private static final int VIEW_TYPE_2 = 2;
    public ViewPager viewPagerLeadsUpdate;
    @Nullable
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.toolBarText)
    TextView toolBarText;
    @BindView(R.id.viewCustomer_detail)
    View viewCustomer_detail;
    @BindView(R.id.viewFollowup)
    View viewFollowup;
    @BindView(R.id.img_toolbar)
    AppCompatImageView imgToolbar;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.layout_toolBar)
    LinearLayout mlayout_toolBar;
    @BindView(R.id.notification_toolbar)
    LinearLayout notification_toolbar;
    @BindView(R.id.linearCustomer_detail)
    LinearLayout linearCustomerDetail;
    @BindView(R.id.linearFollowup)
    LinearLayout linearFollowup;
    @BindString(R.string.title_leadreceived)
    String title_leadreceived;
    @BindView(R.id.notifcation_imageView)
    ImageView notification_imageView;
    ViewPagerAdapter adapter;
    Context mContext;
    SharedPreference sharedPreference;
    String leadId, classname;

    @Override
    public int activityView() {
        return R.layout.activity_received_details;
    }

    @Override
    public void activityCreated() {
        ButterKnife.bind(this);
        mContext = LeadUpdateActivity.this;
        sharedPreference = new SharedPreference(mContext);
        initView();
        setAdapter();
        setViewColor(VIEW_TYPE_1);
        setCurrentViewPager(VIEW_PAGER_0);
        setOnPageChangeListener();
        setToolBar();
    }

    private void initView() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            leadId = extras.getString(respleadId_);
            classname = extras.getString(className);
        }
    }

    @Nullable
    @OnClick({R.id.linearCustomer_detail, R.id.linearFollowup, R.id.layout_toolBar, R.id.notification_toolbar})
    void onButtonClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.linearCustomer_detail:
                    setViewColor(VIEW_TYPE_1);
                    setCurrentViewPager(VIEW_PAGER_0);
                    break;
                case R.id.linearFollowup:
                    setViewColor(VIEW_TYPE_2);
                    setCurrentViewPager(VIEW_PAGER_1);
                    break;

                case R.id.layout_toolBar:
                    startActivity(new Intent(mContext, PickLeadsActivity.class));
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
                    break;
            }
        } catch (NullPointerException n) {
            n.printStackTrace();
        }
    }

    private void setToolBar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Utils.setSvgImageviewDrawable(imgToolbar, mContext, R.drawable.ic_back_arrow);
        notification_imageView.setBackgroundResource(R.drawable.notification);
        toolBarText.setText(title_leadreceived);
    }

    private void setViewColor(int type) {
        switch (type) {
            case VIEW_TYPE_1:
                viewCustomer_detail.setBackgroundColor(getResources().getColor(R.color.floatingOrange));
                viewFollowup.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                break;
            case VIEW_TYPE_2:
                viewCustomer_detail.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                viewFollowup.setBackgroundColor(getResources().getColor(R.color.floatingOrange));
                break;
        }
    }

    public void setCurrentViewPager(int index) {
        viewPager.setCurrentItem(index);
    }


    private void setAdapter() {
        ViewPagerAdapter leadupdateViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), leadId);
        viewPager.setAdapter(leadupdateViewPagerAdapter);
    }

    private void setOnPageChangeListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (classname.equalsIgnoreCase(notification)) {
            Intent in = new Intent(mContext, HomeActivity.class);
            startActivity(in);
            finish();
        } else {
            Intent in = new Intent(mContext, PickLeadsActivity.class);
            startActivity(in);
            finish();
        }

    }
}
