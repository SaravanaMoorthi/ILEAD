package com.tvscs.ilead.retrofit;

import com.tvscs.ilead.utils.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    public static Retrofit getClient(String BaseURL) {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .readTimeout(100000, TimeUnit.MILLISECONDS)
                .connectTimeout(100000, TimeUnit.MILLISECONDS)
                .build();

        Retrofit retrofit = null;
        try {
            if (retrofit == null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(BaseURL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(client)
                        .build();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retrofit;
    }

    public static APICallInterface getAPIService() {

        return APIClient.getClient(Constants.URL).create(APICallInterface.class);
    }

    public static APICallInterface getApiInterface() {
        return APIClient.getClient(Constants.URL).create(APICallInterface.class);
    }
}
