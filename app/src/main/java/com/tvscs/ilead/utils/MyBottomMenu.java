package com.tvscs.ilead.utils;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tvscs.ilead.activity.DashboardActivity;
import com.tvscs.ilead.activity.LeadsGivenActivity;
import com.tvscs.ilead.activity.PickLeadsActivity;
import com.tvscs.ilead.activity.QuickLeadActivity;
import com.tvscs.ilead.helper.SharedPreference;
import com.tvscs.ilead.model.MainMenu;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ilead.tvscs.com.ilead.R;

public class MyBottomMenu implements Constants, View.OnClickListener {

   private AppCompatActivity appCompatActivity;
   private String selectedMenuValue;

    private LinearLayout bottom_menu_quick;
    private LinearLayout bottom_menu_leadsgiven;
    private LinearLayout bottom_menu_leadsreceived;
    private LinearLayout bottom_menu_dashboard;
    private LinearLayout bottom_menu_logout;

    private ImageView img_bottom_menu_quick;
    private ImageView img_bottom_menu_leadsgiven;
    private ImageView img_bottom_menu_leadsreceived;
    private ImageView img_bottom_menu_dashboard;
    private ImageView img_bottom_menu_logout;

    private ArrayList<MainMenu> mainMenuArrayList;
    private SharedPreference sharedPreference;

    public MyBottomMenu(AppCompatActivity appCompatActivity, String bottomMenuValue) {
        this.appCompatActivity = appCompatActivity;
        this.selectedMenuValue = bottomMenuValue;
        initView();
        initData();
        loadBottomMenu();
        setClickListener();
    }

    private void setClickListener() {
        bottom_menu_quick.setOnClickListener(this);
        bottom_menu_leadsgiven.setOnClickListener(this);
        bottom_menu_leadsreceived.setOnClickListener(this);
        bottom_menu_dashboard.setOnClickListener(this);
        bottom_menu_logout.setOnClickListener(this);
    }

    private void initView() {
        bottom_menu_quick = appCompatActivity.findViewById(R.id.bottom_menu_quick);
        bottom_menu_leadsgiven = appCompatActivity.findViewById(R.id.bottom_menu_leadsgiven);
        bottom_menu_leadsreceived = appCompatActivity.findViewById(R.id.bottom_menu_leadsreceived);
        bottom_menu_dashboard = appCompatActivity.findViewById(R.id.bottom_menu_dashboard);
        bottom_menu_logout = appCompatActivity.findViewById(R.id.bottom_menu_logout);
        img_bottom_menu_quick = appCompatActivity.findViewById(R.id.img_bottom_menu_quick);
        img_bottom_menu_leadsgiven = appCompatActivity.findViewById(R.id.img_bottom_menu_leadsgiven);
        img_bottom_menu_leadsreceived = appCompatActivity.findViewById(R.id.img_bottom_menu_leadsreceived);
        img_bottom_menu_dashboard = appCompatActivity.findViewById(R.id.img_bottom_menu_dashboard);
        img_bottom_menu_logout = appCompatActivity.findViewById(R.id.img_bottom_menu_logout);
    }

    private void initData() {
        sharedPreference = new SharedPreference(appCompatActivity);
    }

