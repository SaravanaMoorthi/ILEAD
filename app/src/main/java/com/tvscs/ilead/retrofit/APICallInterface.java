package com.tvscs.ilead.retrofit;

import com.tvscs.ilead.model.CommonPojo;
import com.tvscs.ilead.model.LeadUpdatePojo;
import com.tvscs.ilead.model.LoginResponse;
import com.tvscs.ilead.model.MainMenu;
import com.tvscs.ilead.model.dashboard.DashboardModel;
import com.tvscs.ilead.model.dashboard.DashboardModels;
import com.tvscs.ilead.model.homechartmodel.HomeChartsModel;
import com.tvscs.ilead.model.homechartmodel.RefConenewResult;
import com.tvscs.ilead.model.homechartmodel.homesubchartmodel.HomeSubChartModel;
import com.tvscs.ilead.model.leadgivenmodel.LeadsGivenModel;
import com.tvscs.ilead.model.leadsreceivedmodel.LeadsReceivedModel;
import com.tvscs.ilead.model.pickleadmodel.PickLeadIDModel;
import com.tvscs.ilead.model.pickleadmodel.PickLeadsModel;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APICallInterface {

    @Headers({"Accept: application/json"})
    @POST("RefLeadsGiven")
    Call<LeadsGivenModel> getLeadsGivenResponse(@Body LeadsGivenModel request);

    @Headers({"Accept: application/json"})
    @POST("RefGetLeadDetails")
    Call<ArrayList<LeadsReceivedModel>> getLeadsReceievedResponse(@Body LeadsReceivedModel request);

    @Headers({"Accept: application/json"})
    @POST("Refpicklead")
    Call<PickLeadsModel> getpickLeadsResponse(@Body PickLeadsModel request);

    @Headers({"Accept: application/json"})
    @POST("CheckUserInternalOrExternalLoginMobile")
    Call<ResponseBody> getLogin(@Body LoginResponse request);

    @Headers({"Accept: application/json"})
    @POST("GetRefUserwiseSubmenuMobile")
    Call<ResponseBody> getLoginMenuAccess(@Body LoginResponse request);

    @Headers({"Accept: application/json"})
    @POST("GetLMSSmartPhoneVersion")
    Call<ResponseBody> getVersionUpdate(@Body MainMenu request);

    @Headers({"Accept: application/json"})
    @POST("GetProductNameMappedUsersMobile")
    Call<ResponseBody> getProductMapped(@Body LoginResponse request);

    @Headers({"Accept: application/json"})
    @POST("GetdropdownCommonFiledsMobile")
    Call<ResponseBody> getDropdownFields();

    @Headers({"Accept: application/json"})
    @POST("GetCityName")
    Call<ResponseBody> getCity(@Body CommonPojo request);

    @Headers({"Accept: application/json"})
    @POST("GetTownName")
    Call<ResponseBody> getTown(@Body CommonPojo request);

    @Headers({"Accept: application/json"})
    @POST("RefTownPincode")
    Call<ResponseBody> getTownPinCode(@Body CommonPojo request);

    @Headers({"Accept: application/json"})
    @POST("RefPincode")
    Call<ResponseBody> getPinCodeMapped(@Body CommonPojo request);

    @Headers({"Accept: application/json"})
    @POST("RefDealer")
    Call<ResponseBody> getChannelMapped(@Body CommonPojo request);

    @Headers({"Accept: application/json"})
    @POST("RefleadDataEntryInsert")
    Call<ResponseBody> quickLeadInsert(@Body CommonPojo request);

    @Headers({"Accept: application/json"})
    @POST("RefGetLeadDetails")
    Call<ResponseBody> getleadsReceived(@Body CommonPojo request);

    @Headers({"Accept: application/json"})
    @POST("getMakeList")
    Call<ResponseBody> getMake(@Body CommonPojo request);

    @Headers({"Accept: application/json"})
    @POST("getModelList")
    Call<ResponseBody> getModel(@Body CommonPojo request);

    @Headers({"Accept: application/json"})
    @POST("getVariantlList")
    Call<ResponseBody> getVariant(@Body CommonPojo request);

    @Headers({"Accept: application/json"})
    @POST("RefUpdateLeadDeatils")
    Call<ResponseBody> leadUpdateInsert(@Body LeadUpdatePojo request);

    @Headers({"Accept: application/json"})
    @POST("GetActionMaster")
    Call<ResponseBody> getFollowupAction();

    @Headers({"Accept: application/json"})
    @POST("GetLMSResultMaster")
    Call<ResponseBody> getActionResult(@Body CommonPojo retrofitRequest);

    @Headers({"Accept: application/json"})
    @POST("RefGetLMSNextActionMastermobile")
    Call<ResponseBody> getNextAction();

    @Headers({"Accept: application/json"})
    @POST("GetCommonFieldsMobile")
    Call<ResponseBody> getActionTime(@Body CommonPojo retrofitRequest);

    @Headers({"Accept: application/json"})
    @POST("GetLMSLeadStatus")
    Call<ResponseBody> getLeadStatus(@Body CommonPojo retrofitRequest);

    @Headers({"Accept: application/json"})
    @POST("Get_DROPPED_case_ADMIN_auth_Mobile")
    Call<ResponseBody> getDropcaseAuth(@Body CommonPojo retrofitRequest);

    @Headers({"Accept: application/json"})
    @POST("Get_LOS_data_Mobile")
    Call<ResponseBody> getLOSData(@Body CommonPojo retrofitRequest);

    @Headers({"Accept: application/json"})
    @POST("InsertLMSLeadFollowup")
    Call<ResponseBody> insertFollowupData(@Body CommonPojo retrofitRequest);

    @Headers({"Accept: application/json"})
    @POST("updateLOSdata_in_LMS_mobile")
    Call<ResponseBody> updateLOSData(@Body CommonPojo retrofitRequest);

    @Headers({"Accept: application/json"})
    @POST("GetLMSFollowupDetails")
    Call<ResponseBody> getFollowupData(@Body CommonPojo retrofitRequest);

    @Headers({"Accept: application/json"})
    @POST("GetForgotpassword")
    Call<ResponseBody> getForgotPassword(@Body LoginResponse request);

    @Headers({"Accept: application/json"})
    @POST("RefGetDashBoard")
    Call<ArrayList<DashboardModel>> getDashboardData(@Body DashboardModel dashboardModel);

    @Headers({"Accept: application/json"})
    @POST("RefpickleadbyID")
    Call<PickLeadIDModel> getPickLeadbyId(@Body PickLeadIDModel request);

    @Headers({"Accept: application/json"})
    @POST("RefGetTodayUpdationClockmobile")
    Call<ResponseBody> getClockUpdates(@Body CommonPojo request);

    @Headers({"Accept: application/json"})
    @POST("REFCONE")
    Call<HomeChartsModel> getRefcone(@Body HomeChartsModel request);


    @Headers({"Accept: application/json"})
    @POST("Refconenew")
    Call<HomeSubChartModel> getUsercone(@Body HomeSubChartModel request);


    @Headers({"Accept: application/json"})
    @POST("Ref_Dashboard_New")
    Call<DashboardModels> getDashboardNew(@Body DashboardModels request);

    @Headers({"Accept: application/json"})
    @POST("Ref_Direct")
    Call<ResponseBody> getDirect(@Body CommonPojo request);

    @Headers({"Accept: application/json"})
    @POST("Refagency")
    Call<ResponseBody> getReferralAgent(@Body CommonPojo request);
}
