package com.tvscs.ilead.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tvscs.ilead.helper.SharedPreference;
import com.tvscs.ilead.model.LoginResponse;
import com.tvscs.ilead.retrofit.APICallInterface;
import com.tvscs.ilead.retrofit.APIClient;
import com.tvscs.ilead.utils.LoaderDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ilead.tvscs.com.ilead.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.os.Build.VERSION_CODES.M;
import static com.tvscs.ilead.utils.Utils.getDeviceIMEI;
import static com.tvscs.ilead.utils.Utils.isNetworkAvailable;
import static com.tvscs.ilead.utils.Utils.isPasswordValid;
import static com.tvscs.ilead.utils.Utils.showDialogOK;
import static com.tvscs.ilead.utils.Utils.showErrorDialog;

public class LoginActivity extends BaseActivity {

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    @BindView(R.id.email)
    EditText mEmailView;
    @BindView(R.id.password)
    EditText mPasswordView;
    @BindView(R.id.forgotPass_textView)
    TextView forgotPass_textView;
    @BindView(R.id.login_form)
    View mLoginFormView;
    @BindView(R.id.btnSingIn)
    Button mEmailSignInButton;
    @BindDrawable(R.mipmap.user)
    Drawable usericon;
    @BindDrawable(R.mipmap.lock)
    Drawable lockicon;
    @BindString(R.string.error_field_required)
    String errorField;
    @BindString(R.string.error_invalid_email)
    String emailError;
    @BindString(R.string.error_invalid_password)
    String passwordError;
    LoaderDialog viewDialog;
    APICallInterface mAPIService;
    LoginResponse loginResponse;
    SharedPreference preference;
    Context mContext;
    String email, password;
    Boolean isLoggedIn;
    private boolean sentToSettings = false;

    @Override
    public int activityView() {
        return R.layout.activity_login;
    }

    @Override
    public void activityCreated() {
        ButterKnife.bind(this);
        mAPIService = APIClient.getAPIService();
        mContext = LoginActivity.this;
        viewDialog = new LoaderDialog(this);
        preference = new SharedPreference(mContext);

        usericon.setBounds(0, 0, usericon.getIntrinsicWidth(), usericon.getIntrinsicHeight());
        lockicon.setBounds(0, 0, lockicon.getIntrinsicWidth(), lockicon.getIntrinsicHeight());

        if (usericon != null) {
            usericon.setBounds(0, 0,
                    usericon.getIntrinsicWidth(),
                    usericon.getIntrinsicHeight());
        } else if (lockicon != null) {
            lockicon.setBounds(0, 0,
                    lockicon.getIntrinsicWidth(),
                    lockicon.getIntrinsicHeight());
        }
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    //attemptLogin();
                    InputMethodManager imm = (InputMethodManager) textView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
    }

