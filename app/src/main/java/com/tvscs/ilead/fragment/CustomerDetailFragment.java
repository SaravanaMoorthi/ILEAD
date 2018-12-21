package com.tvscs.ilead.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.tvscs.ilead.activity.LeadUpdateActivity;
import com.tvscs.ilead.helper.SharedPreference;
import com.tvscs.ilead.model.CommonPojo;
import com.tvscs.ilead.model.LeadUpdatePojo;
import com.tvscs.ilead.retrofit.APICallInterface;
import com.tvscs.ilead.retrofit.APIClient;
import com.tvscs.ilead.utils.Constants;
import com.tvscs.ilead.utils.LoaderDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import butterknife.OnTextChanged;
import cn.pedant.SweetAlert.SweetAlertDialog;
import ilead.tvscs.com.ilead.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tvscs.ilead.utils.Utils.ShowAlertDialog;
import static com.tvscs.ilead.utils.Utils.isNetworkAvailable;
import static com.tvscs.ilead.utils.Utils.isValidLicense;
import static com.tvscs.ilead.utils.Utils.showErrorDialog;
import static com.tvscs.ilead.utils.Utils.showSnackbar;
import static com.tvscs.ilead.utils.Utils.validateEditText;
import static com.tvscs.ilead.utils.Utils.validateSpinner;

public class CustomerDetailFragment extends BaseFragment {
    public static String customerCode, customerDescription, stateCode, stateDescription, cityCode, cityDescription, residenceCode,
            residenceDescription, yearCode, yearDescription, makeCode, modelCode, veriantCode, vehicleCode, vehicleDescription,
            talukCode, talukDescription, repaymentCode, repaymentDescription;
    public static String firstName, phoneNo1, phoneNo2, address1, address2, pinCode, registrationNo, loanAmount,
            tenure, emi;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    Context mContext;
    APICallInterface mAPICallInterface;
    SharedPreference preference;
    LoaderDialog viewDialog;
    String leadNo;
    String getLeadId = "";
    int pincodeLen, cityInc = 0, talukInc = 0, makeInc = 0, modelInc = 0, variantInc = 0;
    boolean editTextEmpty, spinnerEmpty, checkBoxEmpty;
    CommonPojo customerPojo;
    LeadUpdatePojo leadUpdatePojo;
    ArrayList<CommonPojo> year;
    ArrayList<CommonPojo> customer;
    ArrayList<CommonPojo> residencestatus;
    ArrayList<CommonPojo> repayment;
    ArrayList<CommonPojo> state;
    ArrayList<CommonPojo> city;
    ArrayList<CommonPojo> make;
    ArrayList<CommonPojo> model;
    ArrayList<CommonPojo> variant;
    ArrayList<CommonPojo> vehicle;
    ArrayList<CommonPojo> taluk;
    JSONObject customerJsonObject;
    ArrayAdapter<CommonPojo> Yearadapter, Customeradapter, Residenceadapter, Repaymentadapter, Stateadapte,
            Cityadapter, TalukTypeadapter, Makeadapter, Modeladapter, Variantadapter, Vehicleadapter;
    @BindView(R.id.cusName_editText)
    EditText cusName_editText;
    @BindView(R.id.cusNo_editText)
    EditText cusNo_editText;
    @BindView(R.id.cusAlterNo_editText)
    EditText cusAlterNo_editText;
    @BindView(R.id.cusAdd1_editText)
    EditText cusAdd1_editText;
    @BindView(R.id.cusAdd2_editText)
    EditText cusAdd2_editText;
    @BindView(R.id.cusPincode_editText)
    EditText cusPincode_editText;
    @BindView(R.id.vehicleRegNo_editText)
    EditText vehicleRegNo_editText;
    @BindView(R.id.loanAmt_editText)
    EditText loanAmt_editText;
    @BindView(R.id.loanTenor_editText)
    EditText loanTenor_editText;
    @BindView(R.id.emiComfort_editText)
    EditText emiComfort_editText;
    @BindView(R.id.btnNext)
    Button btnNext;
    @BindView(R.id.btnSave)
    Button btnSave;
    @BindView(R.id.customerDetail_mainLayout)
    ConstraintLayout customerDetail_mainLayout;
    @BindView(R.id.leadNo_textView)
    TextView leadNo_textView;
    @BindView(R.id.productImageView)
    ImageView productImageView;
    @BindView(R.id.pancard_checkBox)
    CheckBox pancard_checkBox;
    @BindView(R.id.passport_checkBox)
    CheckBox passport_checkBox;
    @BindView(R.id.voterid_checkBox)
    CheckBox voterid_checkBox;
    @BindView(R.id.drivinglic_checkBox)
    CheckBox drivinglic_checkBox;
    @BindView(R.id.rationcard_checkBox)
    CheckBox rationcard_checkBox;
    @BindView(R.id.aadharcard_checkBox)
    CheckBox aadharcard_checkBox;
    @BindView(R.id.cusVehicaleFinance_switch)
    SwitchCompat S_Vehicle;
    @BindView(R.id.cusState_spinner)
    AppCompatSpinner S_State;
    @BindView(R.id.custCity_spinner)
    AppCompatSpinner S_City;
    @BindView(R.id.cusTaluk_spinner)
    AppCompatSpinner S_Taluk;
    @BindView(R.id.cusResiStatus_spinner)
    AppCompatSpinner S_Residence;
    @BindView(R.id.repaymentMode_spinner)
    AppCompatSpinner S_Repayment;
    @BindView(R.id.manufacturedYear_spinner)
    AppCompatSpinner S_Year;
    @BindView(R.id.make_spinner)
    AppCompatSpinner S_Make;
    @BindView(R.id.model_spinner)
    AppCompatSpinner S_Model;
    @BindView(R.id.variant_spinner)
    AppCompatSpinner S_Variant;
    @BindView(R.id.cusProfile_spinner)
    AppCompatSpinner S_Customer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public BaseFragment baseFragment() {
        if (getArguments() != null) {
            leadNo = getArguments().getString(ileadNo);
        }
        return new CustomerDetailFragment();
    }

