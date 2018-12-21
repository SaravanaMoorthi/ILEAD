package com.tvscs.ilead.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.tvscs.ilead.activity.HomeActivity;
import com.tvscs.ilead.helper.SharedPreference;
import com.tvscs.ilead.model.CommonPojo;
import com.tvscs.ilead.model.LeadFollowupPojo;
import com.tvscs.ilead.retrofit.APICallInterface;
import com.tvscs.ilead.retrofit.APIClient;
import com.tvscs.ilead.utils.LoaderDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import ilead.tvscs.com.ilead.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tvscs.ilead.utils.Utils.ShowAlertDialog;
import static com.tvscs.ilead.utils.Utils.ShowSuccessAlertDialog;
import static com.tvscs.ilead.utils.Utils.isNetworkAvailable;
import static com.tvscs.ilead.utils.Utils.showDialogOK;
import static com.tvscs.ilead.utils.Utils.showErrorDialog;
import static com.tvscs.ilead.utils.Utils.showSnackbar;
import static com.tvscs.ilead.utils.Utils.validateSpinner;

public class FollowupsFragment extends BaseFragment {

    static EditText actionDate_editText;
    static String actionDate;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    Context mContext;
    APICallInterface mAPICallInterface;
    SharedPreference preference;
    LoaderDialog viewDialog;
    CommonPojo pojo;
    LeadFollowupPojo followupPojo;
    JSONObject followupJsonData;
    @BindView(R.id.followupAction_spinner)
    AppCompatSpinner followupAction_spinner;
    @BindView(R.id.actionResult_spinner)
    AppCompatSpinner actionResult_spinner;
    @BindView(R.id.nextAction_spinner)
    AppCompatSpinner nextAction_spinner;
    @BindView(R.id.actionTime_spinner)
    AppCompatSpinner actionTime_spinner;
    @BindView(R.id.spinerleadstatus)
    AppCompatSpinner leadStatus_spinner;
    @BindView(R.id.status_editText)
    EditText status_editText;
    @BindView(R.id.statusNo_editText)
    EditText statusNo_editText;
    @BindView(R.id.prospectNo_editText)
    EditText prospectNo_editText;
    @BindView(R.id.agreementNo_editText)
    EditText agreementNo_editText;
    @BindView(R.id.fab)
    FloatingActionButton FAB;
    @BindView(R.id.btnSumbit)
    Button btnSumbit;
    @BindView(R.id.btnSave)
    Button btnSave;
    @BindView(R.id.fragmentFollowups_layout)
    RelativeLayout coordinatorLayout;
    ArrayList<CommonPojo> followup = new ArrayList<CommonPojo>();
    ArrayList<CommonPojo> nextaction = new ArrayList<CommonPojo>();
    ArrayList<CommonPojo> actiontime = new ArrayList<CommonPojo>();
    ArrayList<CommonPojo> actionresult = new ArrayList<CommonPojo>();
    ArrayList<CommonPojo> leadstatus = new ArrayList<CommonPojo>();
    ArrayAdapter<CommonPojo> Followupadapter, Nextactionadapter, Actiontimeadapter, Actionresultadapter, Leadstatusadapter;
    String followupCode;
    String followupId;
    String nextactionCode;
    String nextactionDescription;
    String leadStatus;
    String leadNumber;
    String costomerName;
    String customerProfile;
    String contactNumber;
    String sceEmpono;
    String agreeentNo;
    String actionResult;
    String actionTime;
    String los_prospec_no, los_branch_code, los_disbursedDate, los_disbursed_amount, los_logindate, los_agreementNo;
    Boolean isDateError, isTimeError, isProsPectNumberError, editTextEmpty, spinnerEmpty, leadSpinnerEmpty;
    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    @Override
    public BaseFragment baseFragment() {
        return new FollowupsFragment();
    }

