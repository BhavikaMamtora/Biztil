package com.d2d.biztil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.d2d.biztil.ExceptionHandler.ExceptionHandler;
import com.d2d.biztil.Helper.Constants;
import com.d2d.biztil.Helper.CustomProgressDialog;
import com.d2d.biztil.Helper.Methods;
import com.d2d.biztil.Helper.NetConnection;
import com.d2d.biztil.Helper.PrefsKeys;
import com.d2d.biztil.Webservice.Json_keys;
import com.d2d.biztil.Webservice.WebServerCall;
import com.d2d.biztil.Webservice.Web_url_api;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONObject;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private static NetConnection     internet;
    private static Boolean           netConnection;
    ImageView act_signup_back_iv, act_signup_twitter_iv, act_signup_fb_iv, act_signup_gplus_iv;
    RadioGroup act_signup_gender_rg;
    CheckBox act_signup_buyer_cb, act_signup_seller_cb, act_signup_servicepro_cb;
    RadioButton act_signup_gender_rb;
    MaterialEditText act_signup_name_edt, act_signup_mobileno_edt, act_sign_up_email_edt,
            act_signup_password_edt;
    Button act_signup_register_btn;
    private        SharedPreferences prefsPrivate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.act_signup);
        prefsPrivate = getSharedPreferences(
                Constants.PREFS_PRIVATE, Context.MODE_PRIVATE);
        initControl();
        controlEvents();

    }

    // variable initialization
    private void initControl() {
        act_signup_name_edt = (MaterialEditText) findViewById(R.id.act_signup_name_edt);
        act_signup_mobileno_edt = (MaterialEditText) findViewById(R.id.act_signup_mobileno_edt);
        act_sign_up_email_edt = (MaterialEditText) findViewById(R.id.act_sign_up_email_edt);
        act_signup_password_edt = (MaterialEditText) findViewById(R.id.act_signup_password_edt);

        act_signup_back_iv = (ImageView) findViewById(R.id.act_pinfo_back_iv);
        act_signup_twitter_iv = (ImageView) findViewById(R.id.act_signup_twitter_iv);
        act_signup_fb_iv = (ImageView) findViewById(R.id.act_signup_fb_iv);
        act_signup_gplus_iv = (ImageView) findViewById(R.id.act_signup_gplus_iv);

        act_signup_gender_rg = (RadioGroup) findViewById(R.id.act_signup_gender_rg);

        act_signup_buyer_cb = (CheckBox) findViewById(R.id.act_signup_buyer_cb);
        act_signup_seller_cb = (CheckBox) findViewById(R.id.act_signup_seller_cb);
        act_signup_servicepro_cb = (CheckBox) findViewById(R.id.act_signup_servicepro_cb);


        act_signup_register_btn = (Button) findViewById(R.id.act_signup_register_btn);


    }

    // control click
    private void controlEvents() {

        act_signup_register_btn.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        switch (id) {

            case R.id.act_signup_register_btn:
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(
                        getCurrentFocus()
                                .getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS
                );
                act_signup_name_edt.clearFocus();
                act_signup_mobileno_edt.clearFocus();
                act_sign_up_email_edt.clearFocus();
                act_signup_password_edt.clearFocus();

                act_signup_name_edt.requestFocus();
                act_signup_mobileno_edt.requestFocus();
                act_sign_up_email_edt.requestFocus();
                act_signup_password_edt.requestFocus();

                internet = new NetConnection(SignUpActivity.this);
                netConnection = internet.HaveNetworkConnection();

                if (netConnection == true) {

                    if (isValidForm() == true) {
                        new signupAsyncTask().execute();
                    }

                }
                else {
                    internet.showNetDialog(SignUpActivity.this);

                }

                break;


        }
    }

    private boolean isValidForm() {
        boolean isValidForm = true;

        if (Methods.isEmpty(act_sign_up_email_edt) == true
                && Methods.isEmpty(act_signup_password_edt) == true
                && Methods.isEmpty(act_signup_name_edt) == true
                && Methods.isEmpty(act_signup_mobileno_edt) == true) {

            Methods.animRedTextMethod(SignUpActivity.this, "Please Enter All Field");
            isValidForm = false;

        }

        else if (Methods.isEmpty(act_sign_up_email_edt) == true) {

            Methods.animRedTextMethod(SignUpActivity.this, "Please Enter Email");
            isValidForm = false;

        }
        else if (Methods.isValidEmail(act_sign_up_email_edt.getText().toString()) == true) {

            Methods.animRedTextMethod(SignUpActivity.this, "Please Enter Valid Email");
            isValidForm = false;

        }
        else if (Methods.isEmpty(act_signup_password_edt) == true) {

            Methods.animRedTextMethod(SignUpActivity.this, "Please Enter Password");
            isValidForm = false;

        }
        else if (Methods.isEmpty(act_signup_name_edt) == true) {

            Methods.animRedTextMethod(SignUpActivity.this, "Please Enter Name");
            isValidForm = false;

        }
        else if (Methods.isEmpty(act_signup_mobileno_edt) == true) {

            Methods.animRedTextMethod(SignUpActivity.this, "Please Enter Mobile No.");
            isValidForm = false;

        }
        else if (act_signup_buyer_cb.isChecked() == false && act_signup_seller_cb.isChecked() == false && act_signup_servicepro_cb.isChecked() == false)

        {
            Methods.animRedTextMethod(SignUpActivity.this, "Please Select Iam");
            isValidForm = false;
        }

        return isValidForm;
    }

    class signupAsyncTask extends AsyncTask<Void, Boolean, Boolean> {
        CustomProgressDialog pdiDialog;
        String               name, mobileno, email, pswrd, message;
        String check1,check2,check3,gender;
        String uid, username, iam, type, otp;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdiDialog = new CustomProgressDialog(SignUpActivity.this);
            pdiDialog.setMessage("Loading...");
            pdiDialog.setCancelable(false);
            pdiDialog.show();

            name = act_signup_name_edt.getText().toString();
            pswrd = act_signup_mobileno_edt.getText().toString();
            email = act_sign_up_email_edt.getText().toString();
            pswrd = act_signup_password_edt.getText().toString();
            if(act_signup_buyer_cb.isChecked())
            {
                check1 = "Buyer";
            }
            else
            {
                check1 = "";
            }

            if(act_signup_seller_cb.isChecked())
            {
                check2 = "Seller";
            }
            else
            {
                check2 = "";
            }

            if(act_signup_servicepro_cb.isChecked())
            {
                check3 = "Service Provider";
            }
            else
            {
                check3 = "";
            }
            int selectedId = act_signup_gender_rg.getCheckedRadioButtonId();
            act_signup_gender_rb = (RadioButton) findViewById(selectedId);
            gender = act_signup_gender_rb.getText().toString().toLowerCase();
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


                String url            = Web_url_api.signupUrlApi(name, email, mobileno, gender,
                                                                 pswrd, check1, check2,check3);
                String responseString = WebServerCall.getDataFromServer(url);
                Log.e("responseString", responseString);
                JSONObject jsonObj = new JSONObject(responseString);
                String successStr = jsonObj
                        .getString(Json_keys.SUCCESS);
                message = jsonObj
                        .getString(Json_keys.MESSAGE);
                if (successStr.equalsIgnoreCase("1")) {
                    executedSuccefully = true;
                    JSONArray  data_jarray = jsonObj.getJSONArray(Json_keys.DATA);
                    JSONObject data_obj    = data_jarray.getJSONObject(0);
                    uid = data_obj.getString(Json_keys.UID);
                    iam = data_obj.getString(Json_keys.IAM);
                    type = data_obj.getString(Json_keys.TYPE);
                    otp = data_obj.getString(Json_keys.OTP);
                    username = data_obj.getString(Json_keys.USERNAME);


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

                prefsPrivate = getSharedPreferences(
                        Constants.PREFS_PRIVATE,
                        Context.MODE_PRIVATE
                );

                SharedPreferences.Editor prefsPrivateEdit = prefsPrivate.edit();

                prefsPrivateEdit.putString(PrefsKeys.Login_Prefs_Keys.IAM, iam
                );
                prefsPrivateEdit.putString(
                        PrefsKeys.Login_Prefs_Keys.TYPE,
                        type
                );

                prefsPrivateEdit.putString(
                        PrefsKeys.Login_Prefs_Keys.UID,
                        uid
                );
                prefsPrivateEdit.putString(
                        PrefsKeys.Login_Prefs_Keys.OTP,
                        otp
                );

                prefsPrivateEdit.putString(
                        PrefsKeys.Login_Prefs_Keys.USERNAME,
                        username
                );

                prefsPrivateEdit.putBoolean(
                        PrefsKeys.Login_Prefs_Keys.LOGIN_FLAG,
                        true
                );


                prefsPrivateEdit.commit();
                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                finish();


            }
            else {
                Methods.animRedTextMethod(SignUpActivity.this, message);
            }

        }

    }


}