    @Override
    public View fragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_customerdetail, parent, false);
        ButterKnife.bind(this, view);
        mContext = getActivity();
        if (mContext != null) {
            preference = new SharedPreference(mContext);
        }
        mAPICallInterface = APIClient.getAPIService();
        viewDialog = new LoaderDialog(mContext);
        initViews();

        AppCompatSpinner[] spinners = {S_State, S_City, S_Taluk, S_Residence, S_Repayment, S_Year, S_Make, S_Model, S_Variant, S_Customer};
        for (Spinner mySpinner : spinners) {
            mySpinner.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    try {
                        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return false;
                }
            });
        }

        EditText[] editTexts = {cusName_editText, cusAdd1_editText, cusAdd2_editText, vehicleRegNo_editText};
        for (EditText myEditText : editTexts) {
            myEditText.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            myEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initViews() {
        year = new ArrayList<CommonPojo>();
        customer = new ArrayList<CommonPojo>();
        residencestatus = new ArrayList<CommonPojo>();
        repayment = new ArrayList<CommonPojo>();
        state = new ArrayList<CommonPojo>();
        city = new ArrayList<CommonPojo>();
        make = new ArrayList<CommonPojo>();
        model = new ArrayList<CommonPojo>();
        variant = new ArrayList<CommonPojo>();
        vehicle = new ArrayList<CommonPojo>();
        taluk = new ArrayList<CommonPojo>();
        if (getArguments() != null) {
            getLeadId = getArguments().getString(ileadNo);
        }
        getLeadUpdate(getLeadId);
    }

    @OnTextChanged(value = R.id.vehicleRegNo_editText, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void vehicleNoChanged(CharSequence s) {
        try {
            if (S_Vehicle.isChecked()) {
                vehicleRegNo_editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10), new InputFilter.AllCaps()});
                if (s.length() != 0 && s.length() > 9) {
                    if (!isValidLicense(vehicleRegNo_editText.getText().toString()))
                        vehicleRegNo_editText.setError(regNoInvalidErrorMsg);
                }
            }

        } catch (IndexOutOfBoundsException ie) {
            ie.printStackTrace();
        }
    }

    @OnItemSelected({R.id.cusState_spinner, R.id.custCity_spinner, R.id.cusTaluk_spinner, R.id.cusProfile_spinner,
            R.id.cusResiStatus_spinner, R.id.manufacturedYear_spinner, R.id.make_spinner, R.id.model_spinner,
            R.id.variant_spinner, R.id.repaymentMode_spinner})
    public void spinnerItemSelected(AppCompatSpinner spinner, int position) {
        try {
            switch (spinner.getId()) {
                case R.id.cusState_spinner:
                    CommonPojo stateid = (CommonPojo) S_State.getSelectedItem(); // Object which was selected.
                    stateCode = stateid.getKEY_CODE();
                    stateDescription = stateid.getKEY_DESCRIPTION();
                    if (!stateCode.equalsIgnoreCase("0")) {
                        getCity(stateCode);
                    } else {
                        city.add(clearAdapter(city, S_City));
                        setSpinnerValue(city, S_City, Cityadapter);
                    }
                    break;

                case R.id.custCity_spinner:
                    CommonPojo cityid = (CommonPojo) S_City.getSelectedItem();
                    cityCode = cityid.getKEY_CODE();
                    cityDescription = cityid.getKEY_DESCRIPTION();
                    if (!cityCode.equalsIgnoreCase("0")) {
                        getTaluk(cityCode);
                    } else {
                        taluk.add(clearAdapter(taluk, S_Taluk));
                        setSpinnerValue(taluk, S_Taluk, TalukTypeadapter);
                    }
                    break;

                case R.id.cusTaluk_spinner:
                    CommonPojo talukid = (CommonPojo) S_Taluk.getSelectedItem();
                    talukCode = talukid.getKEY_CODE();
                    talukDescription = talukid.getKEY_DESCRIPTION();
                    if (!talukCode.equalsIgnoreCase("0")) {
                        getTownPinCode(talukCode);
                    }

                case R.id.cusProfile_spinner:
                    CommonPojo profileid = (CommonPojo) S_Customer.getSelectedItem();
                    customerCode = profileid.getKEY_CODE();
                    customerDescription = profileid.getKEY_DESCRIPTION();
                    break;

                case R.id.cusResiStatus_spinner:
                    CommonPojo residencestatusid = (CommonPojo) S_Residence.getSelectedItem();
                    residenceCode = residencestatusid.getKEY_CODE();
                    residenceDescription = residencestatusid.getKEY_DESCRIPTION();
                    break;

                case R.id.manufacturedYear_spinner:
                    CommonPojo year = (CommonPojo) S_Year.getSelectedItem(); // Object which was selected.
                    yearCode = year.getKEY_CODE();
                    yearDescription = year.getKEY_DESCRIPTION();
                    if (!yearCode.equalsIgnoreCase("0")) {
                        getMake(yearCode);
                    } else {
                        make.add(clearAdapter(make, S_Make));
                        variant.add(clearAdapter(variant, S_Variant));
                        model.add(clearAdapter(model, S_Model));
                        setSpinnerValue(make, S_Make, Makeadapter);
                        setSpinnerValue(model, S_Model, Modeladapter);
                        setSpinnerValue(variant, S_Variant, Variantadapter);
                    }
                    break;

                case R.id.make_spinner:
                    CommonPojo make = (CommonPojo) S_Make.getSelectedItem(); // Object which was selected.
                    makeCode = make.getKEY_CODE();
                    if (!makeCode.equalsIgnoreCase("0")) {
                        getModel(yearCode, makeCode);
                    } else {
                        variant.add(clearAdapter(variant, S_Variant));
                        model.add(clearAdapter(model, S_Model));
                        setSpinnerValue(model, S_Model, Modeladapter);
                        setSpinnerValue(variant, S_Variant, Variantadapter);
                    }
                    break;

                case R.id.model_spinner:
                    CommonPojo model = (CommonPojo) S_Model.getSelectedItem(); // Object which was selected.
                    modelCode = model.getKEY_CODE();
                    if (!modelCode.equalsIgnoreCase("0")) {
                        getVaraint(yearCode, makeCode, modelCode);
                    } else {
                        variant.add(clearAdapter(variant, S_Variant));
                        setSpinnerValue(variant, S_Variant, Variantadapter);
                    }
                    break;

                case R.id.variant_spinner:
                    CommonPojo veriantid = (CommonPojo) S_Variant.getSelectedItem();
                    veriantCode = veriantid.getKEY_CODE();
                    break;

                case R.id.repaymentMode_spinner:
                    CommonPojo paymentmodeid = (CommonPojo) S_Repayment.getSelectedItem();
                    repaymentCode = paymentmodeid.getKEY_CODE();
                    repaymentDescription = paymentmodeid.getKEY_DESCRIPTION();
                    break;


                default:
                    break;
            }
        } catch (ClassCastException ce) {
            ce.printStackTrace();
        }
    }

    @OnCheckedChanged(R.id.cusVehicaleFinance_switch)
    public void switchCheckChanged(CompoundButton v, boolean on) {
        if (on) {
            vehicleCode = YTxt;
            vehicleRegNo_editText.setEnabled(true);
            vehicleRegNo_editText.setText(registrationNo);
            if (vehicleRegNo_editText.getText().toString().isEmpty())
                vehicleRegNo_editText.setError(regNoEmptyErrorMsg);
            else
                vehicleRegNo_editText.addTextChangedListener(new CustomTextWatcher(vehicleRegNo_editText));
        } else {
            vehicleRegNo_editText.setEnabled(false);
            vehicleRegNo_editText.setError(null);
            vehicleRegNo_editText.setText("");
            vehicleCode = NTxt;
        }
    }

    @OnTextChanged(value = R.id.cusPincode_editText, callback = OnTextChanged.Callback.TEXT_CHANGED)
    public void onTextChanged(CharSequence s) {
        try {
            pincodeLen = s.length();
            if (s.length() != 0 && s.length() == 6 && cusPincode_editText.hasFocus() == true) {
                getPinCode(cusPincode_editText.getText().toString());
                cusPincode_editText.setFocusable(true);
                cusPincode_editText.setFocusableInTouchMode(true);
            } else if (s.length() == 0) {
                getDropdownFields();
                S_State.setSelection(0);
                S_City.setSelection(0);
                S_Taluk.setSelection(0);
            }
        } catch (IndexOutOfBoundsException ie) {
            ie.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.btnSave, R.id.btnNext})
    public void onClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.btnSave:

                    editTextEmpty = validateEditText(new EditText[]{cusName_editText, cusNo_editText, cusAdd1_editText,
                            cusPincode_editText, loanAmt_editText, loanTenor_editText});

                    spinnerEmpty = validateSpinner(new AppCompatSpinner[]{S_State, S_City, S_Taluk, S_Residence, S_Repayment, S_Customer});

                    checkBoxEmpty = (pancard_checkBox.isChecked() || passport_checkBox.isChecked() || voterid_checkBox.isChecked() || drivinglic_checkBox.isChecked()
                            || rationcard_checkBox.isChecked() || aadharcard_checkBox.isChecked());

                    if (!validateEditText(new EditText[]{cusName_editText}))
                        showSnackbar(customerDetail_mainLayout, customerName);

                    else if (!validateSpinner(new AppCompatSpinner[]{S_Customer}))
                        showSnackbar(customerDetail_mainLayout, selectCustomerProfileTxt);

                    else if (!validateEditText(new EditText[]{cusNo_editText}))
                        showSnackbar(customerDetail_mainLayout, customerNo);

                    else if (!validateEditText(new EditText[]{cusAdd1_editText}))
                        showSnackbar(customerDetail_mainLayout, customerAddress1);

                    else if (!validateEditText(new EditText[]{cusPincode_editText}))
                        showSnackbar(customerDetail_mainLayout, customerPincode);

                    else if (!validateEditText(new EditText[]{loanAmt_editText}))
                        showSnackbar(customerDetail_mainLayout, customerLoanAmt);

                    else if (!validateEditText(new EditText[]{loanTenor_editText}))
                        showSnackbar(customerDetail_mainLayout, customerLoanTenure);

                    else if (!validateSpinner(new AppCompatSpinner[]{S_State}))
                        showSnackbar(customerDetail_mainLayout, selectStateTxt);

                    else if (!validateSpinner(new AppCompatSpinner[]{S_City}))
                        showSnackbar(customerDetail_mainLayout, selectCityTxt);

                    else if (!validateSpinner(new AppCompatSpinner[]{S_Taluk}))
                        showSnackbar(customerDetail_mainLayout, selectTalukTxt);

                    else if (!validateSpinner(new AppCompatSpinner[]{S_Residence}))
                        showSnackbar(customerDetail_mainLayout, selectResStatusTxt);

                    else if (!validateSpinner(new AppCompatSpinner[]{S_Repayment}))
                        showSnackbar(customerDetail_mainLayout, selectRepayModeTxt);

                    /*else if(!validateSpinner(new AppCompatSpinner[]{S_Year}))
                        showSnackbar(customerDetail_mainLayout, selectManufYearTxt);

                    else if(!validateSpinner(new AppCompatSpinner[]{S_Make}))
                        showSnackbar(customerDetail_mainLayout, selectMakeTxt);

                    else if(!validateSpinner(new AppCompatSpinner[]{S_Model}))
                        showSnackbar(customerDetail_mainLayout, selectModelTxt);

                    else if(!validateSpinner(new AppCompatSpinner[]{S_Variant}))
                        showSnackbar(customerDetail_mainLayout, selectVariantTxt);*/

                    else if (!checkBoxEmpty)
                        showSnackbar(customerDetail_mainLayout, customerIDproof);

                    else if (S_Vehicle.isChecked() && !isValidLicense(vehicleRegNo_editText.getText().toString())) {
                        vehicleRegNo_editText.setEnabled(true);
                        showSnackbar(customerDetail_mainLayout, regNoInvalidErrorMsg);
                    } else {
                        btnSave.setEnabled(false);
                        btnSave.setClickable(false);
                        leadUpdate();
                    }

                  /*  if (editTextEmpty && spinnerEmpty && checkBoxEmpty) {
                        leadUpdate();
                    }else{
                        showSnackbar(customerDetail_mainLayout, fillDataMsg);
                    }*/
                    break;
                case R.id.btnNext:
                    ((LeadUpdateActivity) getActivity()).setCurrentViewPager(1);
                    break;
            }
        } catch (NullPointerException n) {
            n.printStackTrace();
        }
    }

    private void leadUpdate() {
        try {
            firstName = cusName_editText.getText().toString().trim();
            phoneNo1 = cusNo_editText.getText().toString().trim();
            phoneNo2 = cusAlterNo_editText.getText().toString().trim();
            address1 = cusAdd1_editText.getText().toString().trim();
            address2 = cusAdd2_editText.getText().toString().trim();
            pinCode = cusPincode_editText.getText().toString().trim();
            registrationNo = vehicleRegNo_editText.getText().toString().trim();
            loanAmount = loanAmt_editText.getText().toString().trim();
            tenure = loanTenor_editText.getText().toString().trim();
            emi = emiComfort_editText.getText().toString().trim();
            veriantCode = S_Variant.getSelectedItem().toString();
            if (veriantCode.equalsIgnoreCase("SELECT VARIANT")) {
                veriantCode = "0";
            } else {
                veriantCode = S_Variant.getSelectedItem().toString();
            }
            if (isNetworkAvailable(mContext)) {

                try {
                    viewDialog.showProgressDialog();
                    leadUpdatePojo = new LeadUpdatePojo();
                    leadUpdatePojo.setLead_id(customerJsonObject.getString(respLeadId_));
                    leadUpdatePojo.setFirst_name(firstName);
                    leadUpdatePojo.setPhone_mobile1(phoneNo1);
                    leadUpdatePojo.setPhone_mobile2(phoneNo2);
                    leadUpdatePojo.setAddress1(address1);
                    leadUpdatePojo.setAddress2(address2);
                    leadUpdatePojo.setState(stateCode);
                    leadUpdatePojo.setCity(cityCode);
                    leadUpdatePojo.setTaluk(talukCode);
                    leadUpdatePojo.setPincode(pinCode);
                    leadUpdatePojo.setLoan_amnt(loanAmount);
                    leadUpdatePojo.setTenure(tenure);
                    leadUpdatePojo.setEmi_comfort("");
                    leadUpdatePojo.setModified_by(preference.getPref(USERID, ""));
                    leadUpdatePojo.setLast_name("");
                    leadUpdatePojo.setResident_code(residenceCode);
                    leadUpdatePojo.setManuf_year(yearCode);
                    leadUpdatePojo.setMake(makeCode);
                    leadUpdatePojo.setModels(modelCode);
                    leadUpdatePojo.setVariant(veriantCode);
                    leadUpdatePojo.setRepay_code(repaymentCode);
                    leadUpdatePojo.setStatus(customerJsonObject.getString(respstatus));
                    leadUpdatePojo.setCreated_by(customerJsonObject.getString(respCreatedBy_));
                    leadUpdatePojo.setVEHICLE_FINALISE(vehicleCode);
                    leadUpdatePojo.setCUS_PROF_CODE(customerCode);
                    leadUpdatePojo.setVEHICLE_REGNO(registrationNo);
                    leadUpdatePojo.setSCE_EMP_NO(customerJsonObject.getString(respSceEmpNum_));
                    leadUpdatePojo.setPROSPECT_NO(customerJsonObject.getString(respProspectNum_));
                    leadUpdatePojo.setAGREEMENT_NO(customerJsonObject.getString(respAgreementNum_));
                    if (pancard_checkBox.isChecked() == true) {
                        if (customerJsonObject.getString(resppanCard_).equalsIgnoreCase("") || customerJsonObject.getString(resppanCard_).equalsIgnoreCase(NTxt)) {
                            leadUpdatePojo.setPan_card(YTxt);
                        } else {
                            leadUpdatePojo.setPan_card(customerJsonObject.getString(resppanCard_));
                        }
                    } else if (pancard_checkBox.isChecked() == false) {
                        leadUpdatePojo.setPan_card(NTxt);
                    }

                    if (passport_checkBox.isChecked() == true) {
                        if (customerJsonObject.getString(respPassport).equalsIgnoreCase("") || customerJsonObject.getString(respPassport).equalsIgnoreCase(NTxt)) {
                            leadUpdatePojo.setPASSPORT(YTxt);
                        } else {
                            leadUpdatePojo.setPASSPORT(customerJsonObject.getString(respPassport));
                        }
                    } else if (passport_checkBox.isChecked() == false) {
                        leadUpdatePojo.setPASSPORT(NTxt);
                    }

                    if (voterid_checkBox.isChecked() == true) {
                        if (customerJsonObject.getString(respVoterId_).equalsIgnoreCase("") || customerJsonObject.getString(respVoterId_).equalsIgnoreCase(NTxt)) {
                            leadUpdatePojo.setVOTER_ID(YTxt);
                        } else {
                            leadUpdatePojo.setVOTER_ID(customerJsonObject.getString(respVoterId_));
                        }
                    } else if (voterid_checkBox.isChecked() == false) {
                        leadUpdatePojo.setVOTER_ID(NTxt);
                    }

                    if (drivinglic_checkBox.isChecked() == true) {
                        if (customerJsonObject.getString(respDrivinglicense_).equalsIgnoreCase("") || customerJsonObject.getString(respDrivinglicense_).equalsIgnoreCase(NTxt)) {
                            leadUpdatePojo.setDRIVING_LICENSE(YTxt);
                        } else {
                            leadUpdatePojo.setDRIVING_LICENSE(customerJsonObject.getString(respDrivinglicense_));
                        }
                    } else if (drivinglic_checkBox.isChecked() == false) {
                        leadUpdatePojo.setDRIVING_LICENSE(NTxt);
                    }

                    if (rationcard_checkBox.isChecked() == true) {
                        if (customerJsonObject.getString(respRationCard_).equalsIgnoreCase("") || customerJsonObject.getString(respRationCard_).equalsIgnoreCase(NTxt)) {
                            leadUpdatePojo.setRATION_CARD(YTxt);
                        } else {
                            leadUpdatePojo.setRATION_CARD(customerJsonObject.getString(respRationCard_));
                        }
                    } else if (rationcard_checkBox.isChecked() == false) {
                        leadUpdatePojo.setRATION_CARD(NTxt);
                    }
                    if (aadharcard_checkBox.isChecked() == true) {
                        if (customerJsonObject.getString(respAdharCard_).equalsIgnoreCase("") || customerJsonObject.getString(respAdharCard_).equalsIgnoreCase(NTxt)) {
                            leadUpdatePojo.setADHAR_CARD(YTxt);
                        } else {
                            leadUpdatePojo.setADHAR_CARD(customerJsonObject.getString(respAdharCard_));
                        }
                    } else if (aadharcard_checkBox.isChecked() == false) {
                        leadUpdatePojo.setADHAR_CARD(NTxt);
                    }
                    Call<ResponseBody> responseBodyCall = mAPICallInterface.leadUpdateInsert(leadUpdatePojo);
                    responseBodyCall.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            viewDialog.hideProgressDialog();
                            if (response != null) {
                                if (response.code() == 200) {
                                    try {
                                        String responseValue = response.body().string();
                                        JSONObject jsonObject = new JSONObject(responseValue);

                                        if (jsonObject != null) {
                                            JSONArray jsonArray = new JSONArray(jsonObject.getString(respleadUpdateResult));
                                            if (jsonArray.length() > 0) {
                                                String message = jsonArray.getJSONObject(0).getString("d_status1");
                                                //ShowSuccessAlertDialog(getActivity(), informationTxt, leadUpdateInsertMsg);
                                                new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE)
                                                        .setTitleText(getLeadId)
                                                        .setContentText(leadUpdateInsertMsg)
                                                        .setConfirmText("Ok")
                                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                            @Override
                                                            public void onClick(SweetAlertDialog sDialog) {
                                                                sDialog.dismissWithAnimation();
                                                                btnSave.setEnabled(true);
                                                                btnSave.setClickable(true);
                                                                /*startActivity(new Intent(mContext, PickLeadsActivity.class));
                                                                getActivity().finish();*/
                                                            }
                                                        })
                                                        .show();
                                            }
                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        viewDialog.hideProgressDialog();
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
                    e.printStackTrace();
                    viewDialog.hideProgressDialog();
                }

            } else {
                showErrorDialog(mContext, internetErrorTitle, internetError);
            }
        } catch (Exception e) {
            e.printStackTrace();
            viewDialog.hideProgressDialog();
        }
    }

    private void getLeadUpdate(String leadId) {
        if (isNetworkAvailable(mContext)) {
            try {
                viewDialog.showProgressDialog();
                customerPojo = new CommonPojo();
                customerPojo.setUser_id(preference.getPref(USERID, ""));
                customerPojo.setLead_id(leadId);
                customerPojo.setMobile("");
                Call<ResponseBody> responseBodyCall = mAPICallInterface.getleadsReceived(customerPojo);
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
                                            customerJsonObject = new JSONObject(preference.getPref(sharedPrefclickUpdate, jsonObject.toString()));

                                            if (jsonObject.length() > 0) {
                                                getActivity().setTitle(cusDetailsTxt);

                                                setProductImage(jsonObject.getString(respproductCode_).toUpperCase());
                                                leadNo_textView.setText(jsonObject.getString(respLeadId_));
                                                cusName_editText.setText(jsonObject.getString(respFirstName_));
                                                cusNo_editText.setText(jsonObject.getString(respPhoneNum_));
                                                cusAlterNo_editText.setText(jsonObject.getString(respPhoneNum2_));

                                                if (jsonObject.getString(respVehiclefinalise_).contains(NTxt) || jsonObject.getString(respVehiclefinalise_).equalsIgnoreCase("")) {
                                                    S_Vehicle.setChecked(false);
                                                    vehicleRegNo_editText.setEnabled(false);
                                                    vehicleRegNo_editText.setError(null);
                                                } else {
                                                    S_Vehicle.setChecked(true);
                                                    vehicleRegNo_editText.setError(null);
                                                    vehicleRegNo_editText.setEnabled(true);
                                                }
                                                if (jsonObject.getString(resppanCard_).equalsIgnoreCase(NTxt) || jsonObject.getString(resppanCard_).equalsIgnoreCase("")) {
                                                    pancard_checkBox.setChecked(false);
                                                } else {
                                                    pancard_checkBox.setChecked(true);
                                                }

                                                if (jsonObject.getString(respPassport).equalsIgnoreCase(NTxt) || jsonObject.getString(respPassport).equalsIgnoreCase("")) {
                                                    passport_checkBox.setChecked(false);
                                                } else {
                                                    passport_checkBox.setChecked(true);
                                                }

                                                if (jsonObject.getString(respVoterId_).equalsIgnoreCase(NTxt) || jsonObject.getString(respVoterId_).equalsIgnoreCase("")) {
                                                    voterid_checkBox.setChecked(false);
                                                } else {
                                                    voterid_checkBox.setChecked(true);
                                                }

                                                if (jsonObject.getString(respDrivinglicense_).equalsIgnoreCase(NTxt) || jsonObject.getString(respDrivinglicense_).equalsIgnoreCase("")) {
                                                    drivinglic_checkBox.setChecked(false);
                                                } else {
                                                    drivinglic_checkBox.setChecked(true);
                                                }

                                                if (jsonObject.getString(respRationCard_).equalsIgnoreCase(NTxt) || jsonObject.getString(respRationCard_).equalsIgnoreCase("")) {
                                                    rationcard_checkBox.setChecked(false);
                                                } else {
                                                    rationcard_checkBox.setChecked(true);
                                                }

                                                if (jsonObject.getString(respAdharCard_).equalsIgnoreCase(NTxt) || jsonObject.getString(respAdharCard_).equalsIgnoreCase("")) {
                                                    aadharcard_checkBox.setChecked(false);
                                                } else {
                                                    aadharcard_checkBox.setChecked(true);
                                                }

                                                registrationNo = jsonObject.getString(respVehicleRegno_);
                                                cusAdd1_editText.setText(jsonObject.getString(respAddress1_));
                                                cusAdd2_editText.setText(jsonObject.getString(respAddress2_));
                                                cusPincode_editText.setText(jsonObject.getString(resPincode_));
                                                vehicleRegNo_editText.setText(jsonObject.getString(respVehicleRegno_));
                                                loanAmt_editText.setText(jsonObject.getString(respLoanAmt_));
                                                loanTenor_editText.setText(jsonObject.getString(respTenure_));
                                                emiComfort_editText.setText(jsonObject.getString(respEmicomfort_));
                                            }
                                        } else {
                                            ShowAlertDialog(mContext, message, noRecordsMsg);
                                        }
                                        //Get Dropdown Fields
                                        getDropdownFields();
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

    private void setProductImage(String productCode) {
        switch (productCode) {
            case Constants.AL:
                productImageView.setImageResource(R.drawable.ic_trans_auto);
                break;
            case AP:
                productImageView.setImageResource(R.drawable.ic_trans_usedtractor);
                break;
            case Constants.CA:
                productImageView.setImageResource(R.drawable.ic_trans_car);
                break;
            case Constants.CD:
                productImageView.setImageResource(R.drawable.ic_trans_commercial);
                break;
            case Constants.DC:
                productImageView.setImageResource(R.drawable.ic_trans_car);
                break;
            case Constants.LN:
                productImageView.setImageResource(R.drawable.ic_trans_commercial);
                break;
            case Constants.TR:
                productImageView.setImageResource(R.drawable.ic_trans_tractor);
                break;
            case Constants.TW:
                productImageView.setImageResource(R.drawable.ic_trans_usedtractor);
                break;
            case Constants.UT:
                productImageView.setImageResource(R.drawable.ic_trans_bike);
                break;
            case Constants.UV:
                productImageView.setImageResource(R.drawable.ic_trans_commercial);
                break;
            default:
                productImageView.setImageResource(R.drawable.ic_trans_bike);
                break;
        }
    }

    private void getDropdownFields() {
        if (isNetworkAvailable(mContext)) {
            try {
                customerPojo = new CommonPojo();
                Call<ResponseBody> responseBodyCall = mAPICallInterface.getDropdownFields();
                responseBodyCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response != null) {
                            if (response.code() == 200) {
                                try {
                                    year = new ArrayList<CommonPojo>();
                                    customer = new ArrayList<CommonPojo>();
                                    residencestatus = new ArrayList<CommonPojo>();
                                    repayment = new ArrayList<CommonPojo>();
                                    state = new ArrayList<CommonPojo>();
                                    city = new ArrayList<CommonPojo>();
                                    make = new ArrayList<CommonPojo>();
                                    model = new ArrayList<CommonPojo>();
                                    variant = new ArrayList<CommonPojo>();
                                    vehicle = new ArrayList<CommonPojo>();
                                    taluk = new ArrayList<CommonPojo>();

                                    String responseValue = response.body().string();
                                    JSONArray jsonArray = new JSONArray(responseValue);
                                    try {
                                        if (jsonArray.length() > 0) {
                                            setFirstArraylist("0", selectCustomerProfileTxt, customer);
                                            setFirstArraylist("0", selectStateTxt, state);
                                            setFirstArraylist("0", selectResStatusTxt, residencestatus);
                                            setFirstArraylist("0", selectManufYearTxt, year);
                                            setFirstArraylist("0", selectRepayModeTxt, repayment);
                                            setFirstArraylist("0", selectVehicleFinaliseTxt, vehicle);
                                            setFirstArraylist(YTxt, YesTxt, vehicle);
                                            setFirstArraylist(NTxt, NoTxt, vehicle);
                                            setFirstArraylist("0", selectCityTxt, city);
                                            setFirstArraylist("0", selectMakeTxt, make);
                                            setFirstArraylist("0", selectModelTxt, model);
                                            setFirstArraylist("0", selectVariantTxt, variant);
                                            setFirstArraylist("0", selectTalukTxt, taluk);
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                if (jsonArray.getJSONObject(i).getString(respKeycode).equalsIgnoreCase(respYear)) {
                                                    setFirstArraylist(jsonArray.getJSONObject(i).getString(respCode), jsonArray.getJSONObject(i).getString(respCodeDesc), year);
                                                } else if (jsonArray.getJSONObject(i).getString(respKeycode).equalsIgnoreCase(respCustomerProfile)) {
                                                    setFirstArraylist(jsonArray.getJSONObject(i).getString(respCode), jsonArray.getJSONObject(i).getString(respCodeDesc), customer);
                                                } else if (jsonArray.getJSONObject(i).getString(respKeycode).equalsIgnoreCase(respResidenceStatus)) {
                                                    setFirstArraylist(jsonArray.getJSONObject(i).getString(respCode), jsonArray.getJSONObject(i).getString(respCodeDesc), residencestatus);
                                                } else if (jsonArray.getJSONObject(i).getString(respKeycode).equalsIgnoreCase(respRepayment)) {
                                                    setFirstArraylist(jsonArray.getJSONObject(i).getString(respCode), jsonArray.getJSONObject(i).getString(respCodeDesc), repayment);
                                                } else if (jsonArray.getJSONObject(i).getString(respKeycode).equalsIgnoreCase(respState)) {
                                                    setFirstArraylist(jsonArray.getJSONObject(i).getString(respCode), jsonArray.getJSONObject(i).getString(respCodeDesc), state);
                                                }
                                            }

                                            setSpinnerValue(year, S_Year, Yearadapter);
                                            setSpinnerValue(customer, S_Customer, Customeradapter);
                                            setSpinnerValue(residencestatus, S_Residence, Residenceadapter);
                                            setSpinnerValue(repayment, S_Repayment, Repaymentadapter);
                                            setSpinnerValue(state, S_State, Stateadapte);
                                            setSpinnerValue(city, S_City, Cityadapter);
                                            setSpinnerValue(make, S_Make, Makeadapter);
                                            setSpinnerValue(model, S_Model, Modeladapter);
                                            setSpinnerValue(variant, S_Variant, Variantadapter);
                                            //setSpinnerValue(vehicle, S_Vehicle, Vehicleadapter);
                                            setSpinnerValue(taluk, S_Taluk, TalukTypeadapter);
                                            if (customerJsonObject.getString(respStateCode_).length() > 0 && (pincodeLen > 0 && cusPincode_editText.getText().toString().length() > 0)) {
                                                S_State.setSelection(getItemFromCode(customerJsonObject.getString(respStateCode_), state));
                                            } else {
                                                S_State.setSelection(getItemFromCode("0", state));
                                            }
                                            if (customerJsonObject.getString(respResidentCode_).length() > 0) {
                                                S_Residence.setSelection(getItemFromCode(customerJsonObject.getString(respResidentCode_), residencestatus));
                                            } else {
                                                S_Residence.setSelection(getItemFromCode("0", residencestatus));
                                            }
                                            if (customerJsonObject.getString(respManufYear_).length() > 0) {
                                                S_Year.setSelection(getItemFromCode(customerJsonObject.getString(respManufYear_), year));
                                            } else {
                                                S_Year.setSelection(getItemFromCode("0", year));
                                            }
                                            /*if (customerJsonObject.getString(respVehiclefinalise_).length() > 0) {
                                                S_Vehicle.setSelection(getItemFromCode(customerJsonObject.getString(respVehiclefinalise_).trim(), vehicle));
                                            } else {
                                                S_Vehicle.setSelection(getItemFromCode("0", vehicle));
                                            }*/
                                            if (customerJsonObject.getString(respCusprofCode_).length() > 0) {
                                                S_Customer.setSelection(getItemFromCode(customerJsonObject.getString(respCusprofCode_), customer));
                                            } else {
                                                S_Customer.setSelection(getItemFromCode("0", customer));
                                            }
                                            if (customerJsonObject.getString(respRepayCode_).length() > 0) {
                                                S_Repayment.setSelection(getItemFromCode(customerJsonObject.getString(respRepayCode_), repayment));
                                            } else {
                                                S_Repayment.setSelection(getItemFromCode("0", repayment));
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
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
                    }
                });

            } catch (Exception e) {

                e.printStackTrace();
            }
        } else {
            showErrorDialog(mContext, internetErrorTitle, internetError);
        }
    }

    private void getCity(String stateCode) {
        try {
            if (isNetworkAvailable(mContext)) {
                city.add(clearAdapter(city, S_City));
                taluk.add(clearAdapter(taluk, S_Taluk));
                customerPojo = new CommonPojo();
                customerPojo.setStatecode(stateCode);
                Call<ResponseBody> responseBodyCall = mAPICallInterface.getCity(customerPojo);
                responseBodyCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response != null) {
                            if (response.code() == 200) {
                                try {
                                    String responseValue = response.body().string();
                                    JSONArray jsonArray = new JSONArray(responseValue);
                                    if (jsonArray.length() > 0) {
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            setFirstArraylist(jsonArray.getJSONObject(i).getString(respCityCode), jsonArray.getJSONObject(i).getString(respCityeDesc), city);
                                        }
                                        setSpinnerValue(city, S_City, Cityadapter);
                                        if (cityInc == 0) {
                                            if (customerJsonObject.getString(respCityCode_).length() > 0) {
                                                S_City.setSelection(getItemFromCode(customerJsonObject.getString(respCityCode_), city));
                                            } else {
                                                S_City.setSelection(getItemFromCode("0", city));
                                            }
                                            cityInc++;
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getTaluk(String cityCode) {
        if (isNetworkAvailable(mContext)) {
            try {
                taluk.add(clearAdapter(taluk, S_Taluk));
                customerPojo = new CommonPojo();
                customerPojo.setCityCode(cityCode);
                Call<ResponseBody> responseBodyCall = mAPICallInterface.getTown(customerPojo);
                responseBodyCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response != null) {
                            if (response.code() == 200) {
                                try {

                                    String responseValue = response.body().string();
                                    JSONArray jsonArray = new JSONArray(responseValue);
                                    if (jsonArray.length() > 0) {
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            setFirstArraylist(jsonArray.getJSONObject(i).getString(respTalukCode), jsonArray.getJSONObject(i).getString(respTalukDesc), taluk);
                                        }
                                        setSpinnerValue(taluk, S_Taluk, TalukTypeadapter);
                                    }
                                    if (talukInc == 0) {
                                        if (customerJsonObject.getString(respTaluk).length() > 0) {
                                            S_Taluk.setSelection(getItemFromCode(customerJsonObject.getString(respTaluk), taluk));
                                        } else {
                                            S_Taluk.setSelection(getItemFromCode("0", taluk));
                                        }
                                        talukInc++;
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
    }

    private void getTownPinCode(String towncode) {
        try {
            if (isNetworkAvailable(mContext)) {
                try {
                    customerPojo = new CommonPojo();
                    customerPojo.setTowncode(towncode);
                    Call<ResponseBody> responseBodyCall = mAPICallInterface.getTownPinCode(customerPojo);
                    responseBodyCall.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            //myDialogTown.dismiss();
                            if (response != null) {
                                if (response.code() == 200) {
                                    try {
                                        String responseValue = response.body().string();
                                        JSONObject jsonObject = new JSONObject(responseValue);
                                        if (jsonObject != null) {
                                            JSONArray jsonArray = new JSONArray(jsonObject.getString(respTownPincodeResult));
                                            if (jsonArray.length() > 0) {
                                                String Pincode = jsonArray.getJSONObject(0).getString(resTownPincode);
                                                cusPincode_editText.setFocusable(false);
                                                cusPincode_editText.setFocusableInTouchMode(false);
                                                cusPincode_editText.setText(Pincode);
                                                cusPincode_editText.setFocusable(true);
                                                cusPincode_editText.setFocusableInTouchMode(true);
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
                            /*if(myDialogTown.isShowing()){
                                myDialogTown.dismiss();
                            }*/
                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                showErrorDialog(mContext, internetErrorTitle, internetError);
            }
        } catch (IndexOutOfBoundsException ie) {
            ie.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getPinCode(String pinCode) {
        try {
            if (isNetworkAvailable(mContext)) {
                try {
                    customerPojo = new CommonPojo();
                    customerPojo.setPincode(pinCode);
                    Call<ResponseBody> responseBodyCall = mAPICallInterface.getPinCodeMapped(customerPojo);
                    responseBodyCall.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response != null) {
                                if (response.code() == 200) {
                                    try {
                                        String responseValue = response.body().string();
                                        JSONObject jsonObject = new JSONObject(responseValue);
                                        if (jsonObject != null) {
                                            JSONArray jsonArray = new JSONArray(jsonObject.getString(respPincodeResult));

                                            if (jsonArray.length() > 0) {
                                                String state = jsonArray.getJSONObject(0).getString("State");
                                                String city = jsonArray.getJSONObject(0).getString("City");
                                                String town = jsonArray.getJSONObject(0).getString("Town");
                                                String StateCode = jsonArray.getJSONObject(0).getString("Statecode");
                                                String Citycode = jsonArray.getJSONObject(0).getString("Citycode");
                                                String Towncode = jsonArray.getJSONObject(0).getString("Towncode");

                                                stateCode = StateCode;
                                                cityCode = Citycode;
                                                talukCode = Towncode;

                                                setPincodeResult(state, S_State);
                                                setPincodeResult(city, S_City);
                                                setPincodeResult(town, S_Taluk);
                                            } else {
                                                ShowAlertDialog(mContext, message, pincodeInvalidError);
                                                cusPincode_editText.setText("");
                                                /*S_State.setSelection(0);
                                                S_City.setSelection(0);
                                                S_Taluk.setSelection(0);
                                                S_Channel.setSelection(0);*/
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
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                showErrorDialog(mContext, internetErrorTitle, internetError);
            }
        } catch (IndexOutOfBoundsException ie) {
            ie.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getMake(String yearCode) {

        if (isNetworkAvailable(mContext)) {
            try {
                make.add(clearAdapter(make, S_Make));
                variant.add(clearAdapter(variant, S_Variant));
                model.add(clearAdapter(model, S_Model));
                customerPojo = new CommonPojo();
                customerPojo.setYear(yearCode);
                Call<ResponseBody> responseBodyCall = mAPICallInterface.getMake(customerPojo);
                responseBodyCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response != null) {
                            if (response.code() == 200) {
                                try {
                                    String responseValue = response.body().string();
                                    JSONArray jsonArray = new JSONArray(responseValue);
                                    if (jsonArray.length() > 0) {
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            customerPojo = new CommonPojo();
                                            customerPojo.setKEY_CODE(jsonArray.getJSONObject(i).getString(respMake));
                                            customerPojo.setKEY_DESCRIPTION(jsonArray.getJSONObject(i).getString(respMake));
                                            make.add(customerPojo);
                                        }
                                        setSpinnerValue(make, S_Make, Makeadapter);
                                        if (makeInc == 0) {
                                            if (customerJsonObject.getString(respmake).length() > 0) {
                                                S_Make.setSelection(getItemFromCode(customerJsonObject.getString(respmake), make));
                                            } else {
                                                S_Make.setSelection(getItemFromCode("0", make));
                                            }
                                            makeInc++;
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
    }

    private void getVaraint(String yearCode, String makeCode, String modelCode) {
        try {
            if (isNetworkAvailable(mContext)) {
                variant.add(clearAdapter(variant, S_Variant));
                try {
                    customerPojo = new CommonPojo();
                    customerPojo.setYear(yearCode);
                    customerPojo.setModel(modelCode);
                    customerPojo.setMake(makeCode);
                    Call<ResponseBody> responseBodyCall = mAPICallInterface.getVariant(customerPojo);
                    responseBodyCall.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response != null) {
                                if (response.code() == 200) {
                                    try {
                                        String responseValue = response.body().string();
                                        JSONArray jsonArray = new JSONArray(responseValue);
                                        if (jsonArray.length() > 0) {
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                customerPojo = new CommonPojo();
                                                customerPojo.setKEY_CODE(jsonArray.getJSONObject(i).getString(respVariant));
                                                customerPojo.setKEY_DESCRIPTION(jsonArray.getJSONObject(i).getString(respVariant));
                                                variant.add(customerPojo);
                                            }
                                            setSpinnerValue(variant, S_Variant, Variantadapter);
                                            if (variantInc == 0) {
                                                if (customerJsonObject.getString(respvariant).length() > 0) {
                                                    S_Variant.setSelection(getItemFromCode(customerJsonObject.getString(respvariant), variant));
                                                } else {
                                                    S_Variant.setSelection(getItemFromCode("0", variant));
                                                }
                                                variantInc++;
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

    private void getModel(String yearCode, String makeCode) {
        if (isNetworkAvailable(mContext)) {
            try {
                model.add(clearAdapter(model, S_Model));
                variant.add(clearAdapter(variant, S_Variant));
                customerPojo = new CommonPojo();
                customerPojo.setYear(yearCode);
                customerPojo.setMake(makeCode);
                Call<ResponseBody> responseBodyCall = mAPICallInterface.getModel(customerPojo);
                responseBodyCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response != null) {
                            if (response.code() == 200) {
                                try {
                                    String responseValue = response.body().string();
                                    JSONArray jsonArray = new JSONArray(responseValue);

                                    if (jsonArray.length() > 0) {
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            customerPojo = new CommonPojo();
                                            customerPojo.setKEY_CODE(jsonArray.getJSONObject(i).getString(respModel));
                                            customerPojo.setKEY_DESCRIPTION(jsonArray.getJSONObject(i).getString(respModel));
                                            model.add(customerPojo);
                                        }
                                        setSpinnerValue(model, S_Model, Modeladapter);
                                        if (modelInc == 0) {
                                            if (customerJsonObject.getString(respmodel).length() > 0) {
                                                S_Model.setSelection(getItemFromCode(customerJsonObject.getString(respmodel), model));
                                            } else {
                                                S_Model.setSelection(getItemFromCode("0", model));
                                            }
                                            modelInc++;
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
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            showErrorDialog(mContext, internetErrorTitle, internetError);
        }

    }

    public void setPincodeResult(String result, Spinner spinner) {
        ArrayList<String> stateRes = new ArrayList<String>();
        stateRes.add(result);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.spinner_item, stateRes);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter);
    }

    public void setSpinnerValue(ArrayList<CommonPojo> list, Spinner spinner, ArrayAdapter<CommonPojo> adapter) {
        adapter = new ArrayAdapter<CommonPojo>(mContext, R.layout.spinner_item, list);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setSelection(0, false);
        spinner.setAdapter(adapter);
    }

    private void setFirstArraylist(String code, String Description, ArrayList<CommonPojo> arrayList) {
        customerPojo = new CommonPojo();
        customerPojo.setKEY_CODE(code);
        customerPojo.setKEY_DESCRIPTION(Description);
        arrayList.add(customerPojo);
    }

    public int getItemFromCode(String code, ArrayList<CommonPojo> arrayList) {
        int returnval = 0;
        try {
            for (int i = 0; i < arrayList.size(); i++) {
                returnval = i;
                CommonPojo temp = arrayList.get(i);
                if (temp.getKEY_CODE().equalsIgnoreCase(code)) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnval;
    }

    public CommonPojo clearAdapter(ArrayList<CommonPojo> adapter, Spinner spinner) {
        CommonPojo selectedItem = (CommonPojo) spinner.getItemAtPosition(0); // Object which was selected.
        String description = selectedItem.getKEY_DESCRIPTION();
        String code = selectedItem.getKEY_CODE();
        adapter.clear();
        customerPojo = new CommonPojo();
        customerPojo.setKEY_CODE(code);
        customerPojo.setKEY_DESCRIPTION(description);
        return customerPojo;
    }

    public interface ViewPageListner {
        void onRefresh();
    }

    private class CustomTextWatcher implements TextWatcher {
        private EditText mEditText;

        public CustomTextWatcher(EditText e) {
            mEditText = e;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public void afterTextChanged(Editable s) {
            if (s.length() != 0 && s.length() > 0) {
                isValidLicense(vehicleRegNo_editText.getText().toString());
            }
        }
    }

}
