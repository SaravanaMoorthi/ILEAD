package com.tvscs.ilead.activity;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tvscs.ilead.adapter.DealersAdapter;
import com.tvscs.ilead.adapter.ProductListAdapter;
import com.tvscs.ilead.helper.SharedPreference;
import com.tvscs.ilead.model.CommonPojo;
import com.tvscs.ilead.model.DealersPojo;
import com.tvscs.ilead.model.LoginResponse;
import com.tvscs.ilead.model.MainMenu;
import com.tvscs.ilead.model.ProductResponse;
import com.tvscs.ilead.model.QuickLeadResponse;
import com.tvscs.ilead.retrofit.APICallInterface;
import com.tvscs.ilead.retrofit.APIClient;
import com.tvscs.ilead.utils.AdapterCallback;
import com.tvscs.ilead.utils.ClearableEditText;
import com.tvscs.ilead.utils.Constants;
import com.tvscs.ilead.utils.LoaderDialog;
import com.tvscs.ilead.utils.MyBottomMenu;
import com.tvscs.ilead.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
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
import static com.tvscs.ilead.utils.Utils.isValidMobile;
import static com.tvscs.ilead.utils.Utils.showErrorDialog;
import static com.tvscs.ilead.utils.Utils.showSnackbar;
import static com.tvscs.ilead.utils.Utils.validateEditText;
import static com.tvscs.ilead.utils.Utils.validateSpinner;


public class QuickLeadActivity extends BaseActivity implements AdapterCallback {

