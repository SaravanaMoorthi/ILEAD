package com.tvscs.ilead.activity;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tvscs.ilead.helper.SharedPreference;
import com.tvscs.ilead.model.LoginResponse;
import com.tvscs.ilead.retrofit.APICallInterface;
import com.tvscs.ilead.retrofit.APIClient;
import com.tvscs.ilead.utils.LoaderDialog;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ilead.tvscs.com.ilead.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tvscs.ilead.utils.Utils.isEmailValid;
import static com.tvscs.ilead.utils.Utils.isNetworkAvailable;
import static com.tvscs.ilead.utils.Utils.showErrorDialog;

public class ForgotPasswordActivity extends BaseActivity {
    LoaderDialog viewDialog;
    APICallInterface mAPIService;
    SharedPreference preference;
    Context mContext;
    LoginResponse loginResponse;

    String emailId;

    @BindView(R.id.emailTextVew)
    EditText emailTextVew;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;

    @Override
    public int activityView() {
        return R.layout.activity_forgotpassword;
    }

    @Override
    public void activityCreated() {
        ButterKnife.bind(this);
        mAPIService = APIClient.getAPIService();
        mContext = ForgotPasswordActivity.this;
        viewDialog = new LoaderDialog(this);
        preference = new SharedPreference(mContext);
    }

    @Nullable
    @OnClick({R.id.btnSubmit})
    void onButtonClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.btnSubmit:
                    emailId = emailTextVew.getText().toString().trim();
                    if (!(emailId.isEmpty()) && isEmailValid(emailId)) {
                        GetForgotPassword(emailId);
                    } else {
                        showErrorDialog(ForgotPasswordActivity.this, informationTxt, mailiIdValidErrorMsg);
                    }
                    break;
            }
        } catch (NullPointerException n) {
            n.printStackTrace();
        }
    }

    private void GetForgotPassword(String emailid) {
        if (isNetworkAvailable(ForgotPasswordActivity.this)) {
            try {
                loginResponse = new LoginResponse();
                viewDialog.showProgressDialog();
                loginResponse.setEmail(emailid);
                Call<ResponseBody> responseBodyCall = mAPIService.getForgotPassword(loginResponse);
                responseBodyCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        viewDialog.hideProgressDialog();
                        if (response != null) {
                            if (response.code() == 200) {
                                try {
                                    String responseValue = response.body().string();
                                    if (responseValue.length() > 0) {
                                        JSONObject jsonObject = new JSONObject(responseValue);
                                        if (jsonObject.length() > 0) {
                                            if (!(jsonObject.getString(respUserid_).equalsIgnoreCase("")) || !(jsonObject.getString(respUsertype_).equalsIgnoreCase(""))) {
                                                showErrorDialog(ForgotPasswordActivity.this, message, forgotPwdMsg);
                                            } else {
                                                showErrorDialog(ForgotPasswordActivity.this, message, errorforgotPwdMsg);
                                            }
                                        }
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                showErrorDialog(mContext, informationTxt, serverErrorMsg);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        viewDialog.hideProgressDialog();
                    }
                });
            } catch (Exception e) {
                viewDialog.hideProgressDialog();
                e.printStackTrace();
            }

        } else {
            showErrorDialog(ForgotPasswordActivity.this, internetErrorTitle, internetError);
        }
    }
}