    @Override
    public View fragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_followups, parent, false);
        ButterKnife.bind(this, view);
        mContext = getActivity();
        preference = new SharedPreference(mContext);
        mAPICallInterface = APIClient.getAPIService();
        viewDialog = new LoaderDialog(mContext);
        actionDate_editText = (EditText) view.findViewById(R.id.actionDate_editText);
        actionDate_editText.setCompoundDrawablePadding(10);
        initView();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getFollowupUpdate(leadNumber);
        //getFollowupAction();
    }

    private void initView() {
        try {
            if (getArguments() != null) {
                leadNumber = getArguments().getString(ileadNo);
            }
            /*if (preference.getPref(sharedPrefclickUpdate, "").length() > 0) {
                leadStatus_spinner.setVisibility(View.GONE);

                getActivity().setTitle(leadFollowupTxt);
                agreementNo_editText.setEnabled(false);
                prospectNo_editText.setEnabled(false);
                statusNo_editText.setEnabled(false);
                status_editText.setEnabled(false);

                followupJsonData = new JSONObject(preference.getPref(sharedPrefclickUpdate, ""));
                statusNo_editText.setText(followupJsonData.getString(respSceEmpNum_));
                status_editText.setText(followupJsonData.getString(respstatus));
                prospectNo_editText.setText(followupJsonData.getString(respProspectNum_));
                agreementNo_editText.setText(followupJsonData.getString(respAgreementNum_));
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getFollowupUpdate(String leadNumber) {
        if (isNetworkAvailable(mContext)) {
            try {
                viewDialog.showProgressDialog();
                pojo = new CommonPojo();
                pojo.setUser_id(preference.getPref(USERID, ""));
                pojo.setLead_id(leadNumber);
                pojo.setMobile("");
                Call<ResponseBody> responseBodyCall = mAPICallInterface.getleadsReceived(pojo);
                responseBodyCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response != null) {
                            if (response.code() == 200) {
                                viewDialog.hideProgressDialog();
                                try {
                                    String responseValue = response.body().string();
                                    JSONArray jsonArray = new JSONArray(responseValue);
                                    try {
                                        if (jsonArray.length() > 0) {
                                            JSONObject jsonObject = new JSONObject(jsonArray.getString(0));
                                            preference.setPref(sharedPrefclickUpdate, jsonObject.toString());
                                            followupJsonData = new JSONObject(preference.getPref(sharedPrefclickUpdate, jsonObject.toString()));

                                            if (jsonObject.length() > 0) {
                                                leadStatus_spinner.setVisibility(View.GONE);

                                                getActivity().setTitle(leadFollowupTxt);
                                                agreementNo_editText.setEnabled(false);
                                                prospectNo_editText.setEnabled(false);
                                                statusNo_editText.setEnabled(false);
                                                status_editText.setEnabled(false);

                                                followupJsonData = new JSONObject(preference.getPref(sharedPrefclickUpdate, ""));
                                                statusNo_editText.setText(followupJsonData.getString(respSceEmpNum_));
                                                status_editText.setText(followupJsonData.getString(respstatus));
                                                prospectNo_editText.setText(followupJsonData.getString(respProspectNum_));
                                                agreementNo_editText.setText(followupJsonData.getString(respAgreementNum_));
                                            }
                                        } else {
                                            ShowAlertDialog(mContext, message, noRecordsMsg);
                                        }
                                        //Get Followup Action into Spinner
                                        getFollowupAction();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
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
            showErrorDialog(mContext, internetErrorTitle, internetError);
        }
    }

    @OnItemSelected({R.id.followupAction_spinner, R.id.actionResult_spinner, R.id.nextAction_spinner, R.id.actionTime_spinner})
    public void spinnerItemSelected(AppCompatSpinner spinner, int position) {
        try {
            switch (spinner.getId()) {
                case R.id.followupAction_spinner:
                    CommonPojo followup = (CommonPojo) followupAction_spinner.getSelectedItem();
                    followupCode = followup.getKEY_CODE();
                    followupId = followup.getKEY_ID();
                    if (!followupId.equalsIgnoreCase("0")) {
                        //Get ActionResult into Spinner
                        getActionResult(followupId);
                    }
                    break;

                case R.id.actionResult_spinner:
                    CommonPojo actionresult = (CommonPojo) actionResult_spinner.getSelectedItem();
                    actionResult = actionresult.getKEY_CODE();
                    break;

                case R.id.nextAction_spinner:
                    CommonPojo nextaction = (CommonPojo) nextAction_spinner.getSelectedItem();
                    nextactionCode = nextaction.getKEY_CODE();
                    nextactionDescription = nextaction.getKEY_DESCRIPTION();
                    if (!nextactionCode.equalsIgnoreCase("0")) {
                        //Get LeadStatus
                        getLeadStatus(nextactionCode);
                    }
                    break;

                case R.id.actionTime_spinner:
                    CommonPojo actiontime = (CommonPojo) actionTime_spinner.getSelectedItem();
                    // actionTime = actiontime.getKEY_CODE();
                    actionTime = actiontime.getKEY_DESCRIPTION();
                    break;

                case R.id.spinerleadstatus:
                    CommonPojo leadstatus = (CommonPojo) leadStatus_spinner.getSelectedItem();
                    leadStatus = leadstatus.getKEY_DESCRIPTION();
                    Actiondatetimevalidation();
                    break;

                default:
                    break;
            }
        } catch (ClassCastException ce) {
            ce.printStackTrace();
        }
    }

    @Nullable
    @OnClick({R.id.actionDate_editText, R.id.btnSumbit, R.id.btnSave, R.id.fab})
    void onButtonClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.actionDate_editText:
                    DialogFragment picker = new DatePickerFragment();
                    picker.show(getFragmentManager(), "datePicker");
                    break;

                case R.id.btnSave:

                    /*editTextEmpty = validateEditText(new EditText[]{actionDate_editText, status_editText, statusNo_editText, prospectNo_editText,
                            agreementNo_editText});*/
                    spinnerEmpty = validateSpinner(new AppCompatSpinner[]{followupAction_spinner, actionResult_spinner, nextAction_spinner});

                    if (!validateSpinner(new AppCompatSpinner[]{followupAction_spinner}))
                        showSnackbar(coordinatorLayout, selectFollowupActionTxt);

                    else if (!validateSpinner(new AppCompatSpinner[]{actionResult_spinner}))
                        showSnackbar(coordinatorLayout, selectActionResultTxt);

                    else if (!validateSpinner(new AppCompatSpinner[]{nextAction_spinner}))
                        showSnackbar(coordinatorLayout, selectNextActionTxt);

                    else if (leadStatus_spinner.getVisibility() == View.VISIBLE) {
                        if (validateSpinner(new AppCompatSpinner[]{leadStatus_spinner})) {
                            leadStatus = leadStatus_spinner.getSelectedItem().toString();
                            Actiondatetimevalidation();
                            getdroppedCaseAuth(leadNumber);
                        }
                    } else
                        getdroppedCaseAuth(leadNumber);

                    /*if (spinnerEmpty) {
                        Actiondatetimevalidation();
                        if(leadStatus_spinner.getVisibility()==View.VISIBLE){
                            leadSpinnerEmpty = validateSpinner(new AppCompatSpinner[]{leadStatus_spinner});
                            if(leadSpinnerEmpty){
                                leadStatus = leadStatus_spinner.getSelectedItem().toString();
                                getdroppedCaseAuth(leadNumber);
                            }
                        }else{
                            getdroppedCaseAuth(leadNumber);
                        }
                    }else{
                        showSnackbar(coordinatorLayout, fillDataMsg);
                    }*/
                    break;

                case R.id.btnSumbit:
                    try {
                        startActivity(new Intent(mContext, HomeActivity.class));
                        getActivity().finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case R.id.fab:
                    try {
                        BottomSheetDialogFragment myBottomSheet = FollowupFragment_BottomSheet.newInstance(leadNumber);
                        myBottomSheet.show(getFragmentManager(), myBottomSheet.getTag());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        } catch (NullPointerException n) {
            n.printStackTrace();
        }
    }

    private void getdroppedCaseAuth(String leadNumber) {

        if (isNetworkAvailable(mContext)) {
            try {
                viewDialog.showProgressDialog();
                pojo = new CommonPojo();
                pojo.setLead_id(leadNumber);
                pojo.setLogged_user_id(preference.getPref(USERID, ""));
                Call<ResponseBody> responseBodyCall = mAPICallInterface.getDropcaseAuth(pojo);
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
                                        if (jsonArray != null) {
                                            JSONObject Dropdata = jsonArray.getJSONObject(0);
                                            if (Dropdata.length() > 0) {
                                                if (jsonArray.getJSONObject(0).getString(respRolecode_) != adminTxt && (jsonArray.getJSONObject(0).getString(respstatus) == droppedTxt || jsonArray.getJSONObject(0).getString(respstatus) == disbursedTxt)) {
                                                    showErrorDialog(getActivity(), informationTxt, followupErrorMsg1 + jsonArray.getJSONObject(0).getString(respstatus) + followupErrorMsg2);
                                                } /*else if (leadStatus.equalsIgnoreCase(loginTxt) || leadStatus.equalsIgnoreCase(disbursedTxt)) {
                                                    //Actiondatetimevalidation();
                                                    entrycheckprospect();
                                                }*/ else {
                                                    Entrysave();
                                                }
                                            }
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
    }

    private void entrycheckprospect() {
        if (prospectNo_editText.getText().toString() != null) {
            isProsPectNumberError = false;
        }
        if (isProsPectNumberError == true) {
            showErrorDialog(getActivity(), informationTxt, prospectNumEmptyErrorMsg);
        } else {
            getLOSData(prospectNo_editText.getText().toString());
        }
    }

    private void getLOSData(final String prospectNo) {
        try {
            if (isNetworkAvailable(mContext)) {
                try {
                    pojo = new CommonPojo();
                    pojo.setProspect_no(prospectNo);
                    Call<ResponseBody> responseBodyCall = mAPICallInterface.getLOSData(pojo);
                    responseBodyCall.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response != null) {
                                if (response.code() == 200) {
                                    try {
                                        String responseValue = response.body().string();
                                        if (responseValue.length() > 0) {
                                            JSONArray jsonArray = new JSONArray(responseValue);
                                            if (jsonArray.length() > 0) {
                                                JSONObject losdata = jsonArray.getJSONObject(0);
                                                if (losdata.length() > 0) {
                                                    if (prospectNo.equalsIgnoreCase(jsonArray.getJSONObject(0).getString(respprospectNum_))) {
                                                        los_prospec_no = losdata.getString(respprospectNum_);
                                                        los_branch_code = losdata.getString(respBranchCode_);
                                                        los_disbursedDate = losdata.getString(respDisburseddate);
                                                        los_disbursed_amount = losdata.getString(respDisbursedAmount_);
                                                        los_logindate = losdata.getString(respLogindate);
                                                        los_agreementNo = losdata.getString(respAgreementNum);
                                                        if (los_agreementNo.equalsIgnoreCase("") && leadStatus.equalsIgnoreCase(disbursedTxt)) {
                                                            showErrorDialog(getActivity(), message, agreementNoError);
                                                        } else {
                                                            entryLOSdata_in_LMS_mobile();
                                                            //Entrysave();
                                                        }
                                                    } else {
                                                        showErrorDialog(getActivity(), message, prospectNoDbError);
                                                    }
                                                }
                                            } else {
                                                showErrorDialog(getActivity(), message, prospectNoInvalidError);
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
                            t.printStackTrace();
                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                showErrorDialog(mContext, internetErrorTitle, internetError);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void Entrysave() {
        try {
            if (!(leadStatus.equalsIgnoreCase(droppedTxt) || leadStatus.equalsIgnoreCase(rejectedTxt) ||
                    leadStatus.equalsIgnoreCase(disbursedTxt) || leadStatus.equalsIgnoreCase(lostTxt) ||
                    leadStatus.equalsIgnoreCase(selectLeadStatusTxt.toUpperCase()))) {
                if ((actionDate != null) && !(actionTime.equalsIgnoreCase(selectActionTimeTxt))) {
                    isTimeError = false;
                    isDateError = false;
                }
                if (isTimeError == true || isDateError == true) {
                    showErrorDialog(getActivity(), informationTxt, dateTimeEmptyErrorMsg);
                } else {
                    insertFollowup();
                }
            } else {
                insertFollowup();
            }
        } catch (NullPointerException n) {
            n.printStackTrace();
        }
    }

    private void insertFollowup() {

        if (isNetworkAvailable(mContext)) {
            try {
                followupPojo = new LeadFollowupPojo();
                followupPojo.setLead_id(leadNumber);
                followupPojo.setAction_code(followupCode);
                followupPojo.setResult_code(actionResult);
                followupPojo.setNxt_act_code(nextactionCode);
                followupPojo.setNxt_act_date(actionDate);
                if (actionTime.contains("Select"))
                    followupPojo.setNxt_act_time("");
                else
                    followupPojo.setNxt_act_time(actionTime);

                followupPojo.setStatus(leadStatus);
                followupPojo.setRemarks("");
                followupPojo.setSce_emp_no(sceEmpono);
                followupPojo.setSce_emp_name("");
                followupPojo.setCreated_by(preference.getPref(USERID, ""));
                followupPojo.setTM_User_ID("");
                followupPojo.setLead_category("");
                followupPojo.setPayout("");
                viewDialog.showProgressDialog();
                pojo = new CommonPojo();
                pojo.setObjLms(followupPojo);
                Call<ResponseBody> responseBodyCall = mAPICallInterface.insertFollowupData(pojo);
                responseBodyCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        viewDialog.hideProgressDialog();
                        if (response != null) {
                            if (response.code() == 200) {
                                //
                                try {
                                    String responseValue = response.body().string();
                                    if (responseValue.length() > 0) {
                                        JSONObject jsonObject = new JSONObject(responseValue);
                                        if (jsonObject.length() > 0) {
                                            if (jsonObject.getString(respleadFollowupResult).equalsIgnoreCase("0")) {
                                                ShowSuccessAlertDialog(getActivity(), informationTxt, leadUpdateInsertMsg);
                                                FollowupClear();
                                            } else {
                                                showErrorDialog(getActivity(), informationTxt, errorSubmittingMsg);
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
            viewDialog.hideProgressDialog();
            ShowAlertDialog(mContext, informationTxt, internetError);
        }
    }

    private void entryLOSdata_in_LMS_mobile() {
        try {
            if (isNetworkAvailable(mContext)) {
                try {
                    pojo = new CommonPojo();
                    pojo.setLead_id(leadNumber);
                    pojo.setProspec_no(los_prospec_no);
                    pojo.setBranch_code(los_branch_code);
                    pojo.setDisbursedamount(los_disbursed_amount);
                    pojo.setLogindate(los_logindate);
                    pojo.setDisbusdate(los_disbursedDate);
                    pojo.setAgreement_no(los_agreementNo);
                    Call<ResponseBody> responseBodyCall = mAPICallInterface.updateLOSData(pojo);
                    responseBodyCall.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response != null) {
                                if (response.code() == 200) {
                                    try {
                                        String responseValue = response.body().string();
                                        if (responseValue.length() > 0) {
                                            JSONObject jsonObject = new JSONObject(responseValue);
                                            if (jsonObject != null) {
                                                if (jsonObject.getString(respupdateLosDataResult).equalsIgnoreCase("true")) {
                                                    showDialogOK(getActivity(), message, Losdatasuccess, OkTxt, new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            dialogInterface.dismiss();
                                                            Entrysave();
                                                        }
                                                    });
                                                } else {
                                                    showDialogOK(getActivity(), message, Losdatafailure, OkTxt, new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            dialogInterface.dismiss();
                                                        }
                                                    });
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
                            t.printStackTrace();
                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                showErrorDialog(mContext, internetErrorTitle, internetError);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getFollowupAction() {

        if (isNetworkAvailable(mContext)) {
            try {
                //viewDialog.showProgressDialog();
                Call<ResponseBody> responseBodyCall = mAPICallInterface.getFollowupAction();
                responseBodyCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response != null) {
                            if (response.code() == 200) {
                                //viewDialog.hideProgressDialog();
                                try {
                                    String responseValue = response.body().string();
                                    JSONArray jsonArray = new JSONArray(responseValue);
                                    if (jsonArray.length() > 0) {
                                        for (int i = 0; i <= jsonArray.length(); i++) {
                                            if (i == 0) {
                                                setFirstMultiArraylist("0", selectFollowupActionTxt, "0", followup);
                                            } else {
                                                setFirstMultiArraylist(jsonArray.getJSONObject(i - 1).getString(respActionCode),
                                                        jsonArray.getJSONObject(i - 1).getString(respActionDesc),
                                                        jsonArray.getJSONObject(i - 1).getString(respActionId), followup);
                                            }
                                        }
                                        setSpinnerValue(followup, followupAction_spinner, Followupadapter);
                                        getNextAction();
                                    }

                                } catch (Exception e) {
                                    //viewDialog.hideProgressDialog();
                                    e.printStackTrace();
                                }
                            } else {
                                //viewDialog.hideProgressDialog();
                                showErrorDialog(mContext, informationTxt, serverErrorMsg);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        //viewDialog.hideProgressDialog();
                    }
                });


            } catch (Exception e) {
                //viewDialog.hideProgressDialog();
                e.printStackTrace();
            }
        } else {
            showErrorDialog(mContext, internetErrorTitle, internetError);
        }
    }

    private void getNextAction() {
        if (isNetworkAvailable(mContext)) {
            try {
                viewDialog.showProgressDialog();
                Call<ResponseBody> responseBodyCall = mAPICallInterface.getNextAction();
                responseBodyCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response != null) {
                            if (response.code() == 200) {
                                viewDialog.hideProgressDialog();
                                try {
                                    String responseValue = response.body().string();
                                    JSONArray jsonArray = new JSONArray(responseValue);
                                    if (jsonArray.length() > 0) {
                                        for (int i = 0; i <= jsonArray.length(); i++) {
                                            if (i == 0) {
                                                setFirstArraylist("0", selectNextActionTxt, nextaction);
                                            } else {
                                                setFirstArraylist(jsonArray.getJSONObject(i - 1).getString(respNextActionCode), jsonArray.getJSONObject(i - 1).getString(respNextActionDesc), nextaction);
                                            }
                                        }
                                        setSpinnerValue(nextaction, nextAction_spinner, Nextactionadapter);
                                        getActionTime();
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
            showErrorDialog(mContext, internetErrorTitle, internetError);
        }

    }

    private void getActionTime() {
        if (isNetworkAvailable(mContext)) {
            try {
                viewDialog.showProgressDialog();
                pojo = new CommonPojo();
                pojo.setKey_cod("Next Time");
                Call<ResponseBody> responseBodyCall = mAPICallInterface.getActionTime(pojo);
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
                                        if (jsonArray != null) {

                                            //jsonParam1.put("key_cod", "Next Time");
                                            setFirstArraylist("0", selectActionTimeTxt, actiontime);
                                            setFirstArraylist("0", selectActionResultTxt, actionresult);
                                            setFirstArraylist("0", selectLeadStatusTxt, leadstatus);

                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                if (jsonArray.getJSONObject(i).getString(respKeycode).equalsIgnoreCase(respNextTime)) {
                                                    setFirstArraylist(jsonArray.getJSONObject(i).getString(respCode), jsonArray.getJSONObject(i).getString(respCodeDesc), actiontime);
                                                }
                                            }
                                            setSpinnerValue(actiontime, actionTime_spinner, Actiontimeadapter);
                                            setSpinnerValue(actionresult, actionResult_spinner, Actionresultadapter);
                                            setSpinnerValue(leadstatus, leadStatus_spinner, Leadstatusadapter);
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
            showErrorDialog(mContext, internetErrorTitle, internetError);
        }
    }

    private void getActionResult(String followupId) {
        if (isNetworkAvailable(mContext)) {
            actionresult.add(clearAdapter(actionresult, actionResult_spinner));
            try {
                viewDialog.showProgressDialog();
                pojo = new CommonPojo();
                pojo.setAction_id(followupId);
                pojo.setLead_id(leadNumber);
                Call<ResponseBody> responseBodyCall = mAPICallInterface.getActionResult(pojo);
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
                                        if (jsonArray != null) {
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                setFirstMultiArraylist(jsonArray.getJSONObject(i).getString(respActionresultCode), jsonArray.getJSONObject(i).getString(respActionresultDesc), jsonArray.getJSONObject(i).getString(respActionresultId), actionresult);
                                            }
                                            setSpinnerValue(actionresult, actionResult_spinner, Actionresultadapter);
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
    }

    private void getLeadStatus(String nextactionCode) {
        try {
            if (isNetworkAvailable(mContext)) {
                leadstatus.add(clearAdapter(leadstatus, leadStatus_spinner));
                try {
                    viewDialog.showProgressDialog();
                    pojo = new CommonPojo();
                    pojo.setNxt_act_code(nextactionCode);
                    Call<ResponseBody> responseBodyCall = mAPICallInterface.getLeadStatus(pojo);
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
                                                int datalength = jsonArray.length();
                                                status_editText.setVisibility(View.GONE);
                                                leadStatus_spinner.setVisibility(View.GONE);
                                                for (int i = 0; i < jsonArray.length(); i++) {
                                                    if (datalength == 1) {
                                                        status_editText.setVisibility(View.VISIBLE);
                                                        status_editText.setText(jsonArray.getJSONObject(i).getString(respstatus));
                                                        leadStatus = status_editText.getText().toString();
                                                        status_editText.setEnabled(false);
                                                        Actiondatetimevalidation();
                                                    } else {
                                                        leadStatus_spinner.setVisibility(View.VISIBLE);
                                                        setFirstArraylist("i+1", jsonArray.getJSONObject(i).getString(respstatus), leadstatus);
                                                    }
                                                }

                                                if (leadStatus_spinner.getVisibility() == leadStatus_spinner.VISIBLE) {
                                                    setSpinnerValue(leadstatus, leadStatus_spinner, Leadstatusadapter);
                                                }

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
                            t.printStackTrace();
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

    public void Actiondatetimevalidation() {
        if (leadStatus.equalsIgnoreCase(droppedTxt) || leadStatus.equalsIgnoreCase(rejectedTxt) || leadStatus.equalsIgnoreCase(disbursedTxt) || leadStatus.equalsIgnoreCase(lostTxt) || leadStatus.equalsIgnoreCase(selectLeadStatusTxt.toUpperCase())) {
            actionDate_editText.setError(null);
            ((TextView) actionTime_spinner.getSelectedView()).setError(null);
            actionDate_editText.setEnabled(false);
            actionTime_spinner.setEnabled(false);
            actionDate_editText.setText("");
            actionDate = "";
            actionTime_spinner.setSelection(0);
            isDateError = false;
            isTimeError = false;

        } else {
            actionDate_editText.setEnabled(true);
            actionTime_spinner.setEnabled(true);
            actionDate_editText.setError(actionDateEmptyErrorMsg);
            ((TextView) actionTime_spinner.getSelectedView()).setError(selectActionTimeTxt);
            isDateError = true;
            isTimeError = true;
            if (actionDate_editText.getText().length() > 0) {
                actionDate_editText.setError(null);
            }
            if (!(actionTime_spinner.getSelectedItem().toString().equalsIgnoreCase(selectActionTimeTxt))) {
                ((TextView) actionTime_spinner.getSelectedView()).setError(null);
            }

        }
        if (leadStatus.equalsIgnoreCase(loginTxt) || leadStatus.equalsIgnoreCase(disbursedTxt) ||
                status_editText.getText().toString().equalsIgnoreCase(disbursedTxt)) {
            /*prospectNo_editText.setError(prospectNumEmptyErrorMsg);
            prospectNo_editText.setEnabled(true);
            isProsPectNumberError = true;
            if (prospectNo_editText.getText().toString().length() > 0) {
                prospectNo_editText.setError(null);
                isProsPectNumberError = false;
            }*/
            prospectNo_editText.setError(null);
            prospectNo_editText.setEnabled(false);
        } else {
            //prospectNo_editText.setText("");
            prospectNo_editText.setError(null);
            prospectNo_editText.setEnabled(false);
            //isProsPectNumberError = false;
        }
    }

    public CommonPojo clearAdapter(ArrayList<CommonPojo> adapter, Spinner spinner) {
        CommonPojo selectedItem = (CommonPojo) spinner.getItemAtPosition(0); // Object which was selected.
        String description = selectedItem.getKEY_DESCRIPTION();
        String code = selectedItem.getKEY_CODE();
        adapter.clear();
        pojo = new CommonPojo();
        pojo.setKEY_CODE(code);
        pojo.setKEY_DESCRIPTION(description);
        return pojo;
    }

    public void setSpinnerValue(ArrayList<CommonPojo> list, Spinner spinner, ArrayAdapter<CommonPojo> adapter) {
        adapter = new ArrayAdapter<CommonPojo>(getActivity(), R.layout.spinner_item, list);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setSelection(0, false);
        spinner.setAdapter(adapter);
    }

    public void setFirstArraylist(String code, String Description, ArrayList<CommonPojo> arrayList) {
        pojo = new CommonPojo();
        pojo.setKEY_CODE(code);
        pojo.setKEY_DESCRIPTION(Description);
        arrayList.add(pojo);

    }

    public void setFirstMultiArraylist(String code, String Description, String id, ArrayList<CommonPojo> arrayList) {
        pojo = new CommonPojo();
        pojo.setKEY_CODE(code);
        pojo.setKEY_DESCRIPTION(Description);
        pojo.setKEY_ID(id);
        arrayList.add(pojo);

    }

    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        actionDate_editText.setText(sdf.format(myCalendar.getTime()));
        actionDate = actionDate_editText.getText().toString();
    }

    public void FollowupClear() {
        actionDate_editText.setText("");
        followupAction_spinner.setSelection(0);
        nextAction_spinner.setSelection(0);
        actionResult_spinner.setSelection(0);
        actionTime_spinner.setSelection(0);
        leadStatus_spinner.setSelection(0);
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        DatePickerDialog datePickerDialog;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            return datePickerDialog;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar c = Calendar.getInstance();
            c.set(year, month, day);

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            String formattedDate = sdf.format(c.getTime());

            actionDate_editText.setText(formattedDate);
            actionDate_editText.setError(null);
            actionDate = actionDate_editText.getText().toString().trim();
        }
    }

}
