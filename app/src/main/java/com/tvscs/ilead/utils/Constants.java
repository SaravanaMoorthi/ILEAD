package com.tvscs.ilead.utils;

import ilead.tvscs.com.ilead.BuildConfig;

public interface Constants {

    public static final String URL = BuildConfig.LEADS_URL;

    String TYPEFACE_BOLD = "fonts/Poppins-Bold.ttf";
    String TYPEFACE_REGULAR = "fonts/Poppins-Regular.ttf";

    public static String SHARE_NAME = "SHARED_LMS";

    public String NAME_TEXT = "Name";
    public String SUB_MENU_TEXT = "SubmenuText";
    public String QUICK_LEADS = "Quick Leads";
    public String PICK_LEADS = "Pick Leads";
    public String LEADS_RECEIVED = "Leads Received";
    public String LEADS_GIVEN = "Leads Given";
    public String EMI_CALC = "EMI Calculator";
    public String DASHBOARD = "Dashboard";

    public String SHARED_PREF_LOGIN_INFO = "LOGININFORMATION";
    public static String SUCCESSCODE = "200";
    public static String ERRORCODE = "100";

    //Reponse Constants
    public static final String respStatus = "Status";
    public static final String respKeycode = "key_code";
    public static final String respState = "state";
    public static final String respCode = "code";
    public static final String respYear = "year";
    public static final String respDIRECT = "DIR";
    public static final String respCodeDesc = "code_desc";
    public static final String respProdCode = "szproductcode";
    public static final String respProdDesc = "szdesc";
    public static final String respChannelCode = "Dealercode";
    public static final String respChannelDesc = "DealerName";
    public static final String respCityCode = "szstatecode";
    public static final String respCityeDesc = "szdesc";
    public static final String respTalukCode = "SZTALUKCODE";
    public static final String respTalukDesc = "SZTALUKDESC";
    public static final String respDirCode = "Dir_code";
    public static final String respDirDesc = "Dir_desc";
    public static final String respTownPincodeResult = "RefTownPincodeResult";
    public static final String respPincodeResult = "RefPincodeResult";
    public static final String respDealerResult = "RefDealerResult";
    public static final String respAgencyResult = "RefagencyResult";
    public static final String respDirectResult = "Ref_DirectResult";
    public static final String respquickLeadInsert = "RefleadDataEntryInsertResult";
    public String respCustomerProfile = "Customer Profile";
    public String respResidenceStatus = "Residence Status";
    public String respRepayment = "Repayment";
    public String respCityCode_ = "city_code";
    public String respTaluk = "Taluk";
    public String respSceEmpNum_ = "SCE_EMP_NO";
    public String respProspectNum_ = "PROSPECT_NO";
    public String respAgreementNum_ = "AGREEMENT_NO";
    public String respleadUpdateResult = "RefUpdateLeadDeatilsResult";
    public String respActionCode = "Action_code";
    public String respActionDesc = "Action_Desc";
    public String respActionId = "action_id";
    public String respNextActionCode = "Nxt_Act_Code";
    public String respNextActionDesc = "Nxt_ACt_Desc";
    public String respActionresultCode = "Result_code";
    public String respActionresultDesc = "Result_desc";
    public String respActionresultId = "Result_id";
    public String respNextTime = "Next Time";
    public String respRolecode_ = "role_code";
    public String respprospectNum_ = "prospect_no";
    public String respBranchCode_ = "branch_code";
    public String respDisburseddate = "disbursedDate";
    public String respDisbursedAmount_ = "disbursed_amount";
    public String respAgreementNum = "agreementNo";
    public String respLogindate = "logindate";
    public String respUserid_ = "User_id";
    public String respUsertype_ = "user_type";
    public String respLeadNo_ = "lead_no";
    public String respProduct = "Product";
    public String respCity = "city";
    public String resptaluk = "taluk";
    public String respLeadsGiven = "Leads_Given";
    public String respLeadsRec = "Leads_Rec";
    public String respleadFollowupResult = "InsertLMSLeadFollowupResult";
    public String respupdateLosDataResult = "updateLOSdata_in_LMS_mobileResult";


    public static final String quickLeadInsertMsg1 = "Your data has been submitted successfully. Keep this number ";
    public static final String quickLeadInsertMsg2 = " for future use.";
    public static final String ileadNo = "LeadNo";
    public static final String sharedPrefclickUpdate = "ItemClickUpdate";
    public String leadUpdateInsertMsg = "Data has been submitted successfully";
    public String agreementNoError = "Agreement No not generted in LOS to disburse the lead";
    public String prospectNoDbError = "Entered Prospect No not found in Database";
    public String prospectNoInvalidError = "Invalid Prospect Number";
    public String pincodeInvalidError = "Invalid Pincode";
    public String errorSubmittingMsg = "Error while submitting. please try again";


