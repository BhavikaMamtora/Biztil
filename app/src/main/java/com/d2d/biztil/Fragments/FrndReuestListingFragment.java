package com.d2d.biztil.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.d2d.biztil.Custom_Listview.FrndReqListEndlessListView;
import com.d2d.biztil.Custom_Listview.Frnd_Request_Adapter;
import com.d2d.biztil.Helper.Constants;
import com.d2d.biztil.Helper.CustomProgressDialog;
import com.d2d.biztil.Helper.Methods;
import com.d2d.biztil.Helper.NetConnection;
import com.d2d.biztil.Helper.PrefsKeys;
import com.d2d.biztil.Model.FrndRequest;
import com.d2d.biztil.R;
import com.d2d.biztil.Webservice.Json_keys;
import com.d2d.biztil.Webservice.WebServerCall;
import com.d2d.biztil.Webservice.Web_url_api;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ratan on 7/29/2015.
 */
public class FrndReuestListingFragment extends Fragment {

    static final int DATE_PICKER_ID = 1111;
    public static Button frg_tr_fromdate, frg_tr_todate, frg_tr_go_btn;
    private static NetConnection internet;
    private static Boolean       netConnection;
    FrndReqListEndlessListView frg_trns_report_lv;
    ArrayList<FrndRequest>     tar_array;
    Frnd_Request_Adapter       tr_adapter;
    View                       v;
    int page = 1;
    private SharedPreferences prefsPrivate;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_frnd_req_listing, null);

        prefsPrivate = getActivity().getSharedPreferences(
                Constants.PREFS_PRIVATE, Context.MODE_PRIVATE);

        getActivity().setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        initControl();
        internet = new NetConnection(getActivity());
        netConnection = internet.HaveNetworkConnection();

        if (netConnection == true) {

            getTransReportAsyncTask fl = new getTransReportAsyncTask(page);
            fl.execute();

        }
        else {
            internet.showNetDialog(getActivity());

        }


        return v;
    }

    private void initControl() {

        frg_trns_report_lv = (FrndReqListEndlessListView) v.findViewById(R.id
                                                                                 .frg_trns_report_lv);
        tar_array = new ArrayList<FrndRequest>();

        frg_trns_report_lv.shouldAddFooterView = false;
        tr_adapter = new Frnd_Request_Adapter(getActivity(), tar_array);
        frg_trns_report_lv.setLoadingView(R.layout.loadmore);

        page = 1;
        frg_trns_report_lv.setListener(new FrndReqListEndlessListView.FrndReqEndlessListener() {

            @Override
            public void loadData() {
                // TODO Auto-generated method stub
                page += 1;
                internet = new NetConnection(getActivity());
                netConnection = internet.HaveNetworkConnection();

                if (netConnection == true) {

                    getTransReportAsyncTask fl = new getTransReportAsyncTask(page);
                    fl.execute();

                }
                else {

                    internet.showNetDialog(getActivity());
                }
            }
        });

        frg_trns_report_lv.setAdapter(tr_adapter);


    }


    class getTransReportAsyncTask extends AsyncTask<Void, Boolean, Boolean> {
        CustomProgressDialog pdiDialog;
        String               success, message, uid;
        int trpage = 1;
        String url;


        public getTransReportAsyncTask(int page) {
            this.trpage = page;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (page == 1) {
                pdiDialog = new CustomProgressDialog(getContext());
                pdiDialog.setMessage("Loading...");
                pdiDialog.setCancelable(false);
                pdiDialog.show();
                tar_array = new ArrayList<FrndRequest>();
                tr_adapter = new Frnd_Request_Adapter(getActivity(), tar_array);
                frg_trns_report_lv.setAdapter(tr_adapter);
            }

            if (tar_array.size() >= 10) {
                frg_trns_report_lv.shouldAddFooterView = true;
            }

            tar_array = new ArrayList<FrndRequest>();

            uid = prefsPrivate.getString(PrefsKeys.Login_Prefs_Keys.UID, "0");

            uid = "2";
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            boolean executedSuccefully = true;
            // /////////////////////////
            if (Build.VERSION.SDK_INT >= 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();

                StrictMode.setThreadPolicy(policy);
            }


            try {

                String responseString = "";
                if (trpage == 1) {


                    url = Web_url_api.frndRequestUrlApi(uid);
                    responseString = WebServerCall.getDataFromServer(url);


                }
                else {

                    url = Web_url_api.frndRequestPaginationUrlApi(uid, page);
                    responseString = WebServerCall.getDataFromServer(url);

                }

                JSONObject jsonObj = new JSONObject(responseString);
                String successStr = jsonObj
                        .getString(Json_keys.SUCCESS);
                message = jsonObj
                        .getString(Json_keys.MESSAGE);
                if (successStr.equalsIgnoreCase("1")) {
                    frg_trns_report_lv.shouldAddFooterView = true;
                    JSONArray data_jarray = jsonObj.getJSONArray(Json_keys
                                                                         .DATA);
                    if (data_jarray != null) {

                        StringBuilder stringBuilder = new StringBuilder();
                        if (data_jarray.length() == 0) {
                            executedSuccefully = false;
                            frg_trns_report_lv.shouldLoadFurthur = false;
                        }
                        else {
                            for (int i = 0; i < data_jarray.length(); i++) {
                                JSONObject data_obj = data_jarray.getJSONObject(i);
                                tar_array.add(new FrndRequest(
                                                      data_obj.getString(Json_keys.MEMBERID),
                                                      data_obj.getString(Json_keys.PROFILEPIC),
                                                      data_obj.getString(Json_keys.MEMBERNAME),
                                                      data_obj.getString(Json_keys.CITY),
                                                      data_obj.getString(Json_keys.TIMEAGO)
                                              )
                                );

                            }
                            executedSuccefully = true;
                        }
                    }
                    else {
                        executedSuccefully = false;
                        frg_trns_report_lv.shouldLoadFurthur = false;

                    }


                }
                else {
                    executedSuccefully = false;
                    frg_trns_report_lv.shouldLoadFurthur = false;
                    frg_trns_report_lv.shouldAddFooterView = false;
                }

            }
            catch (Throwable e) {
                // TODO Auto-generated catch block
                executedSuccefully = false;
                frg_trns_report_lv.shouldLoadFurthur = false;
                message = e.getMessage();

            }
            // ////////////////////////
            return executedSuccefully;

        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (trpage == 1) {
                pdiDialog.dismiss();
            }

            if (result == true) {

                frg_trns_report_lv.addNewData(tar_array);


            }
            else {


                Methods.animRedTextMethod(getActivity(), message);

            }

        }

    }


}
