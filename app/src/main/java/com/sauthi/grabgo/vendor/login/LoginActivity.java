package com.sauthi.grabgo.vendor.login;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.hbb20.CountryCodePicker;
import com.sauthi.grabgo.vendor.AppController;
import com.sauthi.grabgo.vendor.MainActivity;
import com.sauthi.grabgo.vendor.R;
import com.sauthi.grabgo.vendor.global.GlobalFunctions;
import com.sauthi.grabgo.vendor.global.GlobalVariables;
import com.sauthi.grabgo.vendor.services.ServerResponseInterface;
import com.sauthi.grabgo.vendor.services.ServicesMethodsManager;
import com.sauthi.grabgo.vendor.services.model.LoginModel;
import com.sauthi.grabgo.vendor.services.model.ProfileMainModel;
import com.sauthi.grabgo.vendor.services.model.ProfileModel;
import com.sauthi.grabgo.vendor.services.model.StatusMainModel;
import com.sauthi.grabgo.vendor.services.model.StatusModel;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    Context context;
    private static Activity activity;
    View mainView;

    static Toolbar toolbar;
    static ActionBar actionBar;
    static String mTitle;
    static int mResourceID;
    static int titleResourseID;
    static TextView toolbar_title;
    static ImageView toolbar_logo, tool_bar_back_icon;

    private EditText etv_email, password_etv;
    private Button btn_login;
    private TextView  tv_forgot_password;
    private CountryCodePicker country_code_picker;


    LoginModel loginModel = null;
    boolean isLoginBtnEnabled = false;
    String pageType = null;

    GlobalVariables globalVariables;
    GlobalFunctions globalFunctions;

    String selected_country_code = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        context = this;
        activity = this;


        globalFunctions = AppController.getInstance().getGlobalFunctions();
        globalVariables = AppController.getInstance().getGlobalVariables();


       /* toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        tool_bar_back_icon = (ImageView) toolbar.findViewById(R.id.toolbar_icon);
        tool_bar_back_icon.setVisibility(View.GONE);
       *//* tool_bar_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.ColorStatusBar));
        }

        btn_login=findViewById(R.id.btn_login);
        etv_email=findViewById(R.id.etv_email);
        password_etv=findViewById(R.id.password_etv);

        mainView = etv_email;


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateInput();
            }
        });
    }

    private void validateInput() {
        if (etv_email != null) {
            String
                    eMail = etv_email.getText().toString().trim(),
                    password = password_etv.getText().toString().trim();

            if  (eMail.isEmpty()) {
                etv_email.setError(getString(R.string.pleaseFillMandatoryDetails));
                etv_email.setFocusableInTouchMode(true);
                etv_email.requestFocus();
            } else if (!globalFunctions.isEmailValid(eMail)) {
                globalFunctions.displayMessaage(activity, mainView, getString(R.string.emailNotValid));
                etv_email.setFocusableInTouchMode(true);
                etv_email.requestFocus();

            } else if (password.isEmpty()) {
                password_etv.setError( getString( R.string.password_error ) );
                password_etv.setFocusableInTouchMode( true );
                password_etv.requestFocus();
            } else {
                LoginModel loginModel = new LoginModel();
                loginModel.setEmail_id( eMail );
                //loginModel.setCountryCode( selected_country_code );
                loginModel.setPassword( globalFunctions.md5(password ));

                loginUser( context, loginModel );

            }
        }
    }

    private void loginUser(Context context, LoginModel loginModel) {
        globalFunctions.showProgress( context, getString(R.string.loggin) );
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.loginUser( context, loginModel, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                globalFunctions.hideProgress();
                Log.d( TAG, "Response : " + arg0.toString() );
                validateOutput( arg0 );
            }

            @Override
            public void OnFailureFromServer(String msg) {
                globalFunctions.hideProgress();
                GlobalFunctions.displayMessaage( context, mainView, msg );
                Log.d( TAG, "Failure : " + msg );
            }

            @Override
            public void OnError(String msg) {
                globalFunctions.hideProgress();
                GlobalFunctions.displayMessaage( context, mainView, msg );
                Log.d( TAG, "Error : " + msg );
            }
        }, "Login_User" );
    }

    private void validateOutput(Object arg0) {
        if (arg0 instanceof StatusMainModel) {
            StatusMainModel statusMainModel = (StatusMainModel) arg0;
            StatusModel statusModel = statusMainModel.getStatusModel();
            if (statusMainModel.isStatus()) {

                GlobalFunctions.setSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_TOKEN, statusModel.getToken());
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
                //get profile api
                getProfile();
            } else {
                GlobalFunctions.displayErrorDialog(context, statusModel.getMessage());
            }
        }
    }

    private void getProfile() {
        //GlobalFunctions.showProgress(context, getString(R.string.loading_profile) );
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.getProfile(context,new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                //GlobalFunctions.hideProgress();
                Log.d( TAG, "Response : " + arg0.toString() );
                //ProfileDetails(arg0);
                validateProfileOutput(arg0);

            }

            @Override
            public void OnFailureFromServer(String msg) {
                //GlobalFunctions.hideProgress();
                GlobalFunctions.displayMessaage(LoginActivity.this.context, mainView, msg );
                Log.d( TAG, "Failure : " + msg );
            }

            @Override
            public void OnError(String msg) {
                //GlobalFunctions.hideProgress();
                GlobalFunctions.displayMessaage(LoginActivity.this.context, mainView, msg );
                Log.d( TAG, "Error : " + msg );
            }
        }, "Profile" );
    }

    private void validateProfileOutput(Object arg0) {
        if (arg0 instanceof ProfileMainModel){

            GlobalFunctions.closeAllActivities();
            ProfileMainModel profileMainModel = ( ProfileMainModel ) arg0;
            ProfileModel profileModel = profileMainModel.getProfileModel();
            GlobalFunctions.setProfile( context, profileModel );
            Intent intent = new Intent( context, MainActivity.class );
            startActivity( intent );
        }

    }


    @Override
    public void onStop() {
        super.onStop();
    }

    public static void setTitle(String title, int titleImageID, int backgroundResourceID) {
        mTitle = title;
        if (backgroundResourceID != 0) {
            mResourceID = backgroundResourceID;
        } else {
            mResourceID = 0;
        }
        if (titleImageID != 0) {
            titleResourseID = titleImageID;
        } else {
            titleResourseID = 0;
        }
        restoreToolbar();
    }

    @SuppressLint("LongLogTag")
    private static void restoreToolbar() {
        //toolbar = (Toolbar) findViewById(R.id.tool_bar);
        Log.d(TAG, "Restore Tool Bar");
        if (actionBar != null) {
            Log.d(TAG, "Restore Action Bar not Null");
            Log.d(TAG, "Title : " + mTitle);
            toolbar_title.setText(mTitle);
            if (mResourceID != 0) toolbar.setBackgroundResource(mResourceID);
            //actionBar.setTitle("");
            // actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void onBackPressed() {
        closeThisActivity();
        super.onBackPressed();
    }

    public static void closeThisActivity() {
        if (activity != null) {
            activity.finish();
            //activity.overridePendingTransition(R.anim.zoom_in,R.anim.zoom_out);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getFragmentManager().findFragmentByTag(TAG) != null)
            getFragmentManager().findFragmentByTag(TAG).setRetainInstance(true);
    }

    @Override
    public void onStart() {

       /* if(hint != null) {
            hint.launchAutomaticHintForCall(activity.findViewById(R.id.action_call));
        }*/
//       globalFunctions.launchAutomaticHintForSearch(mainView, getString(R.string.search_title),  getString(R.string.search_description));
        super.onStart();
    }

    @Override
    public void onDestroy() {
        if (activity != null) activity = null;
        super.onDestroy();
    }

}
