package com.d2d.biztil;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.d2d.biztil.ExceptionHandler.ExceptionHandler;
import com.d2d.biztil.Helper.Constants;
import com.d2d.biztil.Helper.CustomProgressDialog;
import com.d2d.biztil.Helper.DataBaseHelper;
import com.d2d.biztil.Helper.Methods;
import com.d2d.biztil.Helper.NetConnection;
import com.d2d.biztil.Helper.PrefsKeys;
import com.d2d.biztil.Webservice.Json_keys;
import com.d2d.biztil.Webservice.WebServerCall;
import com.d2d.biztil.Webservice.Web_url_api;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class PersonalInfoActivity extends AppCompatActivity implements View.OnClickListener {


    private static NetConnection internet;
    private static Boolean       netConnection;
    Button act_pinfo_country_btn, act_pinfo_state_btn, act_pinfo_city_btn, act_pinfo_btn,
            act_pinfo_type_day_spn, act_pinfo_type_month_spn, act_pinfo_type_year_spn;
    String            days_list[]                 = new String[31];
    String            months_list[]               = new String[12];
    ArrayList<String> years_list                  = new ArrayList<String>();
    DataBaseHelper    dbhelper                    = new DataBaseHelper(this);
    ArrayList<String> personal_country_list_names = new ArrayList<String>();
    ArrayList<String> personal_states_list_names  = new ArrayList<String>();
    ArrayList<String> personal_city_list_names    = new ArrayList<String>();
    private SharedPreferences prefsPrivate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.act_personal_ifo);
        prefsPrivate = getSharedPreferences(
                Constants.PREFS_PRIVATE, Context.MODE_PRIVATE);
        initControl();
        controlEvents();

        try {
            dbhelper = new DataBaseHelper(this);
            dbhelper.copyDataBase();
            dbhelper.openDatabase();
            SQLiteDatabase db = dbhelper.getWritableDatabase();
            fetchCountryFromDB();
            dbhelper.closeDataBase();
        }
        catch (Exception e) {
            System.out.println("[onCreate] Problem in onCreate...!!!");
            e.printStackTrace();
        }

    }

    // variable initialization
    private void initControl() {
        act_pinfo_country_btn = (Button) findViewById(R.id.act_pinfo_country_btn);
        act_pinfo_state_btn = (Button) findViewById(R.id.act_pinfo_state_btn);
        act_pinfo_city_btn = (Button) findViewById(R.id.act_pinfo_city_btn);
        act_pinfo_btn = (Button) findViewById(R.id.act_pinfo_btn);

        act_pinfo_type_day_spn = (Button) findViewById(R.id.act_pinfo_type_day_spn);
        act_pinfo_type_month_spn = (Button) findViewById(R.id.act_pinfo_type_month_spn);
        act_pinfo_type_year_spn = (Button) findViewById(R.id.act_pinfo_type_year_spn);
    }

    // control click
    private void controlEvents() {

        act_pinfo_btn.setOnClickListener(this);
        act_pinfo_country_btn.setOnClickListener(this);
        act_pinfo_state_btn.setOnClickListener(this);
        act_pinfo_city_btn.setOnClickListener(this);

        act_pinfo_type_day_spn.setOnClickListener(this);
        act_pinfo_type_month_spn.setOnClickListener(this);
        act_pinfo_type_year_spn.setOnClickListener(this);

        String type = prefsPrivate.getString(PrefsKeys.Login_Prefs_Keys.TYPE,"Buyer" );
        if(type.equalsIgnoreCase("Buyer"))
        {
            act_pinfo_btn.setText("Finish");
        }
        else
        {
            act_pinfo_btn.setText("Next");
        }


        //Days
        for (int i = 1; i <= 31; i++) {
            days_list[i - 1] = ("" + i);
        }
        //Days

        //Months
        for (int i = 1; i <= 12; i++) {
            months_list[i - 1] = ("" + i);
        }
        //Months

        //Years
        Calendar calendar = Calendar.getInstance();
        int      year     = calendar.get(Calendar.YEAR);
        for (int i = 1950; i <= year; i++) {
            years_list.add("" + i);
        }
        //Years
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        switch (id) {

            case R.id.act_pinfo_btn:
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(
                        getCurrentFocus()
                                .getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS
                );

                internet = new NetConnection(PersonalInfoActivity.this);
                netConnection = internet.HaveNetworkConnection();

                if (netConnection == true) {

                    if (isValidForm() == true) {
                        new personalInfoAsyncTask().execute();
                    }

                }
                else {
                    internet.showNetDialog(PersonalInfoActivity.this);

                }

                break;
            case R.id.act_pinfo_country_btn:

                final Dialog dialogCountry;
                AlertDialog.Builder builderCountry = new AlertDialog.Builder(
                        new ContextThemeWrapper(this, android.R.style.Theme_Holo_Light_Dialog));
                builderCountry.setTitle("Select Country");
                ArrayAdapter c_adapter = new ArrayAdapter(this, android.R.layout
                        .select_dialog_item, personal_country_list_names);
                builderCountry.setAdapter(
                        c_adapter,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                // Do something
                                act_pinfo_country_btn.setText(personal_country_list_names.get
                                        (arg1));
                                act_pinfo_state_btn.setText("State");
                                act_pinfo_city_btn.setText("City");
                                personal_states_list_names.clear();
                                personal_city_list_names.clear();
                                dbhelper.openDatabase();
                                fetchStatesFromDB();
                                dbhelper.closeDataBase();
                                arg0.dismiss();
                            }
                        }
                );
                dialogCountry = builderCountry.create();
                dialogCountry.setCancelable(false);
                dialogCountry.show();

                break;

            case R.id.act_pinfo_state_btn:

                if (personal_states_list_names.size() > 0) {
                    final Dialog        dialogState;
                    AlertDialog.Builder builderState = new AlertDialog.Builder(
                            new ContextThemeWrapper(this, android.R.style.Theme_Holo_Light_Dialog));
                    builderState.setTitle("Select State");

                    ArrayAdapter s_adapter = new ArrayAdapter(this, android.R.layout
                            .select_dialog_item, personal_states_list_names);
                    builderState.setAdapter(
                            s_adapter,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    // Do something
                                    act_pinfo_state_btn.setText(personal_states_list_names.get
                                            (arg1));
                                    act_pinfo_city_btn.setText("City");

                                    dbhelper.openDatabase();
                                    fetchCityFromDB();
                                    dbhelper.closeDataBase();
                                    arg0.dismiss();
                                }
                            }
                    );

                    dialogState = builderState.create();
                    dialogState.setCancelable(false);
                    dialogState.show();
                }
                break;

            case R.id.act_pinfo_city_btn:

                if (personal_city_list_names.size() > 0) {
                    final Dialog        dialogCity;
                    AlertDialog.Builder builderCity = new AlertDialog.Builder(
                            new ContextThemeWrapper(this, android.R.style.Theme_Holo_Light_Dialog));
                    builderCity.setTitle("Select City");
                    ArrayAdapter cty_adapter = new ArrayAdapter(this, android.R.layout
                            .select_dialog_item, personal_city_list_names);
                    builderCity.setAdapter(
                            cty_adapter,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    // Do something
                                    act_pinfo_city_btn.setText(personal_city_list_names.get(arg1));
                                    arg0.dismiss();
                                }
                            }
                    );

                    dialogCity = builderCity.create();
                    dialogCity.setCancelable(false);
                    dialogCity.show();
                }
                break;

            case R.id.act_pinfo_type_day_spn:

                final Dialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        new ContextThemeWrapper(this, android.R.style.Theme_Holo_Light_Dialog));;
                builder.setTitle("Select Day");
                ArrayAdapter day_adapter = new ArrayAdapter(this, android.R.layout
                        .select_dialog_item, days_list);
                builder.setAdapter(
                        day_adapter,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                // Do something
                                act_pinfo_type_day_spn.setText(days_list[arg1]);
                                arg0.dismiss();
                            }
                        }
                );
                dialog = builder.create();
                dialog.setCancelable(false);
                dialog.show();

                break;

            case R.id.act_pinfo_type_month_spn:
                final Dialog dialog1;
                AlertDialog.Builder builder1 = new AlertDialog.Builder(
                        new ContextThemeWrapper(this, android.R.style.Theme_Holo_Light_Dialog));
                builder1.setTitle("Select Month");

                ArrayAdapter mth_adapter = new ArrayAdapter(this, android.R.layout
                        .select_dialog_item, months_list);
                builder1.setAdapter(
                        mth_adapter,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                // Do something
                                act_pinfo_type_month_spn.setText(months_list[arg1]);
                                arg0.dismiss();
                            }
                        }
                );

                dialog1 = builder1.create();
                dialog1.setCancelable(false);
                dialog1.show();
                break;

            case R.id.act_pinfo_type_year_spn:
                final Dialog dialog2;
                AlertDialog.Builder builder2 = new AlertDialog.Builder(
                        new ContextThemeWrapper(this, android.R.style.Theme_Holo_Light_Dialog));
                builder2.setTitle("Select Year");
                ArrayAdapter year_adapter = new ArrayAdapter(this, android.R.layout
                        .select_dialog_item, years_list);
                builder2.setAdapter(
                        year_adapter,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                // Do something
                                act_pinfo_type_year_spn.setText(years_list.get(arg1));
                                arg0.dismiss();
                            }
                        }
                );

                dialog2 = builder2.create();
                dialog2.setCancelable(false);
                dialog2.show();
                break;
        }
    }

    private boolean isValidForm() {
        boolean isValidForm = true;
        // act_register_edtusername, act_register_edtpassword

//        if (Methods.isEmpty(act_login_email_edt) == true
//                && Methods.isEmpty(act_login_password_edt) == true) {
//
//            Methods.animRedTextMethod(PersonalInfoActivity.this, "Please Enter All Field");
//            isValidForm = false;
//
//        }
//
//        else if (Methods.isEmpty(act_login_email_edt) == true) {
//
//            Methods.animRedTextMethod(PersonalInfoActivity.this, "Please Enter Email");
//            isValidForm = false;
//
//        }
//        else if (Methods.isValidEmail(act_login_email_edt.getText().toString()) == true) {
//
//            Methods.animRedTextMethod(PersonalInfoActivity.this, "Please Enter Valid Email");
//            isValidForm = false;
//
//        }
//        else if (Methods.isEmpty(act_login_password_edt) == true) {
//
//            Methods.animRedTextMethod(PersonalInfoActivity.this, "Please Enter Password");
//            isValidForm = false;
//
//        }

        return isValidForm;
    }

    private void fetchCountryFromDB() {
        Cursor cur;
        //DataBaseHelper dbhelper = new DataBaseHelper(getApplicationContext());
        SQLiteDatabase db1 = dbhelper.getWritableDatabase();
        cur = db1.rawQuery("select CountryId, CountryName from country order by CountryId", null);

        if (cur.moveToFirst()) {
            //personal_country_list.clear();
            personal_country_list_names.clear();
            do {
                //System.out.println("cur.getInt(0) : " + cur.getInt(0));
                //System.out.println("cur.getString(1) : " + cur.getString(1));
                //personal_country_list.add(new PCSC(cur.getInt(0), cur.getString(1)));
                personal_country_list_names.add(cur.getString(1));
            } while (cur.moveToNext());
        }
    }

    private void fetchStatesFromDB() {
        Cursor cur;
        //DataBaseHelper dbhelper = new DataBaseHelper(getApplicationContext());
        SQLiteDatabase db1 = dbhelper.getWritableDatabase();
        String query = "select StateId, StateName from state " +
                "where CountryId = (select CountryId from country " +
                "where CountryName = '" +
                act_pinfo_country_btn.getText().toString().trim() + "')";
        cur = db1.rawQuery(query, null);
        System.out.println("[fetchStatesFromDB] States - cur.getCount() : " + cur.getCount());

        if (cur.moveToFirst()) {
            personal_states_list_names.clear();
            do {
                //System.out.println("cur.getInt(0) : " + cur.getInt(0));
                //System.out.println("cur.getString(1) : " + cur.getString(1));
                //personal_country_list.add(new PCSC(cur.getInt(0), cur.getString(1)));
                personal_states_list_names.add(cur.getString(1));
            } while (cur.moveToNext());
        }
    }

    private void fetchCityFromDB() {
        try {
            Cursor cur;
            //DataBaseHelper dbhelper = new DataBaseHelper(getApplicationContext());
            SQLiteDatabase db2 = dbhelper.getWritableDatabase();
            String query = "select id, cityname from city " +
                    "where stateid = (select StateId from state " +
                    "where StateName = '" + act_pinfo_state_btn.getText().toString().trim() + "')";
            System.out.println("[fetchCityFromDB] Query to fetch city from selected state : " +
                                       query);
            Log.d("fetchCityFromDB-City : ", query);
            cur = db2.rawQuery(query, null);
            System.out.println("[fetchCityFromDB] City - cur.getCount() : " + cur.getCount());

            if (cur.moveToFirst()) {
                personal_city_list_names.clear();
                do {
                    System.out.println("cur.getInt(0) : " + cur.getInt(0));
                    System.out.println("cur.getString(1) : " + cur.getString(1));
                    //personal_country_list.add(new PCSC(cur.getInt(0), cur.getString(1)));
                    personal_city_list_names.add(cur.getString(1));
                } while (cur.moveToNext());
            }
        }
        catch (Exception e) {
            System.out.println("[fetchCityFromDB] Problem in fetching city from DB...!!!");
            e.printStackTrace();
        }
    }

    private String countryIdFromDB(String country_name) {
        Cursor cur;
        SQLiteDatabase db1 = dbhelper.getWritableDatabase();
        cur = db1.rawQuery("select CountryId from country where CountryName = '"+country_name+"'", null);

        if (cur.moveToFirst()) {
            do {

               return  cur.getString(1);
            } while (cur.moveToNext());
        }
        return "";
    }

    private String stateIdFromDB(String state_name) {
        Cursor cur;
        SQLiteDatabase db1 = dbhelper.getWritableDatabase();
        cur = db1.rawQuery("select stateid from state where StateName = '"+state_name+"'", null);

        if (cur.moveToFirst()) {
            do {

                return  cur.getString(1);
            } while (cur.moveToNext());
        }
        return "";
    }

    private String cityIdFromDB(String city_name) {
        Cursor cur;
        SQLiteDatabase db1 = dbhelper.getWritableDatabase();
        cur = db1.rawQuery("select id from city where cityname = '"+city_name+"'", null);

        if (cur.moveToFirst()) {
            do {

                return  cur.getString(1);
            } while (cur.moveToNext());
        }
        return "";
    }




    class personalInfoAsyncTask extends AsyncTask<Void, Boolean, Boolean> {
        CustomProgressDialog pdiDialog;
        String               country, state, city, message;
        String uid, day, month, year;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdiDialog = new CustomProgressDialog(PersonalInfoActivity.this);
            pdiDialog.setMessage("Loading...");
            pdiDialog.setCancelable(false);
            pdiDialog.show();

            country = countryIdFromDB(act_pinfo_country_btn.getText().toString());
            state = stateIdFromDB(act_pinfo_state_btn.getText().toString());
            city = cityIdFromDB(act_pinfo_city_btn.getText().toString());
            day = act_pinfo_type_day_spn.getText().toString();
            month = act_pinfo_type_month_spn.getText().toString();
            year = act_pinfo_type_year_spn.getText().toString();
            uid = prefsPrivate.getString(PrefsKeys.Login_Prefs_Keys.UID, "");
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

            //https://biztil.com/android/savepersonalinfo
            // .php?country=105&state=1104&city=80&day=16&month=08&year=1988&uid=2

            try {


                String url = Web_url_api.personalInfoUrlApi(country, state, city, day,
                                                            month, year, uid
                );
                String responseString = WebServerCall.getDataFromServer(url);
                Log.e("responseString", responseString);
                JSONObject jsonObj = new JSONObject(responseString);
                String successStr = jsonObj
                        .getString(Json_keys.SUCCESS);
                message = jsonObj
                        .getString(Json_keys.MESSAGE);
                if (successStr.equalsIgnoreCase("1")) {
                    executedSuccefully = true;

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

               String type = prefsPrivate.getString(PrefsKeys.Login_Prefs_Keys.TYPE,"Buyer" );
                if(type.equalsIgnoreCase("Buyer"))
                {
                    startActivity(new Intent(PersonalInfoActivity.this, MainActivity.class));
                    finish();
                }
                else
                {
                    startActivity(new Intent(PersonalInfoActivity.this, BusinessInfoActivity.class));
                    finish();
                }



            }
            else {
                Methods.animRedTextMethod(PersonalInfoActivity.this, message);
            }

        }

    }
}
