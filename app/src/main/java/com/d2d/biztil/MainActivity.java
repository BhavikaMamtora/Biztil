package com.d2d.biztil;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.d2d.biztil.ExceptionHandler.ExceptionHandler;
import com.d2d.biztil.Fragments.FrndReuestListingFragment;
import com.d2d.biztil.Fragments.MemberListingFragment;
import com.d2d.biztil.Fragments.SubCategoryListingFragment;
import com.d2d.biztil.Helper.Constants;
import com.d2d.biztil.Helper.CustomProgressDialog;
import com.d2d.biztil.Helper.Methods;
import com.d2d.biztil.Helper.NetConnection;
import com.d2d.biztil.Helper.PrefsKeys;
import com.d2d.biztil.Model.BCategory;
import com.d2d.biztil.Webservice.Json_keys;
import com.d2d.biztil.Webservice.WebServerCall;
import com.d2d.biztil.Webservice.Web_url_api;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private static NetConnection internet;
    private static Boolean       netConnection;
    Toolbar                                      toolbar;
    android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;
    Fragment                                     fragment;
    ImageView                                    left_drawer_icon, business_icon, fun_icon,
            member_icon, offer_icon, right_drawer_icon;
    ImageView frnd_request_icon, message_icon, plus_icon, notification_icon, opportunity_icon;
    LinearLayout plus_layout;
    EditText     search_edt;
    ImageView    all_frnd_icon;
    ArrayList<BCategory> business_catg_list = new ArrayList<BCategory>();
    private SharedPreferences prefsPrivate;
    private DrawerLayout      mDrawerLayout;
    private ListView          mDrawerLeftList;
    CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.act_main);
        prefsPrivate = getSharedPreferences(
                Constants.PREFS_PRIVATE, Context.MODE_PRIVATE);
        initControl();
        controlEvents();
        internet = new NetConnection(MainActivity.this);
        netConnection = internet.HaveNetworkConnection();

        if (netConnection == true) {


            new businessCategoryAsyncTask().execute();
        }
        else {
            internet.showNetDialog(MainActivity.this);

        }

        drawer_setup(savedInstanceState);


    }

    // variable initialization
    private void initControl() {


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerLeftList = (ListView) findViewById(R.id.left_drawer);

        left_drawer_icon = (ImageView) findViewById(R.id.left_drawer_icon);
        business_icon = (ImageView) findViewById(R.id.business_icon);
        fun_icon = (ImageView) findViewById(R.id.fun_icon);
        member_icon = (ImageView) findViewById(R.id.member_icon);
        offer_icon = (ImageView) findViewById(R.id.offer_icon);
        right_drawer_icon = (ImageView) findViewById(R.id.right_drawer_icon);

        frnd_request_icon = (ImageView) findViewById(R.id.frnd_request_icon);
        message_icon = (ImageView) findViewById(R.id.message_icon);
        plus_icon = (ImageView) findViewById(R.id.plus_icon);
        notification_icon = (ImageView) findViewById(R.id.notification_icon);
        opportunity_icon = (ImageView) findViewById(R.id.opportunity_icon);

        all_frnd_icon = (ImageView) findViewById(R.id.all_frnd_icon);

        plus_layout = (LinearLayout) findViewById(R.id.plus_layout);
        search_edt = (EditText) findViewById(R.id.search_edt);


    }

    // control click
    private void controlEvents() {


        left_drawer_icon.setOnClickListener(this);
        business_icon.setOnClickListener(this);
        fun_icon.setOnClickListener(this);
        member_icon.setOnClickListener(this);
        offer_icon.setOnClickListener(this);
        right_drawer_icon.setOnClickListener(this);
        frnd_request_icon.setOnClickListener(this);
        message_icon.setOnClickListener(this);
        plus_icon.setOnClickListener(this);
        notification_icon.setOnClickListener(this);
        opportunity_icon.setOnClickListener(this);
        all_frnd_icon.setOnClickListener(this);
        plus_layout.setOnClickListener(this);


    }

    // Drawer Setup
    public void drawer_setup(Bundle savedInstanceState) {
        setupToolbar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);


        if (savedInstanceState == null) {

            // fragment = (Fragment) getSupportFragmentManager().getFragments().get(0);
        }


    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setupLeftDrawerToggle();
    }


    void setupLeftDrawerToggle() {


        // Defined Array values to show in ListView


        adapter = new CustomAdapter(MainActivity.this, business_catg_list);

        // Assign adapter to ListView
        mDrawerLeftList.setAdapter(adapter);

        // ListView Item Click Listener
        mDrawerLeftList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                BCategory itemValue = (BCategory) adapter.getItem(position);

                // Show Alert
                SharedPreferences.Editor prefsPrivateEdit = prefsPrivate.edit();

                prefsPrivateEdit.putString(PrefsKeys.Login_Prefs_Keys.CATEGORY_ID, itemValue
                        .getCategoryid()
                );
                prefsPrivateEdit.putString(PrefsKeys.Login_Prefs_Keys.CATEGORY_NAME, itemValue
                        .getName()
                );
                prefsPrivateEdit.commit();
                mDrawerLayout.closeDrawer(mDrawerLeftList);
                selectItem(new SubCategoryListingFragment());

            }

        });

        // setting list adapter
        adapter.notifyDataSetChanged();

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this, mDrawerLayout,
                                                                         toolbar, R.string
                                                                                 .app_name, R
                                                                                 .string.app_name
        ){

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                member_icon.setImageResource(R.mipmap.member_friend);
                left_drawer_icon.setImageResource(R.mipmap.cat1_selected);
                business_icon.setImageResource(R.mipmap.business);
                fun_icon.setImageResource(R.mipmap.fun);
                offer_icon.setImageResource(R.mipmap.offer);
                right_drawer_icon.setImageResource(R.mipmap.cat2);
                frnd_request_icon.setImageResource(R.mipmap.member_friend);
                message_icon.setImageResource(R.mipmap.ene);
                notification_icon.setImageResource(R.mipmap.bell);
                opportunity_icon.setImageResource(R.mipmap.oppo);
                super.onDrawerSlide(drawerView, slideOffset);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        //This is necessary to change the icon of the Drawer Toggle upon state change.
        mDrawerToggle.setDrawerIndicatorEnabled(false); //disable "hamburger to arrow" drawable
        mDrawerToggle.syncState();




    }


    @Override
    public void onClick(View view) {


        int id = view.getId();
        switch (id) {

            case R.id.left_drawer_icon:

adapter.notifyDataSetChanged();
                if (mDrawerLayout.isDrawerOpen(mDrawerLeftList)) {
                    mDrawerLayout.closeDrawer(mDrawerLeftList);

                }
                else {
                    mDrawerLayout.openDrawer(mDrawerLeftList);
                }

                member_icon.setImageResource(R.mipmap.member_friend);
                left_drawer_icon.setImageResource(R.mipmap.cat1_selected);
                business_icon.setImageResource(R.mipmap.business);
                fun_icon.setImageResource(R.mipmap.fun);
                offer_icon.setImageResource(R.mipmap.offer);
                right_drawer_icon.setImageResource(R.mipmap.cat2);
                frnd_request_icon.setImageResource(R.mipmap.member_friend);
                message_icon.setImageResource(R.mipmap.ene);
                notification_icon.setImageResource(R.mipmap.bell);
                opportunity_icon.setImageResource(R.mipmap.oppo);

                break;
            case R.id.business_icon:
                member_icon.setImageResource(R.mipmap.member_friend);
                left_drawer_icon.setImageResource(R.mipmap.cat1);
                business_icon.setImageResource(R.mipmap.business_selected);
                fun_icon.setImageResource(R.mipmap.fun);
                offer_icon.setImageResource(R.mipmap.offer);
                right_drawer_icon.setImageResource(R.mipmap.cat2);
                frnd_request_icon.setImageResource(R.mipmap.member_friend);
                message_icon.setImageResource(R.mipmap.ene);
                notification_icon.setImageResource(R.mipmap.bell);
                opportunity_icon.setImageResource(R.mipmap.oppo);

                break;
            case R.id.fun_icon:

                member_icon.setImageResource(R.mipmap.member_friend);
                left_drawer_icon.setImageResource(R.mipmap.cat1);
                business_icon.setImageResource(R.mipmap.business);
                fun_icon.setImageResource(R.mipmap.fun_selected);
                offer_icon.setImageResource(R.mipmap.offer);
                right_drawer_icon.setImageResource(R.mipmap.cat2);
                frnd_request_icon.setImageResource(R.mipmap.member_friend);
                message_icon.setImageResource(R.mipmap.ene);
                notification_icon.setImageResource(R.mipmap.bell);
                opportunity_icon.setImageResource(R.mipmap.oppo);

                break;
            case R.id.member_icon:
                selectItem(new MemberListingFragment());
                member_icon.setImageResource(R.mipmap.member_selected);
                left_drawer_icon.setImageResource(R.mipmap.cat1);
                business_icon.setImageResource(R.mipmap.business);
                fun_icon.setImageResource(R.mipmap.fun);
                offer_icon.setImageResource(R.mipmap.offer);
                right_drawer_icon.setImageResource(R.mipmap.cat2);
                frnd_request_icon.setImageResource(R.mipmap.member_friend);
                message_icon.setImageResource(R.mipmap.ene);
                notification_icon.setImageResource(R.mipmap.bell);
                opportunity_icon.setImageResource(R.mipmap.oppo);

                break;
            case R.id.offer_icon:

                member_icon.setImageResource(R.mipmap.member_friend);
                left_drawer_icon.setImageResource(R.mipmap.cat1);
                business_icon.setImageResource(R.mipmap.business);
                fun_icon.setImageResource(R.mipmap.fun);
                offer_icon.setImageResource(R.mipmap.offer_selected);
                right_drawer_icon.setImageResource(R.mipmap.cat2);
                frnd_request_icon.setImageResource(R.mipmap.member_friend);
                message_icon.setImageResource(R.mipmap.ene);
                notification_icon.setImageResource(R.mipmap.bell);
                opportunity_icon.setImageResource(R.mipmap.oppo);

                break;
            case R.id.right_drawer_icon:
                member_icon.setImageResource(R.mipmap.member_friend);
                left_drawer_icon.setImageResource(R.mipmap.cat1);
                business_icon.setImageResource(R.mipmap.business);
                fun_icon.setImageResource(R.mipmap.fun);
                offer_icon.setImageResource(R.mipmap.offer);
                right_drawer_icon.setImageResource(R.mipmap.cat2_selected);
                frnd_request_icon.setImageResource(R.mipmap.member_friend);
                message_icon.setImageResource(R.mipmap.ene);
                notification_icon.setImageResource(R.mipmap.bell);
                opportunity_icon.setImageResource(R.mipmap.oppo);

                break;
            case R.id.frnd_request_icon:
                selectItem(new FrndReuestListingFragment());

                member_icon.setImageResource(R.mipmap.member_friend);
                left_drawer_icon.setImageResource(R.mipmap.cat1);
                business_icon.setImageResource(R.mipmap.business);
                fun_icon.setImageResource(R.mipmap.fun);
                offer_icon.setImageResource(R.mipmap.offer);
                right_drawer_icon.setImageResource(R.mipmap.cat2);
                frnd_request_icon.setImageResource(R.mipmap.member_selected);
                message_icon.setImageResource(R.mipmap.ene);
                notification_icon.setImageResource(R.mipmap.bell);
                opportunity_icon.setImageResource(R.mipmap.oppo);

                break;
            case R.id.message_icon:
                member_icon.setImageResource(R.mipmap.member_friend);
                left_drawer_icon.setImageResource(R.mipmap.cat1);
                business_icon.setImageResource(R.mipmap.business);
                fun_icon.setImageResource(R.mipmap.fun);
                offer_icon.setImageResource(R.mipmap.offer);
                right_drawer_icon.setImageResource(R.mipmap.cat2);
                frnd_request_icon.setImageResource(R.mipmap.member_friend);
                message_icon.setImageResource(R.mipmap.ene_selected);
                notification_icon.setImageResource(R.mipmap.bell);
                opportunity_icon.setImageResource(R.mipmap.oppo);

                break;
            case R.id.plus_icon:

                break;
            case R.id.notification_icon:
                member_icon.setImageResource(R.mipmap.member_friend);
                left_drawer_icon.setImageResource(R.mipmap.cat1);
                business_icon.setImageResource(R.mipmap.business);
                fun_icon.setImageResource(R.mipmap.fun);
                offer_icon.setImageResource(R.mipmap.offer);
                right_drawer_icon.setImageResource(R.mipmap.cat2);
                frnd_request_icon.setImageResource(R.mipmap.member_friend);
                message_icon.setImageResource(R.mipmap.ene);
                notification_icon.setImageResource(R.mipmap.bell_selected);
                opportunity_icon.setImageResource(R.mipmap.oppo);

                break;
            case R.id.opportunity_icon:

                member_icon.setImageResource(R.mipmap.member_friend);
                left_drawer_icon.setImageResource(R.mipmap.cat1);
                business_icon.setImageResource(R.mipmap.business);
                fun_icon.setImageResource(R.mipmap.fun);
                offer_icon.setImageResource(R.mipmap.offer);
                right_drawer_icon.setImageResource(R.mipmap.cat2);
                frnd_request_icon.setImageResource(R.mipmap.member_friend);
                message_icon.setImageResource(R.mipmap.ene);
                notification_icon.setImageResource(R.mipmap.bell);
                opportunity_icon.setImageResource(R.mipmap.oppo_selected);

                break;
            case R.id.all_frnd_icon:


                break;
            case R.id.plus_layout:

                break;

        }
    }

    private void selectItem(Fragment frag) {


        if (frag != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, frag).commit();

        }
        else {
            Log.e("MainActivity", "Error in creating fragment");
        }

    }

    public class CustomAdapter extends BaseAdapter {
        public LayoutInflater inflater = null;
        ArrayList<BCategory> tr_array;
        Context              context;

        public CustomAdapter(Activity mainActivity, ArrayList<BCategory> tr_array) {
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
        public BCategory getItem(int position) {
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
            rowView = inflater.inflate(R.layout.category_list_item, null);
            holder.catg_list_tv = (TextView) rowView.findViewById(R.id.catg_list_tv);

            holder.catg_list_tv.setText(tr_array.get(position).getName());


            return rowView;
        }

        public class Holder {
            TextView catg_list_tv;

        }

    }

    class businessCategoryAsyncTask extends AsyncTask<Void, Boolean, Boolean> {
        CustomProgressDialog pdiDialog;
        String               message;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdiDialog = new CustomProgressDialog(MainActivity.this);
            pdiDialog.setMessage("Loading...");
            pdiDialog.setCancelable(false);
            pdiDialog.show();

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


                String url            = Web_url_api.categoriesUrlApi();
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
                        business_catg_list.add(new BCategory(data_obj.getString(Json_keys
                                                                                        .CATEGORYID), data_obj.getString(Json_keys.NAME)));

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

                adapter.notifyDataSetChanged();
            }
            else {
                Methods.animRedTextMethod(MainActivity.this, message);
            }

        }

    }

}