    public static final String LS = "LS";
    public static final String PS = "PS";
    public static final String OkTxt = "OK";
    public static final String cancelTxt = "Cancel";
    public static final String YesTxt = "Yes";
    public static final String NoTxt = "No";
    public static final String YTxt = "Y";
    public static final String NTxt = "N";
    public static final String message = "Message";
    public static final String errorMsg = "ErrorMsg";
    public static final String errorCode = "ErrorCode";
    public static final String noRecordsMsg = "No Records Found";
    public String noLeadUpdates = "No lead updates!";
    public String leadStatusEmpty = "Lead status not available";
    public String leadStatus = "Lead status";
    public String BikeStatus = "Bike status";
    public String CarStatus = "Car status";
    public String Notavailable = "Not available";

    public String phonePermissionMsg = "Permissions required for this app";
    public String settingsPermissionsTxt = "Go to settings and enable permissions";

    public static final String informationTxt = "Information";
    public String sharedPrefLeadData = "leadData";
    public String leadFollowupTxt = "Leads FollowUp";
    public static final String serverErrorMsg = "Server Error";
    public static final String internetErrorTitle = "Connection Failed";
    public static final String internetError = "Internet connection problem.Try again....";
    public static final String mobileInvalidError = "Invalid Mobile Number";
    public static final String fieldRequired = "This field is required";
    public String actionDateEmptyErrorMsg = "Enter Action Date";
    public String dateTimeEmptyErrorMsg = "Please Select Action Date/Time";
    public String prospectNumEmptyErrorMsg = "Please Enter Prospect Number to Proceed";
    public String mailiIdValidErrorMsg = "Enter Valid EmailId";
    public String forgotPwdMsg = "UserName and Password has been sent to your MailId";
    public String errorforgotPwdMsg = "Your EmailId is Not Registered";
    public String permissionGrantedMsg = "Permission granted";
    public String permissionNeededMsg = "Permission Needed To Run The App";
    public String permissionDeniedMsg = "Some Permission is Denied";
    public String grantPermissionMsg = "You need to grant access to ";
    public String exitTitle = "Really Exit?";
    public String exitMsg = "Do you want to exit?";

    public String logoutTitle = "Really Logout?";
    public String logoutMsg = "Do you want to Logout?";
    public String PickMsg = "Do you want to Pick this ";
    public String confirmMsg = "Are you Sure?";
    public String confirmSMS = "Message";
    public String cancelPick = "No";
    public String confirmPick = "Yes";
    public String msgValidationText = "Please enter your message";

    public String callDialogTitle = "Do you want to make a call to ";
    public String callDialogMsg = "Are you sure you want to Logout?";
    public String callDialogconfirmPick = "Call";
    public String searchDealer = "Search Dealer";
    public String searchChannel = "Search Channel";
    public String searchReferralAgent = "Search Referral Agent";
    public String channelInvalidError = "Channel list not available";


    public static final String SP_URL = "SP_URL";
    public static final String UpdateApkFilename = "UPDATEiLEAD.apk";
    public static final String downloadUpdateTxt = "downloadupdate";
    public static final String appUpdateMsg = "Application update found.Proceed to update...";

    public static final String select = "Select";
    public static final String selectProdTxt = "Select Product";
    public static final String selectChannelTxt = "Select Channel";
    public static final String selectSourcingTxt = "Select Sourcing";
    public static final String selectReferralAgentTxt = "Select Referral Agent";
    public static final String selectStateTxt = "Select State";
    public static final String selectCityTxt = "Select City";
    public static final String selectTalukTxt = "Select Town";
    public static final String selectCustomerProfileTxt = "Select Customer Profile";
    public static final String selectResStatusTxt = "Select Residence Status";
    public static final String selectManufYearTxt = "Select Manufactured Year";
    public static final String selectRepayModeTxt = "Select Repayment mode";
    public static final String selectVehicleFinaliseTxt = "Vehicle Finalised ?";
    public static final String selectMakeTxt = "Select Make";
    public static final String selectModelTxt = "Select Model";
    public static final String selectVariantTxt = "Select Variant";
    public static final String regNoEmptyErrorMsg = "Enter Registration no";
    public static final String regNoInvalidErrorMsg = "Invalid Registration no";
    public String selectFollowupActionTxt = "Select Followup Action";
    public String selectNextActionTxt = "Select NextAction";
    public String selectActionTimeTxt = "Select Action Time";
    public String selectActionResultTxt = "Select Action Result";
    public String selectLeadStatusTxt = "Select Leadstatus";

    public String followupErrorMsg1 = "Access Denied to put followup on";
    public String followupErrorMsg2 = "contact Admin for details";
    public String Losdatasuccess = "Updated Los Data";
    public String Losdatafailure = "Unable to Update Los Data";
    public String fillDataMsg = "Please fill data";
    public String success = "Success";
    public String leadPickedMsg = "Lead Id has picked!";
    public String leadPicked = "leadpicked";

    public String disbursedTxt = "DISBURSED";
    public String droppedTxt = "DROPPED";
    public String adminTxt = "ADMIN";
    public String loginTxt = "LOGIN";
    public String rejectedTxt = "REJECTED";
    public String lostTxt = "LOST";

    //ProductCode Constants
    public static final String szproductcode = "szproductcode";
    public static final String szdesc = "szdesc";
    public static final String AL = "AL";
    public static final String AP = "AP";
    public static final String CA = "CA";
    public static final String CD = "CD";
    public static final String DC = "DC";
    public static final String LN = "LN";
    public static final String TR = "TR";
    public static final String TW = "TW";
    public static final String UT = "UT";
    public static final String UV = "UV";

