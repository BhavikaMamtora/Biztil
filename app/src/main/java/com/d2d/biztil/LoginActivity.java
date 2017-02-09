package com.d2d.biztil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    MaterialEditText act_login_email_edt, act_login_password_edt;
    TextInputLayout etPasswordLayout;
    Button          act_login_btn;
    TextView        act_login_forgot_pass_tv, act_login_new_acc_tv;
    private SharedPreferences prefsPrivate;
    private static NetConnection internet;
    private static Boolean       netConnection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.act_login);
        prefsPrivate = getSharedPreferences(
                Constants.PREFS_PRIVATE, Context.MODE_PRIVATE);
        initControl();
        controlEvents();

    }

    // variable initialization
    private void initControl() {
        act_login_email_edt = (MaterialEditText) findViewById(R.id.act_login_email_edt);
        act_login_password_edt = (MaterialEditText) findViewById(R.id.act_login_password_edt);

        etPasswordLayout = (TextInputLayout) findViewById(R.id.etPasswordLayout);

        act_login_btn = (Button) findViewById(R.id.act_login_btn);

        act_login_forgot_pass_tv = (TextView) findViewById(R.id.act_login_forgot_pass_tv);
        act_login_new_acc_tv = (TextView) findViewById(R.id.act_login_new_acc_tv);

    }

    // control click
    private void controlEvents() {

        act_login_btn.setOnClickListener(this);
        act_login_new_acc_tv.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        switch (id) {

            case R.id.act_login_btn:
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus()
                                                             .getWindowToken(),
                                                     InputMethodManager.HIDE_NOT_ALWAYS);
                act_login_email_edt.clearFocus();
                act_login_password_edt.clearFocus();

                act_login_email_edt.requestFocus();
                act_login_password_edt.requestFocus();

                internet = new NetConnection(LoginActivity.this);
                netConnection = internet.HaveNetworkConnection();

                if (netConnection == true) {

                    if (isValidForm() == true) {
                        new loginAsyncTask().execute();
                    }

                }
                else {
                    internet.showNetDialog(LoginActivity.this);

                }

                break;

            case R.id.act_login_new_acc_tv:
                startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
                break;


        }
    }

    class loginAsyncTask extends AsyncTask<Void, Boolean, Boolean> {
        CustomProgressDialog pdiDialog;
        String               email, pswrd, message;
        String uid, username, iam, type, otp;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdiDialog = new CustomProgressDialog(LoginActivity.this);
            pdiDialog.setMessage("Loading...");
            pdiDialog.setCancelable(false);
            pdiDialog.show();

            email = act_login_email_edt.getText().toString();
            pswrd = act_login_password_edt.getText().toString();
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


                String url            = Web_url_api.loginUrlApi(email, pswrd);
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
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();


            }
            else {
                Methods.animRedTextMethod(LoginActivity.this, message);
            }

        }

    }

    private
    boolean isValidForm() {
        boolean isValidForm = true;
        // act_register_edtusername, act_register_edtpassword

        if (Methods.isEmpty(act_login_email_edt) == true
                && Methods.isEmpty(act_login_password_edt) == true) {

            Methods.animRedTextMethod(LoginActivity.this, "Please Enter All Field");
            isValidForm = false;

        }

        else if (Methods.isEmpty(act_login_email_edt) == true) {

            Methods.animRedTextMethod(LoginActivity.this, "Please Enter Email");
            isValidForm = false;

        }
        else if (Methods.isValidEmail(act_login_email_edt.getText().toString()) == true) {

            Methods.animRedTextMethod(LoginActivity.this, "Please Enter Valid Email");
            isValidForm = false;

        }
        else if (Methods.isEmpty(act_login_password_edt) == true) {

            Methods.animRedTextMethod(LoginActivity.this, "Please Enter Password");
            isValidForm = false;

        }

        return isValidForm;
    }




}
