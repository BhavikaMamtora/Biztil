package com.d2d.biztil.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.d2d.biztil.Helper.Constants;
import com.d2d.biztil.Helper.CustomProgressDialog;
import com.d2d.biztil.Helper.Methods;
import com.d2d.biztil.Helper.NetConnection;
import com.d2d.biztil.Helper.PrefsKeys;
import com.d2d.biztil.MainActivity;
import com.d2d.biztil.Model.BCategory;
import com.d2d.biztil.Model.SubCategory;
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
public class SubCategoryListingFragment extends Fragment {


    private static NetConnection internet;
    private static Boolean       netConnection;
    View     v;
    TextView sub_catg_list_title_tv;
    ListView sub_catg_list_title_lv;
    ArrayList<SubCategory> sub_catg_list = new ArrayList<SubCategory>();
    CustomAdapter adapter;
    private SharedPreferences prefsPrivate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_subcatg_listing, null);

        prefsPrivate = getActivity().getSharedPreferences(
                Constants.PREFS_PRIVATE, Context.MODE_PRIVATE);

        getActivity().setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        initControl();
        internet = new NetConnection(getActivity());
        netConnection = internet.HaveNetworkConnection();

        if (netConnection == true) {

            new subCategoryAsyncTask().execute();
        }
        else {
            internet.showNetDialog(getActivity());

        }


        return v;
    }

    private void initControl() {

        sub_catg_list_title_lv = (ListView) v.findViewById(R.id
                                                                   .sub_catg_list_title_lv);
        sub_catg_list_title_tv = (TextView)v.findViewById(R.id.sub_catg_list_title_tv);
        String catg_name = prefsPrivate.getString(PrefsKeys.Login_Prefs_Keys.CATEGORY_NAME, "");
        sub_catg_list_title_tv.setText(catg_name);




    }

    public class CustomAdapter extends BaseAdapter {
        public LayoutInflater inflater = null;
        ArrayList<SubCategory> tr_array;
        Context                context;

        public CustomAdapter(Activity mainActivity, ArrayList<SubCategory> tr_array) {
            // TODO Auto-generated constructor stub
            this.tr_array = tr_array;
            context = mainActivity;
            inflater = (LayoutInflater) context.
                                                       getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return tr_array.size();
        }

        @Override
        public SubCategory getItem(int position) {
            // TODO Auto-generated method stub
            return tr_array.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            Holder holder = new Holder();
            View   rowView;
            rowView = inflater.inflate(R.layout.sub_category_list_item, null);
            holder.catg_list_tv = (TextView) rowView.findViewById(R.id.catg_list_tv);

            holder.catg_list_tv.setText(tr_array.get(position).getName());


            return rowView;
        }

        public class Holder {
            TextView catg_list_tv;

        }

    }

    class subCategoryAsyncTask extends AsyncTask<Void, Boolean, Boolean> {
        CustomProgressDialog pdiDialog;
        String               message;
        String               catg_id;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdiDialog = new CustomProgressDialog(getActivity());
            pdiDialog.setMessage("Loading...");
            pdiDialog.setCancelable(false);
            pdiDialog.show();
            catg_id = prefsPrivate.getString(PrefsKeys.Login_Prefs_Keys.CATEGORY_ID, "0");

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

            // email=test@gmail.com&password=121212&restaurant_code=KAMPONG&device_type=1
            // &device_token=1111111111111

            try {


                String url            = Web_url_api.subCategoriesUrlApi(catg_id);
                String responseString = WebServerCall.getDataFromServer(url);
                Log.e("responseString", responseString);
                JSONObject jsonObj = new JSONObject(responseString);
                String successStr = jsonObj
                        .getString(Json_keys.SUCCESS);
                message = jsonObj
                        .getString(Json_keys.MESSAGE);
                if (successStr.equalsIgnoreCase("1")) {
                    executedSuccefully = true;

                    JSONArray data_jarray = jsonObj.getJSONArray(Json_keys.DATA);
                    for (int i = 0; i < data_jarray.length(); i++) {
                        JSONObject data_obj = data_jarray.getJSONObject(i);
                        sub_catg_list.add(new SubCategory(
                                data_obj.getString(Json_keys.CATEGORYID)
                                , data_obj.getString(Json_keys.SUBCATEGORYID),
                                data_obj.getString(Json_keys.NAME)
                        ));

                    }

                }
                else {
                    executedSuccefully = false;
                }

            }
            catch (Throwable e) {
                // TODO Auto-generated catch block
                executedSuccefully = false;
                message = e.getMessage();

            }
            // ////////////////////////
            return executedSuccefully;

        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            pdiDialog.dismiss();
            if (result == true) {

                adapter = new CustomAdapter(getActivity(), sub_catg_list);

                // Assign adapter to ListView
                sub_catg_list_title_lv.setAdapter(adapter);

                // ListView Item Click Listener
                sub_catg_list_title_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {

                        // ListView Clicked item index
                        int itemPosition = position;

                        // ListView Clicked item value
                        SubCategory itemValue = (SubCategory) adapter.getItem(position);

                        // Show Alert


                    }

                });

                // setting list adapter
                adapter.notifyDataSetChanged();


            }
            else {
                Methods.animRedTextMethod(getActivity(), message);
            }

        }

    }


}
