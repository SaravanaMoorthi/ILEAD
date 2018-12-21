package com.tvscs.ilead.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatDrawableManager;
import android.support.v7.widget.AppCompatSpinner;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tvscs.ilead.activity.NotificationActivity;
import com.tvscs.ilead.model.CommonPojo;
import com.tvscs.ilead.retrofit.APICallInterface;
import com.tvscs.ilead.retrofit.APIClient;

import org.json.JSONArray;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ilead.tvscs.com.ilead.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Utils implements Constants {
    public static final SimpleDateFormat MONTH_FORMAT = new SimpleDateFormat("MMMM");
    private static final String TAG = "Utils";
    public static String message;
    public static Dialog dialog;
    public static Handler mProgressHandler;
    public static AlertDialog alertDialog;
    private static String EMPTY_STR = "";
    private static Context context;
    private static boolean cancelable = true;
    public static Runnable mShowCustomSpinnerDialog = new Runnable() {

        public void run() {
            try {
                if (message != null)
                    showSpinner(context, message);
                else
                    showSpinner(context);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * @param ctx
         */
        private void showSpinner(Context ctx) {
            try {
                if (isDialogShown()) {
                    dismisssSpinnerDialog();
                }
                dialog = new Dialog(ctx, android.R.style.Theme_Translucent);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(Utils.cancelable);
                dialog.setContentView(R.layout.process_spinner);
                dialog.show();
            } catch (Exception e) {
                dialog = null;
            }
        }

        private void showSpinner(Context ctx, String msg) {
            try {
                if (isDialogShown()) {
                    dismisssSpinnerDialog();
                }
                dialog = new Dialog(ctx);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(Utils.cancelable);
                dialog.setContentView(R.layout.process_spinner);
                TextView text = dialog.findViewById(R.id.textView1);
                text.setText(msg);
                dialog.show();
            } catch (Exception e) {
                dialog = null;
            }
        }
    };
    private static InputMethodManager inputMethodManager = null;

    public static boolean validateObjectValues(Object _validateObj) {
        if (_validateObj == null)
            return false;
        else {
            return !(_validateObj instanceof String
                    && ((String) _validateObj).trim().equalsIgnoreCase(
                    EMPTY_STR));
        }
    }

    /**
     * @param context
     * @param cancelable
     */
    public static void showSpinnerDialog(Context context, boolean cancelable) {

        try {
            Utils.context = context;
            message = null;
            mProgressHandler = new Handler();
            mProgressHandler.post(mShowCustomSpinnerDialog);
            Utils.cancelable = cancelable;
        } catch (Exception e) {
            dialog = null;
        }
    }

    public static boolean isDialogShown() {
        return dialog != null && dialog.isShowing();
    }

    public static void dismisssSpinnerDialog() {
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dialog = null;
        }
    }

    public static void alertRetrofitExceptionalert(String message, final Context context) {
        LayoutInflater inflater;
        View dialogView;
        inflater = LayoutInflater.from(Utils.context);
        dialogView = inflater.inflate(R.layout.alert_message_layout, null);
        alertDialog = new AlertDialog.Builder(Utils.context).create();
        alertDialog.setView(dialogView);
        TextView erroreMessage = dialogView.findViewById(R.id.textViewDilog);

        erroreMessage.setText(message);
        dialogView.findViewById(R.id.yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    ActivityCompat.finishAffinity((Activity) context);
                } else
                    ((Activity) context).finish();

            }
        });

        if (!alertDialog.isShowing()) {
            alertDialog.show();
        }

    }

    public static void alertRetrofitExceptionDialog(Context context, Throwable t) {
        try {
            if (t instanceof SocketTimeoutException) {
                showErrorDialog(context, "Error Alert", "Network Time out. Please try again.");
            } else if (t instanceof ConnectException) {
                showErrorDialog(context, "Error Alert", "Server Time out. Please try again.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void setValueInTextview(TextView textView, String mValue) {
        if (Utils.validateObjectValues(mValue)) {
            textView.setText(mValue);
        } else {
            textView.setText("");
        }
    }

    public static boolean isEmailValid(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
        //return email.contains("@");
    }

    public static boolean isValidMobile(String phone) {
        Pattern pattern;
        Matcher matcher;
        final String MOBILE_PATTERN = "^[6789]\\d{9}$";
        pattern = Pattern.compile(MOBILE_PATTERN);
        matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    public static boolean isValidLicense(String license_number) {
        Pattern pattern;
        Matcher matcher;
        //final String LICENSE_PATTERN = "^[A-Z]{2}[0-9]{2}([A-Z])?([A-Z]*)[0-9]{4}$";
        final String LICENSE_PATTERN = "^[A-Z]{2}[0-9]{2}[A-Z]{2}[0-9]{4}$";
        pattern = Pattern.compile(LICENSE_PATTERN);
        matcher = pattern.matcher(license_number);
        return matcher.matches();
    }

    public static boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    public static String getDeviceIMEI(Context context) {
        try {
            TelephonyManager tManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String deviceIMEI = tManager.getDeviceId();
            return deviceIMEI;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static Dialog showPopupProgressSpinner(Context context) {
        Dialog progressDialog = null;
        progressDialog = new Dialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setContentView(R.layout.loader_dialog);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.show();
        return progressDialog;
    }

    public static void showProgressDialog(Context context, SweetAlertDialog pDialog) {
        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(R.color.colorPrimaryDark);
        pDialog.getProgressHelper().setRimColor(R.color.colorOrange);
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(true);
        pDialog.show();
    }

    public static void hideProgressDialog(SweetAlertDialog loadingDialog) {
        // loadingDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        loadingDialog.dismiss();
    }

    public static SweetAlertDialog successProgressDialog(Context context) {
        SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setTitleText("Success!")
                .setConfirmText("OK")
                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
        pDialog.show();
        return pDialog;
    }

    public static void ShowAlertDialog(final Context context, final String title, String message) {
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(title)
                .setContentText(message)
                .setConfirmText("Ok")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                })
                .show();
    }

    public static void ShowSuccessAlertDialog(final Context context, final String title, String message) {
        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(title)
                .setContentText(message)
                .setConfirmText("Ok")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                })
                .show();
    }

    public static void showErrorDialog(final Context context, final String title, String message) {
        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(title)
                .setContentText(message)
                .setConfirmText("Ok")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                })
                .show();
    }

    public static void showDialogOK(Context ctx, String title, String message, String Positive, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(ctx)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(Positive, okListener)
                .create()
                .show();
    }

    public static String getVersionNumber(Context context) {
        //Context context = getApplicationContext(); // or activity.getApplicationContext()
        PackageManager packageManager = context.getPackageManager();
        String packageName = context.getPackageName();
        String myVersionName = "not available"; // initialize String

        try {
            myVersionName = packageManager.getPackageInfo(packageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return myVersionName;
    }

    public static boolean validateEditText(EditText[] fields) {
        for (int i = 0; i < fields.length; i++) {
            EditText currentField = fields[i];
            if (currentField.getText().toString().length() <= 0) {
                currentField.setError(fieldRequired);
                return false;
            }
        }
        return true;
    }

    public static boolean validateSpinner(AppCompatSpinner[] fields) {
        for (int i = 0; i < fields.length; i++) {
            AppCompatSpinner currentField = fields[i];
            if (currentField.getCount() <= 0 || currentField.getSelectedItem().toString().contains(select)) {
                ((TextView) currentField.getSelectedView()).setError(fieldRequired);
                return false;
            }
        }
        return true;
    }

    public static boolean validateCheckbox(CheckBox[] fields) {
        for (int i = 0; i < fields.length; i++) {
            CheckBox currentField = fields[i];
            if (!currentField.isChecked()) {
                currentField.setError(fieldRequired);
                return false;
            }
        }
        return true;
    }

    public static void showSnackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }

    public static void alertSuccess(Context context, String message, String content) {
        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(message)
                .setContentText(content)
                .show();

    }

    public static void NotificationUpdates(final Context mContext, String userid) {
        CommonPojo retrofitRequest = new CommonPojo();
        APICallInterface mAPICallInterface = APIClient.getAPIService();
        final LoaderDialog viewDialog = new LoaderDialog(mContext);
        try {
            if (isNetworkAvailable(mContext)) {
                try {
                    viewDialog.showProgressDialog();
                    retrofitRequest.setUserid(userid);
                    Call<ResponseBody> responseBodyCall = mAPICallInterface.getClockUpdates(retrofitRequest);
                    responseBodyCall.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            viewDialog.hideProgressDialog();
                            if (response != null) {
                                if (response.code() == 200) {
                                    try {
                                        String responseValue = response.body().string();
                                        if (responseValue.length() > 0) {
                                            JSONArray jsonArray = new JSONArray(responseValue);
                                            if (jsonArray.length() > 0) {
                                                Intent in = new Intent(mContext, NotificationActivity.class);
                                                in.putExtra(sharedPrefLeadData, jsonArray.toString());
                                                mContext.startActivity(in);
                                            } else {
                                                Utils.ShowAlertDialog(mContext, Constants.message, noLeadUpdates);
                                            }
                                        }
                                    } catch (IndexOutOfBoundsException ie) {
                                        ie.printStackTrace();
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
                viewDialog.hideProgressDialog();
                showErrorDialog(mContext, internetErrorTitle, internetError);
            }
        } catch (IndexOutOfBoundsException ie) {
            viewDialog.hideProgressDialog();
            ie.printStackTrace();
        } catch (Exception e) {
            viewDialog.hideProgressDialog();
            e.printStackTrace();
        }
    }

    public static void setSvgImageviewDrawable(ImageView imageview, Context mContext, int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imageview.setImageResource(id);
        } else {
            imageview.setImageDrawable(vectorToBitmapDrawable(mContext, id));
        }
    }

    public static BitmapDrawable vectorToBitmapDrawable(Context mContext, @DrawableRes int resVector) {
        return new BitmapDrawable(mContext.getResources(), vectorToBitmap(mContext, resVector));
    }

    public static Bitmap vectorToBitmap(Context mContext, @DrawableRes int resVector) {
        @SuppressLint("RestrictedApi") Drawable drawable = AppCompatDrawableManager.get().getDrawable(mContext, resVector);
        Bitmap b = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        drawable.setBounds(0, 0, c.getWidth(), c.getHeight());
        drawable.draw(c);
        return b;
    }

    public static void hideKeyboard(Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(((Activity) mContext).getWindow().getCurrentFocus().getWindowToken(), 0);
    }

    public static void showKeyboard(Context mContext) {
        inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public static void closeKeyboard() {
        if (inputMethodManager != null)
            inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }


}