    @Nullable
    @OnClick({R.id.btnSingIn, R.id.forgotPass_textView})
    void onButtonClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.btnSingIn:
                    attemptLogin();
                    break;
                case R.id.forgotPass_textView:
                    startActivity(new Intent(mContext, ForgotPasswordActivity.class));
                    break;
            }
        } catch (NullPointerException n) {
            n.printStackTrace();
        }
    }

    private void attemptLogin() {
        mEmailView.setError(null, usericon);
        mPasswordView.setError(null, lockicon);

        email = mEmailView.getText().toString();
        password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(errorField, lockicon);
            focusView = mPasswordView;
            cancel = true;
        } else if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(passwordError, lockicon);
            focusView = mPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(errorField, usericon);
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            if (Build.VERSION.SDK_INT >= M) {
                if (checkAndRequestPermissions()) {
                    login(email, password);
                }
            } else {
                login(email, password);
            }
        }
    }

    private boolean checkAndRequestPermissions() {
        int permissionReadphone = ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE);
        int StoragePermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int SMSPermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.SEND_SMS);
        int CallPermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (StoragePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (permissionReadphone != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (SMSPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
        }
        if (CallPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CALL_PHONE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();
                perms.put(Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.SEND_SMS, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CALL_PHONE, PackageManager.PERMISSION_GRANTED);

                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);

                    if (perms.get(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED &&
                            perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                            perms.get(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED &&
                            perms.get(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        login(email, password);
                    } else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
                            showDialogOK(mContext, message, phonePermissionMsg, OkTxt,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;

                                            }
                                        }
                                    });
                        } else {
                            showDialogOK(mContext, message, settingsPermissionsTxt, OkTxt,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    sentToSettings = true;
                                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                                                    intent.setData(uri);
                                                    startActivityForResult(intent, REQUEST_ID_MULTIPLE_PERMISSIONS);
                                                    break;

                                            }
                                        }
                                    });
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                login(email, password);
            }
        }
    }

    private void login(final String userid, final String password) {
        if (isNetworkAvailable(mContext)) {
            try {
                String imeiNumber = getDeviceIMEI(mContext);
                preference.setPref(IMEINUMBER, imeiNumber);
                viewDialog.showProgressDialog();
                loginResponse = new LoginResponse();
                loginResponse.setUserid(userid);
                loginResponse.setPassword(password);
                loginResponse.setIpaddress(preference.getPref(IMEINUMBER));
                loginResponse.setOutErrorCode("");
                loginResponse.setOutErrorMsg("");
                Call<ResponseBody> responseBodyCall = mAPIService.getLogin(loginResponse);
                responseBodyCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response != null) {
                            if (response.code() == 200) {
                                try {
                                    viewDialog.hideProgressDialog();
                                    String responseValue = response.body().string();
                                    JSONObject jsonObject = new JSONObject(responseValue);
                                    try {
                                        if (jsonObject.length() > 0) {
                                            if (!(jsonObject.has(respStatus))) {
                                                if (jsonObject.getString(errorCode).equalsIgnoreCase(LS) || jsonObject.getString(errorCode).equalsIgnoreCase(PS)) {
                                                    if (jsonObject.getString(errorCode).equalsIgnoreCase(PS)) {
                                                        showDialogOK(mContext, informationTxt, jsonObject.getString(errorMsg), OkTxt, new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                switch (i) {
                                                                    case DialogInterface.BUTTON_POSITIVE:
                                                                        viewDialog.hideProgressDialog();
                                                                        LoginAccess(userid, password);
                                                                }
                                                            }
                                                        });
                                                    } else {
                                                        viewDialog.hideProgressDialog();
                                                        LoginAccess(userid, password);
                                                    }
                                                } else {
                                                    viewDialog.hideProgressDialog();
                                                    showErrorDialog(mContext, informationTxt, jsonObject.getString(errorMsg));
                                                }
                                            }

                                        }
                                    } catch (Exception e) {
                                        viewDialog.hideProgressDialog();
                                        e.printStackTrace();
                                    }
                                } catch (Exception e) {
                                    viewDialog.hideProgressDialog();
                                    e.printStackTrace();
                                }
                            } else {
                                showErrorDialog(mContext, informationTxt, serverErrorMsg);
                                viewDialog.hideProgressDialog();
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
                viewDialog.hideProgressDialog();
            }

        } else {
            showErrorDialog(mContext, internetErrorTitle, internetError);
            viewDialog.hideProgressDialog();
        }
    }

    private void LoginAccess(final String userid, final String password) {
        try {
            if (isNetworkAvailable(mContext)) {
                try {
                    viewDialog.showProgressDialog();
                    loginResponse = new LoginResponse();
                    loginResponse.setUser_id(userid);
                    Call<ResponseBody> responseBodyCall = mAPIService.getLoginMenuAccess(loginResponse);
                    responseBodyCall.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response != null) {
                                if (response.code() == 200) {
                                    viewDialog.hideProgressDialog();
                                    try {
                                        String responseValue = response.body().string();
                                        if (responseValue.length() > 0) {
                                            JSONArray jsonArray = new JSONArray(responseValue);
                                            if (jsonArray.length() > 0) {
                                                isLoggedIn = true;
                                                preference.createLoginSession(userid, password);
                                                preference.setPref(LOGININFORMATION, jsonArray.toString());
                                                Intent intent = new Intent(mContext, HomeActivity.class);
                                                intent.putExtra(LOGININFORMATION, jsonArray.toString());
                                                startActivity(intent);
                                                finish();
                                            }
                                        }

                                    } catch (Exception e) {
                                        viewDialog.hideProgressDialog();
                                        e.printStackTrace();
                                    }
                                } else {
                                    viewDialog.hideProgressDialog();
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
                viewDialog.hideProgressDialog();
                showErrorDialog(mContext, internetErrorTitle, internetError);
            }
        } catch (Exception e) {
            viewDialog.hideProgressDialog();
            e.printStackTrace();
        }
    }
}

