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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private SharedPreferences prefsPrivate;
    private static NetConnection internet;
    private static Boolean       netConnection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.act_main);
        prefsPrivate = getSharedPreferences(
                Constants.PREFS_PRIVATE, Context.MODE_PRIVATE);
        initControl();
        controlEvents();

    }

    // variable initialization
    private void initControl() {

    }

    // control click
    private void controlEvents() {




    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        switch (id) {

        }
    }


}
