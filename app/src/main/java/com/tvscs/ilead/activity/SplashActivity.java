package com.tvscs.ilead.activity;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tvscs.ilead.helper.SharedPreference;
import com.tvscs.ilead.retrofit.APICallInterface;
import com.tvscs.ilead.retrofit.APIClient;
import com.tvscs.ilead.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import ilead.tvscs.com.ilead.R;

import static com.tvscs.ilead.utils.Utils.getVersionNumber;

public class SplashActivity extends Activity {
    Context mContext;
    SharedPreference mPrefs;
    APICallInterface mAPIService;

    @BindView(R.id.splashConsLayout)
    LinearLayout splashConsLayout;
    @BindView(R.id.tv_versionCode)
    TextView tv_versionCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        mContext = SplashActivity.this;
        mPrefs = new SharedPreference(mContext);
        mAPIService = APIClient.getAPIService();
        tv_versionCode.setText(Constants.version + " " + getVersionNumber(mContext));
        LaunchApp();
    }

    public void LaunchApp() {
        Thread background = new Thread() {
            public void run() {
                try {
                    sleep(2 * 1000);
                    mPrefs.checkLogin();
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        background.start();
    }
}
