package com.tvscs.ilead.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tvscs.ilead.retrofit.APICallInterface;
import com.tvscs.ilead.utils.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public abstract class BaseActivity extends AppCompatActivity implements Constants {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activityView());
        activityCreated();
    }

    public APICallInterface getInterfaceService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final APICallInterface mInterfaceService = retrofit.create(APICallInterface.class);
        return mInterfaceService;
    }

    public abstract int activityView();

    public abstract void activityCreated();


}