    static TextView channelTextView;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.img_toolbar)
    ImageView imgToolbar;
    @BindView(R.id.toolBarText)
    TextView toolBarText;
    @BindView(R.id.layout_toolBar)
    LinearLayout mlayout_toolBar;
    @BindView(R.id.notification_toolbar)
    LinearLayout notification_toolbar;
    @BindView(R.id.notifcation_imageView)
    ImageView notification_imageView;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.productRecylerView)
    RecyclerView productRecylerView;
    @BindView(R.id.quickLead_mainLayout)
    ConstraintLayout quickLead_mainLayout;
    @BindView(R.id.cutomername_editText)
    ClearableEditText cutomername_editText;
    @BindView(R.id.mobileno_editText)
    ClearableEditText mobileno_editText;
    @BindView(R.id.pincode_editText)
    ClearableEditText pincode_editText;
    @BindView(R.id.stateSpinner)
    AppCompatSpinner stateSpinner;
    @BindView(R.id.citySpinner)
    AppCompatSpinner citySpinner;
    @BindView(R.id.talukSpinner)
    AppCompatSpinner talukSpinner;
    @BindView(R.id.channelSpinner)
    AppCompatSpinner channelSpinner;
    @BindView(R.id.sourcingRadio_group)
    RadioGroup sourcingRadio_group;
    @BindView(R.id.directRbtn)
    RadioButton directRbtn;
    @BindView(R.id.channelRbtn)
    RadioButton channelRbtn;
    @BindView(R.id.btnSumbit)
    Button btnSumbit;
    @BindView(R.id.prevBtn)
    Button prevBtn;
    @BindView(R.id.nextBtn)
    Button nextBtn;
    @BindString(R.string.title_quicklead)
    String title_quicklead;
    @BindView(R.id.bottom_menu_quick)
    LinearLayout bottom_menu_quick;
    @BindView(R.id.bottom_menu_leadsgiven)
    LinearLayout bottom_menu_leadsgiven;
    @BindView(R.id.bottom_menu_leadsreceived)
    LinearLayout bottom_menu_leadsreceived;
    @BindView(R.id.bottom_menu_dashboard)
    LinearLayout bottom_menu_dashboard;
    @BindView(R.id.bottom_menu_logout)
    LinearLayout bottom_menu_logout;
    @BindView(R.id.img_bottom_menu_quick)
    ImageView img_bottom_menu_quick;
    @BindView(R.id.img_bottom_menu_leadsgiven)
    ImageView img_bottom_menu_leadsgiven;
    @BindView(R.id.img_bottom_menu_leadsreceived)
    ImageView img_bottom_menu_leadsreceived;
    @BindView(R.id.img_bottom_menu_dashboard)
    ImageView img_bottom_menu_dashboard;
    @BindView(R.id.img_bottom_menu_logout)
    ImageView img_bottom_menu_logout;
    int mFirst = 0, mLast = 0;
    String channelCode = "", directCode = "", channelDescription, stateCode, stateDescription, cityCode, cityDescription, talukCode, talukDescription,
            prodId, firstName, phoneNo1, pincode;
    boolean editTextEmpty, spinnerEmpty;
    Context mContext;
    FragmentManager fm;
    DealersFragment p;

    APICallInterface mAPICallInterface;
    SharedPreference preference;
    LoaderDialog viewDialog;

    ArrayList<String> productList;
    ProductListAdapter productAdapter;

    CommonPojo obj;
    LoginResponse loginResponse;
    ProductResponse productResponse;
    QuickLeadResponse leadResponse;

    ArrayList<CommonPojo> channelList = new ArrayList<CommonPojo>();
    ArrayList<CommonPojo> stateList = new ArrayList<CommonPojo>();
    ArrayList<CommonPojo> cityList = new ArrayList<CommonPojo>();
    ArrayList<CommonPojo> talukList = new ArrayList<CommonPojo>();
    ArrayAdapter<CommonPojo> channelAdapter, stateAdapter, cityAdapter, talukAdapter;
    private ArrayList<MainMenu> mainMenuArrayList;

    @Override
    public int activityView() {
        return R.layout.activity_quicklead;
    }

    @Override
    public void activityCreated() {
        ButterKnife.bind(this);
        mContext = QuickLeadActivity.this;
        productList = new ArrayList<String>();
        preference = new SharedPreference(mContext);
        mAPICallInterface = APIClient.getAPIService();
        viewDialog = new LoaderDialog(mContext);
        initViews();
    }

    private void initViews() {
        fm = getFragmentManager();
        p = new DealersFragment();
        channelTextView = (TextView) findViewById(R.id.channelTextView);
        cutomername_editText.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        loadBottomMenu();
        new MyBottomMenu(this, bottomMenuQuickValue);
        setToolBar();
        getProduct();
        getdropDownFields();
        setKeyBoardInactive();

        pincode_editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    clearEditTextFocus();
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        productRecylerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager llm = (LinearLayoutManager) productRecylerView.getLayoutManager();
                mLast = llm.findLastCompletelyVisibleItemPosition();
                mFirst = llm.findFirstCompletelyVisibleItemPosition();

                if (mFirst == 0) {
                    nextBtn.setBackgroundResource(R.mipmap.next);
                    prevBtn.setBackgroundResource(R.mipmap.defaultprevious);
                    nextBtn.setVisibility(View.VISIBLE);
                    prevBtn.setVisibility(View.GONE);
                } else {
                    nextBtn.setBackgroundResource(R.mipmap.defaultnext);
                    prevBtn.setBackgroundResource(R.mipmap.previous);
                    nextBtn.setVisibility(View.GONE);
                    prevBtn.setVisibility(View.VISIBLE);
                }
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayoutManager llm = (LinearLayoutManager) productRecylerView.getLayoutManager();
                llm.scrollToPositionWithOffset(mLast + 1, productList.size());
                if (llm.findLastCompletelyVisibleItemPosition() == (productList.size() - 1)) {
                    nextBtn.setBackgroundResource(R.mipmap.defaultnext);
                    prevBtn.setBackgroundResource(R.mipmap.previous);
                    nextBtn.setVisibility(View.GONE);
                    prevBtn.setVisibility(View.VISIBLE);
                } else {
                    nextBtn.setBackgroundResource(R.mipmap.next);
                    prevBtn.setBackgroundResource(R.mipmap.defaultprevious);
                    nextBtn.setVisibility(View.VISIBLE);
                    prevBtn.setVisibility(View.GONE);
                }
            }
        });

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayoutManager llm = (LinearLayoutManager) productRecylerView.getLayoutManager();
                llm.scrollToPositionWithOffset(mFirst - 1, productList.size());
                productRecylerView.scrollToPosition(0);
                if (llm.findFirstCompletelyVisibleItemPosition() == 0) {
                    nextBtn.setBackgroundResource(R.mipmap.next);
                    prevBtn.setBackgroundResource(R.mipmap.defaultprevious);
                    nextBtn.setVisibility(View.VISIBLE);
                    prevBtn.setVisibility(View.GONE);
                } else {
                    nextBtn.setBackgroundResource(R.mipmap.defaultnext);
                    prevBtn.setBackgroundResource(R.mipmap.previous);
                    nextBtn.setVisibility(View.GONE);
                    prevBtn.setVisibility(View.VISIBLE);
                }
            }
        });

        channelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (channelRbtn.isChecked()) {
                        channelTextView.setHint(searchChannel);
                        channelTextView.setText("");
                        getChannel();
                    } else if (directRbtn.isChecked() && directCode.equalsIgnoreCase(referalAgentCode)) {
                        Bundle data = new Bundle();
                        data.putString(resPincode_, pincode_editText.getText().toString());
                        data.putString(respProdCode, prodId);
                        data.putString(sourceOption, direct);
                        p.setArguments(data);
                        p.show(fm, selectChannelTxt);
                    } else {
                        showSnackbar(quickLead_mainLayout, selectSourcingTxt);
                    }
                } catch (Exception se) {
                    se.printStackTrace();
                }
            }
        });
        directRbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    channelSpinner.setVisibility(View.VISIBLE);
                    channelSpinner.setSelection(0);
                    channelTextView.setText(getString(R.string.tvscreditservice));
                    channelTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    channelTextView.setEnabled(false);
                    channelTextView.setClickable(false);
                    getDirect(respDIRECT);
                } catch (IndexOutOfBoundsException io) {
                    io.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        channelRbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    channelSpinner.setVisibility(View.GONE);
                    channelTextView.setEnabled(true);
                    channelTextView.setClickable(true);
                    channelTextView.setHint(searchChannel);
                    channelTextView.setText("");
                    channelTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, AppCompatResources.getDrawable(mContext, R.drawable.ic_search_icon), null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void clearEditTextFocus() {
        cutomername_editText.clearFocus();
        mobileno_editText.clearFocus();
        pincode_editText.clearFocus();
    }

    private void setKeyBoardInactive() {
        AppCompatSpinner[] spinners = {stateSpinner, citySpinner, talukSpinner, channelSpinner};
        for (Spinner mySpinner : spinners) {
            mySpinner.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    try {
                        InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Activity.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return false;
                }
            });
        }
    }

    private void setToolBar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Utils.setSvgImageviewDrawable(imgToolbar, mContext, R.drawable.ic_back_arrow);
        notification_imageView.setBackgroundResource(R.drawable.notification);
        toolBarText.setText(title_quicklead);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
    }

    @OnTextChanged(value = R.id.pincode_editText, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void pincodeChanged(CharSequence s) {
        try {
            if (s.length() != 0 && s.length() == 6 && pincode_editText.hasFocus() == true) {
                getPinCode(pincode_editText.getText().toString());
                //channelList.clear();
            } else if (s.length() == 0) {
                getdropDownFields();
                //channelList.clear();
                //channelTextView.setText("");
                //channelTextView.setHint(searchChannel);
                //channelSpinner.setSelection(0);
            }
        } catch (IndexOutOfBoundsException ie) {
            ie.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnItemSelected({R.id.stateSpinner, R.id.citySpinner, R.id.talukSpinner, R.id.channelSpinner})
    public void spinnerItemSelected(Spinner spinner, int position) {
        try {
            switch (spinner.getId()) {
                case R.id.stateSpinner:
                    CommonPojo stateid = (CommonPojo) stateSpinner.getSelectedItem();
                    stateCode = stateid.getKEY_CODE();
                    stateDescription = stateid.getKEY_DESCRIPTION();
                    if (!stateCode.equalsIgnoreCase("0")) {
                        getCity(stateCode);
                    } else {
                        cityList.add(clearAdapte(cityList, citySpinner));
                        setSpinnerValue(cityList, citySpinner, cityAdapter);
                    }
                    break;

                case R.id.citySpinner:
                    CommonPojo cityid = (CommonPojo) citySpinner.getSelectedItem();
                    cityCode = cityid.getKEY_CODE();
                    cityDescription = cityid.getKEY_DESCRIPTION();
                    if (!cityCode.equalsIgnoreCase("0")) {
                        getTaluk(cityCode);
                    } else {
                        talukList.add(clearAdapte(talukList, talukSpinner));
                        setSpinnerValue(talukList, talukSpinner, talukAdapter);
                    }
                    break;

                case R.id.talukSpinner:
                    CommonPojo talukid = (CommonPojo) talukSpinner.getSelectedItem();
                    talukCode = talukid.getKEY_CODE();
                    talukDescription = talukid.getKEY_DESCRIPTION();
                    if (!talukCode.equalsIgnoreCase("0")) {
                        getTownPinCode(talukCode);
                    }
                    break;

                case R.id.channelSpinner:
                    try {
                        if (channelRbtn.isChecked()) {
                            CommonPojo channelid = (CommonPojo) channelSpinner.getSelectedItem();
                            channelCode = channelid.getKEY_CODE();
                            channelDescription = channelid.getKEY_DESCRIPTION();
                        } else if (directRbtn.isChecked()) {
                            CommonPojo channelid = (CommonPojo) channelSpinner.getSelectedItem();
                            directCode = channelid.getKEY_CODE();
                            channelDescription = channelid.getKEY_DESCRIPTION();
                            if (directCode.equalsIgnoreCase(referalAgentCode)) {
                                channelTextView.setEnabled(true);
                                channelTextView.setClickable(true);
                                channelTextView.setHint(searchReferralAgent);
                                channelTextView.setText("");
                                channelTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, AppCompatResources.getDrawable(mContext, R.drawable.ic_search_icon), null);
                            } else {
                                channelTextView.setText(getString(R.string.tvscreditservice));
                                channelTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                                channelTextView.setEnabled(false);
                                channelTextView.setClickable(false);
                            }
                        }
                    } catch (Exception n) {
                        n.printStackTrace();
                    }
                    break;

                default:
                    break;
            }
        } catch (ClassCastException ce) {
            ce.printStackTrace();
        }
    }

    @OnClick({R.id.btnSumbit, R.id.layout_toolBar, R.id.prevBtn, R.id.nextBtn})
    void onButtonClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.btnSumbit:
                    editTextEmpty = validateEditText(new EditText[]{cutomername_editText, mobileno_editText, pincode_editText});

                    if (prodId == null || prodId.equalsIgnoreCase(""))
                        showSnackbar(quickLead_mainLayout, selectProdTxt);

                    else if (!validateEditText(new EditText[]{cutomername_editText}))
                        showSnackbar(quickLead_mainLayout, customerName);

                    else if (!validateEditText(new EditText[]{mobileno_editText}))
                        showSnackbar(quickLead_mainLayout, customerNo);

                    else if (!isValidMobile(mobileno_editText.getText().toString())) {
                        mobileno_editText.setError(mobileInvalidError);
                        showSnackbar(quickLead_mainLayout, mobileInvalidError);
                    } else if (!validateEditText(new EditText[]{pincode_editText}))
                        showSnackbar(quickLead_mainLayout, customerPincode);

                    else if (!validateSpinner(new AppCompatSpinner[]{stateSpinner}))
                        showSnackbar(quickLead_mainLayout, selectStateTxt);

                    else if (!validateSpinner(new AppCompatSpinner[]{citySpinner}))
                        showSnackbar(quickLead_mainLayout, selectCityTxt);

                    else if (!validateSpinner(new AppCompatSpinner[]{talukSpinner}))
                        showSnackbar(quickLead_mainLayout, selectTalukTxt);

                    /*else if(!directRbtn.isChecked() && !channelRbtn.isChecked())
                        showSnackbar(quickLead_mainLayout, selectSourcingTxt);*/

                    else if (directRbtn.isChecked() && (directCode.equalsIgnoreCase(referalAgentCode)
                            && channelTextView.getText().length() == 0))
                        showSnackbar(quickLead_mainLayout, selectReferralAgentTxt);

                    else if (channelRbtn.isChecked() && channelTextView.getText().length() == 0)
                        showSnackbar(quickLead_mainLayout, searchChannel);

                    else
                        leadInsert();

                    break;

                case R.id.layout_toolBar:
                    startActivity(new Intent(mContext, HomeActivity.class));
                    finish();
                    break;
            }

        } catch (NullPointerException n) {
            n.printStackTrace();
        }
    }

    @OnClick({R.id.bottom_menu_quick, R.id.bottom_menu_leadsgiven, R.id.bottom_menu_leadsreceived, R.id.bottom_menu_dashboard, R.id.bottom_menu_logout})
    public void layoutClicked(LinearLayout linearLayout) {
        switch (linearLayout.getId()) {
            case R.id.bottom_menu_quick:
                startActivity(new Intent(mContext, QuickLeadActivity.class));
                finish();
                break;
            case R.id.bottom_menu_leadsgiven:
                startActivity(new Intent(mContext, LeadsGivenActivity.class));
                finish();
                break;
            case R.id.bottom_menu_leadsreceived:
                startActivity(new Intent(mContext, PickLeadsActivity.class));
                finish();
                break;
            case R.id.bottom_menu_dashboard:
                startActivity(new Intent(mContext, DashboardActivity.class));
                finish();
                break;
            case R.id.bottom_menu_logout:
                alertConfirmLogout();
                break;
        }
    }

    private void alertConfirmLogout() {
        SweetAlertDialog dialog = new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE);
        dialog
                .setTitleText(confirmMsg)
                .setContentText(logoutMsg)
                .setConfirmText(OkTxt)
                .setCancelText(cancelTxt)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        preference.logoutUser();
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


    private void leadInsert() {
        firstName = cutomername_editText.getText().toString().trim();
        phoneNo1 = mobileno_editText.getText().toString().trim();
        pincode = pincode_editText.getText().toString().trim();

        if (isNetworkAvailable(mContext)) {
            try {
                viewDialog.showProgressDialog();
                leadResponse = new QuickLeadResponse();
                String staticChannelCode = "RP";
                leadResponse.setFirst_name(firstName);
                leadResponse.setPRODUCT_CODE(prodId);
                leadResponse.setPhone_mobile1(phoneNo1);
                leadResponse.setPincode(pincode);
                leadResponse.setState_code(stateCode);
                leadResponse.setCity_code(cityCode);
                leadResponse.setTaluk(talukCode);
                leadResponse.setChannel_code(staticChannelCode);
                if (directRbtn.isChecked()) {
                    leadResponse.setDealercode(channelTextView.getText().toString());
                    leadResponse.setDirect(directCode);
                } else if (channelRbtn.isChecked()) {
                    leadResponse.setDealercode(channelCode);
                    leadResponse.setDirect("");
                } else {
                    leadResponse.setDealercode("");
                    leadResponse.setDirect("");
                }

                leadResponse.setCreated_by(preference.getPref(USERID, ""));
                obj = new CommonPojo();
                obj.setLMSobj(leadResponse);

                Call<ResponseBody> responseBodyCall = mAPICallInterface.quickLeadInsert(obj);
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

                                            String leadid = jsonObject.getString(respquickLeadInsert);
                                            new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE)
                                                    .setTitleText(message)
                                                    .setContentText(quickLeadInsertMsg1
                                                            + leadid
                                                            + quickLeadInsertMsg2)
                                                    .setConfirmText("Ok")
                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sDialog) {
                                                            sDialog.dismissWithAnimation();
                                                            startActivity(new Intent(mContext, HomeActivity.class));
                                                            finish();
                                                        }
                                                    })
                                                    .show();
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
            showErrorDialog(mContext, internetErrorTitle, internetError);
        }
    }

    private void checkPincodeListener() {
        pincode_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (s.length() != 0 && s.length() == 6 && pincode_editText.hasFocus() == true) {
                        getPinCode(pincode_editText.getText().toString());
                        channelList.clear();
                    } else if (s.length() == 0) {
                        getdropDownFields();
                        channelList.clear();
                        channelSpinner.setSelection(0);
                        channelTextView.setText("");
                        channelTextView.setHint(searchChannel);
                    }
                } catch (IndexOutOfBoundsException ie) {
                    ie.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @OnClick(R.id.notification_toolbar)
    public void onClick(View view) {
        try {
            if (isNetworkAvailable(mContext)) {
                Utils.NotificationUpdates(mContext, preference.getPref(USERID, ""));

            } else {
                ShowAlertDialog(mContext, internetErrorTitle, internetError);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getProduct() {
        try {
            if (isNetworkAvailable(mContext)) {
                try {
                    viewDialog.showProgressDialog();
                    loginResponse = new LoginResponse();
                    loginResponse.setUserid(preference.getPref(USERID, ""));
                    Call<ResponseBody> responseBodyCall = mAPICallInterface.getProductMapped(loginResponse);
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
                                            if (jsonArray != null) {
                                                for (int i = 0; i < jsonArray.length(); i++) {
                                                    productList.add(jsonArray.getString(i));
                                                }
                                                creatProductView(jsonArray);
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

    private void getdropDownFields() {
        try {
            if (isNetworkAvailable(mContext)) {
                try {
                    Call<ResponseBody> responseBodyCall = mAPICallInterface.getDropdownFields();
                    responseBodyCall.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response != null) {
                                if (response.code() == 200) {
                                    try {
                                        stateList = new ArrayList<CommonPojo>();
                                        cityList = new ArrayList<CommonPojo>();
                                        talukList = new ArrayList<CommonPojo>();
                                        channelList = new ArrayList<CommonPojo>();

                                        String responseValue = response.body().string();
                                        if (responseValue.length() > 0) {
                                            JSONArray jsonArray = new JSONArray(responseValue);
                                            if (jsonArray.length() > 0) {
                                                setFirstArraylist("0", selectStateTxt, stateList);
                                                setFirstArraylist("0", selectCityTxt, cityList);
                                                setFirstArraylist("0", selectTalukTxt, talukList);
                                                setFirstArraylist("0", selectChannelTxt, channelList);
                                                for (int i = 0; i < jsonArray.length(); i++) {
                                                    if (jsonArray.getJSONObject(i).getString(respKeycode).equalsIgnoreCase(respState)) {
                                                        setFirstArraylist(jsonArray.getJSONObject(i).getString(respCode), jsonArray.getJSONObject(i).getString(respCodeDesc), stateList);
                                                    }
                                                }
                                                setSpinnerValue(stateList, stateSpinner, stateAdapter);
                                                setSpinnerValue(cityList, citySpinner, cityAdapter);
                                                setSpinnerValue(talukList, talukSpinner, talukAdapter);
                                                setSpinnerValue(channelList, channelSpinner, channelAdapter);
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
                            t.printStackTrace();
                        }
                    });


                } catch (IndexOutOfBoundsException ie) {
                    ie.printStackTrace();
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

    private void getCity(String stateCode) {
        try {
            if (isNetworkAvailable(mContext)) {
                cityList.add(clearAdapte(cityList, citySpinner));
                try {
                    obj = new CommonPojo();
                    obj.setStatecode(stateCode);
                    Call<ResponseBody> responseBodyCall = mAPICallInterface.getCity(obj);
                    responseBodyCall.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response != null) {
                                if (response.code() == 200) {
                                    try {
                                        String responseValue = response.body().string();
                                        if (responseValue.length() > 0) {
                                            JSONArray jsonArray = new JSONArray(responseValue);
                                            if (jsonArray != null) {
                                                for (int i = 0; i < jsonArray.length(); i++) {
                                                    setFirstArraylist(jsonArray.getJSONObject(i).getString(respCityCode), jsonArray.getJSONObject(i).getString(respCityeDesc), cityList);
                                                }
                                                setSpinnerValue(cityList, citySpinner, cityAdapter);
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

    private void getTaluk(String cityCode) {
        try {
            if (isNetworkAvailable(mContext)) {
                talukList.add(clearAdapte(talukList, talukSpinner));
                try {
                    obj = new CommonPojo();
                    obj.setCityCode(cityCode);
                    Call<ResponseBody> responseBodyCall = mAPICallInterface.getTown(obj);
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
                                                for (int i = 0; i < jsonArray.length(); i++) {
                                                    setFirstArraylist(jsonArray.getJSONObject(i).getString(respTalukCode), jsonArray.getJSONObject(i).getString(respTalukDesc), talukList);
                                                }
                                                setSpinnerValue(talukList, talukSpinner, talukAdapter);
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

    private void getTownPinCode(String towncode) {
        try {
            if (isNetworkAvailable(mContext)) {
                try {
                    obj = new CommonPojo();
                    obj.setTowncode(towncode);
                    Call<ResponseBody> responseBodyCall = mAPICallInterface.getTownPinCode(obj);
                    responseBodyCall.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response != null) {
                                if (response.code() == 200) {
                                    try {
                                        String responseValue = response.body().string();
                                        JSONObject jsonObject = new JSONObject(responseValue);
                                        if (jsonObject != null) {
                                            JSONArray jsonArray = new JSONArray(jsonObject.getString(respTownPincodeResult));
                                            if (jsonArray.length() > 0) {
                                                String Pincode = jsonArray.getJSONObject(0).getString(resTownPincode);
                                                pincode_editText.setText(Pincode);
                                                //checkPincodeListener();
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

    private void getPinCode(String pinCode) {
        try {
            if (isNetworkAvailable(mContext)) {
                try {
                    obj = new CommonPojo();
                    obj.setPincode(pinCode);
                    Call<ResponseBody> responseBodyCall = mAPICallInterface.getPinCodeMapped(obj);
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

                                                setPincodeResult(state, stateSpinner);
                                                setPincodeResult(city, citySpinner);
                                                setPincodeResult(town, talukSpinner);
                                                Utils.closeKeyboard();
                                            } else {
                                                showErrorDialog(mContext, message, pincodeInvalidError);
                                                pincode_editText.setText("");
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

    private void getChannel() {
        try {
            viewDialog.showProgressDialog();
            if (isNetworkAvailable(mContext)) {
                try {
                    obj = new CommonPojo();
                    obj.setUserid(preference.getPref(USERID));
                    Call<ResponseBody> responseBodyCall = mAPICallInterface.getChannelMapped(obj);
                    responseBodyCall.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response != null) {
                                if (response.code() == 200) {
                                    viewDialog.hideProgressDialog();
                                    try {
                                        String responseValue = response.body().string();
                                        JSONObject jsonObject = new JSONObject(responseValue);
                                        if (jsonObject != null) {
                                            JSONArray jsonArray = new JSONArray(jsonObject.getString(respDealerResult));
                                            channelList.clear();
                                            if (jsonArray.length() > 0) {
                                                for (int i = 0; i < jsonArray.length(); i++) {
                                                    setFirstArraylist(jsonArray.getJSONObject(i).getString(respChannelCode), jsonArray.getJSONObject(i).getString(respChannelDesc), channelList);
                                                }
                                                if (channelRbtn.isChecked()) {
                                                    setSpinnerValue(channelList, channelSpinner, channelAdapter);
                                                    if (channelList.size() > 0) {
                                                        channelSpinner.setVisibility(View.GONE);
                                                        channelTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, AppCompatResources.getDrawable(mContext, R.drawable.ic_search_icon), null);
                                                        channelTextView.setEnabled(true);
                                                        Bundle data = new Bundle();
                                                        data.putString(resPincode_, pincode_editText.getText().toString());
                                                        data.putString(respProdCode, prodId);
                                                        data.putString(sourceOption, channel);
                                                        p.setArguments(data);
                                                        p.show(fm, selectChannelTxt);
                                                    } else {
                                                        channelTextView.clearFocus();
                                                        channelTextView.setFocusable(false);
                                                        showSnackbar(quickLead_mainLayout, channelInvalidError);
                                                    }
                                                }
                                            } else {
                                                if (channelRbtn.isChecked()) {
                                                    channelTextView.clearFocus();
                                                    channelTextView.setFocusable(false);
                                                    channelSpinner.setSelection(0);
                                                    showSnackbar(quickLead_mainLayout, channelInvalidError);
                                                }
                                            }
                                        }
                                    } catch (IndexOutOfBoundsException ie) {
                                        viewDialog.hideProgressDialog();
                                        ie.printStackTrace();
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
                            t.printStackTrace();
                            viewDialog.hideProgressDialog();
                        }
                    });
                } catch (Exception e) {
                    viewDialog.hideProgressDialog();
                    e.printStackTrace();
                }
            } else {
                showErrorDialog(mContext, internetErrorTitle, internetError);
                viewDialog.hideProgressDialog();
            }
        } catch (IndexOutOfBoundsException ie) {
            viewDialog.hideProgressDialog();
            ie.printStackTrace();
        } catch (Exception e) {
            viewDialog.hideProgressDialog();
            e.printStackTrace();
        }
    }

    private void getDirect(String directCode) {
        try {
            viewDialog.showProgressDialog();
            if (isNetworkAvailable(mContext)) {
                try {
                    obj = new CommonPojo();
                    obj.setDIRECT(directCode);
                    Call<ResponseBody> responseBodyCall = mAPICallInterface.getDirect(obj);
                    responseBodyCall.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                if (response != null) {
                                    if (response.code() == 200) {
                                        channelList.clear();
                                        viewDialog.hideProgressDialog();
                                        String responseValue = response.body().string();
                                        JSONObject jsonObject = new JSONObject(responseValue);
                                        if (jsonObject != null) {
                                            JSONArray jsonArray = new JSONArray(jsonObject.getString(respDirectResult));
                                            if (jsonArray != null) {
                                                for (int i = 0; i < jsonArray.length(); i++) {
                                                    setFirstArraylist(jsonArray.getJSONObject(i).getString(respDirCode), jsonArray.getJSONObject(i).getString(respDirDesc), channelList);
                                                }
                                                setSpinnerValue(channelList, channelSpinner, channelAdapter);
                                            }
                                        }
                                    } else {
                                        showErrorDialog(mContext, informationTxt, serverErrorMsg);
                                        viewDialog.hideProgressDialog();
                                    }
                                }
                            } catch (IndexOutOfBoundsException ie) {
                                ie.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            t.printStackTrace();
                            viewDialog.hideProgressDialog();
                        }
                    });


                } catch (IndexOutOfBoundsException ie) {
                    ie.printStackTrace();
                    viewDialog.hideProgressDialog();
                } catch (Exception e) {
                    e.printStackTrace();
                    viewDialog.hideProgressDialog();
                }
            } else {
                showErrorDialog(mContext, internetErrorTitle, internetError);
                viewDialog.hideProgressDialog();
            }
        } catch (IndexOutOfBoundsException ie) {
            ie.printStackTrace();
            viewDialog.hideProgressDialog();
        } catch (Exception e) {
            viewDialog.hideProgressDialog();
            e.printStackTrace();
        }
    }

    private void creatProductView(JSONArray jsonArray1) {
        try {
            GridLayoutManager layoutManager = new GridLayoutManager(mContext, 2);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            productRecylerView.setLayoutManager(layoutManager);
            productAdapter = new ProductListAdapter(getDataSet(jsonArray1), mContext, new AdapterCallback() {
                @Override
                public void onClickCallback(int pos, ProductResponse itemModel) {
                    prodId = itemModel.getSzproductcode();
                }
            });
            productRecylerView.setAdapter(productAdapter);
            productRecylerView.scrollToPosition(1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //To set Value to ProductAdapter
    @SuppressWarnings("unchecked")
    private ArrayList<ProductResponse> getDataSet(JSONArray temp) throws JSONException {
        @SuppressWarnings("rawtypes")
        ArrayList results = new ArrayList<ProductResponse>();
        for (int index = 0; index < temp.length(); index++) {
            JSONObject tempobj = temp.getJSONObject(index);
            ProductResponse obj = new ProductResponse(tempobj.getString(szproductcode), tempobj.getString(szdesc));
            results.add(index, obj);
        }
        return results;
    }

    public void setFirstArraylist(String code, String Description, ArrayList<CommonPojo> arrayList) {
        try {
            obj = new CommonPojo();
            obj.setKEY_CODE(code);
            obj.setKEY_DESCRIPTION(Description);
            arrayList.add(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSpinnerValue(ArrayList<CommonPojo> list, Spinner spinner, ArrayAdapter<CommonPojo> adapter) {
        try {
            adapter = new ArrayAdapter<CommonPojo>(this, R.layout.spinner_item, list);
            adapter.setDropDownViewResource(R.layout.spinner_item);
            spinner.setSelection(0, false);
            spinner.setAdapter(adapter);
        } catch (IndexOutOfBoundsException ioe) {
            ioe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPincodeResult(String result, Spinner spinner) {
        try {
            ArrayList<String> stateRes = new ArrayList<String>();
            stateRes.add(result);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, stateRes);
            adapter.setDropDownViewResource(R.layout.spinner_item);
            spinner.setAdapter(adapter);
        } catch (IndexOutOfBoundsException ioe) {
            ioe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CommonPojo clearAdapte(ArrayList<CommonPojo> adapter, Spinner spinner) {
        try {
            CommonPojo selectedItem = (CommonPojo) spinner.getItemAtPosition(0);
            String descriptio = selectedItem.getKEY_DESCRIPTION();
            String code = selectedItem.getKEY_CODE();
            adapter.clear();
            obj = new CommonPojo();
            obj.setKEY_CODE(code);
            obj.setKEY_DESCRIPTION(descriptio);

        } catch (IndexOutOfBoundsException ioe) {
            ioe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    @Override
    public void onClickCallback(int pos, ProductResponse itemModel) {
        Toast.makeText(this, itemModel.getSzproductcode(), Toast.LENGTH_SHORT).show();
    }

    private void loadBottomMenu() {
        try {
            JSONArray menuJsonArray = new JSONArray(preference.getPref(LOGININFORMATION));
            Log.i("JsonSharedPreference", "" + menuJsonArray.toString());
            mainMenuArrayList = new ArrayList<>();

            for (int i = 0; i < menuJsonArray.length(); i++) {
                JSONObject subMenuJsonObject = menuJsonArray.getJSONObject(i);

                MainMenu mainMenu = new MainMenu();
                if (subMenuJsonObject.getString(SUB_MENU_TEXT).equalsIgnoreCase(QUICK_LEADS)) {
                    bottom_menu_quick.setVisibility(View.VISIBLE);
                    Utils.setSvgImageviewDrawable(img_bottom_menu_quick, mContext, R.drawable.ic_quickleads);
                } else if (subMenuJsonObject.getString(SUB_MENU_TEXT).equalsIgnoreCase(LEADS_GIVEN)) {
                    bottom_menu_leadsgiven.setVisibility(View.VISIBLE);
                    Utils.setSvgImageviewDrawable(img_bottom_menu_leadsgiven, mContext, R.drawable.ic_leadsgiven);
                } else if (subMenuJsonObject.getString(SUB_MENU_TEXT).equalsIgnoreCase(LEADS_RECEIVED)) {
                    bottom_menu_leadsreceived.setVisibility(View.VISIBLE);
                    Utils.setSvgImageviewDrawable(img_bottom_menu_leadsreceived, mContext, R.drawable.ic_leadsreceived);
                } else if (subMenuJsonObject.getString(SUB_MENU_TEXT).equalsIgnoreCase(DASHBOARD)) {
                    bottom_menu_dashboard.setVisibility(View.VISIBLE);
                    Utils.setSvgImageviewDrawable(img_bottom_menu_dashboard, mContext, R.drawable.ic_dashboard);
                }
                bottom_menu_logout.setVisibility(View.VISIBLE);
                Utils.setSvgImageviewDrawable(img_bottom_menu_logout, mContext, R.drawable.ic_logout);

                mainMenuArrayList.add(mainMenu);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent in = new Intent(mContext, HomeActivity.class);
        startActivity(in);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }


    public static class DealersFragment extends DialogFragment implements DealersAdapter.ContactsAdapterListener {

        TextView sourceTextView;
        ImageView dismiss;
        RecyclerView rv;
        SearchView sv;

        DealersAdapter adapter;
        ArrayList<String> dealersList;
        CommonPojo obj;
        DealersPojo dealersPojo;

        LoaderDialog viewDialog;
        APICallInterface mAPICallInterface;

        String pincode, prodId, sourcing;
        Context context;
        SharedPreference preference;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // TODO Auto-generated method stub
            View rootView = inflater.inflate(R.layout.dealers_mapping_layout, null);
            context = getActivity();
            viewDialog = new LoaderDialog(context);
            mAPICallInterface = APIClient.getAPIService();

            pincode = getArguments().getString(Constants.resPincode_);
            prodId = getArguments().getString(Constants.respProdCode);
            sourcing = getArguments().getString(Constants.sourceOption);
            getDialog().setTitle(selectChannelTxt);

            preference = new SharedPreference(context);
            dealersList = new ArrayList<String>();
            sv = (SearchView) rootView.findViewById(R.id.searchView1);
            sourceTextView = (TextView) rootView.findViewById(R.id.sourceTextView);
            dismiss = (ImageView) rootView.findViewById(R.id.dismiss);
            rv = (RecyclerView) rootView.findViewById(R.id.mRecyerID);

            if (sourcing.equalsIgnoreCase(channel)) {
                getChannelAPI();
                sourceTextView.setText(selectChannelTxt);
                sv.setQueryHint(searchChannel);
            } else {
                getReferralAgentAPI();
                sourceTextView.setText(selectReferralAgentTxt);
                sv.setQueryHint(searchReferralAgent);
            }

            //SEARCH
            try {
                sv.setIconifiedByDefault(true);
                sv.setFocusable(true);
                sv.setIconified(false);
                sv.clearFocus();
                sv.requestFocusFromTouch();
                sv.setMaxWidth(Integer.MAX_VALUE);
                sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String txt) {
                        adapter.getFilter().filter(txt);
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String txt) {
                        try {
                            adapter.getFilter().filter(txt);
                        } catch (NullPointerException n) {
                            n.printStackTrace();
                        }
                        return false;
                    }
                });
            } catch (NullPointerException n) {
                n.printStackTrace();
            }
            //BUTTON
            dismiss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    dismiss();
                }
            });
            return rootView;
        }

        private void getChannelAPI() {
            try {
                viewDialog.showProgressDialog();
                if (isNetworkAvailable(context)) {
                    try {
                        obj = new CommonPojo();
                        obj.setUserid(preference.getPref(USERID));
                        Call<ResponseBody> responseBodyCall = mAPICallInterface.getChannelMapped(obj);
                        responseBodyCall.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response != null) {
                                    if (response.code() == 200) {
                                        viewDialog.hideProgressDialog();
                                        try {
                                            String responseValue = response.body().string();
                                            JSONObject jsonObject = new JSONObject(responseValue);
                                            if (jsonObject != null) {
                                                JSONArray jsonArray = new JSONArray(jsonObject.getString(respDealerResult));
                                                if (jsonArray.length() > 0) {
                                                    for (int i = 0; i < jsonArray.length(); i++) {
                                                        dealersList.add(jsonArray.getString(i));
                                                    }
                                                    setRecylerviewList(jsonArray);
                                                } else {
                                                    viewDialog.hideProgressDialog();
                                                    showErrorDialog(context, internetErrorTitle, response.code() + " Error");
                                                }
                                            }
                                        } catch (IndexOutOfBoundsException ie) {
                                            viewDialog.hideProgressDialog();
                                            ie.printStackTrace();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        viewDialog.hideProgressDialog();
                                        showErrorDialog(context, informationTxt, serverErrorMsg);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                t.printStackTrace();
                                viewDialog.hideProgressDialog();
                            }
                        });
                    } catch (Exception e) {
                        viewDialog.hideProgressDialog();
                        e.printStackTrace();
                    }
                } else {
                    viewDialog.hideProgressDialog();
                    showErrorDialog(context, internetErrorTitle, internetError);
                }
            } catch (IndexOutOfBoundsException ie) {
                ie.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        private void getReferralAgentAPI() {
            try {
                viewDialog.showProgressDialog();
                if (isNetworkAvailable(context)) {
                    try {
                        obj = new CommonPojo();
                        obj.setUserid(preference.getPref(USERID));
                        Call<ResponseBody> responseBodyCall = mAPICallInterface.getReferralAgent(obj);
                        responseBodyCall.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response != null) {
                                    if (response.code() == 200) {
                                        viewDialog.hideProgressDialog();
                                        try {
                                            String responseValue = response.body().string();
                                            JSONObject jsonObject = new JSONObject(responseValue);
                                            if (jsonObject != null) {
                                                JSONArray jsonArray = new JSONArray(jsonObject.getString(respAgencyResult));
                                                if (jsonArray.length() > 0) {
                                                    for (int i = 0; i < jsonArray.length(); i++) {
                                                        dealersList.add(jsonArray.getString(i));
                                                    }
                                                    setRecylerviewList(jsonArray);
                                                } else {
                                                    viewDialog.hideProgressDialog();
                                                    showErrorDialog(context, internetErrorTitle, response.code() + " Error");
                                                }
                                            }
                                        } catch (IndexOutOfBoundsException ie) {
                                            viewDialog.hideProgressDialog();
                                            ie.printStackTrace();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        viewDialog.hideProgressDialog();
                                        showErrorDialog(context, informationTxt, serverErrorMsg);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                t.printStackTrace();
                                viewDialog.hideProgressDialog();
                            }
                        });
                    } catch (Exception e) {
                        viewDialog.hideProgressDialog();
                        e.printStackTrace();
                    }
                } else {
                    viewDialog.hideProgressDialog();
                    showErrorDialog(context, internetErrorTitle, internetError);
                }
            } catch (IndexOutOfBoundsException ie) {
                ie.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void setRecylerviewList(JSONArray jsonArray) {
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            rv.setLayoutManager(mLayoutManager);
            rv.setItemAnimator(new DefaultItemAnimator());
            rv.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
            try {
                adapter = new DealersAdapter(context, getDataSet(jsonArray), this);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            rv.setAdapter(adapter);
        }

        @SuppressWarnings("unchecked")
        private ArrayList<DealersPojo> getDataSet(JSONArray temp) throws JSONException {
            @SuppressWarnings("rawtypes")
            ArrayList results = new ArrayList<DealersPojo>();
            for (int index = 0; index < temp.length(); index++) {
                JSONObject tempobj = temp.getJSONObject(index);
                DealersPojo obj = new DealersPojo(tempobj.getString(respChannelDesc), tempobj.getString(respChannelCode));
                results.add(index, obj);
            }
            return results;
        }

        @Override
        public void onContactSelected(DealersPojo contact) {
            this.dealersPojo = contact;
            channelTextView.setText(contact.getDealerName());
            channelTextView.clearFocus();
            channelTextView.setFocusable(false);
            Utils.closeKeyboard();
            dismiss();
        }

        @Override
        public void onDismiss(final DialogInterface dialog) {
            super.onDismiss(dialog);
            final Activity activity = getActivity();
            if (activity instanceof DialogInterface.OnDismissListener) {
                ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
            }
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
        }
    }
}