    private void loadBottomMenu() {
        try {
            JSONArray menuJsonArray = new JSONArray(sharedPreference.getPref(LOGININFORMATION));
            mainMenuArrayList = new ArrayList<>();

            for (int i = 0; i < menuJsonArray.length(); i++) {
                JSONObject subMenuJsonObject = menuJsonArray.getJSONObject(i);

                MainMenu mainMenu = new MainMenu();
                if (subMenuJsonObject.getString(SUB_MENU_TEXT).equalsIgnoreCase(QUICK_LEADS)) {
                    bottom_menu_quick.setVisibility(View.VISIBLE);
                    setSelectedImage(Integer.valueOf(selectedMenuValue));
                } else if (subMenuJsonObject.getString(SUB_MENU_TEXT).equalsIgnoreCase(LEADS_GIVEN)) {
                    bottom_menu_leadsgiven.setVisibility(View.VISIBLE);
                    setSelectedImage(Integer.valueOf(selectedMenuValue));
                } else if (subMenuJsonObject.getString(SUB_MENU_TEXT).equalsIgnoreCase(LEADS_RECEIVED)) {
                    bottom_menu_leadsreceived.setVisibility(View.VISIBLE);
                    setSelectedImage(Integer.valueOf(selectedMenuValue));
                } else if (subMenuJsonObject.getString(SUB_MENU_TEXT).equalsIgnoreCase(DASHBOARD)) {
                    bottom_menu_dashboard.setVisibility(View.VISIBLE);
                    setSelectedImage(Integer.valueOf(selectedMenuValue));
                }
                bottom_menu_logout.setVisibility(View.VISIBLE);
                setSelectedImage(Integer.valueOf(selectedMenuValue));
                Utils.setSvgImageviewDrawable(img_bottom_menu_logout,appCompatActivity,R.drawable.ic_logout);
                mainMenuArrayList.add(mainMenu);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bottom_menu_quick:
                setSelectedImage(1);
                appCompatActivity.startActivity(new Intent(appCompatActivity, QuickLeadActivity.class));
                appCompatActivity.finish();
                break;
            case R.id.bottom_menu_leadsgiven:
                setSelectedImage(2);
                appCompatActivity.startActivity(new Intent(appCompatActivity, LeadsGivenActivity.class));
                appCompatActivity.finish();
                break;
            case R.id.bottom_menu_leadsreceived:
                setSelectedImage(3);
                appCompatActivity.startActivity(new Intent(appCompatActivity, PickLeadsActivity.class));
                appCompatActivity.finish();
                break;
            case R.id.bottom_menu_dashboard:
                setSelectedImage(4);
                appCompatActivity.startActivity(new Intent(appCompatActivity, DashboardActivity.class));
                appCompatActivity.finish();
                break;
            case R.id.bottom_menu_logout:
                  alertConfirmLogout();
                break;
        }
    }

    private void setSelectedImage(int i) {
        switch (i) {
            case 0:
                Utils.setSvgImageviewDrawable(img_bottom_menu_quick,appCompatActivity,R.drawable.ic_quickleads);
                Utils.setSvgImageviewDrawable(img_bottom_menu_leadsgiven,appCompatActivity,R.drawable.ic_leadsgiven);
                Utils.setSvgImageviewDrawable(img_bottom_menu_leadsreceived,appCompatActivity,R.drawable.ic_leadsreceived);
                Utils.setSvgImageviewDrawable(img_bottom_menu_dashboard,appCompatActivity,R.drawable.ic_dashboard);
                break;
            case 1:
                Utils.setSvgImageviewDrawable(img_bottom_menu_quick,appCompatActivity,R.drawable.ic_quickleads);
                img_bottom_menu_quick.setColorFilter(ContextCompat.getColor(appCompatActivity, R.color.colorTextOrange), android.graphics.PorterDuff.Mode.SRC_IN);
                Utils.setSvgImageviewDrawable(img_bottom_menu_leadsgiven,appCompatActivity,R.drawable.ic_leadsgiven);
                Utils.setSvgImageviewDrawable(img_bottom_menu_leadsreceived,appCompatActivity,R.drawable.ic_leadsreceived);
                Utils.setSvgImageviewDrawable(img_bottom_menu_dashboard,appCompatActivity,R.drawable.ic_dashboard);
                break;
            case 2:
                Utils.setSvgImageviewDrawable(img_bottom_menu_quick,appCompatActivity,R.drawable.ic_quickleads);
                Utils.setSvgImageviewDrawable(img_bottom_menu_leadsgiven,appCompatActivity,R.drawable.ic_leadsgiven);
                img_bottom_menu_leadsgiven.setColorFilter(ContextCompat.getColor(appCompatActivity, R.color.colorTextOrange), android.graphics.PorterDuff.Mode.SRC_IN);
                Utils.setSvgImageviewDrawable(img_bottom_menu_leadsreceived,appCompatActivity,R.drawable.ic_leadsreceived);
                Utils.setSvgImageviewDrawable(img_bottom_menu_dashboard,appCompatActivity,R.drawable.ic_dashboard);
                break;
            case 3:
                Utils.setSvgImageviewDrawable(img_bottom_menu_quick,appCompatActivity,R.drawable.ic_quickleads);
                Utils.setSvgImageviewDrawable(img_bottom_menu_leadsgiven,appCompatActivity,R.drawable.ic_leadsgiven);
                Utils.setSvgImageviewDrawable(img_bottom_menu_leadsreceived,appCompatActivity,R.drawable.ic_leadsreceived);
                img_bottom_menu_leadsreceived.setColorFilter(ContextCompat.getColor(appCompatActivity, R.color.colorTextOrange), android.graphics.PorterDuff.Mode.SRC_IN);
                Utils.setSvgImageviewDrawable(img_bottom_menu_dashboard,appCompatActivity,R.drawable.ic_dashboard);
                break;
            case 4:
                Utils.setSvgImageviewDrawable(img_bottom_menu_quick,appCompatActivity,R.drawable.ic_quickleads);
                Utils.setSvgImageviewDrawable(img_bottom_menu_leadsgiven,appCompatActivity,R.drawable.ic_leadsgiven);
                Utils.setSvgImageviewDrawable(img_bottom_menu_leadsreceived,appCompatActivity,R.drawable.ic_leadsreceived);
                Utils.setSvgImageviewDrawable(img_bottom_menu_dashboard,appCompatActivity,R.drawable.ic_dashboard);
                img_bottom_menu_dashboard.setColorFilter(ContextCompat.getColor(appCompatActivity, R.color.colorTextOrange), android.graphics.PorterDuff.Mode.SRC_IN);
                break;
        }
        }

    private void alertConfirmLogout() {
        SweetAlertDialog dialog = new SweetAlertDialog(appCompatActivity, SweetAlertDialog.WARNING_TYPE);
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
}