    //Followup Constants
    public static final String rowNo = "RowNo";
    public static final String tmUserID = "TM_User_ID";
    public static final String actionCode = "action_code";
    public static final String createdOn = "created_On";
    public static final String createdBy = "created_by";
    public static final String leadCategory = "lead_category";
    public static final String nxtActCode = "nxt_act_code";
    public static final String nxtActDate = "nxt_act_date";
    public static final String nxtActTime = "nxt_act_time";
    public static final String resultCode = "result_code";
    public static final String status = "status";

    //Notification Constants
    public static final String lead_no = "lead_no";
    public static final String first_name = "first_name";
    public static final String dateTime = "dateTime";
    public static final String Next_Action = "Next_Action";
    public static final String notification = "Notification";


    //SharedPreference Constants
    public static final String PREFS_NAME = "iLEAD";
    public static final String IS_LOGIN = "IS_LOGIN";
    public static final String USERID = "userid";
    public static final String PASSWORD = "password";
    public static final String IMEINUMBER = "imei_number";
    public static final String IPADDRESS = "ipaddress";
    public static final String OUTERRORCODE = "outErrorCode";
    public static final String OUTERRORMSG = "outErrorMsg";
    public static final String LOGININFORMATION = "LOGININFORMATION";

    //CustomerDetail Constants
    public String cusDetailsTxt = "Customer Details";
    public String className = "ClassName";
    public String respleadId_ = "lead_id";
    public String respLeadId_ = "Lead_id";
    public String respFollowupLeadId_ = "Followup_Lead_id";
    public String respFirstName_ = "first_name";
    public String respPhoneNum_ = "phone_mobile1";
    public String respPhoneNum2_ = "phone_mobile2";
    public String respproductDesc_ = "PRODUCT_DESC";
    public String respproductCode_ = "PRODUCT_CODE";
    public String respCreatedOn_ = "created_on";
    public String respstatus = "status";
    public String respActionResult_ = "Action_result";
    public String respPickleadResult = "RefpickleadResult";
    public String respCreatedBy_ = "created_by";
    public String respactionCode_ = "action_code";
    public String respnxtActionCode_ = "nxt_act_code";
    public String respresultCode_ = "result_code";
    public String respnxtActionTime_ = "nxt_act_time";
    public String respsceEmpNum_ = "sce_emp_no";
    public String respcreatedOn_ = "created_On";

    public String resppanCard_ = "pan_card";
    public String respPassport = "PASSPORT";
    public String respVoterId_ = "VOTER_ID";
    public String respDrivinglicense_ = "DRIVING_LICENSE";
    public String respRationCard_ = "RATION_CARD";
    public String respAdharCard_ = "adhar_card";
    public String respAddress1_ = "address_1";
    public String respAddress2_ = "address_2";
    public String resPincode_ = "pin_code";
    public String resTownPincode = "Pincode";
    public String respVehicleRegno_ = "VEHICLE_REGNO";
    public String respLoanAmt_ = "loan_amnt";
    public String respTenure_ = "tenure";
    public String respEmicomfort_ = "emi_comfort";
    public String respStateCode_ = "state_code";
    public String respResidentCode_ = "resident_code";
    public String respManufYear_ = "manuf_year";
    public String respVehiclefinalise_ = "VEHICLE_FINALISE";
    public String respCusprofCode_ = "CUS_PROF_CODE";
    public String respRepayCode_ = "repay_code";
    public String respVariant = "Variant";
    public String respvariant = "variant";
    public String respModel = "Model";
    public String respmodel = "models";
    public String respMake = "Make";
    public String respmake = "make";


    public String customerName = "Enter Customer Name";
    public String customerNo = "Enter Customer Mobile Number";
    public String invalidMobileNum = "Mobile Number Not available";
    public String customerAddress1 = "Enter Customer Address 1";
    public String customerPincode = "Enter Customer Pincode";
    public String customerLoanAmt = "Enter Loan Amount";
    public String customerLoanTenure = "Enter Tenure";
    public String customerIDproof = "Select atleast one ID proof";

    public String bottomMenuQuickValue = "1";
    public String bottomMenuleadsGivenValue = "2";
    public String bottomMenuleadsReceivedValue = "3";
    public String bottomMenuDashboardValue = "4";
    public String bottomMenuDefaultValue = "0";

    public String smsDialogTitle = "Do you want to send SMS to ";
    public String smsDialogconfirmPick = "Send";
    public String smsHint = "Enter your Message";

    public static String selfService = "Self Source";
    public static String selfServiceCode = "SS";
    public static String empRefProgram = "Emp Referral Program";
    public static String empRefProgramCode = "RP";
    public static String referalAgentCode = "RA";
    public static String referalAgent = "Referral Agent";
    public static String sourceOption = "Sourcing";
    public static String channel = "Channel";
    public static String direct = "Direct";
    public static String digital = "Digital";
    public static String digitalCode = "DIG";

    public static String version = "Version";
}
